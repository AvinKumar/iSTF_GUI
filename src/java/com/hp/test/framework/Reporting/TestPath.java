/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.Reporting;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author yanamalp
 */
public class TestPath {

    public static void main(String ar[]) throws IOException {
        URL location = Test.class.getProtectionDomain().getCodeSource().getLocation();

        String path = location.toString();
        path = path.substring(6, path.indexOf("lib"));
        String Logos_path = path + "ATU Reports/HTML_Design_Files/IMG";
        String Source_logs_path = path + "HTML_Design_Files/IMG";
        String Source_Framework_Logo_path = Source_logs_path + "/Framework_Logo.jpg";
        String Source_hp_logo_path = Source_logs_path + "/hp.png";
        String Source_reports_path = Source_logs_path + "/reports.jpg";
        File Target_dir = new File(Logos_path);
        File Source = new File(Source_Framework_Logo_path);
        FileUtils.copyFileToDirectory(Source, Target_dir);
        File Source_Reports = new File(Source_reports_path);
        FileUtils.copyFileToDirectory(Source_Reports, Target_dir);
        File Source_hp = new File(Source_hp_logo_path);
        FileUtils.copyFileToDirectory(Source_hp, Target_dir);
        
        
    }

}
