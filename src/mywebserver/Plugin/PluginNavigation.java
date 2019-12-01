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
    private XMLParserSAX parser;
    private StreetCollection streetCollection;
    private String contentString;
    private Response resp;
    boolean isValid = false;
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
        try {
            if (contentString != null) {
                contentString = URLDecoder.decode(contentString, StandardCharsets.UTF_8.name());
                String[] bodyVal = contentString.split("=");
                if (bodyVal.length > 1) {
                    if(!streetCollection.isInitialized()) {
                        streetCollection = XMLParserSAX.parseXML();
                    }
                    LinkedList<String> cities = streetCollection.getCitiesOfStreet(bodyVal[1]);
                    if (cities != null) {
                        isValid = true;
                        prepareResponse(cities, bodyVal[1]);
                    }
                }
            }
            if(!isValid){
                resp.setContent("Not found: Keine Städte gefunden");
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        resp.setContentType("text/plain");
        resp.setStatusCode(200);
        return resp;
    }

    public void prepareResponse(LinkedList<String> cities, String street){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Street: " + street);
        stringBuilder.append(System.getProperty("line.separator"));
        for(String s : cities){
            stringBuilder.append(s);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        resp.setContent(stringBuilder.toString());
    }
}
