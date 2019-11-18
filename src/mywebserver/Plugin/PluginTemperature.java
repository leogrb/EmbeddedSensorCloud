package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.DAO.PostgresConManager;
import mywebserver.Temperature;
import mywebserver.DAO.TemperatureDao;
import mywebserver.XML.XMLBuilder;
import mywebserver.XML.XMLTransformer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginTemperature implements Plugin {
    private Logger LOGGER = Logger.getLogger(PluginTemperature.class.getName());

    private static PostgresConManager PCN = PostgresConManager.newPCNInstance();
    private TemperatureDao temperatureDao = new TemperatureDao();
    private Connection con = null;
    private LinkedList<Temperature> data = null;

    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        String[] segments = req.getUrl().getSegments();
        for (String s : segments) {
            if (s.equals("GetTemperature")) {
                score += 0.5f;
            }
        }
        return score;

    }

    @Override
    public Response handle(Request req) {
        ResponseImpl resp = new ResponseImpl();
        Url url = req.getUrl();
        String contentString = req.getContentString();
        String[] segments = url.getSegments();
        con = PCN.getConnectionFromPool()   ;
        String date = "0000-00-00";
        boolean isValidDate = false;
        // check which temperature request is given
        try {
            if (segments.length > 3) {
                if (segments[segments.length - 4].equals("GetTemperature")) {
                    date = segments[segments.length - 3] + "-" + segments[segments.length - 2] + "-" + segments[segments.length - 1];
                    LocalDate reqDate = parseDate(date);
                    if (reqDate != null) {
                        isValidDate = true;
                        data = temperatureDao.getTemperatureOfDate(con, reqDate);
                    }
                }
            } else if (segments.length == 1) {
                if (segments[segments.length - 1].substring(0, 11).equals("temperature")) {
                    if (url.getParameterCount() > 0) {
                        Map<String, String> params = url.getParameter();
                        date = params.get("value");
                        LocalDate reqDate = parseDate(date);
                        if (reqDate != null) {
                            isValidDate = true;
                            data = temperatureDao.getTemperatureOfDate(con, reqDate);
                        }
                    } else {
                        isValidDate = true;
                        data = temperatureDao.getAllTemperature(con);
                    }
                }
            }
            //TODO: Invalid Get Request aka GetTemperature/2019
            Document xml;
            if (data != null) {
                xml = XMLBuilder.createValidXML(data);
            } else if (!isValidDate) {
                xml = XMLBuilder.createInvalidDateXML(date);
            } else {
                xml = XMLBuilder.createNotFoundXML(date);
            }
            resp.setContent(XMLTransformer.transformXML(xml));
            resp.setContentType("application/xml");
            resp.setStatusCode(200);
             /*else {
                resp.setContent("Error: No data was found at that date");
            }*/
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
        return resp;
    }

    public LocalDate parseDate(String date) {
        LocalDate Date = null;
        try {
            Date = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            LOGGER.log(Level.WARNING, "Selected Date is invalid");
        } finally {
            return Date;
        }
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
