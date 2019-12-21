package mywebserver.DAO;

import mywebserver.Temperature.Temperature;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

/**
 * Class for providing access to the underlying database.
 */
public class TemperatureDao {

    private PreparedStatement preparedStatement;

    /**
     * Create a table for Temperature data storage
     *
     * @param con The connection for database access
     * @throws SQLException
     */
    public void createTable(Connection con) throws SQLException {
        String preStatement = "CREATE TABLE IF NOT EXISTS temperature(\n" +
                "id SERIAL PRIMARY KEY,\n" +
                "day DATE NOT NULL,\n" +
                "temp REAL NOT NULL\n" +
                ")";
        preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.execute();
    }

    /**
     * Returns the PostgresConManager instance
     *
     * @param con The connection for database access
     * @param obj The temperature object which attributes should be stored
     * @return the row count for SQL Data Manipulation Language (DML)
     * statements or 0 for SQL statements that return nothing
     */
    public int insertTemp(Connection con, Temperature obj) throws SQLException {
        String preStatement = "INSERT INTO temperature (day, temp) VALUES (?, ?)";
        preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.setDate(1, Date.valueOf(obj.getDate()));
        preparedStatement.setFloat(2, obj.getTemp());
        return preparedStatement.executeUpdate();
    }

    /**
     * Get all temperature data stored in the 'temperature' table
     *
     * @param con The connection for database access
     * @return A list of temperature objects, representing data stored in the 'temperature' table
     */
    public LinkedList<Temperature> getAllTemperature(Connection con) throws SQLException {
        LinkedList<Temperature> data = new LinkedList<>();
        String preStatement = "SELECT * FROM temperature ORDER BY day ASC";
        preparedStatement = con.prepareStatement(preStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Temperature obj = new Temperature();
            obj.setId(rs.getInt(1));
            obj.setDate(rs.getObject(2, LocalDate.class));
            obj.setTemp(rs.getFloat(3));
            data.add(obj);
        }
        return data;
    }

    /**
     * Get all temperature data of a specific day stored in the 'temperature' table
     *
     * @param con  The connection for database access
     * @param date The date to be used for querying specific data
     * @return A list of temperature objects, representing data stored in the 'temperature' table
     */
    public LinkedList<Temperature> getTemperatureOfDate(Connection con, LocalDate date) throws SQLException {
        LinkedList<Temperature> data = new LinkedList<>();
        String preStatement = "SELECT * FROM temperature WHERE day = ? ORDER BY day ASC";
        preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.setObject(1, date);
        ResultSet rs = preparedStatement.executeQuery();
        // check if data exists
        if (!rs.isBeforeFirst()) {
            return null;
        }
        while (rs.next()) {
            Temperature obj = new Temperature();
            obj.setId(rs.getInt(1));
            obj.setDate(rs.getObject(2, LocalDate.class));
            obj.setTemp(rs.getFloat(3));
            data.add(obj);
        }
        return data;
    }

    /**
     * Close a prepared Statement
     *
     * @throws SQLException
     */
    public void closeStatement() throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
    }

}
