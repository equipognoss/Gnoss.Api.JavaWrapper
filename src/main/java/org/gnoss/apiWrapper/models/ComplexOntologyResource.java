/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.ApiModel.AttachedResourceFilePropertyTypes;
import org.gnoss.apiWrapper.ApiModel.ResourceVisibility;
import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author jruiz
 */
public class ComplexOntologyResource extends BaseResource {
    //Members
    private String _rdfFile;
    private String _stringRdfFile;
    private Ontology _ontology;
    
    //Properties
    private String GnossId;
    private String RdFile;
    private String StringRdfFile;
    private String PublisherEmail;
    private String MainImage;
    private UUID ShortGnossId;
    public String Author;
    
    //Attached files
    private ArrayList<String> AttachedFilesName;
    private ArrayList<AttachedResourceFilePropertyTypes> AttachedFilesType;
    public ArrayList<byte[]> AttachedFiles;
    
    //Screenshots
    private boolean MustGenerateScreenshot;
    private String ScreenshotUrl;
    private String ScreenshotPredicate;
    private int[] ScreenshotSizes;
    
    
    public ComplexOntologyResource(String largeGnossId) throws GnossAPIArgumentException{
    	Initialize();
    	if(largeGnossId != null){
    		GnossId = largeGnossId;
    	}
    	else{
    		throw new GnossAPIArgumentException("Required. LargeGnossId can't be null or empty");
    	}
    }
    
    public ComplexOntologyResource(UUID shortGnossId){
    	Initialize();
    	ShortGnossId = shortGnossId;
    }
    
    
    public ComplexOntologyResource(){
    	Initialize();
    }
    
    private void Initialize(){
    	Author = null;
    	MustGenerateScreenshot = false;
    	ScreenshotUrl = "";
    	ScreenshotSizes = null;
    	ScreenshotPredicate = "";
    	AttachedFilesName = new ArrayList<String>();
    	AttachedFilesType = new ArrayList<AttachedResourceFilePropertyTypes>();
    	AttachedFiles = new ArrayList<byte[]>();
    	_ontology = null;
    	setAutomaticTagsTextFromTitle("");
    	setAutomaticTagsTextFromDescription("");
    	setDescription("");
    	setPublishInHome(false);
    	setUploaded(false);
    	setPublisherEmail("");
    	setVisibility(ResourceVisibility.open);
    }
    
    @Override
    public String getGnossId() {       
        return super.getGnossId();
    }

    @Override
    public void setGnossId(String GnossId) throws GnossAPIArgumentException {
        super.setGnossId(GnossId);
    }

    public String getRdFile() throws IOException, GnossAPIException {
        if(_rdfFile == null || _rdfFile.length() == 0){
            _rdfFile = this._ontology.GenerateRDF();
        }
        return _rdfFile;
    }

    public void setRdFile(String RdFile) {
        this._rdfFile = RdFile;
    }

    public boolean getMustGenerateScreenshot(){
    	return MustGenerateScreenshot;
    }
    
    public String getScreenshotUrl(){
    	return ScreenshotUrl;
    }
    
    public String getScreenshotPredicate(){
    	return ScreenshotPredicate;
    }
    
    public int[] getScreenshotSizes(){
    	return ScreenshotSizes;
    }
    
    public String getStringRdfFile() throws IOException, GnossAPIException {
        if(_rdfFile == null || _rdfFile.length() == 0){
            _rdfFile = this._ontology.GenerateRDF();
        }
         return new String(_rdfFile);
    }

    public String getPublisherEmail() {
        return PublisherEmail;
    }

    public ArrayList<byte[]> getAttachedFiles(){    	
    	return AttachedFiles;
    }
    
    public ArrayList<AttachedResourceFilePropertyTypes> getAttachedFilesType(){
    	return AttachedFilesType;
    }
    
    public ArrayList<String> getAttachedFilesName(){
    	return AttachedFilesName;    	
    }
    
    public void setPublisherEmail(String PublisherEmail) {
        this.PublisherEmail = PublisherEmail;
    }

    public String getMainImage() {
        return MainImage;
    }

    public void setMainImage(String MainImage) {
        this.MainImage = MainImage;
    }

    public Ontology getOntology() {
        return _ontology;
    }
    
    public UUID getShortGnossId(){
    	return ShortGnossId;
    }
    
    public void setShortGnossId(UUID shortGnossId){
    	ShortGnossId = shortGnossId;
    }

    public void setOntology(Ontology Ontology) throws IOException, GnossAPIException {
        this._ontology = Ontology;
        _rdfFile = _ontology.GenerateRDF();
        ShortGnossId = _ontology.getResourceId();
        setGnossId(_ontology.getIdentifier());
    }
}
