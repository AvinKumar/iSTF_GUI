    package com.hp.test.framework.model.testcasegen;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ReadProperties {

    public  Properties configProp = new Properties();
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReadProperties.class.getName());

    // By default properties will be read in from property file
    public ReadProperties() {

       
      try {      //Private constructor to restrict new instances
      FileInputStream in = new FileInputStream("conf/app.properties");
    //  System.out.println("Read all properties from file");
      
    	
          configProp.load(in);
          in.close();
      } catch (IOException e) {
          log.error(e.getMessage());
          
      }
    }

    //ReadProperties can also be constructed here by user supplied properties
    public ReadProperties(final Properties prop) {  
        configProp = prop;          
    }
        
    //Bill Pugh Solution for singleton pattern
    public static class LazyHolder {

        private static final ReadProperties INSTANCE = new ReadProperties();
    }

    public static ReadProperties getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getProperty(String key) {
        return configProp.getProperty(key);
    }

    public Set<String> getAllPropertyNames() {
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return configProp.containsKey(key);
    }

}
