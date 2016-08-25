
package com.hp.test.framework.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

public class Action extends SeleniumTagSupport {
	protected String test;
	protected String failureExplanation;

	public String getFailureExplanation() {
		return failureExplanation;
	}

	public void setFailureExplanation(String failureExplanation) {
		this.failureExplanation = failureExplanation;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public void doTag(XMLOutput arg0) throws MissingAttributeException,
			JellyTagException {
		if("false".equals(test)) {
			context.setVariable("outcome", Boolean.toString(false));
			context.setVariable("failureExplanation", failureExplanation);
		}
	}
	
}
