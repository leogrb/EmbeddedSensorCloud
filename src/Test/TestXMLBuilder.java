package Test;

import mywebserver.Temperature.Temperature;
import mywebserver.XML.XMLBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestXMLBuilder {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void XMLBuilder_should_create_validXML() throws Exception {
        LinkedList<Temperature> data = new LinkedList<>();
        Temperature temp = new Temperature(LocalDate.now(), 99.9f);
        assertNotNull(temp);
        temp.setId(1);
        data.add(temp);
        Document xml = XMLBuilder.createValidXML(data);
        NodeList nl = xml.getElementsByTagName("temperature");
        Element e = (Element) nl.item(0);
        assertNotNull(e);
        assertTrue(e.getAttributes().getNamedItem("id").getNodeValue().equals(Integer.toString(1)));
        NodeList date = e.getElementsByTagName("date");
        NodeList t = e.getElementsByTagName("temp");
        assertTrue(date.item(0).getFirstChild().getTextContent().equals(LocalDate.now().toString()));
        assertTrue(t.item(0).getFirstChild().getTextContent().equals(Float.toString(99.9f)));
    }

    @Test
    public void XMLBuilder_should_create_invalidDateXML() throws Exception {
        Document xml = XMLBuilder.createInvalidDateXML("202020-12-20");
        NodeList nl = xml.getElementsByTagName("Error");
        Element e = (Element) nl.item(0);
        assertNotNull(e);
        assertTrue(e.getAttributes().getNamedItem("type").getNodeValue().equals("InvalidDate"));
    }

    @Test
    public void XMLBuilder_should_create_notFoundXML() throws Exception {
        Document xml = XMLBuilder.createNotFoundXML("2020-12-20");
        NodeList nl = xml.getElementsByTagName("Error");
        Element e = (Element) nl.item(0);
        assertNotNull(e);
        assertTrue(e.getAttributes().getNamedItem("type").getNodeValue().equals("DateNotFound"));
    }
}
