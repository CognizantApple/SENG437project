package seng301.assn4.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend4.Cents;
import org.lsmr.vending.frontend4.ProductKind;
import org.lsmr.vending.frontend4.hardware.HardwareFacade;

import seng301.assn4.VendingMachine;

@SuppressWarnings("javadoc")
public class BadVendingMachineFactoryTest {
    private HardwareFacade hf;

    @Before
    public void setup() {
	VendingMachine vm = new VendingMachine(new Cents[] {new Cents(5), new Cents(10), new Cents(25), new Cents(100)}, 3, 10, 10, 10);
	hf = vm.getHardware();
    }

    /**
     * bad-script2
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadScript2() {
	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(0)));
    }

    /**
     * U02
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCostsList() {
	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)), new ProductKind("stuff", new Cents(0)));
    }

    /**
     * U03
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadNamesList() {
	hf.configure(new ProductKind("Coke", new Cents(250)), new ProductKind("water", new Cents(250)));
    }

    /**
     * U04
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNonUniqueDenomination() {
	new VendingMachine(new Cents[] {new Cents(1), new Cents(1)}, 1, 10, 10, 10);
    }

    /**
     * U05
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCoinKind() {
	new VendingMachine(new Cents[] {new Cents(0)}, 1, 10, 10, 10);
    }

    /**
     * U06
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadButton() {
	hf.getSelectionButton(3).press();
    }

    /**
     * U07
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadButton2() {
	hf.getSelectionButton(-1).press();
	fail();
    }

    /**
     * U08
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadButton3() {
	hf.getSelectionButton(4).press();
	fail();
    }
}
