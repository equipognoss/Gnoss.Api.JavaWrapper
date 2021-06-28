package org.gnoss.apiWrapper.models;

public enum EstadoCarga {
	
	PENDIENTE(0),
	CORRECTO(1),
	ERRONEO(2),
	EN_PROCESO(3);
	
	private final int value;
	
	EstadoCarga(int value) {
		this.value = value;
	}	
}
