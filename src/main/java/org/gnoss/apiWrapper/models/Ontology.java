/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.DataTypes;
import org.gnoss.apiWrapper.Helpers.Constants;
import org.gnoss.apiWrapper.Helpers.GnossHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

/**
 *
 * @author jruiz
 */
public class Ontology extends BaseOntology {

    //Members
    private String _items;

    //Properties
    private UUID ResourceId;
    private UUID ArticleId;

    //Constructor
    public Ontology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList, ArrayList<OntologyEntity> entityList) throws GnossAPIArgumentException {
        super(graphsUrl, ontologyUrl, rdfType, rdfsLabel, prefixList, propertyList, entityList);
        ResourceId = UUID.randomUUID();
        ArticleId = UUID.randomUUID();

        String RdfType = super.getRdfType();

        if (RdfType != null && RdfType.contains("#")) {
            _items = RdfType.substring(RdfType.lastIndexOf("#") + 1);
        } else if (RdfType != null && RdfType.contains("/")) {
            _items = RdfType.substring(RdfType.lastIndexOf("/") + 1);
        } else {
            _items = RdfType;
        }

        super.setIdentifier(super.getGrahpsUrl() + "items/" + _items + "_" + ResourceId + "_" + ArticleId);
    }

    public Ontology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList) throws GnossAPIArgumentException {
        super(graphsUrl, ontologyUrl, rdfType, rdfsLabel, prefixList, propertyList);

        ResourceId = UUID.randomUUID();
        ArticleId = UUID.randomUUID();

        String RdfType = super.getRdfType();

        if (RdfType != null && RdfType.contains("#")) {
            _items = RdfType.substring(RdfType.lastIndexOf("#") + 1);
        } else if (RdfType != null && RdfType.contains("/")) {
            _items = RdfType.substring(RdfType.lastIndexOf("/") + 1);
        } else {
            _items = RdfType;
        }

        super.setIdentifier(super.getGrahpsUrl() + "items/" + _items + "_" + ResourceId + "_" + ArticleId);
    }

    /**
     * Constructor of Ontology
     *
     * @param graphsUrl Graphs url of the enviroment.
     * @param ontologyUrl Download url of the ontology
     * @param rdfType The rdf type property of the ontology
     * @param rdfsLabel The rdfs label of the ontology
     * @param prefixList Prefix list declared in the ontology
     * @param propertyList Property list of the ontology
     * @param entityList List of auxiliar entities
     * @param resourceId First part of the resource identifier
     * @param articleId Second part of the resource identifier
     * @throws GnossAPIArgumentException GnossAPIArgumentException
     */
    public Ontology(String graphsUrl, String ontologyUrl, String rdfType, String rdfsLabel, ArrayList<String> prefixList, ArrayList<OntologyProperty> propertyList, ArrayList<OntologyEntity> entityList, UUID resourceId, UUID articleId) throws GnossAPIArgumentException {
        super(graphsUrl, ontologyUrl, rdfType, rdfsLabel, prefixList, propertyList, entityList);
        if (!resourceId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
            ResourceId = resourceId;
        } else {
            ResourceId = UUID.randomUUID();
        }

        if (!articleId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
            ArticleId = articleId;
        } else {
            ArticleId = UUID.randomUUID();
        }

        String RdfType = super.getRdfType();
        if (RdfType != null && RdfType.contains("#")) {
            _items = RdfType.substring(RdfType.lastIndexOf("#") + 1);
        } else if (RdfType != null && RdfType.contains("/")) {
            _items = RdfType.substring(RdfType.lastIndexOf("/") + 1);
        } else {
            _items = RdfType;
        }

        super.setIdentifier(super.getGrahpsUrl() + "items/" + _items + "_" + ResourceId + "_" + ArticleId);
    }

    
    
    @Override
    public String GenerateRDF() throws IOException, GnossAPIException {
        StringBuilder stringBuilder = new StringBuilder();
        super.setStringBuilder(stringBuilder);
 
        try {
            WriteRdfHeader();
        } catch (IOException ex) {
            Logger.getLogger(Ontology.class.getName()).log(Level.SEVERE, null, ex);
        }

        HashMap<UUID, OntologyEntity> entitiesDictionary = EntityListToEntityDictionary();
        byte[] rdfFile = null;

        if (StringUtils.isEmpty(getRdfType())) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        }
        else if(StringUtils.isEmpty(getRdfsLabel())){
            throw new GnossAPIArgumentException("Required. RdfsLabel can't be null or empty");
        }
        else{
            String linea = "<rdf:Description rdf:about=\"" + super.getIdentifier() + "\">\n";
            stringBuilder.append(linea);
            Write("rdf:type", getRdfType());
            Write("rdfs:label", getRdfsLabel());
            WritePropertyList(getProperties(), ResourceId);
            if(entitiesDictionary == null || entitiesDictionary.size() == 0){
            	stringBuilder.append("</rdf:Description>\n");
            }
            WriteEntityFirstDescription(entitiesDictionary, ResourceId, true);

            WriteEntityAdditionalDescription(entitiesDictionary, ResourceId);
            stringBuilder.append("</rdf:RDF>");
            
            rdfFile = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
            
            byte[] base64Encoded = Base64.encodeBase64(rdfFile);
            
            String rdfDecoded = new String(base64Encoded, StandardCharsets.UTF_8);
            
            return rdfDecoded;
            //return rdfFile;
        }
    }

    @Override
    public void WritePropertyList(ArrayList<OntologyProperty> propertyList, UUID resourceId)throws IOException{
        if(propertyList != null){
            for(OntologyProperty prop : propertyList){
                if(!StringUtils.isEmpty(prop.getName()) && prop.getValue() != null){
                    if(prop.getClass().equals(DataTypes.OntologyPropertyImage)){
                        if(prop.getValue().toString().contains(Constants.IMAGES_PATH_ROOT)){
                            String value = prop.getValue().toString().substring(prop.getValue().toString().lastIndexOf("/") + "/".length());
                            prop.setValue(value);
                        }
                        prop.setValue(GnossHelper.GetImagePath(resourceId, prop.getValue().toString()));
                    }
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
        }
    }
    
    //Internal methods
    //Getters and Setters
    public UUID getResourceId() {
        return ResourceId;
    }

    public void setResourceId(UUID ResourceId) {
        this.ResourceId = ResourceId;
    }

    public UUID getArticleId() {
        return ArticleId;
    }

    public void setArticleId(UUID ArticleId) {
        this.ArticleId = ArticleId;
    }
}
