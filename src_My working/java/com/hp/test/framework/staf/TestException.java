
package com.hp.test.framework.staf;

import java.util.Formatter;

/**
 * <code>TestException</code> should be thrown when there is unexpected error in a test.
 * 
 */
public class TestException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4858567722963070640L;

    /**
     * No-message exception.
     */
    public TestException() {}

    /**
     * Messaged exception.
     * 
     * @param message
     */
    public TestException(final String message) {
        super(message);
    }

    /**
     * Cause-only exception.
     * 
     * @param cause
     */
    public TestException(final Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * 
     * @param message
     * @param cause
     */
    public TestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Formatted message exception with cause.
     * 
     * @see java.util.Formatter#iformat by try catch block in case of throws.
     * 
     * @param cause
     * @param format
     *            A format string as described in Format string syntax.
     * 
     * @param args
     *            Arguments referenced by the format specifiers in the format string. If there are
     *            more arguments than format specifiers, the extra arguments are ignored. The
     *            maximum number of arguments is limited by the maximum dimension of a Java array as
     *            defined by the Java Virtual Machine Specification.
     * 
     */
    @SuppressWarnings("resource")
    // Since we are not using any streams.
    public TestException(final Throwable cause, final String format, final Object... args) {
        super(new Formatter().format(format, args).toString(), cause);
    }
}
