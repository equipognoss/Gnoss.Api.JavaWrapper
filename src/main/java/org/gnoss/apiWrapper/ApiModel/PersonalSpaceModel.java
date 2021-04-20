package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

public class PersonalSpaceModel {
	
	private UUID resource_id;
	private UUID user_id;
	private Date saved_date;
	
	
	public UUID getResource_id() {
		return resource_id;
	}
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	public UUID getUser_id() {
		return user_id;
	}
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	public Date getSaved_date() {
		return saved_date;
	}
	public void setSaved_date(Date saved_date) {
		this.saved_date = saved_date;
	} 
	
	

}
