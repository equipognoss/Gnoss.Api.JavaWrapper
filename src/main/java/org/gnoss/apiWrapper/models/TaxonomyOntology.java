package org.gnoss.apiWrapper.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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
    
    private byte[] _rdfFile;
    private String _stringRdfFile;
    private List<ConceptEntity> conceptEntities;
    private StringBuilder file;
    
    /**
     * @throws IOException  IOException
     * @throws GnossAPIException GnossAPIException
     * 
     */
    @Override
    public byte[] generateRDF() throws IOException, GnossAPIException {
        // Initialize StringBuilder for building RDF content
        setStringBuilder(new StringBuilder());
        StringBuilder stringBuilder = getStringBuilder();
        byte[] rdfFile = null;
        
        // Write RDF header
        writeRdfHeader();
        
        // Validate required fields
        if (StringUtils.isEmpty(getRdfType()) || StringUtils.isBlank(getRdfType())) {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "RdfType");
        } else if (StringUtils.isEmpty(getRdfsLabel()) || StringUtils.isBlank(getRdfsLabel())) {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "RdfsLabel");
        } else {
            // First Description (Global description)
            stringBuilder.append(String.format("<rdf:Description rdf:about=\"%s\">\n", getIdentifier()));
            
            if (!StringUtils.isEmpty(getRdfType()) && !StringUtils.isEmpty(getRdfsLabel())) {
                write("rdf:type", getRdfType());
                write("rdfs:label", getRdfsLabel());
            } else {
                throw new GnossAPIException("RdfType and RdfLabel are required, they can't be null or empty");
            }
            
            // Write properties
            writePropertyList(getProperties(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
            
            // Write concept entities first description
            writeConceptEntitiesFirstDescription(getConceptEntities());
            
            // Close first description
            stringBuilder.append("</rdf:Description>\n");
            
            // Additional Descriptions
            writeConceptEntitiesAdditionalDescription(getConceptEntities());
            
            // Close RDF
            stringBuilder.append("</rdf:RDF>\n");
            
            // Convert to byte array
            rdfFile = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        }
        
        return rdfFile;
    }

    /**
     * Gets the RDF file as a string
     * @return RDF content as string
     * @throws IOException if there's an I/O error
     * @throws GnossAPIException if there's a validation error
     */
    public String getStringRdfFile() throws IOException, GnossAPIException {
        byte[] rdfBytes = generateRDF();
        return new String(rdfBytes, StandardCharsets.UTF_8);
    }	
    
    private void writeConceptEntitiesAdditionalDescription(List<ConceptEntity> conceptEntityList) throws GnossAPIArgumentException, IOException {
    	if (conceptEntityList!=null) {
    		for(ConceptEntity ec : conceptEntityList ) {
    			writeConceptEntityAdditionalDescription(ec);
    		}
    	}
    }
    
	private void writeConceptEntityAdditionalDescription(ConceptEntity conceptEntity) throws GnossAPIArgumentException, IOException {
		// TODO Auto-generated method stub
		if(!conceptEntity.hasRdfTypeDefined()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfType");
		}
		else if(!conceptEntity.hasRdfsLabelDefined()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfLabel");
		}
		else {
			if(conceptEntity.hasAnyPropertyWithData()) {
				String line="<rdf:Description rdf:about=\""  +conceptEntity.getConceptEntityGnossId() + "\">";
				file.append(line);
				write("rdf:type", conceptEntity.getRdfType());
				write("rdfs:label", conceptEntity.getRdfsLabel());
			}
			if(conceptEntity.getProperties()!=null) {
				for(OntologyProperty prop: conceptEntity.getProperties()) {
					if(conceptEntity.hasAnyPropertyWithData()) {
						write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
					}
				}
			}
			if(conceptEntity.getSubEntities()!=null && conceptEntity.getSubEntities().size()>0) {
				writeConceptEntitiesFirstDescription(conceptEntity.getSubEntities());
			}
			file.append("</rdf:Description>");
			if(conceptEntity.getSubEntities()!=null && conceptEntity.getSubEntities().size()>0) {
				for(ConceptEntity newEc: conceptEntity.getSubEntities()) {
					writeConceptEntityAdditionalDescription(newEc);
				}
			}
		}
		
	}
	
	private void writeConceptEntitiesFirstDescription(List<ConceptEntity> conceptEntityList) throws GnossAPIArgumentException, IOException {
		// TODO Auto-generated method stub
		if(conceptEntityList!=null) {
			for(ConceptEntity ec : conceptEntityList) {
				writeConceptEntityFirstDescription(ec);
			}
		}
		
	}
	private void writeConceptEntityFirstDescription(ConceptEntity conceptEntity) throws GnossAPIArgumentException, IOException {
		// TODO Auto-generated method stub
		if(!conceptEntity.hasRdfTypeDefined()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfType");
		}
		else if(!conceptEntity.hasAnyPropertyWithData()) {
			throw new GnossAPIArgumentException("Required. It can't be null or empty, RdfsLabel");
		}
		else {
			if(conceptEntity.hasAnyPropertyWithData()) {
				if(conceptEntity.getParentIdentifier()==null) {
					write(SKOS_MEMBER_LABEL, conceptEntity.getConceptEntityGnossId());
				}
				else {
					write(SKOS_NARROWER_LABEL, conceptEntity.getConceptEntityGnossId());
				}
			}
		}
		
	}
	
	private void writePropertyList(List<OntologyProperty> propertyList, UUID resourceId) throws IOException {
		if (resourceId== null) {
			if(propertyList!=null ) {
				for(OntologyProperty prop: propertyList) {
					if(prop.getName()!= null || !prop.getName().isEmpty() && prop.getValue()!=null && !prop.getName().equals(DataTypes.OntologyPropertyImage.toString())) {
						write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
					}
				}
			}
		}
	}
	
	public byte[] get_rdfFile() throws IOException, GnossAPIException {
		this._rdfFile=generateRDF();
		return _rdfFile;
	}
	
	public void set_rdfFile(byte[] _rdfFile) {
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
			if(Reader != null) {
				Reader.close();	
			}
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
