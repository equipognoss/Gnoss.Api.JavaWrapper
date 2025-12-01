package org.gnoss.apiWrapper.ApiModel;

/**
 * Model with the parameters to add a new Concept to a Thesaurus
 * @author GNOSS
 */
public class ConceptToAddModel {
    
    private Concept concept;
    private String source;
    private String ontology;
    private String parentCategorySubject;
    private String communityShortName;
    
    /**
     * Represents a Concept according to the Ontology
     * @return concept to add
     */
    public Concept getConcept() {
        return concept;
    }
    
    /**
     * Represents a Concept according to the Ontology
     * @param concept Concept to add
     */
    public void setConcept(Concept concept) {
        this.concept = concept;
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
     * Subject parent of the category to load
     * @return parent category subject
     */
    public String getParentCategorySubject() {
        return parentCategorySubject;
    }
    
    /**
     * Subject parent of the category to load
     * @param parentCategorySubject Parent category subject
     */
    public void setParentCategorySubject(String parentCategorySubject) {
        this.parentCategorySubject = parentCategorySubject;
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