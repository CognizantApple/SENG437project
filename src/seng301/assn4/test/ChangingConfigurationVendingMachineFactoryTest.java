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
public class ChangingConfigurationVendingMachineFactoryTest {
    private HardwareFacade hf;

    @Before
    public void setup() {
	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(5), new Cents(10), new Cents(25), new Cents(100)}, 3, 10, 10, 10);
	hf = vm.getHardware();

	hf.configure(new ProductKind("A", new Cents(5)), new ProductKind("B", new Cents(10)), new ProductKind("C", new Cents(25)));
	hf.getCoinRack(0).load(new Coin(new Cents(5)));
	hf.getCoinRack(1).load(new Coin(new Cents(10)));
	hf.getCoinRack(2).load(new Coin(new Cents(25)), new Coin(new Cents(25)));
	hf.getCoinRack(3).load();
	hf.getProductRack(0).load(new Product("A"));
	hf.getProductRack(1).load(new Product("B"));
	hf.getProductRack(2).load(new Product("C"));

	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
    }

    /**
     * T07
     */
    @Test
    public void testChangingConfiguration() throws DisabledException {
	hf.getSelectionButton(0).press();
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));

	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "A"), Utilities.extractAndSortFromDeliveryChute(hf));

	assertEquals(315, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("B", "C"), Utilities.extractAndSortFromProductRacks(hf));

	hf.getCoinRack(0).load(new Coin(new Cents(5)));
	hf.getCoinRack(1).load(new Coin(new Cents(10)));
	hf.getCoinRack(2).load(new Coin(new Cents(25)), new Coin(new Cents(25)));
	hf.getCoinRack(3).load();
	hf.getProductRack(0).load(new Product("Coke"));
	hf.getProductRack(1).load(new Product("water"));
	hf.getProductRack(2).load(new Product("stuff"));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	hf.getSelectionButton(0).press();
	assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
	assertEquals(315, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromProductRacks(hf));
    }
}
