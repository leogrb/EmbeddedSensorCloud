package Test;

import static org.junit.Assert.*;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.DAO.PostgresConManager;
import mywebserver.DAO.TemperatureDao;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginStatic;
import mywebserver.Plugin.PluginToLower;
import mywebserver.Sensor.Sensorthread;
import mywebserver.XML.XMLBuilder;
import org.junit.*;

import mywebserver.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;

public class CustomTests {

    /*If you allocate expensive external resources in a BeforeClass method you need to
    release them after all the tests in the class have run. Annotating a public static void method
    with @AfterClass causes that method to be run after all the tests in the class have been run.
    All @AfterClass methods are guaranteed to run even if a BeforeClass method throws an exception.
    The @AfterClass methods declared in superclasses will be run after those of the current class.
    */
    @BeforeClass
    public static void setUpBeforeClass() {
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
    public void PCN_should_return_connections() {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        Connection con = PCN.getConnectionFromPool();
        Connection con2 = PCN.getConnectionFromPool();
        Connection con3 = PCN.getConnectionFromPool();
        Connection con4 = PCN.getConnectionFromPool();
        Connection con5 = PCN.getConnectionFromPool();

        assertTrue(con instanceof Connection);
        assertNotNull("PCN returned null", con);
        assertTrue(con2 instanceof Connection);
        assertNotNull("PCN returned null", con2);
        assertTrue(con3 instanceof Connection);
        assertNotNull("PCN returned null", con3);
        assertTrue(con4 instanceof Connection);
        assertNotNull("PCN returned null", con4);
        assertTrue(con5 instanceof Connection);
        assertNotNull("PCN returned null", con5);

        PCN.returnConnectionToPool(con);
        PCN.returnConnectionToPool(con2);
        PCN.returnConnectionToPool(con3);
        PCN.returnConnectionToPool(con4);
        PCN.returnConnectionToPool(con5);
    }

    @Test
    public void TemperatureDao_should_insert_temperature() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        Connection con = PCN.getConnectionFromPool();
        assertNotNull("PCN returned null", con);
        TemperatureDao temperatureDao = new TemperatureDao();
        temperatureDao.createTable(con);
        Temperature temp = new Temperature(LocalDate.now(), 99.9f);
        int res = temperatureDao.insertTemp(con, temp);
        assertEquals(res, 1);
        String preStatement = "SELECT temp FROM temperature WHERE day = ? ORDER BY day ASC";
        PreparedStatement preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.setObject(1, LocalDate.now());
        ResultSet rs = preparedStatement.executeQuery();
        assertTrue("Insert error", rs.isBeforeFirst());
        rs.next();
        assertEquals("Insert error", rs.getFloat(1), 99.9f, 0f);
        PCN.returnConnectionToPool(con);
    }

    @Test
    public void TemperatureDao_should_return_50_entries() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        Connection con = PCN.getConnectionFromPool();
        assertNotNull("PCN returned null", con);
        TemperatureDao temperatureDao = new TemperatureDao();
        temperatureDao.createTable(con);
        LinkedList<Temperature> data = temperatureDao.getAllTemperature(con);
        assertTrue("50 entries have to be returned", data.size() == 50);
        PCN.returnConnectionToPool(con);
    }

    /*@Test
    public void Sensor_should_create_Temperature() throws Exception{
        Sensorthread sensor = new Sensorthread();
        Temperature temp = sensor.createRandomTempObj(LocalDate.now());
        assertNotNull("Sensor returned null", temp);
        assertTrue("Sensor returned Temperature out of range", temp.getTemp() >= -20f && temp.getTemp() <= 50f);
    }
    */


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

    @Test
    public void ToLowerPlugin_should_return_error() throws Exception {
        InputStream s = RequestHelper.getValidRequestStream("localhost/tolower?tolower_plugin=true", "POST", "");
        Request req = new RequestImpl(s);
        Plugin p = new PluginToLower();
        Response res = p.handle(req);
        assertTrue("PluginToLower returned wrong response", getBody(res).toString().contains("Not found: Bitte geben Sie einen Text ein\n"));
    }

    @Test
    public void XMLBuilder_should_create_validXML() throws Exception {
        LinkedList<Temperature> data = new LinkedList<>();
        Temperature temp = new Temperature(LocalDate.now(), 99.9f);
        assertNotNull(temp);
        temp.setId(1);
        data.add(temp);
        Document xml = XMLBuilder.createValidXML(data);
        NodeList nl = xml.getElementsByTagName("temperature");
        Element e = (Element) nl.item(0);
        assertNotNull(e);
        assertTrue(e.getAttributes().getNamedItem("id").getNodeValue().equals(Integer.toString(1)));
        NodeList date = e.getElementsByTagName("date");
        NodeList t = e.getElementsByTagName("temp");
        assertTrue(date.item(0).getFirstChild().getTextContent().equals(LocalDate.now().toString()));
        assertTrue(t.item(0).getFirstChild().getTextContent().equals(Float.toString(99.9f)));
    }

    @Test
    public void XMLBuilder_should_create_invalidDateXML() throws Exception {
        Document xml = XMLBuilder.createInvalidDateXML("202020-12-20");
        NodeList nl = xml.getElementsByTagName("Error");
        Element e = (Element) nl.item(0);
        assertNotNull(e);
        assertTrue(e.getAttributes().getNamedItem("type").getNodeValue().equals("InvalidDate"));
    }

    @Test
    public void XMLBuilder_should_create_notFoundXML() throws Exception {
        Document xml = XMLBuilder.createNotFoundXML("2020-12-20");
        NodeList nl = xml.getElementsByTagName("Error");
        Element e = (Element) nl.item(0);
        assertNotNull(e);
        assertTrue(e.getAttributes().getNamedItem("type").getNodeValue().equals("DateNotFound"));
    }

}
