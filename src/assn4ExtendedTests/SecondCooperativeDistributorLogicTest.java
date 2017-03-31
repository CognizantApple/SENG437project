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
import hardwareToSoftware.SelectionButtonMiddleman;
import seng301.assn4.LogicCore;
import seng301.assn4.VendingMachine;
import seng301.assn4.test.Utilities;

@SuppressWarnings("javadoc")
public class SecondCooperativeDistributorLogicTest {
    private HardwareFacade hf;
	private HardwareFacade hf2;
    private LogicCore lc;
    SelectionButtonMiddleman buttonMiddleman2;
    
    
    @Before
    public void setup() {
		VendingMachine vm = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		VendingMachine vm2 = new VendingMachine(new Cents[] {new Cents(100), new Cents(25), new Cents(10), new Cents(5)}, 3, 10, 10, 10);
		
		hf = vm.getHardware();
		hf2 = vm2.getHardware();
		
		hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(205)));
		hf2.configure(new ProductKind("Pepper", new Cents(200)), new ProductKind("Beer", new Cents(150)), new ProductKind("Dew", new Cents(100)));
		
		hf.loadProducts(1, 1, 1);
		hf2.loadProducts(1, 1, 1);
		
		
		hf.loadCoins(4, 4, 4, 4);
		lc = vm.getLogic();
		//Register the SECOND machine's rack system as an extra payment method for the first machine!!
		// This is also avant-garde stuff.
		
		RackedProductDistributor d = new RackedProductDistributor(vm2.getHardware());
		lc.addProductDistributor(d);
		
		//Note that the second machine has it's own buttonMiddleman sending it notifications
		// as well. This will result in the second machine sending out a lot of complaints
		// about insufficient funds, but we don't need to worry about that :)
		buttonMiddleman2 = new SelectionButtonMiddleman();
		buttonMiddleman2.registerHardware(hf2);	//register the hardware it's listening to
		buttonMiddleman2.register(lc);			//retgister the logic it's talking to
	
    }

    /**
     * Test to check that we could have two racked distributors with different
     * products, and that we could use buttons from the second machine as
     * inputs to the logic of the first machine!
     * Kind of a crazy test that helps assess the modularity of the core logic.
     */
    @Test
    public void testCooperativeSelectorsDistributors() throws DisabledException {
    	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	
		hf.getSelectionButton(0).press();

		assertEquals(Arrays.asList(50, "Coke"), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf2));
    	
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(100)));
	
		// let the second set of buttons send the logic of the first machine a request
		// for a 'Pepper'. the logic will be able to find one in it's registered
		// distribution system of the second machine! 
		hf2.getSelectionButton(0).press();
		
		// Change and payments are still only handled by the first machine, remember.
		assertEquals(Arrays.asList(100), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0, "Pepper"), Utilities.extractAndSortFromDeliveryChute(hf2));
		

		hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
		hf.getCoinSlot().addCoin(new Coin(new Cents(25)));
		hf2.getSelectionButton(2).press();
		
		assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
		assertEquals(Arrays.asList(0, "Dew"), Utilities.extractAndSortFromDeliveryChute(hf2));
		
		assertEquals(Arrays.asList("stuff", "water"), Utilities.extractAndSortFromProductRacks(hf));
		assertEquals(Arrays.asList("Beer"), Utilities.extractAndSortFromProductRacks(hf2));
    }
   
}
