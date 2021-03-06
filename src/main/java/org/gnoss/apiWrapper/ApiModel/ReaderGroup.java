package org.gnoss.apiWrapper.ApiModel;

/**
 * Represents a reader group
 * @author Andrea
 *
 */
public class ReaderGroup {

	private String group_short_name;
	private String organization_short_name;
	
	/**
	 * Group short name
	 * @return Group short name
	 */
	public String getGroup_short_name() {
		return group_short_name;
	}
	/**
	 * Group short name
	 * @param group_short_name Group short name 
	 */
	public void setGroup_short_name(String group_short_name) {
		this.group_short_name = group_short_name;
	}
	/**
	 * Organization short name
	 * @return organization short name
	 */
	public String getOrganization_short_name() {
		return organization_short_name;
	}
	/**
	 * Organization short name
	 * @param organization_short_name Organization short name 
	 */
	public void setOrganization_short_name(String organization_short_name) {
		this.organization_short_name = organization_short_name;
	}
	
	
}
