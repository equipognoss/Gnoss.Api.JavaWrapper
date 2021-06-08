package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user
 * @author Andrea
 *
 */
public class User {
	private String name;
	private String las_name;
	private String email;
	private String password;
	private List<ExtraUserData> extra_data;
	private List<UserEvent> user_events;
	private String aux_data;
	private UUID community_id;
	private String community_short_name;
	private String user_short_name;
	private UUID user_id;
	private String id_card;
	private Date born_date;
	private UUID country_id;
	private String country;
	private UUID province_id;
	private String province;
	private String city;
	private String address;
	private String postal_code;
	private Date join_community_date;
	private String sex;
	private List<ThesaurusCategory> pr;
	private String language;
	
	/**
	 * Name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Name
	 * @param name name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Last name or Family name
	 * @return last name 
	 */
	public String getLas_name() {
		return las_name;
	}
	/**
	 * Last name or Family name 
	 * @param las_name last name 
	 */
	public void setLas_name(String las_name) {
		this.las_name = las_name;
	}
	/**
	 * Email
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Email
	 * @param email email 
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * Password (Only for update not for query)
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * Password (Only for update not for query)
	 * @param password password 
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * User extra data
	 * @return list of user extra data 
	 */
	public List<ExtraUserData> getExtra_data() {
		return extra_data;
	}
	/**
	 * User extra data
	 * @param extra_data entra data
	 */
	public void setExtra_data(List<ExtraUserData> extra_data) {
		this.extra_data = extra_data;
	}
	/**
	 * UserEvents
	 * @return list of user events 
	 */
	public List<UserEvent> getUser_events() {
		return user_events;
	}
	/**
	 * User events 
	 * @param user_events user events
	 */
	public void setUser_events(List<UserEvent> user_events) {
		this.user_events = user_events;
	}
	/**
	 * Auxiliary data 
	 * @return auxiliary data 
	 */
	public String getAux_data() {
		return aux_data;
	}
	/**
	 * Auxiliary data 
	 * @param aux_data aux data
	 */
	public void setAux_data(String aux_data) {
		this.aux_data = aux_data;
	}
	/**
	 * Community identifier
	 * @return community identifier
	 */
	public UUID getCommunity_id() {
		return community_id;
	}
	/**
	 * Community identifier
	 * @param community_id community data
	 */
	public void setCommunity_id(UUID community_id) {
		this.community_id = community_id;
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
	 * @param community_short_name community short name 
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	/**
	 * User short name 
	 * @return user short name 
	 */
	public String getUser_short_name() {
		return user_short_name;
	}
	/**
	 * User short name 
	 * @param user_short_name user short name 
	 */
	public void setUser_short_name(String user_short_name) {
		this.user_short_name = user_short_name;
	}
	/**
	 * User identifier
	 * @return user identifier
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identifier
	 * @param user_id user id 
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * User identification (passport number, dni ...)
	 * @return user identification
	 */
	public String getId_card() {
		return id_card;
	}
	/**
	 * User identification (passport number, dni ...)
	 * @param id_card id card 
	 */
	public void setId_card(String id_card) {
		this.id_card = id_card;
	}
	/**
	 * User born date 
	 * @return user born date 
	 */
	public Date getBorn_date() {
		return born_date;
	}
	/**
	 * User born date
	 * @param born_date born date
	 */
	public void setBorn_date(Date born_date) {
		this.born_date = born_date;
	}
	/**
	 * Country identifier
	 * @return country identifier
	 */
	public UUID getCountry_id() {
		return country_id;
	}
	/**
	 * Country identifier 
	 * @param country_id country id 
	 */
	public void setCountry_id(UUID country_id) {
		this.country_id = country_id;
	}
	/**
	 * User country
	 * @return user country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * User country
	 * @param country country 
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * Province identifier
	 * @return province identifier
	 */
	public UUID getProvince_id() {
		return province_id;
	}
	/**
	 * Province identifier
	 * @param province_id province id 
	 */
	public void setProvince_id(UUID province_id) {
		this.province_id = province_id;
	}
	/**
	 * User province 
	 * @return user province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * User province
	 * @param province province 
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * User city 
	 * @return user city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * User city
	 * @param city city 
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * User address 
	 * @return user address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * User address
	 * @param address address 
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * User postal code 
	 * @return user postal code 
	 */
	public String getPostal_code() {
		return postal_code;
	}
	/**
	 * User postal code 
	 * @param postal_code postal code 
	 */
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	/**
	 * Date when the member has joined to this community
	 * @return date when the member has joined to this community
	 */
	public Date getJoin_community_date() {
		return join_community_date;
	}
	/**
	 * Date when the member has joined to this community
	 * @param join_community_date join community date 
	 */
	public void setJoin_community_date(Date join_community_date) {
		this.join_community_date = join_community_date;
	}
	/**
	 * H for Male or M for Female 
	 * @return H or M 
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * H for Male or M for Female
	 * @param sex sex 
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * User preferences 
	 * @return list of preferences 
	 */
	public List<ThesaurusCategory> getPr() {
		return pr;
	}
	/**
	 * User preferences 
	 * @param pr preferences 
	 */
	public void setPr(List<ThesaurusCategory> pr) {
		this.pr = pr;
	}
	/**
	 * True if this user must receive the community newsletter
	 * @return True or false 
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * True if this user must receive the community newsletter
	 * @param language language 
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	

}
