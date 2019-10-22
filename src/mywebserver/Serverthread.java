package mywebserver;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.Plugin.PluginManagerImpl;

import java.net.*;
import java.io.*;
import java.util.List;

public class Serverthread implements Runnable {
    private Socket socket = null;
    private InputStream in;
    private OutputStream out;
    private PluginManagerImpl pM;
    private List<Plugin> allPlugins;

    public Serverthread(Socket clientsocket) {
        this.socket = clientsocket;
    }

    @Override
    public void run() {
        pM = new PluginManagerImpl();
        try {
            allPlugins = pM.getPlugins();
            // get client request
            in = this.socket.getInputStream();
            out = this.socket.getOutputStream();
            RequestImpl request = new RequestImpl(in);
            Response resp = null;
            Plugin handleRequest = null;
            float score = 0f;
            for (Plugin p : allPlugins) {
                float t = p.canHandle(request);
                if (t > score) {
                    handleRequest = p;
                    score = t;
                }
            }
            if (score != 0) {
                resp = handleRequest.handle(request);
            }
            resp.send(out);
        } catch (IOException e) {
            System.err.println();
        } finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
