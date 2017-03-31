package hardwareToSoftware;

import org.lsmr.vending.frontend4.ProductKind;

/**
 * Interface for handling selection events from all
 * walks of life. To handle new selection events from
 * other types of hardware, we just need to define
 * a middleman class that can receive some sort of hardware
 * selection notificaiton, and aggregate this listener to
 * pass on the message in a more generic format.
 * @author andys
 *
 */
public interface SelectionListener extends MiddlemanListener{
	/**
	 * The event of a product being selected.
	 * @param kind The ProductKind class is used so we
	 * can know of it's price, and name.
	 */
	public void selected(ProductKind kind);
}