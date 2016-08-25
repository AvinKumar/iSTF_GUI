package com.hp.test.framework.objrepo;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ReadProperties {

    public final Properties configProp = new Properties();

    public ReadProperties() {

       
      try {      //Private constructor to restrict new instances
      FileInputStream in = new FileInputStream("conf/app.properties");
      System.out.println("Read all properties from file");
      
    	
          configProp.load(in);
          in.close();
      } catch (IOException e) {
          e.printStackTrace();
          
      }
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
