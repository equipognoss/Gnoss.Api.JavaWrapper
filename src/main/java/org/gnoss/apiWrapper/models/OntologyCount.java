package org.gnoss.apiWrapper.models;
/**
 * Counts for each ontology the number of resources and files 
 * @author Andrea
 *
 */
public class OntologyCount {

	private int resourcesCount;
	private int fileCount;
	
	/**
	 * Constructor
	 */
	public OntologyCount() {}
	
	/**
	 * Constructor
	 * @param resourcesCount resources count 
	 * @param fileCount file count 
	 */
	public OntologyCount(int resourcesCount, int fileCount) {
		this.fileCount=fileCount;
		this.resourcesCount=resourcesCount;
	}
	
	/**
	 * Number of resources in a file
	 * @return resourcesCount
	 */
	public int getResourcesCount() {
		return resourcesCount;
	}
	/**
	 * Number of resources in a file
	 * @param resourcesCount resources count 
	 */
	public void setResourcesCount(int resourcesCount) {
		this.resourcesCount = resourcesCount;
	}
	/**
	 * Number of files of a ontology
	 * @return fileCount
	 */
	public int getFileCount() {
		return fileCount;
	}
	/**
	 * Number of files of a ontology
	 * @param fileCount file count 
	 */
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}
	
}
