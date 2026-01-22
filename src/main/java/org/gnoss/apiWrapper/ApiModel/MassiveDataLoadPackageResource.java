package org.gnoss.apiWrapper.ApiModel;

import java.util.Date;
import java.util.UUID;

/**
 * Parameters of a package of data for a massive data load
 * Compatible with Java 25 and Maven
 * 
 * @author Andrea
 */
public class MassiveDataLoadPackageResource {
	
	private UUID package_id;
	private UUID load_id;
	private String ontology_rute;
	private String search_rute;
	private String sql_rute;
	private byte[] ontology_bytes;
	private byte[] search_bytes;
	private byte[] sql_bytes;	
	private int state;
	private int error;
	private Date date_creation;
	private Date date_processing;
	private String ontology;
	private boolean comprimido;
	private boolean isLast;
	
	/**
	 * Default constructor
	 */
	public MassiveDataLoadPackageResource() {
	}
	
	/**
	 * Package identifier
	 * 
	 * @return Package identifier
	 */
	public UUID getPackage_id() {
		return package_id;
	}
	
	/**
	 * Package identifier
	 * 
	 * @param package_id Package id
	 */
	public void setPackage_id(UUID package_id) {
		this.package_id = package_id;
	}
	
	/**
	 * Load identifier
	 * 
	 * @return Load identifier
	 */
	public UUID getLoad_id() {
		return load_id;
	}
	
	/**
	 * Load identifier
	 * 
	 * @param load_id Load id
	 */
	public void setLoad_id(UUID load_id) {
		this.load_id = load_id;
	}
	
	/**
	 * Ontology file route
	 * 
	 * @return Ontology file route
	 */
	public String getOntology_rute() {
		return ontology_rute;
	}
	
	/**
	 * Ontology file route
	 * 
	 * @param ontology_rute Ontology route
	 */
	public void setOntology_rute(String ontology_rute) {
		this.ontology_rute = ontology_rute;
	}
	
	/**
	 * Search graph file route
	 * 
	 * @return Search graph file route
	 */
	public String getSearch_rute() {
		return search_rute;
	}
	
	/**
	 * Search graph file route
	 * 
	 * @param search_rute Search route
	 */
	public void setSearch_rute(String search_rute) {
		this.search_rute = search_rute;
	}
	
	/**
	 * SQL file route
	 * 
	 * @return SQL file route
	 */
	public String getSql_rute() {
		return sql_rute;
	}
	
	/**
	 * SQL file route
	 * 
	 * @param sql_rute SQL route
	 */
	public void setSql_rute(String sql_rute) {
		this.sql_rute = sql_rute;
	}
	
	/**
	 * Ontology bytes
	 * 
	 * @return Ontology bytes
	 */
	public byte[] getOntology_bytes() {
		return ontology_bytes;
	}
	
	/**
	 * Ontology bytes
	 * 
	 * @param ontology_bytes Ontology bytes
	 */
	public void setOntology_bytes(byte[] ontology_bytes) {
		this.ontology_bytes = ontology_bytes;
	}
	
	/**
	 * Search graph file bytes
	 * 
	 * @return Search graph file bytes
	 */
	public byte[] getSearch_bytes() {
		return search_bytes;
	}
	
	/**
	 * Search graph file bytes
	 * 
	 * @param search_bytes Search bytes
	 */
	public void setSearch_bytes(byte[] search_bytes) {
		this.search_bytes = search_bytes;
	}
	
	/**
	 * SQL file bytes
	 * 
	 * @return SQL file bytes
	 */
	public byte[] getSql_bytes() {
		return sql_bytes;
	}
	
	/**
	 * SQL file bytes
	 * 
	 * @param sql_bytes SQL bytes
	 */
	public void setSql_bytes(byte[] sql_bytes) {
		this.sql_bytes = sql_bytes;
	}
	
	/**
	 * State of the package
	 * 
	 * @return State of the package
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * State of the package
	 * 
	 * @param state State
	 */
	public void setState(int state) {
		this.state = state;
	}
	
	/**
	 * Error in processing the package
	 * 
	 * @return Error in processing the package
	 */
	public int getError() {
		return error;
	}
	
	/**
	 * Error in processing the package
	 * 
	 * @param error Error
	 */
	public void setError(int error) {
		this.error = error;
	}
	
	/**
	 * Date of creation the package
	 * 
	 * @return Date of creation the package
	 */
	public Date getDate_creation() {
		return date_creation;
	}
	
	/**
	 * Date of creation the package
	 * 
	 * @param date_creation Date creation
	 */
	public void setDate_creation(Date date_creation) {
		this.date_creation = date_creation;
	}
	
	/**
	 * Date when the package is processed
	 * 
	 * @return Date when the package is processed
	 */
	public Date getDate_processing() {
		return date_processing;
	}
	
	/**
	 * Date when the package is processed
	 * 
	 * @param date_processing Date processing
	 */
	public void setDate_processing(Date date_processing) {
		this.date_processing = date_processing;
	}
	
	/**
	 * Ontology name
	 * 
	 * @return Ontology name
	 */
	public String getOntology() {
		return ontology;
	}
	
	/**
	 * Ontology name
	 * 
	 * @param ontology Ontology
	 */
	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
	
	/**
	 * The data is compressed
	 * 
	 * @return If the data is compressed
	 */
	public boolean isComprimido() {
		return comprimido;
	}
	
	/**
	 * The data is compressed
	 * 
	 * @param comprimido Comprimido
	 */
	public void setComprimido(boolean comprimido) {
		this.comprimido = comprimido;
	}
	
	/**
	 * The package is the last one
	 * 
	 * @return If the package is the last one
	 */
	public boolean isLast() {
		return isLast;
	}
	
	/**
	 * The package is the last one
	 * 
	 * @param isLast isLast
	 */
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	
	@Override
	public String toString() {
		return "MassiveDataLoadPackageResource{" +
				"package_id=" + package_id +
				", load_id=" + load_id +
				", ontology_rute='" + ontology_rute + '\'' +
				", search_rute='" + search_rute + '\'' +
				", sql_rute='" + sql_rute + '\'' +
				", state=" + state +
				", error=" + error +
				", date_creation=" + date_creation +
				", date_processing=" + date_processing +
				", ontology='" + ontology + '\'' +
				", comprimido=" + comprimido +
				", isLast=" + isLast +
				'}';
	}
}