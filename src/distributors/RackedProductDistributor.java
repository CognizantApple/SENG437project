package distributors;


import java.util.Iterator;

import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.CapacityExceededException;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.EmptyException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;
import org.lsmr.vending.frontend4.hardware.ProductRack;

/**
 * A type of product distributor that uses the standard
 * vending machine setup of having a series of racks
 * to hold products.
 * @author andys
 *
 */
public class RackedProductDistributor extends AbstractProductDistributor{
	
	private HardwareFacade facade;
	
	/**
	 * Register the hardware for this RackedProductDistributor method.
	 * The rack-type distribution system needs to talk to the hardware.
	 * @param f
	 */
	public RackedProductDistributor(HardwareFacade f){
		facade = f;
	}
	
	/**
	 * finds a ProductKind by communicating with the hardware,
	 * and causes it to be dispensed.
	 * @param product
	 * @return true if the product was successfully dispensed.
	 * 			false otherwise.
	 */
	public boolean dispenseProduct(ProductKind product){
		Iterator<ProductRack> iter = facade.productRackIterator();
		int index = 0;
		boolean dispensed = false;
		while(iter.hasNext()){
			ProductRack rack = iter.next();
			if (facade.getProductKind(index).getName().equals(product.getName())){
				try {
					rack.dispenseProduct();
					dispensed = true;
				} catch (DisabledException | EmptyException | CapacityExceededException e) {
					// TODO Auto-generated catch block
					// could make this more responsive - separate exceptions and
					// deliver nice messages for each. meanwhile...
					e.printStackTrace();
					dispensed = false;
				}
				break;
			}
			index++;
		}
		
		return dispensed;
	}

	@Override
	/** Checks to see if a rack has the desired product,
	 *  and that it's not empty. 
	 */
	public boolean isProductDispensable(ProductKind product) {
		Iterator<ProductRack> iter = facade.productRackIterator();
		int index = 0;
		boolean dispensable = false;
		while(iter.hasNext()){
			ProductRack rack = iter.next();
			if (facade.getProductKind(index).getName().equals(product.getName()) && rack.size() > 0){
				dispensable = true;
				break;
			}
			index++;
		}
		
		return dispensable;
	}
}
