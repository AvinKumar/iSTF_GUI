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
import org.openqa.selenium.interactions.Actions;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class DoubleClickTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(DoubleClickTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of DoubleClickTag function");
        WebDriver driver = getSelenium();
        //String temp_ar[] = id.split("=");
        Actions actions = new Actions(driver);
        String Command = "";
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        System.out.println("locator" + locator);
        System.out.println("id" + bylocator);
        switch (bylocator) {
            case "id": {
                logger.info("Executing case id");
                try {
                    WebElement webelement = driver.findElement(By.id(locator));
                    actions.doubleClick(webelement).perform();
                    Command = " WebElement webelement= driver.findElement(By.id(" + locator + "));\n";
                    Command = Command + "Actions actions = new Actions(driver);\n";
                    Command = Command + "actions.doubleClick(webelement).perform();\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in double click method"+ "\n" + e.getMessage());
                }
                break;
            }

            case "name": {
                logger.info("Executing case name");
                try {
                    WebElement webelement = driver.findElement(By.name(locator));
                    actions.doubleClick(webelement).perform();
                    Command = " WebElement webelement= driver.findElement(By.name(" + locator + "));\n";
                    Command = Command + "Actions actions = new Actions(driver);\n";
                    Command = Command + "actions.doubleClick(webelement).perform();\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in double click method"+ "\n" + e.getMessage());
                }
                break;
            }

            case "xpath": {
                try {
                    logger.info("Executing case xpath");
                    //System.out.println("param1:"+temp_ar[0]+"param2:"+temp_ar[1]);
                    WebElement webelement = driver.findElement(By.xpath(locator));
                    actions.doubleClick(webelement).perform();
                    Command = " WebElement webelement= driver.findElement(By.xpath(" + locator + "));\n";
                    Command = Command + "Actions actions = new Actions(driver);\n";
                    Command = Command + "actions.doubleClick(webelement).perform();\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in double click method"+ "\n" + e.getMessage());
                }
                break;
            }

            case "css": {
                logger.info("Executing case xpath");
                try {
                    WebElement webelement = driver.findElement(By.cssSelector(locator));
                    actions.doubleClick(webelement).perform();
                    Command = " WebElement webelement= driver.findElement(By.cssSelector(" + locator + "));\n";
                    Command = Command + "Actions actions = new Actions(driver);\n";
                    Command = Command + "actions.doubleClick(webelement).perform();\n";
                } catch (Exception e) {
                    logger.error("Exception occurred in double click method"+ "\n" + e.getMessage());
                }
                break;
            }
        }

        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }

        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(Command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        logger.info("Completed Execution of DoubleClickTag function");
    }

}
