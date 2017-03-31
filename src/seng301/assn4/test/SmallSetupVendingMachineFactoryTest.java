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
public class SmallSetupVendingMachineFactoryTest {
    private HardwareFacade hf;

    @Before
    public void setup() {
	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(5), new Cents(10), new Cents(25), new Cents(100)}, 1, 10, 10, 10);
	hf = vm.getHardware();
    }

    /**
     * T08
     */
    @Test
    public void testApproximateChange() throws DisabledException {
		hf.configure(new ProductKind("stuff", new Cents(140)));
		hf.getCoinRack(0).load();
		hf.getCoinRack(1).load(new Coin(new Cents(10)), new Coin(new Cents(10)), new Coin(new Cents(10)), new Coin(new Cents(10)), new Coin(new Cents(10)));
		hf.getCoinRack(2).load(new Coin(new Cents(25)));
		hf.getCoinRack(3).load(new Coin(new Cents(100)));
		hf.getProductRack(0).load(new Product("stuff"));
	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(155, "stuff"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(320, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
    }

    /**
     * T09
     */
    @Test
    public void testHardForChange() throws DisabledException {
		hf.configure(new ProductKind("stuff", new Cents(140)));
		hf.loadCoins(1, 6, 1, 1);
		hf.loadProducts(1);
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(160, "stuff"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(330, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
		assertEquals(Arrays.asList(), Utilities.extractAndSortFromProductRacks(hf));
    }

    /**
     * T10
     */
    @Test
    public void testInvalidCoins() throws DisabledException {
		hf.configure(new ProductKind("stuff", new Cents(140)));
		hf.loadCoins(1, 6, 1, 1);
		hf.loadProducts(1);
		hf.getCoinSlot().addCoin(new Coin(new Cents(1)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(139)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(140), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(190, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
		assertEquals(Arrays.asList("stuff"), Utilities.extractAndSortFromProductRacks(hf));
    }

    /**
     * T12
     */
    @Test
    public void testApproximateChangeWithCredit() throws DisabledException {
		hf.configure(new ProductKind("stuff", new Cents(140)));
		hf.loadCoins(0, 5, 1, 1);
		hf.loadProducts(1);
	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(155, "stuff"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(320, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
		assertEquals(Arrays.asList(), Utilities.extractAndSortFromProductRacks(hf));
	
		hf.loadCoins(10, 10, 10, 10);
		hf.loadProducts(1);
	
		hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(10)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(0, "stuff"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(1400, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(135, Utilities.extractAndSumFromStorageBin(hf));
		assertEquals(Arrays.asList(), Utilities.extractAndSortFromProductRacks(hf));
    }

    /**
     * T13
     */
    @Test
    public void testNeedToStorePayment() throws DisabledException {
		hf.configure(new ProductKind("stuff", new Cents(135)));
		hf.loadCoins(10, 10, 10, 10);
		hf.loadProducts(1);
	
		hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(10)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(0, "stuff"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(1400, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(135, Utilities.extractAndSumFromStorageBin(hf));
		assertEquals(Arrays.asList(), Utilities.extractAndSortFromProductRacks(hf));
    }

    /**
     * T14
     */
    @Test
    public void testWeirdName() throws DisabledException {
	hf.configure(new ProductKind("\"", new Cents(135)));
    }
}
