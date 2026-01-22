package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Model for closing the massive data load
 * Compatible with Java 25 and Maven
 * 
 * @author Andrea
 */
public class CloseMassiveDataLoadResource {

	private UUID dataLoadIdentifier;

	/**
	 * Default constructor
	 */
	public CloseMassiveDataLoadResource() {
	}
	
	/**
	 * Constructor with parameters
	 * 
	 * @param dataLoadIdentifier Data load identifier
	 */
	public CloseMassiveDataLoadResource(UUID dataLoadIdentifier) {
		this.dataLoadIdentifier = dataLoadIdentifier;
	}
	
	/**
	 * Data load identifier
	 * 
	 * @return Data load identifier
	 */
	public UUID getDataLoadIdentifier() {
		return dataLoadIdentifier;
	}
	
	/**
	 * Data load identifier
	 * 
	 * @param dataLoadIdentifier Data load identifier
	 */
	public void setDataLoadIdentifier(UUID dataLoadIdentifier) {
		this.dataLoadIdentifier = dataLoadIdentifier;
	}
	
	@Override
	public String toString() {
		return "CloseMassiveDataLoadResource{" +
				"dataLoadIdentifier=" + dataLoadIdentifier +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		CloseMassiveDataLoadResource that = (CloseMassiveDataLoadResource) o;
		
		return dataLoadIdentifier != null ? dataLoadIdentifier.equals(that.dataLoadIdentifier) : that.dataLoadIdentifier == null;
	}
	
	@Override
	public int hashCode() {
		return dataLoadIdentifier != null ? dataLoadIdentifier.hashCode() : 0;
	}
}