package com.hp.test.framework.parallelexecution;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
 
public class ExecutionProperties
{
  static final Logger log = Logger.getLogger(ExecutionProperties.class.getName());
   public final Properties executionprop = new Properties();
    
   public ExecutionProperties()
   {
      
      try {
       FileInputStream in = new FileInputStream("conf/Execution.properties");
      log.info("Read all properties from Execution.properties file");
      
      executionprop.load(in);
          in.close();
      } catch (IOException e) {
        
          log.error("Issue in loading Execution.properties  "+e.getMessage());
      }
   }
 
   //Bill Pugh Solution for singleton pattern
   public static class LazyHolder
   {
      private static final ExecutionProperties INSTANCE = new ExecutionProperties();
   }
 
   public static ExecutionProperties getInstance()
   {
      return LazyHolder.INSTANCE;
   }
    
   public String getProperty(String key){
      return executionprop.getProperty(key);
   }
    
   public Set<String> getAllPropertyNames(){
      return executionprop.stringPropertyNames();
   }
    
   public boolean containsKey(String key){
      return executionprop.containsKey(key);
   }
   
 
}