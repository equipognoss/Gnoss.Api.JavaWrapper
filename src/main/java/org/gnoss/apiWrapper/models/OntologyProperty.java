/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import java.util.regex.Pattern;

/**
 *
 * @author jruiz
 */
public class OntologyProperty {
    
    //Properties
    private String language;
    private String languagePattern = "^[a-z]{2,2}$";
    private Object value;
    private String name;    

    //Constructor
    public OntologyProperty(){
    }
 
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) throws GnossAPIArgumentException {
        if (this.language != null)
        {
            Pattern pattern = Pattern.compile(languagePattern);
            if (pattern.matcher(language).matches())
            {
                this.language = language;
            }
            else
            {
                throw new GnossAPIArgumentException("The language sets in the property {Name} is not valid. It must have only two letters (example: en, es...)");
            }
        }
        else
        {
            this.language = language;
        }
    }
    
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
            
    @Override
    public boolean equals(java.lang.Object obj){
        if (obj != null && obj.getClass() != this.getClass())
        {
            return false;
        }
        else if(obj == this){
            return true;
        }
        else
        {
            OntologyProperty property = (OntologyProperty) obj;
            if (property != null && property.getLanguage()!=null && !property.getLanguage().isEmpty())
            {
                if (this.name.equals(property.getName()) && this.value.equals(property.getValue()) && this.language.equals(property.getLanguage()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (this.value != null && property != null && this.name.equals(property.getName()) && this.value.equals(property.getValue()))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
    }
    
    @Override
    public int hashCode(){
        int hashOntoloyPropertyName = this.name == null ? 0 : this.name.hashCode();
        int hashOntologyPropertyValue = this.value == null ? 0 : this.value.hashCode();
        int hashOntologyPropertyLanguage = this.language == null ? 0 : this.language.hashCode();

        return hashOntoloyPropertyName ^ hashOntologyPropertyValue ^ hashOntologyPropertyLanguage;
    }
}
