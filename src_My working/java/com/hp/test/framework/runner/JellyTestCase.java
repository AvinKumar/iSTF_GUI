
/*
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2009 Stratify, Inc. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published. 
 * Instead, it remains a trade secret of Stratify, Inc.
 *
 * JellyTestCase.java
 *
 * Created on Feb 26, 2009
 */

package com.hp.test.framework.runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.xml.sax.InputSource;

import com.hp.test.framework.jelly.XpathSupport;



/**
 * TODO: Class Description
 * @author arajasekar
 */
public class JellyTestCase extends TestCase{
    private File jellyFile;

    public JellyTestCase(File jellyFile) {
        this.jellyFile=jellyFile;
    }

    @Override
    public String getName() {
        return jellyFile.getAbsolutePath();
    }

    public void runTest() {
    	JellyContext context = null;
        try {
            context=new JellyContext();
            context.setVariable("xpath", new XpathSupport());
            context.setVariable("env", System.getProperties());
            context.setVariable("outcome", Boolean.TRUE);
            context.runScript(new InputSource(new FileInputStream(jellyFile)), XMLOutput.createXMLOutput(new StringWriter()));
            Boolean outcome=(Boolean) context.getVariable("outcome");
            if(!outcome) {
            	fail((String)context.getVariable("failureExplanation"));
            }
        } catch (FileNotFoundException e) {
            fail(e.getMessage());
        } catch (JellyException e) {
            fail(e.getMessage());
        } catch (Throwable e) {
        	fail((String)context.getVariable("failureExplanation") + e.getMessage());
        }
    }
    
    
}
