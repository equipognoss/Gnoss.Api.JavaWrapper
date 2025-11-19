package org.gnoss.apiWrapper.ApiModel;

/**
 * Model for translated texts in different languages
 */
public class TextosTraducidosIdiomas {
    
    /**
     * Text identifier
     */
    private String TextoID;
    
    /**
     * Text content
     */
    private String Texto;
    
    // Getters and Setters
    
    public String getTextoID() {
        return TextoID;
    }
    
    public void setTextoID(String textoID) {
        this.TextoID = textoID;
    }
    
    public String getTexto() {
        return Texto;
    }
    
    public void setTexto(String texto) {
        this.Texto = texto;
    }
}