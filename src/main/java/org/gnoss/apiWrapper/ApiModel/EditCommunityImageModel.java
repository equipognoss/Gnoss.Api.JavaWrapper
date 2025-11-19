package org.gnoss.apiWrapper.ApiModel;

/**
 * Model to edit community image
 */
public class EditCommunityImageModel {
    
    /**
     * Logo of the community
     */
    private byte[] logo;
    
    /**
     * Community name
     */
    private String communityName;
    
    // Getters and Setters
    
    public byte[] getLogo() {
        return logo;
    }
    
    public void setLogo(byte[] logo) {
        this.logo = logo;
    }
    
    public String getCommunityName() {
        return communityName;
    }
    
    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}