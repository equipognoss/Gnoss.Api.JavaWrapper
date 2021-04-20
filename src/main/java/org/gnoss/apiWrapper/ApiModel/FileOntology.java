package org.gnoss.apiWrapper.ApiModel;

public class FileOntology {
	private String community_short_name;
	private String ontology_name;
	private String file_name;
	private byte[] file;
	
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public String getOntology_name() {
		return ontology_name;
	}
	public void setOntology_name(String ontology_name) {
		this.ontology_name = ontology_name;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
}
