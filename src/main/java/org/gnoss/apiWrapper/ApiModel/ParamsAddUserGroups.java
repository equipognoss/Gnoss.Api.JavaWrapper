package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Paraneters for add a user in a organization group
 * @author Andrea
 *
 */
public class ParamsAddUserGroups {
	private UUID uder_id;
	private String organization_short_name;
	private List<String> groups_short_names;
	
	/**
	 * User identifier
	 * @return user identifier
	 */
	public UUID getUder_id() {
		return uder_id;
	}
	/**
	 * User identifier
	 * @param uder_id
	 */
	public void setUder_id(UUID uder_id) {
		this.uder_id = uder_id;
	}
	/**
	 * Organization short name
	 * @return organization name 
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
	 * Groups where the user is going to be added 
	 * @return list of groups where the user is going to be added
	 */
	public List<String> getGroups_short_names() {
		return groups_short_names;
	}
	/**
	 * Groups where the user is going to be added
	 * @param groups_short_names
	 */
	public void setGroups_short_names(List<String> groups_short_names) {
		this.groups_short_names = groups_short_names;
	}
	

}
