package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class MembersGroupCommunityModel {

	private String community_short_name;
	private String group_short_name;
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
