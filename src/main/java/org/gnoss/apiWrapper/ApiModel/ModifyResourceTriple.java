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
public class ModifyResourceTriple {
    private String predicate;
    private String old_object;
    private String new_object;
    private GnossResourceProperty gnoss_property;

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getOld_object() {
        return old_object;
    }

    public void setOld_object(String old_object) {
        this.old_object = old_object;
    }

    public String getNew_object() {
        return new_object;
    }

    public void setNew_object(String new_object) {
        this.new_object = new_object;
    }

    public GnossResourceProperty getGnoss_property() {
        return gnoss_property;
    }

    public void setGnoss_property(GnossResourceProperty gnoss_property) {
        this.gnoss_property = gnoss_property;
    }
    
    
}
