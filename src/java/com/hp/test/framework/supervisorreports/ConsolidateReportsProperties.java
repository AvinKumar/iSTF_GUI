package com.hp.test.framework.supervisorreports;


import com.hp.test.framework.Reporting.*;
import com.hp.test.framework.model.testcasegen.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ConsolidateReportsProperties {

    public final Properties configProp = new Properties();
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReportingProperties.class.getName());
    public ConsolidateReportsProperties() {

       
      try {      //Private constructor to restrict new instances
      FileInputStream in = new FileInputStream("conf/consildatereport.properties");
      System.out.println("Read all properties from file");
      
    	
          configProp.load(in);
          in.close();
      } catch (IOException e) {
          log.error(e.getMessage());
          
      }
    }

    //Bill Pugh Solution for singleton pattern
    public static class LazyHolder {

        private static final ReportingProperties INSTANCE = new ReportingProperties();
    }

    public static ReportingProperties getInstance() {
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
