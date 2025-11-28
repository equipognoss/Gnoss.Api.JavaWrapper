package org.gnoss.apiWrapper.ApiModel;

/**
 * Mail configuration model for custom SMTP settings
 */
public class MailConfigurationModel {
    
    /**
     * Email address
     */
    private String email;
    
    /**
     * SMTP server address
     */
    private String smtp;
    
    /**
     * SMTP port
     */
    private short puerto;
    
    /**
     * Password/Key for authentication
     */
    private String clave;
    
    /**
     * Type of mail configuration
     */
    private String tipo;
    
    
    // Getters and Setters
    
    /**
     * Get email address
     * @return Email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Set email address
     * @param email Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Get SMTP server address
     * @return SMTP server address
     */
    public String getSmtp() {
        return smtp;
    }
    
    /**
     * Set SMTP server address
     * @param smtp SMTP server address
     */
    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }
    
    /**
     * Get SMTP port
     * @return SMTP port
     */
    public short getPuerto() {
        return puerto;
    }
    
    /**
     * Set SMTP port
     * @param puerto SMTP port
     */
    public void setPuerto(short puerto) {
        this.puerto = puerto;
    }
    
    /**
     * Get password/key for authentication
     * @return Password/key
     */
    public String getClave() {
        return clave;
    }
    
    /**
     * Set password/key for authentication
     * @param clave Password/key
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
    
    /**
     * Get type of mail configuration
     * @return Type of mail configuration
     */
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Set type of mail configuration
     * @param tipo Type of mail configuration
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}