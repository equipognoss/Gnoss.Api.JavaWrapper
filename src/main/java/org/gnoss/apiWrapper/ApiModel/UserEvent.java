package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * User event
 * @author Andrea
 *
 */
public class UserEvent {

	private UUID event_id;
	private String name;
	private Date date;
	
	/**
	 * Event identifier
	 * @return event identifier
	 */
	public UUID getEvent_id() {
		return event_id;
	}
	/**
	 * Event identifier
	 * @param event_id event id 
	 */
	public void setEvent_id(UUID event_id) {
		this.event_id = event_id;
	}
	/**
	 * Event name 
	 * @return event name 
	 */
	public String getName() {
		return name;
	}
	/**
	 * Event name 
	 * @param name name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Event date
	 * @return event date 
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Event date 
	 * @param date date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
}
