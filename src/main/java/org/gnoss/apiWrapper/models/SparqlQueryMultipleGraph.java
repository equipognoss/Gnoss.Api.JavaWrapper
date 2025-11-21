package org.gnoss.apiWrapper.models;

import java.util.List;


/**
 * Model for SPARQL queries across multiple graphs
 */
public class SparqlQueryMultipleGraph {
    
    /**
     * Ontology name or community identificator to query. It will be used in the form clause
     */
    private List<String> ontology_list;
    
    /**
     * Community short name
     */
    private String community_short_name;
    
    /**
     * Select clause of the sparql query
     */
    private String query_select;
    
    /**
     * Where clause of the sparql query
     */
    private String query_where;
    
    /**
     * Use virtuoso master connection if true and the affinity connection if false
     */
    private boolean use_virtuoso_balancer = true;
    
    /**
     * Default constructor
     */
    public SparqlQueryMultipleGraph() {
    }
    
    /**
     * Constructor with parameters
     */
    public SparqlQueryMultipleGraph(List<String> ontology_list, String community_short_name, 
                                    String query_select, String query_where) {
        this.ontology_list = ontology_list;
        this.community_short_name = community_short_name;
        this.query_select = query_select;
        this.query_where = query_where;
    }
    
    // Getters and Setters
    
    public List<String> getOntology_list() {
        return ontology_list;
    }
    
    public void setOntology_list(List<String> ontology_list) {
        this.ontology_list = ontology_list;
    }
    
    public String getCommunity_short_name() {
        return community_short_name;
    }
    
    public void setCommunity_short_name(String community_short_name) {
        this.community_short_name = community_short_name;
    }
    
    public String getQuery_select() {
        return query_select;
    }
    
    public void setQuery_select(String query_select) {
        this.query_select = query_select;
    }
    
    public String getQuery_where() {
        return query_where;
    }
    
    public void setQuery_where(String query_where) {
        this.query_where = query_where;
    }
    
    public boolean isUse_virtuoso_balancer() {
        return use_virtuoso_balancer;
    }
    
    public void setUse_virtuoso_balancer(boolean use_virtuoso_balancer) {
        this.use_virtuoso_balancer = use_virtuoso_balancer;
    }
    
    @Override
    public String toString() {
        return "SparqlQueryMultipleGraph{" +
                "ontology_list=" + ontology_list +
                ", community_short_name='" + community_short_name + '\'' +
                ", query_select='" + query_select + '\'' +
                ", query_where='" + query_where + '\'' +
                ", use_virtuoso_balancer=" + use_virtuoso_balancer +
                '}';
    }
}