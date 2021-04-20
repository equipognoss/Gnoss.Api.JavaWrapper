package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Properties of a resource
 * @author Andrea
 *
 */

public class ResourceNoveltiesModel {

	private String community_short_name;
	private UUID resource_id;
	private String title;
	private String description;
	private List<String> tags;
	private List<UUID> categories;
	private int resource_type;
	private List<ResourceTypeName> resource_type_names;
	private String resource_url;
	private String authors;
	private int visibility;
	private List<ReaderEditor> readers_list;
	private List<ReaderEditor> editors_list;
	private Date creation_date;
	private Date last_edition_date;
	private List<ResourceEditionDateByUser> edition_date_by_user;
	private List<ResourceVersionDateByUser> version_date_by_user;
	private ResourceDeleteDateByUser delete_date_by_user;
	private String main_image;
	private List<CommentModel> comments;
	private int views;
	private int plays;
	private int downloads;
	private Date last_view_date;
	private List<VoteModel> votes;
	private List<ShareModel> shared_on;
	private List<PersonalSpaceModel> personal_spaces;
	private String link;
	private List<ItemTypeName> item_type_names;
	
	
	/**
	 * Item type names in different languages
	 * @return item type names
	 */
	public List<ItemTypeName> getItem_type_names() {
		return item_type_names;
	}
	/**
	 * Item type names in different languages
	 * @param item_type_names
	 */
	public void setItem_type_names(List<ItemTypeName> item_type_names) {
		this.item_type_names = item_type_names;
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
	 * Resource identificator
	 * @return resource identificator
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identificator
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Resource title
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Resource title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * Resource description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Resource description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Resource tags 
	 * @return list of tags
	 */
	public List<String> getTags() {
		return tags;
	}
	/**
	 * Resource tags
	 * @param tags
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	/**
	 * Resource categories
	 * @return list of categories
	 */
	public List<UUID> getCategories() {
		return categories;
	}
	/**
	 * Resource categories
	 * @param categories
	 */
	public void setCategories(List<UUID> categories) {
		this.categories = categories;
	}
	/**
	 * Resource type
	 * @return type
	 */
	public int getResource_type() {
		return resource_type;
	}
	/**
	 * Resource type
	 * @param resource_type
	 */
	public void setResource_type(int resource_type) {
		this.resource_type = resource_type;
	}
	/**
	 * Resource type names in different languages
	 * @return list of types names 
	 */
	public List<ResourceTypeName> getResource_type_names() {
		return resource_type_names;
	}
	/**
	 * Resource type names in different languages
	 * @param resource_type_names
	 */
	public void setResource_type_names(List<ResourceTypeName> resource_type_names) {
		this.resource_type_names = resource_type_names;
	}
	/**
	 * Resource url
	 * @return  url 
	 */
	public String getResource_url() {
		return resource_url;
	}
	/**
	 * Resource url 
	 * @param resource_url
	 */
	public void setResource_url(String resource_url) {
		this.resource_url = resource_url;
	}
	/**
	 * Resource authors (comma separated)
	 * @return resource authors
	 */
	public String getAuthors() {
		return authors;
	}
	/**
	 * Resource authors (comma separated)
	 * @param authors
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	/**
	 * Resource visibility
	 * @return visibility
	 */
	public int getVisibility() {
		return visibility;
	}
	/**
	 * REsource visibility
	 * @param visibility
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	/**
	 * Resource readers list
	 * @return readers list
	 */
	public List<ReaderEditor> getReaders_list() {
		return readers_list;
	}
	/**
	 * Resource readers list
	 * @param readers_list
	 */
	public void setReaders_list(List<ReaderEditor> readers_list) {
		this.readers_list = readers_list;
	}
	/**
	 * Resource editors list
	 * @return editors list
	 */
	public List<ReaderEditor> getEditors_list() {
		return editors_list;
	}
	/**
	 * Resource editors list
	 * @param editors_list
	 */
	public void setEditors_list(List<ReaderEditor> editors_list) {
		this.editors_list = editors_list;
	}
	/**
	 * Resource creation date 
	 * @return creation date
	 */
	public Date getCreation_date() {
		return creation_date;
	}
	/**
	 * REsource creation date
	 * @param creation_date
	 */
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	/**
	 * Resource last edition date
	 * @return last edition date
	 */
	public Date getLast_edition_date() {
		return last_edition_date;
	}
	/**
	 * Resource last edition date
	 * @param last_edition_date
	 */
	public void setLast_edition_date(Date last_edition_date) {
		this.last_edition_date = last_edition_date;
	}
	/**
	 * Resource edition date
	 * @return edition date
	 */
	public List<ResourceEditionDateByUser> getEdition_date_by_user() {
		return edition_date_by_user;
	}
	/**
	 * Resource edition date
	 * @param edition_date_by_user
	 */
	public void setEdition_date_by_user(List<ResourceEditionDateByUser> edition_date_by_user) {
		this.edition_date_by_user = edition_date_by_user;
	}
	/**
	 * Resource version date
	 * @return version date
	 */
	public List<ResourceVersionDateByUser> getVersion_date_by_user() {
		return version_date_by_user;
	}
	/**
	 * Resource version date
	 * @param version_date_by_user
	 */
	public void setVersion_date_by_user(List<ResourceVersionDateByUser> version_date_by_user) {
		this.version_date_by_user = version_date_by_user;
	}
	/**
	 * Resource deleted date
	 * @return deleted date
	 */
	public ResourceDeleteDateByUser getDelete_date_by_user() {
		return delete_date_by_user;
	}
	/**
	 * Resource deleted date
	 * @param delete_date_by_user
	 */
	public void setDelete_date_by_user(ResourceDeleteDateByUser delete_date_by_user) {
		this.delete_date_by_user = delete_date_by_user;
	}
	/**
	 * Path of the resource main image
	 * @return path of the resource main image
	 */
	public String getMain_image() {
		return main_image;
	}
	/**
	 * Path of the resource main image
	 * @param main_image
	 */
	public void setMain_image(String main_image) {
		this.main_image = main_image;
	}
	/**
	 * Resource version dates
	 * @return version dates
	 */
	public List<CommentModel> getComments() {
		return comments;
	}
	/**
	 * Resource version dates
	 * @param comments
	 */
	public void setComments(List<CommentModel> comments) {
		this.comments = comments;
	}
	/**
	 * Resource number of views
	 * @return number of views
	 */
	public int getViews() {
		return views;
	}
	/**
	 * Resource number of views
	 * @param views
	 */
	public void setViews(int views) {
		this.views = views;
	}
	/**
	 * Resource number of plays
	 * @return number of plays
	 */
	public int getPlays() {
		return plays;
	}
	/**
	 * Resource number of plays
	 * @param plays
	 */
	public void setPlays(int plays) {
		this.plays = plays;
	}
	/**
	 * Resource number of downloads
	 * @return number of downloads
	 */
	public int getDownloads() {
		return downloads;
	}
	/**
	 * Resource number of downloads
	 * @param downloads
	 */
	public void setDownloads(int downloads) {
		this.downloads = downloads;
	}
	/**
	 * Resource last view date 
	 * @return last view date
	 */
	public Date getLast_view_date() {
		return last_view_date;
	}
	/**
	 * Resource last view date
	 * @param last_view_date
	 */
	public void setLast_view_date(Date last_view_date) {
		this.last_view_date = last_view_date;
	}
	/**
	 * Resource votes
	 * @return  List of votes
	 */
	public List<VoteModel> getVotes() {
		return votes;
	}
	/**
	 * Resource votes
	 * @param votes
	 */
	public void setVotes(List<VoteModel> votes) {
		this.votes = votes;
	}
	/**
	 * Resource sharing
	 * @return list of shared
	 */
	public List<ShareModel> getShared_on() {
		return shared_on;
	}
	/**
	 * Resource sharing
	 * @param shared_on
	 */
	public void setShared_on(List<ShareModel> shared_on) {
		this.shared_on = shared_on;
	}
	/**
	 * Resource saved to user personal space
	 * @return list of personal spaces
	 */
	public List<PersonalSpaceModel> getPersonal_spaces() {
		return personal_spaces;
	}
	/**
	 * Resource saved to user personal space
	 * @param personal_spaces
	 */
	public void setPersonal_spaces(List<PersonalSpaceModel> personal_spaces) {
		this.personal_spaces = personal_spaces;
	}
	/**
	 * If the resource is a link to a web page, this property gets  the url of the web page
	 * @return  url web page
	 */
	public String getLink() {
		return link;
	}
	/**
	 * If the resource is a link to a web page, this property sets  the url of the web page
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	
	
}
