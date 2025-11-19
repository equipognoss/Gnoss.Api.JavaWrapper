package org.gnoss.apiWrapper.ApiModel;

import java.io.Serializable;

/**
 * Model to add a query to cache
 */
public class ConsultaCacheModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Where of the query
     */
    private String WhereSPARQL;
    
    /**
     * Where to facets of the query
     */
    private String WhereFacetasSPARQL;
    
    /**
     * Order by of the query
     */
    private String OrderBy;
    
    /**
     * If omit the rdfType or no in the query
     */
    private boolean OmitirRdfType;
    
    // Getters and Setters
    
    public String getWhereSPARQL() {
        return WhereSPARQL;
    }
    
    public void setWhereSPARQL(String whereSPARQL) {
        this.WhereSPARQL = whereSPARQL;
    }
    
    public String getWhereFacetasSPARQL() {
        return WhereFacetasSPARQL;
    }
    
    public void setWhereFacetasSPARQL(String whereFacetasSPARQL) {
        this.WhereFacetasSPARQL = whereFacetasSPARQL;
    }
    
    public String getOrderBy() {
        return OrderBy;
    }
    
    public void setOrderBy(String orderBy) {
        this.OrderBy = orderBy;
    }
    
    public boolean isOmitirRdfType() {
        return OmitirRdfType;
    }
    
    public void setOmitirRdfType(boolean omitirRdfType) {
        this.OmitirRdfType = omitirRdfType;
    }
}