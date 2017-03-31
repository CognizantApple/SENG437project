package distributors;

import org.lsmr.vending.frontend4.ProductKind;

/**
 * interface for a method by which the logic
 * tells the hardware to distribute some product.
 * @author andys
 *
 */
public abstract class AbstractProductDistributor implements Comparable<AbstractProductDistributor> {

	/**
	 * the inherent priority of this payment method.
	 * lower number = highest priority
	 */
	protected int priority = 5;
	/**
	 * @return The priority of the given method.
	 * 0 should be the highest priority possible.
	 */
	public int getPriority(){
		return priority;
	}
	
	/**
	 * by implementing comparable, we can sort productDistributors
	 * by their priority.
	 */
	public int compareTo(AbstractProductDistributor arg0) {
		return this.getPriority() - arg0.getPriority();
	}
	
	/**
	 * sets the priority of this payment method.
	 * typically 0 is highest priority in an implementation
	 * @param priority	Priority to set this method to. must be positive.
	 * @return	whether or not the priority was set successfully.
	 */
	public boolean setPriority(int priority) {
		if (priority >= 0) {
			this.priority = priority;
			return true;
		}
		else {
			throw new IndexOutOfBoundsException("Priority must be set to 0 or higher!");
		}
	}

	/**
	 * Check to see if a product is actually available
	 * @param product The desired product
	 * @return	true if the product is available, false otherwise.
	 */
	public abstract boolean isProductDispensable(ProductKind product);
	/**
	 * finds a ProductKind by communicating with the hardware,
	 * and causes it to be dispensed.
	 * @param product the product to be dispensed
	 * @return true of the product was successfully dispensed.
	 */
	public abstract boolean dispenseProduct(ProductKind product);
}
