package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class Triples {
	
	private String community_short_name;
	private UUID resource_id;
	private List<Triple> triples_list;
	private boolean publish_home;
	private boolean end_of_load;
	
	/**
	 * Community short name
	 * @return Community short name
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
	 * @return Resource identifier
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
	 * Triple list
	 * @return Triple list
	 */
	public List<Triple> getTriples_list() {
		return triples_list;
	}
	/**
	 * Triple list
	 * @param triples_list
	 */
	public void setTriples_list(List<Triple> triples_list) {
		this.triples_list = triples_list;
	}
	/**
	 * True if the resource must be published in the home of the community (by default false)
	 * @return True or false
	 */
	public boolean isPublish_home() {
		return publish_home;
	}
	/**
	 * True if the resource must be published in the home of the community (by default false)
	 * @param publish_home
	 */
	public void setPublish_home(boolean publish_home) {
		this.publish_home = publish_home;
	}
	/**
	 * True if it is the end of the load and must delete the cache
	 * @return True or false
	 */
	public boolean isEnd_of_load() {
		return end_of_load;
	}
	/**
	 * True if it is the end of the load and must delete the cache
	 * @param end_of_load
	 */
	public void setEnd_of_load(boolean end_of_load) {
		this.end_of_load = end_of_load;
	}
	
	

}
