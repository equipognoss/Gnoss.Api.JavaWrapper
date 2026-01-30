package org.gnoss.apiWrapper.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.DataTypes;

public class SecondaryOntology extends BaseOntology{

	//Members
	private UUID _resourceId = UUID.fromString("00000000-0000-0000-0000-000000000000"); 
	
	//Properties
	private ArrayList<SecondaryEntity> SecondaryEntities;
	
	public SecondaryOntology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList, String identifier, ArrayList<SecondaryEntity> secondaryEntityList, ArrayList<OntologyEntity> entityList) throws GnossAPIArgumentException{
		super(graphsUrl, ontologyUrl, rdfType, rdfsLabel, prefixList, propertyList, identifier, entityList);
		
		_resourceId = UUID.randomUUID();
		if(StringUtils.isEmpty(identifier)){
			throw new GnossAPIArgumentException("Required. Identifier can't be null");
		}
		
		SecondaryEntities = secondaryEntityList;
	}

	//Public methods
	@Override
	public byte[] generateRDF() throws IOException, GnossAPIException{
		// Initialize output stream and string builder
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    setStringBuilder(new StringBuilder());
	    StringBuilder stringBuilder = getStringBuilder();
	    byte[] rdfFile = null;
	    
	    // Write RDF header
	    writeRdfHeader();
	    
	    // Convert entity lists to dictionaries
	    HashMap<UUID, OntologyEntity> entitiesDictionary = entityListToEntityDictionary();
	    HashMap<String, SecondaryEntity> secondaryEntityDictionary = secondaryEntityListToSecondaryEntityDictionary();
	    
	    // Validate required fields
	    if (StringUtils.isEmpty(getRdfType()) || StringUtils.isBlank(getRdfType())) {
	        throw new GnossAPIArgumentException("Required. It can't be null or empty", "RdfType");
	    } else if (StringUtils.isEmpty(getRdfsLabel()) || StringUtils.isBlank(getRdfsLabel())) {
	        throw new GnossAPIArgumentException("Required. It can't be null or empty", "RdfsLabel");
	    } else {
	        // First Description (Global description)
	        String line = String.format("<rdf:Description rdf:about=\"%sitems/%s\">\n", 
	        		getGrahpsUrl(), getIdentifier());
	        stringBuilder.append(line);
	        
	        if (!StringUtils.isEmpty(getRdfType()) && !StringUtils.isEmpty(getRdfsLabel())) {
	            write("rdf:type", getRdfType());
	            write("rdfs:label", getRdfsLabel());
	        } else {
	            throw new GnossAPIException("RdfType and RdfLabel are required, they can't be null or empty");
	        }
	        
	        // Write properties
	        writePropertyList(getProperties(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
	        
	        // Write entity first description
	        writeEntityFirstDescription(entitiesDictionary, _resourceId);
	        
	        // Write secondary entity first description
	        writeSecondaryEntityFirstDescription(secondaryEntityDictionary);
	        
	        // Close first description
	        stringBuilder.append("</rdf:Description>\n");
	        
	        // Additional Descriptions
	        writeEntityAdditionalDescription(entitiesDictionary, _resourceId);
	        
	        // Write secondary entity additional description
	        writeSecondaryEntityAdditionalDescription(secondaryEntityDictionary);
	        
	        // Close RDF
	        stringBuilder.append("</rdf:RDF>\n");
	        
	        // Convert to byte array
	        rdfFile = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
	    }
	    
	    return rdfFile;
	}
	
	@Override
	protected void writePropertyList(ArrayList<OntologyProperty> properties, UUID resourceId) throws IOException{
		if(resourceId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))){
			if(properties != null){
				for(OntologyProperty prop : properties){
					if(!StringUtils.isEmpty(prop.getName()) && prop.getValue() != null && !prop.getClass().equals(DataTypes.OntologyPropertyImage.getClass())){
						if(prop.getValue() instanceof ArrayList<?>) {
							for(Object obj : (ArrayList<Object>)prop.getValue()) {
								write(prop.getName(), obj.toString(), prop.getLanguage());
							}
						}
						else {
							write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
						}
					}
				}
			}
		}
	}
	
	private void writeSecondaryEntityFirstDescription(HashMap<String, SecondaryEntity> secondaryEntityDictionary) throws GnossAPIArgumentException, IOException{
		if(secondaryEntityDictionary != null){
			for(String id : secondaryEntityDictionary.keySet()){
				if(!secondaryEntityDictionary.get(id).hasRdfTypeDefined()){
					throw new GnossAPIArgumentException("Required. " + secondaryEntityDictionary.get(id).getRdfType() + " can't be null");
				}
				else if(!secondaryEntityDictionary.get(id).hasRdfsLabelDefined()){
					throw new GnossAPIArgumentException("Required. " + secondaryEntityDictionary.get(id).getRdfsLabel() + " can't be null");
				}
				else{
					if(secondaryEntityDictionary.get(id).hasAnyPropertyWithData()){
						write(secondaryEntityDictionary.get(id).getLabel(), secondaryEntityDictionary.get(id).getIdentifier());
					}
				}
			}
		}
	}
	
	private HashMap<String, SecondaryEntity> secondaryEntityListToSecondaryEntityDictionary(){
		HashMap<String, SecondaryEntity> secondaryEntityDictionary = null;
		if(SecondaryEntities != null){
			for(SecondaryEntity secondaryEntity : SecondaryEntities){
				if(secondaryEntityDictionary == null){
					secondaryEntityDictionary = new HashMap<String, SecondaryEntity>();
				}
				secondaryEntityDictionary.put(secondaryEntity.getIdentifier(), secondaryEntity);
			}
		}
		return secondaryEntityDictionary;
	}
	
	private void writeSecondaryEntityAdditionalDescription(HashMap<String, SecondaryEntity> secondaryEntityDictionary) throws GnossAPIArgumentException, IOException{
		if(secondaryEntityDictionary != null){
			for(String id : secondaryEntityDictionary.keySet()){
				if(!secondaryEntityDictionary.get(id).hasRdfTypeDefined()){
					throw new GnossAPIArgumentException("Required. " + secondaryEntityDictionary.get(id).getRdfType() + " can't be null");
				}
				else if(!secondaryEntityDictionary.get(id).hasRdfsLabelDefined()){
					throw new GnossAPIArgumentException("Required. " + secondaryEntityDictionary.get(id).getRdfsLabel() + " can't be null");
				}
				else{
					getStringBuilder().append("<rdf:Description rdf:about=\"" + secondaryEntityDictionary.get(id).getIdentifier() + "\">\n");
					
					write("rdf:type", secondaryEntityDictionary.get(id).getRdfType());
					write("rdfs:label", secondaryEntityDictionary.get(id).getRdfsLabel());
					
					if(secondaryEntityDictionary.get(id).getProperties() != null){
						for(OntologyProperty prop : secondaryEntityDictionary.get(id).getProperties()){
							if(secondaryEntityDictionary.get(id).hasAnyPropertyWithData()){
								write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
							}
						}
					}
					getStringBuilder().append("</rdf:Description>\n");
				}
			}
		}
	}
	
	public ArrayList<SecondaryEntity> getSecondaryEntities() {
		return SecondaryEntities;
	}

	public void setSecondaryEntities(ArrayList<SecondaryEntity> secondaryEntities) {
		SecondaryEntities = secondaryEntities;
	}
}
