package mywebserver.MultiServer;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Main;
import mywebserver.Plugin.PluginManagerImpl;

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serverthread implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

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
            if (resp != null) {
                resp.send(out);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
        } finally {
            try {
                out.flush();
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Unexpected Error: " + e.getMessage(), e);
            }
        }
    }
}
