/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import java.util.List;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author sayedmo
 */
public class ClickCheckboxBasedonTextLinkinTableTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(ClickCheckboxBasedonTextLinkinTableTag.class);

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

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of ClickCheckboxBasedonTextLinkinTableTag");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");
        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        String findelementby="";
        
        switch (bylocator) {
            case "id": {
                logger.info("Executing case id");
                try {
                    WebElement table = driver.findElement(By.id(locator));
                    findelementby="WebElement table = driver.findElement(By.id("+locator+"));\n";
                    // Now get all the TR elements from the table 
                    List<WebElement> allRows = table.findElements(By.tagName("tr"));

                    // And iterate over them, getting the cells 
                    for (WebElement row : allRows) {
                        List<WebElement> cells = row.findElements(By.tagName("td"));

                        // iterate each cell and match value
                        for (WebElement cell : cells) {

                            if (cell.getText().equalsIgnoreCase(value)) {
                                System.out.println(cell.getText());
                                WebElement checkbox = row.findElement(By.xpath(".//td/input[@type='checkbox']"));
                                checkbox.click();
                                break;

                            }
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
                     findelementby="WebElement table = driver.findElement(By.name("+locator+"));\n";
                    // Now get all the TR elements from the table 
                    List<WebElement> allRows = table.findElements(By.tagName("tr"));

                    // And iterate over them, getting the cells 
                    for (WebElement row : allRows) {
                        List<WebElement> cells = row.findElements(By.tagName("td"));

                        // iterate each cell and match value
                        for (WebElement cell : cells) {

                            if (cell.getText().equalsIgnoreCase(value)) {
                                System.out.println(cell.getText());
                                WebElement checkbox = row.findElement(By.xpath(".//td/input[@type='checkbox']"));
                                checkbox.click();
                                break;

                            }
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
                    findelementby="WebElement table = driver.findElement(By.xpath("+locator+"));\n";
                    // Now get all the TR elements from the table 
                    List<WebElement> allRows = table.findElements(By.tagName("tr"));

                    // And iterate over them, getting the cells 
                    for (WebElement row : allRows) {
                        List<WebElement> cells = row.findElements(By.tagName("td"));

                        // iterate each cell and match value
                        for (WebElement cell : cells) {

                            if (cell.getText().equalsIgnoreCase(value)) {
                                System.out.println(cell.getText());
                                WebElement checkbox = row.findElement(By.xpath(".//td/input[@type='checkbox']"));
                                checkbox.click();
                                break;

                            }
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
                    findelementby="WebElement table = driver.findElement(By.cssSelector("+locator+"));\n";
                    // Now get all the TR elements from the table 
                    List<WebElement> allRows = table.findElements(By.tagName("tr"));

                    // And iterate over them, getting the cells 
                    for (WebElement row : allRows) {
                        List<WebElement> cells = row.findElements(By.tagName("td"));

                        // iterate each cell and match value
                        for (WebElement cell : cells) {

                            if (cell.getText().equalsIgnoreCase(value)) {
                                System.out.println(cell.getText());
                                WebElement checkbox = row.findElement(By.xpath(".//td/input[@type='checkbox']"));
                                checkbox.click();
                                break;

                            }
                        }

                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                
                
            }
            
            logger.info("Completed Execution of ClickCheckboxBasedonTextLinkinTableTag");
        }
        
        String  Command="try { \n "+ findelementby +" List<WebElement> allRows = table.findElements(By.tagName(\"tr\"));\n";
                Command=Command+"for (WebElement row : allRows) {\n  List<WebElement> cells = row.findElements(By.tagName(\"td\"));\n";
                Command=Command+"for (WebElement cell : cells) {\n ";
                Command=Command+"   if (cell.getText().equalsIgnoreCase(value)) {\n";
                Command=Command+"   System.out.println(cell.getText());\n";
                Command=Command+" WebElement checkbox = row.findElement(By.xpath(\".//td/input[@type='checkbox']\"));\n";
                Command=Command+" checkbox.click();\nbreak\n";
                Command=Command+"}\n}\n}\n";
                Command=Command+"} catch (NoSuchElementException e) { System.out.println(\"Element not found\" + e.getMessage());}";
              
        
           if(GenerateperformanceTests)
        {
          try {
            StartBrowserTag.JunitTestcase_fw.write(Command);
        } catch (IOException ex) {
            System.out.println("Exception in Writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
       

    }
}
