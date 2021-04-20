package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIException;

public class RemoveTriples {

	//Members
	private String _predicate;
	
	//Properties
	private String Value;
	private short ObjectType;
	private boolean Title;
	private boolean Description;
	
	
	//Constructors
	
	/**
	 * Empty constructor
	 */
	public RemoveTriples(){
		Title = false;
		Description = false;
	}

	/**
	 * Remove the triples from a resource without attached files
	 * @param value: Current value of the property to delete
	 * @param predicate: Predicate of the property to delete. If it's a property of an auxiliary entity, the correct syntax is firstLevelPredicate|secondLevelPredicate
	 * @throws GnossAPIException
	 */
	public RemoveTriples(String value, String predicate) throws GnossAPIException{
		Value = value;
		setPredicate(predicate);
	}
	
	/**
	 * Remove the triples from a resource with attached files
	 * @param value: Current value of the property to delete
	 * @param predicate: Predicate of the property to delete. If it's a property of an auxiliary entity, the correct syntax is firstLevelPredicate|secondLevelPredicate
	 * @param objectType: Objet type. It can be image or attached file
	 * @throws GnossAPIException
	 */
	public RemoveTriples(String value, String predicate, short objectType) throws GnossAPIException{		
		setPredicate(predicate);
		Value = value;
		ObjectType = objectType;
	}

	public String getValue() {
		return Value;
	}


	public void setValue(String value) {
		Value = value;
	}

	public String getPredicate(){
		return _predicate;
	}
	
	public void setPredicate(String predicate) throws GnossAPIException{
		if(predicate.contains("http:")){
			_predicate = predicate;
		}
		else{
			throw new GnossAPIException("the predicate must be a complete uri, not a property with namespace");
		}
	}

	public short getObjectType() {
		return ObjectType;
	}


	public void setObjectType(short objectType) {
		ObjectType = objectType;
	}


	public boolean isTitle() {
		return Title;
	}


	public void setTitle(boolean title) {
		Title = title;
	}


	public boolean isDescription() {
		return Description;
	}


	public void setDescritpion(boolean description) {
		Description = description;
	}
	
	
	
}
