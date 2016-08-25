
package com.hp.test.framework.jelly;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class Execute extends SeleniumTagSupport {
	protected String file;
        static Logger logger = Logger.getLogger(Execute.class);
        
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}


	@Override
	public void doTag(XMLOutput arg0) throws MissingAttributeException,
			JellyTagException {
       try {
            JellyContext context=new JellyContext();
            context.setVariable("xpath", new XpathSupport());
            context.setVariable("env", System.getProperties());
            context.setVariable("outcome", Boolean.TRUE);
            context.setVariable("selenium", getSelenium());
            context.runScript(new InputSource(new FileInputStream(file)), XMLOutput.createXMLOutput(new StringWriter()));
            Boolean outcome=(Boolean) context.getVariable("outcome");
            if(!outcome) {
//            	fail((String)context.getVariable("failureExplanation"));
            }
        } catch (FileNotFoundException e) {
        	logger.error("File: " + file + " not found.");
//            fail(e.getMessage());
        } catch (JellyException e) {
        	logger.error("Jelly Exception: " + e.getMessage());
//            fail(e.getMessage());
        }		
	}
	
}
