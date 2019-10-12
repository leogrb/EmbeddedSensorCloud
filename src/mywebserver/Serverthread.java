package mywebserver;

import mywebserver.HttpRequest.RequestImpl;

import java.net.*;
import java.io.*;

public class Serverthread implements Runnable{
    private Socket socket = null;
    private InputStream in;
    private OutputStream out;
    public Serverthread(Socket clientsocket){
        this.socket = clientsocket;
    }
    @Override
    public void run() {
        try {
            in = this.socket.getInputStream();
            out = this.socket.getOutputStream();
            RequestImpl request = new RequestImpl(in);
            System.out.println(request.getMethod());
            System.out.println(request.getUrl().getRawUrl());
            System.out.println(request.getUserAgent());
            System.out.println("read finish");
            socket.close();
        } catch(IOException e){
            System.err.println();
        }
    }
}
