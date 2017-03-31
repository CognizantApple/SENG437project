package assn4ExtendedTests;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.vending.frontend4.Cents;
import seng301.assn4.LogicCore;
import seng301.assn4.VendingMachine;

@SuppressWarnings("javadoc")
public class BadPriorityPaymentDistributionTest {
    private LogicCore lc;

    @Before
    public void setup() {
		VendingMachine vm = new VendingMachine(new Cents[] {new Cents(5), new Cents(10), new Cents(25), new Cents(100)}, 3, 10, 10, 10);
		lc = vm.getLogic();
    }

    /**
     * Test that we can't assign a negative value as a payment method collect priority.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadPaymentCollectPriority() {
		lc.setPaymentMethodCollectPriority(0, -1);
		fail();
    }
    
    /**
     * Test that we can't assign a negative value as a payment method change priority.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadPaymentChangePriority() {
		lc.setPaymentMethodChangePriority(0, -2);
		fail();
    }


    /**
     * Test that we can't assign a negative value as a product distributor priority.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testBadDistributorPriority() {
		lc.setProductDistributorPriority(0, -10);
		fail();
    }

}
