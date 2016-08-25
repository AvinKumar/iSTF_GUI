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
import org.openqa.selenium.WebDriver;


/**
 *
 * @author sayedmo
 */
public class SwitchToParentBrowserTag extends SeleniumTagSupport {
    static Logger logger = Logger.getLogger(SwitchToParentBrowserTag.class);
    
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
    logger.info("Started Execution of SwitchToParentBrowser");
    WebDriver driver = getSelenium();
    
    String Parent= StartBrowserTag.MainBrowserWindow;
    try {

        driver.switchTo().window(Parent);
        
    } catch(Exception e) {
        logger.error("Exception switching Parent Window"+ "\n" + e.getMessage());
    }
    logger.info("Completed Execution of SwitchToParentBrowser");
    }
}

