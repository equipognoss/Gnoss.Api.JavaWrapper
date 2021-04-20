package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Parameters of a package of data for a massive data load 
 * @author Andrea
 *
 */
public class MassiveDataLoadPackageResource {
	private UUID package_id;
	private UUID load_id;
	private String ontology_rute;
	private String search_rute;
	private String sql_rute;
	private int state;
	private int error;
	private Date date_creation;
	private Date date_processing;
	private String ontology;
	private boolean comprimido;
	private  boolean isLast;
	
	/**
	 * Package identifier
	 * @return package identifier
	 */
	public UUID getPackage_id() {
		return package_id;
	}
	/**
	 * Package identifier
	 * @param package_id
	 */
	public void setPackage_id(UUID package_id) {
		this.package_id = package_id;
	}
	/**
	 * Load identifier
	 * @return load identifier
	 */
	public UUID getLoad_id() {
		return load_id;
	}
	/**
	 * Load identifier
	 * @param load_id
	 */
	public void setLoad_id(UUID load_id) {
		this.load_id = load_id;
	}
	/**
	 * Ontology file rute
	 * @return ontology file rute
	 */
	public String getOntology_rute() {
		return ontology_rute;
	}
	/**
	 * Ontology file route
	 * @param ontology_rute
	 */
	public void setOntology_rute(String ontology_rute) {
		this.ontology_rute = ontology_rute;
	}
	/**
	 * Search graph file rute
	 * @return graph file route
	 */
	public String getSearch_rute() {
		return search_rute;
	}
	/**
	 * Search graph file rute
	 * @param search_rute
	 */
	public void setSearch_rute(String search_rute) {
		this.search_rute = search_rute;
	}
	/**
	 * SQL file rute
	 * @return SQL file route
	 */
	public String getSql_rute() {
		return sql_rute;
	}
	/**
	 * SQL file route
	 * @param sql_rute
	 */
	public void setSql_rute(String sql_rute) {
		this.sql_rute = sql_rute;
	}
	/**
	 * State of the package
	 * @return state of the package
	 */
	public int getState() {
		return state;
	}
	/**
	 * State of the package
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
	}
	/**
	 * Error in processing the package
	 * @return error in processing the package
	 */
	public int getError() {
		return error;
	}
	/**
	 * Error in processing the package
	 * @param error
	 */
	public void setError(int error) {
		this.error = error;
	}
	/**
	 * Date of creation the package
	 * @return date of creation the package
	 */
	public Date getDate_creation() {
		return date_creation;
	}
	/**
	 * Date of creation the package
	 * @param date_creation
	 */
	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}
	/**
	 * Date when the package is processed
	 * @return date when the packaged is processed
	 */
	public Date getDate_processing() {
		return date_processing;
	}
	/**
	 * Date when the package is processed
	 * @param date_processing
	 */
	public void setDate_processing(Date date_processing) {
		this.date_processing = date_processing;
	}
	/**
	 * Ontology name 
	 * @return ontology name 
	 */
	public String getOntology() {
		return ontology;
	}
	/**
	 * Ontology name 
	 * @param ontology
	 */
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	/**
	 * The data is compressed 
	 * @return If the data is compress
	 */
	public boolean isComprimido() {
		return comprimido;
	}
	/**
	 * The data is compressed 
	 * @param comprimido
	 */
	public void setComprimido(boolean comprimido) {
		this.comprimido = comprimido;
	}
	/**
	 * The package is the last one
	 * @return If the package is the last one
	 */
	public boolean isLast() {
		return isLast;
	}
	/**
	 * The package is the last one 
	 * @param isLast
	 */
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	

}
