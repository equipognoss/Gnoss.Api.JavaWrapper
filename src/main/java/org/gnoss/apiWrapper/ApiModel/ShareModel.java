package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;
/**
 * Properties of sharing 
 * @author Andrea
 *
 */
public class ShareModel {
	private UUID resource_id;
	private String origin_community_short_name;
	private String destiny_community_short_name;
	private UUID user_id;
	
	/**
	 * Resource identificator 
	 * @return identificator
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identificator 
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Origin community short name 
	 * @return community short name 
	 */
	public String getOrigin_community_short_name() {
		return origin_community_short_name;
	}
	/**
	 * Origin community short name 
	 * @param origin_community_short_name
	 */
	public void setOrigin_community_short_name(String origin_community_short_name) {
		this.origin_community_short_name = origin_community_short_name;
	}
	/**
	 * Destiny community short name 
	 * @return destiny community short name 
	 */
	public String getDestiny_community_short_name() {
		return destiny_community_short_name;
	}
	/**
	 * Destiny community short name 
	 * @param destiny_community_short_name
	 */
	public void setDestiny_community_short_name(String destiny_community_short_name) {
		this.destiny_community_short_name = destiny_community_short_name;
	}
	/**
	 * User identificator who has shared the resource
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * user identificator who has shared the resource
	 * @param user_id
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	
	
	
	

}
