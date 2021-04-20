package org.gnoss.apiWrapper.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.LogHelper;

public class ConceptEntity  extends SecondaryEntity{

	
	
	private Map<String, String> _prefLabelDictionary;
	private String _rootIdentifier;
	private String _nameIdentifier;
	private String _parentNameIdentifier;
	private String _conceptEntityGnossId=null;
	private String _parentGnossId=null;
	
	private String graphsUrl;
	private int level;
	private String parentGnossId;
	private String ConceptEntityGnossId;
	private String parentIdentifier;
	private List<ConceptEntity> subEntities;
	private String identifier;

	protected ConceptEntity(String identifierNameRoot, String identifier, String dcSource, Map<String, String> prefLabelDictionary, List<ConceptEntity> subentities, String parentIdentifier, String GraphsUrl, LogHelper logHelper, int level) throws GnossAPIException {
		super();
		// TODO Auto-generated constructor stub
		Properties properties= new Properties();
		level=1;
		if(dcSource.isEmpty()|| dcSource==null || dcSource==" "|| prefLabelDictionary== null || identifierNameRoot==null ||identifierNameRoot==" " || identifier==null ||identifier==" " || identifier.isEmpty()  ) {
			logHelper.getInstance().Error("RequiredParameterConstructor", this.getClass().getName()); //DUDA
			throw new GnossAPIException ("RequiredParameteronstructor");
		}
		this._prefLabelDictionary=prefLabelDictionary;
		
		
		// rdf:type and rdfs:lbel are always the same 
		String RdfType="http://www.w3.org/2008/05/skos#Concept";
		String RdfsLabel="http://www.w3.org/2008/05/skos#Concept";
		_rootIdentifier= identifierNameRoot;
		_nameIdentifier=identifier.replace('.', '_');
		_parentNameIdentifier=parentIdentifier;
		this.level=level;
		this.subEntities=subentities;
		
		
		
		for(String idioma : _prefLabelDictionary.keySet()) {
			properties.setProperty(idioma, "skos:prefLabel");
		}
		
		properties.setProperty("dc:identifier", identifier);
		
		if(this._parentNameIdentifier!=null) {
			properties.setProperty("skos:broader", identifier);
		}
		
		properties.setProperty("dc:source", dcSource);
		properties.setProperty("skos:symbol", String.valueOf(level));
		
		this.graphsUrl=GraphsUrl;
		
	}

	public String getGraphsUrl() {
		return graphsUrl;
	}

	public void setGraphsUrl(String graphsUrl) {
		this.graphsUrl = graphsUrl;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getParentGnossId() {
		
		if(this._parentNameIdentifier!= null || !this._parentNameIdentifier.isEmpty() || this._parentNameIdentifier== "" ) {
			this._parentGnossId= this._rootIdentifier+"_"+this._parentNameIdentifier;
			this._parentGnossId= this.graphsUrl+" "+ this._parentGnossId;
		}
		else {
			this._parentGnossId= null;
		}
		return _parentGnossId;
	}

	public void setParentGnossId(String parentGnossId) {
		this.parentGnossId = parentGnossId;
	}

	public String getConceptEntityGnossId() {
		this._conceptEntityGnossId=this.graphsUrl+" "+ this.identifier;
		return _conceptEntityGnossId;
	}

	public void setConceptEntityGnossId(String conceptEntityGnossId) {
		ConceptEntityGnossId = conceptEntityGnossId;
	}

	public String getParentIdentifier() {
		return _parentNameIdentifier;
	}

	public void setParentIdentifier(String parentIdentifier) {
		this._parentGnossId= this._rootIdentifier+" "+ this._parentNameIdentifier;
		this._parentGnossId= this.graphsUrl+" "+ this._parentGnossId;
		this.parentIdentifier = parentIdentifier;
	}

	public List<ConceptEntity> getSubEntities() {
		return subEntities;
	}

	public void setSubEntities(List<ConceptEntity> subEntities) {
		this.subEntities = subEntities;
	}
	@Override
	public String getIdentifier() {
		super.setIdentifier(this._rootIdentifier+"_"+this._nameIdentifier);
		this._conceptEntityGnossId=this.graphsUrl+ super.getIdentifier();
		return super.getIdentifier();
	}
	@Override
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	
}
