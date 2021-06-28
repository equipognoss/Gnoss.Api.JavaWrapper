package org.gnoss.apiWrapper.Interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gnoss.apiWrapper.Main.ResourceApi;

/**
 * Interface with the methods of the Ontology class
 * @author Andrea
 *
 */
public interface IGnossOCBase {

	/**
	 * Generates the ontology graph triples
	 * @param pResourceApi Api resource
	 * @return List wit the ontology triples
	 */
	ArrayList<String> ToOntologyGnossTriples(ResourceApi pResourceApi);
	/**
	 * Generates the search graph triples
	 * @param pREsourceApi Api resource
	 * @return List with the search graph triples
	 */
	ArrayList<String> ToSearchGraphTriples(ResourceApi pREsourceApi);
	/**
	 * Generates the sql server list dates
	 * @param resourceAPI Api Resource
	 * @return The id of the document and the necessary dates for insert in sql server
	 */
	HashMap<UUID, String> ToAcidData(ResourceApi resourceAPI);
	/**
	 * Generates the object´s URI
	 * @param ResourceAPI Api resource
	 * @return The object´s URI 
	 */
	String GetURI(ResourceApi ResourceAPI);
	
	int GetID();
}
