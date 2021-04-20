/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author salopez
 */
public class Resource {
   public String community_short_name;
   public UUID resource_id;
   public String title;
   public String description;
   public ArrayList<String> tags;
   public ArrayList<UUID> categories;
   public short resource_type;
   public ArrayList<SemanticAttachedResource> resource_attached_files;
   public String authors;
   public String url_screenshot;
   public String predicate_screenshot;
   public ArrayList<Integer> screenshot_sizes;
   public short visibility;
   public String ontology;
   public ArrayList<ReaderEditor> readers_list;
   public ArrayList<ReaderEditor> editors_list;
   public Date creation_date;
   public String main_image;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<UUID> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<UUID> categories) {
        this.categories = categories;
    }

    public short getResource_type() {
        return resource_type;
    }

    public void setResource_type(short resource_type) {
        this.resource_type = resource_type;
    }

    public ArrayList<SemanticAttachedResource> getResource_attached_files() {
        return resource_attached_files;
    }

    public void setResource_attached_files(ArrayList<SemanticAttachedResource> resource_attached_files) {
        this.resource_attached_files = resource_attached_files;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getUrl_screenshot() {
        return url_screenshot;
    }

    public void setUrl_screenshot(String url_screenshot) {
        this.url_screenshot = url_screenshot;
    }

    public String getPredicate_screenshot() {
        return predicate_screenshot;
    }

    public void setPredicate_screenshot(String predicate_screenshot) {
        this.predicate_screenshot = predicate_screenshot;
    }

    public ArrayList<Integer> getScreenshot_sizes() {
        return screenshot_sizes;
    }

    public void setScreenshot_sizes(ArrayList<Integer> screenshot_sizes) {
        this.screenshot_sizes = screenshot_sizes;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public ArrayList<ReaderEditor> getReaders_list() {
        return readers_list;
    }

    public void setReaders_list(ArrayList<ReaderEditor> readers_list) {
        this.readers_list = readers_list;
    }

    public ArrayList<ReaderEditor> getEditors_list() {
        return editors_list;
    }

    public void setEditors_list(ArrayList<ReaderEditor> editors_list) {
        this.editors_list = editors_list;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }
}



