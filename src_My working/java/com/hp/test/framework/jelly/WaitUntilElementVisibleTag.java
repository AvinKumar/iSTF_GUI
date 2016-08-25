/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

/**
 *
 * @author sayedmo
 */
public class WaitUntilElementVisibleTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(WaitUntilElementVisibleTag.class);

    private String id;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of WaitUntilElementVisible function");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        System.out.println("locator" + locator);
        System.out.println("id" + bylocator);
        switch (bylocator) {
            case "id": {
                try {
                    System.out.println("Executing case id");
                    WebDriverWait wait = new WebDriverWait(driver, 300);
                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locator)));
                } catch (TimeoutException e) {
                    System.out.println("TimeOut Exception"+ "\n" + e.getMessage());
                }
                break;
            }

            case "name": {
                try {
                    System.out.println("Executing case name");
                    WebDriverWait wait = new WebDriverWait(driver, 300);
                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locator)));
                } catch (TimeoutException e) {
                    System.out.println("TimeOut Exception"+ "\n" + e.getMessage());
                }
                break;
            }

            case "xpath": {
                try {
                    System.out.println("Executing case xpath");
                    WebDriverWait wait = new WebDriverWait(driver, 300);
                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
                } catch (TimeoutException e) {
                    System.out.println("TimeOut Exception"+ "\n" + e.getMessage());
                }
                break;
            }

            case "css": {
                try {
                    System.out.println("identify element with CSS path and click");
                    WebDriverWait wait = new WebDriverWait(driver, 300);
                    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(locator)));
                } catch (TimeoutException e) {
                    System.out.println("TimeOut Exception"+ "\n" + e.getMessage());

                    break;
                }
            }

            try {
                Thread.sleep(5000);

            } catch (InterruptedException e) {

            }
            logger.info("Completed Execution of WaitUntilElementClickable function");
        }

    }
}
