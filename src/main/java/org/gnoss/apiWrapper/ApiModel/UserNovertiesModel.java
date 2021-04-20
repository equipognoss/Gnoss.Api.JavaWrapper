package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Properties of an user 
 * @author Andrea
 *
 */
public class UserNovertiesModel {

	private UUID user_id;
	private CommunitySubscriptionModel community_subscriptions;
	private List<UserSubscriptionModel> user_subscription;
	private UserCommunityMembership user_community_membership;
	
	/**
	 * User identificator
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator
	 * @param user_id
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * User subscriptions to community categories
	 * @return subscription to community categories
	 */
	public CommunitySubscriptionModel getCommunity_subscriptions() {
		return community_subscriptions;
	}
	/**
	 * User subscription to community categories
	 * @param community_subscriptions
	 */
	public void setCommunity_subscriptions(CommunitySubscriptionModel community_subscriptions) {
		this.community_subscriptions = community_subscriptions;
	}
	/**
	 * User subscription to another user
	 * @return subscription to another user
	 */
	public List<UserSubscriptionModel> getUser_subscription() {
		return user_subscription;
	}
	/**
	 * User subscription to another user
	 * @param user_subscription
	 */
	public void setUser_subscription(List<UserSubscriptionModel> user_subscription) {
		this.user_subscription = user_subscription;
	}
	/**
	 * User info about community membership
	 * @return user info
	 */
	public UserCommunityMembership getUser_community_membership() {
		return user_community_membership;
	}
	/**
	 * User info about community membership
	 * @param user_community_membership
	 */
	public void setUser_community_membership(UserCommunityMembership user_community_membership) {
		this.user_community_membership = user_community_membership;
	}
	
}
