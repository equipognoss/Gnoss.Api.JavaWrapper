package org.gnoss.apiWrapper.models;

import java.util.ArrayList;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;

public class SecondaryEntity extends OntologyEntity{

	private String Identifier;
	
	public SecondaryEntity(String rdfType, String rdfsLabel, String label, String identifier) throws GnossAPIArgumentException{
		super(rdfType, rdfsLabel, label);
		Identifier = identifier;
	}
	
	public SecondaryEntity(String rdfType, String rdfsLabel, String label, String identifier, ArrayList<OntologyProperty> properties) throws GnossAPIArgumentException{
		super(rdfType, rdfsLabel, label, properties);
		Identifier = identifier;
	}
	
	protected SecondaryEntity() throws GnossAPIArgumentException{
		super("", "", "");		
	}

	public String getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}
}
