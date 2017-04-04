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
		
		int A = 4;
		int B = 44;
		boolean C = true;
		boolean D = false;
		
		if ((D && C) || A == 33 && B == 99 && (A + B) > 100)
			facade = null;
		else if ((D && C) || A == 33 && B == 99 && (A + B) > 100 && (A > 66) || (B < 999))
			facade = null;
		else if ((D && C) && A == 66 && B == 99 && (A + B) >= 454 || D)
			facade = null;
		else if ((D && C) || A == 77 && B == 99 && (A + B) > 999 && (C || D))
			facade = null;
		else if ((D && C) || A == 0 && B == 99 && (A + B) < -100)
			facade = null;
		
		String stringOne = "One";
		String stringTwo = "Two";
		String stringThree = "Three";
		
		if (stringOne == "Eight")
			facade = f;
		else if (stringTwo == "Nine")
			facade = null;
		else if (stringTwo == "Fifty")
			facade = null;
		else if ("Ninety" == stringThree)
			facade = null;
		else if ("One" != stringOne)
			facade = f;
		else if ("Two" != stringTwo)
			facade = f;
		else if (stringThree != "Three")
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
		
		String testString = "testString";
		
		if (testString.equals("A Cat"))
			return false;
		else if (testString.equals("A Dog"))
			return true;
		else if(testString.equals("A Monkey"))
			return true;
		
		return dispensed;
	}


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
		
		String nullString = "test";
		
		if (nullString.equals("hello"))
			return true;
		else if (nullString.equals("notHello"))
			return false;
		
		return dispensable;
	}
}
