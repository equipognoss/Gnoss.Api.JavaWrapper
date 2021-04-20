package org.gnoss.apiWrapper.ApiModel;

import java.util.Map;
import java.util.UUID;

public class ExtraRegisterData {
	private UUID key;
	private String title;
	private Map<UUID, String> options;
	public UUID getKey() {
		return key;
	}
	public void setKey(UUID key) {
		this.key = key;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Map<UUID, String> getOptions() {
		return options;
	}
	public void setOptions(Map<UUID, String> options) {
		this.options = options;
	}
	

}
