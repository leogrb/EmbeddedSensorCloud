package Test;

import mywebserver.Sensor.Months;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestMonths {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void Months_should_return_number_of_days() {
        int numberOfDays = Months.getnumOfDays(12);
        assertTrue(numberOfDays == 31);
    }
}
