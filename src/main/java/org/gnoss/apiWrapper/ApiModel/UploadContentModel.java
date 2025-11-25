package org.gnoss.apiWrapper.ApiModel;

public class UploadContentModel {
	private String path;
	private String community_short_name;
	private byte[] bytes_file;
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
	public byte[] getBytes_file() {
		return bytes_file;
	}
	public void setBytes_file(byte[] bytes_file) {
		this.bytes_file = bytes_file;
	}
	

}
