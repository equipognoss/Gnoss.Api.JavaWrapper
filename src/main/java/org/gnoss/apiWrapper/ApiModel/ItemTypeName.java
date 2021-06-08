package org.gnoss.apiWrapper.ApiModel;
/**
 * Define the name of the resource type in different languages
 * @author Andrea
 *
 */
public class ItemTypeName {
	private String item_type_name;
	private String item_type_name_language;
	
	/**
	 * Resource type name 
	 * @return type name
	 */
	public String getItem_type_name() {
		return item_type_name;
	}
	/**
	 * Resource type name
	 * @param item_type_name Item type name 
	 */
	public void setItem_type_name(String item_type_name) {
		this.item_type_name = item_type_name;
	}
	/**
	 * Language of the type name
	 * @return language type name 
	 */
	public String getItem_type_name_language() {
		return item_type_name_language;
	}
	/**
	 * Language of the type name 
	 * @param item_type_name_language Item type name language
	 */
	public void setItem_type_name_language(String item_type_name_language) {
		this.item_type_name_language = item_type_name_language;
	}
	

}
