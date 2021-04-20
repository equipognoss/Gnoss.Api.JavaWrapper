package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Represents the user community membership information 
 * @author Andrea
 *
 */
public class UserCommunityMembership {
	private UUID user_id;
	private String community_short_name;
	private Date registration_date;
	private boolean administrator_rol;
	
	/**
	 * User identificator
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator
	 * @param user_id
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Community short name 
	 * @return community name 
	 */
	public String getCommunity_short_name() {
		return community_short_name;
	}
	/**
	 * Community short name 
	 * @param community_short_name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * User community registration date 
	 * @return registration date 
	 */
	public Date getRegistration_date() {
		return registration_date;
	}
	/**
	 * User community registration date 
	 * @param registration_date
	 */
	public void setRegistration_date(Date registration_date) {
		this.registration_date = registration_date;
	}
	/**
	 * Indicates if the user manages the community 
	 * @return True if the user manages the community
	 */
	public boolean isAdministrator_rol() {
		return administrator_rol;
	}
	/**
	 * Indicates if the user manages the community
	 * @param administrator_rol
	 */
	public void setAdministrator_rol(boolean administrator_rol) {
		this.administrator_rol = administrator_rol;
	}
	
	

}
