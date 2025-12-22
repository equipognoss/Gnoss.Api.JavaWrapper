package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class TranslateResourceParams {
	
	public UUID resource_id;
	
	public String original_language;
	
	public List<String> target_languages;
	
	public String community_short_name;

	public UUID getResource_id() {
		return resource_id;
	}

	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}

	public String getOriginal_language() {
		return original_language;
	}

	public void setOriginal_language(String original_language) {
		this.original_language = original_language;
	}

	public List<String> getTarget_languages() {
		return target_languages;
	}

	public void setTarget_languages(List<String> target_languages) {
		this.target_languages = target_languages;
	}

	public String getCommunity_short_name() {
		return community_short_name;
	}

	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
}