package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class ModifyResourceCategories {
	public String community_short_name;
	public UUID resource_id;
	public List<UUID> categories;
	
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public UUID getResource_id() {
		return resource_id;
	}
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	public List<UUID> getCategories() {
		return categories;
	}
	public void setCategories(List<UUID> categories) {
		this.categories = categories;
	}
	
}
