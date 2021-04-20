package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class CreateGroupCommunityModel {
	private String community_short_name;
	private String group_short_name;
	private String group_name;
	private String description;
	private List<String> tagas;
	private List<UUID> members;
	private boolean send_notification;
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getGroup_short_name() {
		return group_short_name;
	}
	public void setGroup_short_name(String group_short_name) {
		this.group_short_name = group_short_name;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getTagas() {
		return tagas;
	}
	public void setTagas(List<String> tagas) {
		this.tagas = tagas;
	}
	public List<UUID> getMembers() {
		return members;
	}
	public void setMembers(List<UUID> members) {
		this.members = members;
	}
	public boolean isSend_notification() {
		return send_notification;
	}
	public void setSend_notification(boolean send_notification) {
		this.send_notification = send_notification;
	}

}
