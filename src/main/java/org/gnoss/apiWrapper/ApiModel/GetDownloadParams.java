package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;
/**
 * Parameters to get the download URL of a resource 
 * @author Andrea
 *
 */

public class GetDownloadParams {
	
	private List<UUID> resource_id_list;
	private String community_short_name;
	
	/**
	 * Resource list to get their download URL
	 * @return  resource_id_list resource list to get their download URL
	 */
	public List<UUID> getResource_id_list() {
		return resource_id_list;
	}
	/**
	 * Resource list to get their download URL
	 * @param resource_id_list Resource id list
	 */
	public void setResource_id_list(List<UUID> resource_id_list) {
		this.resource_id_list = resource_id_list;
	}
	/**
	 * Community short name
	 * @return Community short name 
	 */
	public String getCommunity_short_name() {
		return community_short_name;
	}
	/**
	 * Community short name 
	 * @param community_short_name Community short name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	
	

}
