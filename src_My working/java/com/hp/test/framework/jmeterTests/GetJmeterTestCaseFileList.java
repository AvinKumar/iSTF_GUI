/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jmeterTests;

import com.hp.test.framework.ReadProps.ReadJmeterConfigProps;
import static com.hp.test.framework.Reporting.Utlis.replacelogs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author yanamalp
 */
public class GetJmeterTestCaseFileList {

   // public static List<String> jmeterFileList;
    public static List getjmeterTestcaseList() {
        List<String> jmeterFileList;
        jmeterFileList = new ArrayList<String>();
        ReadJmeterConfigProps readjmeterconfigprops = new ReadJmeterConfigProps();
        String path = readjmeterconfigprops.getProperty("location.java.files");
        File dir = new File(path);
        FileFilter fileFilter = new WildcardFileFilter("*.java");
        File[] files = dir.listFiles(fileFilter);
        for (File file : files) {
            jmeterFileList.add(file.getName());
        }
        return jmeterFileList;
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }

    private static int runProcess(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        printLines(command + " stdout:", pro.getInputStream());
        printLines(command + " stderr:", pro.getErrorStream());
        pro.waitFor();
        //  System.out.println(command + " exitValue() " + pro.exitValue());
        return pro.exitValue();
    }

    public static void main(String ar[]) throws IOException {
        ReadJmeterConfigProps readjmeterconfigprops = new ReadJmeterConfigProps();
        String path = readjmeterconfigprops.getProperty("location.java.files");
        path = path + "/";
        // getjmeterTestcaseList();
        String basepath = replacelogs();
        String webdriver_path = basepath + "/libs/selenium-server-standalone-2.45.0.jar";
        try {
            String Command = "javac -d "+ path+"classes " +"-cp \"" + webdriver_path + "\" " + path + "*.java";
            if (runProcess(Command) != 0) {
                System.out.println("Error in compiling jmeter testcases");
                return;
            }
            System.out.println("Compiling the Sources succes");
          //  String temp_jar="jar cf  " + path + "JunitTests.jar " + path + "*.class .";
          //  System.out.println("**"+temp_jar);
            if (runProcess("jar cf  " + path + "JunitTests.jar -C " + path + "classes/ ." ) != 0) {
                System.out.println("Error in creating jar for jmeter testcases");
                return;
            }

            System.out.println("Creation of Jar file success");
            
            FileUtils.copyFileToDirectory(new File(path + "JunitTests.jar"), new File(basepath + "jmeter/apache-jmeter-2.13/lib/junit/"));
            System.out.println("Copy jar file to libs folder succcess");
            
              CreateJmxFilesforUI createjmxfilesforui = new CreateJmxFilesforUI();
              createjmxfilesforui.createJmxFilesforui();
              System.out.println("Jmeter Testplans Creation is success");
        } catch (Exception e) {
            System.out.println("Exception in making jars");
        }
    }

}
