/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author sayedmo
 */
public class ClickLinkinTableTag extends SeleniumTagSupport {

    private String id;
    private String value;
    static Logger logger = Logger.getLogger(ClickLinkinTableTag.class);

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
        logger.info("Started Execution of ClickLinkinTableTag");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");
        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        String elelocator = "";
        switch (bylocator) {
            case "id": {
                logger.info("Executing case id");
                try {
                    WebElement table = driver.findElement(By.id(locator));
                    elelocator = "WebElement table = driver.findElement(By.id(\"" + locator + "\"));\n";
                    List<WebElement> links = table.findElements(By.tagName("a"));
                    for (WebElement link : links) {
//               String s = link.getText();
//               s=s.trim();
//               System.out.println(s);
                        if (link.getText().equalsIgnoreCase(value)) {
                            logger.info("Executing if condition for each link");
                            link.click();
                            logger.info("Clicked the link successfully");
                            break;
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
            }
            case "name": {
                logger.info("Executing case name");
                try {
                    WebElement table = driver.findElement(By.name(locator));
                    elelocator = "WebElement table = driver.findElement(By.name(\"" + locator + "\"));\n";
                    List<WebElement> links = table.findElements(By.tagName("a"));
                    for (WebElement link : links) {
//               String s = link.getText();
//               s=s.trim();
//               System.out.println(s);
                        if (link.getText().equalsIgnoreCase(value)) {
                            logger.info("Executing if condition for each link");
                            link.click();
                            logger.info("Clicked the link successfully");
                            break;
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
            }
            case "xpath": {
                logger.info("Executing case xpath");
                try {
                    WebElement table = driver.findElement(By.xpath(locator));
                    elelocator = "WebElement table = driver.findElement(By.xpath(\"" + locator + "\"));\n";
                    List<WebElement> links = table.findElements(By.tagName("a"));
                    for (WebElement link : links) {
//               String s = link.getText();
//               s=s.trim();
//               System.out.println(s);
                        if (link.getText().equalsIgnoreCase(value)) {
                            logger.info("Executing if condition for each link");
                            link.click();
                            logger.info("Clicked the link successfully");
                            break;
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
            }

            case "css": {
                logger.info("Executing case css");
                try {
                    WebElement table = driver.findElement(By.cssSelector(locator));
                    elelocator = "WebElement table = driver.findElement(By.cssSelector(\"" + locator + "\"));\n";
                    List<WebElement> links = table.findElements(By.tagName("a"));
                    for (WebElement link : links) {
//               String s = link.getText();
//               s=s.trim();
//               System.out.println(s);
                        if (link.getText().equalsIgnoreCase(value)) {
                            logger.info("Executing if condition for each link");
                            link.click();
                            logger.info("Clicked the link successfully");
                            break;
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found" + e.getMessage());

                }
            }
            logger.info("Completed Execution of ClickLinkinTableTag");
        }

        String Command = "try { \n " + elelocator + " List<WebElement> links = table.findElements(By.tagName(\"a\"));\n ";
        Command = Command + "for (WebElement link : links) {\n if (link.getText().equalsIgnoreCase(\""+value+"\")) { \n";
        Command = Command + " link.click();\n break;\n}\n}\n";
        Command = Command + "}catch (NoSuchElementException e) { System.out.println(\"Element not found\" + e.getMessage());}";
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write(Command);
            } catch (IOException ex) {
                System.out.println("Exception in Writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
    }
}
