/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.Helpers;

import java.util.ArrayList;

import org.gnoss.apiWrapper.models.BoolOntologyProperty;
import org.gnoss.apiWrapper.models.DateOntologyProperty;
import org.gnoss.apiWrapper.models.ImageOntologyProperty;
import org.gnoss.apiWrapper.models.ListStringOntologyProperty;
import org.gnoss.apiWrapper.models.StringOntologyProperty;

/**
 *
 * @author salopez
 */
public enum DataTypes {
    ListString(new ArrayList<String>().getClass()), 
    String("".getClass()), 
    Booleano(boolean.class), 
    OntologyPropertyImage(ImageOntologyProperty.class), 
    OntologyPropertyListString(ListStringOntologyProperty.class), 
    OntologyPropertyString(StringOntologyProperty.class), 
    OntologyPropertyDate(DateOntologyProperty.class), 
    OntologyPropertyBoolean(BoolOntologyProperty.class);
    
    private Class dataType;

    DataTypes(Class dataType) {
    	this.dataType = dataType;
    }
    
    public Class getID(){
    	return dataType;
    }
}
