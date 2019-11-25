package mywebserver.Main;

import mywebserver.Config;
import mywebserver.MultiServer.Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static int port;

    public static void main(String[] args) {
        Server multiserver = null;
        Config config = Config.newInstance();
        config.initialize();
        port = Integer.parseInt(config.getProp("port"));
        try {
            LOGGER.log(Level.INFO, "Starting Server on port " + port);
            multiserver = new Server(port); //listen on port
            multiserver.run();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to open port " + port, e);
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } finally {
            try {
                multiserver.shutdown();
                LOGGER.log(Level.INFO, "Shuting down server..");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "SQL error: " + e.getMessage(), e);
            } catch (IOException e){
                LOGGER.log(Level.WARNING, "Error closing server socket: " + e.getMessage(), e);
            }
        }
    }
}
