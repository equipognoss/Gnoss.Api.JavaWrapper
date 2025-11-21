package org.gnoss.apiWrapper.Excepciones;

/**
 * Exceptions produced during the execution of the api
 * @author salopez
 */
public class GnossAPIException extends Exception {
    
    private String paramName;
    
    /**
     * Constructor of GnossAPIException
     * @param message Message of the error
     * @param paramName Parameter with some error
     * @param originalException Original exception
     */
    public GnossAPIException(String message, String paramName, Exception originalException) {
        super(message, originalException);
        this.paramName = paramName;
    }
    
    /**
     * Constructor of GnossAPIException
     * @param message Message of the error
     * @param paramName Parameter with some error
     */
    public GnossAPIException(String message, String paramName) {
        super(message);
        this.paramName = paramName;
    }
    
    /**
     * Constructor of GnossAPIException
     * @param message Message of the error
     * @param originalException Original exception
     */
    public GnossAPIException(String message, Exception originalException) {
        super(message, originalException);
    }
    
    /**
     * Constructor of GnossAPIException
     * @param message Message of the error
     */
    public GnossAPIException(String message) {
        super(message);
    }
    
    /**
     * Get the parameter name that caused the exception
     * @return String paramName
     */
    public String getParamName() {
        return paramName;
    }
    
    /**
     * Get the full error message including parameter name if available
     * @return String full message
     */
    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (paramName != null && !paramName.isEmpty()) {
            return message + " (Parameter: " + paramName + ")";
        }
        return message;
    }
}