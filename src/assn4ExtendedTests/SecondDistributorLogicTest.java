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

import distributors.RackedProductDistributor;
import seng301.assn4.LogicCore;
import seng301.assn4.VendingMachine;
import seng301.assn4.test.Utilities;

@SuppressWarnings("javadoc")
public class SecondDistributorLogicTest {
    private HardwareFacade hf;
	private HardwareFacade hf2;
    private LogicCore lc;
    
    
    @Before
    public void setup() {
		VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		VendingMachine vm2 = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		
		hf = vm.getHardware();
		hf2 = vm2.getHardware();
		
		hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		hf2.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		
		hf.loadProducts(2, 0, 0);
		hf2.loadProducts(2, 0, 0);
		
		
		hf.loadCoins(0, 4, 0, 0);
		lc = vm.getLogic();
		//Register the SECOND machine's rack system as an extra payment method for the first machine!!
		// This is also avant-garde stuff.
		
		RackedProductDistributor d = new RackedProductDistributor(vm2.getHardware());
		
		lc.addProductDistributor(d);
	
    }

    /**
     * Test to check that we can use another vending machine's product racks
     * as a second product distributor, with priority
     */
    @Test
    public void testSecondDistributorPriority() throws DisabledException {

    	// set the priority of the second machine to be higher than the first one.
    	lc.setProductDistributorPriority(1, 0);
    	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	
		hf.getSelectionButton(0).press();
		
		//What we should see is change delivered in the first machine, and a coke delivered
		// from the 'second' distribution system in the second machine!
		assertEquals(Arrays.asList(50), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf2));
    
		// set the priority of the second machine to be lower than the first one.
    	lc.setProductDistributorPriority(1, 10);
    	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	
		hf.getSelectionButton(0).press();
		
		//What we should see is change delivered in the first machine, and a coke delivered
		// from the 'first' distribution system in the first machine!
		assertEquals(Arrays.asList(50,"Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf2));
		
		assertEquals(Arrays.asList("Coke"), Utilities.extractAndSortFromProductRacks(hf));
		assertEquals(Arrays.asList("Coke"), Utilities.extractAndSortFromProductRacks(hf2));
    }
   
}
