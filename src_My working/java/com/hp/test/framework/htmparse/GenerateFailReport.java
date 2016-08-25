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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class GenerateFailReport {

    public static void genarateFailureReport(File SourceFile, String TargetPath) {

        try {

            Map<String, Map<String, List<String>>> Suites_list = new HashMap<>();

            for (String key : UpdateTestCaseDesciption.TestCaseDesMap.keySet()) {
                Map<String, List<String>> test_status_list = new HashMap<>();
                String status = UpdateTestCaseDesciption.TestcaseStatusMap.get(key);

                String[] temp_ar = key.split("\\\\");

                List<String> Des = new ArrayList<String>();
                Des.add(UpdateTestCaseDesciption.TestCaseDesMap.get(key));
                Des.add(status);
                test_status_list.put(key, Des);
                if (!Suites_list.containsKey(temp_ar[0])) {
                    Suites_list.put(temp_ar[0], test_status_list);
                } else {
                    Map<String, List<String>> temp_status_list = Suites_list.get(temp_ar[0]);
                    temp_status_list.put(key, Des);
                    Suites_list.put(temp_ar[0], temp_status_list);
                }

            }

            String[] filename = SourceFile.getName().split("/.");
            BufferedReader in = new BufferedReader(new FileReader(SourceFile));
            BufferedWriter out = new BufferedWriter(new FileWriter(TargetPath + "\\" + filename[0]));

            String str;

            while ((str = in.readLine()) != null) {

                if (str.contains("All Suites")) {
                    out.write(str);
                    for (String key : Suites_list.keySet()) {

                        String Filter = "<option class=\"filterOption\"" + " value=\"" + key + "\">" + key + "</option>";
                        out.write(Filter);
                        out.write("\n");

                    }
                    continue;
                }

                if (str.contains("#suiteFilter")) {
                    out.write(str);
                    for (String key : Suites_list.keySet()) {

                        String suiteFilter = "if($(this).val()=='" + key + "'){      $('.all').hide();$('." + key + "').show(); }";
                        out.write(suiteFilter);
                        out.write("\n");

                    }
                    continue;
                }

                out.write(str);
            }

            in.close();

            int i = 1;

            for (String key : Suites_list.keySet()) {
                Map<String, List<String>> temp_status_list = Suites_list.get(key);
                for (String TestCase : temp_status_list.keySet()) {

                    List<String> temp_list = temp_status_list.get(TestCase);
                    // String[] temp_ar=temp.split("^");
                    String status = temp_list.get(1);
                    String classTag = status + " all " + key;

                    out.write("<tr class=\"" + classTag + "\">");
                    out.write("\n");
                    out.write("<td>" + String.valueOf(i) + "</td>");
                    out.write("\n");
                    out.write("\n");
                    out.write("<td>" + key + "</td>");

                    out.write("\n");
                    out.write("<td><font color=\"#013ADF\" size=\"3\">" + temp_list.get(0) + "</font></td>");
                    out.write("\n");

                    if (status.contains("fail")) {
                        out.write("<td  style=\"font-weight:bold;vertical-align:middle;white-space:nowrap;font size=\"35;\"\"><font size=\"3\" color=\"Red\">" + "Fail" + "</font></td>");
                    }
                    if (status.contains("pass")) {
                        out.write("<td  style=\"font-weight:bold;vertical-align:middle;white-space:nowrap;font size=\"35;\"\"><font color=\"Green\">" + "Pass" + "</font></td>");
                    }
                    if (status.contains("skip")) {
                        out.write("<td  style=\"font-weight:bold;vertical-align:middle;white-space:nowrap;font size=\"35;\"\"><font color=\"Blue\">" + "Skip" + "</font></td>");
                    }
                    out.write("\n");
                    out.write("</tr>");
                    out.write("\n");
                    i = i + 1;

                }
            }

            out.write("</Table>");
            out.write("</body>");
            out.write("</html>");
            out.close();
            System.out.println("Genarating Failure Report is Completed ");

        } catch (IOException e) {
            System.out.println("Exception in Replacing Counts in .js file is done " + e.getMessage());
        }

    }

}
