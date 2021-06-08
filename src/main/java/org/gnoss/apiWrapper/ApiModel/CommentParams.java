package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Parametrs for comments
 * @author Andrea
 *
 */
public class CommentParams {

	private String community_short_name;
	private UUID resource_id;
	private String user_short_name;
	private String html_description;
	private UUID parent_comment_id;
	private Date comment_date;
	private boolean pusblish_home;
	
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
	 * Resource identifier
	 * @return resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id resource Id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * User short name
	 * @return user_short_name 
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
	 * Description of the comment
	 * @return description of the comment
	 */
	public String getHtml_description() {
		return html_description;
	}
	/**
	 * Description of the comment
	 * @param html_description html description
	 */
	public void setHtml_description(String html_description) {
		this.html_description = html_description;
	}
	/**
	 * Parent comment identifier (if the comment is an answer to another comment)
	 * @return parent comment identifier
	 */
	public UUID getParent_comment_id() {
		return parent_comment_id;
	}
	/**
	 * Parent comment identifier (if the comment is an answer to another comment)
	 * @param parent_comment_id parent comment Id
	 */
	public void setParent_comment_id(UUID parent_comment_id) {
		this.parent_comment_id = parent_comment_id;
	}
	/**
	 * Comment date
	 * @return comment date
	 */
	public Date getComment_date() {
		return comment_date;
	}
	/**
	 * Comment date
	 * @param comment_date Comment date
	 */
	public void setComment_date(Date comment_date) {
		this.comment_date = comment_date;
	}
	/**
	 * True if the comment must be published in the community home 
	 * @return True or false
	 */
	public boolean isPusblish_home() {
		return pusblish_home;
	}
	/**
	 * True if the comment must be published in the community home
	 * @param pusblish_home publishing home
	 */
	public void setPusblish_home(boolean pusblish_home) {
		this.pusblish_home = pusblish_home;
	}
	
}
