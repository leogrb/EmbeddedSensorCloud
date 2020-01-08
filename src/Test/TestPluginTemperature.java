package Test;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.DAO.PostgresConManager;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginTemperature;
import mywebserver.Properties.Config;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertTrue;

public class TestPluginTemperature {

    @BeforeClass
    public static void setUpBeforeClass() {
        Config config = Config.newInstance();
        config.initialize();
        config.setProps("username", "postgres");
        config.setProps("password", "leo");
        config.setProps("driver", "org.postgresql.Driver");
        config.setProps("port", "80");
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        PCN.initialize();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        PCN.closeConnections();
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
    public void PluginTemperature_handle_valid_REST() throws Exception {
        Plugin p = new PluginTemperature();
        String resturl = "/GetTemperature/2019/05/11";
        Request req = new RequestImpl(RequestHelper.getValidRequestStream(resturl));
        p.canHandle(req);
        Response res = p.handle(req);
        String b = getBody(res).toString();
        assertTrue("application/xml expected", res.getContentType().equals("application/xml"));
        assertTrue("<temperatures> expected", b.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><temperatures>"));
        assertTrue("Date not found", b.contains("<date>2019-05-11</date>"));
    }

    @Test
    public void PluginTemperature_handle_invalidDate_REST() throws Exception {
        Plugin p = new PluginTemperature();
        String resturl = "/GetTemperature/2019000/05/11";
        Request req = new RequestImpl(RequestHelper.getValidRequestStream(resturl));
        Response res = p.handle(req);
        String b = getBody(res).toString();
        assertTrue("application/xml expected", res.getContentType().equals("application/xml"));
        assertTrue("<Error> expected", b.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Error type=\"InvalidDate\">"));
        assertTrue("Response text expected", b.contains("<Response>Requested Date is invalid. Valid DateFormat is: yyyy-mm-dd</Response>"));
    }

    @Test
    public void PluginTemperature_handle_validDate() throws Exception {
        Plugin p = new PluginTemperature();
        String resturl = "/temperature?value=2019-05-11";
        Request req = new RequestImpl(RequestHelper.getValidRequestStream(resturl));
        Response res = p.handle(req);
        String b = getBody(res).toString();
        System.out.println(b);
        assertTrue("application/xml expected", res.getContentType().equals("application/xml"));
        assertTrue("<temperatures> expected", b.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><temperatures>"));
        assertTrue("Date not found", b.contains("<date>2019-05-11</date>"));
    }

    @Test
    public void PluginTemperature_handle_invalidDate() throws Exception {
        Plugin p = new PluginTemperature();
        String resturl = "/temperature?value=200019-05-11";
        Request req = new RequestImpl(RequestHelper.getValidRequestStream(resturl));
        Response res = p.handle(req);
        String b = getBody(res).toString();
        System.out.println(b);
        assertTrue("application/xml expected", res.getContentType().equals("application/xml"));
        assertTrue("<Error> expected", b.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Error type=\"InvalidDate\">"));
        assertTrue("Response text expected", b.contains("<Response>Requested Date is invalid. Valid DateFormat is: yyyy-mm-dd</Response>"));
    }

}
