package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class CommunityCategoryModel {

	private String community_short_name;
	private String category_name;
	private UUID parent_category_id;
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public UUID getParent_category_id() {
		return parent_category_id;
	}
	public void setParent_category_id(UUID parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
	
}
