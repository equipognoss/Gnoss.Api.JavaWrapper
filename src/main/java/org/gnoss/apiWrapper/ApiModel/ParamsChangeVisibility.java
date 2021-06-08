package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters for add a user in a organization
 * @author Andrea
 *
 */
public class ParamsChangeVisibility {
	private UUID user_id;
	private List<UUID> communities_Id;
	private boolean visibility;
	
	/**
	 * User identifier
	 * @return user  identifier
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
	 * Communities short names where the user is going to be added (The organization must be member of all of them)
	 * @return List of community short names 
	 */
	public List<UUID> getCommunities_Id() {
		return communities_Id;
	}
	/**
	 * Communities short names where the user is going to be added (The organization must be member of all of them)
	 * @param communities_Id Communities id 
	 */
	public void setCommunities_Id(List<UUID> communities_Id) {
		this.communities_Id = communities_Id;
	}
	/**
	 * Communities short names where the user is going to be added (The organization must be member of all of them)
	 * @return visibility
	 */
	public boolean isVisibility() {
		return visibility;
	}
	/**
	 * Communities short names where the user is going to be added (The organization must be member of all of them)
	 * @param visibility Visibility 
	 */
	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
	

}
