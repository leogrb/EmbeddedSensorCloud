package mywebserver.SAX;

import mywebserver.Navigation.StreetCollection;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLParserSAX {
    private static Logger LOGGER = Logger.getLogger(XMLParserSAX.class.getName());
    private static NavStreetHandler handler = null;

    public static StreetCollection parseXML() throws ParserConfigurationException, SAXException, IOException, IllegalAccessException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "data_.xml";
        SAXParser saxParser = saxParserFactory.newSAXParser();
        handler = new NavStreetHandler();
        // try to lock collection
        handler.tryLock();
        LOGGER.log(Level.INFO, "Initializing XML parsing");
        saxParser.parse(new File(path), handler);
        //print employee information
        LOGGER.log(Level.INFO, "XML parsing finished");
        handler.unLock();
        return handler.getsColl();
    }

    public static void releaseLock() {
        if (handler != null) {
            handler.unLock();
        }
    }

   /* public static void main(String[] args) {
        try {
            StreetCollection s = XMLParserSAX.parseXML();
            for (String a : s.getCitiesOfStreet("Hauptstraße")) {
                System.out.println(a);
            }
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "Unexpected Exception", e.getMessage());
        } catch (SAXException e) {
            LOGGER.log(Level.WARNING, "Unexpected Exception", e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unexpected Exception", e.getMessage());
        }
    }*/
}

