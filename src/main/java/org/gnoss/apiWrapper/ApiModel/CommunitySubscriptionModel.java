package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Prepresents the subscription to a community
 * @author Andrea
 *
 */
public class CommunitySubscriptionModel {
	
	private UUID user_id;
	private String community_short_name;
	private List<ThesaurusCategory> category_list;
	
	/**
	 * Subscriptor user identificator
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * Subscription user identificator
	 * @param user_id
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
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
	 * Categories which user is subscribed 
	 * @return List of categories which user is subscribed 
	 */
	public List<ThesaurusCategory> getCategory_list() {
		return category_list;
	}
	/**
	 * Categories which user is subscribed 
	 * @param category_list
	 */
	public void setCategory_list(List<ThesaurusCategory> category_list) {
		this.category_list = category_list;
	}
}
