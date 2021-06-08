package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to move a node
 * @author Andrea
 *
 */
public class ParamsMoveNode {

	private String thesaurus_ontology_url;
	private String resources_ontology_url;
	private String community_short_name;
	private String category_id;
	private String[] path;
	
	/**
	 * Ontology URL of the therausus
	 * @return ontology URL of the thesaurus
	 */
	public String getThesaurus_ontology_url() {
		return thesaurus_ontology_url;
	}
	/**
	 * Ontology URL of the thesaurus
	 * @param thesaurus_ontology_url Thesaurus ontology url 
	 */
	public void setThesaurus_ontology_url(String thesaurus_ontology_url) {
		this.thesaurus_ontology_url = thesaurus_ontology_url;
	}
	/**
	 * Ontology URL of the resources that references this thesaurus
	 * @return Ontology URL
	 */
	public String getResources_ontology_url() {
		return resources_ontology_url;
	}
	/**
	 * Ontology URL of the resources that references this thesaurus
	 * @param resources_ontology_url Resources ontology url 
	 */
	public void setResources_ontology_url(String resources_ontology_url) {
		this.resources_ontology_url = resources_ontology_url;
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
	 * Identificator of the category
	 * @return Category´s identificator
	 */
	public String getCategory_id() {
		return category_id;
	}
	/**
	 * Identificator of the category
	 * @param category_id Category id 
	 */
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	/**
	 * Path from root to the new parent category
	 * @return path from root to the new parent category
	 */
	public String[] getPath() {
		return path;
	}
	/**
	 * Path from root to the new parent category
	 * @param path Path
	 */
	public void setPath(String[] path) {
		this.path = path;
	}
	
}
