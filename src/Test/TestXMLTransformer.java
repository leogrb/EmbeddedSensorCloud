package Test;

import mywebserver.Temperature.Temperature;
import mywebserver.XML.XMLBuilder;
import mywebserver.XML.XMLTransformer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestXMLTransformer {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void XMLTransformer_XMLtoString() throws Exception {
        LinkedList<Temperature> data = new LinkedList<>();
        Temperature temp = new Temperature(LocalDate.now(), 99.9f);
        assertNotNull(temp);
        temp.setId(1);
        data.add(temp);
        String xmlString = XMLTransformer.transformXML(XMLBuilder.createValidXML(data));
        assertNotNull("String is null", xmlString);
        assertTrue("Incorrect string", xmlString.equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><temperatures><temperature id=\"1\"><date>" + LocalDate.now()+"</date><temp>99.9</temp></temperature></temperatures>"));
    }
}
