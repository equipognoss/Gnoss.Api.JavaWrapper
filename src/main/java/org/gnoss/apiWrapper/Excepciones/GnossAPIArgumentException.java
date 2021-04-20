/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.Excepciones;

/**
 *
 * @author salopez
 */
public class GnossAPIArgumentException extends GnossAPIException {   
    /**
     * Constructor of GnossAPIArgumentException
     * @param message Message of the error
     */
    public GnossAPIArgumentException(String message){
        super(message);
    }    
}
