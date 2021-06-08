package org.gnoss.apiWrapper.ApiModel;

public class MassiveTriple {
	
	private String main_resource_id;
	private String subject;
	private String predicate;
	private String new_value;
	private String old_value;
	private boolean is_new_auxiliary_entity;
	private String language;
	
	/**
	 * Main resource id, like http://gnoss.com/items/Product_0015bb97-81fd-5ee7-a70d-4474f4c723e9_9b8d4c13-b443-4e3c-9a93-252d9e12df8a
	 * @return main resource id
	 */
	public String getMain_resource_id() {
		return main_resource_id;
	}
	/**
	 * Main resource id, like http://gnoss.com/items/Product_0015bb97-81fd-5ee7-a70d-4474f4c723e9_9b8d4c13-b443-4e3c-9a93-252d9e12df8a
	 * @param main_resource_id main resource id
	 */
	public void setMain_resource_id(String main_resource_id) {
		this.main_resource_id = main_resource_id;
	}
	/**
	 * Subject of the triple
	 * @return Subject of the triple
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * Subject of the triple
	 * @param subject  subject of the triple
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * Predicate of the triple
	 * @return predicate of the triple
	 */
	public String getPredicate() {
		return predicate;
	}
	/**
	 * Predicate of the triple
	 * @param predicate predicate of the triple
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	/**
	 * New value of the triple
	 * @return new value of the triple
	 */
	public String getNew_value() {
		return new_value;
	}
	/**
	 * New value of the triple
	 * @param new_value new value of the triple
	 */
	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}
	/**
	 * Old value of the triple
	 * @return Old value of the triple
	 */
	public String getOld_value() {
		return old_value;
	}
	/**
	 * Old value of the triple
	 * @param old_value Old value of the triple
	 */
	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}
	/**
	 * Indicate if the new value is the subject of a new auxiliary entity.
	 * You must set it to true if you are creating a new auxiliary entity.
	 * If it is a new auxiliary entity, you must send the rdf:type and rdfs:label triples too. 
	 * @return True if the new value is the subject of a new auxiliary entity.
	 */
	public boolean isIs_new_auxiliary_entity() {
		return is_new_auxiliary_entity;
	}
	/**
	 * Indicate if the new value is the subject of a new auxiliary entity.
	 * You must set it to true if you are creating a new auxiliary entity.
	 * If it is a new auxiliary entity, you must send the rdf:type and rdfs:label triples too. 
	 * @param is_new_auxiliary_entity True if the new value is the subject of a new auxiliary entity.
	 */
	public void setIs_new_auxiliary_entity(boolean is_new_auxiliary_entity) {
		this.is_new_auxiliary_entity = is_new_auxiliary_entity;
	}
	/**
	 * Indicates the language of the object
	 * @return The language of the object
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * Indicates the language of the object
	 *@param language The languaje of the object
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	

}
