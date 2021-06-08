package org.gnoss.apiWrapper.ApiModel;

import java.util.UUID;

/**
 * Model for closing the massive data load
 * @author Andrea
 *
 */
public class CloseMassiveDataLoadResource {

	private UUID DataLoadIdentifier;

	/**
	 * Data load identifier
	 * @return  data load identifier
	 */
	public UUID getDataLoadIdentifier() {
		return DataLoadIdentifier;
	}
	/**
	 * Data load identifier
	 * @param dataLoadIdentifier dataLoadIdentifier
	 */
	public void setDataLoadIdentifier(UUID dataLoadIdentifier) {
		DataLoadIdentifier = dataLoadIdentifier;
	}
	
}
