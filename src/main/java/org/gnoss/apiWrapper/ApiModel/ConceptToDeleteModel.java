package org.gnoss.apiWrapper.ApiModel;

/**
 * Model with the parameters to delete a Concept from a Thesaurus
 * @author GNOSS
 */
public class ConceptToDeleteModel {
    
    private String conceptSubject;
    private String ontology;
    private String communityShortName;
    
    /**
     * Subject of the Concept to delete
     * @return concept subject
     */
    public String getConceptSubject() {
        return conceptSubject;
    }
    
    /**
     * Subject of the Concept to delete
     * @param conceptSubject Concept subject
     */
    public void setConceptSubject(String conceptSubject) {
        this.conceptSubject = conceptSubject;
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
     * Short name of the community
     * @return community short name
     */
    public String getCommunityShortName() {
        return communityShortName;
    }
    
    /**
     * Short name of the community
     * @param communityShortName Community short name
     */
    public void setCommunityShortName(String communityShortName) {
        this.communityShortName = communityShortName;
    }
}