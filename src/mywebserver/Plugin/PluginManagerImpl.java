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
    public PluginManagerImpl(){
        if(allPlugins != null) {
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
        if(!allPlugins.contains(plugin)){
            allPlugins.add(plugin);
        }
    }

    @Override
    public void add(String plugin) throws InstantiationException, IllegalAccessException, ClassNotFoundException, MalformedURLException {
        Plugin plug = null;
        File dir = new File(".");
        File[] filesInDir = dir.listFiles();
        String fileName;
        for(File file : filesInDir){
            if(file.isFile()){
                fileName = file.getName();
                if(fileName.endsWith(".jar")){
                    URL fileUrl = file.toURI().toURL();
                    String pseudofileUrl= "j" + fileUrl;
                    String realfileUrl = pseudofileUrl.substring(1);
                    URL classUrl = new URL(realfileUrl);
                    URL[] classUrls = { classUrl };
                    URLClassLoader ucl = new URLClassLoader(classUrls);

                    Class<?> clazz = Class.forName(plugin, true, ucl);
                    Class[] interfaces = clazz.getInterfaces();
                    for (Class i : interfaces) {
                        if (i.toString().equals(Plugin.class.toString())) {
                            plug = (Plugin) clazz.newInstance();
                        }
                    }
                    /*if(Plugin.class.isAssignableFrom(clazz)){
                        plug = (Plugin) clazz.newInstance();
                    }*/

                }
            }
        }
        if(plug != null){
            add(plug);
        }
    }

    @Override
    public void clear() {
        allPlugins.clear();
    }
}
