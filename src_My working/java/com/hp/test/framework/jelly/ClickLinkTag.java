package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import java.util.Iterator;
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
 * TODO: Class Description
 *
 * @author sayedmo
 */
public class ClickLinkTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(ClickLinkTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of ClickTag function");
        WebDriver driver = getSelenium();
        String Command="";
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
      //  System.out.println("locator"+locator);
        //  System.out.println("id"+bylocator);

        switch (bylocator.toLowerCase()) {
            case "href": {
                logger.info("Executing case href");
                //driver.findElement(By.id(temp_ar[1])).click();
                List<WebElement> anchors = driver.findElements(By.tagName("a"));
                Iterator<WebElement> i = anchors.iterator();

                while (i.hasNext()) {
                    WebElement anchor = i.next();
                    if (anchor.getAttribute("href").contains(locator)) {
                        anchor.click();
                        break;
                    }
                }
                break;
            }
   
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
                    System.out.println("Element not found"+ "\n" + e.getMessage());
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

        }
          if(GenerateperformanceTests)
        {
        try {
            StartBrowserTag.JunitTestcase_fw.write(Command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ex.getMessage());
        }
        }
        try {
            Thread.sleep(5000);

        } catch (InterruptedException e) {

        }
        logger.info("Completed Execution of ClickTag function");
    }

}
