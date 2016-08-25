package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class VerifyEnableDisableTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(VerifyEnableDisableTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of VerifyEnableDisableTag function");
        WebElement EnableDisable;
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        System.out.println("locator" + locator);
        System.out.println("id" + bylocator);
        String command="";
        switch (bylocator) {
            case "id": {
                System.out.println("Executing case id");
                EnableDisable = driver.findElement(By.id(locator));
                command="EnableDisable = driver.findElement(By.id("+locator+"));";
                if (EnableDisable.isEnabled()) {
                    System.out.print("\nWebElement is enabled. Take your action.");
                } else {
                    System.out.print("\nWebElement is disabled. Take your action.");
                }
                break;
            }

            case "name": {
                System.out.println("Executing case name");
                 command="EnableDisable = driver.findElement(By.name("+locator+"));";
                EnableDisable = driver.findElement(By.name(locator));
                if (EnableDisable.isEnabled()) {
                    System.out.print("\nWebElement is enabled. Take your action.");
                } else {
                    System.out.print("\nWebElement is disabled. Take your action.");
                }
                break;
            }

            case "xpath": {
                System.out.println("Executing case xpath");
                EnableDisable = driver.findElement(By.xpath(locator));
                 command="EnableDisable = driver.findElement(By.xpath("+locator+"));";
                if (EnableDisable.isEnabled()) {
                    System.out.print("\nWebElement is enabled. Take your action.");
                } else {
                    System.out.print("\nWebElement is disabled. Take your action.");
                }
                break;
            }

            case "css": {
                System.out.println("identify element with CSS path and click");
                EnableDisable = driver.findElement(By.cssSelector(locator));
                command="EnableDisable = driver.findElement(By.css("+locator+"));";
                if (EnableDisable.isEnabled()) {
                    System.out.print("\nWebElement is enabled. Take your action.");
                } else {
                    System.out.print("\nWebElement is disabled. Take your action.");
                }
                break;
            }
        }

        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }
          command=command+"  if (EnableDisable.isEnabled())org.junit.Assert.Fail(\"not Enabled\");elseorg.junit.Assert.pass(\"Enabled\");}";
           if(GenerateperformanceTests)
        {
         try {
            StartBrowserTag.JunitTestcase_fw.write(command);
        } catch (IOException ex) {
            System.out.println("Exception in Writing Junit testcase file"+ex.getMessage());
        }
        }

        logger.info("Completed Execution of VerifyEnableDisableTag function");
    }

}
