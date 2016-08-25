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
public class SelectSaveandOKonDownloadDialogTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(AutoitClickOKonPrintTag.class);

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException {
        logger.info("Started Execution of SelectSaveandOKonDownloadDialogTag");
        WebDriver driver = getSelenium();

        try {
            Robot robot = new Robot();

            //Pressky ALT+S
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_S);

            //Release ALT
            robot.keyRelease(KeyEvent.VK_ALT);

            //Press Enter
            robot.keyPress(KeyEvent.VK_ENTER);

            //Release Enter
            robot.keyRelease(KeyEvent.VK_ENTER);

        } catch (AWTException e) {
            logger.error("Exception in the Save and Ok on Download dialog"+ "\n" + e.getMessage());
        }

        logger.info("Completed Execution of SelectSaveandOKonDownloadDialogTag");
    }
}
