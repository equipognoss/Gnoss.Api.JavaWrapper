/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import org.gnoss.apiWrapper.ApiModel.ReaderEditor;
import org.gnoss.apiWrapper.ApiModel.ResourceVisibility;

/**
 *
 * @author jruiz
 */
public abstract class BaseResource {
    
	//Members
	private String _gnossId;
    private String _title;
    private String _description;
    private String[] _tags;
    private String _automaticTagsTextFromTitle;
    private String _automaticTagsTextFromDescription;
    private Date _creationDate;
    
    private ArrayList<Multilanguage> _multilanguageTitle;
    private ArrayList<Multilanguage> _multilanguageDescription;
	
	
	private String id;
    private boolean modified; 
    private boolean deleted;
    private UUID shortGnossId;
    private ArrayList<String> textCategories;
    private ArrayList<UUID> categoriesIds;
    private String author;
    private ArrayList<ReaderEditor> readersGroups;
    private ArrayList<ReaderEditor> editorsGroups;
    private boolean PublishInHome;
    private boolean canonicalUrl;
    private boolean uploaded;
    private boolean CreatorIsAuthor;
    private ResourceVisibility Visibility;
    
    public boolean getPublishInHome() {
        return PublishInHome;
    }
    
    public ResourceVisibility getVisibility(){
    	return Visibility;
    }
    
    public void setVisibility(ResourceVisibility visibility){
    	Visibility = visibility;
    }
   
    public boolean getCreatorIsAuthor(){
    	return CreatorIsAuthor;
    }
    
    public void setCreatorIsAuthor(boolean creatorIsAuthor){
    	CreatorIsAuthor = creatorIsAuthor;
    }
   
    public void setPublishInHome(boolean publishInHome) {
        this.PublishInHome = publishInHome;
    }

    public boolean isCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(boolean canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
    
    
    public ArrayList<String> getTextCategories() {
        return textCategories;
    }

    public void setTextCategories(ArrayList<String> textCategories) {
        this.textCategories = textCategories;
    }

    public List<UUID> getCategoriesIds() {
        return categoriesIds;
    }

    public void setCategoriesIds(ArrayList<UUID> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<ReaderEditor> getReadersGroups() {
        return readersGroups;
    }

    public void setReadersGroups(ArrayList<ReaderEditor> readersGroups) {
        this.readersGroups = readersGroups;
    }

    public ArrayList<ReaderEditor> getEditorsGroups() {
        return editorsGroups;
    }

    public void setEditorsGroups(ArrayList<ReaderEditor> editorsGroups) {
        this.editorsGroups = editorsGroups;
    }
    
    public UUID getShortGnossId() {
        return shortGnossId;
    }

    public void setShortGnossId(UUID shortGnossId) {
        this.shortGnossId = shortGnossId;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getGnossId() {
        return _gnossId;
    }

    public void setGnossId(String gnossId) throws GnossAPIArgumentException {
        this._gnossId = gnossId;
        try
        {
            if (_gnossId != null && _gnossId.contains("_"))
            {
                String[] result = _gnossId.split("_");
                String g = result[2];
                shortGnossId = UUID.fromString(result[result.length - 2]);
            }
            else
            {
                if (_gnossId != null)
                {
                    String var = _gnossId.replace("http://gnoss/", "");
                    shortGnossId = UUID.fromString(var);
                }
                else
                {
                    shortGnossId = UUID.fromString("00000000-0000-0000-0000-000000000000");
                }
            }


        }
        catch (Exception ex)
        {
            throw new GnossAPIArgumentException("The GnossId is not valid");
        }
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
        if (this._title != null && !this._title.isEmpty() && !StringUtils.isBlank(title) && this._title.contains("\0"))
        {
            this._title = this._title.replace("\0", "");
        }
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
        
        if (this._description!= null && !StringUtils.isBlank(description) && !this._description.isEmpty()&&  this._description.contains("\0"))
        {
           this._description = this._description.replace("\0", "");
        }
    }

    public String[] getTags() {
        return _tags;
    }

    public void setTags(String[] tags) {
        if (tags != null)
        {
            for(int i = 0; i<tags.length; i++)
            {
                tags[i] = tags[i].toLowerCase();
            }
            this._tags = tags;
        }
    }

    public String getAutomaticTagsTextFromTitle() {
        return _automaticTagsTextFromTitle;
    }

    public void setAutomaticTagsTextFromTitle(String automaticTagsTextFromTitle) {
    	_automaticTagsTextFromTitle = automaticTagsTextFromTitle;
    	if(!StringUtils.isEmpty(_automaticTagsTextFromTitle) && _automaticTagsTextFromTitle.contains("\0")){
    		_automaticTagsTextFromTitle = _automaticTagsTextFromTitle.replace("\0", "");
    	}
    }

    public String getAutomaticTagsTextFromDescription() {
        return _automaticTagsTextFromDescription;
    }

    public void setAutomaticTagsTextFromDescription(String automaticTagsTextFromDescription) {
        this._automaticTagsTextFromDescription = automaticTagsTextFromDescription;
        if(!StringUtils.isEmpty(_automaticTagsTextFromDescription) && !StringUtils.contains("\0", "")){
        	_automaticTagsTextFromDescription = _automaticTagsTextFromDescription.replace("\0", "");
        }
    }

    public Date getCreationDate() {
        if(this._creationDate == new Date(0))
        {
            this._creationDate = new Date();
        }

        return this._creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this._creationDate = creationDate;
    }

    public List<Multilanguage> getMultilanguageTitle() {
        return _multilanguageTitle;
    }

    public void setMultilanguageTitle(ArrayList<Multilanguage> multilanguageTitle) {
        this._multilanguageTitle = multilanguageTitle;
        
        String title = "";
        for(Multilanguage languageTitle : this._multilanguageTitle)
        {
            if (languageTitle.getString()!=null && !languageTitle.getString().isEmpty() )
            {
                title += languageTitle.getString()+"@"+languageTitle.getLanguage()+"|||";
            }
            else
            {
                this._title = "";
            }
        }
        if (title != "")
        {
            this._title = title;
        }
    }

    public List<Multilanguage> getMultilanguageDescription() {
        return _multilanguageDescription;
    }

    public void setMultilanguageDescription(ArrayList<Multilanguage> multilanguageDescription) {
        this._multilanguageDescription = multilanguageDescription;
        
        String description = "";
        for (Multilanguage languageDescription : this._multilanguageDescription)
        {
            if ( languageDescription.getString()!= null && !languageDescription.getString().isEmpty())
            {
                description += languageDescription.getString()+"@"+languageDescription.getLanguage()+"|||";
            }
            else
            {
                this._description = "";
            }
        }
        if (!description.isEmpty())
        {
            this._description = description;
        }
    }
    
    protected byte[] ReadFile(String downloadUrl) throws GnossAPIException, IOException
        {
            byte[] file = null;
            if (downloadUrl != null && !downloadUrl.isEmpty())
            {
                boolean isUri = true;
                URI uri = null;
                URL url = null;
                try{
                    url = new URL(downloadUrl);
                    uri = url.toURI();
                }
                catch(Exception ex){
                    isUri = false;
                }
                if (isUri)
                {        
                    try (InputStream inputStream = url.openStream()) {
                        file = IOUtils.toByteArray(inputStream);
                    }
                    catch(IOException ex){
                        throw new IOException(ex);
                    }
                }
                else
                {
                    File doc = new File(downloadUrl);
                    if (doc.exists())
                    {
                        FileInputStream fs = new FileInputStream(downloadUrl);
                        file = IOUtils.toByteArray(fs);
                        fs.close();
                    }
                    else
                    {
                        throw new GnossAPIException("The file {downloadUrl} doesn't exist or isn't accessible");
                    }
                }
            }
            else
            {
                throw new GnossAPIArgumentException("downloadUrl can't be null or empty");
            }
            return file;
        }
    
    
}
