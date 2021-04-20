package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class LinkParentCommunityModel {

	private String short_name;
	private String parent_community_short_name;
	private UUID admin_id;
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String getParent_community_short_name() {
		return parent_community_short_name;
	}
	public void setParent_community_short_name(String parent_community_short_name) {
		this.parent_community_short_name = parent_community_short_name;
	}
	public UUID getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(UUID admin_id) {
		this.admin_id = admin_id;
	}
	
}
