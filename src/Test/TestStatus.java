package Test;

import mywebserver.HttpResponse.Status;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestStatus {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void Status_should_return_proper_status() {
        Status status = Status.getStatusWithCode(200);
        assertTrue(status.getDescription().equals("OK"));
    }
}
