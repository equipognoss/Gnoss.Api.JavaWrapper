package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class KeyReaders {
	
	private UUID resource_id;
	private List<String> readers;
	private List<ReaderGroup> reader_groups;
	
	/**
	 * Resource identifier
	 * @return resource identifier
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Users short names of the resource
	 * @return List of Users short names of the resource
	 */
	public List<String> getReaders() {
		return readers;
	}
	/**
	 * Users short names of the resource
	 * @param readers
	 */
	public void setReaders(List<String> readers) {
		this.readers = readers;
	}
	/**
	 * Editors group
	 * @return List of editors group
	 */
	public List<ReaderGroup> getReader_groups() {
		return reader_groups;
	}
	/**
	 * Editors group
	 * @param reader_groups
	 */
	public void setReader_groups(List<ReaderGroup> reader_groups) {
		this.reader_groups = reader_groups;
	}
	
	

}
