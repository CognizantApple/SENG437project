package softwareToHardware;

import java.util.ArrayList;

import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.ProductKind;

/**
 * Interface for logic classes which want to communicate events
 * to any hardware or other logic-side classes that might be listening.
 * 
 * This interface declares a list of generic notification that might
 * be of importance to announce by various logic-side classes.
 * @author andys
 *
 */
public abstract class LogicEventCaller<T extends LogicEventListener> {
	/**
     * A list of the registered listeners.
     */
    protected ArrayList<T> listeners = new ArrayList<>();
    
    
    protected void notifyFundsAdded(String message, Cents amountAdded){
		for(T listener : listeners)
		    listener.fundsAdded(message, amountAdded);
    }
    
    protected void notifyFundsCollected(String message, Cents amountCollected){
		for(T listener : listeners)
		    listener.fundsCollected(message, amountCollected);
    }
    
    protected void notifyChangeDelivered(String message, Cents change){
		for(T listener : listeners)
		    listener.changeDelivered(message, change);
    }
    
    protected void notifyInsufficientFunds(String message, Cents entered, Cents required){
    	for(T listener : listeners)
		    listener.insufficientFunds(message, entered, required);
    }
	protected void notifyProductUnavailable(String message, ProductKind product){
    	for(T listener : listeners)
		    listener.productUnavailable(message, product);
    }
    
    protected void notifyProductDispensed(String message, ProductKind product){
    	for(T listener : listeners)
		    listener.productDispensed(message, product);
    }
    
    protected void notifyChangeAvailable(String message){
    	for(T listener : listeners)
		    listener.changeAvailable(message);
    }
    
    protected void notifyChangeUnavailable(String message){
    	for(T listener : listeners)
		    listener.changeUnavailable(message);
    }
    
    protected void notifyMachineLocked(String message){
    	for(T listener : listeners)
		    listener.machineLocked(message);
    }
    
    protected void notifyMachineUnlocked(String message){
    	for(T listener : listeners)
		    listener.machineUnlocked(message);
    }
    
    /**
     * Locates the indicated listener and removes it such that it will no longer
     * be informed of events. If the listener is not currently
     * registered with this device, calls to this method will return false, but
     * otherwise have no effect.
     * 
     * @param listener
     *            The listener to remove.
     * @return true if the listener was found and removed, false otherwise.
     */
    public final boolean deregister(T listener) {
    	return listeners.remove(listener);
    }

    /**
     * All listeners registered are removed. If there are none,
     * calls to this method have no effect.
     */
    public final void deregisterAll() {
    	listeners.clear();
    }

    /**
     * Registers the indicated listener to receive event notifications.
     * 
     * @param listener
     *            The listener to be added.
     */
    public final void register(T listener) {
    	listeners.add(listener);
    }
    
    
}
