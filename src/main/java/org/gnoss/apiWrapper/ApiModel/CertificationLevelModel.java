package org.gnoss.apiWrapper.ApiModel;

import java.util.List;

public class CertificationLevelModel {
	private String community_short_name;
	private List<String> certification_levels;
	private String certification_politics;
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public List<String> getCertification_levels() {
		return certification_levels;
	}
	public void setCertification_levels(List<String> certification_levels) {
		this.certification_levels = certification_levels;
	}
	public String getCertification_politics() {
		return certification_politics;
	}
	public void setCertification_politics(String certification_politics) {
		this.certification_politics = certification_politics;
	}
	

}
