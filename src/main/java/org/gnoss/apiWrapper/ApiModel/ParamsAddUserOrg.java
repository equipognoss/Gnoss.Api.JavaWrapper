package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters for add a user in a organization
 * @author Andrea
 *
 */
public class ParamsAddUserOrg {
	private UUID user_id;
	private String organization_short_name;
	private String position;
	private List<String> communities_short_names;
	
	/**
	 * User identifier
	 * @return user identifier 
	 */
	public UUID getUder_id() {
		return user_id;
	}
	/**
	 * User identifier
	 * @param user_id
	 */
	public void setUder_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Organization short name 
	 * @return Organization name 
	 */
	public String getOrganization_short_name() {
		return organization_short_name;
	}
	/**
	 * Organization short name 
	 * @param organization_short_name
	 */
	public void setOrganization_short_name(String organization_short_name) {
		this.organization_short_name = organization_short_name;
	}
	/**
	 * User position in the organization
	 * @return user position 
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * User position in the organization 
	 * @param position
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * Communities short names where the user is going to be added (The organization must be member of all of them)
	 * @return List of communities names 
	 */
	public List<String> getCommunities_short_names() {
		return communities_short_names;
	}
	/**
	 * Communities short names where the user is going to be added (The organization must be member of all of them)
	 * @param communities_short_names
	 */
	public void setCommunities_short_names(List<String> communities_short_names) {
		this.communities_short_names = communities_short_names;
	} 
}
