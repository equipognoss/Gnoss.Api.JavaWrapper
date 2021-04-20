package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class VotedParameters {
	
	private UUID resource_id;
	private UUID user_id;
	private UUID project_id;
	private float vote_value;
	
	/**
	 * Resource identificator
	 * @return resource identificator
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identificator
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * User identificator who has voted the resource
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator who has voted the resource
	 * @param user_id
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Project identificator
	 * @return project identificator
	 */
	public UUID getProject_id() {
		return project_id;
	}
	/**
	 * Project identificator
	 * @param project_id
	 */
	public void setProject_id(UUID project_id) {
		this.project_id = project_id;
	}
	/**
	 * Vote value
	 * @return vote value
	 */
	public float getVote_value() {
		return vote_value;
	}
	/**
	 * Vote value
	 * @param vote_value
	 */
	public void setVote_value(float vote_value) {
		this.vote_value = vote_value;
	}
	
	
	

}
