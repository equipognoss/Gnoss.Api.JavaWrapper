package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Represents the categories of a resource
 * @author Andrea
 *
 */
public class ResponseGetTags {

	private UUID resource_id;
	private List<String> tags;
	
	/**
	 * Resource identifier
	 * @return resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id resource id 
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Resource tags
	 * @return resource tags
	 */
	public List<String> getTags() {
		return tags;
	}
	/**
	 * Resource tags
	 * @param tags tags 
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}
