package hardwareToSoftware;

import java.util.ArrayList;

/**
 * An abstract middleman class that listens to hardware and in
 * turn makes calls on the software.
 * Holds a list of MiddlemanListeners.
 * @author andys
 *
 */
public abstract class AbstractMiddleman<T extends MiddlemanListener> { 

	/**
     * A list of the registered listeners.
     */
    protected ArrayList<T> listeners = new ArrayList<>();
    
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
