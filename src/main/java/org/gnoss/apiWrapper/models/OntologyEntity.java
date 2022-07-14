/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Helpers.DataTypes;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author salopez
 */
public class OntologyEntity {

    private String RdfType;
    private String RdfsLabel;
    public ArrayList<OntologyEntity> Entities;
    public String Label;
    private String Items;
    public UUID GuidEntidad;

    private String _name;
    private String _rdfsLabel;
    private ArrayList<OntologyProperty> _properties;

    public OntologyEntity(String rdfType, String rdfsLabel, String label) throws GnossAPIArgumentException {

        if (StringUtils.isEmpty(rdfType)) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        } else if (StringUtils.isEmpty(rdfsLabel)) {
            throw new GnossAPIArgumentException("Required. RdfsLabel can't be null or empty");
        } else {
            setRdfType(rdfType);
            setRdfsLabel( rdfsLabel);
            Label = label;
            _properties = new ArrayList<OntologyProperty>();
            Entities = new ArrayList<OntologyEntity>();
            GuidEntidad = UUID.randomUUID();
        }
    }

    public OntologyEntity(String rdfType, String rdfsLabel, String label, ArrayList<OntologyProperty> properties) throws GnossAPIArgumentException {

        if (StringUtils.isEmpty(rdfType)) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        } else if (StringUtils.isEmpty(rdfsLabel)) {
            throw new GnossAPIArgumentException("Required. RdfsLabel can't be null or empty");
        } else {
        	 setRdfType(rdfType);
            setRdfsLabel( rdfsLabel);
            Label = label;
            _properties = properties;
            Entities = new ArrayList<OntologyEntity>();
            GuidEntidad = UUID.randomUUID();
        }
    }

    public OntologyEntity(String rdfType, String rdfsLabel, String label, ArrayList<OntologyProperty> properties, ArrayList<OntologyEntity> entities) throws GnossAPIArgumentException {

        if (StringUtils.isEmpty(rdfType)) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        } else if (StringUtils.isEmpty(rdfsLabel)) {
            throw new GnossAPIArgumentException("Required. RdfsLabel can't be null or empty");
        } else {
        	setRdfType(rdfType);
        	setRdfsLabel( rdfsLabel);
            Label = label;
            _properties = properties;
            Entities = entities;
            GuidEntidad = UUID.randomUUID();
        }
    }

    
    
    public boolean HasAnyPropertyWithData() {
        
        boolean hasProperties = false;
        for (OntologyProperty prop : _properties) {
            if (prop.getClass().equals(DataTypes.OntologyPropertyString.getClass())) {
                if (prop.getValue() != null && !StringUtils.isEmpty(prop.getValue().toString()) && !StringUtils.isEmpty(prop.getValue().toString())) {
                    hasProperties = true;
                    break;
                }
            } else if (prop.getClass().equals(DataTypes.OntologyPropertyListString.getClass())) {
                if (prop.getValue() != null) {
                    for (String valor : (List<String>) prop.getValue()) {
                        if (!StringUtils.isEmpty(valor)) {
                            hasProperties = true;
                            break;
                        }
                    }
                }
            } else if (prop.getClass().equals(DataTypes.OntologyPropertyDate.getClass())) {
                if (prop.getValue() != null && !StringUtils.isEmpty(prop.getValue().toString()) && !StringUtils.isEmpty(prop.getValue().toString()) && !prop.getValue().toString().equals("00000000000000")) {
                    hasProperties = true;
                    break;
                }
            } else {
                hasProperties = true;
            }
        }
        return hasProperties;
    }

    public boolean HasRdfTypeDefined() {
        return !StringUtils.isEmpty(RdfType);
    }

    public boolean HasRdfsLabelDefined() {
        return !StringUtils.isEmpty(RdfType) && !StringUtils.isEmpty(RdfsLabel);
    }

    public boolean HasRDFTypeAndRDFLabelDefined()
    {
        return HasRdfsLabelDefined();
    }
    
    public String getRdfType() {
        return this.RdfType;
    }

    public void setRdfType(String rdfType) throws GnossAPIArgumentException {
        if (StringUtils.isEmpty(rdfType)) {
            throw new GnossAPIArgumentException("Required. RdfType can't be null or empty");
        } else {
            this.RdfType = rdfType;
            if (RdfType.contains("#")) {
                Items = RdfType.substring(RdfType.lastIndexOf("#") + 1);
            } else if (RdfType.contains("/")) {
                Items = RdfType.substring(RdfType.lastIndexOf("#") + 1);
            } else {
                Items = RdfType;
            }
        }
    }

    public String getRdfsLabel() {
        return _rdfsLabel;
    }

    public void setRdfsLabel(String rdfsLabel) throws GnossAPIArgumentException {
        if (StringUtils.isEmpty(rdfsLabel)) {
            throw new GnossAPIArgumentException("Required. RdfsLabel can't be null or empty");
        } else {
            _rdfsLabel = rdfsLabel;
            RdfsLabel = rdfsLabel;
        }
    }

    public String getItems() {
        return Items;
    }

    public void setItems(String Items) {
        this.Items = Items;
    }

    public List<OntologyProperty> getProperties() {
        return _properties;
    }

    public void setProperties(ArrayList<OntologyProperty> properties) {
        _properties = properties;
    }

    public ArrayList<OntologyEntity> getEntities() {
        return Entities;
    }

    public void setEntities(ArrayList<OntologyEntity> Entities) {
        this.Entities = Entities;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String Label) {
        this.Label = Label;
    }

    public UUID getGuidEntidad() {
        return GuidEntidad;
    }
}
