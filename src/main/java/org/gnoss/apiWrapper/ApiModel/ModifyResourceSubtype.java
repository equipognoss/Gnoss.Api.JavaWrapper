package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

public class ModifyResourceSubtype {

	// Community short name
	public String community_short_name;

	// Resource identificator
	public UUID resource_id;

	// Ontology name of the resource to modify
	public String ontology_name;
	
	// Subtype of the resource to modify
	public String subtype;

	// Previous type of the resource
	public String previous_type;

	// The user that try to modify the resource
	public UUID user_id;

	public String getCommunity_short_name() {
		return community_short_name;
	}

	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}

	public UUID getResource_id() {
		return resource_id;
	}

	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}

	public String getOntology_name() {
		return ontology_name;
	}

	public void setOntology_name(String ontology_name) {
		this.ontology_name = ontology_name;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getPrevious_type() {
		return previous_type;
	}

	public void setPrevious_type(String previous_type) {
		this.previous_type = previous_type;
	}

	public UUID getUser_id() {
		return user_id;
	}

	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
}
