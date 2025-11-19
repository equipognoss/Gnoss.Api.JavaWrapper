package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters for add a user in a organization group
 * @author GNOSS
 */
public class ParamsAddUserGroups {
	
	private UUID user_id;
	private String organization_short_name;
	private List<String> groups_short_names;
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
	 * @param uder_id user Id
	 */
	public void setUser_id(UUID uder_id) {
		this.user_id = uder_id;
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
	 * @param organization_short_name Organization short name 
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
	 * @param groups_short_names Groups short names 
	 */
	public void setGroups_short_names(List<String> groups_short_names) {
		this.groups_short_names = groups_short_names;
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