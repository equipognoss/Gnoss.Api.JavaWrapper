package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;

public class StringOntologyProperty extends OntologyProperty {

	public StringOntologyProperty(String label, String value){
		super();
		
		setName(label);
		setValue(value);
	}
	
	public StringOntologyProperty(String label, String value, String language) throws GnossAPIArgumentException{
		super();
		
		setName(label);
		setValue(value);
		setLanguage(language);
	}
}
