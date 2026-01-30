package org.gnoss.apiWrapper.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.gnoss.apiWrapper.Main.ResourceApi;

/**
 * Interface with the methods of the Ontology class
 * Compatible with Java 25 and Maven
 * 
 * @author Andrea
 */
public interface IGnossOCBase {

	/**
	 * Generates the ontology graph triples
	 * 
	 * @param pResourceApi Api resource
	 * @return List with the ontology triples
	 */
	ArrayList<String> toOntologyGnossTriples(ResourceApi pResourceApi);
	
	/**
	 * Generates the search graph triples
	 * 
	 * @param pResourceApi Api resource
	 * @return List with the search graph triples
	 */
	ArrayList<String> toSearchGraphTriples(ResourceApi pResourceApi);
	
	/**
	 * Generates the sql server list dates
	 * 
	 * @param resourceAPI Api Resource
	 * @return The id of the document and the necessary dates for insert in sql server
	 */
	HashMap<UUID, String> toAcidData(ResourceApi resourceAPI);
	
	/**
	 * Generates the object's URI
	 * 
	 * @param resourceAPI Api resource
	 * @return The object's URI
	 */
	String getURI(ResourceApi resourceAPI);
	
	/**
	 * Gets the identifier of the resource
	 * 
	 * @return The resource identifier
	 */
	int getID();
}