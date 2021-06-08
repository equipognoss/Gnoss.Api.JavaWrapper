package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters to set the main images
 * @author Andrea
 *
 */
public class SetMainImageParams {
	private String community_short_name;
	private UUID resource_id;
	private String path;
	
	/**
	 * Community short name
	 * @return community short name 
	 */
	public String getCommunity_short_name() {
		return community_short_name;
	}
	/**
	 * Community short name
	 * @param community_short_name community short name 
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * Resource identifier
	 * @return resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id resource id 
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Path of the image
	 * @return path of the image
	 */
	public String getPath() {
		return path;
	}
	/**
	 * Path of the image
	 * @param path path
	 */
	public void setPath(String path) {
		this.path = path;
	} 
	
	

}
