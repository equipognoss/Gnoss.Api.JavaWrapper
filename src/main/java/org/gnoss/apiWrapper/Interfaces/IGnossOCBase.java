package org.gnoss.apiWrapper.Interfaces;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.gnoss.apiWrapper.Main.ResourceApi;

/**
 * Interface with the methods of thhe Ontology class
 * @author Andrea
 *
 */
public interface IGnossOCBase {

	/**
	 * Generates the ontology graph triples
	 * @param pResourceApi <Api resource>
	 * @return List wit the ontology triples
	 */
	List<String> ToOntologyGnossTriples(ResourceApi pResourceApi);
	/**
	 * Generates the search graph triples
	 * @param pREsourceApi <Api resource>
	 * @return List with the search graph triples
	 */
	List<String> ToSearchGraphTriples(ResourceApi pREsourceApi);
	/**
	 * Generates the sql server list dates
	 * @param resourceAPI <Api Resource>
	 * @return The id of the document and the necessary dates for insert in sql server
	 */
	Map<UUID, String> ToAcidData(ResourceApi resourceAPI);
	/**
	 * Generates the object´s URI
	 * @param ResourceAPI <Api resource>
	 * @return The object´s URI 
	 */
	String GetURI(ResourceApi ResourceAPI);
	
	int GetID();
}
