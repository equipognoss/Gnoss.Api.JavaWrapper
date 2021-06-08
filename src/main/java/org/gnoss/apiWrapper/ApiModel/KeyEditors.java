package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;
/**
 * Resource editors
 * @author Andrea
 *
 */

public class KeyEditors {

	private UUID resource_id;
	private List<String> editors;
	private List<EditorGroup> editor_groups;
	
	/**
	 * Resource identifier
	 * @return Resource identifiers
	 */
	public UUID getResource_id() {
		return resource_id;
	}
	/**
	 * Resource identifier
	 * @param resource_id Resource id
	 */
	public void setResource_id(UUID resource_id) {
		this.resource_id = resource_id;
	}
	/**
	 * Users short name of the resource editors
	 * @return List of users short name of the resource editors
	 */
	public List<String> getEditors() {
		return editors;
	}
	/**
	 * User short name of the resource editors
	 * @param editors Editors
	 */
	public void setEditors(List<String> editors) {
		this.editors = editors;
	}
	/**
	 * Editors group
	 * @return Editors group
	 */
	public List<EditorGroup> getEditor_groups() {
		return editor_groups;
	}
	/**
	 * Editors group 
	 * @param editor_groups Editor groups
	 */
	public void setEditor_groups(List<EditorGroup> editor_groups) {
		this.editor_groups = editor_groups;
	}
	
	
}
