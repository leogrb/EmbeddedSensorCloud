package mywebserver;


import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgresConManager {
    private final static Logger LOGGER = Logger.getLogger(PostgresConManager.class.getName());

    private final String driver = "org.postgresql.Driver";

    private final String userName = "postgres";
    private final String password = "leo";
    private final String url = "jdbc:postgresql://localhost/embeddedsensorcloud";
    private final static int MAX_POOL_SIZE = 5;
    private Vector<Connection> connectionPool = new Vector<>();

    public PostgresConManager()
    {
        initialize();
    }

    /*public PostgresConManager(String databaseUrl, String userName, String password)
    {
        this.url = databaseUrl;
        this.userName = userName;
        this.password = password;
        initialize();
    }
*/
    private void initialize()
    {
        //Here we can initialize all the information that we need
        initializeConnectionPool();
    }

    private void initializeConnectionPool()
    {
        while(!checkIfConnectionPoolIsFull())
        {
            LOGGER.log(Level.INFO, "Connection Pool is NOT full. Proceeding with adding new connections");
            //Adding new connection instance until the pool is full
            connectionPool.addElement(createNewConnectionForPool());
        }
        LOGGER.log(Level.INFO, "Connection Pool is full.");
    }

    private synchronized boolean checkIfConnectionPoolIsFull()
    {

        //Check if pool size is full
        if(connectionPool.size() < MAX_POOL_SIZE)
        {
            return false;
        }

        return true;
    }

    //Creating a connection
    private Connection createNewConnectionForPool()
    {
        Connection connection = null;

        try
        {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, userName, password);
            LOGGER.log(Level.INFO, "New DB connection established");
        }
        catch(SQLException e)
        {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
            return null;
        }
        catch(ClassNotFoundException e)
        {
            LOGGER.log(Level.WARNING, "Unexpected Error: " + e.getMessage(), e);
            return null;
        }

        return connection;
    }

    public synchronized Connection getConnectionFromPool()
    {
        Connection connection = null;

        //Check if there is a connection available. There are times when all the connections in the pool may be used up
        if(connectionPool.size() > 0)
        {
            connection = (Connection) connectionPool.firstElement();
            connectionPool.removeElementAt(0);
        }
        //Giving away the connection from the connection pool
        return connection;
    }

    public synchronized void returnConnectionToPool(Connection connection)
    {
        //Adding the connection from the client back to the connection pool
        connectionPool.addElement(connection);
    }

    public synchronized void closeConnections() throws SQLException {
        for(Connection con : connectionPool){
            LOGGER.log(Level.INFO, "Closing connection " + con);
            con.close();
        }
    }

    public static void main(String args[])
    {
        PostgresConManager ConManager = new PostgresConManager();
        try {
            ConManager.closeConnections();
        } catch (SQLException e){

        }
    }
}
