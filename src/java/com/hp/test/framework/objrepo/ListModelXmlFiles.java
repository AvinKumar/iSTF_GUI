/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.objrepo;

import com.hp.test.framework.generatejellytess.ModelProperties;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yanamalp
 */
public class ListModelXmlFiles {
    
    public static Map<String,String> Model_XMl_List;
    
    
    public static void ListModelXmlFiles()
    {
        ModelProperties mp=new ModelProperties();
        
        String Location=mp.getProperty("MODEL_XML_PATH");
        Model_XMl_List=null;
        Model_XMl_List=new HashMap<String,String>();
  
       
         File root = new File( Location );
         File[] list = root.listFiles();
         if (list == null) return;
         for ( File f : list )
         {
               try
               {
                    System.out.println( "File:" + f.getAbsoluteFile() );
                    String filename=f.getName();
                    String Abpath=f.getAbsolutePath();
                    Model_XMl_List.put(filename, Abpath);
                   
               }
                    catch(Exception e)
                    {
                        System.out.println("Error in updating paths in DM_REFERENCe Table :::" + e.getMessage() );
                    }
                }
            
        


        
        
        
        
    }
    
}
