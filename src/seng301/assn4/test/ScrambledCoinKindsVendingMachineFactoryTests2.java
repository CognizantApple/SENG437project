package seng301.assn4.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.Product;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

import seng301.assn4.VendingMachine;

@SuppressWarnings("javadoc")
public class ScrambledCoinKindsVendingMachineFactoryTests2 {
    private HardwareFacade hf;

    @Before
    public void setup() {
	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(5), new Cents(25), new Cents(10)}, 3, 10, 10, 10);
	hf = vm.getHardware();

	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
	hf.getCoinRack(0).load();
	hf.getCoinRack(1).load(new Coin(new Cents(5)));
	hf.getCoinRack(2).load(new Coin(new Cents(25)), new Coin(new Cents(25)));
	hf.getCoinRack(3).load(new Coin(new Cents(10)));

	hf.loadProducts(1, 1, 1);
    }

    /**
     * T11
     */
    @Test
    public void testExtractBeforeSaleComplex() throws DisabledException {
	hf.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));

	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));

	assertEquals(65, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromProductRacks(hf));

	hf.loadCoins(0, 1, 2, 1);
	hf.loadProducts(1, 1, 1);

	hf.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromProductRacks(hf));

	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(5), new Cents(25), new Cents(10)}, 3, 10, 10, 10);
	hf = vm.getHardware();
	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
	hf.configure(new ProductKind("A", new Cents(5)), new ProductKind("B", new Cents(10)), new ProductKind("C", new Cents(25)));
	assertEquals(0, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromProductRacks(hf));

	hf.getCoinRack(0).load();
	hf.getCoinRack(1).load(new Coin(new Cents(5)));
	hf.getCoinRack(2).load(new Coin(new Cents(25)), new Coin(new Cents(25)));
	hf.getCoinRack(3).load(new Coin(new Cents(10)));
	hf.getProductRack(0).load(new Product("A"));
	hf.getProductRack(1).load(new Product("B"));
	hf.getProductRack(2).load(new Product("C"));

	hf.getCoinSlot().addCoin(new Coin(new Cents(10)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(5)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(10)));
	hf.getSelectionButton(2).press();
	assertEquals(Arrays.asList(0, "C"), Utilities.extractAndSortFromDeliveryChute(hf));
	assertEquals(90, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("A", "B"), Utilities.extractAndSortFromProductRacks(hf));
    }
}
