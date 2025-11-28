package org.gnoss.apiWrapper.ApiModel;

import java.util.List;

/**
 * Notification model for sending emails
 */
public class NotificationModel {

    /**
     * Subject of the email (Required)
     */
    private String subject;
    
    /**
     * Message of the email (Required)
     */
    private String message;
    
    /**
     * True if the message contains HTML
     */
    private boolean is_html;
    
    /**
     * List of email receivers (Required)
     */
    private List<String> receivers;
    
    /**
     * Sender mask
     */
    private String sender_mask;
    
    /**
     * Transmitter SMTP config defined
     */
    private MailConfigurationModel transmitter_mail_configuration;
    
    /**
     * Community short name
     */
    private String community_short_name;
    
    
    // Getters and Setters
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isIs_html() {
        return is_html;
    }
    
    public void setIs_html(boolean is_html) {
        this.is_html = is_html;
    }
    
    public List<String> getReceivers() {
        return receivers;
    }
    
    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }
    
    public String getSender_mask() {
        return sender_mask;
    }
    
    public void setSender_mask(String sender_mask) {
        this.sender_mask = sender_mask;
    }
    
    public MailConfigurationModel getTransmitter_mail_configuration() {
        return transmitter_mail_configuration;
    }
    
    public void setTransmitter_mail_configuration(MailConfigurationModel transmitter_mail_configuration) {
        this.transmitter_mail_configuration = transmitter_mail_configuration;
    }
    
    public String getCommunity_short_name() {
        return community_short_name;
    }
    
    public void setCommunity_short_name(String community_short_name) {
        this.community_short_name = community_short_name;
    }
}