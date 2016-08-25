
package com.hp.test.framework.staf;

/**
 * The <code>ProductDamagedException</code> should be thrown when we know that exception can only
 * happen when the product is not functioning correctly.
 * 
 */
public class ProductDamagedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7846746981037099361L;

    /**
     * Raw exception.
     * 
     */
    public ProductDamagedException() {}

    /**
     * Exception with message.
     * 
     * @param message
     */
    public ProductDamagedException(final String message) {
        super(message);
    }

    /**
     * Exception with cause.
     * 
     * @param cause
     */
    public ProductDamagedException(final Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * 
     * @param message
     * @param cause
     */
    public ProductDamagedException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
