package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Properties of the resource version
 * @author Andrea
 *
 */
public class ResourceVersionDateByUser {

	private UUID resource_id;
	private Date version_date;
	private UUID user_id;
	
	/**
	 * Resource identificator
	 * @return identificator
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identificator
	 * @param resource_id Resource id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Resource version date by user
	 * @return Version date
	 */
	public Date getVersion_date() {
		return version_date;
	}
	/**
	 * Resource version date by user
	 * @param version_date version date
	 */
	public void setVersion_date(Date version_date) {
		this.version_date = version_date;
	}
	/**
	 * User identificator who has versioned the resource
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator who has versioned the resource
	 * @param user_id user id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	
	
}
