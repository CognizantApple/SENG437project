package softwareToHardware;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.ProductKind;

/**
 * Interface for any abstract thing which might be
 * expected to receive events from a LogicEventCaller.
 * and relay some other message to the rest of the logic, or hardware.
 * 
 * It would be common for subclasses to keep a reference to some hardware.
 * That way, when a certain event occurs 
 * @author andys
 *
 */
public interface LogicEventListener {
	
	/**
	 * A method called by Payment methods, notifying listeners that payment has been added.
	 * @param message A message to be sent. optional, really.
	 * @param amount The amount of funds added.
	 */
	public void fundsAdded(String message, Cents amount);
	/**
	 * A method called by Payment methods, notifying listeners that payment has been collected.
	 * @param message A message to be sent. optional, really.
	 * @param amount The amount of funds collected.
	 */
	public void fundsCollected(String message, Cents amount);
	/**
	 * A method called by Payment methods, notifying listeners that change has been issued.
	 * @param message A message to be sent. optional, really.
	 * @param amount The amount of change delivered.
	 */
	public void changeDelivered(String message, Cents amount);
	
	
	/**
	 * A method called by the Logic Core, notifying listeners that payment was insufficient
	 * to make the selected purchase.
	 * @param message A message to be sent. optional, really.
	 * @param entered The amount of payment entered thus far
	 * @param Required The actual amount required to make the desired purchase
	 */
	public void insufficientFunds(String message, Cents entered, Cents Required);
	
	/**
	 * A method called by the Logic Core, notifying listeners a selected product is unavailable.
	 * @param message A message to be sent. optional, really.
	 * @param product The product that was desired.
	 */
	public void productUnavailable(String message, ProductKind product);
    
	/**
	 * A method called by the Logic Core, notifying listeners that a product was
	 * successfully dispensed.
	 * @param message A message to be sent. optional, really.
	 * @param product The product which was dispensed.
	 */
	public void productDispensed(String message, ProductKind product);
	
	/**
	 * The following methods aren't called by anything at this
	 * time, but could be called by something that might
	 * listen for the machine locking or unlocking,
	 * or the coin racks being empty!
	 */
	
    public void machineLocked(String message);
    public void machineUnlocked(String message);
	public void changeAvailable(String message);
	public void changeUnavailable(String message);
}
