package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class MouseHoverTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(MouseHoverTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of MouseHoverTag function");
        WebDriver driver = getSelenium();
        String temp_ar[] = id.split("=", 2);
        Actions actions = new Actions(driver);
        String Command = "";
        switch (temp_ar[0].toLowerCase()) {
            case "id": {
                logger.info("Executing case id");
                try {
                    WebElement webelement = driver.findElement(By.id(temp_ar[1]));
                    actions.moveToElement(webelement).perform();
                    Command = "  WebElement webelement= driver.findElement(By.id(" + temp_ar[1] + "));\n";
                    Command = Command + "Actions actions1 = new Actions(driver);\n";
                    Command = Command + "actions1.moveToElement(webelement).perform();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found" + e.getMessage());
                }
                break;
            }

            case "name": {
                logger.info("Executing case name");
                try {
                    WebElement webelement = driver.findElement(By.name(temp_ar[1]));
                    actions.moveToElement(webelement).perform();
                    Command = "  WebElement webelement= driver.findElement(By.name(" + temp_ar[1] + "));\n";
                    Command = Command + "Actions actions1 = new Actions(driver);\n";
                    Command = Command + "actions1.moveToElement(webelement).perform();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }

                break;
            }

            case "xpath": {
                logger.info("Executing case xpath");
                try {
                	logger.info("MouseHoverTag temp_arr[1]: " + temp_ar[1]);
                	WebElement webelement = driver.findElement(By.xpath(temp_ar[1]));
                	//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", webelement);
                    actions.moveToElement(webelement).perform();
                    Command = "  WebElement webelement= driver.findElement(By.xpath(" + temp_ar[1] + "));\n";
                    Command = Command + "Actions actions1 = new Actions(driver);\n";
                    Command = Command + "actions1.moveToElement(webelement).perform();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }

                break;
            }

            case "css": {
                logger.info("Executing case xpath");
                try {
                    WebElement webelement = driver.findElement(By.cssSelector(temp_ar[1]));
                    actions.moveToElement(webelement).perform();
                    Command = "  WebElement webelement= driver.findElement(By.cssSelector(" + temp_ar[1] + "));\n";
                    Command = Command + "Actions actions1 = new Actions(driver);\n";
                    Command = Command + "actions1.moveToElement(webelement).perform();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
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
        logger.info("Completed Execution of MouseHoverTag function");
    }

}
