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

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class SelectCheckBoxTag extends SeleniumTagSupport {

    private String id;
    private String value;
    static Logger logger = Logger.getLogger(SelectCheckBoxTag.class);

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
        logger.info("Started Execution of SelectCheckBoxTag");
        WebDriver driver = getSelenium();
        String Commnad = "";
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "id": {
                logger.info("Executing case id");
                try {
                    WebElement checkBox = driver.findElement(By.id(locator));
                    if (!checkBox.isSelected()) {
                        checkBox.click();
                    }
                    Commnad = "driver.findElement(By.id(\"" + locator + "\")).click();\n";
                    // Commnad=Commnad+" if(!checkBox.isSelected()){checkBox.click();}\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;

            }

            case "name": {
                logger.info("Executing case name");
                try {
                    WebElement checkBox = driver.findElement(By.name(locator));
                    if (!checkBox.isSelected()) {
                        checkBox.click();
                    }
                    Commnad = "driver.findElement(By.name(\"" + locator + "\")).click();\n";
                    //  Commnad=Commnad+" if(!checkBox.isSelected()){checkBox.click();}\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }

            case "xpath": {
                logger.info("Executing case xpath");
                try {
                    WebElement checkBox = driver.findElement(By.xpath(locator));
                    checkBox.click();
                    if (!checkBox.isSelected()) {
                        checkBox.click();
                    }
                    Commnad = "driver.findElement(By.xpath(\"" + locator + "\")).click();\n";
                    //Commnad=Commnad+" if(!checkBox.isSelected()){checkBox.click();}\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }

            case "css": {
                logger.info("Executing case css");
                try {
                    WebElement checkBox = driver.findElement(By.cssSelector(locator));
                    checkBox.click();
                    if (!checkBox.isSelected()) {
                        checkBox.click();
                    }
                    Commnad = "driver.findElement(By.cssSelector(\"" + locator + "\")).click();\n";
                    //Commnad=Commnad+" if(!checkBox.isSelected()){checkBox.click();}\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
        }
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(Commnad);
            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }
        logger.info("Completed Eexcution of SelectCheckbox function");
    }

}
