package org.gnoss.apiWrapper.models;

import java.util.ArrayList;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;

public class ListStringOntologyProperty extends OntologyProperty {

	//public ListStringOntologyProperty(String label, ArrayList<String> value){
	public ListStringOntologyProperty(String label, ArrayList value){
		super();
		
		setName(label);
		setValue(value);
	}
	
	//public ListStringOntologyProperty(String label, ArrayList<String> value){
	public ListStringOntologyProperty(String label, ArrayList value, String language) throws GnossAPIArgumentException{
		super();
		
		setName(label);
		setValue(value);
		setLanguage(language);
	}
}
