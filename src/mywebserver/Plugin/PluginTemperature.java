package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import BIF.SWE1.interfaces.Url;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.DAO.PostgresConManager;
import mywebserver.Temperature.Temperature;
import mywebserver.DAO.TemperatureDao;
import mywebserver.XML.XMLBuilder;
import mywebserver.XML.XMLTransformer;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
    private ResponseImpl resp = null;
    private boolean isGetReq = false;
    private boolean isRestReq = false;

    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        int c = 0;
        String[] segments = req.getUrl().getSegments();
        if (segments.length > 1) {
            if (segments.length == 4) {
                for (String s : segments) {
                    if (s.equals("GetTemperature")) {
                        c++;
                        score += 0.5f;
                    }
                }
                if (c != 1) {
                    return 0f;
                } else {
                    isRestReq = true;
                }
            } else return 0f;
        } else if (segments.length == 1 && segments[0].contains("temperature?value=")) {
            score += 0.5f;
            isGetReq = true;
        } else {
            return 0f;
        }
        return score;

    }

    @Override
    public Response handle(Request req) {
        resp = new ResponseImpl();
        Url url = req.getUrl();
        String contentString = req.getContentString();
        String[] segments = url.getSegments();
        con = PCN.getConnectionFromPool();
        String date = "0000-00-00";
        boolean isValidDate = false;

        // check which temperature request is given
        try {
            if (isRestReq) {
                date = segments[segments.length - 3] + "-" + segments[segments.length - 2] + "-" + segments[segments.length - 1];
                LocalDate reqDate = parseDate(date);
                if (reqDate != null) {
                    isValidDate = true;
                    data = temperatureDao.getTemperatureOfDate(con, reqDate);
                }
            } else if (isGetReq) {
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

            Document xml;
            if (data != null) {
                xml = XMLBuilder.createValidXML(data);
            } else if (!isValidDate) {
                xml = XMLBuilder.createInvalidDateXML(date);
            } else {
                xml = XMLBuilder.createNotFoundXML(date);
            }
            resp.setMimeType("xml");
            resp.setContentType(resp.getMimeType());
            resp.setContent(XMLTransformer.transformXML(xml));
            resp.setStatusCode(200);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } catch (TransformerException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } finally {
            PCN.returnConnectionToPool(con);
            try {
                temperatureDao.closeStatement();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
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

}
