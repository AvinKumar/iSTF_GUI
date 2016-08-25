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
public class SwitchToChildBrowserTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(SwitchToChildBrowserTag.class);

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of SwitchToChildBrowser");
        WebDriver driver = getSelenium();
        try {
    //Set <String> set1=driver.getWindowHandles();
            //Iterator <String> window=set1.iterator();
            //String ParentWindow=window.next();
            //String ChildWindow=window.next();

            for (String ChildWindow : driver.getWindowHandles()) {
                driver.switchTo().window(ChildWindow);
            }
            setSelenium((WebDriver) driver);

        } catch (Exception e) {
            logger.error("Exception switching Child Window"+ "\n" + e.getMessage());
        }
        logger.info("Completed Execution of SwitchToChildBrowser");
    }
}
