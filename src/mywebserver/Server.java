package mywebserver;

import java.io.IOException;
import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Exception;
import java.lang.String;
import java.lang.Thread;

public class Server {
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port); //listen on port
    }

    public void run() throws IOException, NullPointerException{
        while (true) {
            Socket cl = this.serverSocket.accept(); //accept connection and create socket obj
            Serverthread client = new Serverthread(cl);
            Thread thread = new Thread(client);
            thread.start();
        }
    }
}

