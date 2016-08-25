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
 *
 * @author sayedmo
 */
public class StopBrowserTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(StopBrowserTag.class);

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        WebDriver driver = getSelenium();
        logger.info("Started Execution of StopBrowserTag");
        //driver.close
        if (GenerateperformanceTests) {
            try {
                StartBrowserTag.JunitTestcase_fw.write("}catch(Exception e){}");
                StartBrowserTag.JunitTestcase_fw.write(" if(driver!=null){ driver.quit();}}}\n");
                StartBrowserTag.JunitTestcase_fw.close();

            } catch (IOException ex) {
                System.out.println("Exception in writing Junit testcase file"+ "\n" + ex.getMessage());
            }
        }
        if (driver != null) {
            driver.close();
            driver.quit();

            System.out.println("inside the quit method");
        }
        logger.info("Completed Execution of StopBrowserTag");
    }
}
