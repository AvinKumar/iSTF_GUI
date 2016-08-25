package com.hp.test.framework.supervisorreports;

import com.hp.test.framework.Reporting.ReportingProperties;
import com.hp.test.framework.htmparse.ParseAllEnvironmentReports;
import com.hp.test.framework.model.testcasegen.CreateDirectory;
import com.hp.test.framework.staf.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hp.test.framework.Log4j.PropConfigurator;

/**
 *
 * @author sayedmo
 */
public class CopyReports {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReportingProperties.class.getName());

    public static void main(String ar[]) throws IOException {
        String fileName, foldertocreate;
        CopyReports cp = new CopyReports();
        File source, destination;
    PropConfigurator.configure();
        ReportingProperties rp = new ReportingProperties();
        String ReportPath = rp.getProperty("MasterReportsPath");

        //This will create new Run_ directory in reports path
        FileUtils.CheckReportsDirAndCreateRunFolder();
        //Get the last created folder in reports directory
        String lastcreatedfolder = FileUtils.GetLatestFolderinDirectory(ReportPath);
        log.info(lastcreatedfolder);

        ConsolidateReportsProperties crp = new ConsolidateReportsProperties();
        String path = crp.getProperty("allreportspath");

        String[] paths = path.split(",");
        for (String s : paths) {
            fileName = s.substring(s.lastIndexOf("/") + 1);
            foldertocreate = ReportPath + "/" + lastcreatedfolder + "/" + fileName+"_ATUReports";
            log.info("last directory name" + fileName);

            log.info("Folder to be created:" + foldertocreate);
            CreateDirectory.createdir(foldertocreate);
            //Do your stuff here
            log.info("Path Name:" + s);
            s=s+"/ATU Reports";
            source = new File(s);
            destination = new File(foldertocreate);
            cp.recursiveCopy(source, destination);
            try {
                //Call Consolidated Report Environment Wise
                ParseAllEnvironmentReports.ConsolidatedReport();
            } catch (ClassNotFoundException ex) {
                log.error("Exception occured while copying reports" + ex.fillInStackTrace());
            } catch (IOException ex) {
                log.error("Exception occured while copying reports" + ex.fillInStackTrace());
            }

        }
    }

    private void recursiveCopy(File fSource, File fDest) {
        try {
            if (fSource.isDirectory()) {
                // A simple validation, if the destination is not exist then create it
                if (!fDest.exists()) {
                    fDest.mkdirs();
                }

                // Create list of files and directories on the current source
                // Note: with the recursion 'fSource' changed accordingly
                String[] fList = fSource.list();

                for (int index = 0; index < fList.length; index++) {
                    File dest = new File(fDest, fList[index]);
                    File source = new File(fSource, fList[index]);

                    // Recursion call take place here
                    recursiveCopy(source, dest);
                }
            } else {
               // Found a file. Copy it into the destination, which is already created in 'if' condition above

                // Open a file for read and write (copy)
                FileInputStream fInStream = new FileInputStream(fSource);
                FileOutputStream fOutStream = new FileOutputStream(fDest);

                // Read 2K at a time from the file
                byte[] buffer = new byte[2048];
                int iBytesReads;

                // In each successful read, write back to the source
                while ((iBytesReads = fInStream.read(buffer)) >= 0) {
                    fOutStream.write(buffer, 0, iBytesReads);
                }

                // Safe exit
                if (fInStream != null) {
                    fInStream.close();
                }

                if (fOutStream != null) {
                    fOutStream.close();
                }
            }
        } catch (IOException ex) {
            // Please handle all the relevant exceptions here
            System.out.println("Exception occured while copying reports" + ex.fillInStackTrace());
        }
    }

}
