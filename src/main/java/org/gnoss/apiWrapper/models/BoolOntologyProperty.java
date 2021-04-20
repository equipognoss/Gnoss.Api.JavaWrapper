package org.gnoss.apiWrapper.models;

public class BoolOntologyProperty extends OntologyProperty{
	
	public BoolOntologyProperty(String label, boolean value){
		super();
		
		setName(label);
		setValue(value);
	}
}
