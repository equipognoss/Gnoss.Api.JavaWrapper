package org.gnoss.apiWrapper.models;

public class TriplesToModify extends TriplesToInclude{

	//Properties
	String OldValue;
	
	
	//Constructors	
	/**
	 * Empty constructor
	 */
	public TriplesToModify(){
		
	}
	
	/**
	 * Creates a new object to modify a property in a resource
	 * @param newValue: New value of the property
	 * @param oldValue: Current value of the property
	 * @param predicate: Predicate of the property to be modified, with namespace without prefix. 
	 * If it's an auxiliary entity property, you must set the predicate as firstLevelPredicate|secondLevelPredicate
	 * @param title: If the property to be modified is the title, set this property to TRUE
	 * @param description: If the property to be modified is the description, set this property to TRUE
	 */
	public TriplesToModify(String newValue, String oldValue, String predicate, boolean title, boolean description){
		super(newValue, predicate, title, description);
		
		OldValue = oldValue;
	}
	
	/**
	 * Creates a new object to modify a property in a resource
	 * @param newValue: New value of the property
	 * @param oldValue: Current value of the property
	 * @param predicate: Predicate of the property to be modified, with namespace without prefix. 
	 * If it's an auxiliary entity property, you must set the predicate as firstLevelPredicate|secondLevelPredicate
	 * @param title: If the property to be modified is the title, set this property to TRUE
	 */
	public TriplesToModify(String newValue, String oldValue, String predicate, boolean title){
		super(newValue, predicate, title);
		
		OldValue = oldValue;
	}
	
	/**
	 * Creates a new object to modify a property in a resource
	 * @param newValue: New value of the property
	 * @param oldValue: Current value of the property
	 * @param predicate: Predicate of the property to be modified, with namespace without prefix. 
	 * If it's an auxiliary entity property, you must set the predicate as firstLevelPredicate|secondLevelPredicate
	 */
	public TriplesToModify(String newValue, String oldValue, String predicate){
		super(newValue, predicate);
		
		OldValue = oldValue;
	}
	
	public String getOldValue(){
		return OldValue;
	}

	public void setOldValue(String oldValue){
		this.OldValue = oldValue;		
	}
	
	
	
}
