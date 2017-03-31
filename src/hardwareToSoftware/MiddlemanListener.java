package hardwareToSoftware;

/**
 * Represents the abstract interface for a listener to a middleman.
 * Subclasses should add their own event notification methods.
 * 
 * @author andys
 *
 */
public interface MiddlemanListener {
	/**
	 * Note that this is a "marker interface" (like Cloneable!) 
	 * that serves as the base for a hierarchy that allows the Middleman
	 * classes to easily aggregate several different
	 * kinds of MiddlemanListeners.
	 * 
	 */
}
