package mywebserver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Exception;
import java.lang.String;
import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private ServerSocket serverSocket;
    private PostgresConManager postgresConManager;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port); //listen on port
    }

    public void shutdown() throws SQLException {
        //postgresConManager.closeConnections();
    }

    public void run() throws IOException, NullPointerException, SQLException {
        /*postgresConManager = postgresConManager.newPCNInstance();
        postgresConManager.initialize();
        TemperatureDao temperatureDao = new TemperatureDao();
        temperatureDao.createTable(postgresConManager.getConnectionFromPool());
        Sensorthread sensor = new Sensorthread();
        Thread threadSens = new Thread(sensor);
        LOGGER.log(Level.INFO, "Starting Sensorreader");
        threadSens.start();*/

        while (true) {
            Socket cl = this.serverSocket.accept(); //accept connection and create socket obj
            Serverthread client = new Serverthread(cl);
            Thread threadCl = new Thread(client);
            threadCl.start();
        }
    }
}

