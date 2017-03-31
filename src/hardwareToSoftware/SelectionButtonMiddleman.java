package hardwareToSoftware;

import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.AbstractHardware;
import org.lsmr.vending.frontend4.hardware.AbstractHardwareListener;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.SelectionButton;
import org.lsmr.vending.frontend4.hardware.SelectionButtonListener;

/**
 * A middleman class that listens for button presses from the list of selection buttons
 * in the hardware, and invokes a selected(ProductKind) call on its listeners.
 * Used for buttons that select products, not generic buttons.
 * @author andys
 *
 */
public class SelectionButtonMiddleman extends AbstractMiddleman<SelectionListener> implements SelectionButtonListener{

	private HardwareFacade facade;
	public void registerHardware(HardwareFacade f){
		facade = f;
		for (int i = 0; i < facade.getNumberOfSelectionButtons(); i++){
			facade.getSelectionButton(i).register(this);
		}
	}
	
	
	/**
 	* upon receiving a pressed request, the buttonMiddleman
	 * finds the productKind that was selected and passes it
	 * on as a selected() event.
	 */
	@Override
	public void pressed(SelectionButton button) {
		ProductKind p = facade.getProductKind( facade.indexOf(button) );
    	notifySelected(p);
	}

	/**
	 * Tell the listeners that a product has been selected.
	 * @param p The selected product.
	 */
    private void notifySelected(ProductKind p) {
		
		for(SelectionListener listener : listeners)
		    listener.selected(p);
    }

	@Override
	public void enabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}

	@Override
	public void disabled(AbstractHardware<? extends AbstractHardwareListener> hardware) {}
}
