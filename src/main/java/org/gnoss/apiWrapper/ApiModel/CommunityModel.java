package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class CommunityModel {
	
	private String community_name;
	private String community_short_name;
	private String description;
	private String tags;
	private int type;
	private int access_type;
	private String parent_community_short_name;
	private UUID admin_id;
	private String organization_short_name;
	private byte[] logo;
	
	
	public String getCommunity_name() {
		return community_name;
	}
	public void setCommunity_name(String community_name) {
		this.community_name = community_name;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getAccess_type() {
		return access_type;
	}
	public void setAccess_type(int access_type) {
		this.access_type = access_type;
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
	public String getOrganization_short_name() {
		return organization_short_name;
	}
	public void setOrganization_short_name(String organization_short_name) {
		this.organization_short_name = organization_short_name;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	

}
