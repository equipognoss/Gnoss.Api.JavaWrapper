package org.gnoss.apiWrapper.Main;

import org.gnoss.apiWrapper.ApiModel.AumentedReading;
import org.gnoss.apiWrapper.ApiModel.CommentParams;
import org.gnoss.apiWrapper.ApiModel.DeleteParams;
import org.gnoss.apiWrapper.ApiModel.ExistsUrlParams;
import org.gnoss.apiWrapper.ApiModel.FileOntology;
import org.gnoss.apiWrapper.ApiModel.GetDownloadParams;
import org.gnoss.apiWrapper.ApiModel.GetUrlParams;
import org.gnoss.apiWrapper.ApiModel.GnossResourceProperty;
import org.gnoss.apiWrapper.ApiModel.InsertAttributeParams;
import org.gnoss.apiWrapper.ApiModel.KeyEditors;
import org.gnoss.apiWrapper.ApiModel.KeyReaders;
import org.gnoss.apiWrapper.ApiModel.LinkedParams;
import org.gnoss.apiWrapper.ApiModel.LoadResourceParams;
import org.gnoss.apiWrapper.ApiModel.MassiveResourceLoad;
import org.gnoss.apiWrapper.ApiModel.MassiveTripleModifyParameters;
import org.gnoss.apiWrapper.ApiModel.ModifyResourceProperty;
import org.gnoss.apiWrapper.ApiModel.ModifyResourceTriple;
import org.gnoss.apiWrapper.ApiModel.ModifyResourceTripleListParams;
import org.gnoss.apiWrapper.ApiModel.ModifyTripleListModel;
import org.gnoss.apiWrapper.ApiModel.PersistentDeleteParams;
import org.gnoss.apiWrapper.ApiModel.ReaderEditor;
import org.gnoss.apiWrapper.ApiModel.RegisterLoadModel;
import org.gnoss.apiWrapper.ApiModel.Resource;
import org.gnoss.apiWrapper.ApiModel.ResourceNoveltiesModel;
import org.gnoss.apiWrapper.ApiModel.ResourceVisibility;
import org.gnoss.apiWrapper.ApiModel.ResponseGetCategories;
import org.gnoss.apiWrapper.ApiModel.ResponseGetMainImage;
import org.gnoss.apiWrapper.ApiModel.ResponseGetTags;
import org.gnoss.apiWrapper.ApiModel.ResponseGetUrl;
import org.gnoss.apiWrapper.ApiModel.SecondaryEntityModel;
import org.gnoss.apiWrapper.ApiModel.SemanticAttachedResource;
import org.gnoss.apiWrapper.ApiModel.SetMainImageParams;
import org.gnoss.apiWrapper.ApiModel.SetReadersEditorsParams;
import org.gnoss.apiWrapper.ApiModel.ShareParams;
import org.gnoss.apiWrapper.ApiModel.SparqlObject;
import org.gnoss.apiWrapper.ApiModel.SparqlQuery;
import org.gnoss.apiWrapper.ApiModel.ThesaurusCategory;
import org.gnoss.apiWrapper.ApiModel.TiposDocumentacion;
import org.gnoss.apiWrapper.ApiModel.Triple;
import org.gnoss.apiWrapper.ApiModel.Triples;
import org.gnoss.apiWrapper.ApiModel.UnsharedResourceParams;
import org.gnoss.apiWrapper.ApiModel.VotedParameters;
import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPICategoryException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.CharArrayDelimiters;
import org.gnoss.apiWrapper.Helpers.DataTypes;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.Languages;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.Helpers.StringDelimiters;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.gnoss.apiWrapper.Utilities.StringUtilities;
import org.gnoss.apiWrapper.Web.GnossWebClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.gnoss.apiWrapper.models.AuxiliaryEntitiesTriplesToInclude;
import org.gnoss.apiWrapper.models.BasicOntologyResource;
import org.gnoss.apiWrapper.models.ComplexOntologyResource;
import org.gnoss.apiWrapper.models.Multilanguage;
import org.gnoss.apiWrapper.models.OntologyProperty;
import org.gnoss.apiWrapper.models.RemoveTriples;
import org.gnoss.apiWrapper.models.SecondaryResource;
import org.gnoss.apiWrapper.models.TriplesToInclude;
import org.gnoss.apiWrapper.models.TriplesToModify;
import org.springframework.web.reactive.function.client.WebClient;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author slopez
 */
public class ResourceApi extends GnossApiWrapper{

	//Members
	private static String _ontologyUrl;
	private String _ontologyNameWithoutExtension;
	private String ontologyNameWithoutExtension;
	private String ontologyNameWithExtension;
	private CommunityApi _communityApi;
	private String _loadIdentifier; 
	private final int _DEFAULT_LOCK_DURATION = 60;
	private final int _DEFAULT_TIMEOUT_LOCK = 60;
	private ILogHelper _logHelper;
	
	//Properties
	private String DeveloperEmail;
	private String OntologyName;
	private CommunityApi CommunityApiWrapper;
	public String GraphsUrl;


	/**
	 * Constructor of ResourceApi
	 * @param oauth OAuth information to sign the Api requests
	 * @param communityShortName Community short name which you want to use the API
	 * @param ontologyName Ontology name of the resources that you are going to query, upload or modify
	 * @param developerEmail If you want to be informed of any incident that may happends during a large load of resources, an email will be sent to this email address
	 */
	public ResourceApi(OAuthInfo oauth, String communityShortName, String ontologyName, String developerEmail){
		super(oauth, communityShortName);
		DeveloperEmail = developerEmail;
		OntologyName = ontologyName;

		LoadApi();
	}

	/**
	 * Constructor of ResourceApi
	 * @param oauth OAuth information to sign the Api requests
	 * @param communityShortName Community short name which you want to use the API
	 * @param ontologyName Ontology name of the resources that you are going to query, upload or modify
	 */
	public ResourceApi(OAuthInfo oauth, String communityShortName, String ontologyName){
		super(oauth, communityShortName);        
		DeveloperEmail = "";
		OntologyName = ontologyName;

		LoadApi();
	}

	/**
	 * Constructor of ResourceApi
	 * @param oauth OAuth information to sign the Api requests
	 * @param communityShortName Community short name which you want to use the API	 
	 */
	public ResourceApi(OAuthInfo oauth, String communityShortName) {
		super(oauth, communityShortName); 
		DeveloperEmail = "";
		OntologyName = "";

		LoadApi();
	}

	/**
	 * Read the configuration from a configuration file
	 * @param filePath File Path
	 * @throws ParserConfigurationException Parser Configuration exception
	 * @throws IOException  IO Exception
	 * @throws SAXException  SAX Exception 
	 * @throws GnossAPIException  Gnoss API Exception
	 */
	@Override
	protected void ReadConfigFile(String filePath) throws ParserConfigurationException, SAXException, IOException, GnossAPIException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	Document xmlDocument = builder.parse(new File(filePath));
    	
    	NodeList consumerNode = xmlDocument.getElementsByTagName("ontologyName");
    	if(consumerNode.getLength() > 0){
    		OntologyName = consumerNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains ontologyName");
    	}
    	
    	super.ReadConfigFile(filePath);
	}
	
	
	

	/**
	 * Constructor of ResourceApi
	 * @param configFilePath Configuration file path, with a structure like http://api.gnoss.com/v3/exampleConfig.txt
	 * @throws GnossAPIException Gnoss API Exception
	 * @throws ParserCOnfigurationException parser configuration exception
	 * @throws SAXException sax exception
	 * @throws IOException IO exception 
	 */
	public ResourceApi(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException{
		super(configFilePath);
				
		DeveloperEmail = getOAuthInstance().getDeveloperEmail();
	}

	//Public methods
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarch
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 */
	public void LoadComplexSemanticResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps);
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarch
	 */
	public void LoadComplexSemanticResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 5);
	}
	
	/**
	 * Load resources of main entities in an otology and a community 
	 * @param resourceList List of resources to load
	 * @param hierarchycalCategories Indicates whether the categories has hierarchy
	 * @param ontology Ontology where resource will be loaded
	 * @param communityShortName Community short name where the resources will be loaded
	 */
	public void LoadComplexSemanticResourceListWithOntologyAndCommunity(ArrayList<ComplexOntologyResource> resourceList, boolean hierarchycalCategories, String ontology, String communityShortName){
		LoadComplexSemanticResourceListWithOntologyAndCommunityInt(resourceList, hierarchycalCategories, ontology, communityShortName);
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param rdfsPath Path to save the RDF, if necessary
	 */
	public void LoadComplexSemanticResourceListSavingLocalRdf(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps, String rdfsPath){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, null, rdfsPath);
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 */
	public void LoadComplexSemanticResourceListSavingLocalRdf(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, null, null);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 */
	public void LoadComplexSemanticResourceListSavingLocalRdf(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 5, null, null);
	}
	
	/**
	 * Load resources of main entities in a community
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param communityShortName Number of retries loading of the failed load of a resource
	 * @param numAttemps Defined if it is necessary the load in other community that the specified in the OAuth
	 */
	public void LoadComplexSemanticResourceListCommunityShortName(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String communityShortName, int numAttemps){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, communityShortName);
	}
	
	/**
	 * Load resources of main entities in a community
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param communityShortName Number of retries loading of the failed load of a resource	 
	 */
	public void LoadComplexSemanticResourceListCommunityShortName(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String communityShortName){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 5, communityShortName);
	}
	
	/**
	 * Load a complex semantic resource ComplexSemanticResource saving the resource rdf
	 * @param resource Resource to load
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @throws GnossAPIArgumentException Gnoss argument exception 
	 * @return Resource identifier string
	 */
	public String LoadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath, boolean hierarquicalCategories, boolean isLast, int numAttemps) throws GnossAPIArgumentException{
		if(StringUtils.isEmpty(rdfsPath)){
			throw new GnossAPIArgumentException("You must set the parameter rdfsPath");
		}
		
		return LoadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, null, rdfsPath);
	}
	
	/**
	 * Load a complex semantic resource ComplexSemanticResource saving the resource rdf
	 * @param resource Resource to load
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @throws GnossAPIArgumentException Gnoss argument exception 
	 * @return Resource identifier string
	 */	
	public String LoadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath, boolean hierarquicalCategories, boolean isLast) throws GnossAPIArgumentException{
		return LoadComplexSemanticResourceSaveRdf(resource, rdfsPath, hierarquicalCategories, isLast, 2);
	}

	/**
	 * Load a complex semantic resource ComplexSemanticResource saving the resource rdf
	 * @param resource Resource to load
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @return Resource identifier string
	 * @throws GnossAPIArgumentException Gnoss argument exception 
	 */
	public String LoadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath, boolean hierarquicalCategories) throws GnossAPIArgumentException{
		return LoadComplexSemanticResourceSaveRdf(resource, rdfsPath, hierarquicalCategories, false, 2);
	}
	
	/**
	 * Load a complex semantic resource ComplexSemanticResource saving the resource rdf
	 * @param resource Resource to load
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @throws GnossAPIArgumentException Gnoss argument exception 
	 * @return Resource identifier string
	 */
	public String LoadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath) throws GnossAPIArgumentException{
		return LoadComplexSemanticResourceSaveRdf(resource, rdfsPath, false, false, 2);
	}
	
	/**
	 * Load a complex semantic resource in the community
	 * @param resource: Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @return Resource identifier string
	 */
	public String LoadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName, boolean hierarquicalCategories, boolean isLast, int numAttemps){
		return LoadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, communityShortName);
	}
	
	/**
	 * Load a complex semantic resource in the community
	 * @param resource Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @return Resource identifier string
	 */
	public String LoadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName, boolean hierarquicalCategories, boolean isLast){
		return LoadComplexSemanticResourceCommunityShortName(resource, communityShortName, hierarquicalCategories, isLast, 2);
	}
	
	/**
	 * Load a complex semantic resource in the community
	 * @param resource Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @return Resource identifier string
	 */
	public String LoadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName, boolean hierarquicalCategories){
		return LoadComplexSemanticResourceCommunityShortName(resource, communityShortName, hierarquicalCategories, false, 2);
	}
	
	/**
	 * Load a complex semantic resource in the community
	 * @param resource Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @return Resource identifier string
	 */
	public String LoadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName){
		return LoadComplexSemanticResourceCommunityShortName(resource, communityShortName, false, false, 2);
	}	
	
	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @return String resource Gnoss ID
	 */
	public String LoadComplexSemanticResourceRdf(ComplexOntologyResource resource, String rdfFile, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName, String rdfsPath){
		try{
			if(resource.getTextCategories() != null){
				if(hierarquicalCategories){
					if(StringUtils.isEmpty(communityShortName)){
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
				else{
					if(StringUtils.isEmpty(communityShortName)){
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}
			
			String documentId = "";
			if(StringUtils.isEmpty(communityShortName)){
				communityShortName = getCommunityShortName();
			}
			
			resource.setRdFile(rdfFile);
			LoadResourceParams model = GetResourceModelOfComplexOntologyResource(communityShortName, resource, false, isLast);
			documentId = CreateComplexOntologyResource(model);
			resource.setUploaded(true);
			
			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle: " + resource.getTitle() + "\tResourceID: " + resource.getGnossId());
			if(resource.getShortGnossId() != UUID.fromString("00000000-0000-0000-0000-000000000000") && documentId != resource.getGnossId()){
				LogHelper.getInstance().Info("Resource loaded with the id: " + documentId + "\nThe IDGnoss provided to the method is different from the returned by the API");
			}
			
			if(!StringUtils.isEmpty(rdfsPath)){
				File directory = new File(rdfsPath + "/" + GetOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl()));
				if(!directory.exists()){
					directory.mkdir();
				}
				
				File file = new File(rdfsPath + "/" + GetOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl() + ".rdf"));
				if(!file.exists()){
					FileWriter fileWriter = new FileWriter (file);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(resource.getRdFile().toString());
					bufferedWriter.close();
					fileWriter.close();
				}
			}
			
			resource.setGnossId(documentId);
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("Error loading the resource: \tID: " + resource.getId() + ". Title: " + resource.getTitle() + ". Message: " + ex.getMessage());
		}
		
		return resource.getGnossId();
	}
	
	/**
	 * Loads the attached files of a not yet loaded resource.
	 * @param resource
 	 *		community_short_name = community short name string
     *   	resource_id = resource identifier guid
     *   	resource_attached_files = resource attached files. List of SemanticAttachedResource
     *   	SemanticAttachedResource:
     *   	file_rdf_properties = image name
     *  	file_property_type = type of file
     *   	rdf_attached_files = image to load byte[]
     *   	main_image = main image string
	 */
	public void MassiveUploadFiles(LoadResourceParams resource){
		try{
			UploadImages(resource.getResource_id(), resource.getResource_attached_files(), resource.getMain_image());
			
			String imageId = "";
			
			if(resource.getResource_attached_files() != null && resource.getResource_attached_files().size() > 0){
				imageId = resource.getResource_attached_files().get(0).getFile_rdf_property();
			}
			
			LogHelper.getInstance().Debug("Massive uploading files correct of the resource '" + imageId + "' del recurso " + resource.getResource_id());			
		}
		catch(Exception ex){
			LogHelper.getInstance().Debug("Error uploading files of the resource " + resource.getResource_id() + ex.getMessage());
		}
	}
	
	/**
	 * Loads the attached files of a not yet loaded resource.
	 * @param resource
 	 *		community_short_name = community short name string
     *   	resource_id = resource identifier guid
     *   	resource_attached_files = resource attached files. List of SemanticAttachedResource
     *   	SemanticAttachedResource:
     *   	file_rdf_properties = image name
     *  	file_property_type = type of file
     *   	rdf_attached_files = image to load byte[]
     *   	main_image = main image string
	 */
	public void MassiveUploadImages(LoadResourceParams resource) throws GnossAPIArgumentException{
		try{
			UploadImages(resource.getResource_id(), resource.getResource_attached_files(), resource.getMain_image());
			
			LogHelper.getInstance().Debug("Massive uploading images correct of the resource '" + resource.getResource_id() + "'");
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("Error uploading images of the resource " + resource.getResource_id() + ex.getMessage());
			throw new GnossAPIArgumentException("Error uploading images of the resource " + resource.getResource_id() + ex.getMessage());
		}
	}
	
	/**
	 * Loads a partitioned xml of the ontology
	 * @param xmlFile xml file 
	 * @param fileName File name 
	 * @return boolean T or F  
	 */
	public boolean LoadPartitionedXmlOntology(byte[] xmlFile, String fileName){
		try{
			String url = getApiUrl() + "/ontology/upload-partitioned-xml";
			
			FileOntology model = new FileOntology();
			model.setCommunity_short_name(getCommunityShortName());
			model.setOntology_name(getOntologyUrl());
			model.setFile_name(fileName);
			model.setFile(xmlFile);
			
			WebRequestPostWithJsonObject(url, model);
			
			LogHelper.getInstance().Debug("The xml file " + fileName + " of the ontology " + getOntologyUrl() + " has been upload in the community " + getCommunityShortName());
			
			return true;
		}
		catch(Exception e){
			LogHelper.getInstance().Error("El XML " + fileName + " no se ha subido. " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * Loads a partitioned ontology.
	 * @param ontologyFile ontology file 
	 * @param  fileName file name 
	 * @return boolean  T or F 
	 */
	public boolean LoadPartitionedOntology(byte[] ontologyFile, String fileName){
		 try
         {
             String url = getApiUrl() + "/ontology/upload-partitioned-ontology";

             FileOntology model = new FileOntology();  
             model.setCommunity_short_name(getCommunityShortName());
             model.setOntology_name(getOntologyUrl());
             model.setFile_name(fileName);
             model.setFile(ontologyFile);
             
             WebRequestPostWithJsonObject(url, model);

             LogHelper.getInstance().Debug("The file of the ontology has been upload in the communtiy " + getCommunityShortName());

             return true;
         }
         catch (Exception e)
         {
             LogHelper.getInstance().Error("El file " + fileName + " has not been uploaded. " + e.getMessage());
             return false;
         }
	}
	
	//Modify
	
	/**
	 * Modifies the complex ontology resource with the rdf
	 * @param resource Resource to load
	 * @param rdfFile Resource rdf file
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 */
	public void ModifyComplexOntologyResourceRDF(ComplexOntologyResource resource, String rdfFile, boolean hierarquicalCategories, boolean isLast){
		LogHelper.getInstance().Trace("******************** Begin the resource modification: " + resource.getGnossId());

		try{
			if(resource.getTextCategories() != null){
				if(hierarquicalCategories){
					resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
				else{
					resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
			}
			
			resource.setRdFile(rdfFile);
			LoadResourceParams model = GetResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
			resource.setModified(ModifyComplexOntologyResource(model));
			
			if(resource.isModified()){
				LogHelper.getInstance().Debug("Successfully modified the resource with id: " + resource.getShortGnossId());
			}
			else{
				LogHelper.getInstance().Error("ERROR --> The resource with id: " + resource.getShortGnossId() + " has not been modified");
			}
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("ERROR --> The resource with id: " + resource.getShortGnossId() + " has not been modified." + ex.getMessage());
		}
	}
	
	/**
	 * Modifies the complex ontology resource
	 * @param resource Resource to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @param communityShortName Community short name where the resources will be loaded
	 */
	public void ModifyComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, String communityShortName){
		LogHelper.getInstance().Trace("******************** Begin modification of resource: " + resource.getGnossId());
		try{
			if(resource.getTextCategories() != null){
				if(hierarquicalCategories){
					resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
				}
				else{
					resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
				}
			}
			
			LoadResourceParams model = GetResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
			resource.setModified(ModifyComplexOntologyResource(model));
			
			if(resource.isModified()){
				LogHelper.getInstance().Debug("Successfully modified the resource with id: " + resource.getId() + " and Gnoss identifier " + resource.getShortGnossId() + "belonging to the ontology '" + resource.getOntology().getOntologyUrl() + "' and RdfType = '" + resource.getOntology().getRdfType() + "'");
			}
			else{
				LogHelper.getInstance().Error("The resource with id: " + resource.getShortGnossId() + " of the ontology '" + resource.getOntology().getOntologyUrl() + "' has not bean modified.");
			}
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("The resource with id: " + resource.getShortGnossId() + " of the ontology '" + resource.getOntology().getOntologyUrl() + "' has not been modified." + ex.getMessage());
		}
	}
	
	/*
	/**
	 * Modifies the basic ontology resource
	 * @param resource: Resource to load
	 * @param hierarquicalCategories: Indicates whether the categories has hierarchy
	 * @param isLast: There are not resources left to load
	 */
	/*
	public void ModifyBasicOntologyResource(BasicOntologyResource resource, boolean hierarquicalCategories, boolean isLast){
		try{
			if(resource.get)
		}
	}
	*/
	
	
	/**
	 * Modifies the complex ontology resource generating a new rdf, and also changes the physical image with the new image
	 * @param resource Resource to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 */
	public void ModifyComplexOntologyResourceWithImages(ComplexOntologyResource resource, boolean hierarquicalCategories, int numAttemps){
		int attempNumber = 0;
		
		while(attempNumber < numAttemps){
			attempNumber++;
			LogHelper.getInstance().Trace("******************** Begin the lap number: " + attempNumber);
			
			try{
				if(resource.getTextCategories() != null){
					if(hierarquicalCategories){
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
				}
				
				LoadResourceParams model = GetResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, true);
				resource.setModified(ModifyComplexOntologyResource(model));
				
				LogHelper.getInstance().Debug("Successfully modified the resource with ID: " + resource.getId() + " and Gnoss identifier " + resource.getShortGnossId());
				if(resource.isModified()){
					try{
						OntologyProperty propOntoImage = null;
						for(OntologyProperty ontology : resource.getOntology().getProperties()){
							if(ontology.getClass().equals(DataTypes.OntologyPropertyImage.getClass())){
								propOntoImage = ontology;
							}
						}
						
						String prefijoPredicado = propOntoImage.getName().split(CharArrayDelimiters.Colon.toString())[0];
						String nombreEtiquetaSinPrefijo = propOntoImage.getName().split(CharArrayDelimiters.Colon.toString())[1];
						String nombreImagen = propOntoImage.getValue().toString();
						String predicado = "";
						for(String prefix : resource.getOntology().getPrefixList()){
							if(prefix.contains("xmlns:" + prefijoPredicado)){
								predicado = prefix.split(CharArrayDelimiters.Equal.toString())[1].toString().replace("\"", "") + nombreEtiquetaSinPrefijo;
							}
						}
						
						ArrayList<ModifyResourceTriple> triplesList = new ArrayList<ModifyResourceTriple>();
						ModifyResourceTriple triple = new ModifyResourceTriple();
						triple.setOld_object("");
						triple.setPredicate(predicado);
						triple.setNew_object(nombreImagen);
						triple.setGnoss_property(GnossResourceProperty.none);
						triplesList.add(triple);
						
						ArrayList<SemanticAttachedResource> resourceAttachedFiles = new ArrayList<SemanticAttachedResource>();
						if(resource.getAttachedFilesName().size() > 0){
							int i = 0;
							for(String name : resource.getAttachedFilesName()){
								SemanticAttachedResource adjunto = new SemanticAttachedResource();
								adjunto.setFile_rdf_property(name);
								adjunto.setFile_property_type((short)resource.getAttachedFilesType().get(i).getID());
								adjunto.setRdf_attacherd_file(resource.getAttachedFiles().get(i));
								adjunto.setDelete_file(resource.getAttachedFiles().get(i).equals(null));
								i++;
								resourceAttachedFiles.add(adjunto);
							}
						}
						
						ModifyTripleList(resource.getShortGnossId(), triplesList, getLoadIdentifier(), resource.getPublishInHome(), resource.getMainImage(), resourceAttachedFiles, true);
					}
					catch(Exception ex){
						LogHelper.getInstance().Error("ERROR replacing the image of the resource: " + resource.getId() + ". Title: " + resource.getTitle() + ". \nMessage: " + ex.getMessage());
					}
				}
				else{
					LogHelper.getInstance().Error("The resource with id: " + resource.getGnossId() + " has not been modified.");
				}
			}
			catch(Exception ex){
				LogHelper.getInstance().Error("ERROR at: " + resource.getId() + ". Title: " + resource.getTitle() + ". Message: " + ex.getMessage());
			}
			
			LogHelper.getInstance().Debug("******************** Finished lap number: " + attempNumber);
		}
	}
	
	public void ModifyComplexOntologyResourceWithImages(ComplexOntologyResource resource, boolean hierarquicalCategories){
		ModifyComplexOntologyResourceWithImages(resource, hierarquicalCategories, 5);
	}
	
	/**
	 * Modifies the complex ontology resource
	 * @param resource Resource to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 */
	public void ModifyComplexOntologyResource(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast){
		LogHelper.getInstance().Trace("******************** Begin the resource modification: " + resource.getGnossId());

		try{
			if(resource.getTextCategories() != null){
				if(hierarquicalCategories){
					resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
				else{
					resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
			}
			
			LoadResourceParams model = GetResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
			resource.setModified(ModifyComplexOntologyResource(model));
			
			if(resource.isModified()){
				LogHelper.getInstance().Debug("Successfully modified the resource with id: " + resource.getId() + " and Gnoss identifier " + resource.getShortGnossId() + " belonging to the ontology '" + resource.getOntology().getOntologyUrl() + "' with RdfType = '" + resource.getOntology().getRdfType() + "'");
			}
			else{
				LogHelper.getInstance().Error("The resource with id: " + resource.getShortGnossId() + " of the ontology '" + resource.getOntology().getOntologyUrl() + "' has not been modified.");
			}
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("The resource with id " + resource.getGnossId() + " has not been modified. Message: " + ex.getMessage());
		}
	}
	
	public boolean UploadImages(UUID resourceId, ArrayList<SemanticAttachedResource> imageList, String mainImage) throws GnossAPIArgumentException{
		boolean loaded = false;
		LoadResourceParams model = null;
		try{
			String url = getApiUrl() + "/resource/upload-images";
			model = new LoadResourceParams();
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setResource_attached_files(imageList);
			model.setMain_image(mainImage);
			
			WebRequestPostWithJsonObject(url, model);
			loaded = true;
			LogHelper.getInstance().Debug("Ended images upload");
		}
		catch(Exception ex){
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
	        String mensaje = "Error uploading resource " + resourceId + " images. \r\n Json: " + json;
			LogHelper.getInstance().Error("Error uploading resource " + resourceId + " images. \r\n Json: " + json);
			throw new GnossAPIArgumentException(mensaje);
		}
		
		return loaded;
	}
	
	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @return String LoadComplexSemanticResourceRdf 
	 */
	public String LoadComplexSemanticResourceRdf(ComplexOntologyResource resource, String rdfFile){
		return LoadComplexSemanticResourceRdf(resource, rdfFile, false, false, 5, null, null);	
	}
	
	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @return String LoadComplexSemanticResourceRdf
	 */
	public String LoadComplexSemanticResourceRdf(ComplexOntologyResource resource, String rdfFile, boolean hierarquicalCategories){
		return LoadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, false, 5, null, null);	
	}
	
	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @return String LoadComplexSemanticResourceRdf
	 */
	public String LoadComplexSemanticResourceRdf(ComplexOntologyResource resource, String rdfFile, boolean hierarquicalCategories, boolean isLast){
		return LoadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, isLast, 5, null, null);
	}
	
	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @return String LoadComplexSemanticResourceRdf
	 */
	public String LoadComplexSemanticResourceRdf(ComplexOntologyResource resource, String rdfFile, boolean hierarquicalCategories, boolean isLast, int numAttemps){
		return LoadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, isLast, numAttemps, null, null);
	}
	
	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @return String LoadComplexSemanticResourceRdf
	 */
	public String LoadComplexSemanticResourceRdf(ComplexOntologyResource resource, String rdfFile, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName){
		return LoadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, isLast, numAttemps, communityShortName, null);
	}
	
	public String getApiUrl(){
		return getOAuthInstance().getApiUrl();
	}

	/**
	 * Returns a guid from a resource large identigier. If it cannot get it, returns an empty guid.
	 * @param largeResourceId Resource large identifier
	 * @return UUID UUID ID
	 */
	public UUID GetShortGuid(String largeResourceId){
		try{
			if(largeResourceId.contains("items")){
				String[] result = largeResourceId.split("_");
				return (UUID.fromString(result[result.length - 2]));
			}
			else{
				String[] result = largeResourceId.split("/");
				return (UUID.fromString(result[result.length - 1]));
			}
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("Unable to get the guid of " + largeResourceId + ". \n MESSAGE:" + ex.getMessage());
			return UUID.fromString("00000000-0000-0000-0000-000000000000");
		}
	}
	
	/**
	 * Modifies secondary entities loaded in a normal way (one RDF by resource)
	 * @param resourceList Resource list to delete
	 * @throws GnossAPIException  Gnoss API Exception 
	 */
	public void ModifySecondaryResourcesList(ArrayList<SecondaryResource> resourceList) throws GnossAPIException{
		LogHelper.getInstance().Debug("Modifyng " + resourceList.size() + "resources");
		int left = resourceList.size();
		for(SecondaryResource rs : resourceList){
			if(ModifySecondaryResource(rs)){
				left--;
				LogHelper.getInstance().Debug("Still remaining " + left + "resources");
			}
		}
	}
	
	/**
	 * Modifies secondary entities loaded in a normal way (one RDF by resource)
	 * @param resourceList Resource list to delete
	 * @return boolean T or F 
	 * @throws GnossAPIException Gnoss API Exception 
	 */
	public boolean ModifySecondaryResource(SecondaryResource resourceList) throws GnossAPIException{
		boolean modified = false;
		
		try{
			ModifySecondaryEntity(getOntologyUrl(), getCommunityShortName(), resourceList.getRdfFile());
			resourceList.setModified(true);
			modified = true;			
		}
		catch(Exception ex){
			String message = "The secondary resource with ID: " + resourceList.getId() + " cannot be modified. \n MESSAGE: " + ex.getMessage();
			LogHelper.getInstance().Error(message);
			resourceList.setModified(false);
			throw new GnossAPIException(message);
		}
		return modified;
	}
	
	/**
	 * Creates a complex ontology resource
	 * @param parameters parameters
	 * @return Resource identifier guid
	 */
	public String CreateComplexOntologyResource(LoadResourceParams parameters)
	{
		String resourceId = "";

		try
		{
			String url = getApiUrl() + "/resource/create-complex-ontology-resource";
			resourceId = WebRequestPostWithJsonObject(url, parameters);
			if(resourceId != null){
				StringUtilities strUtil = new StringUtilities();
				resourceId = strUtil.trimEnd(resourceId, '"');
			}

			if (StringUtils.isEmpty(resourceId))
			{
				LogHelper.getInstance().Debug("Complex ontology resource created: {resourceId}");
			}
			else
			{
				PrepareAttachedToLog(parameters.resource_attached_files);
				LogHelper.getInstance().Debug("Complex ontology resource not created: {JsonConvert.SerializeObject(parameters)}");
			}
		}
		catch (Exception ex)
		{
			PrepareAttachedToLog(parameters.resource_attached_files);
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(parameters);
			LogHelper.getInstance().Error("Error trying to create a complex ontology resource. \r\n Error description: {ex.Message}. \r\n: Json: " + json);
		}

		return resourceId;
	}

	/**
	 * Modifies a complex ontology resouce
	 * @param parameter parameters
	 * @return If the resource was modified T or F 
	 */
	public boolean ModifyComplexOntologyResource(LoadResourceParams parameters){
		boolean modified = false;
		try{
			String url = getApiUrl() + "/resource/modify-complex-ontology-resource";
			String response = WebRequestPostWithJsonObject(url, parameters);
			
			if(!StringUtils.isEmpty(response)){
				modified = true;
				LogHelper.getInstance().Debug("Complex ontology resource modified: " + parameters.getResource_id());
			}
		}
		catch(Exception ex){
			PrepareAttachedToLog(parameters.getResource_attached_files());
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(parameters);
			LogHelper.getInstance().Error("Error modifying resource " + parameters.getResource_id() + ".\r\n: Json: " + json);
		}
		
		return modified;
	}
	
	public void ModifyProperty(UUID resourceId, String property, String newObject) throws Exception{
		ModifyResourceProperty model = null;
		try{
			String url = getApiUrl() + "/resource/modify-property";
			model = new ModifyResourceProperty();
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setProperty(property);
			model.setNew_object(newObject);
			
			WebRequestPostWithJsonObject(url, model);
			
			LogHelper.getInstance().Debug("Ended resource deleting");
		}
		catch(Exception ex){
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
	        String message = "Error deleting resource " + resourceId + ". \r\n: Json: " + json + "Error: " + ex.getMessage();
			LogHelper.getInstance().Error(message);
			throw new Exception(message);
		}
	}
	
	/**
	 * Method to add / modify / delete triples of multiple complex ontology resources
	 * @param multipleResourceTriples Dictionary with resource identifier guid and the resourde triples list to modify
	 * @param loadId Charge identifier string
	 * @param publishHome Dictionary with resource identifier guid and the resource attached files list.
	 * @param mainImage Main image String
	 * @param multipleResourceAttachedFiles Indicates whether the home must be updated
	 * @throws Exception exception 
	 */
	public void ModifyMultipleResourcesTripleList(HashMap<UUID, ArrayList<ModifyResourceTriple>> multipleResourceTriples, String loadId, boolean publishHome, 
			String mainImage, HashMap<UUID, ArrayList<SemanticAttachedResource>> multipleResourceAttachedFiles) throws Exception	{
		ArrayList<ModifyResourceTripleListParams> model = null;
		try{
			String url = getApiUrl() + "/resource/modify-multiple-resources-triple-list";
			int i = 0;
			int contResources = multipleResourceTriples.keySet().size();
			for(UUID resourceID : multipleResourceTriples.keySet()){
				i++;
				if(model == null){
					model = new ArrayList<ModifyResourceTripleListParams>();
				}
				
				ArrayList<ModifyResourceTriple> tripleList = null;
				if(multipleResourceTriples != null && multipleResourceTriples.containsKey(resourceID)){
					tripleList = multipleResourceTriples.get(resourceID);
					}
				
				ArrayList<SemanticAttachedResource> attachedList = null;
				if(multipleResourceAttachedFiles != null && multipleResourceAttachedFiles.containsKey(resourceID)){
					attachedList = multipleResourceAttachedFiles.get(resourceID);
				}
				
				boolean endOfLoad = false;
				if(i == contResources){
					endOfLoad = true;
				}
				
				ModifyResourceTripleListParams newModifyResourceTripleListParams = new ModifyResourceTripleListParams();
				newModifyResourceTripleListParams.setResource_triples(tripleList);
				newModifyResourceTripleListParams.setCharge_id(loadId);
				newModifyResourceTripleListParams.setResource_id(resourceID);
				newModifyResourceTripleListParams.setCommunity_short_name(getCommunityShortName());
				newModifyResourceTripleListParams.setPublish_home(publishHome);
				newModifyResourceTripleListParams.setMain_image(mainImage);
				newModifyResourceTripleListParams.setResource_attached_files(attachedList);
				newModifyResourceTripleListParams.setEnd_of_load(endOfLoad);				
				model.add(newModifyResourceTripleListParams);
			}
			WebRequestPostWithJsonObject(url, model);
			LogHelper.getInstance().Debug("Ended resource triple list modification");
		}
		catch(Exception ex){
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
			LogHelper.getInstance().Error("Error modifying multiple resource triple list. \r\n: Json: " + model);
			throw new Exception("Error modifying multiple resource triple list. \r\n: Json: " + model);
		}		
	}		
	
	/**
	 * Method for adding one or more properties of a loaded resource. In IncluirTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToInclude list of the resource properties that will be included
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param publishHome Indicates whether the home must be updated
	 * @return HashMap Indicates whether the properties have been added to the loaded resource
	 */
	public HashMap<UUID, Boolean> InsertPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, int numAttemps, boolean publishHome){
		return InsertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}
	
	/**
	 * Method for adding one or more properties of a loaded resource. In IncluirTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToInclude list of the resource properties that will be included
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @return HashMap Indicates whether the properties have been added to the loaded resource
	 */
	public HashMap<UUID, Boolean> InsertPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, int numAttemps){
		return InsertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	/**
	 * Method for adding one or more properties of a loaded resource. In IncluirTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToInclude list of the resource properties that will be included
	 * @return HashMap Indicates whether the properties have been added to the loaded resource
	 */
	public HashMap<UUID, Boolean> InsertPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples){
		return InsertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName());
	}
	
	private HashMap<UUID, Boolean> InsertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
		HashMap<UUID, Boolean> result = new HashMap<UUID, Boolean>();
		int processedNumber = 0;
		int attempNumber = 0;
		HashMap<UUID, ArrayList<TriplesToInclude>> toInsert = new HashMap<UUID, ArrayList<TriplesToInclude>>(resourceTriples);
		while(toInsert != null && toInsert.size() > 0 && attempNumber < numAttemps){
			int i = 0;
			int contResource = resourceTriples.keySet().size();
			for(UUID docID : resourceTriples.keySet()){
				i++;
				ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
				attempNumber++;
				processedNumber++;
				for(TriplesToInclude iT : resourceTriples.get(docID)){
					ModifyResourceTriple triple = new ModifyResourceTriple();
					triple.setOld_object(null);
					triple.setPredicate(iT.getPredicate());
					triple.setNew_object(iT.getNewValue());
					triple.setGnoss_property(GnossResourceProperty.none);
					
					if(iT.isTitle()){
						triple.setGnoss_property(GnossResourceProperty.title);
					}
					else if(iT.isDescription()){
						triple.setGnoss_property(GnossResourceProperty.description);
					}
					
					listaValores.add(triple);
				}
				try{
					boolean endOfLoad = false;
					if(i == contResource){
						endOfLoad = true;
					}
					ModifyTripleList(docID, listaValores, getLoadIdentifier(), publishHome, null, null, endOfLoad);
					
					LogHelper.getInstance().Debug(processedNumber + "of" + resourceTriples.size() + "Object: " + docID + ". Resource: " + resourceTriples.get(docID).toArray());
					toInsert.remove(docID);
					result.put(docID, true);
				}
				catch(Exception ex){
					LogHelper.getInstance().Error("Resource" + docID + " : " + ex.getMessage());
					result.put(docID, false);
				}
			}
			LogHelper.getInstance().Debug("******************** Lap number: " + attempNumber + " finished");
		}
		return result;
	}
	
	private HashMap<UUID, Boolean> InsertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName, int numAttemps){
		return InsertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, false);
	}
	
	private HashMap<UUID, Boolean> InsertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName){
		return InsertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), 2, false); 		
	}
	
	/**
	 * Method for modifying one or more properties of a loaded resource. In ModificarTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToModify list of the resource properties that will be modified
	 * @param numAttemps Indicates whether the home must be updated
	 * @param publishHome Default 2. Number of retries loading of the failed load of a resource
	 * @return HashMap Indicates whether the properties have been modified of the loaded resource
	 */
	public HashMap<UUID, Boolean> ModifyPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, int numAttemps, boolean publishHome){
		return ModifyPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}
	
	public HashMap<UUID, Boolean> ModifyPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, int numAttemps){
		return ModifyPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps);
	}
	
	public HashMap<UUID, Boolean> ModifyPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples){
		return ModifyPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName());
	}
	
	private HashMap<UUID, Boolean> ModifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
		HashMap<UUID, Boolean> result = new HashMap<UUID, Boolean>();
		int processedNumer = 0;
		int attempNumber = 0;
		
		ArrayList<String> valores = new ArrayList<String>();
		HashMap<UUID, ArrayList<TriplesToModify>> toModify = new HashMap<UUID, ArrayList<TriplesToModify>>(resourceTriples);
		while(toModify != null && toModify.size() > 0 && attempNumber < numAttemps){
			int i = 0;
			int contResources = resourceTriples.keySet().size();
			for(UUID docID : resourceTriples.keySet()){
				i++;
				ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
				attempNumber++;
				processedNumer++;
				for(TriplesToModify mT : resourceTriples.get(docID)){
					ModifyResourceTriple triple = new ModifyResourceTriple();
					triple.setOld_object(mT.getOldValue());
					triple.setPredicate(mT.getPredicate());
					triple.setNew_object(mT.getNewValue());
					triple.setGnoss_property(GnossResourceProperty.none);
					
					if(mT.isTitle()){
						triple.setGnoss_property(GnossResourceProperty.title);
					}
					else if(mT.isDescription()){
						triple.setGnoss_property(GnossResourceProperty.description);
					}
					
					listaValores.add(triple);
				}
				
				try{
					boolean endOfLoad = false;
					if(i == contResources){
						endOfLoad = true;
					}
					ModifyTripleList(docID, listaValores, getLoadIdentifier(), publishHome, null, null, endOfLoad);
					
					LogHelper.getInstance().Debug(processedNumer + " of " + resourceTriples.size() + ". Object: " + docID + ".Resource: " + resourceTriples.get(docID).toArray());
					toModify.remove(docID);
					result.put(docID, true);					
				}
				catch(Exception ex){
					LogHelper.getInstance().Error("Resource " + docID + " : " + ex.getMessage());
					result.put(docID, false);
				}
			}
			LogHelper.getInstance().Debug("******************** Lap number: " + attempNumber + "finished");
		}
		
		return result;
	}
	
	private HashMap<UUID, Boolean> ModifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps){
		return ModifyPropertiesLoadedResourcesInt(resourceTriples, communityShortName, numAttemps, false);
	}
	
	
	private HashMap<UUID, Boolean> ModifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName){
		return ModifyPropertiesLoadedResourcesInt(resourceTriples, communityShortName, 2, false);
	}
	
	/**
	 * Method to add / modify / delete triples of complex ontology resource
	 * @param resourceID Resource identifier guid
	 * @param tripleList Resource triples list to modify
	 * @param loadId Charge identifier string
	 * @param publishHome Resource attached files list
	 * @param mainImage Main image string
	 * @param resourceAttachedFiles Indicates whether the home must be updated
	 * @param endOfLoad Indicates the resource modified is the last and it must deletes cache
	 * @throws Exception Exception 
	 */
	public void ModifyTripleList(UUID resourceID, ArrayList<ModifyResourceTriple> tripleList, String loadId, boolean publishHome, String mainImage, ArrayList<SemanticAttachedResource> resourceAttachedFiles, boolean endOfLoad) throws Exception{
		ModifyResourceTripleListParams model = null;
		try{
			String url = getApiUrl() + "/resource/modify-triple-list";
			model = new ModifyResourceTripleListParams();
			model.setResource_triples(tripleList);
			model.setCharge_id(loadId);
			model.setResource_id(resourceID);
			model.setCommunity_short_name(getCommunityShortName());
			model.setPublish_home(publishHome);
			model.setMain_image(mainImage);
			model.setResource_attached_files(resourceAttachedFiles);
			model.setEnd_of_load(endOfLoad);
			WebRequestPostWithJsonObject(url, model);
			
			LogHelper.getInstance().Debug("Ended resource triples list modification");
		}
		catch(Exception ex){
			PrepareAttachedToLog(model.getResource_attached_files());
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
			LogHelper.getInstance().Error("Error modifying resource triple list. \r\n: Json: " + model);
			throw new Exception("Error modifying resource triple list. \r\n: Json: " + model);
		}
	}
	
	/**
	 * Changes the current ontology by the indicated ontology.
	 * @param newOntology New ontology name
	 */
	public void ChangeOntology(String newOntology){
		String ontologia = newOntology.toLowerCase().replace(".owl", "");
		
		setOntologyUrl(null);
		
		if(!StringUtils.isEmpty(ontologia)){
			setOntologyUrl(getGraphsUrl() + "Ontologia/" + ontologia + ".owl");
		}
	}
	
	/**
	 * Loads a list of SecondaryResource
	 * @param resourceList List of SecondaryResource to load
	 * @throws GnossAPIException Gnoss API Exception 
	 */
	public void LoadSecondaryResourceList(ArrayList<SecondaryResource> resourceList) throws GnossAPIException{
		for(SecondaryResource rs : resourceList){
			LoadSecondaryResource(rs);
		}
	}
	
	public boolean LoadSecondaryResource(SecondaryResource resource) throws GnossAPIException{
		boolean success = false;
		
		try {
			CreateSecondaryEntity(resource.getSecondaryOntology().getOntologyUrl(), getCommunityShortName(), resource.getRdfFile());
			LogHelper.getInstance().Debug("Loaded secondary resource with ID: " + resource.getSecondaryOntology().getIdentifier());
			success = true;
			resource.setUploaded(true);
		}
		catch(Exception ex){
			LogHelper.getInstance().Error("Resource has not been loaded the secondary resource with ID: " + resource.getId() + ". Mensaje: " + ex.getMessage());
                        throw new GnossAPIException(("Resource has not been loaded the secondary resource with ID: " + resource.getId() + ". Mensaje: " + ex.getMessage()));
		}
		return success;
	}
	
	/**
	 * Load the basic parameters for the API
	 */
	@Override
	protected void LoadApi(){
		GetGraphsUrl();
		
		if(!StringUtils.isEmpty(OntologyName)){
			if(!OntologyName.contains(".owl")){
				_ontologyUrl = GraphsUrl + "Ontologia/" + OntologyName + ".owl";
			}
			else{
				_ontologyUrl = GraphsUrl + "Ontologia/" + OntologyName;
			}
		}
	}

	private void PrepareAttachedToLog(ArrayList<SemanticAttachedResource> resourceAttachedFiles){
		if(resourceAttachedFiles != null){
			for(SemanticAttachedResource adjunto : resourceAttachedFiles){
				adjunto.setRdf_attacherd_file(null);
			}
		}
	}
	
	/**
	 * Allows a virtuoso query, setting the 'SELECT' and 'WHERE' parts of the query and the graph name
	 * @param selectPart The 'SELECT' query part
	 * @param wherePart The 'WHERE' query part
	 * @param ontologyName Graph name where the query runs (without extension '.owl')
	 * @return SparqlObject DataSet with the query result
	 * @throws GnossAPIException  Gnoss API Exception
	 */
	public SparqlObject VirtuosoQuery(String selectPart, String wherePart, String ontologyName) throws GnossAPIException{
		LogHelper.getInstance().Trace("Entering the method VirtuosoQuery");
		return VirtuosoQueryInt(selectPart, wherePart, ontologyName);
	}

	private SparqlObject VirtuosoQueryInt(String selectPart, String wherePart, String graph) throws GnossAPIException{
		LogHelper.getInstance().Trace("Entering in the method: VirtuosoQueryInt");
		LogHelper.getInstance().Trace("SELECT: " + selectPart);
		LogHelper.getInstance().Trace("Grafo name: " + graph);
		LogHelper.getInstance().Trace("WHERE: " + wherePart);
		
		SparqlObject SO = new SparqlObject();
		
		try{
			LogHelper.getInstance().Trace("Query start");
			
			String url = getApiUrl() + "/sparql-endpoint/query";
			
			SparqlQuery model = new SparqlQuery();
			model.setOntology(graph);
			model.setCommunity_short_name(graph);
			model.setQuery_select(selectPart);
			model.setQuery_where(wherePart);
			
			String response = WebRequestPostWithJsonObject(url, model);
			
			Gson jsonUtilities = new Gson();
	        SO = jsonUtilities.fromJson(response, SparqlObject.class);
	        
	        LogHelper.getInstance().Trace("Query end");
		}
		catch(Exception wex){
			String resultado = wex.getMessage();
			throw new GnossAPIException("Could not make the query to Virtuoso.\n");
		}
		
		LogHelper.getInstance().Trace("Leaving the method");
		return SO;
	}
	
	public String LoadComplexSemanticResource(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, int numAttemps) {
		return LoadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, null, null);
	}

	public String LoadComplexSemanticResource(ComplexOntologyResource resource) {      
		return LoadComplexSemanticResourceInt(resource, false, false, 5, null, null);
	}

	private void CreateSecondaryEntity(String ontology_url, String community_short_name, String rdf) throws IOException, GnossAPIException{
		String url = getApiUrl() + "/secondary-entity/create";
		
		SecondaryEntityModel model = new SecondaryEntityModel();
		model.setOntology_url(ontology_url);
		model.setCommunity_short_name(community_short_name);
		model.setRdf(rdf);
		
		WebRequestPostWithJsonObject(url, model);
		
		LogHelper.getInstance().Debug("The secondary entity has been created in the graph " + ontology_url + " of the community " + community_short_name);
	}
	
	private void ModifySecondaryEntity(String ontology_url, String community_short_name, String rdf) throws IOException, GnossAPIException{
		String url = getApiUrl() + "/secondary-entity/modify";
		
		SecondaryEntityModel model = new SecondaryEntityModel();
		model.setOntology_url(ontology_url);
		model.setCommunity_short_name(community_short_name);
		model.setRdf(rdf);
		
		WebRequestPostWithJsonObject(url, model);
		
		LogHelper.getInstance().Debug("The secondary entity has been modified in the graph " + getOntologyUrl() + " of the community " + community_short_name);
	}
	
	private void DeleteSecondaryEntity(String ontology_url, String community_short_name, String entity_id) throws IOException, GnossAPIException{
		String url = getApiUrl() + "/secondary-entity/delete";
		
		SecondaryEntityModel model = new SecondaryEntityModel();
		model.setOntology_url(ontology_url);
		model.setCommunity_short_name(community_short_name);
		model.setEntity_id(entity_id);
		
		WebRequestPostWithJsonObject(url, model);
		
		LogHelper.getInstance().Debug("The secondary entity " + entity_id + " has been deleted in the graph " + ontology_url + " of the community " + community_short_name);
	}
	
	/**
	 * Logical delete of the resource
	 * @param resourceId Resource identifier
	 * @param loadId Charge identifier
	 * @param endOfCharge Marks the end of the charge  T or F 
	 * @throws Exception exception
	 */
	public void Delete(UUID resourceId, String loadId, boolean endOfCharge) throws Exception{
		DeleteParams model = null;
		try{
			String url = getApiUrl() + "/resource/delete";
			model = new DeleteParams();
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setCharge_id(loadId);
			model.setEnd_of_load(endOfCharge);
			
			WebRequestPostWithJsonObject(url, model);
			
			LogHelper.getInstance().Debug("Ended resource deleting");
		}
		catch(Exception ex){
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
			LogHelper.getInstance().Error("Error deleting resource " + resourceId + ". \r\n: Json: " + json);
			throw new Exception("Error deleting resource " + resourceId + ". \r\n: Json: " + json);
		}
	}
	
	/**
	 * Logical delete of the resource
	 * @param resourceId Resource identifier
	 * @param loadId Charge identifier
	 * @throws Exception exception
	 */
	public void Delete(UUID resourceId, String loadId) throws Exception{
		Delete(resourceId, loadId, false);
	}
	
	/**
	 * Persistent delete of the resource
	 * @param resourceId Resource identifier
	 * @param deleteAttached Indicates if the attached resources must be deleted
	 * @param endOfCharge Marks the end of the charge
	 * @return boolean If the resource was deleted
	 * @throws Exception  exception 
	 */
	public boolean PersistentDelete(UUID resourceId, boolean deleteAttached, boolean endOfCharge) throws Exception{
		boolean deleted = false;
		PersistentDeleteParams model = null;
		try{
			String url = getApiUrl() + "/resource/persistent-delete";
			model = new PersistentDeleteParams();
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setEnd_of_load(endOfCharge);
			model.setDelete_attached(deleteAttached);
			WebRequestPostWithJsonObject(url, model);
			deleted = true;
			LogHelper.getInstance().Debug("Ended resource persistent deleting");
		}
		catch(Exception ex){
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
			LogHelper.getInstance().Error("Error on persistent deleting resource " + resourceId + ". \r\n: Json:" + json + " /n" + ex.getMessage());
			throw new Exception("Error on persistent deleting resource " + resourceId + ". \r\n: Json:" + json + " /n" + ex.getMessage());
		}
		
		return deleted;
	}
	
	public void DeleteSecondaryEntitiesList(ArrayList<String> urlList, int numAttemps){
		int processedNumber = 0;
		int attempNumber = 0;
		
		ArrayList<String> originalResourceList = new ArrayList<String>(urlList);
		ArrayList<String> resourcesToDelete = new ArrayList<String>(urlList);
		while(urlList != null && urlList.size() > 0 && attempNumber <= numAttemps){
			attempNumber++;
			LogHelper.getInstance().Trace("******************** Begin lap number: " + attempNumber);
			for(String url : urlList){
				try{
					processedNumber++;
					DeleteSecondaryEntity(getOntologyUrl(), getCommunityShortName(), url);
					LogHelper.getInstance().Debug("Successfully deleted th resource with ID: " + url);
					resourcesToDelete.remove(url);
				}
				catch(Exception ex)
				{
					LogHelper.getInstance().Error("ERROR deleting: " + processedNumber + " of " + urlList.size() + "\tID: " + url + ". Message: " + ex.getMessage());
				}
			}
			LogHelper.getInstance().Debug("******************** Finished lap number: " + attempNumber);
			urlList = new ArrayList<String>(resourcesToDelete);
		}
	}
	
	public void DeleteSecondaryEntitiesList(ArrayList<String> urlList){
		DeleteSecondaryEntitiesList(urlList, 5);
	}
	
	/**
	 * Persistent delete of the resource
	 * @param resourceId Resource identifier
	 * @param deleteAttached Indicates if the attached resources must be deleted
	 * @return boolean If the resource was deleted
	 * @throws Exception exception
	 */
	public boolean PersistentDelete(UUID resourceId, boolean deleteAttached) throws Exception{
		return PersistentDelete(resourceId, deleteAttached, false);
	}
	
	/**
	 * @param resourceId Resource identifier
	 * @return Boolean T or F
	 * @throws Exception 
	 */
	public boolean PersistentDelete(UUID resourceId) throws Exception{
		return PersistentDelete(resourceId, false, false);
	}
	
	
	/**
	 * Modify one or more properties of an entity of a loaded resource.
	 * @param resourceTriples Contains as a key the large Gnoss identifier of secondary resource to modify and as a value a ModificarTriples list of the resource properties to modified
	 * @param numAttemps Indicates whether the home must be updated
	 * @param publishHome Number of retries loading of the failed load of a resource
	 */
	public void ModifyPropertyLoadedSecondaryResource(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, int numAttemps, boolean publishHome){
		ModifyPropertyLoadedSecondaryResourceInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}
	
	/**
	 * Modify one or more properties of an entity of a loaded resource.
	 * @param resourceTriples Contains as a key the large Gnoss identifier of secondary resource to modify and as a value a ModificarTriples list of the resource properties to modified
	 * @param numAttemps Indicates whether the home must be updated
	 */
	public void ModifyPropertyLoadedSecondaryResource(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, int numAttemps){
		ModifyPropertyLoadedSecondaryResourceInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	/**
	 * Modify one or more properties of an entity of a loaded resource.
	 * @param resourceTriples Contains as a key the large Gnoss identifier of secondary resource to modify and as a value a ModificarTriples list of the resource properties to modified
	 */
	public void ModifyPropertyLoadedSecondaryResource(HashMap<String, ArrayList<TriplesToModify>> resourceTriples){
		ModifyPropertyLoadedSecondaryResourceInt(resourceTriples, getCommunityShortName());
	}
	
	private void ModifyPropertyLoadedSecondaryResourceInt(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
		int processedNumber = 0;
		int attempNumber = 0;
		ArrayList<String[]> valuesList = new ArrayList<String[]>();
		ArrayList<String> values = new ArrayList<String>();
		HashMap<String, ArrayList<TriplesToModify>> toModify = new HashMap<String, ArrayList<TriplesToModify>>(resourceTriples);
		while(toModify != null && toModify.size() > 0 && attempNumber < numAttemps){
			for(String secondaryEntityId : resourceTriples.keySet()){
				attempNumber++;
				processedNumber++;
				for(TriplesToModify mT : resourceTriples.get(secondaryEntityId)){
					String acido = "0";
					values = new ArrayList<String>();
					values.add(mT.getOldValue());
					values.add(mT.getPredicate());
					values.add(mT.getNewValue());
					values.add(acido);
					valuesList.add((String[])values.toArray());
				}
				try{
					String url = getApiUrl() + "/secondary-entity/modify-triple-list";
					ModifyTripleListModel model = new ModifyTripleListModel();
					model.setCommunity_short_name(communityShortName);
					model.setSecondary_ontology_url(_ontologyUrl);
					model.setSecondary_entity(secondaryEntityId);
					model.setTriple_list((String[][])valuesList.toArray());
					WebRequestPostWithJsonObject(url, model);
					
					valuesList = new ArrayList<String[]>();
					LogHelper.getInstance().Debug(processedNumber + " of " + resourceTriples.size() + " Object: " + secondaryEntityId + ". Resource: " + resourceTriples.get(secondaryEntityId).toArray());
					toModify.remove(secondaryEntityId);
				}
				catch (Exception ex){
					LogHelper.getInstance().Error("Resource " + secondaryEntityId + " : " + ex.getMessage());
					valuesList = new ArrayList<String[]>();
				}
			}
			LogHelper.getInstance().Debug("******************** Finished lap number: " + attempNumber);
		}
	}
	
	private void ModifyPropertyLoadedSecondaryResourceInt(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps){
		ModifyPropertyLoadedSecondaryResourceInt(resourceTriples, communityShortName, numAttemps, false);
	}
	
	private void ModifyPropertyLoadedSecondaryResourceInt(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, String communityShortName){
		ModifyPropertyLoadedSecondaryResourceInt(resourceTriples, communityShortName, 2, false);
	}
	
	private String LoadComplexSemanticResourceInt(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName) {
		return LoadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, communityShortName, null);
	}
	
	private String LoadComplexSemanticResourceInt(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName, String rdfsPath) {
		try {
			if (resource.getTextCategories() != null) {
				if (hierarquicalCategories) {
					if (StringUtils.isEmpty(communityShortName)) {
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					} 
					else {
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				} 
				else {
					if (StringUtils.isEmpty(communityShortName)) {
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					} 
					else {
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}

			String documentId = "";

			if (StringUtils.isEmpty(communityShortName)) {
				communityShortName = getCommunityShortName();
			}

			LoadResourceParams model = GetResourceModelOfComplexOntologyResource(communityShortName, resource, false, isLast);
			documentId = CreateComplexOntologyResource(model);
			resource.setUploaded(true);

			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle:" + resource.getTitle() + "\tResourceID: " + resource.getOntology().getResourceId());

			if (!StringUtils.isEmpty(rdfsPath)) {
				File directory = new File(rdfsPath + "/" + GetOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl()));
				if (!directory.exists()){
					directory.mkdir();
				}

				String rdfFile = rdfsPath + "/" + GetOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl()) + "/" + resource.getId() + ".rdf";
				File file = new File(rdfFile);
				if (!file.exists()){
					BufferedWriter bw = new BufferedWriter(new FileWriter(file));
					bw.write(resource.getStringRdfFile());
				}	
			}

			resource.setGnossId(documentId);
		} catch (GnossAPICategoryException gacex) {
			LogHelper.getInstance().Error("Error loading the resource: \tID: " + resource.getId() + ". Title: " + resource.getTitle() +". Message: " + gacex.getMessage());

		}
		catch (Exception ex) {
			LogHelper.getInstance().Error("Error loading the resource: \tID: " + resource.getId() + ". Title: " + resource.getTitle() +". Message: " + ex.getMessage());

		}
		return resource.getGnossId();
	}

	public static String GetOntologyNameWithOutExtensionFromUrlOntology(String urlOntology){
		return urlOntology.substring(urlOntology.lastIndexOf(StringDelimiters.Slash) + StringDelimiters.Slash.length()).replace(".owl", "");
	}

	private LoadResourceParams GetResourceModelOfComplexOntologyResource(String communityShortName, ComplexOntologyResource rec, boolean pCrearVersion, boolean pEsUltimo) throws IOException, GnossAPIException{
		LoadResourceParams model = new LoadResourceParams();
		model.setResource_id(rec.getShortGnossId());
		model.setCommunity_short_name(communityShortName);	
		model.setTitle(rec.getTitle());
		model.setDescription(rec.getDescription());
		model.setResource_type((short)TiposDocumentacion.ontology.getID());

		if(rec.getTags() != null){
			model.setTags(new ArrayList<String>(Arrays.asList(rec.getTags())));
		}

		if(pCrearVersion){
			model.create_version = pCrearVersion;
		}

		ArrayList<UUID> listaCategorias = new ArrayList<UUID>();
		if(rec.getCategoriesIds() != null){
			for(UUID categoria : rec.getCategoriesIds()){
				if(!listaCategorias.contains(categoria)){
					listaCategorias.add(categoria);
				}    			
			}
		}

		model.setCategories(listaCategorias);
		model.setResource_url(rec.getOntology().getOntologyUrl());
		model.setResource_file(rec.getRdFile());

		int i = 0;
		if(rec.getAttachedFilesName().size() > 0){
			model.setResource_attached_files(new ArrayList<SemanticAttachedResource>());
			for(String nombre : rec.getAttachedFilesName()){
				SemanticAttachedResource adjunto = new SemanticAttachedResource();
				adjunto.setFile_rdf_property(nombre);
				adjunto.setFile_property_type((short)rec.getAttachedFilesType().get(i).getID());
				adjunto.setRdf_attacherd_file(rec.getAttachedFiles().get(i));
				adjunto.setDelete_file(rec.getAttachedFiles().get(i).equals(null));
				i++;
				ArrayList<SemanticAttachedResource> listaAuxiliar = model.getResource_attached_files();
				listaAuxiliar.add(adjunto);
				model.setResource_attached_files(listaAuxiliar);
			}
		}

		model.setCreator_is_author(rec.getCreatorIsAuthor());
		model.setAuthors(rec.getAuthor());
		model.setAuto_tags_title_text(rec.getAutomaticTagsTextFromTitle());
		model.setAuto_tags_description_text(rec.getAutomaticTagsTextFromDescription());
		model.setCreate_screenshot(rec.getMustGenerateScreenshot());
		model.setUrl_screenshot(rec.getScreenshotUrl());
		model.setPredicate_screenshot(rec.getScreenshotPredicate());
		if (rec.getScreenshotSizes() != null)
		{
			model.setScreenshot_sizes(new ArrayList(Arrays.asList(rec.getScreenshotSizes())));
		}
		model.setEnd_of_load(pEsUltimo);

		model.setCreation_date(rec.getCreationDate());

		model.setPublisher_email(rec.getPublisherEmail());
		model.setPublish_home(rec.getPublishInHome());
		model.setLoad_id(getLoadIdentifier());
		model.setMain_image(rec.getMainImage());
		model.setVisibility((short)rec.getVisibility().getID());
		model.setEditor_list(rec.getEditorsGroups());
		model.setReader_list(rec.getReadersGroups());

		return model;
	}

	private ArrayList<UUID> GetHierarquicalCategoriesIdentifiersList(ArrayList<String> hierarquicalCategoriesList) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
		ArrayList<UUID> categories = null;

		if (hierarquicalCategoriesList != null && hierarquicalCategoriesList.size() > 0) {
			for (String cat : hierarquicalCategoriesList) {
				ThesaurusCategory category = FindHierarquicalCategoryNameInCategories(cat, CommunityApiWrapper.getCommunityCategories());
				if (category != null) {
					if (categories == null) {
						categories = new ArrayList<UUID>();
					}
					categories.add(category.getCategory_id());
				}
			}
		}

		if(hierarquicalCategoriesList == null || categories == null || hierarquicalCategoriesList.size() != categories.size()){
			throw new GnossAPICategoryException("Error obtaining the identifiers of one of the categories. It is possible that some of the introduced categories do not belong to the thesaurus");        	
		}

		return categories;
	}

	private ArrayList<UUID> GetHierarquicalCategoriesIdentifiersList(ArrayList<String> hierarquicalCategoriesList, String communityShortName) throws GnossAPICategoryException, MalformedURLException, IOException, GnossAPIException {
		ArrayList<UUID> resultList = null;

		ArrayList<ThesaurusCategory> categories = null;

		if(communityShortName.equals(getCommunityShortName())){
			categories = CommunityApiWrapper.getCommunityCategories();
		}
		else{
			categories = CommunityApiWrapper.LoadCategories(communityShortName);
		}

		if(hierarquicalCategoriesList != null && hierarquicalCategoriesList.size() > 0){
			for(String cat : hierarquicalCategoriesList){
				ThesaurusCategory category = FindHierarquicalCategoryNameInCategories(cat, categories);
				if(category != null){
					if(resultList == null){
						resultList = new ArrayList<UUID>();
					}
					resultList.add(category.getCategory_id());
				}
			}
		}

		if(hierarquicalCategoriesList == null || resultList == null || hierarquicalCategoriesList.size() != resultList.size()){
			throw new GnossAPICategoryException("Error obtaining the identifiers of one of the categories. It is possible that some of the introduced categories do not belong to the thesaurus");
		}

		return resultList;
	}

	private ArrayList<UUID> GetNotHierarquicalCategoriesIdentifiersList(ArrayList<String> notHierarquicalCategoriesList) throws GnossAPICategoryException, MalformedURLException, IOException, GnossAPIException{
		ArrayList<UUID> resultList = null;
		if(notHierarquicalCategoriesList != null && notHierarquicalCategoriesList.size() > 0){
			String[] categoryList = null;
			String[] separator = new String[]{ "|||" };

			for(ThesaurusCategory category : CommunityApiWrapper.getCommunityCategories()){
				if(!StringUtils.isEmpty(category.getCategory_name()) && category.getCategory_name().contains("|||")){
					categoryList = category.getCategory_name().split(separator.toString(), -1);
					for(int i = 0; i< categoryList.length; i++){
						String valor = categoryList[i].substring(0, categoryList[i].indexOf("@"));
						for(String cat : notHierarquicalCategoriesList){
							if(categoryList[i].substring(0, categoryList[i].indexOf("@")).equals(cat)){
								if(resultList == null){
									resultList = new ArrayList<UUID>();
								}
								if(!resultList.contains(category.getCategory_id())){
									resultList.add(category.getCategory_id());
								}
							}
							else{
								ThesaurusCategory thesaurusCategory = null; 
								for(ThesaurusCategory thesCat : CommunityApiWrapper.getCommunityCategories()){
									if(thesCat.getCategory_name().equals(cat)){
										thesaurusCategory = thesCat;    									
									}
								}
								if(thesaurusCategory != null){
									if(resultList == null){
										resultList = new ArrayList<UUID>();
									}
									if(!resultList.contains(thesaurusCategory.getCategory_id())){
										resultList.add(thesaurusCategory.getCategory_id());
									}
								}
							}
						}
					}
				}
				else{
					for(String cat : notHierarquicalCategoriesList){
						ThesaurusCategory thesaurusCategory = null;

						for(ThesaurusCategory thesCat : CommunityApiWrapper.getCommunityCategories()){
							if(thesCat.getCategory_name().equals(cat)){
								thesaurusCategory = thesCat;
							}
						}
						if(thesaurusCategory != null){
							if(resultList == null){
								resultList = new ArrayList<UUID>();
							}
							if(!resultList.contains(thesaurusCategory.getCategory_id())){
								resultList.add(thesaurusCategory.getCategory_id());
							}
						}
						else{
							resultList.add(UUID.fromString("00000000-0000-0000-0000-000000000000"));
							LogHelper.getInstance().Debug("The category " + cat + "not exists in the community");
						}
					}
				}
			}

			if(notHierarquicalCategoriesList == null || resultList == null || notHierarquicalCategoriesList.size() != resultList.size()){
				throw new GnossAPICategoryException("Error obtaining the identifiers of one of the categories. It is possible that some of the introduced categories do not belong to the thesaurus");
			}
		}

		return resultList;
	}

	private ArrayList<UUID> GetNotHierarquicalCategoriesIdentifiersList(ArrayList<String> notHierarquicalCategoriesList, String communityShortName) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException{
		ArrayList<ThesaurusCategory> categoryList = null;

		if(communityShortName.equals(getCommunityShortName())){
			categoryList = CommunityApiWrapper.getCommunityCategories();
		}
		else{
			categoryList = CommunityApiWrapper.LoadCategories(getCommunityShortName());
		}

		ArrayList<UUID> resultList = null;
		if(notHierarquicalCategoriesList != null && notHierarquicalCategoriesList.size() > 0){
			String[] categories = null;
			String[] separator = new String[] { "|||" };

			for(ThesaurusCategory category : categoryList){
				if(!StringUtils.isEmpty(category.getCategory_name()) && category.getCategory_name().contains("|||")){
					categories = category.getCategory_name().split(separator.toString(), -1);
					for(int i = 0; i < categories.length; i++){
						String value = categories[i].substring(0, categories[i].indexOf("@"));
						for(String cat : notHierarquicalCategoriesList){
							if(categories[i].substring(0, categories[i].indexOf("@")).equals(cat)){
								if(resultList == null){
									resultList = new ArrayList<UUID>();
								}
								if(!resultList.contains(category.getCategory_id())){
									resultList.add(category.getCategory_id());
								}
							}
							else{
								ThesaurusCategory thesaurusCategory = null;
								for(ThesaurusCategory thesCat : categoryList){
									if(thesCat.getCategory_name().equals(cat)){
										thesaurusCategory = thesCat;
									}
								}

								if(thesaurusCategory != null){
									if(resultList == null){
										resultList = new ArrayList<UUID>();
									}
									if(!resultList.contains(thesaurusCategory.getCategory_id())){
										resultList.add(thesaurusCategory.getCategory_id());
									}    								
								}
							}
						}
					}
				}
				else{
					for(String cat : notHierarquicalCategoriesList){

						ThesaurusCategory thesaurusCategory = null;
						for(ThesaurusCategory thesCat : categoryList){
							if(thesCat.getCategory_name().equals(cat)){
								thesaurusCategory = thesCat;
							}    					
						}

						if(thesaurusCategory != null){
							if(resultList == null){
								resultList = new ArrayList<UUID>();
							}
							if(!resultList.contains(thesaurusCategory.getCategory_id())){
								resultList.add(thesaurusCategory.getCategory_id());        						
							}
						}
					}
				}
			}
		}

		if(notHierarquicalCategoriesList == null || resultList == null || notHierarquicalCategoriesList.size() != resultList.size()){
			throw new GnossAPICategoryException("Error trying to get the categories identificators. It's possible that some categories not exists in the community");    		
		}

		return resultList;
	}


	public boolean CheckLoadIdentifier(String loadIdentifier) throws MalformedURLException, IOException, GnossAPIException{
		String url = getApiUrl() + "/community/get-responsible-load?community_short_name=" + getCommunityShortName() + "&load_id=" + loadIdentifier;

		DeveloperEmail = WebRequest("GET", url, "", "", "aplication/json");

		if(!DeveloperEmail.equals("")){
			LogHelper.getInstance().Info("Valid identifier " + getLoadIdentifier() + "for the community " + getCommunityShortName() + ". Developer: " + DeveloperEmail);
			_loadIdentifier = loadIdentifier;

			return true;
		}
		else{
			return false;
		}

	}

	private void LoadIdentifierGenerator() throws IOException, GnossAPIException{
		if(StringUtils.isEmpty(_loadIdentifier)){
			Date date = null;
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));			
			_loadIdentifier = getCommunityShortName() + "~" + cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "~" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
			RegisterLoadIdentifier(_loadIdentifier);
		}
	}

	private void GetGraphsUrl(){
		try{
			String url = getApiUrl() + "/ontology/get-graphs-url";
			
			GraphsUrl = WebRequest("GET", url, "", "", "application/json").replaceAll("\"", "");
			
			LogHelper.getInstance().Debug("The url of the graphs is: " + GraphsUrl);
		}
		catch(Exception ex){
			LogHelper.getInstance().Debug("Error obtaining the intragnoss URL: " + ex.getMessage());
		}
	}
	
	private void RegisterLoadIdentifier(String loadIdentifier) throws IOException, GnossAPIException{
		if(!StringUtils.isEmpty(loadIdentifier)){
			if(!StringUtils.isEmpty(DeveloperEmail)){
				String url = getApiUrl() + "/community/register-load";

				RegisterLoadModel model = new RegisterLoadModel();
				model.setLoad_id(loadIdentifier);
				model.setCommunity_short_name(getCommunityShortName());
				model.setEmail_responsible(DeveloperEmail);

				WebRequestPostWithJsonObject(url, model);

				_loadIdentifier = loadIdentifier;
				LogHelper.getInstance().Info("Your load identifier is: " + loadIdentifier + ".Developer: " + DeveloperEmail + ". Community: " + getCommunityShortName());
			}
			else{
				throw new GnossAPIArgumentException("The property DeveloperEmail cannot be nul or empty. Yoy must set it at ther ResourceApi constructor");
			}    		
		}
		else{
			throw new GnossAPIArgumentException("Required. LoadIdentifier can't be null or empty");
		}
	}

	private ThesaurusCategory FindHierarquicalCategoryNameInCategories(String hierarchicalName, ArrayList<ThesaurusCategory> categories){
		ThesaurusCategory resultCategory = null;

		String[] path = hierarchicalName.split("|");
		for(String category : path){
			if(category != null || !category.equals("")){
				if(resultCategory == null){
					for(ThesaurusCategory thesCat : categories){
						if(thesCat.getCategory_name().equals(category)){
							resultCategory = thesCat;
						}
					}
				}
				else{
					ArrayList<ThesaurusCategory> childrenList = resultCategory.getChildren();
					for(ThesaurusCategory thesCat : childrenList){
						if(thesCat.getCategory_name().equals(category)){
							resultCategory = thesCat;
						}
					}
				}

				if(resultCategory == null){
					return null;
				}
			}
		}

		return resultCategory;
	}
	
	/**
	 * Method for adding one or more properties of a loaded resource. In RemoveTriples can indicate whether title or description. 
	 * By default false two fields. It influences the value of the resource searches.
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a RemoveTriples list of the resource properties that will be deleted.
	 * @param numAttemps Indicates whether the home must be updated
	 * @param publishHome Default 2. Number of retries loading of the failed load of a resource
	 * @return HashMap resource identifier and boolean indicating the successfull, or not, result of the action
	 */
	public HashMap<UUID, Boolean> DeletePropertiesLoadedResources(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, int numAttemps, boolean publishHome){
		return DeletePropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}
	
	/**
	 * Method for adding one or more properties of a loaded resource. In RemoveTriples can indicate whether title or description. 
	 * By default false two fields. It influences the value of the resource searches.
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a RemoveTriples list of the resource properties that will be deleted.
	 * @param numAttemps Indicates whether the home must be updated
	 * @return HashMap resource identifier and boolean indicating the successfull, or not, result of the action
	 */
	public HashMap<UUID, Boolean> DeletePropertiesLoadedResources(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, int numAttemps){
		return DeletePropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	/**
	 * Method for adding one or more properties of a loaded resource. In RemoveTriples can indicate whether title or description. 
	 * By default false two fields. It influences the value of the resource searches.
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a RemoveTriples list of the resource properties that will be deleted.
	 * @return HashMap resource identifier and boolean indicating the successfull, or not, result of the action
	 */
	public HashMap<UUID, Boolean> DeletePropertiesLoadedResources(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples){
		return DeletePropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName());
	}

	private HashMap<UUID, Boolean> DeletePropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
		HashMap<UUID, Boolean> result = new HashMap<UUID, Boolean>();
		int processedNumber = 0;
		int attempNumber = 0;
		HashMap<UUID, ArrayList<RemoveTriples>> toDelete = new HashMap<UUID, ArrayList<RemoveTriples>>(resourceTriples);
		while(toDelete != null && toDelete.size() > 0 && attempNumber < numAttemps){
			int i = 0;
			int contResources = resourceTriples.keySet().size();
			for(UUID docID : resourceTriples.keySet()){
				i++;
				ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
				processedNumber++;
				attempNumber++;
				for(RemoveTriples iT : resourceTriples.get(docID)){
					ModifyResourceTriple triple = new ModifyResourceTriple();
					triple.setOld_object(iT.getValue());
					triple.setPredicate(iT.getPredicate());
					triple.setNew_object(null);
					triple.setGnoss_property(GnossResourceProperty.none);
					
					if(iT.isTitle()){
						triple.setGnoss_property(GnossResourceProperty.title);
					}
					else if(iT.isDescription()){
						triple.setGnoss_property(GnossResourceProperty.description);
					}
					
					listaValores.add(triple);
				}
				try{
					boolean endOfLoad = false;
					if(i == contResources){
						endOfLoad = true;
					}
					ModifyTripleList(docID, listaValores, getLoadIdentifier(), publishHome, null, null, endOfLoad);
					
					LogHelper.getInstance().Debug(processedNumber + " of " + resourceTriples.size() + " Object: " + docID + ". Resource: " + resourceTriples.get(docID).toArray());
					toDelete.remove(docID);
					
					result.put(docID, true);
				}
				catch (Exception ex){
					LogHelper.getInstance().Error("Resource " + docID + " : " + ex.getMessage());
					result.put(docID, false);
				}
			}
			LogHelper.getInstance().Debug("******************** Lap number: " + attempNumber + " finished");
		}
		
		return result;
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param rdfsPath Path to save the RDF, if necessary
	 */
	private void LoadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps, String communityShortName, String rdfsPath){
		int processedNumber = 0;
		boolean last = false;
		ArrayList<ComplexOntologyResource> originalResourceList = new ArrayList<ComplexOntologyResource>(resourceList);
		int attempNumber = 0;
		int resourcesToUpload = 0;
		for(ComplexOntologyResource resource : originalResourceList){
			if(!resource.isUploaded()){
				resourcesToUpload++;
			}
		}
		
		while(originalResourceList != null && originalResourceList.size() > 0 && resourcesToUpload > 0 && attempNumber < numAttemps){
			attempNumber++;
			int totalResources = originalResourceList.size();
			int resourcesLeft = originalResourceList.size();
			
			for(ComplexOntologyResource rec : originalResourceList){
				if(!rec.isUploaded()){
					processedNumber++;
					try{
						if(processedNumber == originalResourceList.size()){
							last = true;
						}
						if(StringUtils.isEmpty(rec.getOntology().getOntologyUrl())){
							rec.getOntology().setOntologyUrl(_ontologyUrl);
						}
						LoadComplexSemanticResourceInt(rec, hierarquicalCategories, last, numAttemps, communityShortName, rdfsPath);
						resourcesLeft--;
					}
					catch(Exception ex){
						LogHelper.getInstance().Error("ERROR at: " + processedNumber + " of " + originalResourceList.size() + "\tID: " + rec.getId() + ". Title: " + rec.getTitle() + ". Message: " + ex.getMessage());
					}
				}
				processedNumber = 0;
				for(ComplexOntologyResource resource : originalResourceList){
					if(!resource.isUploaded()){
						resourcesToUpload++;
					}
				}
			}
		}
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 */
	private void LoadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps, String communityShortName){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, communityShortName, null);
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 */
	private void LoadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, null, null);
	}
	
	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 */
	private void LoadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories){
		LoadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 2, null, null);
	}
	
	private void LoadComplexSemanticResourceListWithOntologyAndCommunityInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String ontology, String communityShortName, int numAttemps){
		int processedNumber = 0;
		boolean last = false;
		ArrayList<ComplexOntologyResource> originalResourceList = new ArrayList<ComplexOntologyResource>(resourceList);
		int attempNumber = 0;
		int resourcesToUpload = 0;
		for(ComplexOntologyResource resource : originalResourceList){
			if(!resource.isUploaded()){
				resourcesToUpload++;
			}
		}
		
		while(originalResourceList != null && originalResourceList.size() > 0 && resourcesToUpload > 0 && attempNumber < numAttemps){
			attempNumber++;
			int totalResources = originalResourceList.size();
			int resourcesLeft = originalResourceList.size();
			
			for(ComplexOntologyResource rec : originalResourceList){
				if(!rec.isUploaded()){
					processedNumber++;
					try{
						if(processedNumber == originalResourceList.size()){
							last = true;
						}
						if(StringUtils.isEmpty(rec.getOntology().getOntologyUrl())){
							rec.getOntology().setOntologyUrl(_ontologyUrl);
						}
						LoadComplexSemanticResourceWithOntologyAndCommunityInt(rec, hierarquicalCategories, last, ontology, communityShortName);
						resourcesLeft--;
					}
					catch(Exception ex){
						LogHelper.getInstance().Error("ERROR at: " + processedNumber + " of " + originalResourceList.size() + "\tID: " + rec.getId() + ". Title: " + rec.getTitle() + ". Message: " + ex.getMessage());
					}
				}
			}			
			processedNumber = 0;
			for(ComplexOntologyResource resource : originalResourceList){
				if(!resource.isUploaded()){
					resourcesToUpload++;
				}
			}
		}
	}
	
	private String LoadComplexSemanticResourceWithOntologyAndCommunityInt(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, String ontology, String communityShortName){
		try{
			if(resource.getTextCategories() != null){
				if(hierarquicalCategories){
					if(StringUtils.isEmpty(communityShortName)){
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
				else{
					if(StringUtils.isEmpty(communityShortName)){
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}
			
			String documentId = "";
			ontology = ontology.toLowerCase().replace(".owl", "");
			ontology = _ontologyUrl.replace(_ontologyUrl.substring(_ontologyUrl.lastIndexOf("/") + 1), ontology + ".owl");
			
			LoadResourceParams model = GetResourceModelOfComplexOntologyResource(communityShortName, resource, false, isLast);
			documentId = CreateComplexOntologyResource(model);
			resource.setUploaded(true);
			
			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle: " + resource.getTitle() + "\tResourceID: " + resource.getOntology().getResourceId());
			resource.setGnossId(documentId);
		}
		catch(Exception ex){
			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle: " + resource.getTitle() + "\tResourceID: " + resource.getOntology().getResourceId());
		}
		return resource.getGnossId();
	}
	
	private void LoadComplexSemanticResourceListWithOntologyAndCommunityInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String ontology, String communityShortName){
		LoadComplexSemanticResourceListWithOntologyAndCommunityInt(resourceList, hierarquicalCategories, ontology, communityShortName, 1);
	}
	
	
	private HashMap<UUID, Boolean> DeletePropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, String communityShortName, int numAttemps){
		return DeletePropertiesLoadedResourcesInt(resourceTriples, communityShortName, numAttemps, false);
	}

	private HashMap<UUID, Boolean> DeletePropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, String communityShortName){
		return DeletePropertiesLoadedResourcesInt(resourceTriples, communityShortName, 2, false);
	}
	
	
	public String getDeveloperEmail() {
		return DeveloperEmail;
	}

	public String getOntologyUrl(){
		return _ontologyUrl;
	}
	
	public void setOntologyUrl(String ontologyUrl){
		_ontologyUrl = ontologyUrl;
	}
	
	public void setDeveloperEmail(String DeveloperEmail) {
		this.DeveloperEmail = DeveloperEmail;
	}

	public String getOntologyName() {
		return OntologyName;
	}

	public void setOntologyName(String OntologyName) {
		this.OntologyName = OntologyName;
	}

	public String getGraphsUrl(){
		return GraphsUrl;
	}
	
	public void setGraphsUrl(String graphsUrl){
		GraphsUrl = graphsUrl;
	}
	
	public String getLoadIdentifier() throws IOException, GnossAPIException{
		if(_loadIdentifier == null){
			LoadIdentifierGenerator();
		}
		return _loadIdentifier;
	}

	public void setLoadIdentifier(String loadIdentifier) throws MalformedURLException, GnossAPIArgumentException, IOException, GnossAPIException{
		if(CheckLoadIdentifier(loadIdentifier)){
			_loadIdentifier = loadIdentifier;
		}
		else{
			throw new GnossAPIArgumentException("Invalid identifier " + loadIdentifier + "for the community" + getCommunityShortName() + ". The load identifier must be registered before.");
		}
	}

	public String getOntologyNameWithoutExtension() {
		return ontologyNameWithoutExtension;
	}

	public void setOntologyNameWithoutExtension(String ontologyNameWithoutExtension) {
		this.ontologyNameWithoutExtension = ontologyNameWithoutExtension;
	}

	public String getOntologyNameWithExtension() {
		return ontologyNameWithExtension;
	}

	public void setOntologyNameWithExtension(String ontologyNameWithExtension) {
		this.ontologyNameWithExtension = ontologyNameWithExtension;
	}

	public CommunityApi getCommunityApiWrapper() {
		if(this._communityApi==null) {
			this._communityApi= new CommunityApi(getOAuthInstance(), getCommunityShortName());
		}
		return this._communityApi;
	}

	public void setCommunityApiWrapper(CommunityApi communityApiWrapper) {
		CommunityApiWrapper = communityApiWrapper;
	}
	
	
	private HashMap<UUID, Boolean> ActionsOnPropertiesLoadedResourcesInt(HashMap<UUID, List<TriplesToModify>> resourceTriples, HashMap<UUID, List<RemoveTriples>> deleteList, HashMap<UUID, List<TriplesToInclude>> insertList, HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntitiesInsertTriplesList, String communityShortName, boolean publishHome){
		
		HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
		int processedNumber=0;
		int attempNumber=0;
		List<ModifyResourceTriple> valuesList = new ArrayList<ModifyResourceTriple>();
		List<String> values = new ArrayList<String> ();
		HashMap <UUID, List<ModifyResourceTriple>> resources = new HashMap<UUID, List<ModifyResourceTriple>>();
		
		
		//delete triples
		
		if(deleteList!=null) {
			HashMap<UUID, List<RemoveTriples>> toDelete = new HashMap<UUID, List<RemoveTriples>>(deleteList);
			while(toDelete!=null && toDelete.size()>0) {
				for(UUID docID  : deleteList.keySet()) {
					valuesList= new ArrayList<ModifyResourceTriple>();
					processedNumber++;
					attempNumber++;
					
					for(RemoveTriples iT :deleteList.get(docID)) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(iT.getValue());
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(null);
						triple.setGnoss_property(GnossResourceProperty.none);
						
						if(iT.isTitle()) {
							triple.setGnoss_property(GnossResourceProperty.none);
						}else if( iT.isDescription()) {
							triple.setGnoss_property(GnossResourceProperty.description);
						}
						valuesList.add(triple);
					}
					resources.put(docID, valuesList);
					toDelete.remove(docID);
					
				
				}
			}
		}
		
		//Modify triples
		if(resourceTriples!=null) {
			HashMap<UUID, List<TriplesToModify>> toModify = new HashMap<UUID, List<TriplesToModify>>(resourceTriples);
			while(toModify!=null && toModify.size()>0) {
				for (UUID docID : resourceTriples.keySet()) {
					valuesList=new ArrayList<ModifyResourceTriple>();
					
					if(resources.containsKey(docID)) {
						valuesList.addAll(resources.get(docID));
					}
					
					
				processedNumber++;
				attempNumber++;
				for(TriplesToModify mT : resourceTriples.get(docID)) {
					ModifyResourceTriple triple= new ModifyResourceTriple();
					triple.setOld_object(mT.getOldValue());
					triple.setPredicate(mT.getPredicate());
					triple.setNew_object(mT.getNewValue());
					triple.setGnoss_property(GnossResourceProperty.none);
					
					if(mT.isTitle()) {
						triple.setGnoss_property(GnossResourceProperty.title);
						
					}else if(mT.isDescription()) {
						triple.setGnoss_property(GnossResourceProperty.description);
					}
					
					valuesList.add(triple);
				}
				
				// IF exists, replace the value list. Else, add the value list
				
				if(resources.containsKey(docID)) {
					resources.get(docID).addAll(valuesList);
				}else {
					resources.put(docID, valuesList);
				}
				toModify.remove(docID);
			
				}
			}
		}
		
		//Insert Triples
		if(insertList!=null) {
			HashMap<UUID, List<TriplesToInclude>> toInsert = new HashMap<UUID, List<TriplesToInclude>>(insertList);
			while(toInsert!=null && toInsert.size()>0) {
				for(UUID docID : insertList.keySet()) {
					valuesList= new ArrayList<ModifyResourceTriple>();
					
					if(resources.containsKey(docID)) {
						valuesList.addAll(resources.get(docID));
					}
					processedNumber++;
					attempNumber++;
					
					for(TriplesToInclude iT : insertList.get(docID)) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(null);
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(iT.getNewValue());
						triple.setGnoss_property(GnossResourceProperty.none);
						
						if(iT.isTitle()) {
							triple.setGnoss_property(GnossResourceProperty.title);
						}
						else if(iT.isDescription()) {
							triple.setGnoss_property(GnossResourceProperty.description);
						}
						
						valuesList.add(triple);
					}
					if(resources.containsKey(docID)) {
						resources.get(docID).addAll(valuesList);
					}
					else {
						resources.put(docID, valuesList);
					}
					toInsert.remove(docID);
				}
			}
		}
		
		//Insert auxiliary entity triples
		
		if(auxiliaryEntitiesInsertTriplesList!=null) {
			HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntityTriplesToInsert= new HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>>(auxiliaryEntitiesInsertTriplesList);
			while(auxiliaryEntityTriplesToInsert!= null && auxiliaryEntityTriplesToInsert.size()>0) {
				
				for(UUID docID : auxiliaryEntitiesInsertTriplesList.keySet()) {
					
					valuesList= new ArrayList<ModifyResourceTriple>();
					
					
					if(resources.containsKey(docID)) {
						valuesList.addAll(resources.get(docID));
					}
					processedNumber++;
					attempNumber++;
					
					for(AuxiliaryEntitiesTriplesToInclude iT : auxiliaryEntitiesInsertTriplesList.get(docID)) {
						ModifyResourceTriple triple = new ModifyResourceTriple();
						triple.setOld_object(null);
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(GraphsUrl+"items/"+iT.getName()+"_"+docID+"_"+iT.getIdentifier()+"|"+iT.getValue());
						triple.setGnoss_property(GnossResourceProperty.none);
						
						valuesList.add(triple);
					}
					
					if(resources.containsKey(docID)) {
						resources.get(docID).addAll(valuesList);
					}else {
						resources.put(docID, valuesList);
					}
					auxiliaryEntityTriplesToInsert.remove(docID);
				}
			}
			
		}
		
		int i=0;
		int contResources= resources.keySet().size();
		
		for(UUID docID : resources.keySet()){
			i++;
			try {
				boolean endOfLoad=false;
				if(i==contResources) {
					endOfLoad=true;
				}
				
				ModifyTripleList(docID, valuesList, getLoadIdentifier(), publishHome, null, null, endOfLoad);
				valuesList= new ArrayList<ModifyResourceTriple>();
				
				this._logHelper.Debug("Object "+ docID);
				
				if(result.containsKey(docID)) {
					result.get(docID);
				}
				else {
					result.put(docID, true);
				}
			}
		
		catch(Exception ex) {
			this._logHelper.Error("Resource "+docID+ ": "+ex.getMessage());
			valuesList= new ArrayList<ModifyResourceTriple>();
			
			if(result.containsKey(docID)) {
				result.get(docID);
				}
			else {
				result.put(docID, false);
			}
			}
			
			
		}
		return result;
		
	}
	


	/**
	 * Deletes, modifies and inserts triples in an already loaded resources.It is used in the case in which, to the same resource, want to do more than one of these actions at once.
	 * @param resourceTriplesModify Contains as a key the resource guid identifier to modify and as a value a TriplesToModify list of the resource properties that will be modified
	 * @param resourceTriplesDelete Contains as a key the resource guid identifier to modify and as a value a RemoveTriples list of the resource properties that will be deleted
	 * @param resourceTriplesInsert Contains as a key the resource guid identifier to modify and as a value a TriplesToInclude list of the resource properties that will be inserted
	 * @param resourceTriplesAddAuxiliarEntity Contains as a key the resource guid identifier to modify and as a value a AuxiliaryEntitiesTriplesToInclude list of the resource properties that will be inserted
	 * @param publishHome Indicates whether the home must be updated
	 * @return Dictionary resource identifier and boolean indicating the successfull, or not, result of the action
	 */
	public HashMap<UUID, Boolean> ActionsOnPropertiesLoadedResources(HashMap<UUID, List<TriplesToModify>> resourceTriplesModify, HashMap<UUID, List<RemoveTriples>> resourceTriplesDelete, HashMap<UUID, List<TriplesToInclude>> resourceTriplesInsert, HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>> resourceTriplesAddAuxiliarEntity, boolean publishHome){
		return ActionsOnPropertiesLoadedResourcesInt(resourceTriplesModify, resourceTriplesDelete, resourceTriplesInsert, resourceTriplesAddAuxiliarEntity, getCommunityShortName(), false);
	}
	
	

	private void ModifyTripleList(UUID docID, List<ModifyResourceTriple> valuesList, String loadIdentifier,
			boolean publishHome, Object mainImage, Object resourceAttachedFiles, boolean endOfLoad) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	//region REST methods
	
	
	/**
	 * Method to know if there are pending resources in a community
	 * @param ontologyURL ontology URL 
	 * @return The number of pending actions in a community
	 * @throws Exception exception
	 */
	public int getPendingActions(String ontologyURL) throws Exception {
		try {
			String ontology=getOntologyUrl();
			if(this._ontologyUrl==null || this._ontologyUrl.isEmpty()) {
				ontology=GetOntologyNameWithOutExtensionFromUrlOntology(ontologyURL);
			}
			String url= getApiUrl()+"/resource/get-pending-actions?ontology_name="+ontology+"&community_short_name="+getCommunityShortName();
			String response=WebRequest("GET", url, "application/json");
			int pendingActions=Integer.parseInt(response);
			this._logHelper.Debug("The ontology "+ontology+ " has "+ pendingActions +" pending actions");
			return pendingActions;
		}catch(Exception ex) {
			this._logHelper.Error("There has been an error trying to know if there are outstanding resources "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets the community short name to which a resource belongs
	 * @param resourceID  Resource identifier
	 * @return Community short name 
	 * @throws Exception exception 
	 */
	public String getCommunityShortNameByResourceID(UUID resourceID) throws Exception {
		String communityShortName="";
		try {
			String url=getApiUrl()+"/resource/get-community-short-name-by-resource_id?resource_id="+resourceID;
			communityShortName=WebRequest("GET", url, "application/x-www-form-urlencoded");
			this._logHelper.Debug("The community short name for the resource "+resourceID+ " is "+communityShortName);
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the community short name "+ex.getMessage());
			throw ex;
		}
		return communityShortName;
	}
	
	
	/**
	 * Checks whether the user has permission on the resource editing
	 * @param resourceId resource identifier
	 * @param userId User identifier
	 * @return True if the user has editing permission on the resource. False if not.
	 * @throws Exception exception 
	 */
	public boolean HasUserEditingPermissionOnResourceByCommunityName(UUID resourceId, UUID userId) throws Exception {
		boolean result=false;
		if(!resourceId.equals("") && !userId.equals("")) {
			try {
				String url=getApiUrl()+"/resource/get-user-editing-permission-on-resource-by-community-name?resource_id="+resourceId+"&user_id="+userId+"&community_short_name="+getCommunityShortName();
				String response=WebRequest("GET", url);
				Gson gson = new Gson();
				result= gson.fromJson(response, Boolean.class); 
				
				if(result) {
					this._logHelper.Debug("The user "+userId+" is allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
					
				}else {
					this._logHelper.Debug("The user "+userId+" is not allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
				}
			}catch(Exception ex) {
				this._logHelper.Error(ex.getMessage());
				throw ex;
			}
		}else {
			this._logHelper.Error("Any of this are null or empty: resourceId, userId");
		}
		return result;
	}
	
	
	/**
	 * Checks whether the user has permission on the resource editing
	 * @param resourceId Resource identifier
	 * @param userId User identifier
	 * @param communityId Community identifier
	 * @return True if the user has editing permission on the resource. False if not.
	 * @throws Exception exception 
	 */
	public boolean HasUserEditingPermissionOnResourceByCommunityID(UUID resourceId, UUID userId, UUID communityId) throws Exception {
		boolean result=false;
		if(!resourceId.equals("") && !userId.equals("")) {
			try {
				String url=getApiUrl()+"/resource/get-user-editing-permission-on-resource?resource_id="+resourceId+"&user_id="+userId+"&community_id="+communityId;
				String response=WebRequest("GET", url);
				Gson gson = new Gson();
				result= gson.fromJson(response, Boolean.class); 
				
				if(result) {
					this._logHelper.Debug("The user "+userId+" is allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
				}else {
					this._logHelper.Debug("The user "+userId+" is not allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
				}
			}catch(Exception ex) {
				this._logHelper.Error(ex.getMessage());
				throw ex;
			}
		}else {
			this._logHelper.Error("Any of this are null or empty: resourceId, userId");
		}
		return result;
	}
	
	
	/**
	 * Gets the visibility of the resource
	 * @param resourceId Resource identifier
	 * @return esourceVisibility with the visibility of the resource. Null if it fails
	 * @throws Exception exception 
	 */
	public ResourceVisibility GetResourceVisibility (UUID resourceId) throws Exception {
		ResourceVisibility visibilidad= null;
		try {
			String url=getApiUrl()+"/resource/get-visibility?resource_id="+resourceId;
			String result= WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			visibilidad= gson.fromJson(result, ResourceVisibility.class);
			
			if(visibilidad==null) {
				this._logHelper.Error("Resource visibility not obtained "+resourceId);
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the visibility of "+resourceId+": "+ex.getMessage());
			throw ex;
		}
		return visibilidad;
	}
	
	
	
	public HashMap<UUID, List<UUID>> getRelatedResourcesFromList(List<UUID> resourceIds){
		HashMap<UUID, List<UUID>> listaIds = null;
		try {
			Object aux= new Object();
			{
				
				
				
			}
		}catch(Exception ex) {
			
		}
		return null;
	}
	
	
	/**
	 * Gets the related resources of a resource
	 * @param resourceId Resource identifier
	 * @return Related resources
	 * @throws Exception exception 
	 */
	public List<UUID> getRelatedResources(UUID resourceId) throws Exception{
		List<UUID> listaIds=null;
		try {
			String url=getApiUrl()+"/resource/get-related-resources?resource_id="+resourceId+"&community_short_name="+getCommunityShortName();
			String result=WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			listaIds= gson.fromJson(result, (Type) new ArrayList<UUID>());
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the related resources of "+resourceId+ ": "+ex.getMessage());
			throw ex;
		}
		return listaIds;
	}
	
	
	/**
	 * Gets the documents publisher by user
	 * @param userId User identifier
	 * @return List of community names
	 * @throws Exception exception
	 */
	public HashMap<String, List<UUID>> getDocumentsPublishedByUser(UUID userId) throws Exception{
		HashMap<String, List<UUID>> listaDocs=null;
		try {
			String url=getApiUrl()+"/resource/get-documents-published-by-user?user_id="+userId;
			String result=WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			listaDocs= gson.fromJson(result, (Type) new HashMap<String, List<UUID>>());
			this._logHelper.Debug("The user id "+userId+" publisher "+result);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the shared communities of  "+userId+ ": "+ex.getMessage());
			throw ex;
		}
		return  listaDocs;
	}
	
	
	/**
	 * 
	 * @param proyectoId proyecto id 
	 * @return String valor 
	 * @throws Exception exception
	 */
	public String ObtenerPathEstilos(UUID proyectoId) throws Exception {
		String valor= null;
		try {
			String url=getApiUrl()+"/resource/get-path-styles?id_proyecto="+proyectoId;
			String result=WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			valor= gson.fromJson(result, String.class);
			this._logHelper.Debug("The proyect id "+proyectoId+ " have styles path "+result);
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the shared communities of "+proyectoId+": "+ex.getMessage());
			throw ex;
		}
		return valor;
	}
	
	/**
	 * Gets the communities where a resource has been shared
	 * @param resourceId Resource identifier
	 * @return List of community names
	 * @throws Exception exception
	 */
	public List<String> getCommunitiesResourcesShared(UUID resourceId) throws Exception{
		List<String> communities=null;
		try {
			String url=getApiUrl()+"/resource/get-communities-resource-shared?resource_id="+resourceId;
			String result=WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			communities= gson.fromJson(result, (Type) new ArrayList<String>());
			this._logHelper.Debug("The resource "+resourceId+ " has been shared in "+result);
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the shared communities of "+resourceId+": "+ex.getMessage());
			throw ex;
		}
		return communities;
	}
	
	
	/**
	 * Gets the readers or the readers groups short name of the resource
	 * @param resourceId Resource identifier
	 * @return List of strings with the short names
	 * @throws Exception exception 
	 */
	public KeyReaders getResourceReaders(UUID resourceId) throws Exception {
		KeyReaders readers=null;
		try {
			String url=getApiUrl()+"/resource/get-resource-readers?resource_id="+resourceId;
			String response= WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			readers= gson.fromJson(response, KeyReaders.class);
			
			if(readers!=null) {
				this._logHelper.Debug("Resource readers of "+resourceId+": "+response);
			}else {
				this._logHelper.Error("Couldn´t get readers of:" +resourceId);
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the resources of "+resourceId+ ": "+ex.getMessage());
			throw ex;
		}
		return readers;
	}
	
	/**
	 * Unshare resource of a community
	 * @param resourceId Resource identifier
	 * @param communityShortName Community short name
	 * @return True if the resource has been unshared. False if not.
	 * @throws Exception exception 
	 */
	public boolean UnsharedCommunityResource(UUID resourceId, String communityShortName) throws Exception {
		boolean unshared=false;
		try {
			String url=getApiUrl()+"/resource/unshared-community-resource";
			UnsharedResourceParams parameters = new UnsharedResourceParams();
			{
				parameters.setResource_id(resourceId);
				parameters.setCommunity_short_name(communityShortName);
			}
			String response=WebRequestPostWithJsonObject(url, parameters);
			Gson gson = new Gson();
			unshared= gson.fromJson(response, Boolean.class);
			
			if(unshared) {
				this._logHelper.Debug("Resource "+resourceId+ " unshared from "+communityShortName);
			}else {
				this._logHelper.Debug("Resource "+resourceId+ " not unshared from "+communityShortName);
			}
		}
		catch(Exception ex) {
			this._logHelper.Error("Error trying to unsare the resource "+resourceId+" from "+communityShortName+": "+ex.getMessage());
			throw ex;
		}
		return unshared;
	}
	
	/**
	 * Inserts properties in Triples format in a loaded resource
     *   /// To create a new secondary entity, this properties must be sent: 
     *   /// .- Property of the parent resource
     *   /// .- rdfType
     *   /// .- rdfLabel
     *   /// .- The property hasEntidad of the secondaryEntity: subject = GraphsUrl + resource, predicate = http://gnoss/hasEntidad, and object = the property binded with the parent resource
     *   /// .- The properties of the secondary entities
	 * @param resourceId Resource identifier
	 * @param tripleList List of Triple
	 * @param publishHome True if this resource must appear in the community home
	 * @param communityShortName Community short name where the resource is published
	 * @return True if success
	 * @throws Exception exception 
	 */
	public boolean InsertPropertiesLoadedResource(UUID resourceId, List<Triple> tripleList, boolean publishHome, String communityShortName) throws Exception {
		boolean success=false;
		Triples triplesToInsert = new Triples(); {
			triplesToInsert.setResource_id(resourceId);
			triplesToInsert.setCommunity_short_name((communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) ? getCommunityShortName() : communityShortName);
			triplesToInsert.setPublish_home(publishHome);
			triplesToInsert.setTriples_list(tripleList);
			triplesToInsert.setEnd_of_load(true);
		}
		
		try {
			String url=getApiUrl()+"/resource/insert-props-loaded-resource";
			String response=WebRequestPostWithJsonObject(url, triplesToInsert);
			Gson gson = new Gson();
			success= gson.fromJson(response, Boolean.class);
			
			if(success) {
				this._logHelper.Debug("Triples inserted in "+resourceId+ ": "+"JsonConvert.SerializeObject(triplesToInsert");
			}else {
				this._logHelper.Debug("Triples not  inserted in "+resourceId+ ": "+"JsonConvert.SerializeObject(triplesToInsert");
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error trying to insert propertiess into "+resourceId+ ": "+ex.getMessage() +". \r\n :Json: JsonConvert.SerializeObject(triplesToInsert)");
			throw ex;
		}
		return success;
	}
	
	/**
	 * Delete a list of triples from a loaded resource
	 * @param resourceId Resource identifier
	 * @param tripleList List of Triple to delete
	 * @param publishHome  True if this resource appeared in the community home
	 * @param communityShortName Community short name where the resource is published
	 * @return True if success
	 * @throws Exception exception 
	 */
	public boolean DeletePropertiesLoadedResource (UUID resourceId, List<Triple> tripleList, boolean publishHome, String communityShortName) throws Exception {
		boolean success=false;
		Triples triplesToInsert= new Triples();
		{
			triplesToInsert.setResource_id(resourceId);
			triplesToInsert.setCommunity_short_name((communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) ? getCommunityShortName() : communityShortName);
			triplesToInsert.setPublish_home(publishHome);
			triplesToInsert.setTriples_list(tripleList);
			triplesToInsert.setEnd_of_load(true);
		}
		try {
			String url=getApiUrl()+"/resource/delete-props-loaded-resource";
			String response=WebRequestPostWithJsonObject(url, triplesToInsert);
			Gson gson = new Gson();
			success= gson.fromJson(response, Boolean.class);
			
			if(success) {
				this._logHelper.Debug("Triples deleted in "+resourceId+ ": "+"JsonConvert.SerializeObject(triplesToInsert");
			}else {
				this._logHelper.Debug("Triples not  deleted in "+resourceId+ ": "+"JsonConvert.SerializeObject(triplesToInsert");
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error trying to delete propertiess into "+resourceId+ ": "+ex.getMessage() +". \r\n :Json: JsonConvert.SerializeObject(triplesToInsert)");
			throw ex;
		}
		return success;
	}
	
	/**
	 * Gets the short names of resource editors and editors groups.
	 * @param resourceId_list resources identifiers list
	 * @return List with the short names of editors and editors groups
	 */
	public List<KeyEditors> getEditors(List<UUID> resourceId_list){
		List<KeyEditors> editorsList=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-editors";
			String response=WebRequestPostWithJsonObject(url, resourceId_list);
			Gson gson = new Gson();
			editorsList= gson.fromJson(response, (Type) new ArrayList<KeyEditors>());
			
			if(editorsList!=null && editorsList.size()>0) {
				this._logHelper.Debug("Editors of the resources "+gson1.toJson(resourceId_list)+": "+response);
			}
			else {
				this._logHelper.Debug("There is no Editors for the resources "+gson1.toJson(resourceId_list));
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the editors of the resources "+gson1.toJson(resourceId_list)+". Error description: "+ex.getMessage());
		}
		return editorsList;
	}
	
	
	/**
	 * Gets the resources download urls
	 * @param resourceId_list Resources identifiers list
	 * @return ResponseGetUrl list with the existent resources download urls
	 * @throws Exception exception 
	 */
	public List<ResponseGetUrl> getDownloadUrl(List<UUID> resourceId_list) throws Exception{
		List<ResponseGetUrl> urlList=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-download-url";
			GetDownloadParams parameters= new GetDownloadParams();
			{
				parameters.setResource_id_list(resourceId_list);
				parameters.setCommunity_short_name(getCommunityShortName());
				
			}
			String response= WebRequestPostWithJsonObject(url, parameters);
			Gson gson = new Gson();
			urlList= gson.fromJson(response, (Type) new ArrayList<ResponseGetUrl>());
			
			if(urlList!=null && urlList.size()==0) {
				this._logHelper.Debug("Downloads urls of the resources "+gson1.toJson(resourceId_list)+": "+response);
			}else {
				this._logHelper.Debug("There is no download  url for the resources "+gson1.toJson(resourceId_list));
			}
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the download urls  of the resources "+gson1.toJson(resourceId_list)+": Error description"+ex.getMessage());
			throw ex;
		}
		
		return urlList;
	}
	
	
	/**
	 * Gets the resources urls in the indicated language
	 * @param resourceId_list Resources identifiers list
	 * @param language language code string
	 * @return Resource ResponseGetUrl list with the existent resources urls
	 * @throws Exception exception 
	 */
	public List<ResponseGetUrl> getUrl(List<UUID> resourceId_list, String language) throws Exception{
		List<ResponseGetUrl> urlList=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-url";
			GetUrlParams parameters= new GetUrlParams();
			{
				parameters.setResource_id_list(resourceId_list);
				parameters.setCommunity_short_name(getCommunityShortName());
				parameters.setLanguage(language);
				
			}
			String response= WebRequestPostWithJsonObject(url, parameters);
			Gson gson = new Gson();
			urlList= gson.fromJson(response, (Type) new ArrayList<ResponseGetUrl>());
			
			if(urlList!=null && urlList.size()==0) {
				this._logHelper.Debug("Urls of the resources "+gson1.toJson(parameters)+": "+response);
			}else {
				this._logHelper.Debug("There is no urls for the resources "+gson1.toJson(parameters));
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the download urls  of the resources "+gson1.toJson(resourceId_list)+": Error description"+ex.getMessage());
			throw ex;
		}
		
		return urlList;
	}
	
	
	/**
	 * Sets the readers of the resource
	 * @param resourceId Resource identifier
	 * @param visibility Resource visibility
	 * @param readers_list Resource readers 
	 * @param publishHome Indicates whether the home must be updated
	 * @throws Exception exception 
	 */
	public void setReaders(UUID resourceId, ResourceVisibility visibility, List<ReaderEditor> readers_list, boolean publishHome) throws Exception {
		SetReadersEditorsParams readers=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/set-readers";
			readers= new SetReadersEditorsParams();
			{
				readers.setResource_id(resourceId);
				readers.setCommunity_short_name(getCommunityShortName());
				readers.setPublish_home(publishHome);
				readers.setReaders_list(readers_list);
				readers.setVisibility( Integer.parseInt(visibility.toString()));
			}
			WebRequestPostWithJsonObject(url, readers);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description"+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Adds the readers of the resuorce
	 * @param resourceId Resource identifier
	 * @param readers_list Resource readers
	 * @throws Exception exception 
	 */
	public void AddReaders(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
		SetReadersEditorsParams readers=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/add-readers";
			readers= new SetReadersEditorsParams();
			{
				readers.setResource_id(resourceId);
				readers.setCommunity_short_name(getCommunityShortName());
				
				readers.setReaders_list(readers_list);
				
			}
			WebRequestPostWithJsonObject(url, readers);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description"+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Remove the readers of the resource
	 * @param resourceId Resorce identifier
	 * @param readers_list Resource readers
	 * @throws Exception exception
	 */
	public void RemoveReaders(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
		SetReadersEditorsParams readers=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/remove-readers";
			readers= new SetReadersEditorsParams();
			{
				readers.setResource_id(resourceId);
				readers.setCommunity_short_name(getCommunityShortName());
				
				readers.setReaders_list(readers_list);
				
			}
			WebRequestPostWithJsonObject(url, readers);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description"+ex.getMessage());
			throw ex;
		
		}
		}
	
	/**
	 * Sets the readers of the resource
	 * @param resourceId Resource identifier
	 * @param readers_list Resource visibility
	 * @param visibility Resource readers
	 * @param publishHome indicates whether the home must be update
	 * @throws Exception exception 
	 */
	public void SetEditors(UUID resourceId, List<ReaderEditor> readers_list, ResourceVisibility visibility, boolean publishHome) throws Exception {
		SetReadersEditorsParams readers=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/set-editors";
			readers= new SetReadersEditorsParams();
			{
				readers.setResource_id(resourceId);
				readers.setCommunity_short_name(getCommunityShortName());
				readers.setPublish_home(publishHome);
				readers.setReaders_list(readers_list);
				readers.setVisibility( Integer.parseInt(visibility.toString()));
				
			}
			WebRequestPostWithJsonObject(url, readers);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description"+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Add the readers of the resources
	 * @param resourceId Resource identifier
	 * @param readers_list Resource readers
	 * @throws Exception exception 
	 */
	public void AddEditors(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
		SetReadersEditorsParams readers=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/add-editors";
			readers= new SetReadersEditorsParams();
			{
				readers.setResource_id(resourceId);
				readers.setCommunity_short_name(getCommunityShortName());
			
				readers.setReaders_list(readers_list);
				
				
			}
			WebRequestPostWithJsonObject(url, readers);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description"+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Remove readers of the resources
	 * @param resourceId resource id
	 * @param readers_list resource readers
	 * @throws Exception exception 
	 */
	public void RemoveEditors(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
		SetReadersEditorsParams readers=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/remove-editors";
			readers= new SetReadersEditorsParams();
			{
				readers.setResource_id(resourceId);
				readers.setCommunity_short_name(getCommunityShortName());
				
				readers.setReaders_list(readers_list);
				
				
			}
			WebRequestPostWithJsonObject(url, readers);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description"+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Insert or update Votes for document 
	 * @param pIdentidadID Identidad Id
	 * @param pValorVoto Valor voto
	 * @param pDocumentoID Documento ID
	 * @param pProyectoID proyecto ID
	 * @throws Exception exception 
	 */
	public void VoteDocument(UUID pIdentidadID, float pValorVoto, UUID pDocumentoID, UUID pProyectoID) throws Exception {
		VotedParameters vote=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/add-editors";
			vote= new VotedParameters();
			{
				vote.setUser_id(pIdentidadID);
				vote.setVote_value(pValorVoto);
				vote.setResource_id(pDocumentoID);
				vote.setProject_id(pProyectoID);
				
			}
			WebRequestPostWithJsonObject(url, vote);
			this._logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			this._logHelper.Error("Error setting resource "+pDocumentoID+" readers. \r\n json:" +gson1.toJson(vote)+": Error description"+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets the email of the resources creator 
	 * @param resourceId_list Resources identifier list
	 * @return ResponseGetCreatorEmail list with the email of the resources creators
	 * @throws Exception exception 
	 */
	public HashMap<UUID, String> getCreatorEmail(List<UUID> resourceId_list) throws Exception{
		GetDownloadParams model= new GetDownloadParams();
		model.setCommunity_short_name(getCommunityShortName());
		model.setResource_id_list(resourceId_list);
		Gson gson1 = new Gson();
		HashMap<UUID, String> emailsList= null;
		try {
			String url=getApiUrl()+"/resource/get-creator-email";
			String response=WebRequestPostWithJsonObject(url, model);
			Gson gson = new Gson();
			emailsList= gson.fromJson(response, (Type) new HashMap<UUID, String>());
			
			if(emailsList!=null && emailsList.size()==0) {
				this._logHelper.Debug("Urls of resources "+gson1.toJson(resourceId_list)+": "+response);
			}
			else {
				this._logHelper.Debug("There is no Urls of resources "+gson1.toJson(resourceId_list));
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the Urls of resources "+gson1.toJson(resourceId_list)+": "+ex.getMessage());
			throw ex;
		}
		return emailsList;
		
	}
	
	
	/**
	 * Gets the categories of the resources 
	 * @param resourceId_list Resources identifiers list
	 * @return ResponseCategories list with the category identifiers of the existent resources
	 */
	public List<ResponseGetCategories> getCategories(List<UUID> resourceId_list){
		GetDownloadParams parameters = new GetDownloadParams();
		Gson gson1 = new Gson();
		{
			parameters.setCommunity_short_name(getCommunityShortName());
			parameters.setResource_id_list(resourceId_list);
		}
		List<ResponseGetCategories> categoriesList= null;
		try {
			String url=getApiUrl()+"/resource/get-categories";
			String response=WebRequestPostWithJsonObject(url, parameters);
			Gson gson = new Gson();
			categoriesList= gson.fromJson(response, (Type) new ArrayList<ResponseGetCategories>());
			
			if(categoriesList!=null && categoriesList.size()==resourceId_list.size()) {
				this._logHelper.Debug("Categories of resources "+gson1.toJson(resourceId_list)+": "+response);
			}else {
				List<UUID> listaRecursosSinCategoria=null;
				int i=0;
				for(UUID cat :resourceId_list) {
					for (ResponseGetCategories res : categoriesList) {
						if(cat==res.getResource_id()) {
							i++;
						}
					}
				}
				if(i==0) {
					 listaRecursosSinCategoria=resourceId_list;
				}
				this._logHelper.Debug("There is no categories for resources "+gson1.toJson(listaRecursosSinCategoria));
				
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the categories of resources: "+gson1.toJson(resourceId_list)+". Error descripcion "+ex.getMessage());
		}
		return categoriesList;
	}
	
	
	/**
	 * Get the tags of the resources
	 * @param   resourceId_list resource Id List
	 * @return ResponseGetsTags list with the tags of the resources 
	 * @throws Exception exception
	 */
	public List<ResponseGetTags> getTags(List<UUID> resourceId_list) throws Exception{
		List<ResponseGetTags> tagsList =null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-tags";
			String response = WebRequestPostWithJsonObject(url, resourceId_list);
			
			tagsList= gson.fromJson(response, (Type) new ArrayList<ResponseGetTags>());
			
			if(tagsList!=null && tagsList.size()==resourceId_list.size()) {
				this._logHelper.Debug("Tags of resources "+ gson.toJson(resourceId_list)+ ":"+ response);
			}
			else {
				List<UUID> listaRecursosSinTags=null;
				int i=0;
				for(UUID cat :resourceId_list) {
					for (ResponseGetTags res : tagsList) {
						if(cat==res.getResource_id()) {
							i++;
						}
					}
				}
				if(i==0) {
					listaRecursosSinTags=resourceId_list;
				}
				this._logHelper.Debug("There is no tags for resources "+gson.toJson(resourceId_list));
				
			}
			}catch(Exception ex) {
				this._logHelper.Error("Error getting the tags of resources :"+gson.toJson(resourceId_list)+" Error description "+ex.getMessage());
				throw ex;
			}
		return tagsList;
		}
	
	
	/**
	 * Gets the main image of the resources 
	 * @param resourceId_list Resources identifiers list
	 * @return ResponseGetMainImage list with the path of the main image of the resources and their available sizes
	 * @throws Exception exception 
	 */
	public List<ResponseGetMainImage> getMainImage(List<UUID> resourceId_list) throws Exception{
		List<ResponseGetMainImage> mainImagesList=null;
		Gson gson = new Gson();
		
		try {
			String url=getApiUrl()+"/resource/get-main-image";
			String response=WebRequestPostWithJsonObject(url, resourceId_list);
			mainImagesList=gson.fromJson(response, (Type) new  ArrayList<ResponseGetMainImage>());
			
			if(mainImagesList!=null && mainImagesList.size()==0) {
				this._logHelper.Debug("Main images of resources "+gson.toJson(resourceId_list)+":"+response);
				
			}else {
				this._logHelper.Debug("There is no main images for resources: "+gson.toJson(resourceId_list));
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting thhe main images of resources: "+ gson.toJson(resourceId_list)+". Error description: " +ex.getMessage());
			throw ex;
		}
		return mainImagesList;
	}
	
	
	/**
	 * Gets the resource novelties in the community from the search date
	 * @param resourceId_list Resources id list
	 * @return Get resource/get-increased-reading-by-resource
	 * @throws Exception exception 
	 */
	public HashMap<UUID, AumentedReading> getIncreasedReading(List<UUID> resourceId_list) throws Exception{
		HashMap<UUID, AumentedReading> resource=null;
		Gson gson = new Gson();
		
		try {
			String url=getApiUrl()+"/resource/get-increased-reading-by-resources";
			String response= WebRequestPostWithJsonObject(url, resourceId_list);
			resource= gson.fromJson(response, (Type) new HashMap<UUID, AumentedReading>());
			
			if(resource!=null) {
				this._logHelper.Debug("Increased reading obtained");
			}else {
				this._logHelper.Debug("Error getting increased reading");
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the increades reading "+ex.getMessage());
			throw ex;
		}
		return resource;
	}
	
	
	public Resource getResource(UUID resourceId, String pCommunityShortName) throws Exception {
		Resource resource=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-resource?resource_id="+resourceId+"&community_short_name="+URLEncoder.encode(pCommunityShortName);
			String response=WebRequest("GET", url, "application/x-www-form-urlencoded");
			resource=gson.fromJson(response, Resource.class);
			
			if(resource!=null) {
				this._logHelper.Debug("Resource "+resourceId+" obtained");
				
			}else {
				this._logHelper.Debug("Error getting the resource "+resourceId);
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the resource "+resourceId+", "+ex.getMessage());
			throw ex;
		}
		return resource;
	}
	
	/**
	 * Gets the rdf of the complex semanthic resource
	 * @param resourceId Resource identifier
	 * @return String with the rdf of the resource
	 * @throws Exception exception 
	 */
	public String getRDF(UUID resourceId) throws Exception {
		String rdf="";
		try {
			String url=getApiUrl()+"/resource/get-rdf?resource_id="+resourceId;
			rdf=WebRequest("GET", url, "application/json");
			
			if(!StringUtils.isBlank(rdf) || !rdf.isEmpty()) {
				this._logHelper.Debug("Rdf obtained for the resource "+resourceId);
			}else {
				this._logHelper.Debug("There is no rdf for the resource "+resourceId);
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the rdf for the resource "+resourceId);
			throw ex;
		}
		return rdf;
	}
	
	/**
	 * Inserts the value in the graph
	 * @param graph Graph identifier 
	 * @param value Value to insert in the graph
	 * @throws Exception exception 
	 */
	public void InsertAttribute(String graph, String value) throws Exception {
		InsertAttributeParams insertAttribute=null;
		
		try {
			String url=getApiUrl()+"/resource/insert-attribute";
			insertAttribute= new InsertAttributeParams();
			insertAttribute.setGraph(graph);
			insertAttribute.setValue(value);
			WebRequestPostWithJsonObject(url, insertAttribute);
			
			this._logHelper.Debug("Ended inserting the value: "+value+" in the graph: "+graph);
		
		}catch(Exception ex) {
			this._logHelper.Error("Error inserting value:"+value+" in the graph "+graph+". Error description: "+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Checks whether the url exists in a resource of the community. (Searchs on the resource description)
	 * @param url link to search in the community
	 * @return True if the link exists in a resource of the community
	 * @throws Exception exception 
	 */
	public boolean ExistsUrl (String url) throws Exception{
		boolean exists=false;
		ExistsUrlParams model=null;
		Gson gson = new Gson();
		try {
			String urlMethod=getApiUrl()+"/resource/exists-url";
			model=new ExistsUrlParams();
			model.setCommunity_short_name(getCommunityShortName());
			model.setUrl(url);
			
			String response=WebRequestPostWithJsonObject(urlMethod, model);
			exists=gson.fromJson(response, Boolean.class);
			this._logHelper.Debug("Ended resource persistent deleting");
			
			
		}
		catch(Exception ex) {
			this._logHelper.Error("Error on a searching of an url. \r\n:  Json "+gson.toJson(model)+", "+ ex.getMessage());
			throw ex;
			
		}
		return exists;
	}
	
	/**
	 * Link the resources in the list to another resource
	 * @param resourceId Resource that has been linked
	 * @param resourceListToLink Resource list to link 
	 * @return boolean T or F 
	 * @throws Exception exception 
	 */
	public boolean LinkResource(UUID resourceId, ArrayList<UUID> resourceListToLink) throws Exception {
		boolean loaded=false;
		LinkedParams model=null;
		Gson gson = new Gson();
		try {
			
			String url=getApiUrl()+"/resource/link-resource";
			model= new LinkedParams();
			model.setResource_id(resourceId);
			model.setResource_list_to_link(resourceListToLink);
			
			
			WebRequestPostWithJsonObject(url, model);
			loaded=true;
			this._logHelper.Debug("Ended link resources");
		}
		catch(Exception ex) {
			this._logHelper.Error("Error linked resource "+resourceId+". \r\n Json: "+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
		return loaded;
	}
	
	
	/**
	 * Shares the resource in the target community
	 * @param targetCommunity target community short name string
	 * @param resourceId resource identifier Guid
	 * @param categories guid list where the document is going to be shared to
	 * @param publisher_email publisher email
	 * @return boolean T or F 
	 * @throws Exception exception 
	 */
	public boolean Share(String targetCommunity, UUID resourceId, ArrayList<UUID> categories, String publisher_email) throws Exception {
		boolean shared=false;
		ShareParams model=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/share";
			model= new ShareParams();
			model.setDestination_community_short_name(targetCommunity);
			model.setResource_id(resourceId);
			model.setCategories(categories);
			model.setPublisher_email(publisher_email);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Ended resource sharing");
			shared=true;
		}
		catch(Exception ex) {
			this._logHelper.Error("Error sharing resource "+resourceId+ ". \r\n: Json:"+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
		return shared;
	}
	
	
	/**
	 * Shares the resources 
	 * @param parameters List of shareParams model 
	 * @return boolean  T or F 
	 * @throws Exception Exception 
	 */
	public boolean ShareResources(List<ShareParams> parameters) throws Exception {
		boolean shared=false;
		Gson gson = new Gson();
		
		try {
			String url=getApiUrl()+"/resource/share-resources";
			WebRequestPostWithJsonObject(url, parameters);
			this._logHelper.Debug("Ended resource sharing");
			shared=true;
		}
		catch(Exception ex) {
			this._logHelper.Error("Error sharing resources. \r\n: Json: "+gson.toJson(parameters));
			throw ex;
		}
		
		return shared;
	}
	
	/**
	 * Sets the resource main image
	 * @param resourceId Resource identifier UUID
	 * @param path Relative path with the image name, image sizes available and [IMGPrincipal] mask 
	 * @return boolean T or F 
	 * @throws Exception Exception 
	 */
	public boolean setMainImage(UUID resourceId, String path) throws Exception {
		boolean setted=false;
		SetMainImageParams model=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/set-main-image";
			model= new SetMainImageParams();
			model.setCommunity_short_name(getCommunityShortName());
			model.setResource_id(resourceId);
			model.setPath(path);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Ended resource main image setting");
			setted=true;
		}
		catch(Exception ex) {
			this._logHelper.Error("Error setting resource main image "+resourceId+". \r\n: Json: "+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
		return setted;
	}
	

	/**
	 * Adds a comment in a resource. It can be a response of another parent comment.
	 * @param resourceId resource identifier
	 * @param userShortName publish date of the comment
	 * @param description Html content of the comment wrapped in a Html paragraph and special characters encoded in ANSI. Example: <p>Descripci&amp;oacute;n del comentario</p> string
	 * @param parentCommentId optional parent comment identifier Guid. The current comment is its answer
	 * @param commentDate  publisher user short name
	 * @param publishHome indicates whether the home must be updated
	 * @return UUID UUID ID 
	 * @throws Exception exception 
	 */
	public UUID Comment (UUID resourceId, String userShortName, String description, UUID parentCommentId, Date commentDate, boolean publishHome) throws Exception {
		UUID commendId=UUID.fromString("");
		CommentParams model=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/comment";
			model= new CommentParams();
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setUser_short_name(userShortName);
			model.setHtml_description(description);
			model.setComment_date(commentDate);
			model.setParent_comment_id(parentCommentId);
			model.setPusblish_home(publishHome);
			String response=WebRequestPostWithJsonObject(url, model);
			
			UUID comentId=UUID.fromString(response);
			if(comentId!=null) {
				this._logHelper.Debug("Ended resource "+resourceId+" comment: "+comentId);
			}else {
				this._logHelper.Debug("Error comenting resource "+resourceId);
			}
			return comentId;
		}catch(Exception ex) {
			this._logHelper.Error("Error comenting resource "+resourceId+". \r\n: Json: "+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
		
	}
	
	
	/**
	 * Creates a complex ontology resource
	 * @param parameters parameters
	 * @return resource identifier guid 
	 * @throws Exception exception 
	 */
	public String  CreateBasicOntologyResource(LoadResourceParams parameters) throws Exception {
		String resourceId="";
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/create-basic-ontology-resource";
			resourceId=WebRequestPostWithJsonObject(url, parameters);
			
			if(!resourceId.isEmpty() || !StringUtils.isBlank(resourceId)) {
				this._logHelper.Debug("Basic ontology resource created: "+resourceId);
			}
			else {
				this._logHelper.Debug("Basic ontology resource not created: "+gson.toJson(parameters));
			}
		}
		catch(Exception ex) {
			this._logHelper.Error("Error trying to create a basic ontology resource. \r\n Error description: "+ex.getMessage()+ ".\r\n Json: "+gson.toJson(parameters));
			throw ex;
		}
		return resourceId;
	}
	
	
	/**
	 * Creates a complex Ontology resource 
	 * @param parameters parameters
	 * @param pCargaID cargaId
	 * @param hierarquicalCategories hierarquical categories
	 * @param communityShortName community short name 
	 * @return Resource identifier UUID
	 * @throws MalformedURLException Mal Formed Exception 
	 * @throws IOException IO Exception
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public String MassiveComplexOntologyResourceCreation(List<ComplexOntologyResource> parameters, UUID pCargaID, boolean hierarquicalCategories, String communityShortName) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
		List<LoadResourceParams> listaLoadResourceParams = new ArrayList<LoadResourceParams>();
		Gson gson = new Gson();
		
		for(ComplexOntologyResource resource : parameters) {
			if(resource.getTextCategories()!=null) {
				if(hierarquicalCategories) {
					if(communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) {
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}else {
						resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}else {
					if(communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) {
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}else {
						resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}
			String documentId="";
			
			if(communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) {
				communityShortName=getCommunityShortName();
			}
			LoadResourceParams resourceParam=GetResourceModelOfComplexOntologyResource(communityShortName, resource, false, false);
			listaLoadResourceParams.add(resourceParam);
			resource.setUploaded(true);
		}
		
		MassiveResourceLoad massiveLoad= new MassiveResourceLoad();
		massiveLoad.setResources(listaLoadResourceParams);
		massiveLoad.setLoad_id(pCargaID);
		String resourceId="";
		
		try {
			String url=getApiUrl()+"/resource/massive-complex-ontology-resource-creation";
			WebRequestPostWithJsonObject(url, massiveLoad);
			
			if(!StringUtils.isBlank(resourceId) || !resourceId.isEmpty()) {
				this._logHelper.Debug("Complex ontology resource created: "+resourceId);
			}else {
				this._logHelper.Debug("Massive Complex ontology resource not created: "+gson.toJson(massiveLoad));
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error trying to create a Massive complex ontology resource. \r\n Error description: "+ex.getMessage()+". \r\n Json: "+gson.toJson(massiveLoad));
			throw ex;
		}
		
		return resourceId;
	}
	
	/**
	 * Modifies a basic ontology resource 
	 * @param parameters parameters 
	 * @return Resource identifier UUID 
	 * @throws Exception exception
	 */
	public boolean ModifyBasicOntologyResource(LoadResourceParams parameters) throws Exception {
		boolean modified=false;
		Gson gson = new Gson();
		 try {
			 String url=getApiUrl()+"/resource/modify-basic-ontology-resource";
			 String response=WebRequestPostWithJsonObject(url, parameters);
			 
			 this._logHelper.Debug("Ended resource main image setting");
			 modified=gson.fromJson(response, Boolean.class);
			 
		 }catch(Exception ex) {
			 this._logHelper.Error("Error modifying resource "+parameters.resource_id +". \r\n Json: "+gson.toJson(parameters)+", "+ex.getMessage());
			 throw ex; 
		 }
		return modified;
	}
	
	/**
	 * Method to add / modify / delete triples of complex ontology resource
	 * @param resourceId resource identifier guid
	 * @param tripleList resource triples list to modify
	 * @param loadId charge identifier string 
	 * @param publishHome indicates whether the home must be updated
	 * @param mainImage main image string
	 * @param resourceAttachedFiles resource attached files list
	 * @param endOfLoadindicates the resource modified is the last and it must deletes cache
	 * @param userId User that try to modify the resource
	 * @throws Exception Exception 
	 */
	public void ModifyTripleList (UUID resourceId, ArrayList<ModifyResourceTriple> tripleList, String loadId, boolean publishHome, String mainImage, ArrayList<SemanticAttachedResource> resourceAttachedFiles, boolean endOfLoad, UUID userId) throws Exception {
		
		ModifyResourceTripleListParams model=null;
		Gson gson = new Gson();
		
		try {
			String url=getApiUrl()+"/resource/modify-triple-list";
			model= new ModifyResourceTripleListParams();
			model.setResource_triples(tripleList);
			model.setCharge_id(loadId);
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setPublish_home(publishHome);
			model.setMain_image(mainImage);
			model.setResource_attached_files(resourceAttachedFiles);
			model.setEnd_of_load(endOfLoad);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Ended resource triples list modification");
			
			
		}catch(Exception ex) {
			PrepareAttachedToLog(model.getResource_attached_files());
			this._logHelper.Error("Error modifying resource triples list. \r\n: Json: "+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Modify a big list of triples
	 * @param parameters Parameters for the modification
	 * @throws Exception exception 
	 */
	public void MasiveTripleModify(MassiveTripleModifyParameters parameters) throws Exception {
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/masive-triple-modify";
			WebRequestPostWithJsonObject(url, parameters);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error modifying massive triples. \r\n: Json: "+gson.toJson(parameters)+", "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Locks a resource for editing for 60 seconds. Just this ResourceApi object can modify the resource from this moment. 
     * Don't forget to create a try-finally block, making a request to UnlockResource in the finally clause
	 * @param resourceId Resource identifier to lock
	 * @param secondsTimeout Timeout to wait a resource lock, in seconds (60 seconds by default)
	 * @param secondsLockDuration Max number of seconds a resource will be locked if isn't unlocked before (60 seconds by default)
	 * @throws Exception exception 
	 */
	public void LockResource(UUID resourceId, int secondsTimeout, int secondsLockDuration) throws Exception {
		String url=getApiUrl()+"/resource/lock-document?community_short_name="+getCommunityShortName()+"&resource_id="+resourceId+"&lock_seconds_duration="+secondsLockDuration+"&timeout_seconds="+secondsTimeout;
		Gson gson = new Gson();
		try {
			String token=WebRequest("POST", url);
			token=gson.fromJson(token, String.class);
			SetLockTokenForResource(resourceId, token);
		}catch(Exception ex) {
			this._logHelper.Error("Error, the document "+resourceId+" can´t be locked. "+ ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Unlocks a resource previously locked. 
     * This method must be called from a finally clause, in order to be sure you unlock a locked resource if something goes wrong
	 * @param resourceId Resource identifier to unlock
	 * @throws Exception exception 
	 */
	public void UnlockResource (UUID resourceId) throws Exception {
		String token=GetLockTokenForResource(resourceId);
		String url= getApiUrl()+"/resource/unlock-document?community_short_name="+getCommunityShortName()+"&resource_id="+resourceId+"&token="+token;
		
		try {
			WebRequest("POST", url);
			SetLockTokenForResource(resourceId, null);
		}catch(Exception ex) {
			this._logHelper.Error("Error, the document "+resourceId+ " can´t be unlocked. "+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Checks if a resource has been previously locked. 
	 * @param resourceId Resource identifier to verify
	 * @return boolean  T or F 
	 * @throws Exception exception 
	 */
	public boolean CheckLockedResource (UUID resourceId) throws Exception {
		String token=GetLockTokenForResource(resourceId);
		String url=getApiUrl()+"/resource/check-document-is-locked?resource_id="+resourceId;
		Gson gson = new Gson();
		
		try {
			return gson.fromJson(WebRequest("GET", url), Boolean.class);
		}catch(Exception ex) {
			this._logHelper.Error("Error, we couldn´t verify if resource "+resourceId+" was locked. "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets the modified resources from a datetime in a community
	 * @param communityShortName Community short name
	 * @param searchDate Start search datetime in ISO8601 format string ("yyyy-MM-ddTHH:mm:ss.mmm" (no spaces) OR "yyyy-MM-ddTHH:mm:ss.mmmZ" (no spaces)
	 * @return List with the modified resources identifiers 
	 * @throws Exception exception 
	 */
	public List<UUID> GetModifiedResourcesFromDate(String communityShortName, String searchDate) throws Exception{
		List<UUID> resources=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-modified-resources?community_short_name="+getCommunityShortName()+"&search_date="+searchDate;
			String response=WebRequest("GET", url);
			resources=gson.fromJson(response, (Type) new ArrayList<UUID>());
			
			this._logHelper.Debug("Resources obtained of the community "+communityShortName+" from date "+searchDate);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the resources of "+communityShortName+" from date "+searchDate);
			throw ex;
		}
		
		return resources;
	}
	
	
	/**
	 * Gets the novelties of the resource from a datetime
	 * @param resourceId Resource identifier
	 * @param communityShortName Community short name
	 * @param searchDate Start search datetime in ISO8601 format string ("yyyy-MM-ddTHH:mm:ss.mmm" (no spaces) OR "yyyy-MM-ddTHH:mm:ss.mmmZ" (no spaces))
	 * @return ResourceNoveltiesModel with the novelties of the resource from the search date
	 * @throws Exception exception 
	 */
	public ResourceNoveltiesModel GetResourceNoveltiesFromDate(UUID resourceId, String communityShortName, String searchDate) throws Exception {
		ResourceNoveltiesModel resource=null;
		Gson gson = new Gson();
		
		try {
			String url=getApiUrl()+"/resource/get-resource-novelties?resource_id="+resourceId+"&community_short_name="+communityShortName+"&search_date="+searchDate;
			String response = WebRequest("GET", url, "application/x-www-form-urlencoded");
			resource=gson.fromJson(response, ResourceNoveltiesModel.class);
			
			if(resource!=null) {
				this._logHelper.Debug("Obtained the resource "+resourceId+" of the community "+communityShortName+" from date "+searchDate);
			}
			else {
				this._logHelper.Debug("The resource "+resourceId+" could not be obtained of the community "+communityShortName+" from date "+searchDate);
			}
		}catch(Exception ex) {
			this._logHelper.Error("Error getting  the resource "+resourceId+" of the community "+communityShortName+" from date "+searchDate);
			throw ex;
		}
		return resource;
	}
	
	
	/**
	 * 
	 * @param communityShortName community short name 
	 * @param rec rec 
	 * @param pEsUltimo boolean 
	 * @param pTipoDoc tipo Documento 
	 * @return LoadResourceParams LoadResourceParams
	 */
	private LoadResourceParams GetResourceModelOfBasicOntologyResource(String communityShortName, BasicOntologyResource rec, boolean pEsUltimo, short pTipoDoc ) {
		
		LoadResourceParams model= new LoadResourceParams();
		model.setResource_id(rec.getShortGnossId());
		model.setCommunity_short_name(communityShortName);
		model.setTitle(rec.getTitle());
		model.setDescription(rec.getDescription());
		ArrayList tags = new ArrayList<String>();
		tags=(ArrayList) Arrays.asList(rec.getTags());
		model.setTags(tags);
		
		ArrayList<UUID> listaCategorias = new ArrayList<UUID>();
		if(rec.getCategoriesIds()!=null) {
			for(UUID categoria : rec.getCategoriesIds()) {
				if(!listaCategorias.contains(categoria)) {
					listaCategorias.add(categoria);
				}
			}
		}
		
		if(pTipoDoc!=-1)
		{
			model.setResource_type(pTipoDoc);
		}		
		
		model.setCategories(listaCategorias);
		model.setResource_url(rec.DownloadUrl);
		model.setResource_file(rec.AttachedFile.toString());
		model.setCreator_is_author(rec.getCreatorIsAuthor());
		model.setAuthors(rec.getAuthor());
		model.setAuto_tags_title_text(rec.getAutomaticTagsTextFromTitle());
		model.setAuto_tags_description_text(rec.getAutomaticTagsTextFromDescription());
		model.setCreate_screenshot(rec.GenerateSnapshot);
		model.setUrl_screenshot(rec.DownloadUrl);
		
		ArrayList Snap = new ArrayList<Integer>();
		model.setScreenshot_sizes((ArrayList) Arrays.asList(rec.SnapshotSizes));
		model.setEnd_of_load(pEsUltimo);
		model.setCreation_date(rec.getCreationDate());
		model.setPublish_home(rec.getPublishInHome());
		String vis= rec.getVisibility().toString();
		int i=Integer.parseInt(vis);
		model.setVisibility( (short) i);
		model.setEditor_list(rec.getEditorsGroups());
		model.setReader_list(rec.getReadersGroups());
		
		return model;
		
	}
	
	private LoadResourceParams GetResourceModelOfCOmplexOntologyResource(String communityShortName, ComplexOntologyResource rec, boolean pCrearVersion, boolean pEsUltimo) throws IOException, GnossAPIException {
		LoadResourceParams model = new LoadResourceParams();
		model.setResource_id(rec.getShortGnossId());
		model.setCommunity_short_name(communityShortName);
		model.setTitle(rec.getTitle());
		model.setDescription(rec.getDescription());
		String vis= TiposDocumentacion.ontology.toString();
		int i=Integer.parseInt(vis);
		model.setResource_type( (short) i);
		
		if(rec.getTags()!=null) {
			model.setTags((ArrayList) Arrays.asList(rec.getTags()));
		}
		if(pCrearVersion) {
			model.setCreate_version(pCrearVersion);
		}
		
		ArrayList<UUID> listaCategorias= new ArrayList<UUID>();
		if(rec.getCategoriesIds()!=null) {
			for(UUID categoria : rec.getCategoriesIds()) {
				if(!listaCategorias.contains(categoria)) {
					listaCategorias.add(categoria);
				}
			}
		}
		
		model.setCategories(listaCategorias);
		model.setResource_url(rec.getOntology().getOntologyUrl());
		model.setResource_file(rec.getRdFile());
		
		int k=0;
		if(rec.getAttachedFiles().size()>0) {
			model.setResource_attached_files(new ArrayList<SemanticAttachedResource>());
			
			for(String nombre : rec.getAttachedFilesName()) {
				SemanticAttachedResource adjunto = new SemanticAttachedResource();
				adjunto.setFile_rdf_property(nombre);
				int s=Integer.parseInt(rec.getAttachedFiles().get(k).toString());
				adjunto.setFile_property_type( (short) s);
				adjunto.setRdf_attacherd_file(rec.getAttachedFiles().get(k));
				adjunto.setDelete_file(rec.getAttachedFiles().get(k).equals(null));
				k++;
				model.resource_attached_files.add(adjunto);
				
			}
		}
		
		model.setCreator_is_author(rec.getCreatorIsAuthor());
		model.setAuthors(rec.getAuthor());
		model.setAuto_tags_title_text(rec.getAutomaticTagsTextFromDescription());
		model.setAuto_tags_description_text(rec.getAutomaticTagsTextFromDescription());
		model.setCreate_screenshot(rec.getMustGenerateScreenshot());
		model.setUrl_screenshot(rec.getScreenshotUrl());
		model.setPredicate_screenshot(rec.getScreenshotPredicate());
		
		if(rec.getScreenshotSizes()!=null) {
			model.setScreenshot_sizes((ArrayList) Arrays.asList(rec.getScreenshotSizes()));
		}
		
		model.setEnd_of_load(pEsUltimo);
		model.setCreation_date(rec.getCreationDate());
		model.setPublisher_email(rec.getPublisherEmail());
		model.setLoad_id(getLoadIdentifier());
		model.setMain_image(rec.getMainImage());
		String vis1= rec.getVisibility().toString();
		int h=Integer.parseInt(vis1);
		model.setVisibility( (short) h);
		model.setEditor_list(rec.getEditorsGroups());
		model.setReader_list(rec.getReadersGroups());
		
		return model;
	}
	
	
	//region Common methods for basic and complex ontology resources
	private HashMap<UUID, Boolean> ModifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps , boolean publishHome , UUID userId )
    {
		HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
		int procesedNumber=0;
		int attempNumber=0;
		
		ArrayList<String> valores= new ArrayList<String>();
		HashMap<UUID, ArrayList<TriplesToModify>> toModify= new HashMap<UUID, ArrayList<TriplesToModify>>();
		toModify=resourceTriples;
		while(toModify!=null && toModify.size()>0 && attempNumber<numAttemps) {
			int i=0;
			int contResources=resourceTriples.keySet().size();
			
			for(UUID docID : resourceTriples.keySet()) {
				i++;
				ArrayList<ModifyResourceTriple> listaValores= new ArrayList<ModifyResourceTriple>();
				attempNumber++;
				procesedNumber++;
				for(TriplesToModify mT : resourceTriples.get(docID)) {
					ModifyResourceTriple triple = new ModifyResourceTriple();
					triple.setOld_object(mT.getOldValue());
					triple.setPredicate(mT.getPredicate());
					triple.setNew_object(mT.getNewValue());
					triple.setGnoss_property(GnossResourceProperty.none);
					
					if(mT.isTitle()) {
						triple.setGnoss_property(GnossResourceProperty.title);
						
					}else if(mT.isDescription()) {
						triple.setGnoss_property(GnossResourceProperty.description);
					}
					listaValores.add(triple);
				}
				
				try {
					boolean endOfLoad=false;
					if(i==contResources) {
						endOfLoad=true;
					}
					ModifyTripleList(docID, listaValores, getLoadIdentifier(), publishHome, null, null, endOfLoad, userId);
					this._logHelper.Debug(procesedNumber+" of "+resourceTriples.size()+". Object: "+docID+". Resource: "+resourceTriples.get(docID).toArray());
					toModify.remove(docID);
					
					if(result.containsKey(docID)) {
						result.get(docID.equals(true));
					}else {
						result.put(docID, true);
					}
				}catch(Exception ex) {
					this._logHelper.Error("Resource "+docID+": "+ex.getMessage());
					
					if(result.containsKey(docID)) {
						result.get(docID.equals(false));
					}else {
						result.put(docID, false);
					}
				}
				
				
			}
			this._logHelper.Debug("******************** Lap number: "+attempNumber +"finished");
		}
		
		return result;
    }
	
	private HashMap <UUID, Boolean> InsertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome, UUID usuarioId){
		HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
		int procesedNumber=0;
		int attempNumber=0;
		
		HashMap<UUID, ArrayList<TriplesToInclude>> toInsert= new HashMap<UUID, ArrayList<TriplesToInclude>>();
		toInsert=resourceTriples;
		while(toInsert!=null && toInsert.size()>0 && attempNumber<numAttemps) {
			int i=0;
			int contResources=resourceTriples.keySet().size();
			
			for(UUID docID : resourceTriples.keySet()) {
				i++;
				ArrayList<ModifyResourceTriple> listaValores= new ArrayList<ModifyResourceTriple>();
				attempNumber++;
				procesedNumber++;
				for(TriplesToInclude mT : resourceTriples.get(docID)) {
					ModifyResourceTriple triple = new ModifyResourceTriple();
					triple.setOld_object(null);
					triple.setPredicate(mT.getPredicate());
					triple.setNew_object(mT.getNewValue());
					triple.setGnoss_property(GnossResourceProperty.none);
					
					if(mT.isTitle()) {
						triple.setGnoss_property(GnossResourceProperty.title);
						
					}else if(mT.isDescription()) {
						triple.setGnoss_property(GnossResourceProperty.description);
					}
					listaValores.add(triple);
				}
				
				try {
					boolean endOfLoad=false;
					if(i==contResources) {
						endOfLoad=true;
					}
					ModifyTripleList(docID, listaValores, getLoadIdentifier(), publishHome, null, null, endOfLoad, usuarioId);
					this._logHelper.Debug(procesedNumber+" of "+resourceTriples.size()+". Object: "+docID+". Resource: "+resourceTriples.get(docID).toArray());
					toInsert.remove(docID);
					
					if(result.containsKey(docID)) {
						result.get(docID.equals(true));
					}else {
						result.put(docID, true);
					}
				}catch(Exception ex) {
					this._logHelper.Error("Resource "+docID+": "+ex.getMessage());
					
					if(result.containsKey(docID)) {
						result.get(docID.equals(false));
					}else {
						result.put(docID, false);
					}
				}
				
				
			}
			this._logHelper.Debug("******************** Lap number: "+attempNumber +"finished");
		}
		return result;
		
	}
	
	private HashMap<UUID, Boolean> ActionsPropertiesLoadedResourcesInt (HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, HashMap<UUID, ArrayList<RemoveTriples>> deleteList, HashMap<UUID, ArrayList<TriplesToInclude>>insertList,  HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntitiesInsertTriplesList, String communityShortName, boolean publishHome){

		HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
		int procesedNumber=0;
		int attempNumber=0;
		ArrayList<ModifyResourceTriple> valuesList= new ArrayList<ModifyResourceTriple>();
		ArrayList<String> values = new ArrayList<String>();
		HashMap<UUID, ArrayList<ModifyResourceTriple>> resources = new HashMap<UUID, ArrayList<ModifyResourceTriple>>();
		
		if(deleteList!=null) {
			HashMap<UUID, ArrayList<RemoveTriples>> toDelete = new HashMap<UUID, ArrayList<RemoveTriples>>();
			toDelete=deleteList;
			while(toDelete!=null && toDelete.size()>0) {
				for(UUID docID : deleteList.keySet()) {
					valuesList= new ArrayList<ModifyResourceTriple>();
					procesedNumber++;
					attempNumber++;
					
					for(RemoveTriples iT : deleteList.get(docID)) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(iT.getValue());
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(null);
						triple.setGnoss_property(GnossResourceProperty.none);
						
						if(iT.isTitle()) {
							triple.setGnoss_property(GnossResourceProperty.title);
							
						}else if(iT.isDescription()) {
							triple.setGnoss_property(GnossResourceProperty.description);
						}
						valuesList.add(triple);
					}
					resources.put(docID, valuesList);
					toDelete.remove(docID);
				}
			}
		}
		
		if(resourceTriples!=null) {
			HashMap<UUID, ArrayList<TriplesToModify>> toModify = new HashMap<UUID, ArrayList<TriplesToModify>>();
			toModify=resourceTriples;
			while(toModify!=null && toModify.size()>0) {
				for(UUID docID : resourceTriples.keySet()) {
					valuesList= new ArrayList<ModifyResourceTriple>();
					
					if(resources.containsKey(docID)) {
						valuesList.addAll(resources.get(docID));
					}
					procesedNumber++;
					attempNumber++;
					
					for(TriplesToModify iT : resourceTriples.get(docID)) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(iT.getOldValue());
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(iT.getNewValue());
						triple.setGnoss_property(GnossResourceProperty.none);
						
						if(iT.isTitle()) {
							triple.setGnoss_property(GnossResourceProperty.title);
							
						}else if(iT.isDescription()) {
							triple.setGnoss_property(GnossResourceProperty.description);
						}
						valuesList.add(triple);
					}
					if(resources.containsKey(docID)) {
						resources.put(docID, valuesList);
					}else {
						resources.put(docID, valuesList);
					}
					toModify.remove(docID);
				}
			}
		}
		
		if(insertList!=null) {
			HashMap<UUID, ArrayList<TriplesToInclude>> toInsert = new HashMap<UUID, ArrayList<TriplesToInclude>>();
			toInsert=insertList;
			while(toInsert!=null && toInsert.size()>0) {
				for(UUID docID : insertList.keySet()) {
					valuesList= new ArrayList<ModifyResourceTriple>();
					if(resources.containsKey(docID)) {
						valuesList.addAll(resources.get(docID));
					}
					procesedNumber++;
					attempNumber++;
					for(TriplesToInclude iT : insertList.get(docID)) {
						ModifyResourceTriple triple = new ModifyResourceTriple();
						triple.setOld_object(null);
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(iT.getNewValue());
						triple.setGnoss_property(GnossResourceProperty.none);
						
						if(iT.isTitle()) {
							triple.setGnoss_property(GnossResourceProperty.title);
						}else if(iT.isDescription()) {
							triple.setGnoss_property(GnossResourceProperty.description);
						}
						valuesList.add(triple);
					}
					if(resources.containsKey(docID)) {
						resources.put(docID, valuesList);
					}else {
						resources.put(docID, valuesList);
					}
					toInsert.remove(docID);	
				}
			}
		}
		
		if(auxiliaryEntitiesInsertTriplesList!=null) {
			HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntityTriplesToInsert= new HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>>();
			auxiliaryEntityTriplesToInsert=auxiliaryEntitiesInsertTriplesList;
			while(auxiliaryEntityTriplesToInsert!=null && auxiliaryEntityTriplesToInsert.size()>0) {
				for(UUID docID : auxiliaryEntitiesInsertTriplesList.keySet()) {
					valuesList= new ArrayList<ModifyResourceTriple>();
					
					if(resources.containsKey(docID)) {
						valuesList.addAll(resources.get(docID));
					}
					procesedNumber++;
					attempNumber++;
					
					for(AuxiliaryEntitiesTriplesToInclude iT : auxiliaryEntitiesInsertTriplesList.get(docID)) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(null);
						triple.setPredicate(iT.getPredicate());
						triple.setNew_object(getApiUrl()+"items/"+iT.getName()+"_"+docID+"_"+iT.getIdentifier()+"|"+iT.getValue());
						triple.setGnoss_property(GnossResourceProperty.none);
						
						valuesList.add(triple);
					}
					if(resources.containsKey(docID)) {
						resources.put(docID, valuesList);
					}
					else {
						resources.put(docID, valuesList);
					}
					auxiliaryEntityTriplesToInsert.remove(docID);
				}
			}
		}
		int i=0;
		int constResources=resources.keySet().size();
		
		for(UUID docID : resources.keySet()) {
			i++;
			try {
				boolean endOfLoad=false;
				if(i==constResources) {
					endOfLoad=true;
				}
				ModifyTripleList(docID, valuesList, getLoadIdentifier(), publishHome, null, null, endOfLoad);
				valuesList= new ArrayList<ModifyResourceTriple>();
				
				this._logHelper.Debug("Object: "+docID);
				
				if(result.containsKey(docID)) {
					result.get(docID).equals(true);
				}else {
					result.put(docID, true);
				}
			}catch(Exception ex) {
				this._logHelper.Error("Resource "+docID+": "+ex.getMessage());
				valuesList= new ArrayList<ModifyResourceTriple>();
				if(result.containsKey(docID)) {
					result.get(docID).equals(false);
				}else {
					result.put(docID, false);
				}
			}
		}
		
		return result;		
	}
	
	private boolean InsertAuxiliarEntityOnPropertiesLoadedResourceInt(HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome, UUID userId) {
		
		int processedNumber=0;
		int attempNumber=0;
		boolean inserted=false;
		HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> pDiccionarioInsertar = new HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>>();
		pDiccionarioInsertar=resourceTriples;
		
		while(pDiccionarioInsertar!=null && pDiccionarioInsertar.size()>0 && attempNumber<numAttemps) {
			int i=0;
			int contResources=resourceTriples.keySet().size();
			
			for(UUID docID : resourceTriples.keySet()) {
				i++;
				ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
				attempNumber++;
				processedNumber++;
				
				for(AuxiliaryEntitiesTriplesToInclude iT : resourceTriples.get(docID)) {
					ModifyResourceTriple triple = new ModifyResourceTriple();
					triple.setOld_object(null);
					triple.setPredicate(iT.getPredicate());
					triple.setNew_object(getApiUrl()+"items/"+iT.getName()+"_"+docID+"_"+iT.getIdentifier()+"|"+iT.getValue());
					triple.setGnoss_property(GnossResourceProperty.none);
					
					listaValores.add(triple);
				}
				
				try {
					boolean endOfLoad=false;
					if(i==contResources) {
						endOfLoad=true;
					}
					ModifyTripleList(docID, listaValores, getLoadIdentifier(), publishHome, null, null, endOfLoad, userId);
					this._logHelper.Debug(processedNumber+" of "+resourceTriples.size()+" Object: "+docID+". Resource: "+resourceTriples.get(docID).toArray());
					pDiccionarioInsertar.remove(docID);
					inserted=true;
				}
				catch(Exception ex) {
					this._logHelper.Error("Resource "+docID+" : "+ex.getMessage());
				}
			}
			this._logHelper.Debug("******************** Lap number: "+attempNumber+ " finished");
		}
		return inserted; 
	}
	
	//Region Basic semantic ontology resources 
	
	/**
	 * Loads a basic ontology resource
	 * @param resource Basic intology resource to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param resourceType Indicates the type of resource to load
	 * @param isLast Indicates There are not resources left to load
	 * @throws Exception exception 
	 */
	private void LoadBasicOntologyResourceInt (BasicOntologyResource resource, boolean hierarquicalCategories, TiposDocumentacion resourceType, boolean isLast) throws Exception {
		this._logHelper.Trace("******************** Begin Load"+ this.getClass().getSimpleName());
		
		if(((resource.getCategoriesIds()!=null && resource.getCategoriesIds().size()==0) || resource.getCategoriesIds()==null )  && resource.getTextCategories()!=null && resource.getTextCategories().size()>0) {
			if(hierarquicalCategories) {
				resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
			}else {
				resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
			}
		}
		try {
			int i= Integer.parseInt(resourceType.toString());
			LoadResourceParams model= GetResourceModelOfBasicOntologyResource(getCommunityShortName(), resource, isLast, (short) i);
			String documentId=CreateBasicOntologyResource(model);
			resource.setUploaded(true);
			this._logHelper.Debug("Loaded "+resource.getGnossId()+"\t Title: "+resource.getTitle()+ "\t ResourceID : "+documentId+ this.getClass().getSimpleName());
			
			if(documentId!= resource.getGnossId()) {
				throw new GnossAPIException("Resource loaded with the id :"+ documentId+ "\n The IDGnpss provided to the method is different from the returned by the API");
			}
			
		}catch(GnossAPIException ex) {
			this._logHelper.Info("Resource "+resource.getId()+ ". Title: "+resource.getTitle()+". Message: "+ex.getMessage()+ " "+this.getClass().getSimpleName());
		}
		catch(Exception ex) {
			this._logHelper.Error("ERROR at : "+resource.getId()+". Titile: "+resource.getTitle()+". Message: "+ex.getMessage()+" "+this.getClass().getSimpleName());
		}
		this._logHelper.Debug("******************** End Load "+this.getClass().getSimpleName());
	}
	
	
	/**
	 * 
	 * @param resource resource 
	 * @param hierarquicalCategories hierarquical categories
	 * @param resourceType resource type 
	 * @param isLast is Last
	 * @throws MalformedURLException Mal formed Exception 
	 * @throws IOException IO Exception 
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	private void LoadBasicOntologyResourceIntVideo(BasicOntologyResource resource, boolean hierarquicalCategories, TiposDocumentacion resourceType, boolean isLast) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
		this._logHelper.Trace("******************** Begin Load "+this.getClass().getSimpleName());
		Gson gson= new Gson();
		
		if(hierarquicalCategories) {
			resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
		}else {
			resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
		}
		
		try {
			int i= Integer.parseInt(resourceType.toString());
			LoadResourceParams model= GetResourceModelOfBasicOntologyResource(getCommunityShortName(), resource, isLast, (short) i);
			String documentId=CreateBasicOntologyResource(model);
			resource.setUploaded(true);
			
			try {
				documentId=gson.fromJson(documentId, String.class);
			}catch(Exception ex) {
				
			}
			resource.setShortGnossId(UUID.fromString(documentId));
			this._logHelper.Debug("Loaded: "+resource.getId()+" \t Title: "+resource.getTitle()+" \t ResourceID: "+documentId+" "+this.getClass().getSimpleName());
		}
		catch(Exception ex) {
			this._logHelper.Error("Error at: "+resource.getId()+". Title: "+resource.getTitle()+". Message: "+ex.getMessage()+" "+this.getClass().getSimpleName());
		}
		this._logHelper.Debug("******************** End Load "+this.getClass().getSimpleName());
	}
	
	private void LoadBasicOntologyResourceListInt(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, TiposDocumentacion resourceType, int numAttemps) {
		
		int processedNumber=0;
		ArrayList<BasicOntologyResource> originalResourceList = new ArrayList<BasicOntologyResource>();
		ArrayList<BasicOntologyResource> resourcesToLoad= new ArrayList<BasicOntologyResource>();
		originalResourceList=resourceList;
		resourcesToLoad=resourceList;
		int attempNumber=0;
		
		while(resourcesToLoad!=null && resourcesToLoad.size()>0 && attempNumber<= numAttemps) {
			attempNumber++;
			this._logHelper.Trace("******************** Begin lap number: "+attempNumber+" "+this.getClass().getSimpleName());
			
			for(BasicOntologyResource rec : resourceList) {
				processedNumber++;
				try {
					LoadBasicOntologyResourceInt(rec, hierarquicalCategories, resourceType, processedNumber==resourceList.size());
					
					if(rec.isUploaded()) {
						this._logHelper.Debug("Loaded: "+processedNumber+" of "+resourceList.size()+" \t ID: "+rec.getId()+" \t Title: "+rec.getTitle());
					}
					resourcesToLoad.remove(rec);
				}
				catch(Exception ex) {
					this._logHelper.Error("ERROR in : "+processedNumber+" of "+resourceList.size()+ "\t ID: "+rec.getId()+ "  Title: "+rec.getTitle()+". Message: "+ex.getMessage());
				}
				this._logHelper.Debug("******************** Finished lap number: "+attempNumber+" "+this.getClass().getSimpleName());
			}
		}
	}
	
	
	private void LoadBasicOntologyResourceListIntVideo(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, TiposDocumentacion resourceType, int numAttemps) {
		int proccessedNumber=0;
		ArrayList<BasicOntologyResource> originalResourceList= new ArrayList<BasicOntologyResource>();
		originalResourceList=resourceList;
		ArrayList<BasicOntologyResource> resourcesToLoad= new ArrayList<BasicOntologyResource>();
		resourcesToLoad=resourceList;
		int attempNumber=0;
		
		while(resourcesToLoad!=null && resourcesToLoad.size()>0 && attempNumber<=numAttemps) {
			attempNumber++;
			this._logHelper.Trace("******************** Begin lap number: "+attempNumber+", "+this.getClass().getSimpleName());
			
			for(BasicOntologyResource rec : resourceList) {
				proccessedNumber++;
				
				try {
					LoadBasicOntologyResourceInt(rec, hierarquicalCategories, resourceType, proccessedNumber==resourceList.size());
					this._logHelper.Debug("Loaded: "+proccessedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+" \t Title: "+rec.getTitle());
					resourcesToLoad.remove(rec);
				}
				catch(Exception ex) {
					this._logHelper.Error("ERROR in: "+proccessedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Mensaje: "+ex.getMessage()+", "+this.getClass().getSimpleName());
				}
			}
			this._logHelper.Debug("******************** Finished lap number: "+attempNumber+", "+this.getClass().getSimpleName());
		}
	}
	
	public void ModifyBasicOntologyResource(BasicOntologyResource resource, boolean hierarquicalCategories, boolean isLast) {
		try {
			if(resource.getTextCategories()!=null) {
				if(hierarquicalCategories) {
					resource.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
				else {
					resource.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
			}
			LoadResourceParams model= GetResourceModelOfBasicOntologyResource(getCommunityShortName(), resource, isLast, (Short) null);
			resource.setUploaded(ModifyBasicOntologyResource(model));
		}
		catch(Exception ex) {
			this._logHelper.Error("The basic Ontology resource with  id: "+resource.getGnossId()+" has not been modified, "+ex.getMessage());
			
		}
	}
	
	/**
	 * Modifies the basic ontology resource of the list
	 * @param resourceList It is necessary that the basic ontology resource has assigned the property. Resource list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 2. Number of retries loading of the failed load of a resource
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public void ModifyBasicOntologyResourceList(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) throws GnossAPICategoryException {
		ArrayList<BasicOntologyResource> originalResourceList= new ArrayList<BasicOntologyResource>();
		originalResourceList=resourceList;
		ArrayList<BasicOntologyResource> resourcesToModify= new ArrayList<BasicOntologyResource>();
		resourcesToModify=resourceList;
		int processedNumber=0;
		int attempNumber=0;
		
		while(resourcesToModify!=null && resourcesToModify.size()>0 && attempNumber<numAttemps) {
			attempNumber++;
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			
			for(BasicOntologyResource rec : resourceList){
				processedNumber++;
				
				try {
					ModifyBasicOntologyResource(rec, hierarquicalCategories, processedNumber==resourceList.size());
					resourcesToModify.remove(rec);
				}
				catch(Exception ex) {
					this._logHelper.Error("ERROR at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title. "+rec.getTitle()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Lap number: "+attempNumber+" finalizada ", this.getClass().getSimpleName());
			}
		}
		
	}
	
	/**
	 * Modifies the basic ontology resource of the list. It is necessary that the basic ontology resource has assigned the property
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param ontology Ontology where resource will be loaded
	 * @param communityShortName Community short name where the resources will be loaded
	 * @param numAttemps Default 1. Number of retries loading of the failed load of a resource
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public void ModifyComplexSemanticResourceListWithOntologyAndCommunity(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String ontology, String communityShortName, int numAttemps) throws GnossAPICategoryException{
		int resourcesToModify=0;
		ArrayList<ComplexOntologyResource> resourceList1= new ArrayList<ComplexOntologyResource>();
		for(ComplexOntologyResource cor : resourceList) {
			if (!cor.isModified()) {
				resourceList1.add(cor);
				resourcesToModify++;
			}
		}
		
		int processedNumber=0;
		int attempNumber=0;
		
		while(resourceList!=null && resourceList.size()>0 && resourcesToModify>0 && attempNumber<numAttemps) {
			attempNumber++;
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			for(ComplexOntologyResource rec : resourceList1) {
				processedNumber++;
				
				try {
					ModifyComplexSemanticResourceWithOntologyAndCommunity(rec, hierarquicalCategories, processedNumber==resourceList.size(), ontology, communityShortName);
					resourcesToModify--;
					this._logHelper.Debug("There are "+resourcesToModify+ " resources left to modify");
				}
				catch(Exception ex) {
					this._logHelper.Error("ERROR at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Lap number: "+attempNumber+" finalizada ", this.getClass().getSimpleName());
			}
		}
	}

	
	/**
	 * Modifies the basic ontology resource. It is necessary that the basic ontology resource has assigned the property
	 * @param rec Resource to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param b There are not resources left to load
	 * @param ontology Ontology where resource will be loaded
	 * @param communityShortName Community short name where the resources will be loaded
	 */
	private void ModifyComplexSemanticResourceWithOntologyAndCommunity(ComplexOntologyResource rec,
			boolean hierarquicalCategories, boolean b, String ontology, String communityShortName) {
		
		this._logHelper.Trace("******************** Begin the resource modification: "+rec.getGnossId(), this.getClass().getSimpleName(), getCommunityShortName());
		try {
			if(rec.getTextCategories()!=null) {
				if(hierarquicalCategories) {
					rec.setCategoriesIds(GetHierarquicalCategoriesIdentifiersList(rec.getTextCategories()));
				}else {
					rec.setCategoriesIds(GetNotHierarquicalCategoriesIdentifiersList(rec.getTextCategories()));
				}
			}
			String ontologyUrl=ontology.toLowerCase().replace(".owl", "");
			ontologyUrl=getOntologyUrl().replace((getOntologyUrl().substring(getOntologyUrl().lastIndexOf("/")+1)), ontologyUrl+".owl");
			LoadResourceParams model=GetResourceModelOfComplexOntologyResource(getCommunityShortName(), rec, false, b);
			model.setResource_url(ontologyUrl);
			rec.setModified(ModifyComplexOntologyResource(model));
			
			if(rec.isModified()) {
				this._logHelper.Debug("Successfully modified the resource with id: "+rec.getId()+" and Gnoss identifier "+rec.getShortGnossId()+" belonging to the ontology '"+ontologyUrl+"' and RdfType='"+rec.getOntology().getRdfType()+"'", this.getClass().getSimpleName());
			}else {
				this._logHelper.Error("The resources with id: "+rec.getShortGnossId()+" of the ontology '"+ontologyUrl+"' hhas not been modified.", this.getClass().getSimpleName());
			}
		}
		catch(Exception ex ) {
			this._logHelper.Error("The resource with id "+rec.getGnossId()+" has not been modified, "+ex.getMessage());
		}
	}
	
	/**
	 * Modifies the basic ontology resource. It is necessary that the basic ontology resource has assigned the property
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 1. Number of retries loading of the failed load of a resource
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public void ModifyComplexSemanticResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) throws GnossAPICategoryException{
		int resourcesToModify=0;
		ArrayList<ComplexOntologyResource> resourceList1= new ArrayList<ComplexOntologyResource>();
		for(ComplexOntologyResource cor : resourceList) {
			if (!cor.isModified()) {
				resourceList1.add(cor);
				resourcesToModify++;
			}
		}
		int processedNumber=0;
		int attempNumber=0;
		
		while(resourceList!=null && resourceList.size()>0 && resourcesToModify>0 && attempNumber<numAttemps) {
			attempNumber++;
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			for(ComplexOntologyResource rec : resourceList1) {
				processedNumber++;
				
				try {
					ModifyComplexOntologyResource(rec, hierarquicalCategories, processedNumber==resourceList.size());
					resourcesToModify--;
					this._logHelper.Debug("There are "+resourcesToModify+" resources left to modify.");
				}
				catch(Exception ex) {
					this._logHelper.Error("Error at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Mesage: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			
		}
	}
	

	/**
	 * Modifies the complex ontology resource. It is necessary that the basic ontology resource has assigned the property 
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param communityShortName Community short name where the resources will be loaded
	 * @param numAttemps Default 2. Number of retries loading of the failed load of a resource
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public void ModifyComplexSemanticResourceListCommunityShortName(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String communityShortName, int numAttemps) throws GnossAPICategoryException{
		int resourcesToModify=0;
		ArrayList<ComplexOntologyResource> resourceList1= new ArrayList<ComplexOntologyResource>();
		for(ComplexOntologyResource cor : resourceList) {
			if (!cor.isModified()) {
				resourceList1.add(cor);
				resourcesToModify++;
			}
		}
		int processedNumber=0;
		int attempNumber=0;
		
		while(resourceList!=null && resourceList.size()>0 && resourcesToModify>0 && attempNumber<numAttemps) {
			attempNumber++;
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			for(ComplexOntologyResource rec : resourceList1) {
				processedNumber++;
				
				try {
					ModifyComplexSemanticResourceCommunityShortName(rec, hierarquicalCategories, processedNumber==resourceList.size(), communityShortName);
					resourcesToModify--;
					this._logHelper.Debug("There are "+resourcesToModify+" resources left to modify.");
				}
				catch(Exception ex) {
					this._logHelper.Error("Error at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Mesage: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				this._logHelper.Trace("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			
		}
	}
	
	public void ModifyCategoriesTagsComplexOntologyResource(UUID resourceID, String property, ArrayList<String> listToModify, boolean hierarquicalCategories ) throws Exception {
		this._logHelper.Debug("******************** Start modification: ", this.getClass().getSimpleName());
		List<UUID> categoriesIdentifiers=null;
		String newObject="";
		
		if(listToModify!=null && property.equals("skos:ConceptID")) {
			categoriesIdentifiers= new ArrayList<UUID>();
			
			if(hierarquicalCategories) {
				categoriesIdentifiers=GetHierarquicalCategoriesIdentifiersList(listToModify);
			}
			else {
				categoriesIdentifiers=GetNotHierarquicalCategoriesIdentifiersList(listToModify);
			}
		}
		else {
			if(listToModify!=null && property.equals("sioc_t:Tag")) {
				for(String identificador : listToModify) {
					if(identificador.isEmpty()) {
						newObject+=identificador.trim()+",";
					}else {
						newObject +=identificador.trim()+",";
					}
				}
			}
		}
		if(categoriesIdentifiers!=null) {
			for(UUID identifier : categoriesIdentifiers) {
				if(identifier.equals(UUID.fromString(""))) {
					newObject+=identifier.toString()+",";
				}
				else {
					newObject += identifier.toString()+",";
				}
			}
		}
		ModifyProperty(resourceID, property, newObject);
		Calendar c1 = Calendar.getInstance();
		this._logHelper.Debug("The resource '"+resourceID+"' has been modifies correctly ..."+c1 );
	}

	
	/**
	 * Removes attached files of the resource
	 * @param resourceId Resource identifier guid
	 * @param removeTripleList Resource triples list to modify
	 * @param publishHome Indicates whether the home must be updated
	 * @throws IOException IO exception 
	 * @throws GnossAPIException Gnoss API Exception
	 * @throws Exception Exception 
	 */
	public void RemoveResourceAttachedFiles(UUID resourceId, ArrayList<RemoveTriples> removeTripleList, boolean publishHome ) throws IOException, GnossAPIException, Exception{
		int numTriples=removeTripleList.size();
		String attachedValue="";
		String attachedPredicate="";
		short attachedObjectType =-1;
		
		
		if(resourceId.equals(UUID.fromString(""))) {
			ArrayList<ModifyResourceTriple> triplesList = new ArrayList<ModifyResourceTriple>();
			ArrayList<SemanticAttachedResource> resourceAttachedFiles= new ArrayList<SemanticAttachedResource>();
			
			for(RemoveTriples tripleToDelete : removeTripleList) {
				if(tripleToDelete!= null) {
					attachedValue=tripleToDelete.getValue();
					attachedPredicate=tripleToDelete.getPredicate();
					attachedObjectType=tripleToDelete.getObjectType();
					
					if(!attachedValue.isEmpty() && !attachedPredicate.isEmpty() && attachedObjectType!=-1) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(attachedValue);
						triple.setPredicate(attachedPredicate);
						triple.setNew_object("");
						triple.setGnoss_property(GnossResourceProperty.none);
						
						triplesList.add(triple);
						
						SemanticAttachedResource attach= new SemanticAttachedResource();
						attach.setFile_rdf_property(attachedValue);
						attach.setFile_property_type(attachedObjectType);
						attach.setRdf_attacherd_file(null);
						attach.setDelete_file(true);
						
						resourceAttachedFiles.add(attach);
						
					}
				}
			}
			if(removeTripleList.size()>0) {
				ModifyTripleList(resourceId, triplesList, getLoadIdentifier(), publishHome, null, resourceAttachedFiles, true);
				this._logHelper.Debug("Modified resource with attached. ResourceId: "+resourceId);
			}
			}
		
	}
	
	
	/**
	 * Attaches a file to a semantic resource.
	 * @param resourceId Resource identifier UUID
	 * @param filePredicate Resource predicate on the ontology
	 * @param fileName File name to attach
	 * @param fileRdfPropertiesList Attached files names list
	 * @param filePropertiesTypeList It indicates whether the attachment is a file or a ArchivoLink
	 * @param attachedFilesList Attached files list
	 * @param publishHome Indicates whether the home must be updated 
	 * @throws IOException IO Exception 
	 * @throws GnossAPIException GnossAPI Exception 
	 * @throws Exception Exception 
	 */
	public void AttachedFileToResource (UUID resourceId, String filePredicate, String fileName, ArrayList<String> fileRdfPropertiesList, ArrayList<Short> filePropertiesTypeList, ArrayList<byte[]> attachedFilesList, boolean publishHome) throws IOException, GnossAPIException, Exception {
		ArrayList<ModifyResourceTriple> triplesList = new ArrayList<ModifyResourceTriple>();
		ArrayList<SemanticAttachedResource> resourceAttachedFiles = new ArrayList<SemanticAttachedResource>();	
		
		ModifyResourceTriple triple = new ModifyResourceTriple();
		triple.setOld_object("");
		triple.setPredicate(filePredicate);
		triple.setNew_object(fileName);
		triple.setGnoss_property(GnossResourceProperty.none);
		
		triplesList.add(triple);
		
		if(fileRdfPropertiesList.size()>0) {
			int i =0;
			for(String name : fileRdfPropertiesList) {
				SemanticAttachedResource attach = new SemanticAttachedResource();
				attach.setFile_rdf_property(name);
				attach.setFile_property_type(filePropertiesTypeList.get(i));
				attach.setDelete_file(attachedFilesList.get(i).equals(null));
				i++;
				resourceAttachedFiles.add(attach);
			}
			ModifyTripleList(resourceId, triplesList, getLoadIdentifier(), publishHome, null, resourceAttachedFiles, true);
		}
		
		this._logHelper.Debug("Modified the resource with attached file: "+resourceId);
	}
	
	/**
	 * Replaces the resource attached images
	 * @param resourceId Resource identifier UUID
	 * @param oldImageName Old image file name
	 * @param newImageName New image file name
	 * @param imagePredicate he predicate that will collect the image. Watch out, it does not support prefix, must be whole URIs.
     *   /// For example, with "gnoss:mainImage" setting, it would not work, it should be: "http://www.gnoss.com/mainImage"
	 * @param fileRdfPropertiesList Images names to load
	 * @param filePropertiesTypeList Attached files types
	 * @param attachedFiles List Images to attach
	 * @param publishHome Indicates whether the home must be updated
	 * @throws IOException IO Exception
	 * @throws GnossAPIException Gnoss API 
	 * @throws Exception exception 
	 */
	public void ReplaceResourceImage(UUID resourceId, String oldImageName, String newImageName, String imagePredicate, ArrayList<String> fileRdfPropertiesList, ArrayList<Short> filePropertiesTypeList, ArrayList<byte[]> attachedFilesList, boolean publishHome) throws IOException, GnossAPIException, Exception {
		ArrayList<ModifyResourceTriple> triplesList= new ArrayList<ModifyResourceTriple>();
		ArrayList<SemanticAttachedResource> resourceAttachedFiles = new ArrayList<SemanticAttachedResource>();
		ModifyResourceTriple triple = new ModifyResourceTriple();
		triple.setOld_object(oldImageName);
		triple.setPredicate(imagePredicate);
		triple.setNew_object(newImageName);
		triple.setGnoss_property(GnossResourceProperty.none);
		
		triplesList.add(triple);
		
		if(fileRdfPropertiesList.size()>0) {
			int i =0;
			for(String name : fileRdfPropertiesList) {
				SemanticAttachedResource attach= new SemanticAttachedResource();
				attach.setFile_rdf_property(name);
				attach.setFile_property_type(filePropertiesTypeList.get(i));
				attach.setRdf_attacherd_file(attachedFilesList.get(i));
				attach.setDelete_file(attachedFilesList.get(i).equals(null));
				i++;
				resourceAttachedFiles.add(attach);
			}
			ModifyTripleList(resourceId, triplesList, getLoadIdentifier(), publishHome, null, resourceAttachedFiles, true);
			
		}
		this._logHelper.Debug("Modified the resource with attached image: "+resourceId);
	}
	
	
	//Region DELETE
	
	
	/**
	 * Delete resources list. It is necessary that the resource has assigned the properties 
	 * @param resourceList Resources list to delete
	 * @param numAttemp Default 5. Number of retries loading of the failed load resource 
	 */
	public void DeleteResourceList(ArrayList<ComplexOntologyResource> resourceList, int numAttemp) {
		int processedNumber = 0;
        int attempNumber = 0;
        int numResourcesLeft = resourceList.size();
        
        ArrayList<ComplexOntologyResource> originalResourceList = new ArrayList<ComplexOntologyResource>();
        originalResourceList=resourceList;
        ArrayList<ComplexOntologyResource> resourcesToDelete = new ArrayList<ComplexOntologyResource>();
        resourcesToDelete=resourceList;
        
        int resourcesToModify=0;
		ArrayList<ComplexOntologyResource> resourceList1= new ArrayList<ComplexOntologyResource>();
		for(ComplexOntologyResource cor : originalResourceList) {
			if (!cor.isDeleted()) {
				resourceList1.add(cor);
				resourcesToModify++;
			}
		}
        while(originalResourceList!=null && resourcesToModify>0 && attempNumber<=numAttemp) {
        	attempNumber++;
        	this._logHelper.Trace("******************** Starts lap number: "+attempNumber, this.getClass().getSimpleName());
        	
        	for(ComplexOntologyResource resource : originalResourceList) {
        		processedNumber++;
        		try {
        			while(!resource.isDeleted()) {
        				Delete(resource.getShortGnossId(), getLoadIdentifier(), processedNumber==originalResourceList.size());
        				numResourcesLeft--;
        				
        				this._logHelper.Debug("Successfully deleted the resource with ID: "+resource.getGnossId()+". "+numResourcesLeft +"resources left", this.getClass().getSimpleName());
        				resource.setDeleted(true);
        				break;
        			}
        		}catch(Exception ex) {
        			this._logHelper.Error("ERROR deleting: "+processedNumber+" of "+originalResourceList.size()+"\t ID: "+resource.getGnossId()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
        		}
        	}
        	this._logHelper.Debug("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
        	originalResourceList= new ArrayList<ComplexOntologyResource>();
        	originalResourceList=resourcesToDelete;
        	resourceList=originalResourceList;
        }  
	}
	
	/**
	 * Persistent delete of a resources list. if not specified, deletes the attachments of the resource.
	 * @param guidList Resource identifiers list
	 * @param deleteAttaches Indicates if the attached resources must be deleted
	 * @throws Exception exception 
	 */
	public void PersistentDeleteResourceIdList(ArrayList<UUID> guidList, boolean deleteAttaches) throws Exception {
		int count=guidList.size();
		for(UUID uuid : guidList) {
			count--;
			PersistentDelete(uuid, deleteAttaches, count==0);
		}
	}
	
	/**
	 * Persistent delete of a resources list
	 * @param resourceList Resources list to delete
	 * @param deletedAttached Indicates if the attached resources must be deleted
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void PeersistentDeleteResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean deletedAttached, int numAttemps) {
		int processedNumber = 0;
        int attempNumber = 0;
        int numResourcesLeft = resourceList.size();

        boolean last = false;
        
        ArrayList<ComplexOntologyResource> originalResourceList= new ArrayList<ComplexOntologyResource>();
        originalResourceList=resourceList;
        ArrayList<ComplexOntologyResource> resourcesToDelete = new ArrayList<ComplexOntologyResource>();
        resourcesToDelete=resourceList;
        
        int resourcesToModify=0;
		ArrayList<ComplexOntologyResource> resourceList1= new ArrayList<ComplexOntologyResource>();
		for(ComplexOntologyResource cor : originalResourceList) {
			if (cor.isDeleted()==false) {
				resourcesToModify++;
			}
		}
		int j=0;
		for(ComplexOntologyResource cor : resourcesToDelete) {
			if (cor.isDeleted()==false) {
				resourceList1.add(cor);
				j++;
			}
		}
        
        while(originalResourceList!=null && resourcesToModify>0 && attempNumber<= numAttemps) {
        	attempNumber++;
        	this._logHelper.Trace("******************** Starts lap number: "+attempNumber, this.getClass().getSimpleName());
        	
        	for(ComplexOntologyResource resource : resourceList1) {
        		processedNumber++;
        		try {
        			if(processedNumber==j) {
        				last=true;
        			}
        			
        			while (true) {
        				resource.setDeleted(PersistentDelete(resource.getShortGnossId(), deletedAttached, last));
        				numResourcesLeft--;
        				this._logHelper.Debug("Successfully deleted the resource with ID: "+resource.getGnossId()+". "+numResourcesLeft+" resources left", this.getClass().getSimpleName());
        				break;
        			}
        		}catch(Exception ex) {
        			this._logHelper.Error("ERROR deleting: "+processedNumber+" of "+originalResourceList.size()+"\t ID: "+resource.getGnossId()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
        		}
        	}
        	this._logHelper.Debug("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
        	originalResourceList= new ArrayList<ComplexOntologyResource>();
        	originalResourceList=resourcesToDelete;
        }
	}
	
	//endRegion
	
	//region LOAD
	
	/**
	 * Loads a basic ontology resource
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void LoadBasicOntologyResourceListIntNote(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		LoadBasicOntologyResourceListInt(resourceList, hierarquicalCategories, TiposDocumentacion.note, numAttemps);
		this._logHelper.Debug("Resources succesfully loaded. End of load");
	}
	
	
	/**
	 * Loads a basic ontology resource
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 2. Number of retries loading of the failed load of a resource
	 */
	public void LoadBasicOntologyResourceListLink(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		LoadBasicOntologyResourceListInt(resourceList, hierarquicalCategories, TiposDocumentacion.hyperlink, numAttemps);
		this._logHelper.Debug("Resources succesfully loaded. End of load");
	}
	
	
	/**
	 * Loads a basic ontology resource
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void LoadBasicOntologyResourceListFile(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		LoadBasicOntologyResourceListInt(resourceList, hierarquicalCategories, TiposDocumentacion.server_file, numAttemps);
		this._logHelper.Debug("Resources succesfully loaded. End of load");
	}
	
	
	/**
	 * Loads a basic ontology resource list with a link to a Youtube video with resource type HyperLink
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void LoadBasicOntologyResourceListLinkVideo(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		LoadBasicOntologyResourceListIntVideo(resourceList, hierarquicalCategories, TiposDocumentacion.hyperlink, numAttemps);
		this._logHelper.Debug("Resources succesfully loaded. End of load");
	}
	
	
	/**
	 * Loads a basic ontology resource list with resource type Video
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void LoadBasicOntologyResourceListVideo (ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		LoadBasicOntologyResourceListInt( resourceList, hierarquicalCategories, TiposDocumentacion.video, numAttemps);
		this._logHelper.Debug("Resources succesfully loaded. End of load");
	}
	
	//endRegion
	
	
	/**
	 * Adds one or more properties to an auxiliary entity to a loaded resource. It influences the value of the resource searches. If not exists, an auxiliary entity is created
	 * @param resourceTriples  Contains as a key the resource UUID identifier to modify and as a value a AuxiliaryEntitiesTriplesToInclude list of the resource properties that will be inserted.
     *    For example: in http://www.gnoss.com/items/Application_223b30c1-2552-4ed0-ba5f-e257585b08bf_9c126c3a-7850-4cdc-b176-95ae6fd0bb78 the entity identifier is: 9c126c3a-7850-4cdc-b176-95ae6fd0bb78
     *    If an entity has more than one property, that UUID indicates that all properties belong to the same entity. Without that UUID, an entity is created for each property
	 * @param communityShortName Indicates whether the home must be updated
	 * @param numAttemps Default 2. Number of retries loading of the failed load of a resource
	 * @param publishHome Community short name where the AuxiliaryEntitiesTriplesToInclude will be loaded
	 * @param userId Indicates whether the properties have been inserted in the auxiliar entity 
	 * @return boolean T or F 
	 */
	public boolean InsertAuxiliarEntityOnPropertiesLoadedResource (HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome, UUID userId) {
		if(!communityShortName.isEmpty()) {
			return InsertAuxiliarEntityOnPropertiesLoadedResourceInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome, userId);
		}else {
			return InsertAuxiliarEntityOnPropertiesLoadedResourceInt(resourceTriples, communityShortName, numAttemps, publishHome, userId);
		}
	}
	
	
	/**
	 * Gets the ontology name (with extension) from the url of the ontology
	 * @param urlOntology URL of the ontology
	 * @return String with the ontology name 
	 */
	public static String GetOntologyNameWithExtensionFromUrlOntology(String urlOntology) {
		return urlOntology.substring(urlOntology.lastIndexOf(StringDelimiters.Slash)+ StringDelimiters.Slash.length());
	}
	
	
	/**
	 * Sorts the multilanguage list of title or description with the main language in the first element
	 * @param listToOrder Lista posiblemente desordenada
	 * @return The list with the main language in the first element
	 */
	public ArrayList<Multilanguage> ShortMultimediaTitleDescriptionString (ArrayList<Multilanguage> listToOrder){
		String mainLanguage=CommunityApiWrapper.getCommunityMainLanguage();
		
		if(mainLanguage=="") {
			mainLanguage=Languages.Spanish;
		}
		else if(mainLanguage==null) {
			mainLanguage=Languages.Spanish;
		}
		
		Multilanguage firstElement = new Multilanguage();
		
		for(Multilanguage titleDescription : listToOrder) {
			if(titleDescription.getLanguage()==mainLanguage) {
				firstElement=titleDescription;
			}
		}
		ArrayList<Multilanguage> orderedList = new ArrayList<Multilanguage>();
		orderedList.add(firstElement);
		
		for(Multilanguage tituloDescripcion : listToOrder) {
			orderedList.add(tituloDescripcion);
		}
		return orderedList;
	}
	
	
	/**
	 * Gets an OAuth signed url
	 * @param url url to sign
	 * @return Signed url string 
	 * @throws SignatureException Signature Exception
	 * @throws MalformedURLException Mal formed URL Exception 
	 * @throws URISyntaxException URI Syntax Exception 
	 */
	public String  GetStringForUrl(String url) throws SignatureException, MalformedURLException, URISyntaxException {
		
		String sign=getOAuthInstance().getSignedUrl(url);
		return sign.replace("&", ",").replace("url", "");		
	}
	
	/**
	 * Gets the resource rdf and downloads it in the indicated directory path
	 * @param domain Domain where the community belongs to
	 * @param resourceId Resource short identifier
	 * @param directoryPath Directory path where the resource will be downloaded
	 * @return Resource rdf
	 * @throws IOException IO Exception 
	 */
	public String GetBasicOntologyResourceRdf(String domain, String resourceId, String directoryPath) throws IOException {
		String rdf="";
		String resourceUrl=domain+"/comunidad/"+getCommunityShortName()+"/recurso/nombre/"+resourceId;
		String urlRdf=resourceUrl+"?rdf";
		String filePath=directoryPath+"basicOntologyResouceRdf.rdf";
		
		WebClient client= WebClient.create();
		
		URL url= new URL(urlRdf);
		
		URLConnection urlCon=url.openConnection();
		String rdf_final="";
		
		try {
			
			InputStream is= urlCon.getInputStream();
			FileOutputStream fos= new FileOutputStream("C:/recurso.txt");
			byte[] array= new byte[1024];
			int leido=is.read(array);
			while(leido>0) {
				fos.write(array, 0, leido);
				leido=is.read(array);
			}
			
			File file= new File("C:/recurso.txt");
			FileReader fr= new FileReader(file);
			BufferedReader br= new BufferedReader(fr);
			
			String linea=br.readLine();
			while (linea!=null) {
				rdf+=linea+" ";
				linea=br.readLine();
			}
			
			byte [] redf1=rdf.getBytes("UTF-8");
			rdf_final=new String(redf1);
			
			//No sé si realmente esto sirve 
			client.head().header("Referer", "Referer:"+urlRdf);
			client.head().header("User-Agent", "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36 gnossspider");
			client.head().header("Authorization", "Authorization: OAuth \n"+ GetStringForUrl(resourceUrl));
		
			file.delete();
			fr.close();
			br.close();
			is.close();
			fos.close();
		}
		catch(Exception ex) {
			this._logHelper.Debug("Error downloading file: "+urlRdf+". Error "+ex.getMessage());
		}
		return rdf_final;
	}
	
	public void DownloadFilesFromURL(String URL, String fileName) {
		GnossWebClient webCLient= (GnossWebClient) WebClient.create();
		
		try {
			webCLient.head().header("Referer", "Referer:"+URL);
			webCLient.head().header("User-Agent", "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36 gnossspider");
			String sign= getSignForUrl(URL);
			webCLient.head().header("Authorization", "Authorization: OAuth \n"+ GetStringForUrl(sign));
			DownloadFile(URL, fileName);
		}catch(Exception ex) {
			
			System.out.println("File "+fileName+" not downloaded. "+ex.getMessage());
		}

	}
	
	
	   public String getSignForUrl(String url) throws SignatureException, MalformedURLException, URISyntaxException
       {
           String sign = OAuthInfo.GetSignedUrl(url);
           return sign.replace("&", ",").replace("url", "");
       }
	   
	/*
	public ArrayList<String> GetAutomaticLabelingTags(String title, String description) throws GnossAPIArgumentException{
		ArrayList<String> tagsList=null;
		
		if(title.isEmpty() && description.isEmpty()) {
			throw new GnossAPIArgumentException("Both parameters at GetAutomaticLabelingTags cannot be empty at the same time. At least one of them must have value.");
		}else {
			
		}
	}*/
	   public void DownloadFile(String URL, String fileName) throws IOException {
		   
		   URL url= new URL(URL);
		   URLConnection urlCon=url.openConnection();
		   InputStream is = urlCon.getInputStream();
		   FileOutputStream fos = new FileOutputStream(fileName);
		   byte [] array = new byte[1000];
		   int leido = is.read(array);
		   while (leido > 0) {
		      fos.write(array,0,leido);
		      leido=is.read(array);
		   }
		   is.close();
		   fos.close();
	   }
	
	
	/**
	 * Get the community members email list
	 * @return ArrayList ArrayList
	 */
	public ArrayList<String> GetCommunityMembersEmailList(){
		ArrayList<String> emails= new ArrayList<String>();
		HashMap<UUID, String> emailPerson =(HashMap<UUID, String>) CommunityApiWrapper.getCommunityPersonIDEmail();
		
		for(UUID personaId : emailPerson.keySet()) {
			emails.add(emailPerson.get(personaId));
		}
		return emails;
	}
	
	/**
	 * Sets the resource main image. The resource and the image must be loaded.  
	 * @param resourceId resource identifier
	 * @param imageName image name string
	 * @param sizes Availables sizes of the image, the main size must be the first of the list. [IMGPrincipal][318,234,992,]cce87492-2a13-4fc5-80a9-b3d59b63a2f1.jpg
	 * @throws Exception exception 
	 */
	public void SetMainImageLoadedImage(UUID resourceId, String imageName, ArrayList<String> sizes) throws Exception {
		String sizeMask="[";
		for(String size : sizes) {
			sizeMask+=sizeMask +size;
		}
		sizeMask=sizeMask+"]";
		String image="";
		if(!imageName.isEmpty()) {
			image="[IMGPrincipal]"+sizeMask+imageName;
		}
		System.out.println("\"Image string, the first number will be the main one: "+image);
		setMainImage(resourceId, image);
		
		if(!image.isEmpty()) {
			this._logHelper.Debug("Correct main image setting of resource "+resourceId+", of the new main image is  "+sizes.get(0)+"and itrs name is "+imageName);
		}
		else {
			this._logHelper.Debug("The main image of the resources "+resourceId+ " has been deleted");
		}
	}
}
