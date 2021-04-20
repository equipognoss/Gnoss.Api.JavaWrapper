package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Parameters to get a resource url
 * @author Andrea
 *
 */
public class ResponseGetUrl {

	private UUID resource_id;
	private String url;
	
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
	 * Url of the resource
	 * @return url of the resource
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Url of the resource
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
