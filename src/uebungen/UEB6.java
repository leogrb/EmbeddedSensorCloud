package uebungen;

import java.io.InputStream;
import java.time.LocalDate;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.PluginManager;
import BIF.SWE1.interfaces.Request;
import mywebserver.HttpRequest.RequestImpl;
import mywebserver.Plugin.PluginToLower;

public class UEB6 {

    public void helloWorld() {

    }

    public Request getRequest(InputStream inputStream) {
        return new RequestImpl(inputStream);
    }

    public PluginManager getPluginManager() {
        return null;
    }

    public Plugin getTemperaturePlugin() {
        return null;
    }

    public Plugin getNavigationPlugin() {
        return null;
    }

    public Plugin getToLowerPlugin() {
        return new PluginToLower();
    }

    public String getTemperatureUrl(LocalDate localDate, LocalDate localDate1) {
        return null;
    }

    public String getTemperatureRestUrl(LocalDate localDate, LocalDate localDate1) {
        return null;
    }

    public String getNaviUrl() {
        return null;
    }

    public String getToLowerUrl() {
        return "/tolower?tolower_plugin=true";
    }
}
