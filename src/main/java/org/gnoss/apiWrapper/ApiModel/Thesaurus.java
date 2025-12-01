package org.gnoss.apiWrapper.ApiModel;

/**
 * Represents a Thesaurus according to the Ontology
 * @author GNOSS
 */
public class Thesaurus {
    
    private Collection collection;
    private String communityShortName;
    private String ontology;
    private String source;
    
    /**
     * Collections of the Thesaurus
     * @return collection of the thesaurus
     */
    public Collection getCollection() {
        return collection;
    }
    
    /**
     * Collections of the Thesaurus
     * @param collection Collection of the thesaurus
     */
    public void setCollection(Collection collection) {
        this.collection = collection;
    }
    
    /**
     * Short name of the community when the thesaurus will be loaded
     * @return community short name
     */
    public String getCommunityShortName() {
        return communityShortName;
    }
    
    /**
     * Short name of the community when the thesaurus will be loaded
     * @param communityShortName Community short name
     */
    public void setCommunityShortName(String communityShortName) {
        this.communityShortName = communityShortName;
    }
    
    /**
     * Name of the ontology
     * @return ontology name
     */
    public String getOntology() {
        return ontology;
    }
    
    /**
     * Name of the ontology
     * @param ontology Ontology name
     */
    public void setOntology(String ontology) {
        this.ontology = ontology;
    }
    
    /**
     * Source of the Thesaurus to load
     * @return source of the thesaurus
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Source of the Thesaurus to load
     * @param source Source of the thesaurus
     */
    public void setSource(String source) {
        this.source = source;
    }
}