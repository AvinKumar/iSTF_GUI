/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jelly;

import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author sayedmo
 */
public class GetVariabletag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(GetVariabletag.class);

    private String id;
    private String var, name;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getvar() {
        return var;
    }

    public void setvar(String var) {
        this.var = var;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException {
        logger.info("Started Execution of GetVariableTag");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);

        switch (bylocator.toLowerCase()) {
            case "id": {
                try {
                    logger.info("Executing case id");
                    name = driver.findElement(By.id(locator)).getAttribute("value");
                    context.setVariable(var, name);
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }

            case "name": {
                try {
                    logger.info("Excecuting case name");
                    name = driver.findElement(By.name(locator)).getAttribute("value");
                    context.setVariable(var, name);
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }

            case "xpath": {
                try {
                    logger.info("Executing case xpath");
                    name = driver.findElement(By.xpath(locator)).getAttribute("value");
                    context.setVariable(var, name);
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }

            case "css": {
                try {
                    logger.info("Executing case css");
                    name = driver.findElement(By.cssSelector(locator)).getAttribute("value");
                    context.setVariable(var, name);
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());

                }
                break;
            }
        }
        logger.info("End of Execution of GetVariableTag");

    }

}
