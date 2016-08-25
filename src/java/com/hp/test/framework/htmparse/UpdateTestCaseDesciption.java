/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.htmparse;

import static com.hp.test.framework.htmparse.GenerateFailReport.genarateFailureReport;
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
public class UpdateTestCaseDesciption {

    public static String basepath = "";

    public static Map<String, String> TestCaseDesMap = new HashMap<String, String>();
    public static Map<String, String> TestcaseStatusMap = new HashMap<String, String>();

    public static void getTestDescription(String path) {
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File(basepath + path), "UTF-8");
        } catch (IOException e) {
            System.out.println("Exception in parse Current Run html file" + e.getMessage());
        }

        for (Element table : htmlFile.select("table[id=tableStyle]")) {
            Elements row1 = table.select("tr");
            for (int j = 0; j < row1.size(); j++) {
                Element tds1 = row1.get(j);
                Elements tds = tds1.select("td");

                for (int i = 0; i < tds.size(); i++) {
                    Element link = tds.get(i);
                    String link_temp = link.toString();

                    if (i == 1) {
                        //   System.out.println("data" + link_temp);
                        if (!TestCaseDesMap.containsKey(path)) {
                            TestCaseDesMap.put(path, Jsoup.parse(link_temp).text());
                        }
                        break;
                    }
                }

            }
        }

    }

    public static void getTestCaseHtmlPath(String path) {
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File(path), "UTF-8");
        } catch (IOException e) {
            System.out.println("Exception in parse Current Run html file" + e.getMessage());
        }
        for (Element table : htmlFile.select("table[id=tableStyle]")) {
            Elements row1 = table.select("tr");
            for (int j = 0; j < row1.size(); j++) {
                Element tds1 = row1.get(j);
                Elements tds = tds1.select("td");
                for (int i = 0; i < tds.size(); i++) {
                    Element link = tds.get(i);
                    Elements href = link.select("a");

                    if (i == 0) {
                        if (href.size() > 0) {
                            String[] temp_ar = href.get(0).text("href").toString().split("\"");
                            getTestDescription(temp_ar[1]);
                            break;

                        }
                    }

                }
            }

        }

    }

    public static void replaceDetailsTable(String path) throws IOException {

        File source = new File(path);
        Document report = null;
        try {
            report = Jsoup.parse(source, "UTF-8");
        } catch (IOException e) {
            System.out.println("Unable to open [" + source.getAbsolutePath() + "] for parsing!");
        }
        Elements dom = report.children();
        Elements tds = report.select("table[id=tableStyle] td");  // select the tds from your table
        String temp_key = "";
        for (Element td : tds) {  // loop through them

            String[] temp_ar = td.toString().split("\"");
            String Key = temp_ar[1];
            String Status = "";

            if (td.toString().contains("pass.png")) {
                Status = "pass";
            }
            if (td.toString().contains("fail.png")) {
                Status = "fail";
            }
            if (td.toString().contains("skip.png")) {
                Status = "skip";
            }

            if (TestCaseDesMap.containsKey(temp_key) && Status.length() > 1) {
                TestcaseStatusMap.put(temp_key, Status);
                temp_key = "";
            }

            if (td.text().contains("Test Method")) {  // found the one you want
                String TestcaseDes;
                if (!TestCaseDesMap.containsKey(Key)) {
                    TestcaseDes = "  ---------       ";
                    TestCaseDesMap.put(Key, TestcaseDes);
                    temp_key = Key;

                } else {
                    TestcaseDes = TestCaseDesMap.get(Key);
                    temp_key = Key;
                    // TestcaseStatusMap.put(Key, Status);
                }
                td.text(TestcaseDes);
                // Replace with your text
            }
        }

        Elements ths = report.select("table[id=tableStyle] th");  // select the tds from your table
        for (Element th : ths) {  // loop through them

            if (th.text().contains("Method Type")) {  // found the one you want
                th.text("TestCase Description");

            }
            if (th.text().contains("Test Case Name")) {  // found the one you want
                th.text("Testng Method");

            }
        }

        if (!source.canWrite()) {
            System.out.println("Can't write this file!");//Just check if the file is writable or not
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(source));
        bw.write(dom.toString()); //toString will give all the elements as a big string
        bw.close();  //Close to apply the changes
      //  genarateFailureReport(new File("C:\\Users\\yanamalp\\Desktop\\Gen_jelly\\HTML_Design_Files\\CSS\\HtmlReport.html"), "c:\\");

    }

}
