package com.hp.test.framework.jelly;

import atu.testng.reports.ATUReports;
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
 * TODO: Verify the Element is Not displayed/not visible this is applied to all
 * controls like button, checkbox, radiobutton, checkbox, combobox etc
 *
 * @author sayedmo
 */
public class VerifyNotDisplayedTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyNotDisplayedTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyNotDisplayedTag function");
        WebElement Element;
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
                    System.out.println("Executing case id");
                    Element = driver.findElement(By.id(locator));
                    command = " Element = driver.findElement(By.id(" + locator + "));";
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Displayed", "Displayed", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Displayed");

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Displayed", "Not Displayed", false);
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
                    command = " Element = driver.findElement(By.name(" + locator + "));";
                    Element = driver.findElement(By.name(locator));
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Displayed", "Displayed", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Displayed");

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Displayed", "Not Displayed", false);
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
                    command = " Element = driver.findElement(By.xpath(" + locator + "));";
                    Element = driver.findElement(By.xpath(locator));
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Displayed", "Displayed", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Displayed");

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Displayed", "Not Displayed", false);
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
                    if (Element.isDisplayed()) {
                        System.out.print("\nWebElement is Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("Verifying the element with the Locator" + id, "Not Displayed", "Displayed", false);
                        org.testng.Assert.fail("Verifying the element with the Locator" + id + "Displayed");

                    } else {
                        System.out.print("\nWebElement is Not Displayed. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Not Displayed", "Not Displayed", false);

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
        command = command + "  if (Element.isDisplayed())org.junit.Assert.Fail(\"not displayed\");elseorg.junit.Assert.pass(\"displayed\");}";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }

        logger.info("Completed Execution of VerifyNotDisplayedTag function");
    }

}
