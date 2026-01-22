package org.gnoss.apiWrapper.models;

/**
 * Model for the state of a massive data load
 * Compatible with Java 25 and Maven
 * 
 * @author Andrea
 */
public class EstadoCargaModel {

	private boolean cerrado;
	private EstadoCarga estadoCarga;
	private int numPaquetesPendientes;
	private int numPaquetesCorrectos;
	private int numPaquetesErroneos;
	
	/**
	 * Default constructor
	 */
	public EstadoCargaModel() {
	}
	
	/**
	 * Constructor with parameters
	 * 
	 * @param cerrado True if the load is closed
	 * @param estadoCarga State of the load
	 * @param numPaquetesPendientes Number of pending packages
	 * @param numPaquetesCorrectos Number of correct packages
	 * @param numPaquetesErroneos Number of packages with errors
	 */
	public EstadoCargaModel(boolean cerrado, EstadoCarga estadoCarga, int numPaquetesPendientes, 
			int numPaquetesCorrectos, int numPaquetesErroneos) {
		this.cerrado = cerrado;
		this.estadoCarga = estadoCarga;
		this.numPaquetesPendientes = numPaquetesPendientes;
		this.numPaquetesCorrectos = numPaquetesCorrectos;
		this.numPaquetesErroneos = numPaquetesErroneos;
	}
	
	/**
	 * Gets if the load is closed
	 * 
	 * @return True if closed
	 */
	public boolean isCerrado() {
		return cerrado;
	}
	
	/**
	 * Sets if the load is closed
	 * 
	 * @param cerrado True if closed
	 */
	public void setCerrado(boolean cerrado) {
		this.cerrado = cerrado;
	}
	
	/**
	 * Gets the state of the load
	 * 
	 * @return State of the load
	 */
	public EstadoCarga getEstadoCarga() {
		return estadoCarga;
	}
	
	/**
	 * Sets the state of the load
	 * 
	 * @param estadoCarga State of the load
	 */
	public void setEstadoCarga(EstadoCarga estadoCarga) {
		this.estadoCarga = estadoCarga;
	}
	
	/**
	 * Gets the number of pending packages
	 * 
	 * @return Number of pending packages
	 */
	public int getNumPaquetesPendientes() {
		return numPaquetesPendientes;
	}
	
	/**
	 * Sets the number of pending packages
	 * 
	 * @param numPaquetesPendientes Number of pending packages
	 */
	public void setNumPaquetesPendientes(int numPaquetesPendientes) {
		this.numPaquetesPendientes = numPaquetesPendientes;
	}
	
	/**
	 * Gets the number of correct packages
	 * 
	 * @return Number of correct packages
	 */
	public int getNumPaquetesCorrectos() {
		return numPaquetesCorrectos;
	}
	
	/**
	 * Sets the number of correct packages
	 * 
	 * @param numPaquetesCorrectos Number of correct packages
	 */
	public void setNumPaquetesCorrectos(int numPaquetesCorrectos) {
		this.numPaquetesCorrectos = numPaquetesCorrectos;
	}
	
	/**
	 * Gets the number of packages with errors
	 * 
	 * @return Number of packages with errors
	 */
	public int getNumPaquetesErroneos() {
		return numPaquetesErroneos;
	}
	
	/**
	 * Sets the number of packages with errors
	 * 
	 * @param numPaquetesErroneos Number of packages with errors
	 */
	public void setNumPaquetesErroneos(int numPaquetesErroneos) {
		this.numPaquetesErroneos = numPaquetesErroneos;
	}
	
	@Override
	public String toString() {
		return "EstadoCargaModel{" +
				"cerrado=" + cerrado +
				", estadoCarga=" + estadoCarga +
				", numPaquetesPendientes=" + numPaquetesPendientes +
				", numPaquetesCorrectos=" + numPaquetesCorrectos +
				", numPaquetesErroneos=" + numPaquetesErroneos +
				'}';
	}
}