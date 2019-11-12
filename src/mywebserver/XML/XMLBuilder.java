package mywebserver.XML;

import mywebserver.Temperature;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.LinkedList;

public class XMLBuilder {

    public static Document createValidXML(LinkedList<Temperature> data) throws ParserConfigurationException {
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
        return xml;
    }

    public static Document createInvalidDateXML(String date) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.newDocument();
        // root
        Element root = xml.createElement("Error");
        root.setAttribute("type", "InvalidDate");
        xml.appendChild(root);
        Element warn = xml.createElement("Warning");
        warn.appendChild(xml.createTextNode("Invalid Date: " + date));
        Element resp = xml.createElement("Response");
        resp.appendChild(xml.createTextNode("Requested Date is invalid. Valid DateFormat is: yyyy-mm-dd"));
        root.appendChild(warn);
        root.appendChild(resp);
        return xml;
    }

    public static Document createNotFoundXML(String date) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document xml = db.newDocument();
        // root
        Element root = xml.createElement("Error");
        root.setAttribute("type", "DateNotFound");
        xml.appendChild(root);
        Element warn = xml.createElement("Warning");
        warn.appendChild(xml.createTextNode("Date not found: " + date));
        Element resp = xml.createElement("Response");
        resp.appendChild(xml.createTextNode("No data for " + date + " available"));
        root.appendChild(warn);
        root.appendChild(resp);
        return xml;
    }

}
