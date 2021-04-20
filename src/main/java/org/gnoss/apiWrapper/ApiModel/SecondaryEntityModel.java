package org.gnoss.apiWrapper.ApiModel;

public class SecondaryEntityModel {
	private String ontology_url;
	private String community_short_name;
	private String entity_id;
	private String rdf;
	
	public String getOntology_url() {
		return ontology_url;
	}
	public void setOntology_url(String ontology_url) {
		this.ontology_url = ontology_url;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getEntity_id() {
		return entity_id;
	}
	public void setEntity_id(String entity_id) {
		this.entity_id = entity_id;
	}
	public String getRdf() {
		return rdf;
	}
	public void setRdf(String rdf) {
		this.rdf = rdf;
	}
}
