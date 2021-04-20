package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Represents the categories of a resource
 * @author Andrea
 *
 */
public class ResponseGetCategories {
	
	private UUID resource_id;
	private List<ThesaurusCategory> category_id_list;
	
	/**
	 * Resource identifier
	 * @return resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Categories of the resource
	 * @return List of categories of the resource
	 */
	public List<ThesaurusCategory> getCategory_id_list() {
		return category_id_list;
	}
	/**
	 * Categories of the resource
	 * @param category_id_list
	 */
	public void setCategory_id_list(List<ThesaurusCategory> category_id_list) {
		this.category_id_list = category_id_list;
	}
	
	

}
