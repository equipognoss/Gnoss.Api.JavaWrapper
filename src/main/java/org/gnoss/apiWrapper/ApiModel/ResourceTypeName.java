package org.gnoss.apiWrapper.ApiModel;
/**
 * Define the name of the resource type in different languages
 * @author Andrea
 *
 */
public class ResourceTypeName {

	private String resource_type_name;
	private String resource_type_name_language;
	
	/**
	 * Resource type name
	 * @return type name
	 */
	public String getResource_type_name() {
		return resource_type_name;
	}
	/**
	 * Resource type name
	 * @param resource_type_name resource type name
	 */
	public void setResource_type_name(String resource_type_name) {
		this.resource_type_name = resource_type_name;
	}
	/**
	 * Language of the type name 
	 * @return resource type name language
	 */
	public String getResource_type_name_language() {
		return resource_type_name_language;
	}
	/**
	 * Language of the type name 
	 * @param resource_type_name_language resource type name language
	 */
	public void setResource_type_name_language(String resource_type_name_language) {
		this.resource_type_name_language = resource_type_name_language;
	}
	
}
