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
 * @author mana, patra
 * Usage: <sel:cleartext id="ObjectID" />
 */

public class ClearTextTag extends SeleniumTagSupport {
	
	static Logger logger = Logger.getLogger(ClearTextTag.class);
    private String id;
    //private String value;

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of TypeTag");
        WebDriver driver = getSelenium();
        int loc = id.indexOf("=");

        String locator = id.substring(loc + 1);
        String bylocator = id.substring(0, loc);
        
        switch (bylocator.toLowerCase()) {
                      
            case "model": {
            	try {
                    logger.info("Excecuting case ng-model");
                    
                    driver.findElement(ByAngular.model(locator)).clear();
                   
                } catch (NoSuchElementException e) {
                    System.out.println("Element not found"+ "\n" + e.getMessage());
                }
                break;
            }
        }
       
        logger.info("End of Execution of TypeTag");
    }

}
