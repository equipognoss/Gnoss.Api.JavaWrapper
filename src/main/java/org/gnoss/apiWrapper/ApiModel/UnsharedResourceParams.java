package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class UnsharedResourceParams {
	
	private String community_short_name;
	private UUID resource_id;
	
	/**
	 * Community short name
	 * @return Community short name
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
	 * Resource identifier
	 * @return Resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	
	

}
