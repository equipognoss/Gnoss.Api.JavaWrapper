package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Represents a user 
 * @author Andrea
 *
 */
public class UserCommunity {

	private String name;
	private String last_name;
	private String user_short_name;
	private UUID user_id;
	private String num_resources;
	private String num_comments;
	private List<String> groups;
	
	/**
	 * Name 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/**
	 * Name 
	 * @param name name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Last name or Family name 
	 * @return last name 
	 */
	public String getLast_name() {
		return last_name;
	}
	/**
	 * Last name or Family name 
	 * @param last_name last name 
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	/**
	 * User short name 
	 * @return user short name 
	 */
	public String getUser_short_name() {
		return user_short_name;
	}
	/**
	 * User short name 
	 * @param user_short_name user short name 
	 */
	public void setUser_short_name(String user_short_name) {
		this.user_short_name = user_short_name;
	}
	/**
	 * User identificator
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator
	 * @param user_id user id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Number of resources
	 * @return number of resources
	 */
	public String getNum_resources() {
		return num_resources;
	}
	/**
	 * Number of resources
	 * @param num_resources number of resources 
	 */
	public void setNum_resources(String num_resources) {
		this.num_resources = num_resources;
	}
	/**
	 * Number of comments 
	 * @return number of comments
	 */
	public String getNum_comments() {
		return num_comments;
	}
	/**
	 * Number of comments 
	 * @param num_comments number of comments 
	 */
	public void setNum_comments(String num_comments) {
		this.num_comments = num_comments;
	}
	/**
	 * Groups 
	 * @return list of groups
	 */
	public List<String> getGroups() {
		return groups;
	}
	/**
	 * Groups
	 * @param groups groups 
	 */
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	
}
