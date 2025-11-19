package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters for a community user
 * @author GNOSS
 */
public class ParamsUserCommunity {
	private String user_short_name;
	private String community_short_name;
	private UUID user_id;
	
	/**
	 * User short name
	 * @return user short name 
	 */
	public String getUser_short_name() {
		return user_short_name;
	}
	
	/**
	 * User short name
	 * @param user_short_name User short name 
	 */
	public void setUser_short_name(String user_short_name) {
		this.user_short_name = user_short_name;
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
	 * @param community_short_name Community short name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	
	/**
	 * User identifier
	 * @return user identifier
	 */
	public UUID getUser_id() {
		return user_id;
	}
	
	/**
	 * User identifier
	 * @param user_id User identifier
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
}