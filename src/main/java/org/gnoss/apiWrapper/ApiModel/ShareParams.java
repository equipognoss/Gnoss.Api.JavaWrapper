package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters to share a resource
 * @author Andrea
 *
 */
public class ShareParams {
	
	private String destination_community_short_name;
	private UUID resource_id;
	private List<UUID> categories;
	private String publisher_email;
	
	/**
	 * Community short name where the resource is going to be publisher
	 * @return community short name
	 */
	public String getDestination_community_short_name() {
		return destination_community_short_name;
	}
	/**
	 * Community short name where the resource
	 * @param destination_community_short_name
	 */
	public void setDestination_community_short_name(String destination_community_short_name) {
		this.destination_community_short_name = destination_community_short_name;
	}
	/**
	 * Resource identifier
	 * @return resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource udentifier
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Categories of the destination community
	 * @return List of categories os the destination community
	 */
	public List<UUID> getCategories() {
		return categories;
	}
	/**
	 * Categories of the destination community
	 * @param categories
	 */
	public void setCategories(List<UUID> categories) {
		this.categories = categories;
	}
	
	public String getPublisher_email() {
		return publisher_email;
	}
	public void setPublisher_email(String publisher_email) {
		this.publisher_email = publisher_email;
	}
	
	

}
