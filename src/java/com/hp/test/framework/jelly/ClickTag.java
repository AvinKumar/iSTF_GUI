package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.angular.ngwebdriver.ByAngular;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class ClickTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(ClickTag.class);

    private String id, value;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getvalue() {
        return value;
    }

    public void setvalue(String value) {
        this.value = value;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of ClickTag function");
        WebDriver driver = getSelenium();
        String Command="";
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        System.out.println("locator" + locator);
        System.out.println("id" + bylocator);
        switch (bylocator) {
            case "id": {
                try {
                    System.out.println("Executing case id");
                    driver.findElement(By.id(locator)).click();
                    Command=" driver.findElement(By.id(\""+locator+"\")).click();";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }

            case "name": {
                try {
                    System.out.println("Executing case name");
                    driver.findElement(By.name(locator)).click();
                     Command=" driver.findElement(By.name(\""+locator+"\")).click();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }

            case "xpath": {
                try {
                    System.out.println("Executing case xpath");
                    
                    driver.findElement(By.xpath(locator)).click();
                     Command=" driver.findElement(By.xpath(\""+locator+"\")).click();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found" + e.getMessage());
                }
                
                break;
            }

            case "css": {
                try {
                    System.out.println("Identify element with CSS path and click");
                    driver.findElement(By.cssSelector(locator)).click();
                     Command=" driver.findElement(By.cssSelector(\""+locator+"\")).click();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
            
            // Added by: mana, patra
            // This clicks element by property angular binding
            // This takes two parameter: locator and it's value
            case "binding":
            case "exactbinding": {
            	String act, status="false";
                try {
                    System.out.println("Identify element with binding and click");
                    List<WebElement> we = driver.findElements(ByAngular.exactBinding(locator));
            		logger.info("Binding Size: " + we.size());
            		
            	for (int i = 0; i <= we.size(); i++) {
            		 if (we.get(i).getText().equalsIgnoreCase(value)) {
            			 logger.info("Clicking on text: " + value);
            			 we.get(i).click();
            			 status = "true";
            			 act=we.get(i).getText();
            			 break;
                     } else {
                    	 act=we.get(i).getText();
                     }
            	}
                     Command=" driver.findElement(By.cssSelector(\""+locator+"\")).click();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
            
            case "repeater": {
            	String act, status="false";
                try {
                    System.out.println("Identify element with binding and click");
                    List<WebElement> we = driver.findElements(ByAngular.repeater(locator));
            		logger.info("Repeater Size: " + we.size());
            		
            	for (int i = 0; i <= we.size(); i++) {
            		 if (we.get(i).getText().equalsIgnoreCase(value)) {
            			 logger.info("Clicking on text: " + value);
            			 we.get(i).click();
            			 status = "true";
            			 act=we.get(i).getText();
            			 break;
                     } else {
                    	 act=we.get(i).getText();
                     }
            	}
                     Command=" driver.findElement(By.cssSelector(\""+locator+"\")).click();\n";
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
        }
          if(GenerateperformanceTests)
        {
        try {
            StartBrowserTag.JunitTestcase_fw.write(Command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }
        logger.info("Completed Execution of ClickTag function");
    }

}
