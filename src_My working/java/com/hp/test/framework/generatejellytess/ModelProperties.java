package com.hp.test.framework.generatejellytess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
 
public class ModelProperties
{
  static final Logger log = Logger.getLogger(ModelProperties.class.getName());
   public final Properties modelprop = new Properties();
    
   public ModelProperties()
   {
      //Private constructor to restrict new instances
      //InputStream in = this.getClass().getClassLoader().getResourceAsStream("DSSearchModelTests.properties");
      try {
       FileInputStream in = new FileInputStream("conf/Model_File_TestCaseGen.properties");
      log.info("Read all properties from Model_File_TestCaseGen.properties file");
      
          modelprop.load(in);
          in.close();
      } catch (IOException e) {
        
          log.error("Issue in loading Model_File_TestCaseGen.properties  "+e.getMessage());
      }
   }
 
   //Bill Pugh Solution for singleton pattern
   public static class LazyHolder
   {
      private static final ModelProperties INSTANCE = new ModelProperties();
   }
 
   public static ModelProperties getInstance()
   {
      return LazyHolder.INSTANCE;
   }
    
   public String getProperty(String key){
      return modelprop.getProperty(key);
   }
    
   public Set<String> getAllPropertyNames(){
      return modelprop.stringPropertyNames();
   }
    
   public boolean containsKey(String key){
      return modelprop.containsKey(key);
   }
   
 
}