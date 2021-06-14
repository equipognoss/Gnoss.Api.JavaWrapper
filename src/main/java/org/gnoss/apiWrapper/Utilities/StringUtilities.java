/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.Utilities;

/**
 *
 * @author salopez
 */
public class StringUtilities {
    public StringUtilities(){
    }
    
    /**
     * Quita todas las repeticiones finales de un carácter de la cadena actual.
     * @param cadena Cadena a modificar
     * @param caracterEliminar Caracter a eliminar del final de la cadena
     * @return String cadena
     */
    public String trimEnd(String cadena, char caracterEliminar){        
        int indice = cadena.length() - 1;
        
        while(cadena.charAt(indice) == caracterEliminar){
            indice--;
        }
        
        return cadena.substring(0, indice + 1);
    }
}
