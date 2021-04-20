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
public class LoadResourceParams {
    public String community_short_name;
    public UUID resource_id;
    public String title;
    public String description;
    public ArrayList<String> tags;
    public ArrayList<UUID> categories;
    public short resource_type;
    public String resource_url;
    public String resource_file;
    public ArrayList<SemanticAttachedResource> resource_attached_files;
    public boolean creator_is_author;
    public String authors;
    public String auto_tags_title_text;
    public String auto_tags_description_text;
    public boolean create_screenshot;
    public String url_screenshot;
    public String predicate_screenshot;
    public ArrayList<Integer> screenshot_sizes;
    public int priority;
    public short visibility;
    public ArrayList<ReaderEditor> reader_list;
    public ArrayList<ReaderEditor> editor_list;
    public Date creation_date;
    public String publisher_email;
    public boolean publish_home;
    public String load_id;
    public String main_image;
    public boolean end_of_load;
    public boolean create_version;
    public String canonical_url;

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

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public String getResource_file() {
        return resource_file;
    }

    public void setResource_file(String resource_file) {
        this.resource_file = resource_file;
    }

    public ArrayList<SemanticAttachedResource> getResource_attached_files() {
        return resource_attached_files;
    }

    public void setResource_attached_files(ArrayList<SemanticAttachedResource> resource_attached_files) {
        this.resource_attached_files = resource_attached_files;
    }

    public boolean isCreator_is_author() {
        return creator_is_author;
    }

    public void setCreator_is_author(boolean creator_is_author) {
        this.creator_is_author = creator_is_author;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAuto_tags_title_text() {
        return auto_tags_title_text;
    }

    public void setAuto_tags_title_text(String auto_tags_title_text) {
        this.auto_tags_title_text = auto_tags_title_text;
    }

    public String getAuto_tags_description_text() {
        return auto_tags_description_text;
    }

    public void setAuto_tags_description_text(String auto_tags_description_text) {
        this.auto_tags_description_text = auto_tags_description_text;
    }

    public boolean isCreate_screenshot() {
        return create_screenshot;
    }

    public void setCreate_screenshot(boolean create_screenshot) {
        this.create_screenshot = create_screenshot;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public short getVisibility() {
        return visibility;
    }

    public void setVisibility(short visibility) {
        this.visibility = visibility;
    }

    public ArrayList<ReaderEditor> getReader_list() {
        return reader_list;
    }

    public void setReader_list(ArrayList<ReaderEditor> reader_list) {
        this.reader_list = reader_list;
    }

    public ArrayList<ReaderEditor> getEditor_list() {
        return editor_list;
    }

    public void setEditor_list(ArrayList<ReaderEditor> editor_list) {
        this.editor_list = editor_list;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getPublisher_email() {
        return publisher_email;
    }

    public void setPublisher_email(String publisher_email) {
        this.publisher_email = publisher_email;
    }

    public boolean isPublish_home() {
        return publish_home;
    }

    public void setPublish_home(boolean publish_home) {
        this.publish_home = publish_home;
    }

    public String getLoad_id() {
        return load_id;
    }

    public void setLoad_id(String load_id) {
        this.load_id = load_id;
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

    public boolean isCreate_version() {
        return create_version;
    }

    public void setCreate_version(boolean create_version) {
        this.create_version = create_version;
    }

    public String getCanonical_url() {
        return canonical_url;
    }

    public void setCanonical_url(String canonical_url) {
        this.canonical_url = canonical_url;
    }
    
    
}
