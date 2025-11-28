package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for checking mail state
 */
public class MailStateModel {
    
    /**
     * List of emails pending to be sent
     */
    private List<String> pending_mails;
    
    /**
     * List of emails that failed to be sent
     */
    private List<String> error_mails;
    
    
    /**
     * Constructor - initializes empty lists
     */
    public MailStateModel() {
        this.pending_mails = new ArrayList<>();
        this.error_mails = new ArrayList<>();
    }
    
    
    // Getters and Setters
    
    /**
     * Get list of pending emails
     * @return List of pending emails
     */
    public List<String> getPending_mails() {
        return pending_mails;
    }
    
    /**
     * Set list of pending emails
     * @param pending_mails List of pending emails
     */
    public void setPending_mails(List<String> pending_mails) {
        this.pending_mails = pending_mails;
    }
    
    /**
     * Get list of error emails
     * @return List of error emails
     */
    public List<String> getError_mails() {
        return error_mails;
    }
    
    /**
     * Set list of error emails
     * @param error_mails List of error emails
     */
    public void setError_mails(List<String> error_mails) {
        this.error_mails = error_mails;
    }
}