package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to change the parent node of a node 
 * @author Andrea
 *
 */
public class ParamsParentNode {
	private String thesaurus_ontology_url;
	private String community_short_name;
	private String parent_category_id;
	private String child_category_id;
	
	/**
	 * URL of the thesaurus ontology 
	 * @return URL of the thesaurus ontology
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
	 * Identificator of the parent category 
	 * @return identificator of the parent category 
	 */
	public String getParent_category_id() {
		return parent_category_id;
	}
	/**
	 * Identificator of the parent category 
	 * @param parent_category_id Parent category id 
	 */
	public void setParent_category_id(String parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
	/**
	 * Identificator of the child category 
	 * @return identificator of the child category 
	 */
	public String getChild_category_id() {
		return child_category_id;
	}
	/**
	 * Identificator of the child category 
	 * @param child_category_id Child category id 
	 */
	public void setChild_category_id(String child_category_id) {
		this.child_category_id = child_category_id;
	}
	

}
