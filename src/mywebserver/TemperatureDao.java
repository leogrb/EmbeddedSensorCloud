package mywebserver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
