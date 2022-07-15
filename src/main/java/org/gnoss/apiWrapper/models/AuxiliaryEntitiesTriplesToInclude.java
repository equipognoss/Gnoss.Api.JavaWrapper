package org.gnoss.apiWrapper.models;

import java.util.UUID;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;

/**
 * Auxiliary entities triples
 * @author Andrea
 *
 */
public class AuxiliaryEntitiesTriplesToInclude {
	
	private String value;
	private String predicate;
	private String name;
	private UUID identifier;
	/**
	 * Empty constructor
	 */
	public AuxiliaryEntitiesTriplesToInclude() {}
	
	/**
	 * Constructor of AuxiliaryEntitiesTriplesToInclude
	 * @param value value
	 * @param predicate predicate 
	 * @param entityName entity name 
	 * @param entityIdentifier entity identifier 
	 */
	public AuxiliaryEntitiesTriplesToInclude(String value, String predicate, String entityName, UUID entityIdentifier) {
		this.value=value;
		this.predicate=predicate;
		this.name=entityName;
		this.identifier=entityIdentifier;
	}

	/**
	 * Gets the value of thhe predicate
	 * @return String value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * Sets the value of the predicate
	 * @param value value 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the predicate of the auxiliary entity
	 * @return String predicate
	 */
	public String getPredicate() {
		return predicate;
	}
	/**
	 * Sets the predicate of the auxiliary entity
	 * @param predicate predicate 
	 * @throws GnossAPIArgumentException  GnossAPIArgumentException
	 */
	public void setPredicate(String predicate) throws GnossAPIArgumentException {
		if(value.contains("|")) {
			this.predicate=value;
			
		}else {
			throw new GnossAPIArgumentException("The label must be complete, with complete namespace of the auxiliary entity property + | + complete namespace of the property to load");
		}
		this.predicate = value;
		
	}

	/**
	 * Gets the entity name
	 * For example, in http://gnoss.com/items/article_223b30c1-2552-4ed0-ba5f-e257585b08bf_9c126c3a-7850-4cdc-b176-95ae6fd0bb78
     * the entity name is: article
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/** 
	 * Sets the entity name
	 * For example, in http://gnoss.com/items/article_223b30c1-2552-4ed0-ba5f-e257585b08bf_9c126c3a-7850-4cdc-b176-95ae6fd0bb78
     * the entity name is: article
	 * @param name name 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the entity identifier
	 * For example in http://gnoss.com/items/article_223b30c1-2552-4ed0-ba5f-e257585b08bf_9c126c3a-7850-4cdc-b176-95ae6fd0bb78
     * the identifier is: 9c126c3a-7850-4cdc-b176-95ae6fd0bb78
	 * @return UUID identifier
	 */
	public UUID getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the entity identifier 
	 * in http://gnoss.com/items/article_223b30c1-2552-4ed0-ba5f-e257585b08bf_9c126c3a-7850-4cdc-b176-95ae6fd0bb78
     * the identifier is: 9c126c3a-7850-4cdc-b176-95ae6fd0bb78
	 * @param identifier identifier 
	 */
	public void setIdentifier(UUID identifier) {
		this.identifier = identifier;
	}
	
	
}
