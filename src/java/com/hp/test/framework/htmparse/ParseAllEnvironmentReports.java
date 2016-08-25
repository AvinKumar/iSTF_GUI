/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.htmparse;

//import static com.hp.test.framework.Reporting.Utlis.replacelogs;

import com.hp.test.framework.Reporting.ReportingProperties;
import static com.hp.test.framework.Reporting.Utlis.replacelogs;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author yanamalp
 */
public class ParseAllEnvironmentReports {
    
    
    //public static String Reports_path = "C:\\Users\\yanamalp\\Desktop\\Gen_jelly\\Summary";
    public static String Reports_path;

    //public static void main(String ar[]) throws ClassNotFoundException, IOException {
    public static void ConsolidatedReport() throws ClassNotFoundException, IOException {
    	ReportingProperties rp  =new ReportingProperties();

        Reports_path=rp.getProperty("MasterReportsPath");
        ParseAllEnvironmentReports parseallenvironmentreports = new ParseAllEnvironmentReports();
        List<TestCaseStatus> tcs_list;
        Map<String, List<TestCaseStatus>> Suites_list = new HashMap<>();
        //   String =
        //String LastRunFolder = GetLatestFolderinDirectory(Reports_path);
        String LastRunFolder ="Run_"+ParseAllEnvironmentReports.getLastRun(Reports_path);
        File file = new File(Reports_path + "/" + LastRunFolder);
        String[] directories = file.list(new FilenameFilter() {

            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        System.out.println(Arrays.toString(directories));
        Map<String, String> env_reportList = new HashMap<String, String>();
        for (int i = 0; i < directories.length; i++) {

            String[] temp = directories[i].toString().split("_");
            String env = temp[0];
            tcs_list = parseallenvironmentreports.getTestCases(Reports_path + "/" + LastRunFolder + "/" + directories[i].toString() + "/Results/HtmlReport.html");
            Suites_list.put(env, tcs_list);
        }

        parseallenvironmentreports.getstatusEnvWise(Suites_list);
          String basepath = replacelogs();
        GenerateEnvWiseReport genenvreport = new GenerateEnvWiseReport();
        genenvreport.openDatabaseConnection();
       
        genenvreport.generateEnvReport(new File(basepath+"/HTML_Design_Files/EnvWiseReport/index_Source.html"),Reports_path,LastRunFolder);
    

        System.out.println("Done Generating Environment Wise Reports");
    }

    public void getstatusEnvWise(Map<String, List<TestCaseStatus>> Suites_list) throws ClassNotFoundException {

        ReportingProperties rp = new ReportingProperties();
        String temp_path = rp.getProperty("MasterReportDB");
        temp_path = temp_path + "/Reports.dat";

        CreateTempReportDatabase createdatabase = new CreateTempReportDatabase();
        createdatabase.createDatabaseTable(temp_path);
        System.out.println("Temp Reports database created successfully");

        Connection connection;
        PreparedStatement prestatement;

        Class.forName("org.sqlite.JDBC");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + temp_path);
//            TESTCASE_STATUS(Environment TEXT,"
//                    + "Feature TEXT,TestCaseDesc TEXT,Status TEXT)";
            String sql = "INSERT INTO TESTCASE_STATUS (Environment, Feature,TestCaseDesc,Status) VALUES (?, ?, ?, ?)";
            prestatement = connection.prepareStatement(sql);

            Map<String, Map<String, Integer>> Suites_list1 = new HashMap<>();

            for (String env : Suites_list.keySet()) {
                List<TestCaseStatus> tcs_list = Suites_list.get(env);

                for (int i = 1; i < tcs_list.size(); i++) {
                    TestCaseStatus tcs = tcs_list.get(i);

                    String Feature = tcs.Feature;
                    String Testcse = tcs.TestcaseName;
                    String TestCaseStatus = tcs.Status;
                    prestatement.setString(1, env);
                    prestatement.setString(2, Feature);
                    prestatement.setString(3, Testcse);
                    prestatement.setString(4, TestCaseStatus);
                    prestatement.execute();

                }

            }

        } catch (SQLException e) {
            System.out.println("Error in Connecting Reporting Database" + e.getMessage());
        }

    }

    public List<TestCaseStatus> getTestCases(String path) {
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File(path), "UTF-8");
        } catch (IOException e) {
            System.out.println("Exception in parse Current Run html file" + e.getMessage());
        }

        List<TestCaseStatus> tcs_list = new ArrayList<TestCaseStatus>();
        for (Element table : htmlFile.select("table[id=tableStyle]")) {
            Elements row1 = table.select("tr");
            for (int j = 0; j < row1.size(); j++) {
                TestCaseStatus tcs = new TestCaseStatus();
                Element tds1 = row1.get(j);
                Elements tds = tds1.select("td");

                for (int i = 1; i < tds.size(); i++) {

                    Element link = tds.get(i);
                    String text = link.text();
                    if (i == 1) {
                        tcs.Feature = text;
                    }
                    if (i == 2) {
                        tcs.TestcaseName = text;
                    }
                    if (i == 3) {
                        tcs.Status = text;
                    }

                }
                tcs_list.add(tcs);
                System.out.println("\n\n\n");
            }
        }
     //   Suites_list.put("Win 2k", tcs_list);

        return tcs_list;
    }

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
