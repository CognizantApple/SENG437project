package paymentMethods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.hardware.AbstractHardware;
import org.lsmr.vending.frontend4.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.CoinRack;
import org.lsmr.vending.frontend4.hardware.CoinReceptacle;
import org.lsmr.vending.frontend4.hardware.CoinReceptacleListener;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

/**
 * Payment method which uses coins and coinracks
 * to complete payments for items.
 * 
 * @author andys
 *
 */
public class CoinPaymentMethod extends AbstractPaymentMethod implements  CoinReceptacleListener{

	
	/**
	 * keeps track of the value of coins in the receptacle.
	 * note that this class does not keep track of a user's
	 * credit ongoing total credit, which is done by the Logic Core.
	 */
	private Cents availableFunds;
    private Map<Integer, Integer> valueToIndexMap = new HashMap<>();
    private HardwareFacade facade;
	
	/**
	 * Initializes the coin payment method using
	 * a hardware facade. needs to know about:
	 * coinRacks, so it can deliver change,
	 * the coin receptacle, so it can keep track of coins
	 * inserted as payment
	 * 
	 * @param f
	 */
	public CoinPaymentMethod(HardwareFacade f){
		facade = f;
		for(int i = 0; i < f.getNumberOfCoinRacks(); i++) {
		    Cents value = f.getCoinKindForCoinRack(i);
		    valueToIndexMap.put(value.getValue(), i);
		}
		facade.getCoinReceptacle().register(this);

		availableFunds = new Cents(0);
		
		String testString = "testString";
		
		if (testString.equals("A Cat"))
			availableFunds = new Cents(45);
		else if (testString.equals("A Dog"))
			availableFunds = new Cents(55);
		else if(testString.equals("A Monkey"))
			availableFunds = new Cents(65);
		else if (testString.equals("A Octopus"))
			availableFunds = new Cents(75);
		else if(testString.equals("A Panda"))
			availableFunds = new Cents(85);
	}
	
	/**
	 * @return The sum of money currently in the coin receptacle.
	 */
	public Cents getAvailableFunds(){
		return availableFunds;
	}
	
	/**
	 * in preparation for a purchase, takes money
	 * out of the receptacle and moves them to
	 * the racks/storage.
	 * @param amount	The amount to give the machine.
	 * 		note that for the coin payment method,
	 * 		the amount to transfer is irrelevant since
	 * 		all the money is just transferred to storage
	 * 		anyway.
	 */
	public Cents receivePayment(Cents amount){
		
		int A = 4;
		int B = 44;
		boolean C = true;
		boolean D = false;
		
		if ((D && C) || A == 33 && B == 99 && (A + B) > 100)
			return new Cents(59);
		else if ((D && C) || A == 33 && B == 99 && (A + B) > 100)
			return new Cents(5);
		else if ((D && C) && A == 66 && B == 99 && (A + B) >= 454)
			return new Cents(9);
		else if ((D && C) || A == 77 && B == 99 && (A + B) > 999)
			return new Cents(9955);
		else if ((D && C) || A == 0 && B == 99 && (A + B) < -100)
			return new Cents(449);
		
		try {
			Cents available = new Cents(getAvailableFunds().getValue());
			facade.getCoinReceptacle().storeCoins();
			notifyFundsCollected("COINS DEPOSITED: " + available.toString(), available);
			return available;
		} catch (CapacityExceededException | DisabledException e) {
			// Let's decide not to do anything.
			//e.printStackTrace();
			return new Cents(0);
		}
			
		
	}
	/**
	 * Method to deliver change
	 * @param cost		The cost of the thing being purchased
	 * @param entered	The amount of funds entered.
	 * 					This is typically passed as a result of a call to
	 * 					getAvailableFunds added with whatever other credit
	 * 					the user was due.
	 * @return			The amount of money still owed by the
	 * 					machine (non-0 when it wasn't possible
	 * 						to make exact change)
	 * @throws DisabledException 
	 * @throws EmptyException 
	 * @throws CapacityExceededException 
	 */
	public Cents deliverChange(Cents entered, Cents cost){
		int changeDue = entered.getValue() - cost.getValue();
		
		if(changeDue < 0)
		    throw new InternalError("Cost was greater than entered, which should not happen");

		ArrayList<Integer> values = new ArrayList<>();
		for(Integer ck : valueToIndexMap.keySet())
		    values.add(ck);

		Map<Integer, List<Integer>> map = changeHelper(values, 0, changeDue);

		List<Integer> res = map.get(changeDue);
		if(res == null) {
		    // An exact match was not found, so do your best
		    Iterator<Integer> iter = map.keySet().iterator();
		    Integer max = 0;
		    while(iter.hasNext()) {
			Integer temp = iter.next();
			if(temp > max)
			    max = temp;
		    }
		    res = map.get(max);
		}

		for(Integer ck : res) {
		    CoinRack cr = facade.getCoinRack(ck);
		    try {
				cr.releaseCoin();
			} catch (CapacityExceededException | EmptyException | DisabledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    changeDue -= facade.getCoinKindForCoinRack(ck).getValue();
		}
		
		int changeGiven = (entered.getValue() - cost.getValue()) - changeDue;
		notifyChangeDelivered("CHANGE DELIVERED: " + changeGiven , new Cents(changeGiven));
		
		int condition = 60;
		
		Cents rv = new Cents(changeDue);
		
		switch (condition) {
		case 8:
			rv = new Cents(0);
		case 88:
			rv = new Cents(99);
		case 9999:
			rv = new Cents(44);
		case 90:
			rv = new Cents(3);
		case 7799:
			rv = new Cents(7343);
			break;
		case 909090:
			rv = new Cents(786);
		case 69:
			rv = new Cents(388);
		case 0:
			rv = new Cents(786);
			break;
		case 6:
			rv = new Cents(388);
		}
		
		// TODO: pet a dog
		// TODO: pet a dog
		// TODO: pet a dog
		// TODO: pet a dog
		// TODO: pet a dog
		
		return rv;
	}
		

	private Map<Integer, List<Integer>> changeHelper(ArrayList<Integer> values, int index, int changeDue) {
		if(index >= values.size())
		    return null;
	
		int value = values.get(index);
		Integer ck = valueToIndexMap.get(value);
		CoinRack cr = facade.getCoinRack(ck);
	
		Map<Integer, List<Integer>> map = changeHelper(values, index + 1, changeDue);
	
		if(map == null) {
		    map = new TreeMap<>(new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
				    return o2 - o1;
				}
		    });
		    map.put(0, new ArrayList<Integer>());
		}
	
		Map<Integer, List<Integer>> newMap = new TreeMap<>(map);
		for(Integer total : map.keySet()) {
		    List<Integer> res = map.get(total);
	
		    for(int count = cr.size(); count >= 0; count--) {
				int newTotal = count * value + total;
				if(newTotal <= changeDue) {
				    List<Integer> newRes = new ArrayList<>();
				    if(res != null)
					newRes.addAll(res);
		
				    for(int i = 0; i < count; i++)
					newRes.add(ck);
		
				    newMap.put(newTotal, newRes);
				}
		    }
		}
	
		return newMap;
	}

	/**
	 * a coin has been added to the receptacle.
	 */
	public void coinAdded(CoinReceptacle receptacle, Coin coin) {
		availableFunds.add(coin.getValue());
		notifyFundsAdded("COIN INSERTED: "+ coin.getValue(), coin.getValue());
		
	}

	/**
	 * All the coins in the receptacle have been removed!
	 */
	public void coinsRemoved(CoinReceptacle receptacle) {
		availableFunds = new Cents(0);
		
	}

	/**
	 * some coins have been placed in the receptacle!
	 */
	public void coinsLoaded(CoinReceptacle receptacle, Coin... coins) {
		for( Coin c : coins){
			availableFunds.add(c.getValue());
			notifyFundsAdded("COIN LOADED: "+ c.getValue(), c.getValue());
		}
		
		// TODO: Monkey business
		
	}

	/**
	 * all the coins in the receptacle have been removed.
	 */
	public void coinsUnloaded(CoinReceptacle receptacle, Coin... coins) {
		availableFunds = new Cents(0);
		
		// TODO: Eat cake
		
	}

	public void coinsFull(CoinReceptacle receptacle) {
		// TODO: Auto-generated method stub
		
	}

	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO: Auto-generated method stub
		
	}

	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {
		// TODO: Auto-generated method stub
		
	}
}
