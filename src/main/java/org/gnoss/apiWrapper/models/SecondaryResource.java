package org.gnoss.apiWrapper.models;

import java.io.IOException;

import org.gnoss.apiWrapper.Excepciones.GnossAPIException;

public class SecondaryResource {
	//Members
	private String _rdfFile;
	private String _stringRdfFile;
	
	//Properties
	private String Id;
	private SecondaryOntology SecondaryOntology; 
	private boolean Uploaded;
	private boolean Modified;
	private boolean Deleted;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public SecondaryOntology getSecondaryOntology() {
		return SecondaryOntology;
	}
	public void setSecondaryOntology(SecondaryOntology secondaryOntology) {
		SecondaryOntology = secondaryOntology;
	}
	public boolean isUploaded() {
		return Uploaded;
	}
	public void setUploaded(boolean uploaded) {
		Uploaded = uploaded;
	}
	public boolean isModified() {
		return Modified;
	}
	public void setModified(boolean modified) {
		Modified = modified;
	}
	public boolean isDeleted() {
		return Deleted;
	}
	public void setDeleted(boolean deleted) {
		Deleted = deleted;
	}
	
	public String getRdfFile() throws IOException, GnossAPIException{
		_rdfFile = SecondaryOntology.GenerateRDF();		
		return _rdfFile;
	}
	
	public void setRdfFile(String rdfFile){
		_rdfFile = rdfFile;
	}
	
	public String getStringRdfFile() throws IOException, GnossAPIException{
		_rdfFile = SecondaryOntology.GenerateRDF();
		return _rdfFile;
	}
}
