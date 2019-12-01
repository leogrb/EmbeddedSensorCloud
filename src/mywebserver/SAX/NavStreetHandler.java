package mywebserver.SAX;

import mywebserver.Navigation.Node;
import mywebserver.Navigation.StreetCollection;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NavStreetHandler extends DefaultHandler {
    private String elementValue;
    private String city;
    private String street;
    private int attrCount = 0;
    private Node node;
    private boolean isNode;
    private StreetCollection sColl = StreetCollection.newStreetCollectionInstance();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        elementValue = new String(ch, start, length);
    }

    @Override
    // Methode wird aufgerufen wenn der Parser zu einem Start-Tag kommt
    public void startElement(String uri, String localName, String qName,
                             Attributes atts) throws SAXException {
        if (qName.equals("node")) {
            isNode = true;
        }
        if (isNode && qName.equals("tag")) {
            String key = atts.getValue("k");
            if (key.equals("addr:city")) {
                attrCount++;
                city = atts.getValue("v");
            }
            if (key.equals("addr:street")) {
                attrCount++;
                street = atts.getValue("v");
            }
        }
    }

    // Methode wird aufgerufen wenn der Parser zu einem End-Tag kommt
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("node")) {
            // check if city and street belong to same node
            if (attrCount == 2) {
                node = new Node(city, street);
                sColl.handleNode(node);
            }
            attrCount = 0;
            isNode = false;
        }
    }

    public StreetCollection getsColl() {
        return sColl;
    }
}

