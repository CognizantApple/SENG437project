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

import paymentMethods.CoinPaymentMethod;
import seng301.assn4.LogicCore;
import seng301.assn4.VendingMachine;
import seng301.assn4.test.Utilities;

@SuppressWarnings("javadoc")
public class SecondPaymentMethodLogicTest {
    private HardwareFacade hf;
	private HardwareFacade hf2;
    private LogicCore lc;
    
    
    @Before
    public void setup() {
		VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		hf = vm.getHardware();
		lc = vm.getLogic();
		
		VendingMachine vm2 = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		hf2 = vm2.getHardware();
		hf2.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		hf2.loadCoins(0, 1, 0, 0);
		
		hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		hf.loadCoins(0, 1, 0, 0);
		hf.loadProducts(2, 1, 1);
		
		//Register the SECOND machine's coin system as an extra payment method for the first machine!!
		// This is some avant-garde vending technology right here
		CoinPaymentMethod p = new CoinPaymentMethod(vm2.getHardware());
		p.setchangePriority(6);
		p.setcollectPriority(6);
		lc.addPaymentMethod(p);
	
    }

    /**
     * Test to check that we can use funds from another coin payment method in our purchases
     */
    @Test
    public void testSecondCoinPaymentMethod() throws DisabledException {
    
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf2));
		
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));

		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf2));
		
		hf2.getCoinSlot().addCoin(new Coin(new Cents(100)));
	
		//300 inserted, 200 in first payment method, 100 in second
		hf.getSelectionButton(0).press();
		
		//What we should see is a complete payment between the two sources, and
		// 25 cents in change delivered by each.
		assertEquals(Arrays.asList(25, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(25), Utilities.extractAndSortFromDeliveryChute(hf2));
    }
    
    /**
     * Test to check that we can affect how payment is distributed between methods
     * by modifying priorities
     * @throws DisabledException
     */
    @Test
    public void testPrimaryChangeGivingPaymentMethod() throws DisabledException {
    
    	// Remove change from the first method's racks 
    	assertEquals(25, Utilities.extractAndSumFromCoinRacks(hf));
    	// put extra change in the second racks
    	hf2.loadCoins(1, 1, 4, 2);
    	
    	// Set the second payment method to be the primary change-giver
    	lc.setPaymentMethodChangePriority(1, 0);
    	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(0, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(50), Utilities.extractAndSortFromDeliveryChute(hf2));

		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getSelectionButton(0).press();
		assertEquals(Arrays.asList(0, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(50), Utilities.extractAndSortFromDeliveryChute(hf2));
		
		// All the payment has been placed in the racks of the first payment method,
		// and not used for change.
    	assertEquals(600, Utilities.extractAndSumFromCoinRacks(hf));
    	
    	// All of the change has been taken from the second payment method, except the 100 coin.
    	assertEquals(100, Utilities.extractAndSumFromCoinRacks(hf2));
    }
   
}
