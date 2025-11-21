package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Parameters for loading a resource
 * @author salopez
 */
public class LoadResourceParams {
    
    /**
     * Community short name
     */
    public String community_short_name;
    
    /**
     * Resource identifier
     */
    public UUID resource_id;
    
    /**
     * Resource title
     */
    public String title;
    
    /**
     * Resource description
     */
    public String description;
    
    /**
     * Resource tags
     */
    public List<String> tags;
    
    /**
     * Resource categories
     */
    public List<UUID> categories;
    
    /**
     * Resource type
     */
    public short resource_type;
    
    /**
     * Resource url
     */
    public String resource_url;
    
    /**
     * Resource attached file
     */
    public byte[] resource_file;
    
    /**
     * Resource attached files
     */
    public List<SemanticAttachedResource> resource_attached_files;
    
    /**
     * True if the resource creator is the author
     */
    public boolean creator_is_author;
    
    /**
     * Resource authors (comma separated)
     */
    public String authors;
    
    /**
     * Tags auto extracted of title
     */
    public String auto_tags_title_text;
    
    /**
     * Tags auto extracted of description
     */
    public String auto_tags_description_text;
    
    /**
     * True if a screenshot of the resource must be generated
     */
    public boolean create_screenshot;
    
    /**
     * Url to make a screenshot
     */
    public String url_screenshot;
    
    /**
     * Screenshot predicate
     */
    public String predicate_screenshot;
    
    /**
     * Screenshot possible sizes
     */
    public List<Integer> screenshot_sizes;
    
    /**
     * Priority of the upload
     */
    public int priority;
    
    /**
     * Resource visibility
     */
    public short visibility;
    
    /**
     * Resource readers list
     */
    public List<ReaderEditor> readers_list;
    
    /**
     * Resource editors list
     */
    public List<ReaderEditor> editors_list;
    
    /**
     * Resource creation date
     */
    public Date creation_date;
    
    /**
     * Resource publisher email
     */
    public String publisher_email;
    
    /**
     * True if the resource must be published in the home of the community
     */
    public boolean publish_home;
    
    /**
     * Path of the resource main image
     */
    public String main_image;
    
    /**
     * True if it's the end of the load
     */
    public boolean end_of_load;
    
    /**
     * True if the resource must be versioned
     */
    public boolean create_version;
    
    /**
     * The canonical url for the resource
     */
    public String canonical_url;
    
    /**
     * Augmented reading
     */
    public AumentedReading aumented_reading;
    
    
    // Getters and Setters
    
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<UUID> getCategories() {
        return categories;
    }

    public void setCategories(List<UUID> categories) {
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

    public byte[] getResource_file() {
        return resource_file;
    }

    public void setResource_file(byte[] resource_file) {
        this.resource_file = resource_file;
    }

    public List<SemanticAttachedResource> getResource_attached_files() {
        return resource_attached_files;
    }

    public void setResource_attached_files(List<SemanticAttachedResource> resource_attached_files) {
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

    public List<Integer> getScreenshot_sizes() {
        return screenshot_sizes;
    }

    public void setScreenshot_sizes(List<Integer> screenshot_sizes) {
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

    public List<ReaderEditor> getReaders_list() {
        return readers_list;
    }

    public void setReaders_list(List<ReaderEditor> readers_list) {
        this.readers_list = readers_list;
    }

    public List<ReaderEditor> getEditors_list() {
        return editors_list;
    }

    public void setEditors_list(List<ReaderEditor> editors_list) {
        this.editors_list = editors_list;
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

    public AumentedReading getAumented_reading() {
        return aumented_reading;
    }

    public void setAumented_reading(AumentedReading aumented_reading) {
        this.aumented_reading = aumented_reading;
    }
}