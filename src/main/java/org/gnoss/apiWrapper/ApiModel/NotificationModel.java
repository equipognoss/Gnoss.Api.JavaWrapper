package org.gnoss.apiWrapper.ApiModel;

import java.util.List;

public class NotificationModel {

	private String subject;
	private String message;
	private boolean is_html;
	private List<String> receivers;
	private String sender_mask;
	private String community_short_name;
	
	/**
	 * Subject of the email
	 * @return Subject of the email
	 */
	public String getObject() {
		return subject;
	}
	/**
	 * Subject of the email
	 * @param Subject of the email
	 */
	public void setObject(String subject) {
		this.subject = subject;
	}
	/**
	 * Message of the email
	 * @return Message of the email
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Message of the email
	 * @param Message of the email
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * True if the message contains html
	 * @return True or False
	 */
	public boolean isIs_html() {
		return is_html;
	}
	/**
	 * True if the message contains html
	 * @param True or false
	 */
	public void setIs_html(boolean is_html) {
		this.is_html = is_html;
	}
	/**
	 * List of email receivers
	 * @return list of email
	 */
	public List<String> getReceivers() {
		return receivers;
	}
	/**
	 * List of email receivers
	 * @param list of email
	 */
	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}
	/**
	 * Sender mask
	 * @return sender mask
	 */
	public String getSender_mask() {
		return sender_mask;
	}
	/**
	 * Sender mask
	 * @param sender mask
	 */
	public void setSender_mask(String sender_mask) {
		this.sender_mask = sender_mask;
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
	 * @param community short name
	 */
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	
}
