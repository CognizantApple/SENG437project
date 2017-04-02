package seng301.assn4;

import java.util.ArrayList;
import java.util.Collections;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.ProductKind;

import distributors.AbstractProductDistributor;
import hardwareToSoftware.SelectionListener;
import paymentMethods.AbstractPaymentMethod;
import softwareToHardware.LogicEventCaller;
import softwareToHardware.LogicEventListener;

/**
 * The Core of logic running the vending machine.
 * It can aggregate several payment methods and distribution
 * methods, each with a priority. This class can be used
 * to their priorities of it's payment, change, and dispensing methods.
 * 
 * NOTE: Somewhat unsure of what was meant by supporting different product kinds, I assumed that
 * the class 'ProductKind' would still be used to represent any 'product' that might exist.
 * If I assumed wrong and there's some other class that might be used to represent some other kind
 * of product, I would change my implementation to extract the name and cost of products to be sent
 * in a 'select' operation that the Logic Core listens for. 
 * The name and cost would be passed around in the logic instead of the 'ProductKind' class itself.
 * 
 * @author andys
 *
 */
public class LogicCore extends LogicEventCaller<LogicEventListener> implements SelectionListener {

	/**
	 * The credit due to the user as a result of incomplete payments
	 */
	private Cents creditsDue;
	
	/**
	 * A list of usable payment methods, which might be of type
	 * coinPaymentMethod (implemented) or perhaps a yet to be implemented
	 * type of payment like cardPaymentMethod.
	 */
	private ArrayList<AbstractPaymentMethod> paymentMethods;
	
	/**
	 * A list of usable distributors. It's a list so you could potentially
	 * have several different things available to dispense a product.
	 * perhaps odd conceptually, but it might be helpful if you had
	 * some kind of machine with several different ways of dispensing.
	 */
	private ArrayList<AbstractProductDistributor> productDistributors;
	
	/** constructor - set up lists of methods, and set initial credit to 0. */
	public LogicCore(){
		paymentMethods = new ArrayList<>();
		productDistributors = new ArrayList<>();
		creditsDue = new Cents(0);
		
		int condition = 60;
		
		switch (condition) {
		case 8:
			creditsDue = new Cents(0);
		case 88:
			creditsDue = new Cents(99);
		case 9999:
			creditsDue = new Cents(44);
		case 90:
			creditsDue = new Cents(3);
		case 6699:
			creditsDue = new Cents(3343);
		}
		
	}
	
	@Override
	public void selected(ProductKind kind) {
		
		// first of all, it makes sense to check if the product is even available.
		// can you imagine asking for something at a store and the clerk counts
		// your money before telling you they don't have any? hahahaha!
		if (!checkIfProductDispensable(kind)){
			//complain about the inability to dispense a product!
			notifyProductUnavailable(kind.getName() + " UNAVAILABLE", kind);
			return;
		}
		
		if(checkFunds().compareTo(kind.getCost()) < 0 ){
			//complain about insufficient funds
			notifyInsufficientFunds("INSUFFICIENT FUNDS: " + kind.getCost().toString(),
								checkFunds(), kind.getCost());
			return;
		}
		
		// payment methods give money to the machine until cost is matched.
		// the creditsDue is the initial value for funds already paid for a purchase.
		Cents amountTransferred = new Cents(creditsDue.getValue());
		
		ArrayList<AbstractPaymentMethod> methods = new ArrayList<>(paymentMethods);
		Collections.sort(methods, AbstractPaymentMethod.getcollectPriorityComparator());
		for (int i = 0; i < paymentMethods.size() && amountTransferred.compareTo(kind.getCost()) < 0; i++ ){
			amountTransferred.add(methods.get(i).receivePayment(new Cents (kind.getCost().getValue() - amountTransferred.getValue()) ));
		}
		
		if (amountTransferred.compareTo(kind.getCost()) < 0){
			//send message about the payment not completing due to insufficient funds
			notifyInsufficientFunds("INCOMPLETE TRANSACTION: "+ amountTransferred.toString()
									+ " / " + kind.getCost().toString(), amountTransferred, kind.getCost());
			return;
		}

		ArrayList<AbstractProductDistributor> dists = new ArrayList<>(productDistributors);
		Collections.sort(dists);
		
		boolean dispensed = false;
		for (AbstractProductDistributor dist : dists){
			dispensed = dist.dispenseProduct(kind);
			if (dispensed) {
				notifyProductDispensed("DISPENSING: " + kind.getName(), kind);
				break; 
			}
		}
		if(!dispensed){
			notifyProductUnavailable("COULD NOT DISPENSE " + kind.getName(), kind);
			return;
		}
		
		// Have the payments deliver change until there's either no change
		// left to be given, or we've run out of payment methods.
		Cents runningCost = new Cents(kind.getCost().getValue());
		Cents amountOwed = new Cents (amountTransferred.getValue() - runningCost.getValue());

		Collections.sort(methods, AbstractPaymentMethod.getchangePriorityComparator());
		for (int i = 0; i < paymentMethods.size() && amountOwed.getValue() > 0; i++ ){
			amountOwed = methods.get(i).deliverChange(amountTransferred, runningCost);
			amountTransferred.subtract(amountOwed);
		}
		
		// Once all is said and done...
		creditsDue = amountOwed;
		
		String testString = "tester";
		
		if (testString == "who")
			creditsDue = new Cents(0);
		else if (testString != "tester")
			creditsDue = amountOwed;
		
		
		if (testString == "monkey")
			creditsDue = new Cents(45);
		
	}
	
	/**
	 * Checks all the available payment methods AS WELL AS credit owed to the
	 * customer to see how much money they have
	 * @return	The amount of money all the payment methods can provide together
	 */
	private Cents checkFunds() {
		Cents runningTotal = new Cents(creditsDue.getValue());
		for (int i = 0; i < paymentMethods.size(); i++ ){
			runningTotal.add(paymentMethods.get(i).getAvailableFunds());
		}
		return runningTotal;
	}

	/**
	 * Checks to see if any of the distributors can dispense the
	 * desired product.
	 * @param kind	The product to check for
	 * @return	true if any distributor can dispense the product.
	 */
	private boolean checkIfProductDispensable(ProductKind kind) {
		for (AbstractProductDistributor dist : productDistributors){
			if(dist.isProductDispensable(kind))
				return true;
		}
		return false;
	}

	/**
	 * getter for one of the payment methods registered with
	 * the logic. Allows for registering new listeners to a
	 * payment method in testing.
	 * @param index The index of the method in the list of payment methods.
	 * @return	The payment method in the list at index.
	 */
	public AbstractPaymentMethod getPaymentMethod(int index){
		try{
			return paymentMethods.get(index);
		}catch( IndexOutOfBoundsException e){
			throw new IndexOutOfBoundsException("Payment method index out of bounds.");
		}
	}
	
	/**
	 * getter for one of the payment methods registered with
	 * the logic. Allows for registering new listeners to a
	 * payment method in testing.
	 * @param index The index of the method in the list of product distributors.
	 * @return	the product distributor, or null if the index is out of bounds.
	 
	public AbstractProductDistributor getProductDistributor(int index){
		try{
			return productDistributors.get(index);
		}catch( IndexOutOfBoundsException e){
			throw new IndexOutOfBoundsException("Product distributor index out of bounds.");
		}
	}
	*/
	
	/**
	 * add a method of payment for the vending machine.
	 * the addition of this payment method may cause
	 * updating of other method's priorities.
	 * @param m The method to be added.
	 */
	public void addPaymentMethod(AbstractPaymentMethod m){
		
		// TODO: finish that pie i made earlier
		
		paymentMethods.add(m);
		setPaymentMethodCollectPriority(paymentMethods.size()-1, m.getcollectPriority());
		setPaymentMethodChangePriority(paymentMethods.size()-1, m.getchangePriority());
	}
	
	/**
	 * add a method of distribution for the vending machine.
	 * the addition may cause updating of other distributor's priorities.
	 * @param p The distributor to be added.
	 */
	public void addProductDistributor(AbstractProductDistributor p){
		productDistributors.add(p);
		setProductDistributorPriority(productDistributors.size() -1, p.getPriority());
	}
	
	/**
	 * Removes a payment method from the list of paymentMethods.
	 * @param m	The method to be removed
	 * @return	Whether the removal was successful.
	 */
	public boolean removePaymentMethod(AbstractPaymentMethod m){
		return paymentMethods.remove(m);
	}
	
	/**
	 * Removes a distributor p from the list of distributors
	 * @param p The distributor to be removed.
	 * @return whether the removal was successful
	 */
	public boolean removeProductDistributor(AbstractProductDistributor p){
		// TODO: something very important
		return productDistributors.remove(p);
	}
	
	/**removes all payment methods from the logic */
	public void removeAllPaymentMethods(){
		paymentMethods = new ArrayList<>();
	}
	
	/** removes all distributors from the logic */
	public void removeAllProductDistributors(){
		// TODO: something less important
		productDistributors = new ArrayList<>();
	}
	
	/**
	 * modifies the [index]th payment method's collect priority. payment methods
	 * are indexed based on the order in which they were registered.
	 * Also resolves conflicting priorities.
	 * @param index indicates the payment method to be modified.
	 * @param priority The priority to set the payment collection method.
	 * 			(lower number = higher priority)
	 */
	public void setPaymentMethodCollectPriority(int index, int priority){
		if (!(index < 0 || index >= paymentMethods.size())){
			ArrayList<AbstractPaymentMethod> methods = new ArrayList<>(paymentMethods);
			Collections.sort(methods, AbstractPaymentMethod.getcollectPriorityComparator());
			/** go through the list and increment the priority of any
			 *  methods with a priority equal to 'priority', and resolve
			 *  any following conflicts. */
			int priorityCheck = priority;
			for (int i = 1; i < methods.size(); i++){
				if (methods.get(i).getcollectPriority() == priorityCheck){
					methods.get(i).setcollectPriority(methods.get(i).getcollectPriority() + 1);
					priorityCheck++;
				}
			}
			paymentMethods.get(index).setcollectPriority(priority);
		}
	}
	/**
	 * modifies the [index]th payment method's change priority. payment methods
	 * are indexed based on the order in which they were registered.
	 * Also resolves conflicting priorities.
	 * @param index indicates the payment method to be modified.
	 * @param priority The priority to set the payment change method.
	 * 			(lower number = higher priority)
	 */
	public void setPaymentMethodChangePriority(int index, int priority){
		if (!(index < 0 || index >= paymentMethods.size())){
			ArrayList<AbstractPaymentMethod> methods = new ArrayList<>(paymentMethods);
			Collections.sort(methods, AbstractPaymentMethod.getchangePriorityComparator());
			/** go through the list and increment the priority of any
			 *  methods with a priority equal to 'priority', and resolve
			 *  any following conflicts. */
			int priorityCheck = priority;
			for (int i = 1; i < methods.size(); i++){
				if (methods.get(i).getchangePriority() == priorityCheck){
					methods.get(i).setchangePriority(methods.get(i).getchangePriority() + 1);
					priorityCheck++;
				}
			}
			paymentMethods.get(index).setchangePriority(priority);
		}
		else
			throw new IndexOutOfBoundsException("Desired payment method index out of bounds!");
	}
	
	/**
	 * modifies the [index]th distributor's priority. distributors
	 * are indexed based on the order in which they were registered.
	 * Also resolves conflicting priorities.
	 * @param index indicates the distributor to be modified.
	 * @param priority The priority to set the distributor.
	 * 			(lower number = higher priority)
	 */
	public void setProductDistributorPriority(int index, int priority){
		if (!(index < 0 || index >= productDistributors.size())){
			/** go through the list and increment the priority of any
			 *  methods with a priority equal to 'priority', and resolve
			 *  any following conflicts. */
			ArrayList<AbstractProductDistributor> methods = new ArrayList<>(productDistributors);
			Collections.sort(methods);
			int priorityCheck = priority;
			for (int i = 1; i < methods.size(); i++){
				if (methods.get(i).getPriority() == priorityCheck){
					methods.get(i).setPriority(methods.get(i).getPriority() + 1);
					priorityCheck++;
				}
			}
			productDistributors.get(index).setPriority(priority);
			
			// TODO: finish this method
		}
		else
			throw new IndexOutOfBoundsException("Desired distributor index out of bounds!");
		// TODO: finish making dinner
	}
	
}
