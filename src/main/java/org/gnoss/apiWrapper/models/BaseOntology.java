/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.Constants;
import org.gnoss.apiWrapper.Helpers.DataTypes;
import org.gnoss.apiWrapper.Helpers.GnossHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jruiz
 */
public abstract class BaseOntology {

    private String name;
    private String _rdfType;
    private String _rdfsLabel;
    private ArrayList<OntologyProperty> _properties;
    private String OntologyUrl;

    private ArrayList<String> PrefixList;
    private String identifier;
    private ArrayList<OntologyEntity> Entities;
    private String GraphsUrl;    
    private StringBuilder StringBuilder;

    public BaseOntology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList, String identifier, ArrayList<OntologyEntity> entityList) throws GnossAPIArgumentException {
        if (StringUtils.isEmpty(rdfType.trim())) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        } else if (StringUtils.isEmpty(rdfsLabel)) {
            throw new GnossAPIArgumentException("Rquired. RdfsLabel can't be null or empty");
        } else {
            this._rdfType = rdfType;
            this._rdfsLabel = rdfsLabel;
            this._properties = propertyList;
            this.OntologyUrl = ontologyUrl;
            this.PrefixList = prefixList;
            this.identifier = identifier;
            this.Entities = entityList;
            this.GraphsUrl = graphsUrl;
        }
    }

    public BaseOntology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList, String identifier) throws GnossAPIArgumentException {
        if (StringUtils.isEmpty(rdfType.trim())) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        } else if (StringUtils.isEmpty(rdfsLabel)) {
            throw new GnossAPIArgumentException("Rquired. RdfsLabel can't be null or empty");
        } else {
            this._rdfType = rdfType;
            this._rdfsLabel = rdfsLabel;
            this._properties = propertyList;
            this.OntologyUrl = ontologyUrl;
            this.PrefixList = prefixList;
            this.identifier = identifier;
            this.Entities = null;
            this.GraphsUrl = graphsUrl;
        }
    }
    
    public BaseOntology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList, ArrayList<OntologyEntity> entityList) throws GnossAPIArgumentException{
    	if(StringUtils.isEmpty(rdfType)){
    		throw new GnossAPIArgumentException("RdfType can't be null");
    	}
    	else if(StringUtils.isEmpty(rdfsLabel)){
    		throw new GnossAPIArgumentException("RdfsLabel can't be null");
    	}
    	else{
    		GraphsUrl = graphsUrl;
    		OntologyUrl = ontologyUrl;
    		this._rdfType = rdfType;
    		this._rdfsLabel = rdfsLabel;
    		PrefixList = prefixList;
    		this._properties = propertyList;
    		Entities = entityList;
    	}
    }
    
    public BaseOntology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList) throws GnossAPIArgumentException{
    	if(StringUtils.isEmpty(rdfType)){
    		throw new GnossAPIArgumentException("RdfType can't be null");
    	}
    	else if(StringUtils.isEmpty(rdfsLabel)){
    		throw new GnossAPIArgumentException("RdfsLabel can't be null");
    	}
    	else{
    		GraphsUrl = graphsUrl;
    		OntologyUrl = ontologyUrl;
    		this._rdfType = rdfType;
    		this._rdfsLabel = rdfsLabel;
    		PrefixList = prefixList;
    		this._properties = propertyList;
    		Entities = null;
    	}
    }

    protected BaseOntology(){
    	
    }
    
    protected HashMap<UUID, OntologyEntity> EntityListToEntityDictionary(ArrayList<OntologyEntity> entityList) {
        HashMap<UUID, OntologyEntity> entityDictionary = null;
        if (entityList == null) {
            if (Entities != null) {
                for (OntologyEntity entity : Entities) {
                    if (entityDictionary == null) {
                        entityDictionary = new HashMap<>();
                    }

                    if (!entityDictionary.containsKey(entity.getGuidEntidad())) {
                        entityDictionary.put(entity.getGuidEntidad(), entity);
                    }
                }
            }
        } else {
            for (OntologyEntity entity : entityList) {
                if (entityDictionary == null) {
                    entityDictionary = new HashMap<>();
                }

                if (!entityDictionary.containsKey(entity.getGuidEntidad())) {
                    entityDictionary.put(entity.getGuidEntidad(), entity);
                }
            }
        }
        return entityDictionary;
    }

    protected HashMap<UUID, OntologyEntity> EntityListToEntityDictionary() {
        return EntityListToEntityDictionary(null);
    }

    protected void Write(String label, String value, String languageAttribute) throws IOException{
        if(!StringUtils.isEmpty(value)){
            if(value.endsWith(("\0"))){
                value = value.replace("\0", "");
            }
            if(value.contains("\r\n")){
                value = value.replace("\r\n", "<br />");
            }
            if(value.contains("\n")){
                value = value.replace("\n", "<br />");
            }
            if(!StringUtils.isEmpty(value)){
                if(value.contains("&") || value.contains("<") || value.contains(">")){
                    if(languageAttribute == null){
                        String linea = "<" + label + "><![CDATA[" + value + "]]></" + label + ">\n";
                        StringBuilder.append(linea);
                    }
                    else{
                        String linea = "<" + label + " xml:lang=\"" + languageAttribute + "\"><![CDATA[" + value + "]]></" + label + ">\n";
                        StringBuilder.append(linea);
                    }
                }
                else{
                    if(languageAttribute == null){
                        String linea = "<" + label + ">" + value + "</" + label + ">\n";
                        StringBuilder.append(linea);
                    }
                    else{
                        String linea = "<" + label + "xml:lang=\"" + languageAttribute + "\">" + value + "</" + label + ">\n";
                        StringBuilder.append(linea);
                    }
                }
            }
        }
    }
    
    protected void Write(String label, String value) throws IOException{
        Write(label, value, null);
    }
    
    protected void WriteRdfHeader() throws IOException {
        String txt = "";

        txt += "<rdf:RDF ";
        txt += "xmlns:gnossonto=\"" + OntologyUrl + "\"";
        for (String ontologia : PrefixList) {
            txt += " " + ontologia;
        }
        txt += ">";
        StringBuilder.append(txt);
    }

    protected void WriteEntityAdditionalDescription(HashMap<UUID, OntologyEntity> entityDictionary, UUID resourceId) throws IOException, GnossAPIException{
        if(entityDictionary != null){
            for(UUID id : entityDictionary.keySet()){
                if((entityDictionary.get(id).HasAnyPropertyWithData()) || (entityDictionary.get(id).getEntities() != null && entityDictionary.get(id).getEntities().size() > 0) && entityDictionary.get(id).HasRDFTypeAndRDFLabelDefined()){
                    String line = "<rdf:Description rdf:about=\"" + GraphsUrl + "items/" + entityDictionary.get(id).getItems() + "_" + resourceId + "_" + id + "\">";
                    StringBuilder.append(line);
                    if(StringUtils.isEmpty(entityDictionary.get(id).getRdfsLabel()) || StringUtils.isEmpty(entityDictionary.get(id).getRdfType())){
                        throw new GnossAPIException("rdfType and rdfLabel are required, they can't be null or empty");
                    }
                    else{
                        Write("rdf:type", entityDictionary.get(id).getRdfType());
                        Write("rdfs:label", entityDictionary.get(id).getRdfsLabel());
                        
                        if(entityDictionary.get(id).getProperties() != null){
                            for(OntologyProperty prop : entityDictionary.get(id).getProperties()){
                                if(prop.getClass().equals(DataTypes.OntologyPropertyImage)){
                                    if(prop.getValue().toString().contains(Constants.IMAGES_PATH_ROOT)){
                                        String value = prop.getValue().toString().substring(prop.getValue().toString().lastIndexOf("/") + "/".length());
                                        prop.setValue(value);
                                    }
                                    prop.setValue(GnossHelper.GetImagePath(resourceId, prop.getValue().toString()));
                                }
                                if(prop.getValue()!= null) {
                                	// Write(prop.getName(), prop.getValue().toString(), prop.getLanguage());
                                	if(prop instanceof ListStringOntologyProperty) {
                                        ArrayList<String> listaValores = (ArrayList<String>)prop.getValue();
                                        for(String valor : listaValores) {
                                            Write(prop.getName(), valor, prop.getLanguage());
                                        }
                                    }
                                    else {
                                        Write(prop.getName(), prop.getValue().toString(), prop.getLanguage());   
                                    }
                                }                               
                            }
                            if(entityDictionary.get(id).getEntities() == null || entityDictionary.get(id).getEntities().size() == 0){
                            	StringBuilder.append("</rdf:Description>\n");
                            }
                        }
                        if(entityDictionary.get(id).getEntities() != null && entityDictionary.get(id).getEntities().size() > 0){
                            HashMap<UUID, OntologyEntity> dicSubEnt = EntityListToEntityDictionary(entityDictionary.get(id).getEntities());
                            WriteEntityFirstDescription(dicSubEnt, resourceId, true);
                            WriteEntityAdditionalDescription(dicSubEnt, resourceId);
                        }
                    }
                }
            }
        }
    }
    
    protected void WriteEntityFirstDescription(HashMap<UUID, OntologyEntity> entityDictionary, UUID resourceId, boolean includeCloseDescription) throws IOException{
        if(entityDictionary != null){
            for(UUID id : entityDictionary.keySet()){
                if((entityDictionary.get(id).HasAnyPropertyWithData() || (entityDictionary.get(id).getEntities() != null && entityDictionary.get(id).getEntities().size() > 0)) && entityDictionary.get(id).HasRDFTypeAndRDFLabelDefined()){
                    String line = GraphsUrl + "items/" + entityDictionary.get(id).getItems() + "_" + resourceId + "_" + id;
                    Write(entityDictionary.get(id).getLabel(), line);
                }
            }
            if (includeCloseDescription)
            {
            	StringBuilder.append("</rdf:Description>");
            }
        }
       
    }
    
    protected void WriteEntityFirstDescription(HashMap<UUID, OntologyEntity> entityDictionary, UUID resourceId) throws IOException{
        WriteEntityFirstDescription(entityDictionary, resourceId, false);
    }
    
    protected String GenerateRDF() throws GnossAPIArgumentException, IOException, GnossAPIException{
        return null;
    }

    protected void WritePropertyList(ArrayList<OntologyProperty> propertyList, UUID resourceId)throws IOException{ }
    
    public String getOntologyUrl() {
        return OntologyUrl;
    }

    public void setOntologyUrl(String ontologyUrl) {
        this.OntologyUrl = ontologyUrl;
    }

    public ArrayList<String> getPrefixList() {
        return PrefixList;
    }

    public void setPrefixList(ArrayList<String> prefixList) {
        this.PrefixList = prefixList;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ArrayList<OntologyEntity> getEntities() {
        return Entities;
    }

    public void setEntities(ArrayList<OntologyEntity> entities) {
        this.Entities = entities;
    }

    public String getGrahpsUrl() {
        return GraphsUrl;
    }

    public void setGrahpsUrl(String grahpsUrl) {
        this.GraphsUrl = grahpsUrl;
    }

    public String getName() {
        if (this.name == null || this.name.isEmpty() || StringUtils.isBlank(name)) {
            this.name = OntologyUrl.substring(OntologyUrl.lastIndexOf('/') + 1);
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRdfType() {
        return _rdfType;
    }

    public void setRdfType(String rdfType) {
        this._rdfType = rdfType;
    }

    public String getRdfsLabel() {
        return _rdfsLabel;
    }

    public void setRdfsLabel(String rdfsLabel) {
        this._rdfsLabel = rdfsLabel;
    }

    public ArrayList<OntologyProperty> getProperties() {
        if (_properties != null) {
            return new ArrayList(new HashSet(_properties));
        }
        return _properties;
    }

    public void setProperties(ArrayList<OntologyProperty> properties) {
        this._properties = properties;
    }

    public StringBuilder getStringBuilder() {
        return StringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.StringBuilder = stringBuilder;
    }  
}