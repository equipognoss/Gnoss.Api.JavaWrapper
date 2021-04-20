package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Properties of a comment
 * @author Andrea
 *
 */
public class CommentModel {

	private String community_short_name;
	private UUID resource_id;
	private UUID user_id;
	private String html_description;
	private UUID parent_comment_id;
	private Date comment_date; 
	
	/**
	 * Comment creation date
	 * @return creation date 
	 */
	public Date getComment_date() {
		return comment_date;
	}
	/**
	 * comment creation date 
	 * @param comment_date
	 */
	public void setComment_date(Date comment_date) {
		this.comment_date = comment_date;
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
	 * @return identificator 
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
	 * User identificator who has commented the resource
	 * @return user identificator
	 */
	public UUID getUser_id() {
		return user_id;
	}
	/**
	 * User identificator who has commented the resource
	 * @param user_id
	 */
	public void setUser_id(UUID user_id) {
		this.user_id = user_id;
	}
	/**
	 * Description of the comment
	 * @return html description
	 */
	public String getHtml_description() {
		return html_description;
	}
	/**
	 * Description of the comment 
	 * @param html_description
	 */
	public void setHtml_description(String html_description) {
		this.html_description = html_description;
	}
	/**
	 * Parent comment identificator (if the comment is an answer to another comment)
	 * @return parent comment identificator
	 */
	public UUID getParent_comment_id() {
		return parent_comment_id;
	}
	/**
	 * Parent comment identificator (if the comment is an answer to another comment)
	 * @param parent_comment_id
	 */
	public void setParent_comment_id(UUID parent_comment_id) {
		this.parent_comment_id = parent_comment_id;
	}
	
}
