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
public enum GnossResourceProperty {

    none(0),
    title(1),
    description(2);

    private int gnossResourcePropertyType;

    GnossResourceProperty(int gnossResourcePropertyType) {
        this.gnossResourcePropertyType = gnossResourcePropertyType;
    }
}
