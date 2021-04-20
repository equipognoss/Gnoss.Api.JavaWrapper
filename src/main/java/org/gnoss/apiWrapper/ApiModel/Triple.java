package org.gnoss.apiWrapper.ApiModel;

public class Triple {

	private String subject;
	private String predicate;
	private String object_t;
	private String language;
	
	/**
	 * Subject
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * Subject
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * Predicate
	 * @return predicate
	 */
	public String getPredicate() {
		return predicate;
	}
	/**
	 * Predicate
	 * @param predicate
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	/**
	 * Object 
	 * @return Object
	 */
	public String getObject_t() {
		return object_t;
	}
	/**
	 * Object
	 * @param object_t
	 */
	public void setObject_t(String object_t) {
		this.object_t = object_t;
	}
	/**
	 * Languaje of the object
	 * @return languaje of the object
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * Languaje of the object
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
