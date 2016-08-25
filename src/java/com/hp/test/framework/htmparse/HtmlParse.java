/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.htmparse;


import com.hp.test.framework.Reporting.ReportingProperties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author yanamalp
 */
public class HtmlParse {


    public static String getCountsSuiteswise(String path) {
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File(path), "UTF-8");
        } catch (IOException e) {
            System.out.println("Exception in parse Current Run html file" + e.getMessage());
        }

        Map<String, Map<String, Integer>> Suites_list = new HashMap<>();
        for (Element table : htmlFile.select("table[id=tableStyle]")) {
             Elements row1 = table.select("tr");
            for (int j = 0; j < row1.size(); j++) {
                Element tds1 = row1.get(j);
                Elements tds = tds1.select("td");
                String SuiteName = "";
                String Method_type = "";
                String TestCaseStatus = "";
                Map<String, Integer> test_status_list = new HashMap<String, Integer>();
                for (int i = 0; i < tds.size(); i++) {
                    Element link = tds.get(i);
                    String link_temp = link.toString();
                    Elements href = link.select("a");
                    
                    if (i == 0) {
                        if (href.size() > 0) {
                            SuiteName = href.get(0).text();
                        }
                    }

                    if (i == 3)
                    {
                        if (href.size() > 0) {
                            Method_type = href.get(0).text();
                        }

                    }
                    if (i == 7 && Method_type.equals("Test Method")) {
                        if (link_temp.contains("pass.png") || link_temp.contains("fail.png")||link_temp.contains("skip.png")) {
                                      //          img style=\"border: none;width: 25px
                                    //   ing str="img  style=\"border: none;width: 25px";
                           
                        if (link_temp.contains("pass.png")) {
                                TestCaseStatus = "pass";
                            } else if (link_temp.contains("fail.png")) {
                                TestCaseStatus = "fail";
                            } else {
                                TestCaseStatus = "skip";
                            }
                           // System.out.println("SuiteName::" + SuiteName);
                          //  System.out.println("Method_type::" + Method_type);
                           // System.out.println("TestCaseStatus::" + TestCaseStatus);
                          //  System.out.println("*****************************");

                            if (Suites_list.get(SuiteName) == null) {
                                if (TestCaseStatus.equals("pass")) {
                                    test_status_list.put("pass", 1);
                                    test_status_list.put("fail", 0);
                                    test_status_list.put("skip", 0);
                                }

                                if (TestCaseStatus.equals("fail")) {
                                    test_status_list.put("pass", 0);
                                    test_status_list.put("fail", 1);
                                    test_status_list.put("skip", 0);
                                }

                                if (TestCaseStatus.equals("skip")) {
                                    test_status_list.put("pass", 0);
                                    test_status_list.put("fail", 0);
                                    test_status_list.put("skip", 1);
                                }

                                Suites_list.put(SuiteName, test_status_list);
                            } else {
                                Map<String, Integer> temp_list = Suites_list.get(SuiteName);

                                for (String status : temp_list.keySet()) {
                                    if (status.equals(TestCaseStatus)) {
                                        int count = temp_list.get(status);
                                        count = count + 1;
                                        temp_list.put(status, count);
                                    }
                                }
                                Suites_list.put(SuiteName, temp_list);

                            }

                        }
                    }
                }
            }

        }
        String variable = "var chartData = [";
        int NoofSuites = Suites_list.size();
        int i = 1;
        for (String FeatureName : Suites_list.keySet()) {
            String feature_data = " { \n \"feature\":\"" + FeatureName + "\",\n";

            Map<String, Integer> temp_list = Suites_list.get(FeatureName);

            for (String status : temp_list.keySet()) {
                feature_data = feature_data + "\"" + status + "\":" + temp_list.get(status) + ",\n";
            }
            if (!(NoofSuites == i)) {
                feature_data = feature_data + "},\n";
            } else {
                feature_data = feature_data + "}\n";
            }
            variable = variable + feature_data;
            i = i + 1;
        }
        variable = variable + "];";
        System.out.println("Getting the Counts Functionality Wise is Completed");
        return variable;

    }

    public static void replaceMainTable(Boolean onlyMainTable, File f) {

        try {
            String[] filename = f.getName().split("/.");
            String absolutePath = f.getAbsolutePath();
            String filePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
            BufferedReader in = new BufferedReader(new FileReader(f));
            String Filepathof_newFile=filePath + "/" + filename[0] + "temp.html";
            ReportingProperties reportingprop=new ReportingProperties();
            String customizedText=reportingprop.getProperty("CustomizedText");
            String CustomizedTitle=reportingprop.getProperty("CustomizedTitle");
            reportingprop=null;
           
            BufferedWriter out = new BufferedWriter(new FileWriter(Filepathof_newFile));
          //    BufferedWriter out = new BufferedWriter(new FileWriter(filePath + "/" + filename[0] ));

            String str;
            while ((str = in.readLine()) != null) {

                if (str.contains("<div class=\"chartStyle\" style=\"text-align: left;margin-left: 30px;float: left;width: 60%;\"> ")) {
                 //   out.write("</div> <div     class=\"chartStyle\" style=\"text-align: left;margin-left: 30px;float: left;width: 30%;\"> ");
                    out.write(str);
                    out.write(in.readLine());
                    out.write(in.readLine());
                    out.write(in.readLine());
                  //  out.write("\n");
                    out.write("<div class=\"chartStyle\" style=\"text-align: left;margin-left: 5px;float: left;width: 95%;\">");
                    out.write("\n");
                    out.write("<div id=\"chartdiv\" style=\"width: 1300px; height: 350px;\"></div>");
                    out.write("\n");
                    out.write("</div>");
                    continue;

                }
                
                if(str.contains("No Video Recording Available"))
                    continue;
                if(str.contains("Run Description"))
                {
                    str=str.replace("Run Description", CustomizedTitle);
                    str=str.replace("Here you can give description about the current Run", customizedText);
                     out.write(str);
                     out.write("\n");
                    continue;
                }
//                if(str.contains("Here you can give description about the current Run"))
//                {
//                    str=str.replace("Here you can give description about the current Run", customizedText);
//                   out.write(str);
//                   out.write("\n");
//                   continue;
//                }
                if (!str.contains("Current Run Reports")) {
                    out.write(str);
                    out.write("\n");

                } else {
                    out.write(str);
                    out.write("\n");

                    out.write(" <link rel=\"stylesheet\" href=\"style.css\" type=\"text/css\">");
                    out.write("\n");
                    out.write("<script src=\"amcharts.js\" type=\"text/javascript\"></script>");
                    out.write("\n");
                    out.write("<script src=\"serial.js\" type=\"text/javascript\"></script>");
                    out.write("\n");
                    out.write("<script src=\"3dChart.js\" type=\"text/javascript\"></script>");
                    out.write("\n");
                }

            }
            in.close();
            out.close();
            File renameFile=new File(Filepathof_newFile);
            
           FileUtils.copyFile(renameFile, f);
            System.out.println("Replacing Counts Feature wise is Completed");
        } catch (IOException e) {
            System.out.println("Exception replacing Counts Feature wise" + e.getLocalizedMessage());
        }
    }

    public static void replaceCountsinJSFile(File jsFile, String TargetPath) {

        try {
            String[] filename = jsFile.getName().split("/.");
            BufferedReader in = new BufferedReader(new FileReader(jsFile));
            BufferedWriter out = new BufferedWriter(new FileWriter(TargetPath + "\\" + filename[0]));

            String str;

            while ((str = in.readLine()) != null) {

                if (str.contains("AmCharts.ready")) {
                    out.write(str);
                    out.write("\n");
                    out.write(HtmlParse.getCountsSuiteswise(TargetPath + "/CurrentRun.html"));

                } else {
                    out.write(str);
                    out.write("\n");
                }

            }
            in.close();
            out.close();
            System.out.println("Replacing Counts in .js file is done ");

            File amcharts = new File("HTML_Design_Files/JS/amcharts.js");
            File serial = new File("HTML_Design_Files/JS/serial.js");
            FileUtils.copyFile(amcharts, new File(TargetPath + "/amcharts.js"));
            FileUtils.copyFile(serial, new File(TargetPath + "/serial.js"));

        } catch (IOException e) {
            System.out.println("Exception in Replacing Counts in .js file is done " + e.getMessage());
        }

    }

}
