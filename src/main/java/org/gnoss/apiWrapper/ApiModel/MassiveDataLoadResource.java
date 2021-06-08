package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Parameters to create a massive data load 
 * @author Andrea
 *
 */

public class MassiveDataLoadResource {

	private UUID load_id;
	private String name;
	private int state;
	private Date data_create;
	private UUID organization_id;
	private UUID project_id;
	private UUID identity_id;
	private String community_name;
	
	/**
	 * Load identifier
	 * @return identifier
	 */
	public UUID getLoad_id() {
		return load_id;
	}
	/**
	 * Load identifier
	 * @param load_id Load id 
	 */
	public void setLoad_id(UUID load_id) {
		this.load_id = load_id;
	}
	/**
	 * Load name 
	 * @return name 
	 */
	public String getName() {
		return name;
	}
	/**
	 * Load name 
	 * @param name Name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * State of the data load 
	 * @return state of the data load 
	 */
	public int getState() {
		return state;
	}
	/**
	 * State of the data load 
	 * @param state State
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * Date creation of the data load
	 * @return date creation 
	 */
	public Date getData_create() {
		return data_create;
	}
	/**
	 * Date creation of the data load 
	 * @param data_create Date create
	 */
	public void setData_create(Date data_create) {
		this.data_create = data_create;
	}
	/**
	 * Organization identifier
	 * @return organization identifier
	 */
	public UUID getOrganization_id() {
		return organization_id;
	}
	/**
	 * Organization identifier
	 * @param organization_id Organization id 
	 */
	public void setOrganization_id(UUID organization_id) {
		this.organization_id = organization_id;
	}
	/**
	 * Project identifier
	 * @return project identifier
	 */
	public UUID getProject_id() {
		return project_id;
	}
	/**
	 * Project identifier
	 * @param project_id Project id 
	 */
	public void setProject_id(UUID project_id) {
		this.project_id = project_id;
	}
	/**
	 * Identity identifier
	 * @return identity identifier
	 */
	public UUID getIdentity_id() {
		return identity_id;
	}
	/**
	 * Identity identifier
	 * @param identity_id Identity id 
	 */
	public void setIdentity_id(UUID identity_id) {
		this.identity_id = identity_id;
	}
	/**
	 * Short name of the community
	 * @return short name of the community
	 */
	public String getCommunity_name() {
		return community_name;
	}
	/**
	 * Short name of the community
	 * @param community_name Community short name 
	 */
	public void setCommunity_name(String community_name) {
		this.community_name = community_name;
	}
	
	
}
