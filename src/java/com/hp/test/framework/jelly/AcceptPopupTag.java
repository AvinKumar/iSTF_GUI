
package com.hp.test.framework.jelly;


import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;

import org.openqa.selenium.WebDriver;

/**
 * TODO: Class Description
 * @author sayedmo
 */
public class AcceptPopupTag extends SeleniumTagSupport {
    static Logger logger = Logger.getLogger(AcceptPopupTag.class);
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        WebDriver driver = getSelenium();
        String Command="";
        logger.info("Started Execution of AcceptPopupTag");   
        try {
        Alert alert = driver.switchTo().alert();
        alert.accept();
        Command=" Alert alert = driver.switchTo().alert();\n  alert.accept();\n";
        } catch(Exception e) {
            logger.error("Exception occurred in Accept Pop up"+ "\n" + e.getMessage());
        }
          if(GenerateperformanceTests)
        {
         try {
            StartBrowserTag.JunitTestcase_fw.write(Command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
        logger.info("Completed Execution of AcceptPopupTag");
    }
}
