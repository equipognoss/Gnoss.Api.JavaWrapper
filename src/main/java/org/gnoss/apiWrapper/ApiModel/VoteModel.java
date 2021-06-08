package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Properties of a vote
 * @author Andrea
 *
 */
public class VoteModel {

	private UUID resource_id;
	private UUID user_id;
	private Date vote_time;
	
	/**
	 * Resource identificator
	 * @return identificator
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identificator 
	 * @param resource_id resource id 
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
	 * @param user_id user id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Vote date 
	 * @return vote date
	 */
	public Date getVote_time() {
		return vote_time;
	}
	/**
	 * Vote date
	 * @param vote_time vote time 
	 */
	public void setVote_time(Date vote_time) {
		this.vote_time = vote_time;
	} 
	
	
}
