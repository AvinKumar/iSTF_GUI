package com.hp.test.framework.jelly;

import atu.testng.reports.ATUReports;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

/**
 * This class checks whether the element specified by the locator exists or
 * visible on the page.
 *
 * @author sayedmo
 */
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.Listeners;
//
//@Listeners({ATUReportsListener.class, ConfigurationListener.class,
//    MethodListener.class})
public class VerifyElementNotExistTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyElementNotExistTag.class);

    private String id;
//    private String info; /* refer the description in line 46 -Avin */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

/* The following commented code can be made use whenever we want to pass any information under the test description of the execution report.
   This can be achieved by making changes in the CreateJellyTestCase.java class--> Case: VerifyElementNotExist.
   We need to pass this info parameter from that class and the text details can be obtained from the MasterModel XML.
*/
    
//    public String getInfo() {
//        return info;
//    }
//
//    public void setInfo(String info) {
//        this.info = info;
//    }

    @Override
    public void doTag(XMLOutput output) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyElementNotExist function");
        int i = 0;
        boolean found = false;
        // DefaultSelenium selenium=getSelenium();
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        System.out.println("locator" + locator);
        System.out.println("id" + bylocator);
        String command="";
        switch (bylocator) {

            case "id": {
                try {
                    int count = driver.findElements(By.id(locator)).size();
                    command=" int count = driver.findElements(By.id("+locator+")).size();";
                    System.out.println("element found " + found);
                    if (count == 0) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Exists", "Not Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Exists", "Exists", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
				break;
            }
            case "name": {
                try {
                    int count = driver.findElements(By.name(locator)).size();
                    command=" int count = driver.findElements(By.name("+locator+")).size();";
                    System.out.println("element found " + found);
                    if (count == 0) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Exists", "Not Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Exists", "Exists", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
				break;
            }
            case "xpath": {
                try {
                    int count = driver.findElements(By.xpath(locator)).size();
                    command=" int count = driver.findElements(By.xpath("+locator+")).size();";
                    logger.info("element found " + found);
                 
                    if (count == 0) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Exists", "Not Exists", false);
                    //    ATUReports.add("Passed Step", LogAs.PASSED, new CaptureScreen(
                     //           CaptureScreen.ScreenshotOf.DESKTOP));

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Exists", "Exists", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
				break;
            }
            case "css":
                try {
                    int count = driver.findElements(By.cssSelector(locator)).size();
                    command=" int count = driver.findElements(By.css("+locator+")).size();";
                    System.out.println("element found " + found);
                    if (count == 0) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Exists", "Not Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Exists", "Exists", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
				break;
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        
          command=command+"  if (count==0)org.junit.Assert.Fail(\"Element not the page\");elseorg.junit.Assert.pass(\"element on the page\");}";
           if(GenerateperformanceTests)
        {
         try {
            StartBrowserTag.JunitTestcase_fw.write(command);
        } catch (IOException ex) {
            System.out.println("Exception in Writing Junit testcase file"+ex.getMessage());
        }
        }
       
        logger.info("Completed Execution of VerifyElementNotExist function");
    }

}
