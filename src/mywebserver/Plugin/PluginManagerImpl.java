package mywebserver.Plugin;

import BIF.SWE1.interfaces.Plugin;
import BIF.SWE1.interfaces.PluginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class PluginManagerImpl implements PluginManager {
    private List<Plugin> allPlugins = new ArrayList<Plugin>();

    public PluginManagerImpl() {
        if (allPlugins != null) {
            allPlugins.add(new PluginTemperature());
            allPlugins.add(new PluginStatic());
            allPlugins.add(new PluginNavi());
            allPlugins.add(new PluginToLower());
        }
    }

    @Override
    public List<Plugin> getPlugins() {
        return allPlugins;
    }

    @Override
    public void add(Plugin plugin) {
        if (!allPlugins.contains(plugin)) {
            allPlugins.add(plugin);
        }
    }

    @Override
    public void add(String plugin) throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException {
        Plugin plug = null;
        Class<?> clazz = Class.forName(plugin);
        Class[] interfaces = clazz.getInterfaces();
        for (Class i : interfaces) {
            if (i.toString().equals(Plugin.class.toString())) {
                plug = (Plugin) clazz.newInstance();
            }
        }
                    /*if(Plugin.class.isAssignableFrom(clazz)){
                        plug = (Plugin) clazz.newInstance();
                    }*/
        if (plug != null) {
            add(plug);
        } else {
            throw new ClassNotFoundException();
        }
    }

    @Override
    public void clear() {
        allPlugins.clear();
    }
}
