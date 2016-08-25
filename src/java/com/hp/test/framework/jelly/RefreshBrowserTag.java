
package com.hp.test.framework.jelly;

import static com.hp.test.framework.jelly.StartBrowserTag.GenerateperformanceTests;
import java.io.IOException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;

import org.openqa.selenium.WebDriver;

/**
 * TODO: Class Description
 * @author sayedmo
 */
public class RefreshBrowserTag extends SeleniumTagSupport {
    static Logger logger = Logger.getLogger(RefreshBrowserTag.class);
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        WebDriver driver = getSelenium();
        logger.info("Started Execution of RefreshBrowserTag");
        try {
        driver.navigate().refresh();
        } catch(Exception e) {
            logger.error("Exception occurred in refresh method"+ "\n" + e.getMessage());
        }
        logger.info("Completed Execution of RefreshBrowserTag");
        
        String Command="driver.navigate().refresh();\n";
        
          if(GenerateperformanceTests)
        {
          try {
            StartBrowserTag.JunitTestcase_fw.write(Command);
        } catch (IOException ex) {
            System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
        }
        }
    }
}