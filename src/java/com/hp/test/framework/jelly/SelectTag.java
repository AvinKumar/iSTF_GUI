package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;

import java.io.IOException;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import com.angular.ngwebdriver.ByAngular;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class SelectTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(SelectTag.class);

    private String id;
    private String value;
    String multipleSelection[];
    /* milliseconds */

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {

        //DefaultSelenium selenium=getSelenium();
        logger.info("Started Execution of SelectTag");
        WebDriver driver = getSelenium();
        String command = "";
        //selenium.select(id,label);
        //new Select(driver.findElement(By.id(id))).selectByVisibleText(label);

        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "id": {
                try {
                    logger.info("Executing case id");
                    if (value.contains("::")) {
                        multipleSelection = value.split("::");
                        for (String valueToBeSelected : multipleSelection) {

                            new Select(driver.findElement(By.id(locator))).selectByVisibleText(valueToBeSelected);
                            driver.findElement(By.id(locator)).sendKeys(Keys.CONTROL);
                        }
                    } else {
                        new Select(driver.findElement(By.id(locator))).selectByVisibleText(value);
                        command = " new Select(driver.findElement(By.id(\"" + locator + "\"))).selectByVisibleText(\"" + value + "\");\n";
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;

            }

            case "name": {
                try {
                    logger.info("Executing case name");
                    if (value.contains("::")) {
                        multipleSelection = value.split("::");
                        for (String valueToBeSelected : multipleSelection) {

                            new Select(driver.findElement(By.name(locator))).selectByVisibleText(valueToBeSelected);
                            driver.findElement(By.name(locator)).sendKeys(Keys.CONTROL);
                        }
                    } else {
                        new Select(driver.findElement(By.name(locator))).selectByVisibleText(value);
                        command = " new Select(driver.findElement(By.name(\"" + locator + "\"))).selectByVisibleText(\"" + value + "\");\n";
                        //  driver.findElement(By.name(locator))("label="+value) ;
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }

            case "xpath": {
                try {
                    logger.info("Executing case xpath");
                    if (value.contains("::")) {
                        multipleSelection = value.split("::");
                        for (String valueToBeSelected : multipleSelection) {

                            new Select(driver.findElement(By.xpath(locator))).selectByVisibleText(valueToBeSelected);
                            driver.findElement(By.xpath(locator)).sendKeys(Keys.CONTROL);
                        }
                    } else {
                        new Select(driver.findElement(By.xpath(locator))).selectByVisibleText(value);
                        command = " new Select(driver.findElement(By.xpath(\"" + locator + "\"))).selectByVisibleText(\"" + value + "\");\n";
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }

            case "css": {
                try {
                    logger.info("Executing case css");
                    if (value.contains("::")) {
                        multipleSelection = value.split("::");
                        for (String valueToBeSelected : multipleSelection) {

                            new Select(driver.findElement(By.cssSelector(locator))).selectByVisibleText(valueToBeSelected);
                            driver.findElement(By.xpath(locator)).sendKeys(Keys.CONTROL);
                        }
                    } else {
                        new Select(driver.findElement(By.cssSelector(locator))).selectByVisibleText(value);
                        command = " new Select(driver.findElement(By.cssSelector(\"" + locator + "\"))).selectByVisibleText(\"" + value + "\");\n";
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
            
            case "model": {
                try {
                    logger.info("Executing case model");
                      	driver.findElement(ByAngular.model(locator)).click();
                      	driver.findElement(By.xpath("//option[text()=" +"'" + value + "']")).click();
                    	driver.findElement(ByAngular.model(locator)).click();
                   
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
        }
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(command);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        logger.info("Completed Execution of SelectTag");

    }

}
