package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Properties of the resource edition
 * @author Andrea
 *
 */
public class ResourceEditionDateByUser {

	private UUID resourde_id;
	private Date edition_date;
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
	 * @param resourde_id Resource id 
	 */
	public void setResourde_id(UUID resourde_id) {
		this.resourde_id = resourde_id;
	}
	/**
	 * Resource edition date by user 
	 * @return Edition date
	 */
	public Date getEdition_date() {
		return edition_date;
	}
	/**
	 * Resource edition date by user
	 * @param edition_date Edition date 
	 */
	public void setEdition_date(Date edition_date) {
		this.edition_date = edition_date;
	}
	/**
	 * User identificator who has edited the resource
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator who has edited the resource
	 * @param user_id User id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	} 
	
	
}
