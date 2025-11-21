package org.gnoss.apiWrapper.Excepciones;

/**
 * Exception produced if there are invalid arguments
 * @author salopez
 */
public class GnossAPIArgumentException extends GnossAPIException {
    
    /**
     * Constructor of GnossAPIArgumentException
     * @param message Message of the error
     * @param paramName Parameter with some error
     * @param originalException Original exception
     */
    public GnossAPIArgumentException(String message, String paramName, Exception originalException) {
        super(message, paramName, originalException);
    }
    
    /**
     * Constructor of GnossAPIArgumentException
     * @param message Message of the error
     * @param paramName Parameter with some error
     */
    public GnossAPIArgumentException(String message, String paramName) {
        super(message, paramName);
    }
    
    /**
     * Constructor of GnossAPIArgumentException
     * @param message Message of the error
     * @param originalException Original exception
     */
    public GnossAPIArgumentException(String message, Exception originalException) {
        super(message, originalException);
    }
    
    /**
     * Constructor of GnossAPIArgumentException
     * @param message Message of the error
     */
    public GnossAPIArgumentException(String message) {
        super(message);
    }
}