package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters to change the category name
 * @author Andrea
 *
 */
public class ParamsChangeCategoryName {
	private String community_short_name;
	private UUID category_id;
	private String new_category_name;
	
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
	 * Category_id
	 * @param category_id
	 */
	public void setCategory_id(UUID category_id) {
		this.category_id = category_id;
	}
	/**
	 * New Category name 
	 * @return category name 
	 */
	public String getNew_category_name() {
		return new_category_name;
	}
	/**
	 * New category name 
	 * @param new_category_name
	 */
	public void setNew_category_name(String new_category_name) {
		this.new_category_name = new_category_name;
	}
	
	

}
