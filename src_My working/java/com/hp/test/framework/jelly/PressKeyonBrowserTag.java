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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 *
 * @author sayedmo
 */
public class PressKeyonBrowserTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(PressKeyonBrowserTag.class);

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        logger.info("Started Execution of PressKeyonBrowserTag");
        WebDriver driver = getSelenium();
        try {
            Actions action = new Actions(driver);
            if (value.contains("+")) {

                String[] temp = value.split("\\+");
                switch (temp[0].toUpperCase()) {
                    case "CONTROL":
                        action.sendKeys(Keys.chord(Keys.CONTROL, temp[1].toLowerCase())).build().perform();
                        break;
                    case "ALT":
                        action.sendKeys(Keys.chord(Keys.ALT, temp[1].toLowerCase())).build().perform();
                        break;
                }
            } else {
                action.sendKeys(Keys.valueOf(value)).build().perform();
            }
        } catch (Exception e) {
            logger.error("Exception in the Pres Key on Browser"+ "\n" + e.getMessage());
        }
        logger.info("End of Execution of PressKeyonBrowserTag");
    }

}
