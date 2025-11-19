package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters for change visibility of a user in communities
 * @author GNOSS
 */
public class ParamsChangeVisibility {
	private UUID user_id;
	private List<UUID> communities_Id;
	private boolean visibility;
	private String login;
	
	/**
	 * User identifier
	 * @return user identifier
	 */
	public UUID getUser_id() {
		return user_id;
	}
	
	/**
	 * User identifier
	 * @param user_id User id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	
	/**
	 * Communities identifiers where the user visibility is going to be changed
	 * @return List of community identifiers 
	 */
	public List<UUID> getCommunities_Id() {
		return communities_Id;
	}
	
	/**
	 * Communities identifiers where the user visibility is going to be changed
	 * @param communities_Id Communities id 
	 */
	public void setCommunities_Id(List<UUID> communities_Id) {
		this.communities_Id = communities_Id;
	}
	
	/**
	 * User visibility status
	 * @return visibility
	 */
	public boolean isVisibility() {
		return visibility;
	}
	
	/**
	 * User visibility status
	 * @param visibility Visibility 
	 */
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * User short name or email
	 * @return login
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * User short name or email
	 * @param login login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
}