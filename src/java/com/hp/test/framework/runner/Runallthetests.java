/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.runner;



import com.hp.test.framework.generatejellytess.CreateJellyTestCase;
import com.hp.test.framework.jelly.XpathSupport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;

/**
 *
 * @author $hpedservice
 */
//@Listeners({ATUReportsListener.class, ConfigurationListener.class,    MethodListener.class})
@Listeners({ SuiteListener.class })
public class Runallthetests {
     static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Runallthetests.class.getName());
   
     JellyContext context = null;
      @BeforeTest 
     public void pres(ITestContext ctx) throws JellyException, FileNotFoundException
     {
            context=new JellyContext();
            context.setVariable("xpath", new XpathSupport());
            context.setVariable("env", System.getProperties());
            context.setVariable("outcome", Boolean.TRUE);
//         Map<String,String> suiteName_list = ctx.getCurrentXmlTest().getTestParameters();
//             for(String a:suiteName_list.keySet())
//             System.out.println("key"+a+"value"+suiteName_list.get(a));
           
     }
     
     
//   CompareKeywords ckw=new CompareKeywords();
    @DataProvider(name = "getjellyFiles")
    public Object[][] getJellyTests() throws Exception {
       
     
    String path="";

        ISuite suiteListner = SuiteListener.getAccess();
        String runningSuite=suiteListner.getName();
//        System.out.println("**********************"+runningSuite);
//        System.out.println("*****"+TestngSuite.testngParams.get(runningSuite));
    log.info("Running Functionality::>"+TestngSuite.testngParams.get(runningSuite));
    log.info("********************************************************************");
     ArrayList testcases=CreateJellyTestCase.readTestFiles(TestngSuite.testngParams.get(runningSuite));//   ckw.getdatafromKWYH();
     
        
        Object[][] obj = new Object[testcases.size()][2];
        log.info("Number of TestCases"+ testcases.size());
        for (int i = 0; i < testcases.size() ; i++) {
            obj[i] = new Object[]{testcases.get(i), "Executing jelly script "+testcases.get(i) };
        }
        return obj;
    }

    @Test(enabled =true, dataProvider = "getjellyFiles")
    public void test(String parm1, Object parm2) throws Exception {
       // ckw.compar_kywords(parm1);
          context.runScript(new InputSource(new FileInputStream(parm1)), XMLOutput.createXMLOutput(new StringWriter()));

    }
     
//    @Test
//public void f(ITestContext ctx) {
//  Map<String,String> suiteName_list = ctx.getCurrentXmlTest().getTestParameters();
//  for(String a:suiteName_list.keySet())
//            System.out.println("key"+a+"value"+suiteName_list.get(a));
//}
}
