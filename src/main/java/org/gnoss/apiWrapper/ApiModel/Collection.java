package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.Map;

/**
 * Represents a Collection according to the Ontology
 * @author GNOSS
 */
public class Collection {
    
    private String subject;
    private List<Concept> member;
    private Map<String, String> scopeNote;
    
    /**
     * Subject of the Collection
     * @return subject of the collection
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * Subject of the Collection
     * @param subject Subject of the collection
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * The Subjects of the members
     * @return list of member concepts
     */
    public List<Concept> getMember() {
        return member;
    }
    
    /**
     * The Subjects of the members
     * @param member List of member concepts
     */
    public void setMember(List<Concept> member) {
        this.member = member;
    }
    
    /**
     * Name to represents the Collection. The key is the language (es, en, de, eu, ...) and the value is the Name in the indicated language.
     * @return scope note map
     */
    public Map<String, String> getScopeNote() {
        return scopeNote;
    }
    
    /**
     * Name to represents the Collection. The key is the language (es, en, de, eu, ...) and the value is the Name in the indicated language.
     * @param scopeNote Scope note map
     */
    public void setScopeNote(Map<String, String> scopeNote) {
        this.scopeNote = scopeNote;
    }
}