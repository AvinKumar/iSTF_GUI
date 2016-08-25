
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
public class CancelPopupTag extends SeleniumTagSupport {
    static Logger logger = Logger.getLogger(CancelPopupTag.class);
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        WebDriver driver = getSelenium();
        logger.info("Started Execution of CancelPopupTag");   
        try {
        Alert alert = driver.switchTo().alert();
        alert.dismiss();
        } catch(Exception e) {
            logger.error("Exception occured in cancel popup"+ "\n" + e.getMessage());
        }
        String Command="Alert alert = driver.switchTo().alert();\n alert.dismiss();\n";
        
          if(GenerateperformanceTests)
        {
          try {
            StartBrowserTag.JunitTestcase_fw.write(Command);
        } catch (IOException ex) {
            System.out.println("Exception in Writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
        logger.info("Completed Execution of CancelPopupTag");
    }
}
