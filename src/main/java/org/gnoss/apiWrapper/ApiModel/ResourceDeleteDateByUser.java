package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Properties od the resource deleted
 * @author Andrea
 *
 */
public class ResourceDeleteDateByUser {

	private UUID resourde_id;
	private Date delete_date;
	private UUID user_id;
	
	/**
	 * Resource identificator
	 * @return identificator
	 */
	public UUID getResourde_id() {
		return resourde_id;
	}
	/**
	 * Resource identificator
	 * @param resourde_id
	 */
	public void setResourde_id(UUID resourde_id) {
		this.resourde_id = resourde_id;
	}
	/**
	 * Resource deleted date by user
	 * @return deleted date
	 */
	public Date getDelete_date() {
		return delete_date;
	}
	/**
	 * Resource deleted date by user 
	 * @param delete_date
	 */
	public void setDelete_date(Date delete_date) {
		this.delete_date = delete_date;
	}
	/**
	 * User identificator who has deleted the resource
	 * @return user identifiator
	 */
	public UUID getUder_id() {
		return user_id;
	}
	/**
	 * User identificator who has deleted the resource
	 * @param uder_id
	 */
	public void setUder_id(UUID user_id) {
		this.user_id = user_id;
	} 
	
	
}
