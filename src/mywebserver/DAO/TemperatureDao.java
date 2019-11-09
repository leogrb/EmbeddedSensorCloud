package mywebserver.DAO;

import mywebserver.Temperature;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;

public class TemperatureDao {

    private PreparedStatement preparedStatement;

    public void createTable(Connection con) throws SQLException {
        String preStatement = "CREATE TABLE IF NOT EXISTS temperature(\n" +
                "id SERIAL PRIMARY KEY,\n" +
                "day DATE NOT NULL,\n" +
                "temp REAL NOT NULL\n" +
                ")";
        preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.execute();
    }

    public int insertTemp(Connection con, Temperature obj) throws SQLException {
        String preStatement = "INSERT INTO temperature (day, temp) VALUES (?, ?)";
        preparedStatement = con.prepareStatement(preStatement);
        preparedStatement.setDate(1, Date.valueOf(obj.getDate()));
        preparedStatement.setFloat(2, obj.getTemp());
        return preparedStatement.executeUpdate();
    }

    //limit 50 for testing
    public LinkedList<Temperature> getAllTemperature(Connection con) throws SQLException {
        LinkedList<Temperature> data = new LinkedList<>();
        String preStatement = "SELECT * FROM temperature ORDER BY day ASC LIMIT 50";
        preparedStatement = con.prepareStatement(preStatement);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Temperature obj = new Temperature();
            obj.setId(1);
            obj.setDate(rs.getObject(2, LocalDate.class));
            obj.setTemp(rs.getFloat(3));
            data.add(obj);
        }
        return data;
    }

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

}
