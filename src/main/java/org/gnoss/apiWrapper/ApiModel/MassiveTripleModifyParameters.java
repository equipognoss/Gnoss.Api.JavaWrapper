package org.gnoss.apiWrapper.ApiModel;

import java.util.List;

public class MassiveTripleModifyParameters {

	private List<MassiveTriple> triples;
	private String ontology;
	private String community_short_name;
	
	/**
	 * Triples to modify
	 * @return List of triples
	 */
	public List<MassiveTriple> getTriples() {
		return triples;
	}
	/**
	 * Triples to modify
	 * @param List of triples
	 */
	public void setTriples(List<MassiveTriple> triples) {
		this.triples = triples;
	}
	/**
	 * Ontology to modify
	 * @return Ontology
	 */
	public String getOntology() {
		return ontology;
	}
	/**
	 * Ontology to modify
	 * @param Ontology
	 
	 */
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	/**
	 * Community Short name
	 * @return community short name
	 */
	public String getCommunity_short_name() {
		return community_short_name;
	}
	/**
	 * Community short name
	 * @param community short name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	
	
	
}
