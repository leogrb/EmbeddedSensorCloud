package Test;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginToLower;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class TestPluginToLower {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private StringBuilder getBody(Response resp) throws UnsupportedEncodingException, IOException {
        StringBuilder body = new StringBuilder();

        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        try {
            resp.send(ms);
            BufferedReader sr = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(ms.toByteArray()), "UTF-8"));
            String line;
            while ((line = sr.readLine()) != null) {
                body.append(line + "\n");
            }
        } finally {
            ms.close();
        }
        return body;
    }

    @Test
    public void ToLowerPlugin_should_return_error() throws Exception {
        InputStream s = RequestHelper.getValidRequestStream("localhost/tolower?tolower_plugin=true", "POST", "");
        Request req = new RequestImpl(s);
        Plugin p = new PluginToLower();
        Response res = p.handle(req);
        assertTrue("PluginToLower returned wrong response", getBody(res).toString().contains("Not found: Bitte geben Sie einen Text ein\n"));
    }
}
