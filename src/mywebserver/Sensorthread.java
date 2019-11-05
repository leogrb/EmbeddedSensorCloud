package mywebserver;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sensorthread implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(Sensorthread.class.getName());
    private Connection con = null;
    private PostgresConManager PCN;
    private TemperatureDao temperatureDao;
    private static final float MINTEMP = -20f;
    private static final float MAXTEMP = 50f;
    @Override
    public void run() {
        PCN = PostgresConManager.newPCNInstance();
        con = PCN.getConnectionFromPool();
        Random r = new Random();
        temperatureDao = new TemperatureDao();
        // create temperature data
        while(true){
            float temp = MINTEMP + r.nextFloat() * (MAXTEMP - MINTEMP);
            LocalDate date = LocalDate.now();
            Temperature tempObj = new Temperature(date, temp);
            try {
                temperatureDao.insertTemp(con, tempObj);
                Thread.sleep(10000);
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "SQL error: " + e.getMessage(), e);
            } catch (InterruptedException e){
                LOGGER.log(Level.WARNING, "Unexpected error: " + e.getMessage(), e);
            } finally {
                PCN.returnConnectionToPool(con);
            }
        }

    }
}
