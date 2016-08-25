/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.Reporting;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 *
 * @author yanamalp
 */
public class cleanTempDir {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(cleanTempDir.class.getName());
    public static void cleanandCreate(String Reports_path,int last_run)
    {
       
    String temp_path=Reports_path+ "\\temp";
    
    
    deleteDir(new File(temp_path)) ;
      File file = new File(temp_path);
         boolean isDirectoryCreated = file.mkdir();
     if (isDirectoryCreated) {
         log.info ("successfully Created directory");
        } else {
          file.delete();
          file.mkdir();
          log.info("deleted and Created directory");
          }
     
    File trgDir = new File(temp_path);
  
    File srcDir = new File(Reports_path);
  
     
        try {
            FileUtils.copyDirectory(srcDir, trgDir);
           log.info("Copying reports to temp path is completed");
        } catch (IOException ex) {
            log.error("Issue with the copying reports to temp directory"+ex.getMessage());
        }
        try {
            removerunlinks.removelinksforpreRuns(temp_path+"\\results\\","ConsolidatedPage.html",last_run);
        } catch (IOException ex) {
            log.error("Error in removing links from the page ConsolidatedPage.html"+ex.getMessage());
        }
        for(int i=1;i<last_run;i++)
        {
        try {
            deleteDir(new File(temp_path+"\\results\\Run_"+i));
        } catch (Exception ex) {
            log.error("Not able to delete directory"+temp_path+"\\results\\Run_"+i);
        }
            log.error("Deleted"+temp_path+"\\results\\Run_"+i);
        }
        FileUtils.deleteQuietly(new File(temp_path+"\\ISTF_Reports.zip"));
       // ZipUtils.ZipFolder(temp_path, Reports_path+);
        
     
    }
    
    
    public static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
        String[] children = dir.list();
        for (int i=0; i<children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
    }
    return dir.delete();
}
}
