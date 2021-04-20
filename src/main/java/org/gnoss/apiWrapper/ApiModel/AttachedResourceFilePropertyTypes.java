/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.ApiModel;

/**
 *
 * @author salopez
 */
public enum AttachedResourceFilePropertyTypes {
    
    //Indicates the attached resource is a file
    file(0), 
    //Indicates the attached resource is an image
    image(1),       
    //Indicates the attached resource is a link file
    downloadableFile(2);
    
    private int tipoFichero;
    
    AttachedResourceFilePropertyTypes(int tipoFichero){
        this.tipoFichero = tipoFichero;
    }
    
    public int getID(){
    	return tipoFichero;
    }
}
