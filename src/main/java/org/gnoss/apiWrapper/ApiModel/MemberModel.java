package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class MemberModel {

	private UUID user_id;
	private String community_short_name;
	private String organization_short_name;
	private int identity_type;
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
	public String getOrganization_short_name() {
		return organization_short_name;
	}
	public void setOrganization_short_name(String organization_short_name) {
		this.organization_short_name = organization_short_name;
	}
	public int getIdentity_type() {
		return identity_type;
	}
	public void setIdentity_type(int identity_type) {
		this.identity_type = identity_type;
	}
	
	
}
