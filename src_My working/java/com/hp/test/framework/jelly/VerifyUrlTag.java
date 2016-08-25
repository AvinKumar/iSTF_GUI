package com.hp.test.framework.jelly;

import atu.testng.reports.ATUReports;
import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * TODO: Verify URL of Web Page
 *
 */
public class VerifyUrlTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyUrlTag.class);

    private String expected;

    public String getexpected() {
        return expected;
    }

    public void setexpected(String expected) {
        this.expected = expected;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyUrlTag function");
        final WebDriverException exception = new WebDriverException();
        WebDriver driver = getSelenium();
        String command="";
        String actualURL="";
        try {
            actualURL = driver.getCurrentUrl();
            if (expected.equals(actualURL)) {
                System.out.println("Verification Successful - The correct URL is displayed on the web page.");
                ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                ATUReports.add("Verifying URL", expected, actualURL, false);
                //System.out.println(expected);
                //System.out.println(actualURL);               
            } else {
                System.out.println("Verification Failed - An incorrect URL is displayed on the web page.");
                ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                ATUReports.add("Verifying URL", expected, actualURL, false);
                org.testng.Assert.fail("Verifying URL" + actualURL + "actualURL" + expected);
                //System.out.println(expected);
                //System.out.println(actualURL);
            }
        } catch (WebDriverException e) {
            System.out.println("Element not found"+ "\n" + e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
        command="String actualURL = driver.getCurrentUrl;\n";
        command=command+"org.junit.Assert.assertEquals("+expected+", actualURL);";
            if(GenerateperformanceTests)
        {
         try {
            StartBrowserTag.JunitTestcase_fw.write(command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
            
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }    

        logger.info("Completed Execution of VerifyUrlTag function");
    }
}
