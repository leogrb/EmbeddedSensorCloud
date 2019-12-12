package Test;

import static org.junit.Assert.*;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.DAO.PostgresConManager;
import mywebserver.DAO.TemperatureDao;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginStatic;
import mywebserver.Plugin.PluginTemperature;
import mywebserver.Plugin.PluginToLower;
import mywebserver.Sensor.Sensorthread;
import mywebserver.XML.XMLBuilder;
import mywebserver.XML.XMLTransformer;
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



    /*@Test
    public void Sensor_should_create_Temperature() throws Exception{
        Sensorthread sensor = new Sensorthread();
        Temperature temp = sensor.createRandomTempObj(LocalDate.now());
        assertNotNull("Sensor returned null", temp);
        assertTrue("Sensor returned Temperature out of range", temp.getTemp() >= -20f && temp.getTemp() <= 50f);
    }
    */

}
