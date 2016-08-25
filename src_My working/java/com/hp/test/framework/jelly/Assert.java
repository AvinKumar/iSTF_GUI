
package com.hp.test.framework.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

public class Assert extends SeleniumTagSupport {
	protected boolean test;
	protected String failureExplanation;

	public String getFailureExplanation() {
		return failureExplanation;
	}

	public void setFailureExplanation(String failureExplanation) {
		this.failureExplanation = failureExplanation;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	@Override
	public void doTag(XMLOutput arg0) throws MissingAttributeException,
			JellyTagException {
		if(!test) {
			context.setVariable("outcome", Boolean.FALSE);
			context.setVariable("failureExplanation", failureExplanation);
		} 
	}
	
}
