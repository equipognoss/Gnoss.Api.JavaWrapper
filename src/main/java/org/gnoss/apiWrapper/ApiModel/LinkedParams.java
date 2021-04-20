package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters to link a resource
 * @author Andrea
 *
 */
public class LinkedParams {
	
	private UUID resource_id;
	private String community_short_name;
	private List<UUID> resource_list_to_link;
	
	/**
	 * Resource to be linked by the resource list
	 * @return resource to be linked by the resource list
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource to be linked by the resource list
	 * @param resource_id
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
	 * @param community_short_name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * List of resources to link 
	 * @return list of resources to link 
	 */
	public List<UUID> getResource_list_to_link() {
		return resource_list_to_link;
	}
	/**
	 * List of resources to link
	 * @param resource_list_to_link
	 */
	public void setResource_list_to_link(List<UUID> resource_list_to_link) {
		this.resource_list_to_link = resource_list_to_link;
	}
	

}
