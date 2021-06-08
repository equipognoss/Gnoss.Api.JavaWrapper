package org.gnoss.apiWrapper.ApiModel;
/**
 * Represents the image of a resource
 * @author Andrea
 *
 */

import java.util.List;
import java.util.UUID;

public class ResponseGetMainImage {
	
	private UUID resource_id;
	private String path;
	private List<Integer> sizes;
	
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
	 * The image path
	 * @return the image path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * The image path
	 * @param path path
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * All the possible sizes for the image
	 * @return list of possible sizes of the image
	 */
	public List<Integer> getSizes() {
		return sizes;
	}
	/**
	 * All the possible sizes for the image
	 * @param sizes sizes 
	 */
	public void setSizes(List<Integer> sizes) {
		this.sizes = sizes;
	}
	
	
	

}
