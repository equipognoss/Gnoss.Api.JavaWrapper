package org.gnoss.apiWrapper.models;

/**
 * Counts for each ontology the number of resources and files
 * Compatible with Java 25 and Maven
 * 
 * @author Andrea
 */
public class OntologyCount {

	private int resourcesCount;
	private int fileCount;
	
	/**
	 * Default constructor
	 */
	public OntologyCount() {
	}
	
	/**
	 * Constructor with parameters
	 * 
	 * @param resourcesCount Resources count
	 * @param fileCount File count
	 */
	public OntologyCount(int resourcesCount, int fileCount) {
		this.resourcesCount = resourcesCount;
		this.fileCount = fileCount;
	}
	
	/**
	 * Number of resources in a file
	 * 
	 * @return Resources count
	 */
	public int getResourcesCount() {
		return resourcesCount;
	}
	
	/**
	 * Number of resources in a file
	 * 
	 * @param resourcesCount Resources count
	 */
	public void setResourcesCount(int resourcesCount) {
		this.resourcesCount = resourcesCount;
	}
	
	/**
	 * Number of files of an ontology
	 * 
	 * @return File count
	 */
	public int getFileCount() {
		return fileCount;
	}
	
	/**
	 * Number of files of an ontology
	 * 
	 * @param fileCount File count
	 */
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}
	
	/**
	 * Increment the resource count by one
	 */
	public void incrementResourcesCount() {
		this.resourcesCount++;
	}
	
	/**
	 * Increment the file count by one
	 */
	public void incrementFileCount() {
		this.fileCount++;
	}
	
	/**
	 * Reset both counters to zero
	 */
	public void reset() {
		this.resourcesCount = 0;
		this.fileCount = 0;
	}
	
	@Override
	public String toString() {
		return "OntologyCount{" +
				"resourcesCount=" + resourcesCount +
				", fileCount=" + fileCount +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		OntologyCount that = (OntologyCount) o;
		
		if (resourcesCount != that.resourcesCount) return false;
		return fileCount == that.fileCount;
	}
	
	@Override
	public int hashCode() {
		int result = resourcesCount;
		result = 31 * result + fileCount;
		return result;
	}
}