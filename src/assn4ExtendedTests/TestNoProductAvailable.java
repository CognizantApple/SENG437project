package assn4ExtendedTests;

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
import seng301.assn4.test.Utilities;

@SuppressWarnings("javadoc")
public class TestNoProductAvailable {
    private HardwareFacade hf;
    private VendingMachine vm;

    @Before
    public void setup() {
		vm = new VendingMachine(new Cents[] {new Cents(5), new Cents(10), new Cents(25), new Cents(100)}, 3, 10, 10, 10);
		hf = vm.getHardware();
	
		hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		hf.loadCoins(1, 1, 2, 0);
		hf.loadProducts(0, 1, 1);
    }


    /**
     * T01
     */
    @Test
    public void testInsertAndPressWithExactChange() throws DisabledException {
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(25)));

	hf.getSelectionButton(0).press();

	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
	assertEquals(65, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromProductRacks(hf));
    }

}
