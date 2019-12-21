package Test;

import mywebserver.DAO.PostgresConManager;
import mywebserver.DAO.TemperatureDao;
import mywebserver.Temperature.Temperature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class TestTemperatureDao {
    @BeforeClass
    public static void setUpBeforeClass() {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        PCN.initialize();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        PCN.closeConnections();
    }

    @Test
    public void TemperatureDao_should_insert_temperature() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        Connection con = PCN.getConnectionFromPool();
        assertNotNull("PCN returned null", con);
        TemperatureDao temperatureDao = new TemperatureDao();
        temperatureDao.createTable(con);
        Temperature temp = new Temperature(LocalDate.now(), 99.9f);
        int res = temperatureDao.insertTemp(con, temp);
        assertEquals(res, 1);
        String preStatement = "SELECT temp FROM temperature WHERE day = ? ORDER BY day ASC";
        PreparedStatement preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.setObject(1, LocalDate.now());
        ResultSet rs = preparedStatement.executeQuery();
        assertTrue("Insert error", rs.isBeforeFirst());
        rs.next();
        assertEquals("Insert error", rs.getFloat(1), 99.9f, 0f);
        PCN.returnConnectionToPool(con);
    }

    @Test
    public void TemperatureDao_should_return_50_entries() throws Exception {
        PostgresConManager PCN = PostgresConManager.newPCNInstance();
        Connection con = PCN.getConnectionFromPool();
        assertNotNull("PCN returned null", con);
        TemperatureDao temperatureDao = new TemperatureDao();
        temperatureDao.createTable(con);
        LinkedList<Temperature> data = temperatureDao.getAllTemperature(con);
        assertTrue("50 entries have to be returned", data.size() == 50);
        PCN.returnConnectionToPool(con);
    }
}
