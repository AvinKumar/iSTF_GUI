package com.hp.test.framework.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;

/**
 * TODO: Class Description
 *
 * @author sayedmo
 * Usage: <sel:sleep value="1000" />
 */
public class SleepTag extends SeleniumTagSupport {

    int milliseconds;
    private String value; /* milliseconds */

    static Logger logger = Logger.getLogger(SleepTag.class);
    
    public String getvalue() {
        return value;
    }
    
    public void setvalue(String value) {
        this.value = value;
    }
    
    @Override
    public void doTag(XMLOutput arg0) throws MissingAttributeException, JellyTagException {
        
        
        logger.info("Started Execution of SleepTag");
        try {
            milliseconds= Integer.parseInt(value);
            Thread.sleep(milliseconds);
        }  catch (NumberFormatException | InterruptedException ex) {
            logger.error("Exception occured in Sleep method"+ "\n" + ex.getMessage());
        }
        logger.info("Completed Execution of SleepTag");
    }
    
}
