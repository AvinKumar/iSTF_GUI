/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hp.test.framework.jelly;

import atu.testng.reports.ATUReports;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author sayedmo
 */
public class PopupVerifyTextTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(PopupVerifyTextTag.class);

    private String expected, actual;

    public String getexpected() {
        return expected;
    }

    public void setexpected(String expected) {
        this.expected = expected;
    }

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        WebDriver driver = getSelenium();
        logger.info("Started Execution of PopupVerifyTextTag");
        try {
            Alert alert = driver.switchTo().alert();
            actual = alert.getText();
            if (actual.contains(expected)) {
                System.out.println(expected + " text is on this popup");
                ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                ATUReports.add("Verification of popup text", expected, actual, false);
            } else {
                System.out.println(expected + " text is NOT on this popup");
                ATUReports.setAuthorInfo("Core Automation Team", "DATE()", "1.0");
                ATUReports.add("Verification of popup text", expected, actual, false);
                //try {
                org.testng.Assert.fail("Verification of popup text" + actual + "Expected" + expected);
            }
        } catch (Exception e) {
            logger.error("Exception occurred while getting text from alert"+ "\n" + e.fillInStackTrace());
        }
        logger.info("Completed Execution of PopupVerifyTextTag");
    }
}
