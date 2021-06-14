package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;

/**
 * Represents a user
 * @author Andrea
 *
 */
public class Userlite {

	private String name;
	private String last_name;
	private String user_short_name;
	private String image;
	private Date born_date;
	
	/**
	 * Name 
	 * @return name name
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
	 * Imagen
	 * @return imagen imagen
	 */
	public String getImage() {
		return image;
	}
	/**
	 * Imagen
	 * @param image image 
	 */
	public void setImage(String image) {
		this.image = image;
	}
	/**
	 * User born date 
	 * @return User born date 
	 */
	public Date getBorn_date() {
		return born_date;
	}
	/**
	 * User born date
	 * @param born_date born date 
	 */
	public void setBorn_date(Date born_date) {
		this.born_date = born_date;
	}
	
}
