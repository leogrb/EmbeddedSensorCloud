package mywebserver.Properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {
    private static Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static Config config = null;
    private Properties props = null;

    public void initialize() {
        try {
            if (props == null) {
                props = new Properties();
                String propertiesConfigPath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "config.properties";
                props.load(new FileInputStream((propertiesConfigPath)));
            }
        } catch(IOException e){
            LOGGER.log(Level.WARNING, "Error loading properties" + e.getMessage());
        }
    }

    public static synchronized Config newInstance(){
        if(config == null){
            config = new Config();
        }
        return config;
    }

    public void setProps(String key, String value){
        props.setProperty(key, value);
    }

    public String getProp(String property){
        return props.getProperty(property);
    }

}
