package org.gnoss.apiWrapper.ApiModel;

/**
 * Parameters to obtain a text in other language
 */
public class GetTextByLanguageModel {
    
    /**
     * Community short name
     */
    private String community_short_name;
    
    /**
     * Language of the text
     */
    private String language;
    
    /**
     * ID of the text
     */
    private String texto_id;
    
    // Getters and Setters
    
    public String getCommunity_short_name() {
        return community_short_name;
    }
    
    public void setCommunity_short_name(String community_short_name) {
        this.community_short_name = community_short_name;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getTexto_id() {
        return texto_id;
    }
    
    public void setTexto_id(String texto_id) {
        this.texto_id = texto_id;
    }
}