package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

/**
 * 
 * @author Andrea
 *
 */
public class MassiveResourceLoad {
	private List<LoadResourceParams> resources;
	private UUID load_id;
	
	
	public List<LoadResourceParams> getResources() {
		return resources;
	}
	public void setResources(List<LoadResourceParams> resources) {
		this.resources = resources;
	}
	public UUID getLoad_id() {
		return load_id;
	}
	public void setLoad_id(UUID load_id) {
		this.load_id = load_id;
	}
	

}
