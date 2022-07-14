package org.gnoss.apiWrapper.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.UUID;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.DataTypes;

/**
 * Represents the ontology taxonomy.owl
 * @author Andrea
 *
 */
public class TaxonomyOntology extends BaseOntology{
	
	private final String SKOS_NARROWER_LABEL = "skos:narrower";
	private final String SKOS_MEMBER_LABEL = "skos:member";
    private final String XSD = "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"";
    private final String DC = "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"";
    private final String RDFS = "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"";
    private final String RDF = "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"";
    private final String OWL = "xmlns:owl=\"http://www.w3.org/2002/07/owl#\"";
    private final String TAXO = "xmlns:taxo=\"http://www.taxonomy-ontology/2013-10#\"";
    private final String SKOS = "xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"";
    private final String RDF_TYPE_COLLECTION = "http://www.w3.org/2008/05/skos#Collection";
    private final String RDFS_LABEL_COLLECTION = "http://www.w3.org/2008/05/skos#Collection";
    
    private String _rdfFile;
    private String _stringRdfFile;
    private List<ConceptEntity> conceptEntities;
    private StringBuilder file;
    
    /**
     * @throws IOException  IOException
     * @throws GnossAPIException GnossAPIException
     * 
     */
    @Override
    public  String GenerateRDF() throws IOException, GnossAPIException {
    	File file= new File("file.txt");
    	FileWriter fileWriter = null;
    	Reader b2 = null;
    	try {
    		fileWriter = new FileWriter(file);
        	String rdfFile=null;
        	
        	WriteRdfHeader();
        	if(getRdfType()==""|| getRdfType()==null || getRdfType().isEmpty() ) {
        		throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfType");
        	}
        	else if(getRdfsLabel()=="" || getRdfsLabel()==null || getRdfsLabel().isEmpty()){
        		throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfsLabel");
        	}
        	else {
        		fileWriter.write("<rdf:Description rdf:about=\"" +getIdentifier()+"\">");
        		if(getRdfType()!= null || getRdfType().isEmpty() || getRdfsLabel()!=null || getRdfsLabel().isEmpty()) {
        			Write("rdf:type", getRdfType());
    				Write("rdfs:label",getRdfsLabel());
        		}
        		else {
        			throw new GnossAPIException("RdfType and RdfLabel are required, they can't be null or empty");
        		}
        		WritePropertyList(getProperties(), UUID.randomUUID());
        		WriteConceptEntitiesFirstDescription(conceptEntities);
        		fileWriter.write(conceptEntities.toString());
        		fileWriter.write("</rdf:Description>");
        		
        		WriteConceptEntitiesAdditionalDescription(conceptEntities);
        		
        		fileWriter.write("</rdf:RDF>");
        		
        		fileWriter.flush();
        		b2 = new FileReader("fileReader.txt");
        	}
    	}
    	catch (Exception e) {
    		throw new GnossAPIException(e.getMessage());
		}
    	finally {
    		fileWriter.close();
    		b2.close();
    	}
    	return DC;
    }	
    
    private void WriteConceptEntitiesAdditionalDescription(List<ConceptEntity> conceptEntityList) throws GnossAPIArgumentException, IOException {
    	if (conceptEntityList!=null) {
    		for(ConceptEntity ec : conceptEntityList ) {
    			WriteConceptEntityAdditionalDescription(ec);
    		}
    	}
    }
    
	private void WriteConceptEntityAdditionalDescription(ConceptEntity conceptEntity) throws GnossAPIArgumentException, IOException {
		// TODO Auto-generated method stub
		if(!conceptEntity.HasRdfTypeDefined()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfType");
		}
		else if(!conceptEntity.HasRdfsLabelDefined()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfLabel");
		}
		else {
			if(conceptEntity.HasAnyPropertyWithData()) {
				String line="<rdf:Description rdf:about=\""  +conceptEntity.getConceptEntityGnossId() + "\">";
				file.append(line);
				Write("rdf:type", conceptEntity.getRdfType());
				Write("rdfs:label", conceptEntity.getRdfsLabel());
			}
			if(conceptEntity.getProperties()!=null) {
				for(OntologyProperty prop: conceptEntity.getProperties()) {
					if(conceptEntity.HasAnyPropertyWithData()) {
						Write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
					}
				}
			}
			if(conceptEntity.getSubEntities()!=null && conceptEntity.getSubEntities().size()>0) {
				WriteConceptEntitiesFirstDescription(conceptEntity.getSubEntities());
			}
			file.append("</rdf:Description>");
			if(conceptEntity.getSubEntities()!=null && conceptEntity.getSubEntities().size()>0) {
				for(ConceptEntity newEc: conceptEntity.getSubEntities()) {
					WriteConceptEntityAdditionalDescription(newEc);
				}
			}
		}
		
	}
	
	private void WriteConceptEntitiesFirstDescription(List<ConceptEntity> conceptEntityList) throws GnossAPIArgumentException, IOException {
		// TODO Auto-generated method stub
		if(conceptEntityList!=null) {
			for(ConceptEntity ec : conceptEntityList) {
				WriteConceptEntityFirstDescription(ec);
			}
		}
		
	}
	private void WriteConceptEntityFirstDescription(ConceptEntity conceptEntity) throws GnossAPIArgumentException, IOException {
		// TODO Auto-generated method stub
		if(!conceptEntity.HasRdfTypeDefined()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfType");
		}
		else if(!conceptEntity.HasAnyPropertyWithData()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfsLabel");
		}
		else {
			if(conceptEntity.HasAnyPropertyWithData()) {
				if(conceptEntity.getParentIdentifier()==null) {
					Write(SKOS_MEMBER_LABEL, conceptEntity.getConceptEntityGnossId());
				}
				else {
					Write(SKOS_NARROWER_LABEL, conceptEntity.getConceptEntityGnossId());
				}
			}
		}
		
	}
	private void WritePropertyList(List<OntologyProperty> propertyList, UUID resourceId) throws IOException {
		if (resourceId== null) {
			if(propertyList!=null ) {
				for(OntologyProperty prop: propertyList) {
					if(prop.getName()!= null || !prop.getName().isEmpty() && prop.getValue()!=null && !prop.getName().equals(DataTypes.OntologyPropertyImage.getClass())) {
						Write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
					}
				}
			}
		}
	}
	public String get_rdfFile() throws IOException, GnossAPIException {
		this._rdfFile=GenerateRDF();
		return _rdfFile;
	}
	
	public void set_rdfFile(String _rdfFile) {
		this._rdfFile = _rdfFile;
	}
	/**
	 * Gets the content of the RdfFile
	 * @return String _stringRdfFile
	 * @throws IOException IOException
	 * @throws GnossAPIException GnossAPIException
	 */
	public String get_stringRdfFile() throws IOException, GnossAPIException {
		String fich= new String(get_rdfFile());
		File file= new File(fich);
		BufferedReader Reader = null;
		try {
			Reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line=Reader.readLine())!=null) {
				this._stringRdfFile+=line;
			}
			
		}
		catch (Exception e) {
			throw new GnossAPIException(e.getMessage());
		}
		finally {
			Reader.close();
		}
		
		return _stringRdfFile;
	}
	
	/**
	 * Gets the concept entity list of the ontology
	 * @return conceptEntities conceptEntities
	 */
	public List<ConceptEntity> getConceptEntities() {
		return conceptEntities;
	}
	/**
	 * Sets the concept entity list of the ontology
	 * @param conceptEntities concept entities 
	 */
	public void setConceptEntities(List<ConceptEntity> conceptEntities) {
		this.conceptEntities = conceptEntities;
	}
    
    

}
