package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters for a community user
 * @author Andrea
 *
 */
public class ParamsUserCommunity {
	private String user_short_name;
	private String community_short_name;
	
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
	

}
