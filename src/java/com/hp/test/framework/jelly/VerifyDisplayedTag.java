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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * TODO: Verify the Element is Displayed/Visible this is applied to all controls
 * like button, checkbox, radiobutton, checkbox, combobox etc
 *
 * @author sayedmo
 */
public class VerifyDisplayedTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyDisplayedTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyDisplayedTag function");
        WebElement Element;
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
                    System.out.println("Executing case id");
                    Element = driver.findElement(By.id(locator));
                    command="Element = driver.findElement(By.id("+locator+"));";
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "displayed", "displayed", false);

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "displayed", "not displayed", false);
                        org.testng.Assert.fail("with the Locator" + id + "not displayed");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "name": {
                try {
                    System.out.println("Executing case name");
                    Element = driver.findElement(By.name(locator));
                     command="Element = driver.findElement(By.name("+locator+"));";
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "displayed", "displayed", false);
                        ATUReports.add("Passed Step", LogAs.PASSED, new CaptureScreen(
                                CaptureScreen.ScreenshotOf.DESKTOP));

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "displayed", "not displayed", false);
                        org.testng.Assert.fail("with the Locator" + id + "not displayed");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "xpath": {
                try {
                    System.out.println("Executing case xpath");
                    Element = driver.findElement(By.xpath(locator));
                     command="Element = driver.findElement(By.xpath("+locator+"));";
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "displayed", "displayed", false);
                        ATUReports.add("Passed Step", LogAs.PASSED, new CaptureScreen(
                                CaptureScreen.ScreenshotOf.DESKTOP));

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "displayed", "not displayed", false);
                        org.testng.Assert.fail("with the Locator" + id + "not displayed");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }

            case "css": {
                try {
                    System.out.println("identify element with CSS path and click");
                    Element = driver.findElement(By.cssSelector(locator));
                     command="Element = driver.findElement(By.cssSelector("+locator+"));";
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "displayed", "displayed", false);
                        ATUReports.add("Passed Step", LogAs.PASSED, new CaptureScreen(
                                CaptureScreen.ScreenshotOf.DESKTOP));

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "displayed", "not displayed", false);
                        org.testng.Assert.fail("with the Locator" + id + "not displayed");
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                    throw new WebDriverException(e.getMessage());
                }
                break;
            }
        }

        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }
        
            command=command+"  if (Element.isDisplayed())org.junit.Assert.Fail(\"Element is displayed on the page\");elseorg.junit.Assert.pass(\"element is not displayed on the page\");}";
           if(GenerateperformanceTests)
        {
         try {
            StartBrowserTag.JunitTestcase_fw.write(command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
      
        logger.info("Completed Execution of VerifyDisplayedTag function");
    }

}
