package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.PostgresConManager;
import mywebserver.Temperature;
import mywebserver.TemperatureDao;
import mywebserver.UrlImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginTemperature implements Plugin {
    private Logger LOGGER = Logger.getLogger(PluginTemperature.class.getName());

    private static PostgresConManager PCN = PostgresConManager.newPCNInstance();
    private TemperatureDao temperatureDao = new TemperatureDao();
    private Connection con = null;

    @Override
    public float canHandle(Request req) {
        return PluginUtil.calcScore(this.getClass(), req);
    }

    @Override
    public Response handle(Request req) {
        ResponseImpl resp = new ResponseImpl();
        Url url = req.getUrl();
        String contentString = req.getContentString();
        String[] segments = url.getSegments();
        con = PCN.getConnectionFromPool();
        // check which temperature request is given
        try {
            if (segments[segments.length - 1].substring(0, 11).equals("temperature")) {
                if (url.getParameterCount() > 0) {
                    Map<String, String> params = url.getParameter();
                    LocalDate date = LocalDate.parse(params.get("value"));
                    LinkedList<Temperature> data = temperatureDao.getTemperatureOfDate(con, date);
                    if (data != null) {
                        Document xml = createXML(data);
                        resp.setContent(transformXML(xml));
                    } else {
                        resp.setContent("Error: No data was found");
                    }
                } else {
                    resp.setContent("Error: Date empty");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } catch (TransformerException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } finally {
            PCN.returnConnectionToPool(con);
            try {
                PCN.closeConnections();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        resp.setContentType("text/plain");
        resp.setStatusCode(200);
        return resp;
    }


    public Document createXML(LinkedList<Temperature> data) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.newDocument();
        // root
        Element root = xml.createElement("temperatures");
        xml.appendChild(root);
        if (data != null) {
            for (Temperature t : data) {
                // temperature element
                Element c1 = xml.createElement("temperature");
                // temperature attribute(id)
                Attr attr = xml.createAttribute("id");
                attr.setValue(Integer.toString(t.getId()));
                c1.setAttributeNode(attr);
                root.appendChild(c1);
                // temperature element(date)
                Element c2 = xml.createElement("date");
                c2.appendChild(xml.createTextNode(t.getDate().toString()));
                c1.appendChild(c2);
                //temperature element(temp)
                Element c3 = xml.createElement("temp");
                c3.appendChild(xml.createTextNode(Float.toString(t.getTemp())));
                c1.appendChild(c3);
            }
        }
        /*Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(new File("output.xml"));
        Source input = new DOMSource(xml);

        transformer.transform(input, output);*/
        return xml;
    }

    public String transformXML(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        transformer = tf.newTransformer();

        // Uncomment if you do not require XML declaration
        // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        //A character stream that collects its output in a string buffer,
        //which can then be used to construct a string.
        StringWriter writer = new StringWriter();

        //transform document to string
        transformer.transform(new DOMSource(doc), new StreamResult(writer));

        return writer.getBuffer().toString();
    }
    /*public static void main(String[] args) {
        PluginTemperature p = new PluginTemperature();
        LinkedList <Temperature> l = new LinkedList<>();
        Temperature t = new Temperature(LocalDate.now(), 2.0f);
        t.setId(1);
        Temperature t2 = new Temperature(LocalDate.now(), 3.0f);
        t2.setId(2);
        l.add(t);
        l.add(t2);
        try {
            p.createXML(l);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }*/
}
