package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to check if exists a url
 * @author Andrea
 *
 */
public class ExistsUrlParams {

	private String url;
	private String community_short_name;
	
	/**
	 * Url to check
	 * @return url to check
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Url check
	 * @param url Url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * Community short name
	 * @return community short name
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
