package mywebserver.DAO;


import mywebserver.Config;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresConManager {
    private final static Logger LOGGER = Logger.getLogger(PostgresConManager.class.getName());

    private static PostgresConManager PCN = null;
    private String driver = null;
    private final String url = "jdbc:postgresql://localhost/embeddedsensorcloud";
    private String userName = null;
    private String password = null;
    private final static int INIT_MAX_POOL_SIZE = 5;
    private Vector<Connection> connectionPool = new Vector<>();


    private PostgresConManager()
    {
        this.userName = Config.newInstance().getProp("username");
        this.password = Config.newInstance().getProp("password");
        this.driver = Config.newInstance().getProp("driver");
    }

    public void initialize() {
        //Here we can initialize all the information that we need
        initializeConnectionPool();
    }

    // make object shareable
    public static synchronized PostgresConManager newPCNInstance() {
        if (PCN != null) {
        } else {
            PCN = new PostgresConManager();
        }
        return PCN;
    }

    private void initializeConnectionPool() {
        while (!checkIfConnectionPoolIsFull()) {
            LOGGER.log(Level.INFO, "Connection Pool is NOT full. Proceeding with adding new connections");
            //Adding new connection instance until the pool is full
            connectionPool.addElement(createNewConnectionForPool());
        }
        LOGGER.log(Level.INFO, "Connection Pool is full.");
    }

    private synchronized boolean checkIfConnectionPoolIsFull() {

        //Check if pool size is full
        if (connectionPool.size() < INIT_MAX_POOL_SIZE) {
            return false;
        }

        return true;
    }

    //Creating a connection
    private Connection createNewConnectionForPool() {
        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
            LOGGER.log(Level.INFO, "New DB connection established");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
        }

        return connection;
    }

    public synchronized Connection getConnectionFromPool() {
        Connection connection = null;

        //Check if there is a connection available. There are times when all the connections in the pool may be used up
        if (connectionPool.size() > 0) {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        // if no connection available establish new connection
        else {
            connection = createNewConnectionForPool();
        }
        //Giving away the connection from the connection pool
        return connection;
    }

    public synchronized void returnConnectionToPool(Connection connection) {
        //Adding the connection from the client back to the connection pool
        connectionPool.addElement(connection);
    }

    public synchronized void closeConnections() throws SQLException {
        for (Connection con : connectionPool) {
            LOGGER.log(Level.INFO, "Closing connection " + con);
            con.close();
        }
    }
}
