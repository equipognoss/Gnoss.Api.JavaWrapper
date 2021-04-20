package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class CommunityInfoModel {

	private String name;
	private String short_name;
	private String description;
	private String tags;
	private String type;
	private int access_type;
	private List<UUID> categories;
	private List<UUID> users;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getAccess_type() {
		return access_type;
	}
	public void setAccess_type(int access_type) {
		this.access_type = access_type;
	}
	public List<UUID> getCategories() {
		return categories;
	}
	public void setCategories(List<UUID> categories) {
		this.categories = categories;
	}
	public List<UUID> getUsers() {
		return users;
	}
	public void setUsers(List<UUID> users) {
		this.users = users;
	}
	
}
