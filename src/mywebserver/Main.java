package mywebserver;

import mywebserver.Server;
import mywebserver.Serverthread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int port = 80;

    public static void main(String[] args) {
        try {
            Server multiserver = new Server(port); //listen on port
            multiserver.run();
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            System.exit(1);
        } catch (NullPointerException e) {
            System.err.println("Connection error: " + e.getMessage());
            System.exit(1);
        }
    }
}
