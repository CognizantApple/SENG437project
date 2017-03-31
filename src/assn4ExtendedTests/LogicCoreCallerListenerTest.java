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

import seng301.assn4.LogicCore;
import seng301.assn4.VendingMachine;
import seng301.assn4.test.Utilities;
import softwareToHardware.LogicEventListener;

@SuppressWarnings("javadoc")
public class LogicCoreCallerListenerTest {
    private HardwareFacade hf;
    private LogicCore lc;

	private int insufficientFundsEvent;
	private int productUnavailableEvent;
	private int productDispensedEvent;
    @Before
    public void setup() {
		VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		hf = vm.getHardware();
		lc = vm.getLogic();
		
		hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		hf.loadCoins(1, 3, 2, 1);
		hf.loadProducts(1, 1, 1);
		lc.register(coreEventListener);
	
    }

    /**
     * Test to check the coin payment method is responding as frequently as it should be,
     * as a caller to a listner.
     */
    @Test
    public void testCoinPaymentMethodCallerActivity() throws DisabledException {
    	hf.getSelectionButton(0).press();
    	
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getSelectionButton(0).press();
	
		assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
		
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getSelectionButton(1).press();
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		
		// no coke left!
		hf.getSelectionButton(0).press();
		hf.getSelectionButton(0).press();
		
		// you can't always get what you want...
		hf.getSelectionButton(1).press();
	
		assertEquals(Arrays.asList(50, "water"), Utilities.extractAndSortFromDeliveryChute(hf));
		
		assertEquals(700, Utilities.extractAndSumFromCoinRacks(hf));
		assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
		assertEquals(Arrays.asList("stuff"), Utilities.extractAndSortFromProductRacks(hf));
		
		assertEquals(insufficientFundsEvent,	2);
		assertEquals(productUnavailableEvent,	2);
		assertEquals(productDispensedEvent,		2);
		
    }
    
    /**
     * stub class for listening to logic events
     */
    private LogicEventListener coreEventListener = new LogicEventListener(){

		@Override
		public void fundsAdded(String message, Cents amount) {
			throw new RuntimeException();
		}

		@Override
		public void fundsCollected(String message, Cents amount) {
			throw new RuntimeException();
		}

		@Override
		public void changeDelivered(String message, Cents amount) {
			throw new RuntimeException();
		}
		@Override
		public void insufficientFunds(String message, Cents entered, Cents required) {
			insufficientFundsEvent++;
		}
		@Override
		public void productUnavailable(String message, ProductKind kind) {
			productUnavailableEvent++;
		}
		@Override
		public void productDispensed(String message, ProductKind kind) {
			productDispensedEvent++;
		}
		@Override
		public void machineLocked(String message) {
			throw new RuntimeException();
		}
		@Override
		public void machineUnlocked(String message) {
			throw new RuntimeException();
		}
		@Override
		public void changeAvailable(String message) {
			throw new RuntimeException();
		}
		@Override
		public void changeUnavailable(String message) {
			throw new RuntimeException();
		}
    };
}
