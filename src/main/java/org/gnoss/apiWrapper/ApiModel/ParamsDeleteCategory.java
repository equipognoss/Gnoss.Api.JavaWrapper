package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters to create a new category
 * @author Andrea
 *
 */
public class ParamsDeleteCategory {
	private String community_short_name;
	private UUID category_id;
	
	/**
	 * Community short name 
	 * @return community short name
	 */
	public String getCommunity_short_name() {
		return community_short_name;
	}
	/**
	 * Community short name 
	 * @param community_short_name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * Category id 
	 * @return category id 
	 */
	public UUID getCategory_id() {
		return category_id;
	}
	/**
	 * Category id 
	 * @param category_id
	 */
	public void setCategory_id(UUID category_id) {
		this.category_id = category_id;
	} 
	
	

}
