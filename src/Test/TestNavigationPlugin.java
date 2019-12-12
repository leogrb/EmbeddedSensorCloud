package Test;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginNavigation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestNavigationPlugin {
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
    public void PluginNavigation_should_set_error_response_if_streets_not_loaded() throws Exception{
        String navUrl = "/navigation?navigation_plugin=true";
        Request req = new RequestImpl(RequestHelper.getValidRequestStream(navUrl, "POST", "value=kksd&load=false"));
        Plugin nav = new PluginNavigation();
        Response resp = nav.handle(req);
        String body = getBody(resp).toString();
        assertTrue(body.contains("Error: Straßen sind nicht verfügbar"));
    }

    @Test
    public void PluginNavigation_should_set_error_response_if_form_empty() throws Exception{
        String navUrl = "/navigation?navigation_plugin=true";
        Request req = new RequestImpl(RequestHelper.getValidRequestStream(navUrl, "POST", "value=&load=false"));
        Plugin nav = new PluginNavigation();
        Response resp = nav.handle(req);
        String body = getBody(resp).toString();
        assertTrue(body.contains("Not found: Keine Städte gefunden"));
    }
}
