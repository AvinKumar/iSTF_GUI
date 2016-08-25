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
 * TODO: Verify the Element is disabled this is applied to all controls like
 * button, checkbox, radiobutton, checkbox, combobox etc
 *
 * @author sayedmo
 */
public class VerifyDisableTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyDisableTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyDisableTag function");
        WebElement EnableDisable;
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
                    EnableDisable = driver.findElement(By.id(locator));
                    command = " EnableDisable = driver.findElement(By.id(" + locator + "));";
                    if (EnableDisable.isEnabled()) {
                        System.out.print("\nWebElement is enabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "Disabled", "Enabled", false);
                        org.testng.Assert.fail("with the Locator" + id + "Enabled");

                    } else {
                        System.out.print("\nWebElement is disabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Disabled", "Disabled", false);
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
                    EnableDisable = driver.findElement(By.name(locator));
                    command = " EnableDisable = driver.findElement(By.name(" + locator + "));";
                    if (EnableDisable.isEnabled()) {
                        System.out.print("\nWebElement is enabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "Disabled", "Enabled", false);
                        org.testng.Assert.fail("with the Locator" + id + "Enabled");

                    } else {
                        System.out.print("\nWebElement is disabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Disabled", "Disabled", false);
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
                    EnableDisable = driver.findElement(By.xpath(locator));
                    command = " EnableDisable = driver.findElement(By.xpath(" + locator + "));";
                    if (EnableDisable.isEnabled()) {
                        System.out.print("\nWebElement is enabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "Disabled", "Enabled", false);
                        org.testng.Assert.fail("with the Locator" + id + "Enabled");

                    } else {
                        System.out.print("\nWebElement is disabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Disabled", "Disabled", false);
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
                    EnableDisable = driver.findElement(By.cssSelector(locator));
                    command = " EnableDisable = driver.findElement(By.cssSelector(" + locator + "));";
                    if (EnableDisable.isEnabled()) {
                        System.out.print("\nWebElement is enabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("with the Locator" + id, "Disabled", "Enabled", false);
                        org.testng.Assert.fail("with the Locator" + id + "Enabled");

                    } else {
                        System.out.print("\nWebElement is disabled. Take your action.");
                        ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                        ATUReports.add("looking for " + id, "Disabled", "Disabled", false);
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

        command = command + "  if (EnableDisable.isEnabled())org.junit.Assert.Fail(\"Element is enabled\");elseorg.junit.Assert.pass(\"element is disabled\");}";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        logger.info("Completed Execution of VerifyDisableTag function");
    }

}
