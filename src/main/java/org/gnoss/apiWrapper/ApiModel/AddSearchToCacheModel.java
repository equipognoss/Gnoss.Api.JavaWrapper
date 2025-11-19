package org.gnoss.apiWrapper.ApiModel;

/**
 * Model to Add results of the query to cache
 */
public class AddSearchToCacheModel {
    
    /**
     * Key to add to cache
     */
    private String key;
    
    /**
     * Value to add to cache
     */
    private ConsultaCacheModel value;
    
    /**
     * Short name of the community
     * Example: ferdev
     */
    private String community_short_name;
    
    /**
     * Duration of cache expiration in seconds
     */
    private double duration;
    
    // Getters and Setters
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public ConsultaCacheModel getValue() {
        return value;
    }
    
    public void setValue(ConsultaCacheModel value) {
        this.value = value;
    }
    
    public String getCommunity_short_name() {
        return community_short_name;
    }
    
    public void setCommunity_short_name(String community_short_name) {
        this.community_short_name = community_short_name;
    }
    
    public double getDuration() {
        return duration;
    }
    
    public void setDuration(double duration) {
        this.duration = duration;
    }
}