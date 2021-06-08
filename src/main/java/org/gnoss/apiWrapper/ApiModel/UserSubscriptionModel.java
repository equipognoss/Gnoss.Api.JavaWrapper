package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Represents the susbcription to a community
 * @author Andrea
 *
 */
public class UserSubscriptionModel {
	private UUID user_id;
	private UUID user_followed_id;
	private String community_short_name;
	private Date subscription_date;
	
	/**
	 * Susbcriptor user identificator
	 * @return user identificator 
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * Subscriptor user identificator
	 * @param user_id user id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Community short name. Null if user is a follower
	 * @return community short name 
	 */
	public String getCommunity_short_name() {
		return community_short_name;
	}
	/**
	 * Community short name. Null if user is a follower
	 * @param community_short_name community short name 
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * User identificator who user is subscribed 
	 * @return user identificator
	 */
	public UUID getUser_followed_id() {
		return user_followed_id;
	}
	/**
	 * User identificator who user is subscribe
	 * @param user_followed_id user followed id 
	 */
	public void setUser_followed_id(UUID user_followed_id) {
		this.user_followed_id = user_followed_id;
	}
	/**
	 * Categories which user is subscribed
	 * @return Date of subscribed categories 
	 */
	public Date getSubscription_date() {
		return subscription_date;
	}
	/**
	 * Categories which user is subscribed 
	 * @param subscription_date subscription date 
	 */
	public void setSubscription_date(Date subscription_date) {
		this.subscription_date = subscription_date;
	}
}
