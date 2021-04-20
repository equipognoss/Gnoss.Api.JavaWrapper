/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author salopez
 */
public class ModifyResourceTripleListParams {
    private String community_short_name;
    private UUID resource_id;
    private ArrayList<ModifyResourceTriple> resource_triples;
    private ArrayList<SemanticAttachedResource> resource_attached_files;
    private boolean publish_home;
    private String charge_id;
    private String main_image;
    private boolean end_of_load;

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

    public ArrayList<ModifyResourceTriple> getResource_triples() {
        return resource_triples;
    }

    public void setResource_triples(ArrayList<ModifyResourceTriple> resource_triples) {
        this.resource_triples = resource_triples;
    }

    public ArrayList<SemanticAttachedResource> getResource_attached_files() {
        return resource_attached_files;
    }

    public void setResource_attached_files(ArrayList<SemanticAttachedResource> resource_attached_files) {
        this.resource_attached_files = resource_attached_files;
    }

    public boolean isPublish_home() {
        return publish_home;
    }

    public void setPublish_home(boolean publish_home) {
        this.publish_home = publish_home;
    }

    public String getCharge_id() {
        return charge_id;
    }

    public void setCharge_id(String charge_id) {
        this.charge_id = charge_id;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public boolean isEnd_of_load() {
        return end_of_load;
    }

    public void setEnd_of_load(boolean end_of_load) {
        this.end_of_load = end_of_load;
    }
    
}
