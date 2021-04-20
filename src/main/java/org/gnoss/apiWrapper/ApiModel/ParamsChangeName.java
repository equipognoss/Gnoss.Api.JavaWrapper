package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to change the name of a node 
 * @author Andrea
 *
 */
public class ParamsChangeName {
	private String thesaurus_ontology_url;
	private String community_short_name;
	private String category_id;
	private String category_name;
	
	/**
	 * URL of the thesaurus ontology
	 * @return URL of the thesaurus
	 */
	public String getThesaurus_ontology_url() {
		return thesaurus_ontology_url;
	}
	/**
	 * URL of the thesaurus ontology
	 * @param thesaurus_ontology_url
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
	 * @param community_short_name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * Identificator of the category 
	 * @return identificator of the category
	 */
	public String getCategory_id() {
		return category_id;
	}
	/**
	 * Identificator of the category 
	 * @param category_id
	 */
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	/**
	 * Name of category
	 * @return name of category
	 */
	public String getCategory_name() {
		return category_name;
	}
	/**
	 * Name of category 
	 * @param category_name
	 */
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
	
	
	

}
