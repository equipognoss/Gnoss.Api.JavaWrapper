package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to insert a node 
 * @author Andrea
 *
 */
public class ParamsInsertNode {
	private String thesaurus_ontology_url;
	private String community_short_name;
	private byte[] rdf_category;
	
	/**
	 * URL of the thesaurus ontology 
	 * @return url of the thesaurus
	 */
	public String getThesaurus_ontology_url() {
		return thesaurus_ontology_url;
	}
	/**
	 * URL of the thesaurus ontology 
	 * @param thesaurus_ontology_url Thesaurus ontology url 
	 */
	public void setThesaurus_ontology_url(String thesaurus_ontology_url) {
		this.thesaurus_ontology_url = thesaurus_ontology_url;
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
	/**
	 * RDF of the category 
	 * @return RDF of the category 
	 */
	public byte[] getRdf_category() {
		return rdf_category;
	}
	/**
	 * RDF of the category 
	 * @param rdf_category RDF category
	 */
	public void setRdf_category(byte[] rdf_category) {
		this.rdf_category = rdf_category;
	}
	

}
