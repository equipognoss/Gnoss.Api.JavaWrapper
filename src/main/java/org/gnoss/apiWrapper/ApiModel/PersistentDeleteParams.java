package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class PersistentDeleteParams {
	private UUID resource_id;
	private String community_short_name;
	private boolean end_of_load;
	private boolean delete_attached;
	
	public UUID getResource_id() {
		return resource_id;
	}
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public boolean isEnd_of_load() {
		return end_of_load;
	}
	public void setEnd_of_load(boolean end_of_load) {
		this.end_of_load = end_of_load;
	}
	public boolean isDelete_attached() {
		return delete_attached;
	}
	public void setDelete_attached(boolean delete_attached) {
		this.delete_attached = delete_attached;
	}
}
