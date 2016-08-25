package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.angular.ngwebdriver.ByAngular;
import com.angular.ngwebdriver.NgWebDriver;


/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class TypeTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(TypeTag.class);
    private String id;
    private String value;

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

    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of TypeTag");
        WebDriver driver = getSelenium();
        String Command = "";
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        //System.out.println("User name:" + temp_ar[1] + "password:" + value + "ID" + id);

        switch (bylocator.toLowerCase()) {
            case "id": {
                try {
                    logger.info("Executing case id");
                    driver.findElement(By.id(locator)).clear();
                    driver.findElement(By.id(locator)).sendKeys(value);
                    Command = "  driver.findElement(By.id(\"" + locator + "\")).clear();\n";
                    Command = Command + "driver.findElement(By.id(\"" + locator + "\")).sendKeys(\"" + value + "\");\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }

            case "name": {
                try {
                    logger.info("Excecuting case name");
                    driver.findElement(By.name(locator)).clear();
                    driver.findElement(By.name(locator)).sendKeys(value);
                    Command = "  driver.findElement(By.name(\"" + locator + "\")).clear();\n";
                    Command = Command + "driver.findElement(By.name(\"" + locator + "\")).sendKeys(\"" + value + "\");\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }

            case "xpath": {
                try {
                    logger.info("Executing case xpath");
                    driver.findElement(By.xpath(locator)).clear();
                    driver.findElement(By.xpath(locator)).sendKeys(value);
                    Command = "  driver.findElement(By.xpath(\"" + locator + "\")).clear();\n";
                    Command = Command + "driver.findElement(By.xpath(\"" + locator + "\")).sendKeys(\"" + value + "\");\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }

            case "css": {
                try {
                    logger.info("Executing case xpath");
                    driver.findElement(By.cssSelector(locator)).clear();
                    driver.findElement(By.cssSelector(locator)).sendKeys(value);
                    Command = "  driver.findElement(By.cssSelector(\"" + locator + "\")).clear();\n";
                    Command = Command + "driver.findElement(By.cssSelector(\"" + locator + "\")).sendKeys(\"" + value + "\");\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }
            
            // Added by : mana, patra
            // This set input value based on angular model name
            
            case "model": {
            	try {
                    logger.info("Excecuting case ng-model");
                    
                    driver.findElement(ByAngular.model(locator)).clear();
                    driver.findElement(ByAngular.model(locator)).sendKeys(value);
                    //driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
                    
                    Command = "  driver.findElement(By.name(\"" + locator + "\")).clear();\n";
                    Command = Command + "driver.findElement(By.name(\"" + locator + "\")).sendKeys(\"" + value + "\");\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
        }

        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(Command);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(TypeTag.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.info("End of Execution of TypeTag");
    }

}
