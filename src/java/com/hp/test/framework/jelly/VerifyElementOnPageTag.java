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
import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.Listeners;

import com.angular.ngwebdriver.ByAngular;

@Listeners({ATUReportsListener.class, ConfigurationListener.class,
    MethodListener.class})
public class VerifyElementOnPageTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyElementOnPageTag.class);
    private String id;
//    private String info; /* refer the description in line 44 -Avin */
    Boolean isPresent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

/* The following commented code can be made use whenever we want to pass any information under the test description of the execution report.
   This can be achieved by making changes in the CreateJellyTestCase.java class--> Case: VerifyElementExist.
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
        logger.info("Started Execution of VerifyElementOnPageTag function");
        int i = 0;
        boolean found = false;
        // DefaultSelenium selenium=getSelenium();

        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        System.out.println("locator" + locator);
        System.out.println("id" + bylocator);
        String command = "";
        switch (bylocator) {

            case "id": {
                try {
                    isPresent = driver.findElements(By.id(locator)).size() > 0;
                    command = " isPresent = driver.findElements(By.id(" + locator + ")).size() > 0;";
                    if (isPresent) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator-->" + id, "Exists", "Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Exists", "Not Exists", false);
                        Assert.fail("looking for " + id + "Not Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
            case "name": {
                try {
                    isPresent = driver.findElements(By.name(locator)).size() > 0;
                    command = " isPresent = driver.findElements(By.name(" + locator + ")).size() > 0;";
                    if (isPresent) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator-->" + id, "Exists", "Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Exists", "Not Exists", false);
                        Assert.fail("looking for " + id + "Not Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
            case "xpath": {
                try {
                    isPresent = driver.findElements(By.xpath(locator)).size() > 0;
                    command = " isPresent = driver.findElements(By.xpath(" + locator + ")).size() > 0;";
                    if (isPresent) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator-->" + id, "Exists", "Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Exists", "Not Exists", false);
                        Assert.fail("looking for " + id + "Not Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
            case "css": {
                try {
                    isPresent = driver.findElements(By.cssSelector(locator)).size() > 0;
                    command = " isPresent = driver.findElements(By.cssSelector(" + locator + ")).size() > 0;";
                    if (isPresent) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator-->" + id, "Exists", "Exists", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Exists", "Not Exists", false);
                        Assert.fail("looking for " + id + "Not Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
            
            case "model": {
                try {
                    isPresent = driver.findElements(ByAngular.model(locator)).size() > 0;
                    command = " isPresent = driver.findElements(By.cssSelector(" + locator + ")).size() > 0;";
                    if (isPresent) {

                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator " + id, "Exits", "Exits", false);

                    } else {
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Exits", "Not Exits", false);
                        Assert.fail("looking for " + id + "Not Exists");

                    }
                } catch (Exception e) {
                    System.out.println("Exception thrown"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
        }

        command = command + "  if (!isPresent)org.junit.Assert.Fail(\"Element on the page\");elseorg.junit.Assert.pass(\"element not on the page\");}";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }

        logger.info("Completed Execution of VerifyElementOnPageTag function");
    }

}
