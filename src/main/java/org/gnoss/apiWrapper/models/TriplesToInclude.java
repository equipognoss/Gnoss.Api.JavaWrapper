package org.gnoss.apiWrapper.models;

public class TriplesToInclude {

	//Members
	private String _predicate;
	
	//Properties
	private boolean Title;
	private boolean Description;
	private String NewValue;
	private String Predicate;
	
	//Constructors
	
	/**
	 * Empty constructor
	 */
	public TriplesToInclude(){
		Title = false;
		Description = false;
	}

	/**
	 * Creates a new object to insert a new property in a resource
	 * @param value: Value of the new property
	 * @param predicate: Predicate of the property to be inserted, with namespace without prefix. 
	 * If it's an auxiliary entity property, you must set the predicate as firstLevelPredicate|secondLevelPredicate
	 * @param title: If the property inserted is the title, set his property to TRUE
	 * @param description: If the preoperty inserted is the description, set this property to TRUE 
	 */
	public TriplesToInclude(String value, String predicate, boolean title, boolean description){
		NewValue = value;
		Predicate = predicate;
		
		Title = title;
		Description = description;
	}
	
	/**
	 * Creates a new object to insert a new property in a resource
	 * @param value: Value of the new property
	 * @param predicate: Predicate of the property to be inserted, with namespace without prefix. 
	 * If it's an auxiliary entity property, you must set the predicate as firstLevelPredicate|secondLevelPredicate
	 * @param title: If the property inserted is the title, set his property to TRUE
	 */
	public TriplesToInclude(String value, String predicate, boolean title){
		NewValue = value;
		Predicate = predicate;
		
		Title = title;
		Description = false;
		
	}
	
	/**
	 * Creates a new object to insert a new property in a resource
	 * @param value: Value of the new property
	 * @param predicate: Predicate of the property to be inserted, with namespace without prefix. 
	 * If it's an auxiliary entity property, you must set the predicate as firstLevelPredicate|secondLevelPredicate
	 */
	public TriplesToInclude(String value, String predicate){
		NewValue = value;
		Predicate = predicate;
		
		Title = false;
		Description = false;
	}
	
	//Getters and setters
	public boolean isTitle() {
		return Title;
	}

	public void setTitle(boolean title) {
		Title = title;
	}

	public boolean isDescription() {
		return Description;
	}

	public void setDescription(boolean description) {
		Description = description;
	}

	public String getNewValue() {
		return NewValue;
	}

	public void setNewValue(String newValue) {
		NewValue = newValue;
	}

	public String getPredicate() {
		return Predicate;
	}

	public void setPredicate(String predicate) {
		Predicate = predicate;
	}
	
	
	
}
