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
public class ReaderEditor {
    private String user_short_name;
    private String group_short_name;
    private String organization_short_name;

    public String getUser_short_name() {
        return user_short_name;
    }

    public void setUser_short_name(String user_short_name) {
        this.user_short_name = user_short_name;
    }

    public String getGroup_short_name() {
        return group_short_name;
    }

    public void setGroup_short_name(String group_short_name) {
        this.group_short_name = group_short_name;
    }

    public String getOrganization_short_name() {
        return organization_short_name;
    }

    public void setOrganization_short_name(String organization_short_name) {
        this.organization_short_name = organization_short_name;
    }
    
}
