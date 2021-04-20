package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters to creadte a new category 
 * @author Andrea
 *
 */
public class ParamsCreateCategory {
	private String community_short_name;
	private String category_name;
	private UUID parent_category_id;
	
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
	 * Category name 
	 * @return category name 
	 */
	public String getCategory_name() {
		return category_name;
	}
	/**
	 * Category name 
	 * @param category_name
	 */
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	/**
	 * Identificator of the parent category
	 * @return identificator of the parent category 
	 */
	public UUID getParent_category_id() {
		return parent_category_id;
	}
	/**
	 * Identificator of the parent category
	 * @param parent_category_id
	 */
	public void setParent_category_id(UUID parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
	
	

}
