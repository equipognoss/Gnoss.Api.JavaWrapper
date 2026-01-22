package org.gnoss.apiWrapper.models;

import com.google.gson.annotations.SerializedName;

/**
 * Enumeration for the state of a massive data load Compatible with Java 25 and
 * Maven
 * 
 * @author Andrea
 */
public enum EstadoCarga {
	/**
	 * Load is pending
	 */
	@SerializedName("0")
	PENDIENTE(0),

	/**
	 * Load is in progress
	 */
	@SerializedName("1")
	EN_PROCESO(1),

	/**
	 * Load is completed
	 */
	@SerializedName("2")
	COMPLETADA(2),

	/**
	 * Load has errors
	 */
	@SerializedName("3")
	CON_ERRORES(3),

	/**
	 * Load is cancelled
	 */
	@SerializedName("4")
	CANCELADA(4);

	private final int value;

	/**
	 * Constructor
	 * 
	 * @param value Numeric value of the state
	 */
	EstadoCarga(int value) {
		this.value = value;
	}

	/**
	 * Gets the numeric value of the state
	 * 
	 * @return Numeric value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets the EstadoCarga from a numeric value
	 * 
	 * @param value Numeric value
	 * @return EstadoCarga corresponding to the value
	 * @throws IllegalArgumentException if the value doesn't match any state
	 */
	public static EstadoCarga fromValue(int value) {
		for (EstadoCarga estado : EstadoCarga.values()) {
			if (estado.value == value) {
				return estado;
			}
		}
		throw new IllegalArgumentException("Invalid EstadoCarga value: " + value);
	}
}