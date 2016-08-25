/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.Reporting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author yanamalp
 */
public class removerunlinks {
static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(removerunlinks.class.getName());
    public static void removelinksforpreRuns(String FilePath, String FileName,int lastrun) throws FileNotFoundException, IOException {
        
        log.info("Removing hyper link for the last run");
        ArrayList<String> Links_To_Remove = new ArrayList<String>();

        for (int i = 1; i < lastrun; i++) {
            Links_To_Remove.add("href=\"Run_" + i + "\\CurrentRun.html\"");
        }

        File source = new File(FilePath + FileName);
        File temp_file = new File(FilePath + "temp.html");
        BufferedReader in = new BufferedReader(new FileReader(source));
        BufferedWriter out = new BufferedWriter(new FileWriter(temp_file));
        String str = "";
        while ((str = in.readLine()) != null) {

            String temp_ar[] = str.split(" ");

            for (int i = 0; i < temp_ar.length; i++) {
                String temp = temp_ar[i].trim();
                if (!temp.equals("")) {

                    if (Links_To_Remove.contains(temp)) {
                        out.write(" ");
                        out.newLine();

                    } else {
                        out.write(temp);
                        out.newLine();

                    }

                }
            }
        }

        out.close();
        in.close();
        out = null;
        in = null;
  
        source.delete();
        temp_file.renameTo(source);
        

    }

 
}
