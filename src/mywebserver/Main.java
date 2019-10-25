package mywebserver;

import mywebserver.Server;
import mywebserver.Serverthread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final int port = 80;

    public static void main(String[] args) {
        try {
            LOGGER.log(Level.INFO, "Starting Server on port " + port);
            Server multiserver = new Server(port); //listen on port
            multiserver.run();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to open port " + port, e);
            System.exit(1);
        } catch (NullPointerException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}
