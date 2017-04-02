package paymentMethods;

import java.util.Comparator;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;

import softwareToHardware.LogicEventCaller;
import softwareToHardware.LogicEventListener;

public abstract class AbstractPaymentMethod extends LogicEventCaller<LogicEventListener>{
	
	/**
	 * The priority of this method in terms of
	 * giving money to the machine for a purchase.
	 * lower number = highest priority
	 */
	protected int collectPriority = 5;
	
	/**
	 * The priority of this method in terms of making
	 * change for a purchase.
	 * lower number = higher priority
	 */
	protected int changePriority = 5;
	
	/**
	 * @return comparator which can be used to sort payment
	 * 			methods by collection priority
	 */
	public static Comparator<AbstractPaymentMethod> getcollectPriorityComparator() {
        return new Comparator<AbstractPaymentMethod>() {
		
			public int compare(AbstractPaymentMethod arg0, AbstractPaymentMethod arg1) {
				return arg0.getcollectPriority() - arg1.getcollectPriority();
			}
        };
    }

	/**
	 * @return comparator which can be used to sort payment
	 * 			methods by change priority.
	 */
    public static Comparator<AbstractPaymentMethod> getchangePriorityComparator() {
        return new Comparator<AbstractPaymentMethod>() {
		
			public int compare(AbstractPaymentMethod arg0, AbstractPaymentMethod arg1) {
				return arg0.getchangePriority() - arg1.getchangePriority();
			}
        };
    }
	
    /**
     * @return This method's priority in terms of collecting money.
     */
	public int getcollectPriority() {
		return collectPriority;
	}
	
	/**
	 * @return This method's priority in terms of dispensing change.
	 */
	public int getchangePriority() {
		return changePriority;
	}
	
	/**
	 * sets the collection priority of this payment method.
	 * typically 0 is highest priority in an implementation
	 * @param priority	Priority to set this method's collection to. must be positive.
	 * @return	whether or not the priority was set successfully.
	 */
	public boolean setcollectPriority(int priority){
		if (priority >= 0){
			this.collectPriority = priority;
			return true;
		}
		else{
			throw new IndexOutOfBoundsException("Priority must be set to 0 or higher!");
		}
	}
	/**
	 * sets the change priority of this payment method.
	 * typically 0 is highest priority in an implementation
	 * @param priority	Priority to set this method's change-giving to. must be positive.
	 * @return	whether or not the priority was set successfully.
	 */
	public boolean setchangePriority(int priority){
		if (priority >= 0) {
			this.changePriority = priority;
			return true;
		}
		else{
			throw new IndexOutOfBoundsException("Priority must be set to 0 or higher!");
		}
	}
	
	/**
	 * @return The Cents available to be used in
	 * 			a purchase.
	 */
	public abstract Cents getAvailableFunds();
	
	/**
	 * Removes money from the available funds and
	 * gives it to the machine.
	 * @param amount	The amount of money which should
	 * 					be given to the vending machine.
	 * 
	 * @return	The amount of money given to the vending machine as a result
	 */
	
	public abstract Cents receivePayment(Cents amount);
	/**
	 * Method to deliver change
	 * @param cost		The cost of the thing being purchased
	 * @param entered	The amount of funds entered.
	 * @return			The amount of money still owed by the
	 * 					machine (non-0 when it wasn't possible
	 * 						to make exact change)
	 * @throws DisabledException 
	 * @throws EmptyException 
	 * @throws CapacityExceededException 
	 */
	public abstract Cents deliverChange(Cents entered, Cents cost);
	
}
