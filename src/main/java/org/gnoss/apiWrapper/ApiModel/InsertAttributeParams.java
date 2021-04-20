package org.gnoss.apiWrapper.ApiModel;
/**
 * Parameters to insert attributes
 * @author Andrea
 *
 */
public class InsertAttributeParams {

	private String graph;
	private String value;
	
	/**
	 * Graph Url
	 * @return Graph Url
	 */
	public String getGraph() {
		return graph;
	}
	/**
	 * Graph Url
	 * @param graph
	 */
	public void setGraph(String graph) {
		this.graph = graph;
	}
	/**
	 * Value to insert
	 * @return value to insert
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Value to insert
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
