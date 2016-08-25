/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jelly;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author sayedmo
 */
public class ClickOKonWinDialogTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(AutoitClickOKonPrintTag.class);

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException {
        logger.info("Started Execution of ClickOkonWinDialogTag");
        WebDriver driver = getSelenium();

        try {
            Robot robot = new Robot();

            //Pressky Enter/OK Button
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            logger.error("Exception in the Click OK in Windows dialog"+ "\n" + e.getMessage());
        }

        logger.info("Completed Execution of ClickOkonWinDialogTag");
    }
}
