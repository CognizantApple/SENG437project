package seng301.assn4.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

import seng301.assn4.VendingMachine;

@SuppressWarnings("javadoc")
public class NoConfigureOrLoadVendingMachineFactoryTests {
    private HardwareFacade hf;

    @Before
    public void setup() {
	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(5), new Cents(10), new Cents(25), new Cents(100)}, 3, 10, 10, 10);
	hf = vm.getHardware();
    }

    /**
     * T03
     */
    @Test
    public void testTeardownWithoutConfigureOrLoad() {
	assertEquals(Arrays.asList(0), Utilities.extractAndSortFromDeliveryChute(hf));
	assertEquals(0, Utilities.extractAndSumFromCoinRacks(hf));
	assertEquals(0, Utilities.extractAndSumFromStorageBin(hf));
	assertEquals(Arrays.asList(), Utilities.extractAndSortFromProductRacks(hf));
    }
}
