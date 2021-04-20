package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * User extra data 
 * @author Andrea
 *
 */
public class ExtraUserData {

	private UUID name_id;
	private String name;
	private UUID value_id;
	private String value;
	
	/**
	 * Identificator of the extra data
	 * @return identificator of the extra data 
	 */
	public UUID getName_id() {
		return name_id;
	}
	/**
	 * Identificator of the extra data 
	 * @param name_id
	 */
	public void setName_id(UUID name_id) {
		this.name_id = name_id;
	}
	/**
	 * Extra data name 
	 * @return data name 
	 */
	public String getName() {
		return name;
	}
	/**
	 * Extra data name 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Extra data value identifier. Only if the list of values has been defined. 
	 * @return data value identifier
	 */
	public UUID getValue_id() {
		return value_id;
	}
	/**
	 * Extra data value identifier. Only if the list of values has been defined. 
	 * @param value_id
	 */
	public void setValue_id(UUID value_id) {
		this.value_id = value_id;
	}
	/**
	 * Extra data value
	 * @return extra data value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Extra data value
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
