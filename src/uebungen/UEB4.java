package uebungen;

import java.io.InputStream;

import BIF.SWE1.interfaces.PluginManager;
import BIF.SWE1.interfaces.Request;
import BIF.SWE1.interfaces.Response;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.HttpResponse.ResponseImpl;
import mywebserver.Plugin.PluginManagerImpl;

public class UEB4 {

    public void helloWorld() {

    }

    public Request getRequest(InputStream inputStream) {
        return new RequestImpl(inputStream);
    }

    public Response getResponse() {
        return new ResponseImpl();
    }

    public PluginManager getPluginManager() {
        return new PluginManagerImpl();
    }
}
