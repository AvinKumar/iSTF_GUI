/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.runner;

/**
 *
 * @author PYanamalamanda
 */

import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import com.hp.test.framework.generatejellytess.ModelProperties;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;





import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestngSuite {

    public static List<XmlSuite> mySuites = new ArrayList<XmlSuite>();
    public static TestNG myTestNG = new TestNG();
    public static Map<String, String> testngParams;
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestngSuite.class.getName());
    public void runTestNGTest(Map<String, String> testngParams) {

        List<Class> reports = new ArrayList<Class>();
        reports.add(ATUReportsListener.class);
        reports.add(ConfigurationListener.class);
        reports.add(MethodListener.class);

        myTestNG.setListenerClasses(reports);

        for (String SuiteName : testngParams.keySet()) {
            XmlSuite mySuite = new XmlSuite();

            mySuite.setName(SuiteName);
            XmlTest myTest = new XmlTest(mySuite);
            myTest.addParameter(SuiteName, testngParams.get(SuiteName));
            List<XmlClass> myClasses = new ArrayList<XmlClass>();
            myClasses.add(new XmlClass("com.hp.test.framework.runner.Runallthetests"));
            myTest.setXmlClasses(myClasses);
            List<XmlTest> myTests = new ArrayList<XmlTest>();
            myTests.add(myTest);
            mySuite.setTests(myTests);
            mySuites.add(mySuite);

        }
        log.info("Started Running Functionalities (No of Functionalities ::> "+mySuites.size()+")");
        myTestNG.setXmlSuites(mySuites);
        myTestNG.run();

    }

    public static void main(String args[]) {

        TestngSuite testngsuite = new TestngSuite();
        ModelProperties modelproperties=new ModelProperties();
        String root_path=modelproperties.getProperty("JELLY_TESTS_LOCATION");
        testngParams = new LinkedHashMap<String, String>();
        File f = new File(root_path);
        List<String> directories = new ArrayList<String>();
       log.info("Started Listing Fucntionality Suites in the path::::>"+root_path);
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                directories.add(file.getName());

            }
        }

        for (String directorie : directories) {
            testngParams.put(directorie, root_path + "\\" + directorie);
        }

        testngsuite.runTestNGTest(testngParams);

    }

}
