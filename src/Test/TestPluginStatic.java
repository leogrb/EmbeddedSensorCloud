package Test;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginStatic;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;

public class TestPluginStatic {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void PluginStatic_throws_exception() throws Exception {
        try {
            InputStream s = RequestHelper.getValidRequestStream("localhost/test");
            Request req = new RequestImpl(s);
            Plugin p = new PluginStatic();
            Response res = p.handle(req);
        } catch (Exception e) {
            assertTrue(e.getClass() == FileNotFoundException.class);
        }
    }
}

