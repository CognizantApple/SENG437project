package seng301.assn4.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.Product;
import org.lsmr.vending.frontend4.hardware.CoinRack;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

/**
 * A set of simple utility methods for use by test cases.
 * 
 * @author Robert J. Walker
 */
public class Utilities {
    /**
     * Extracts all items in the delivery chute of the indicated vending
     * machine, sorting them.
     * 
     * @param hf
     *            The vending machine from which to extract.
     * @return A list of the extracted items.
     */
    public static List<Object> extractAndSortFromDeliveryChute(HardwareFacade hf) {
	Object[] actualItems = hf.getDeliveryChute().removeItems();
	int actualValue = 0;
	List<Object> actualList = new ArrayList<>();

	for(Object obj : actualItems) {
	    if(obj instanceof Product) {
		Product product = (Product)obj;
		String name = product.getName();
		actualList.add(name);
	    }
	    else
		actualValue += ((Coin)obj).getValue().getValue();
	}

	Collections.sort(actualList, null);

	actualList.add(0, actualValue);
	return actualList;
    }

    /**
     * All coins in the coin racks of the indicated vending machine are
     * extracted.
     *
     * @param hf
     *            The vending machine to extract from.
     * @return The sum of the values of the extracted coins.
     */
    public static int extractAndSumFromCoinRacks(HardwareFacade hf) {
	int total = 0;

	for(int i = 0, max = hf.getNumberOfCoinRacks(); i < max; i++) {
	    CoinRack cr = hf.getCoinRack(i);
	    List<Coin> coins = cr.unload();
	    for(Coin coin : coins)
		total += coin.getValue().getValue();
	}

	return total;
    }

    /**
     * All coins in the storage bin of the indicated vending machine are
     * extracted.
     *
     * @param hf
     *            The vending machine to extract from.
     * @return The sum of the values of the extracted coins.
     */
    public static int extractAndSumFromStorageBin(HardwareFacade hf) {
	int total = 0;

	List<Coin> coins = hf.getStorageBin().unload();
	for(Coin coin : coins)
	    total += coin.getValue().getValue();

	return total;
    }

    /**
     * Extracts all products in the product racks of the indicated vending
     * machine, sorting them.
     * 
     * @param hf
     *            The vending machine from which to extract.
     * @return A list of the extracted items.
     */
    public static List<String> extractAndSortFromProductRacks(HardwareFacade hf) {
	List<Product> actualProducts = new ArrayList<>();
	for(int i = 0, max = hf.getNumberOfProductRacks(); i < max; i++)
	    actualProducts.addAll(hf.getProductRack(i).unload());

	List<String> actualList = new ArrayList<>();
	for(Product product : actualProducts)
	    actualList.add(product.getName());

	Collections.sort(actualList, null);

	return actualList;
    }
}
