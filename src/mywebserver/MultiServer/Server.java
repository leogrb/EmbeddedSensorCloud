package mywebserver.MultiServer;

import mywebserver.DAO.PostgresConManager;
import mywebserver.DAO.TemperatureDao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private ServerSocket serverSocket;
    private static PostgresConManager PCN = PostgresConManager.newPCNInstance();
    private TemperatureDao temperatureDao = new TemperatureDao();
    private Connection con = null;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port); //listen on port
    }

    public void shutdown() throws SQLException, IOException {
        //postgresConManager.closeConnections();
        if(serverSocket != null){
            serverSocket.close();
        }
        temperatureDao.closeStatement();
    }

    public void run() throws IOException, NullPointerException, SQLException {
        /*PCN = PostgresConManager.newPCNInstance();
        PCN.initialize();
        con = PCN.getConnectionFromPool();
        temperatureDao.createTable(con);
        PCN.returnConnectionToPool(con);
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

