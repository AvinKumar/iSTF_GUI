
package com.hp.test.framework.jelly;

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
public class DeleteCookiesTag extends SeleniumTagSupport {

    static Logger logger = Logger.getLogger(DeleteCookiesTag.class);

    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        WebDriver driver = getSelenium();
        logger.info("Started Execution of DeleteCookiesTag function");
        try {
            driver.manage().deleteAllCookies();
        } catch (Exception e) {
            logger.error("Exception occurred while deleting cookies"+ "\n" + e.getMessage());
        }
        logger.info("Completed Execution of DeleteCookiesTag function");
    }
}
