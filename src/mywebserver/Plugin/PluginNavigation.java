package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.Navigation.*;
import mywebserver.SAX.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginNavigation implements Plugin {

    private Logger LOGGER = Logger.getLogger(PluginNavigation.class.getName());
    private XMLParserSAX parser = null;
    private StreetCollection streetCollection;
    private String contentString;
    private Response resp;
    private boolean isValid = false;

    @Override
    public float canHandle(Request req) {
        float score = PluginUtil.calcScore(this.getClass(), req);
        return score;
    }

    @Override
    public Response handle(Request req) {
        resp = new ResponseImpl();
        contentString = req.getContentString();
        streetCollection = StreetCollection.newStreetCollectionInstance();
        boolean isInitialized = true;
        try {
            if (contentString != null) {
                contentString = URLDecoder.decode(contentString, StandardCharsets.UTF_8.name());
                String[] bodyVal = contentString.split("&");
                String[] value = bodyVal[0].split("=");
                String load = bodyVal[1].split("=")[1];
                boolean isLoad = load.equals("True");
                if (!isLoad) {
                    if (value.length > 1) {
                        if (streetCollection.isInitialized()) {
                            // check if lock is owned by another thread
                            if (streetCollection.setLock()) {
                                // if not - release it since no parsing here
                                streetCollection.unLock();
                                LinkedList<String> cities = streetCollection.getCitiesOfStreet(value[1]);
                                if (cities != null) {
                                    isValid = true;
                                    prepareResponse(cities, value[1]);
                                }
                            }
                        } else if (!streetCollection.isInitialized()) {
                            resp.setContent("Error: Straßen sind nicht verfügbar");
                            isInitialized = false;
                        }
                    }
                } else if (isLoad) {
                    isInitialized = false;
                    streetCollection = XMLParserSAX.parseXML();
                    if (streetCollection != null) {
                        resp.setContent("Straßen neu aufbereitet");
                    }
                }
            }
            if (!isValid && isInitialized) {
                resp.setContent("Not found: Keine Städte gefunden");
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        } catch (SAXException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            resp.setContent("Warning: Parser is busy right now");
            resp.setStatusCode(200);
            return resp;
        } finally {
            // try to release a lock
            if (parser != null) parser.releaseLock();
        }
        resp.setContentType("text/plain");
        resp.setStatusCode(200);
        return resp;
    }

    public void prepareResponse(LinkedList<String> cities, String street) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Street: " + street);
        stringBuilder.append(System.getProperty("line.separator"));
        for (String s : cities) {
            stringBuilder.append(s);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        resp.setContent(stringBuilder.toString());
    }
}
