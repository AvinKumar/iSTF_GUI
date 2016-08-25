
package com.hp.test.framework.jelly;

import java.util.concurrent.TimeUnit;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * TODO: Class Description this is for implicit wait
 * @author sayedmo
 */
public class ImplicitWaitTag extends SeleniumTagSupport {
        static Logger logger = Logger.getLogger(ImplicitWaitTag.class);
	private int time; /*seconds */
        WebDriver driver = null;
    
    public int getTime() {
		return time;
	}


	public void setTime(int time) {
		this.time = time;
	}

	@Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
            logger.info("Started Execution of ImplicitWaitTag function");
            try {
            driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
            } catch(Exception e) {
                logger.error("Exception occurred in implicit wait"+ "\n" + e.getMessage());
            }
            logger.info("Completed Execution of ImplicitWaitTag function");
    }

}
