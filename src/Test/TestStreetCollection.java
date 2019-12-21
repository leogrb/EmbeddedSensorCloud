package Test;

import mywebserver.Navigation.Node;
import mywebserver.Navigation.StreetCollection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestStreetCollection {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Test
    public void handleNode_should_add_street() {
        StreetCollection s = StreetCollection.newStreetCollectionInstance();
        assertNotNull("Class should return new object", s);
        Node n = new Node("Wien", "Hauptstraße");
        s.handleNode(n);
        boolean isInit = s.isInitialized();
        assertTrue("Street Map should not be emtpy", isInit);
    }

    @Test
    public void get_cities_of_street_should_return_cities() {
        StreetCollection s = StreetCollection.newStreetCollectionInstance();
        Node n = new Node("Wien", "Hauptstraße");
        Node n2 = new Node("Bad Vöslau", "Hauptstraße");
        s.handleNode(n);
        s.handleNode(n2);
        LinkedList<String> c = s.getCitiesOfStreet("Hauptstraße");
        assertNotNull("List is null", c);
        assertTrue(c.size() == 2);
    }

}
