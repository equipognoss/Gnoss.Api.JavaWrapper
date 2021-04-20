package org.gnoss.apiWrapper.ApiModel;

public class SparqlQuery {
	private String ontology;
	private String community_short_name;
	private String query_select;
	private String query_where;
	
	public String getOntology() {
		return ontology;
	}
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getQuery_select() {
		return query_select;
	}
	public void setQuery_select(String query_select) {
		this.query_select = query_select;
	}
	public String getQuery_where() {
		return query_where;
	}
	public void setQuery_where(String query_where) {
		this.query_where = query_where;
	}
}
