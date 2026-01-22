package org.gnoss.apiWrapper.ApiModel;

/**
 * Parameters to create a massive data load test
 * 
 * @author Andrea
 */
public class MassiveDataLoadTestResource {
	
	private String url;
	private byte[] fileHash;
	
	/**
	 * Default constructor
	 */
	public MassiveDataLoadTestResource() {
	}
	
	/**
	 * Constructor with parameters
	 * 
	 * @param url URL
	 * @param fileHash File hash
	 */
	public MassiveDataLoadTestResource(String url, byte[] fileHash) {
		this.url = url;
		this.fileHash = fileHash;
	}
	
	/**
	 * Get URL
	 * 
	 * @return URL
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set URL
	 * 
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Get file hash
	 * 
	 * @return File hash
	 */
	public byte[] getFileHash() {
		return fileHash;
	}
	
	/**
	 * Set file hash
	 * 
	 * @param fileHash File hash
	 */
	public void setFileHash(byte[] fileHash) {
		this.fileHash = fileHash;
	}
}