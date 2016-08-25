/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hp.test.framework.jelly;

import java.io.IOException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;


/**
 *
 * @author sayedmo
 */
public class AutoitClickOKonPrintTag extends SeleniumTagSupport {
    static Logger logger = Logger.getLogger(AutoitClickOKonPrintTag.class);
    
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException {
    logger.info("Started Execution of ClickOKonPrintTag");
    WebDriver driver = getSelenium();
   
    try {
        Process exec = Runtime.getRuntime().exec("conf/ClickOKonPrint.exe");
        
    } catch(IOException e) {
        
        logger.error("Exception while executing the autoit script/executable"+ "\n" + e.getMessage());
    }
   
    logger.info("Completed Execution of ClickOKonPrintTag");
    }
}