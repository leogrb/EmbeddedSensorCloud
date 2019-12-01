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

    public static StreetCollection parseXML() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "data_.xml";
        SAXParser saxParser = saxParserFactory.newSAXParser();
        NavStreetHandler handler = new NavStreetHandler();
        LOGGER.log(Level.INFO, "Initializing XML parsing");
        saxParser.parse(new File(path), handler);
        //print employee information
        return handler.getsColl();
    }

    public static void main(String[] args) {
        try {
            StreetCollection s = XMLParserSAX.parseXML();
            for (String a : s.getCitiesOfStreet("Hauptstraße")) {
                System.out.println(a);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

