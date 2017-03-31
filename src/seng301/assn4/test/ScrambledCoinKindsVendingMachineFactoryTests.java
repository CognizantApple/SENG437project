package seng301.assn4.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.Coin;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.DisabledException;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

import seng301.assn4.VendingMachine;

@SuppressWarnings("javadoc")
public class ScrambledCoinKindsVendingMachineFactoryTests {
    private HardwareFacade hf;

    @Before
    public void setup() {
	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(5), new Cents(25), new Cents(10)}, 3, 10, 10, 10);
	hf = vm.getHardware();

	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
	hf.loadCoins(0, 1, 2, 1);
	hf.loadProducts(1, 1, 1);
    }

    /**
     * T06
     */
    @Test
    public void testExtractBeforeSale() throws DisabledException {
	hf.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));

	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("Coke", "stuff", "water"), Utilities.extractAndSortFromProductRacks(hf));
    }
}
