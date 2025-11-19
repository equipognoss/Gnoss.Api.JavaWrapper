package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Model for community category
 */
public class CommunityCategoryModel {

	/**
	 * Short name of the community
	 */
	private String community_short_name;
	
	/**
	 * Name of the category
	 */
	private String category_name;
	
	/**
	 * Identifier of the parent category
	 */
	private UUID parent_category_id;
	
	/**
	 * Image of the category
	 */
	private byte[] category_image;
	
	
	// Getters and Setters
	
	public String getCommunity_short_name() {
		return community_short_name;
	}
	
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	
	public String getCategory_name() {
		return category_name;
	}
	
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
	public UUID getParent_category_id() {
		return parent_category_id;
	}
	
	public void setParent_category_id(UUID parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
	
	public byte[] getCategory_image() {
		return category_image;
	}
	
	public void setCategory_image(byte[] category_image) {
		this.category_image = category_image;
	}
}