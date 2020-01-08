package Test;

import mywebserver.DAO.PostgresConManager;
import mywebserver.Properties.Config;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestPostgresConManager {
    @BeforeClass
    public static void setUpBeforeClass() {
        Config config = Config.newInstance();
        config.initialize();
        config.setProps("username", "postgres");
        config.setProps("password", "leo");
        config.setProps("driver", "org.postgresql.Driver");
        config.setProps("port", "80");
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        PCN.initialize();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        PCN.closeConnections();
    }
    @Test
    public void PCN_should_return_connections() {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        Connection con = PCN.getConnectionFromPool();
        Connection con2 = PCN.getConnectionFromPool();
        Connection con3 = PCN.getConnectionFromPool();
        Connection con4 = PCN.getConnectionFromPool();
        Connection con5 = PCN.getConnectionFromPool();

        assertTrue(con instanceof Connection);
        assertNotNull("PCN returned null", con);
        assertTrue(con2 instanceof Connection);
        assertNotNull("PCN returned null", con2);
        assertTrue(con3 instanceof Connection);
        assertNotNull("PCN returned null", con3);
        assertTrue(con4 instanceof Connection);
        assertNotNull("PCN returned null", con4);
        assertTrue(con5 instanceof Connection);
        assertNotNull("PCN returned null", con5);

        PCN.returnConnectionToPool(con);
        PCN.returnConnectionToPool(con2);
        PCN.returnConnectionToPool(con3);
        PCN.returnConnectionToPool(con4);
        PCN.returnConnectionToPool(con5);
    }
}
