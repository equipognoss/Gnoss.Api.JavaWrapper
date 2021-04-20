package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * Parameters to set the publisher os a resource
 * @author Andrea
 *
 */
public class SetPublisherParams {

	private String community_short_name;
	private UUID resource_id;
	private String publisher_email;
	private List<UUID> resource_id_list;
	private boolean keep_editors;
	
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
	 * Email of the resource publisher
	 * @return email of the resource publisher
	 */
	public String getPublisher_email() {
		return publisher_email;
	}
	/**
	 * Email of the resource publisher
	 * @param publisher_email
	 */
	public void setPublisher_email(String publisher_email) {
		this.publisher_email = publisher_email;
	}
	/**
	 * Resource identifier list
	 * @return list of resource identifier
	 */
	public List<UUID> getResource_id_list() {
		return resource_id_list;
	}
	/**
	 * Resource identifier list
	 * @param resource_id_list
	 */
	public void setResource_id_list(List<UUID> resource_id_list) {
		this.resource_id_list = resource_id_list;
	}
	/**
	 * True if the original editors must be keepers
	 * @return True or false
	 */
	public boolean isKeep_editors() {
		return keep_editors;
	}
	/**
	 * True if the original editors must be keepers 
	 * @param keep_editors
	 */
	public void setKeep_editors(boolean keep_editors) {
		this.keep_editors = keep_editors;
	}
	
}
