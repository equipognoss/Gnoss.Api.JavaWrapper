/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 *
 * @author salopez
 */
public class ModifyResourceProperty {
    private String community_short_name;
    private UUID resource_id;
    private String property;
    private String new_object;

    public String getCommunity_short_name() {
        return community_short_name;
    }

    public void setCommunity_short_name(String community_short_name) {
        this.community_short_name = community_short_name;
    }

    public UUID getResource_id() {
        return resource_id;
    }

    public void setResource_id(UUID resource_id) {
        this.resource_id = resource_id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getNew_object() {
        return new_object;
    }

    public void setNew_object(String new_object) {
        this.new_object = new_object;
    }
    
    
}
