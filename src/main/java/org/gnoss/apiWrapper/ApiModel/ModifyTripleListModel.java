package org.gnoss.apiWrapper.ApiModel;

public class ModifyTripleListModel {
	private String community_short_name;
	private String secondary_ontology_url;
	private String secondary_entity;
	private String[][] triple_list;
	
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getSecondary_ontology_url() {
		return secondary_ontology_url;
	}
	public void setSecondary_ontology_url(String secondary_ontology_url) {
		this.secondary_ontology_url = secondary_ontology_url;
	}
	public String getSecondary_entity() {
		return secondary_entity;
	}
	public void setSecondary_entity(String secondary_entity) {
		this.secondary_entity = secondary_entity;
	}
	public String[][] getTriple_list() {
		return triple_list;
	}
	public void setTriple_list(String[][] triple_list) {
		this.triple_list = triple_list;
	}
}
