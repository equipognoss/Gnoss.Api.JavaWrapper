package org.gnoss.apiWrapper.ApiModel;

/**
 * Model with the params to delete the indicated thesaurus
 * @author GNOSS
 */
public class ThesaurusToDeleteModel {
    
    private String communityShortName;
    private String ontology;
    private String source;
    
    /**
     * Short name of the community when the thesaurus will be deleted (Required)
     * @return community short name
     */
    public String getCommunityShortName() {
        return communityShortName;
    }
    
    /**
     * Short name of the community when the thesaurus will be deleted (Required)
     * @param communityShortName Community short name
     */
    public void setCommunityShortName(String communityShortName) {
        this.communityShortName = communityShortName;
    }
    
    /**
     * Name of the ontology (Required)
     * @return ontology name
     */
    public String getOntology() {
        return ontology;
    }
    
    /**
     * Name of the ontology (Required)
     * @param ontology Ontology name
     */
    public void setOntology(String ontology) {
        this.ontology = ontology;
    }
    
    /**
     * Source of the Thesaurus to delete (Required)
     * @return source of the thesaurus
     */
    public String getSource() {
        return source;
    }
    
    /**
     * Source of the Thesaurus to delete (Required)
     * @param source Source of the thesaurus
     */
    public void setSource(String source) {
        this.source = source;
    }
}