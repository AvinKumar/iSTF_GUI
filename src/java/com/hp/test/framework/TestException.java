
/*
 * Confidential and Proprietary
 * (c) Copyright 1999 - 2009 Stratify, Inc. ( f/k/a PurpleYogi f/k/a Calpurnia ). All rights reserved.
 * The foregoing shall not be deemed to indicate that this source has been published. 
 * Instead, it remains a trade secret of Stratify, Inc.
 *
 * TestException.java
 *
 * Created on Feb 27, 2009
 */

package com.hp.test.framework;

/**
 * Test Exception 
 * @author arajasekar
 */
public class TestException extends Exception {

    public TestException() {
        super();
    }

    public TestException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestException(String message) {
        super(message);
    }

    public TestException(Throwable cause) {
        super(cause);
    }
    
}
