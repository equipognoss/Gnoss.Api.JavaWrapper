package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to delete a node
 * @author Andrea
 *
 */
public class ParamsDeleteNode {
	private String thesaurus_ontology_url;
	private String resources_ontology_url;
	private String community_short_name;
	private String category_id;
	private String[] path;
	
	/**
	 * URL of the thesaurus ontology
	 * @return URL of the thesaurus ontology
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
	 * Ontology URL of the resources that references this thesaurus
	 * @return Ontology URL
	 */
	public String getResources_ontology_url() {
		return resources_ontology_url;
	}
	/**
	 * Ontology URL of the resources that references this thesaurus 
	 * @param resources_ontology_url
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
	 * Path from root to her last child to which will move the resource that are in the deleted category 
	 * @return path from root 
	 */
	public String[] getPath() {
		return path;
	}
	/**
	 * Path from root to her last child to which will move the resource that are in the deleted category 
	 * @param path
	 */
	public void setPath(String[] path) {
		this.path = path;
	}
	
	

}
