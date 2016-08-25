
package com.hp.test.framework.staf;

/**
 *
 * @author sayedmo
 */

import com.hp.test.framework.Reporting.ReportingProperties;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import com.ibm.staf.STAFException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class FileUtils {
    static Logger logger = Logger.getLogger(FileUtils.class);
    public static String runfolder;
    
    //public static String ReportPath, latestcreatedfolder;
   
    StafDemo stafdmo=new StafDemo();
    
    
    public static void main(String args[]) throws Exception {
        FileUtils test=new FileUtils();
        FileUtils.CheckReportsDirAndCreateRunFolder();
        //test.CreateReportFolderBasedonOS();
        //test.CopyReportsDirToMaster();
        //FileUtils.getOS();
    }

    public static String CheckReportsDirAndCreateRunFolder() {
  
       ReportingProperties rp  =new ReportingProperties();
       String ReportPath=rp.getProperty("MasterReportsPath");
       File files[]; 
       files = FileUtils.dirListByDescendingDate
      (new File(ReportPath));
       
       if (files.length==0){
        System.out.println("Reports Directory Empty Hence Creating Run_1");   
        String FolderToCreate = ReportPath + "/" + "Run_1" ;
        createdir(FolderToCreate); 
        logger.info("Folder Created "+FolderToCreate+ "Successfully"); 
       }
       
       else {
        
        logger.info("*********");

        String lastcreatedfolder = FileUtils.GetLatestFolderinDirectory(ReportPath);
        logger.info("Last Created folder in the directory is:" + lastcreatedfolder);
        String[] valueofi = lastcreatedfolder.split("_");
        logger.info("Value of i is::" + valueofi[1]);

        int i;
        i = Integer.parseInt(valueofi[1]);
        i++;
        logger.info("Value of i is::" + i);

        String FolderNameToCreate = ReportPath + "/" + "Run_" + i;
        createdir(FolderNameToCreate);
        logger.info("Folder Created "+FolderNameToCreate+ "Successfully"); 
        runfolder="Run_"+i;
        logger.info("Run Folder is "+runfolder); 
        
       }
       return runfolder;
    }

    @SuppressWarnings("unchecked")
    public static String GetLatestFolderinDirectory(String rootreportpath) {
        File folder = new File(rootreportpath);
        if (!folder.isDirectory()) {
            return null;
        }
        File files[] = folder.listFiles();
        Arrays.sort(files, new Comparator() {
            public int compare(final Object o1, final Object o2) {
                return new Long(((File) o2).lastModified()).compareTo(new Long(((File) o1).lastModified()));
            }
        });
        //return files;
        String latestcreatedfolder = files[0].getName();
        return latestcreatedfolder;
    }
    
    @SuppressWarnings("unchecked")
  public static File[] dirListByDescendingDate(File folder) {
    if (!folder.isDirectory()) {
      return null;
    }
    File files[] = folder.listFiles();
    Arrays.sort( files, new Comparator()
    {
      public int compare(final Object o1, final Object o2) {
        return new Long(((File)o2).lastModified()).compareTo
             (new Long(((File) o1).lastModified()));
      }
    }); 
    return files;
  }  

    public static void createdir(String dirpath) {
        //File file = new File("C:\\Directory1");
        File file = new File(dirpath);
        if (!file.exists()) {
            if (file.mkdir()) {
                logger.info("Directory is created!");
            } else {
                logger.info("Failed to create directory!");
            }
        }
    }
    
    public void CreateReportFolderBasedonOS(String runfoldename) throws UnknownHostException {
        //String Basepath="C:/Reports";
        ReadStafProperties stafprop=new ReadStafProperties();
        String Basepath=stafprop.getProperty("MasterReportsPath");
        String DestinationPath=stafprop.getProperty("DestinationIP");
        //String runfolder=FileUtils.GetLatestFolderinDirectory(Basepath);
        // The key for getting operating system name
        String command="";
        String name = FileUtils.getOS();
        //name=System.getProperty(name);
        if(name.equalsIgnoreCase("WINDOWS 7")){

            String winfolder=Basepath+"/"+runfoldename+"/"+"Win7_ATU Reports_"+InetAddress.getLocalHost().getHostName();
            command="staf "+DestinationPath+" FS CREATE DIRECTORY "+winfolder+"";
            //FileUtils.createdir(winfolder);
            stafdmo.ExecuteProcessCommand(command);
            logger.info("Folder Created "+winfolder+ "Successfully"); 
            
        } else if(name.contains("LINUX")){
            String linuxfolder=Basepath+"/"+runfoldename+"/"+"Linux_ATU Reports_"+InetAddress.getLocalHost().getHostName();
            command="staf "+DestinationPath+" FS CREATE DIRECTORY "+linuxfolder+"";
            stafdmo.ExecuteProcessCommand(command);
            logger.info("Folder Created "+linuxfolder+ "Successfully"); 
        }
    }
    
    public void CopyReportsDirToMaster(String runfoldename) throws STAFException, STAFQAException, UnknownHostException {
        ReadStafProperties stafprop=new ReadStafProperties();
        String Basepath=stafprop.getProperty("MasterReportsPath");
        
        String osname= FileUtils.getOS();
        //String Basepath="C:/Reports";
        //String runfolder=FileUtils.GetLatestFolderinDirectory(Basepath);
        
        if(osname.equalsIgnoreCase("WINDOWS 7")){
          String DestinationPath= Basepath+"/"+runfoldename+"/"+"Win7_ATU Reports_"+InetAddress.getLocalHost().getHostName(); 
          stafdmo.copyFolder("localhost", stafprop.getProperty("SourceReportsOnWin"), stafprop.getProperty("DestinationIP"), DestinationPath);
          logger.info("Successfully Copied Reports from "+stafprop.getProperty("SourceReportsOnWin")+ "to"+DestinationPath); 
          
        } else if(osname.contains("LINUX")){
           String DestinationPath= Basepath+"/"+runfoldename+"/"+"Linux_ATU Reports_"+InetAddress.getLocalHost().getHostName(); 
           stafdmo.copyFolder("localhost", stafprop.getProperty("SourceReportsOnLinux"), stafprop.getProperty("DestinationIP"), DestinationPath); 
           logger.info("Successfully Copied Reports from "+stafprop.getProperty("SourceReportsOnLinux")+ "to"+DestinationPath); 
        }
    }
    
    public static String getOS(){
        String name = "os.name";   
        name=System.getProperty(name);
       // logger.info("Operating system OS is: "+name); 
System.out.println("Operating system OS is: "+name); 
        return name;
    }
    
    public static int getLastRun(String path) {
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        
       List<String> Only_run_list=new ArrayList<String>();
      
       for (int i = 1; i < directories.length; i++) {
           if(directories[i].contains("_"));
           Only_run_list.add(directories[i]);
       }
        String temp_ar[] = Only_run_list.get(0).split("_");
        
        int max = Integer.parseInt(temp_ar[1]);
        for (int i = 1; i < Only_run_list.size(); i++) {
            String temp_ar1[] = Only_run_list.get(i).split("_");
            
            int run = Integer.parseInt(temp_ar1[1]);
            
            if (run > max) {
                max = run;
            }
            
        }
        System.out.println(Arrays.toString(directories));
        System.out.println("Latest Run" + max);
        return max;
    }
}
