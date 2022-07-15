/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.Helpers;

import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author salopez
 */
public class GnossHelper {
 
    public static String GetImagePath(UUID resourceID, String imageName){
        return Constants.IMAGES_PATH_ROOT + resourceID.toString().substring(0, 2) + "/" + resourceID.toString().substring(0, 4) + "/" + resourceID + "/" + imageName;
    }
    
    public static UUID GetResourceID(String largeID){
        String[] splittedID = largeID.split(Arrays.toString(CharArrayDelimiters.Underscore));
        try{
            return UUID.fromString(splittedID[splittedID.length - 2]);
        }
        catch(Exception ex){
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
    }
}

