package mywebserver.Sensor;

import mywebserver.DAO.PostgresConManager;
import mywebserver.DAO.TemperatureDao;
import mywebserver.Temperature;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sensorthread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Sensorthread.class.getName());
    private Connection con = null;
    private PostgresConManager PCN;
    private TemperatureDao temperatureDao;
    private static final float MINTEMP = -20f;
    private static final float MAXTEMP = 50f;
    private Random r;
    private LocalDate cur;
    private boolean isTestData = true;
    private long numYears = 10;

    @Override
    public void run() {
        cur = LocalDate.now();
        PCN = PostgresConManager.newPCNInstance();
        con = PCN.getConnectionFromPool();
        r = new Random();
        temperatureDao = new TemperatureDao();
        try {
            if (!isTestData) {
                createTestData();
            }
            // read temperature data
            while (true) {
                temperatureDao.insertTemp(con, createRandomTempObj(cur));
                Thread.sleep(10000);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "SQL error: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Unexpected error: " + e.getMessage(), e);
        } finally {
            PCN.returnConnectionToPool(con);
        }

    }

    public Temperature createRandomTempObj(LocalDate date) {
        float temp = MINTEMP + r.nextFloat() * (MAXTEMP - MINTEMP);
        Temperature tempObj = new Temperature(date, temp);
        return tempObj;
    }

    // generate test data over last 10 years
    public void createTestData() throws SQLException {
        int count = 0;
        int idxY = cur.getYear() - (int) numYears;
        while (idxY <= cur.getYear()) {
            if (idxY != cur.getYear()) {
                for (int idxM = 1; idxM <= Months.getSize(); idxM++) {
                    for (int idxD = 1; idxD <= Months.getnumOfDays(idxM); idxD++) {
                        // create 3 datasets for each day
                        for (int i = 0; i < 3; i++) {
                            temperatureDao.insertTemp(con, createRandomTempObj(LocalDate.of(idxY, idxM, idxD)));
                        }
                    }
                }
            } else {
                for (int idxM = 1; idxM <= Months.getSize() && idxM <= cur.getMonthValue(); idxM++) {
                    for (int idxD = 1; idxD <= Months.getnumOfDays(idxM) && idxD <= cur.getDayOfMonth(); idxD++) {
                        // create 3 datasets for each day
                        for (int i = 0; i < 3; i++) {
                            temperatureDao.insertTemp(con, createRandomTempObj(LocalDate.of(idxY, idxM, idxD)));
                        }
                    }
                }
            }
            idxY++;
        }
    }
}
