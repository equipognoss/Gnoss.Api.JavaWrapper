package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Represents a community
 */
public class CommunityModel {
	
	/**
	 * Name
	 */
	private String community_name;
	
	/**
	 * Short name
	 */
	private String community_short_name;
	
	/**
	 * Default language
	 */
	private String community_default_language = "es";
	
	/**
	 * Brief Description
	 */
	private String description;
	
	/**
	 * Tags (comma separated)
	 */
	private String tags;
	
	/**
	 * Type: 0 for standard communities, 5 for static catalogs, 8 for static catalogs without members
	 */
	private int type;
	
	/**
	 * Access type: 0: public (Any user can be member), 1: private (Only users with invitation can be members), 
	 * 2: restricted (users can request access, but admin must accept the requests), 
	 * 3: reserved (private community children of other private community)
	 */
	private int access_type;
	
	/**
	 * Parent community short name
	 */
	private String parent_community_short_name;
	
	/**
	 * User identifier of the administrator of the community
	 */
	private UUID admin_id;
	
	/**
	 * Organization short name of the user
	 */
	private String organization_short_name;
	
	/**
	 * Logo for the community
	 */
	private byte[] logo;
	
	/**
	 * Community domain
	 */
	private String domain;
	
	
	// Getters and Setters
	
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
	
	public String getCommunity_default_language() {
		return community_default_language;
	}
	
	public void setCommunity_default_language(String community_default_language) {
		this.community_default_language = community_default_language;
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
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
}