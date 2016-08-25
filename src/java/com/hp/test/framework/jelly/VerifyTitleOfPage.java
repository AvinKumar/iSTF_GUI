package com.hp.test.framework.jelly;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

/**
 * TODO: Verify the Title of Web Page
 *
 * @author sayedmo
 */
public class VerifyTitleOfPage extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyTitleOfPage.class);

    private String expected;

    public String getexpected() {
        return expected;
    }

    public void setexpected(String expected) {
        this.expected = expected;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyTitelOfPage function");
        final WebDriverException exception = new WebDriverException();
        WebDriver driver = getSelenium();
        String command = "";
        String actualTitle = "";
        try {
            actualTitle = driver.getTitle();
            if (expected.equals(actualTitle)) {
                System.out.println("Verification Successful - The correct title is displayed on the web page.");
                ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                ATUReports.add("Verify Title of the Page", expected, actualTitle, false);

            } else {
                System.out.println("Verification Failed - An incorrect title is displayed on the web page.");
                ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                ATUReports.add("Verify Title of the Page", expected, actualTitle, false);
                org.testng.Assert.fail("Verify Title of the Page" + actualTitle + "actualTitle" + expected);
            }
        } catch (WebDriverException e) {
            System.out.println("Element not found"+ "\n" + e.getMessage());
            throw new WebDriverException(e.getMessage());
        }
        command = "String actualTitle = driver.getTitle();\n";
        command = command + "org.junit.Assert.assertEquals(" + expected + ", actualTitle);";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in Writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }

        logger.info("Completed Execution of VerifyTitleOfPage function");
    }

}
