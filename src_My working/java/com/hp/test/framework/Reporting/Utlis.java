/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.Reporting;

import com.google.common.io.Files;
import com.hp.test.framework.generatejellytess.ModelProperties;
import com.hp.test.framework.htmparse.GenerateFailReport;
import static com.hp.test.framework.htmparse.GenerateFailReport.genarateFailureReport;
import com.hp.test.framework.htmparse.HtmlParse;
import com.hp.test.framework.htmparse.UpdateTestCaseDesciption;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import org.testng.annotations.Test;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author $hpedservice
 */
public class Utlis {

    static Logger log = Logger.getLogger(Utlis.class.getName());

    static List<File> fileList = new ArrayList<>();

    public static void main(String ar[]) throws IOException {

        ArrayList<String> runs_list = new ArrayList<String>();
        String basepath = replacelogs();
        String path = basepath + "ATU Reports\\Results";
        File directory = new File(path);
        File[] subdirs = directory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        String htmlReport = basepath + "HTML_Design_Files\\CSS\\HtmlReport.html";
        for (File dir : subdirs) {

            runs_list.add(dir.getName());
        }
        String Reports_path = basepath + "ATU Reports\\";
        int LatestRun = Utlis.getLastRun(Reports_path);
        String Last_run_path = Reports_path + "Results\\Run_" + String.valueOf(LatestRun) + "\\";

        HtmlParse.replaceMainTable(false, new File(Last_run_path + "/CurrentRun.html"));
        HtmlParse.replaceCountsinJSFile(new File("HTML_Design_Files/JS/3dChart.js"), Last_run_path);
        UpdateTestCaseDesciption.basepath = Last_run_path;
        UpdateTestCaseDesciption.getTestCaseHtmlPath(Last_run_path + "/CurrentRun.html");
        UpdateTestCaseDesciption.replaceDetailsTable(Last_run_path + "/CurrentRun.html");
        //   GenerateFailReport.genarateFailureReport(new File(htmlReport), Reports_path + "Results\\Run_" + String.valueOf(LatestRun));
        genarateFailureReport(new File(htmlReport), Reports_path + "Results\\");
        Map<String, List<String>> runCounts = GetCountsrunsWise(runs_list, path);

        int success = replaceCounts(runCounts, path);

        if (success == 0) {
            File SourceFile = new File(path + "\\lineChart_temp.js");
            File RenameFile = new File(path + "\\lineChart.js");

            renameOriginalFile(SourceFile, RenameFile);

            File SourceFile1 = new File(path + "\\barChart_temp.js");
            File RenameFile1 = new File(path + "\\barChart.js");

            renameOriginalFile(SourceFile1, RenameFile1);

            Utlis.getpaths(Reports_path + "\\Results\\Run_" + String.valueOf(LatestRun));
            try {
                Utlis.replaceMainTable(false, new File(Reports_path + "index.html"));
                Utlis.replaceMainTable(true, new File(Reports_path + "results\\" + "ConsolidatedPage.html"));
                Utlis.replaceMainTable(true, new File(Reports_path + "Results\\Run_" + String.valueOf(LatestRun) + "CurrentRun.html"));

                fileList.add(new File(Reports_path + "\\Results\\Run_" + String.valueOf(LatestRun) + "CurrentRun.html"));
                for (File f : fileList) {
                    Utlis.replaceMainTable(true, f);
                }

            } catch (Exception ex) {
                log.info("Error in updating Report format" + ex.getMessage());
            }

            Last_run_path = Reports_path + "Results\\Run_" + String.valueOf(LatestRun) + "\\";

            //   HtmlParse.replaceMainTable(false, new File(Last_run_path + "/CurrentRun.html"));
            //   HtmlParse.replaceCountsinJSFile(new File("../HTML_Design_Files/JS/3dChart.js"), Last_run_path);
            ArrayList<String> to_list = new ArrayList<String>();
            ReportingProperties reportingproperties = new ReportingProperties();
            String temp_eml = reportingproperties.getProperty("TOLIST");
            String JenkinsURL = reportingproperties.getProperty("JENKINSURL");

            String ScreenShotsDir = reportingproperties.getProperty("ScreenShotsDirectory");
            Boolean cleanScreenshotsDir = Boolean.valueOf(reportingproperties.getProperty("CleanPreScreenShots"));
            Boolean generatescreenshots = Boolean.valueOf(reportingproperties.getProperty("GenerateScreenShots"));
            String HtmlreportFilePrefix=reportingproperties.getProperty("HtmlreportFilePrefix");

            if (cleanScreenshotsDir) {
                if (!CleanFilesinDir(ScreenShotsDir)) {
                    log.error("Not able to Clean the Previous Run Details in the paht" + ScreenShotsDir);
                } else {
                    log.info("Cleaning Previous Run Details in the paht" + ScreenShotsDir + "Sucess");
                }
            }
            List<String> scr_fileList;
             List<String> html_fileList;
            if (generatescreenshots) {
                scr_fileList = GetFileListinDir(ScreenShotsDir,"png");

                int len = scr_fileList.size();
                len = len + 1;

                screenshot.generatescreenshot(Last_run_path + "CurrentRun.html", ScreenShotsDir + "screenshot_" + len + ".png");
                File source=new File(Reports_path + "Results\\HtmlReport.html");
                File dest=new File(ScreenShotsDir +HtmlreportFilePrefix+"_HtmlReport.html");
              //  Files.copy(f.toPath(), (new File((ScreenShotsDir+HtmlreportFilePrefix+"_HtmlReport.html").toPath(),StandardCopyOption.REPLACE_EXISTING);
                FileUtils.copyFile(source, dest);
                scr_fileList.clear();
               
            }

            scr_fileList = GetFileListinDir(ScreenShotsDir,"png");
            html_fileList=GetFileListinDir(ScreenShotsDir,"html");

            if (temp_eml.length() > 1) {
                String[] to_list_temp = temp_eml.split(",");

                if (to_list_temp.length > 0) {
                    for (String to_list_temp1 : to_list_temp) {
                        to_list.add(to_list_temp1);
                    }
                }
                if (to_list.size() > 0) {
                    screenshot.generatescreenshot(Last_run_path + "CurrentRun.html", Last_run_path + "screenshot.png");

                    //    cleanTempDir.cleanandCreate(Reports_path, LatestRun);
                    // ZipUtils.ZipFolder(Reports_path + "temp", Reports_path + "ISTF_Reports.zip");
                    if (generatescreenshots) {
                        SendingEmail.sendmail(to_list, JenkinsURL, scr_fileList,html_fileList);
                    } else {
                        SendingEmail.sendmail(to_list, JenkinsURL, Reports_path + "/Results/HtmlReport.html", Last_run_path + "screenshot.png");
                    }

                    //  FileUtils.deleteQuietly(new File(Reports_path + "ISTF_Reports.zip"));
                    // FileUtils.deleteDirectory(new File(Reports_path + "temp"));
                }
            }
        }
    }

    public static Map<String, List<String>> GetCountsrunsWise(ArrayList runs, String path) {
        String string = "";

        Map<String, List<String>> Run_counts = new HashMap<String, List<String>>();
        for (int count = 0; count < runs.size(); count++) {
            Boolean runhasData = false;
            String file = path + "\\" + runs.get(count) + "\\pieChart.js";

            //reading   
            try {
                InputStream ips = new FileInputStream(file);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String line;
                ArrayList<String> ar = new ArrayList<String>();
                while ((line = br.readLine()) != null) {
                    runhasData = true;
                    if (line.contains("var data")) {
                        // log.info(line);
                        String tem_ar[] = line.split("=");
                        //log.info(tem_ar[1]);
                        String temp_ar1[] = tem_ar[1].split(",");
                        String counts = "";
                        for (int i = 0; i < temp_ar1.length; i++) {
                            temp_ar1[i] = temp_ar1[i].replace("[", "");
                            temp_ar1[i] = temp_ar1[i].replace("]", "");
                            temp_ar1[i] = temp_ar1[i].replace("'", "");
                            String temp = temp_ar1[i];
                            ar.add(temp);
                            counts = counts + temp;
                            // log.info(temp_ar1[i]);

                        }
                        string += line + "\n";
                        Run_counts.put(runs.get(count).toString(), ar);
                        log.info("counts run wise-->" + runs.get(count) + "***" + ar.toString());
                        break;
                    }
                }
                br.close();
            } catch (Exception e) {
                log.info(e.toString());

            }
            if (!runhasData) {
                ArrayList<String> ar = new ArrayList<String>();
                ar.add("Passed");
                ar.add("0");
                ar.add("Failed");
                ar.add("0");
                ar.add("Skipped");
                ar.add("0;");

                Run_counts.put(runs.get(count).toString(), ar);
                log.info("has NO*******  data for " + runs.get(count));
            }
        }
        return Run_counts;

    }

    public static int replaceCounts(Map<String, List<String>> Run_counts, String path) {
        // String file = path + "\\"+runs.get(count)+"\\pieChart.js";

        //reading
        String passed_count = "";
        String failed_count = "";
        String skipped_count = "";
        int first = 0;
        Map<Integer, List<String>> Sort_list = new HashMap<Integer, List<String>>();

        for (String item : Run_counts.keySet()) {
            String temp_ar[] = item.split("_");
            Sort_list.put(Integer.parseInt(temp_ar[1]), Run_counts.get(item));

        }

        SortedSet<Integer> keys = new TreeSet<Integer>(Sort_list.keySet());

        for (Integer key : keys) {
            List<String> ar = Sort_list.get(key);
            if (first == 0) {
                passed_count = ar.get(1);
                failed_count = ar.get(3);
                skipped_count = ar.get(5);
                first++;
                continue;
            }

            passed_count = passed_count + "," + ar.get(1);
            failed_count = failed_count + "," + ar.get(3);
            skipped_count = skipped_count + "," + ar.get(5);
        }
        skipped_count = skipped_count.replace(";", "");
        log.info("Passed Counts::::" + passed_count);
        log.info("failed_count" + failed_count);
        log.info("skipped_count" + skipped_count);
        File line_chart = new File(path + "\\lineChart.js");
        File line_chart_temp = new File(path + "\\lineChart_temp.js");
        File bar_chart = new File(path + "\\barChart.js");
        File bar_chart_temp = new File(path + "\\barChart_temp.js");

        create_tempfile(line_chart, line_chart_temp, passed_count, failed_count, skipped_count, "line");
        create_tempfile(bar_chart, bar_chart_temp, passed_count, failed_count, skipped_count, "bar");
        return 0;
    }

    public static void create_tempfile(File SourceFile, File TempFile, String passed_count, String failed_count, String skipped_count, String graphType) {
        try {
            InputStream ips = new FileInputStream(SourceFile);
            InputStreamReader ipsr = new InputStreamReader(ips);
            BufferedReader br = new BufferedReader(ipsr);

            FileWriter fw = new FileWriter(TempFile);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter fileOut = new PrintWriter(bw);
            //        fileOut.println (string+"\n test of read and write !!"); 
            //  fileOut.close();

            String line;
            String variable = "s";
            if (graphType.equals("line")) {
                variable = "line";

            }
            //ArrayList<String> ar = new ArrayList<String>();
            int i = 0;
            while ((line = br.readLine()) != null) {

                if (i == 0) {
                    fileOut.println(line);
                    i = i + 1;
                    continue;

                }
                if (line.contains("var " + variable)) {
                    i = i + 1;

                    String[] tem_ar = line.split("=");

                    String variable1 = "var " + variable + "1 = [" + passed_count + "];";
                    String variable2 = "var " + variable + "2 = [" + failed_count + "];";
                    String variable3 = "var " + variable + "3 = [" + skipped_count + "];";
                    if (i == 2) {
                        fileOut.println(variable1);
                        fileOut.println(variable2);
                        fileOut.println(variable3);
                    }
                    // string += line + "\n";
                    // Run_counts.put(runs.get(count).toString(), ar);

                } else {
                    fileOut.println(line);
                }
            }

            fileOut.close();
            br.close();
            log.info("Temp file after replacing counts created successfully--> " + TempFile.getPath());
        } catch (Exception e) {
            log.error(e.toString());

        }
    }

    public static void renameOriginalFile(File OriginalFile, File RenameFile) {

        try {

            if (OriginalFile.exists()) {
                if (RenameFile.exists()) {
                    RenameFile.delete();
                    log.info("Already file exists with this name--so deleted");
                }
                log.info("" + RenameFile.getAbsolutePath());
                if (OriginalFile.renameTo(RenameFile)) {
                    log.info("Rename succesful");
                } else {
                    log.info("Rename failed");
                }
            }
        } catch (Exception e) {
            log.error("rename file failed" + e.getMessage());
        }
    }

    public static String gettimeinMilliSec() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String dateInString = sdf.format(now);
        Date date = sdf.parse(dateInString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.getTimeInMillis());
    }

    public static void replaceMainTable(Boolean onlyMainTable, File f) {

        String content = "";
        String content1 = "";
        String replace = "";
        String imagePath = "../../../../../../../";
        if (f.getName().contentEquals("index.html")) {
            imagePath = "";
        }

        if (f.getName().contentEquals("ConsolidatedPage.html")) {
            imagePath = "../";
        }

        if (f.getName().contentEquals("index.html")) {
            imagePath = "";
        }

        if (f.getName().contentEquals("CurrentRun.html")) {
            imagePath = "../../";
        }

        replace = "<tr id=\"header\" >" + "\n";
        replace = replace + "<td id=\"logo\"><img src=\"" + imagePath + "HTML_Design_Files/IMG/Framework_Logo.jpg\" alt=\"Logo\" height=\"70\" width=\"140\" /> <br/><i style=\"float:left;padding-left:20px;font-size:12px\">Reflections of Visionary Minds</i></td>";
        replace = replace + " <td id=\"headertext\">";
        replace = replace + "Intelligent Solutions Test Framework";
        replace = replace + "<div style=\"padding-right:20px;float:right\"><img src=\"" + imagePath + "HTML_Design_Files/IMG/hp.png\" height=\"70\" width=\"140\" /> </i></div>                </td>";
        replace = replace + " </tr>";

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));

            String str;
            Boolean found = false;
            boolean readingdone = false;
            while ((str = in.readLine()) != null) {

                content1 += str;
                if (!onlyMainTable) {
                    if (str.contains("<td id=\"content\">")) {

                        content1 += " <img src=\"HTML_Design_Files\\\\IMG\\reports.jpg\"  alt=\"BackGround\" height=\"500\" width=\"500\" /> ";
                        in.readLine();
                        continue;
                    }
                }
                if (str.contains("<table id=\"mainTable\">")) {
                    found = true;

                }

                if (found) {
                    while ((str = in.readLine()) != null) {

                        if (str.contains("</tr>")) {
                            found = false;
                            readingdone = true;
                            content1 += replace;
                            break;

                        }
                    }

                }

            }
            in.close();
            BufferedWriter out = new BufferedWriter(new FileWriter(f));

            out.write(content1);
            out.close();

        } catch (IOException e) {
        }
    }

    public static int getLastRun(String path) {
        File file = new File(path + "\\results\\");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        String temp_ar[] = directories[0].split("_");
        int max = Integer.parseInt(temp_ar[1]);
        for (int i = 1; i < directories.length; i++) {
            String temp_ar1[] = directories[i].split("_");

            int run = Integer.parseInt(temp_ar1[1]);

            if (run > max) {
                max = run;
            }

        }
        log.info(Arrays.toString(directories));
        log.info("Latest Run" + max);
        return max;
    }

    public static void getpaths(String path) {
        log.info("started getting all the documents paths :::" + path);
        File root = new File(path);
        File[] list = root.listFiles();
        if (list == null) {
            return;
        }
        for (File f : list) {
            if (f.isDirectory()) {
                getpaths(f.getAbsolutePath());

            } else {

                String filename = f.getName();
                String Abpath = f.getAbsolutePath();
                String extension = "";
                int i = filename.lastIndexOf('.');
                if (i >= 0) {
                    extension = filename.substring(i + 1);
                    if (extension.contentEquals("html")) {

                        fileList.add(new File(f.getAbsolutePath()));
                    }
                }

            }
        }
    }

    public static void Cleantemppath(String path) {
        File tmpLoc = new File(path);
        File tmpLoc1 = new File(path + "\\output");
        if (tmpLoc.exists()) {
            try {
                FileUtils.deleteDirectory(tmpLoc1);
                FileUtils.deleteDirectory(tmpLoc);

            } catch (IOException ex) {
                log.error("Exception" + ex.getMessage());
            }
        }
        if (!tmpLoc.exists()) {
            tmpLoc.mkdirs();
            tmpLoc1.mkdir();
        }

    }

    public static String replacelogs() throws IOException {
        URL location = Test.class.getProtectionDomain().getCodeSource().getLocation();

        String path = location.toString();
        path = path.substring(6, path.indexOf("lib"));
        path = path.replace("%20", " ");
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
        return path;
    }

    public static List<String> GetFileListinDir(String path,String extension) {
        List<String> filenames = new ArrayList<String>();
        File folder = new File(path);

        File[] listofFiles = folder.listFiles();

        for (File f : listofFiles) {
            if (f.isFile()) {
                if(f.getName().contains(extension))
                filenames.add(f.getName());
            }
        }
        return filenames;
    }

    public static boolean CleanFilesinDir(String path) {
        List<String> filenames = new ArrayList<String>();
        File folder = new File(path);

        File[] listofFiles = folder.listFiles();

        for (File f : listofFiles) {
            if (f.isFile()) {
                f.delete();
            }
        }

        List<String> FileList = GetFileListinDir(path,"*");

        if (FileList.size() == 0) {
            return true;
        } else {
            return false;
        }

    }

}
