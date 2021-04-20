package org.gnoss.apiWrapper.ApiModel;

public class UploadContentModel {
	private String path;
	private String community_short_name;
	private byte[] bytes_files;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getCommunity_short_name() {
		return community_short_name;
	}
	public void setCommunity_short_name(String community_short_name) {
		this.community_short_name = community_short_name;
	}
	public byte[] getBytes_files() {
		return bytes_files;
	}
	public void setBytes_files(byte[] bytes_files) {
		this.bytes_files = bytes_files;
	}
	

}
