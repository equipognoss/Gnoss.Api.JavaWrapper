package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters to set the resource editors
 * @author Andrea
 *
 */

public class SetReadersEditorsParams {
	
	private UUID resource_id;
	private String community_short_name;
	private int visibility;
	private List<ReaderEditor> readers_list;
	private boolean publish_home;
	
	/**
	 * Resource identifier
	 * @return resource identifiers
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
	 * Visibility of the resource
	 * @return visibility of the resource
	 */
	public int getVisibility() {
		return visibility;
	}
	/**
	 * Visibility of the resource
	 * @param visibility visibility 
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	/**
	 * Users short names of the resource editors or readers
	 * @return List of users short names
	 */
	public List<ReaderEditor> getReaders_list() {
		return readers_list;
	}
	/**
	 * Users short names of the resource editors or readers
	 * @param readers_list readers list
	 */
	public void setReaders_list(List<ReaderEditor> readers_list) {
		this.readers_list = readers_list;
	}
	/**
	 * True if the resource must be published in the home of the community (by default false)
	 * @return True or false
	 */
	public boolean isPublish_home() {
		return publish_home;
	}
	/**
	 * True if the resource must be published in the home of the community (by default false)
	 * @param publish_home publish home 
	 */
	public void setPublish_home(boolean publish_home) {
		this.publish_home = publish_home;
	}
	
}
