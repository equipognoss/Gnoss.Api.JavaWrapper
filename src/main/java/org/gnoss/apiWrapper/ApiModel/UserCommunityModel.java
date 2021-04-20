package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class UserCommunityModel {

	private UUID user_id;
	private String community_short_name;
	public UUID getUser_id() {
		return user_id;
	}
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	
}
