package org.gnoss.apiWrapper.ApiModel;

public class RegisterLoadModel {

	private String load_id;
	private String community_short_name;
	private String email_responsible;
	
	
	public String getLoad_id() {
		return load_id;
	}
	public void setLoad_id(String load_id) {
		this.load_id = load_id;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getEmail_responsible() {
		return email_responsible;
	}
	public void setEmail_responsible(String email_responsible) {
		this.email_responsible = email_responsible;
	}
}
