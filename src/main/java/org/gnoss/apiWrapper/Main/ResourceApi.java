package org.gnoss.apiWrapper.Main;

import org.gnoss.apiWrapper.ApiModel.*;
import org.gnoss.apiWrapper.Excepciones.*;
import org.gnoss.apiWrapper.Helpers.*;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.gnoss.apiWrapper.Utilities.StringUtilities;
import org.gnoss.apiWrapper.Web.GnossWebClient;
import org.gnoss.apiWrapper.models.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


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
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
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

	@Override
	protected void LoadEnvironmentVariables() throws GnossAPIException
	{
		if (System.getenv("ontologyName") != null) {
		    OntologyName = System.getenv("ontologyName");
		} else {
		    throw new GnossAPIException("The environment variables doesn't contains ontologyName");
		}
		
	    super.LoadEnvironmentVariables();
	}
	

	/**
	 * Constructor of ResourceApi
	 * @param configFilePath Configuration file path, with a structure like http://api.gnoss.com/v3/exampleConfig.txt
	 * @throws GnossAPIException Gnoss API Exception
	 * @throws ParserConfigurationException parser configuration exception
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
	public void loadComplexSemanticResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarch
	 */
	public void loadComplexSemanticResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 5);
	}

	/**
	 * Load resources of main entities in an otology and a community 
	 * @param resourceList List of resources to load
	 * @param hierarchycalCategories Indicates whether the categories has hierarchy
	 * @param ontology Ontology where resource will be loaded
	 * @param communityShortName Community short name where the resources will be loaded
	 */
	public void loadComplexSemanticResourceListWithOntologyAndCommunity(ArrayList<ComplexOntologyResource> resourceList, boolean hierarchycalCategories, String ontology, String communityShortName){
		loadComplexSemanticResourceListWithOntologyAndCommunityInt(resourceList, hierarchycalCategories, ontology, communityShortName);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @param rdfsPath Path to save the RDF, if necessary
	 */
	public void loadComplexSemanticResourceListSavingLocalRdf(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps, String rdfsPath){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, null, rdfsPath);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 */
	public void loadComplexSemanticResourceListSavingLocalRdf(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, null, null);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 */
	public void loadComplexSemanticResourceListSavingLocalRdf(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 5, null, null);
	}

	/**
	 * Load resources of main entities in a community
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param communityShortName Number of retries loading of the failed load of a resource
	 * @param numAttemps Defined if it is necessary the load in other community that the specified in the OAuth
	 */
	public void loadComplexSemanticResourceListCommunityShortName(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String communityShortName, int numAttemps){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, communityShortName);
	}

	/**
	 * Load resources of main entities in a community
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param communityShortName Number of retries loading of the failed load of a resource	 
	 */
	public void loadComplexSemanticResourceListCommunityShortName(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String communityShortName){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 5, communityShortName);
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
	public String loadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath, boolean hierarquicalCategories, boolean isLast, int numAttemps) throws GnossAPIArgumentException{
		if(StringUtils.isEmpty(rdfsPath)){
			throw new GnossAPIArgumentException("You must set the parameter rdfsPath");
		}

		return loadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, null, rdfsPath);
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
	public String loadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath, boolean hierarquicalCategories, boolean isLast) throws GnossAPIArgumentException{
		return loadComplexSemanticResourceSaveRdf(resource, rdfsPath, hierarquicalCategories, isLast, 2);
	}

	/**
	 * Load a complex semantic resource ComplexSemanticResource saving the resource rdf
	 * @param resource Resource to load
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @return Resource identifier string
	 * @throws GnossAPIArgumentException Gnoss argument exception 
	 */
	public String loadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath, boolean hierarquicalCategories) throws GnossAPIArgumentException{
		return loadComplexSemanticResourceSaveRdf(resource, rdfsPath, hierarquicalCategories, false, 2);
	}

	/**
	 * Load a complex semantic resource ComplexSemanticResource saving the resource rdf
	 * @param resource Resource to load
	 * @param rdfsPath Path to save the RDF, if necessary
	 * @throws GnossAPIArgumentException Gnoss argument exception 
	 * @return Resource identifier string
	 */
	public String loadComplexSemanticResourceSaveRdf(ComplexOntologyResource resource, String rdfsPath) throws GnossAPIArgumentException{
		return loadComplexSemanticResourceSaveRdf(resource, rdfsPath, false, false, 2);
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
	public String loadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName, boolean hierarquicalCategories, boolean isLast, int numAttemps){
		return loadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, communityShortName);
	}

	/**
	 * Load a complex semantic resource in the community
	 * @param resource Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @return Resource identifier string
	 */
	public String loadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName, boolean hierarquicalCategories, boolean isLast){
		return loadComplexSemanticResourceCommunityShortName(resource, communityShortName, hierarquicalCategories, isLast, 2);
	}

	/**
	 * Load a complex semantic resource in the community
	 * @param resource Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @return Resource identifier string
	 */
	public String loadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName, boolean hierarquicalCategories){
		return loadComplexSemanticResourceCommunityShortName(resource, communityShortName, hierarquicalCategories, false, 2);
	}

	/**
	 * Load a complex semantic resource in the community
	 * @param resource Resource to load
	 * @param communityShortName Defined if it is necessary the load in other community that the specified in the OAuth
	 * @return Resource identifier string
	 */
	public String loadComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, String communityShortName){
		return loadComplexSemanticResourceCommunityShortName(resource, communityShortName, false, false, 2);
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
	public String loadComplexSemanticResourceRdf(ComplexOntologyResource resource, byte[] rdfFile, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName, String rdfsPath){
		BufferedWriter bw = null;
	    FileWriter fw = null;
	    try {
	        if (resource.getTextCategories() != null && resource.getTextCategories().size() > 0) {
	            if (hierarquicalCategories) {
	                resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
	            } else {
	                resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
	            }
	        }

	        String documentId = "";
	        resource.setRdfFile(rdfFile);
	        LoadResourceParams model = getResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
	        documentId = createComplexOntologyResource(model);
	        resource.setUploaded(true);

	        _logHelper.Trace("Loaded: \tID: " + resource.getShortGnossId() + "\tTitle: " + resource.getTitle() + "\tResourceID: " + resource.getGnossId());
	        
	        if (resource.getShortGnossId() != null && 
	            !resource.getShortGnossId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000")) && 
	            !documentId.equals(resource.getGnossId())) {
	            _logHelper.Trace("Resource loaded with the id: " + documentId + "\nThe IDGnoss provided to the method is different from the returned by the API");
	        }

	        if (!StringUtils.isEmpty(rdfsPath)) {
	            String directoryPath = rdfsPath + "/" + getOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl());
	            File directory = new File(directoryPath);
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            String rdfFilePath = directoryPath + "/" + resource.getShortGnossId() + ".rdf";
	            File file = new File(rdfFilePath);
	            if (!file.exists()) {
	                try {
	                    fw = new FileWriter(file);
	                    bw = new BufferedWriter(fw);
	                    bw.write(resource.getStringRdfFile());
	                } catch (Exception e) {
	                    _logHelper.Error("Error writing the rdf file of the resource: \tID: " + resource.getShortGnossId() + ". Title: " + resource.getTitle() + ". Message: " + e.getMessage());
	                }
	            }
	        }

	        resource.setGnossId(documentId);
	    } catch (GnossAPICategoryException gacex) {
	        _logHelper.Error("Error loading the resource: \tID: " + resource.getShortGnossId() + " . Title: " + resource.getTitle() + ". Message: " + gacex.getMessage());
	    } catch (Exception ex) {
	        _logHelper.Error("Error loading the resource: \tID: " + resource.getShortGnossId() + " . Title: " + resource.getTitle() + ". Message: " + ex.getMessage());
	    } finally {
	        try {
	            if (bw != null) {
	                bw.close();
	            }
	        } catch (IOException e) {
	            // Error al cerrar el stream
	        }
	        
	        try {
	            if (fw != null) {
	                fw.close();
	            }
	        } catch (IOException e) {
	            // Error al cerrar el writer
	        }
	    }
	    return resource.getGnossId();
	}

	/**
	 * @deprecated This method is deprecated, please use {@link #uploadImages()} instead.
	 */
	@Deprecated
	public void massiveUploadFiles(LoadResourceParams resource){
		try{
			uploadImages(resource.getResource_id(), resource.getResource_attached_files(), resource.getMain_image());

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
	 * @deprecated This method is deprecated, please use {@link #uploadImages()} instead.
	 */
	@Deprecated
	public void massiveUploadImages(LoadResourceParams resource) throws GnossAPIArgumentException{
		try{
			uploadImages(resource.getResource_id(), resource.getResource_attached_files(), resource.getMain_image());

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
	public boolean loadPartitionedXmlOntology(byte[] xmlFile, String fileName){
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
	public boolean loadPartitionedOntology(byte[] ontologyFile, String fileName){
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
	public void modifyComplexOntologyResourceRDF(ComplexOntologyResource resource, byte[] rdfFile, boolean hierarquicalCategories, boolean isLast){
		LogHelper.getInstance().Trace("******************** Begin the resource modification: " + resource.getGnossId());

		try{
			if(resource.getTextCategories() != null && resource.getTextCategories().size() > 0){
				if(hierarquicalCategories){
					resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
				else{
					resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
			}

			resource.setRdfFile(rdfFile);
			LoadResourceParams model = getResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
			resource.setModified(modifyComplexOntologyResource(model));

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
	public void modifyComplexSemanticResourceCommunityShortName(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, String communityShortName){
		LogHelper.getInstance().Trace("******************** Begin modification of resource: " + resource.getGnossId());
		try{
			if(resource.getTextCategories() != null){
				if(hierarquicalCategories){
					resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
				}
				else{
					resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
				}
			}

			LoadResourceParams model = getResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
			resource.setModified(modifyComplexOntologyResource(model));

			if(resource.isModified()){
				LogHelper.getInstance().Debug("Successfully modified the resource with id: " + resource.getId() + " and Gnoss identifier " + resource.getShortGnossId() + "belonging to the ontology '" + resource.getOntology().getOntologyUrl() + "' and RdfType = '" + resource.getOntology().getRdfType() + "'");
			}
			else{
				LogHelper.getInstance().Error("The resource with id: " + resource.getShortGnossId() + " of the ontology '" + resource.getOntology().getOntologyUrl() + "' has not been modified.");
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
	public void modifyComplexOntologyResourceWithImages(ComplexOntologyResource resource, boolean hierarquicalCategories, int numAttemps){
		int attempNumber = 0;

		while(attempNumber < numAttemps){
			attempNumber++;
			LogHelper.getInstance().Trace("******************** Begin the lap number: " + attempNumber);

			try{
				if(resource.getTextCategories() != null){
					if(hierarquicalCategories){
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
				}

				LoadResourceParams model = getResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, true);
				resource.setModified(modifyComplexOntologyResource(model));

				LogHelper.getInstance().Debug("Successfully modified the resource with ID: " + resource.getId() + " and Gnoss identifier " + resource.getShortGnossId());
				if(resource.isModified()){
					try{
						OntologyProperty propOntoImage = null;
						for(OntologyProperty ontology : resource.getOntology().getProperties()){
							if(ontology.getClass().equals(DataTypes.OntologyPropertyImage.getClass())){
								propOntoImage = ontology;
							}
						}
						if(propOntoImage != null) {
							
						
						String prefijoPredicado = propOntoImage.getName().split(Arrays.toString(CharArrayDelimiters.Colon))[0];
						String nombreEtiquetaSinPrefijo = propOntoImage.getName().split(Arrays.toString(CharArrayDelimiters.Colon))[1];
						String nombreImagen = propOntoImage.getValue().toString();
						String predicado = "";
						for(String prefix : resource.getOntology().getPrefixList()){
							if(prefix.contains("xmlns:" + prefijoPredicado)){
								predicado = prefix.split(Arrays.toString(CharArrayDelimiters.Equal))[1].toString().replace("\"", "") + nombreEtiquetaSinPrefijo;
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
								adjunto.setRdf_attached_file(resource.getAttachedFiles().get(i));
								adjunto.setDelete_file(resource.getAttachedFiles().get(i) == null);
								i++;
								resourceAttachedFiles.add(adjunto);
							}
						}

						modifyTripleList(resource.getShortGnossId(), triplesList, resource.getPublishInHome(), resource.getMainImage(), resourceAttachedFiles, true);
						}
						else {
							LogHelper.getInstance().Error("ERROR geting the ontology property of the resource's image: " + resource.getId() + ". Title: " + resource.getTitle());
						}
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

	public void modifyComplexOntologyResourceWithImages(ComplexOntologyResource resource, boolean hierarquicalCategories){
		modifyComplexOntologyResourceWithImages(resource, hierarquicalCategories, 5);
	}

	/**
	 * Modifies the complex ontology resource
	 * @param resource Resource to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 */
	public void modifyComplexOntologyResource(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast){
		LogHelper.getInstance().Trace("******************** Begin the resource modification: " + resource.getGnossId());

		try{
			if(resource.getTextCategories() != null && resource.getTextCategories().size() > 0){
				if(hierarquicalCategories){
					resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
				else{
					resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
			}

			LoadResourceParams model = getResourceModelOfComplexOntologyResource(getCommunityShortName(), resource, false, isLast);
			resource.setModified(modifyComplexOntologyResource(model));

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

	public boolean uploadImages(UUID resourceId, List<SemanticAttachedResource> imageList, String mainImage) throws GnossAPIArgumentException{
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
	public String loadComplexSemanticResourceRdf(ComplexOntologyResource resource, byte[] rdfFile){
		return loadComplexSemanticResourceRdf(resource, rdfFile, false, false, 5, null, null);	
	}

	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @return String LoadComplexSemanticResourceRdf
	 */
	public String loadComplexSemanticResourceRdf(ComplexOntologyResource resource, byte[] rdfFile, boolean hierarquicalCategories){
		return loadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, false, 5, null, null);	
	}

	/**
	 * Load a complex semantic resource in the community with a rdf file
	 * @param resource Resource to load
	 * @param rdfFile Path to save the RDF, if necessary
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param isLast There are not resources left to load
	 * @return String LoadComplexSemanticResourceRdf
	 */
	public String loadComplexSemanticResourceRdf(ComplexOntologyResource resource, byte[] rdfFile, boolean hierarquicalCategories, boolean isLast){
		return loadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, isLast, 5, null, null);
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
	public String loadComplexSemanticResourceRdf(ComplexOntologyResource resource, byte[] rdfFile, boolean hierarquicalCategories, boolean isLast, int numAttemps){
		return loadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, isLast, numAttemps, null, null);
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
	public String loadComplexSemanticResourceRdf(ComplexOntologyResource resource, byte[] rdfFile, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName){
		return loadComplexSemanticResourceRdf(resource, rdfFile, hierarquicalCategories, isLast, numAttemps, communityShortName, null);
	}

	public String getApiUrl(){
		return getOAuthInstance().getApiUrl();
	}

	/**
	 * Returns a guid from a resource large identifier. If it cannot get it, returns an empty guid.
	 * @param largeResourceId Resource large identifier
	 * @return UUID UUID ID
	 */
	public UUID getShortGuid(String largeResourceId){
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
	public void modifySecondaryResourcesList(ArrayList<SecondaryResource> resourceList) throws GnossAPIException{
		LogHelper.getInstance().Debug("Modifyng " + resourceList.size() + "resources");
		int left = resourceList.size();
		for(SecondaryResource rs : resourceList){
			if(modifySecondaryResource(rs)){
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
	public boolean modifySecondaryResource(SecondaryResource secondaryResource) throws GnossAPIException{
		boolean modified = false;

		try{
			modifySecondaryEntity(getOntologyUrl(), getCommunityShortName(), secondaryResource.getRdfFile());
			secondaryResource.setModified(true);
			modified = true;			
		}
		catch(Exception ex){
			String message = "The secondary resource with ID: " + secondaryResource.getId() + " cannot be modified. \n MESSAGE: " + ex.getMessage();
			LogHelper.getInstance().Error(message);
			secondaryResource.setModified(false);
			throw new GnossAPIException(message);
		}
		return modified;
	}

	/**
	 * Creates a complex ontology resource
	 * @param parameters parameters
	 * @return Resource identifier guid
	 */
	public String createComplexOntologyResource(LoadResourceParams parameters)
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
				prepareAttachedToLog(parameters.resource_attached_files);
				LogHelper.getInstance().Debug("Complex ontology resource not created: {JsonConvert.SerializeObject(parameters)}");
			}
		}
		catch (Exception ex)
		{
			prepareAttachedToLog(parameters.resource_attached_files);
			Gson jsonUtilities = new Gson();
			String json = jsonUtilities.toJson(parameters);
			LogHelper.getInstance().Error("Error trying to create a complex ontology resource. \r\n Error description: {ex.Message}. \r\n: Json: " + json);
		}

		return resourceId;
	}

	/**
	 * Modifies a complex ontology resouce
	 * @param parameters parameters
	 * @return If the resource was modified T or F 
	 */
	public boolean modifyComplexOntologyResource(LoadResourceParams parameters){
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
			prepareAttachedToLog(parameters.getResource_attached_files());
			Gson jsonUtilities = new Gson();
			String json = jsonUtilities.toJson(parameters);
			LogHelper.getInstance().Error("Error modifying resource " + parameters.getResource_id() + ".\r\n: Json: " + json);
		}

		return modified;
	}

	public void modifyProperty(UUID resourceId, String property, String newObject) throws Exception{
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
	public void modifyMultipleResourcesTripleList(HashMap<UUID, ArrayList<ModifyResourceTriple>> multipleResourceTriples, boolean publishHome, String mainImage, HashMap<UUID, ArrayList<SemanticAttachedResource>> multipleResourceAttachedFiles) throws Exception	{
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
	public HashMap<UUID, Boolean> insertPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, int numAttemps, boolean publishHome){
		return insertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}

	/**
	 * Method for adding one or more properties of a loaded resource. In IncluirTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToInclude list of the resource properties that will be included
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 * @return HashMap Indicates whether the properties have been added to the loaded resource
	 */
	public HashMap<UUID, Boolean> insertPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, int numAttemps){
		return insertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	/**
	 * Method for adding one or more properties of a loaded resource. In IncluirTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToInclude list of the resource properties that will be included
	 * @return HashMap Indicates whether the properties have been added to the loaded resource
	 */
	public HashMap<UUID, Boolean> insertPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples){
		return insertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName());
	}

	private HashMap<UUID, Boolean> insertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
	    HashMap<UUID, Boolean> result = new HashMap<UUID, Boolean>();
	    int processedNumber = 0;
	    int attempNumber = 0;
	    HashMap<UUID, ArrayList<TriplesToInclude>> toInsert = new HashMap<UUID, ArrayList<TriplesToInclude>>(resourceTriples);
	    
	    while(toInsert != null && toInsert.size() > 0 && attempNumber < numAttemps){
	        int i = 0;
	        int contResource = toInsert.keySet().size();
	        
	        Iterator<UUID> iterator = toInsert.keySet().iterator();
	        
	        while(iterator.hasNext()){
	            UUID docID = iterator.next();
	            i++;
	            ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
	            attempNumber++;
	            processedNumber++;
	            
	            for(TriplesToInclude iT : toInsert.get(docID)){
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
	                modifyTripleList(docID, listaValores, publishHome, null, null, endOfLoad);

	                LogHelper.getInstance().Debug(processedNumber + " of " + toInsert.size() + " Object: " + docID + ". Resource: " + toInsert.get(docID).toArray());
	                
	                iterator.remove();
	                result.put(docID, true);
	            }
	            catch(Exception ex){
	                LogHelper.getInstance().Error("Resource " + docID + " : " + ex.getMessage());
	                result.put(docID, false);
	            }
	        }
	        LogHelper.getInstance().Debug("******************** Lap number: " + attempNumber + " finished");
	    }
	    return result;
	}

	private HashMap<UUID, Boolean> insertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName, int numAttemps){
		return insertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, false);
	}

	private HashMap<UUID, Boolean> insertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName){
		return insertPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), 2, false); 		
	}

	/**
	 * Method for modifying one or more properties of a loaded resource. In ModificarTriples can indicate whether title or description. By default false two fields. 
	 * It influences the value of the resource searches
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a TriplesToModify list of the resource properties that will be modified
	 * @param numAttemps Indicates whether the home must be updated
	 * @param publishHome Default 2. Number of retries loading of the failed load of a resource
	 * @return HashMap Indicates whether the properties have been modified of the loaded resource
	 */
	public HashMap<UUID, Boolean> modifyPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, int numAttemps, boolean publishHome){
		return modifyPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}

	public HashMap<UUID, Boolean> modifyPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, int numAttemps){
		return modifyPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	public HashMap<UUID, Boolean> modifyPropertiesLoadedResources(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples){
		return modifyPropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName());
	}

	private HashMap<UUID, Boolean> modifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
	    HashMap<UUID, Boolean> result = new HashMap<UUID, Boolean>();
	    int processedNumer = 0;
	    int attempNumber = 0;

	    ArrayList<String> valores = new ArrayList<String>();
	    HashMap<UUID, ArrayList<TriplesToModify>> toModify = new HashMap<UUID, ArrayList<TriplesToModify>>(resourceTriples);
	    
	    while(toModify != null && toModify.size() > 0 && attempNumber < numAttemps){
	        int i = 0;
	        int contResources = toModify.keySet().size();
	        
	        Iterator<UUID> iterator = toModify.keySet().iterator();
	        
	        while(iterator.hasNext()){
	            UUID docID = iterator.next();
	            i++;
	            ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
	            attempNumber++;
	            processedNumer++;
	           
	            for(TriplesToModify mT : toModify.get(docID)){
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
	                modifyTripleList(docID, listaValores, publishHome, null, null, endOfLoad);

	                LogHelper.getInstance().Debug(processedNumer + " of " + toModify.size() + ". Object: " + docID + ". Resource: " + toModify.get(docID).toArray());
	                
	                iterator.remove();
	                result.put(docID, true);
	            }
	            catch(Exception ex){
	                LogHelper.getInstance().Error("Resource " + docID + " : " + ex.getMessage());
	                result.put(docID, false);
	            }
	        }
	        LogHelper.getInstance().Debug("******************** Lap number: " + attempNumber + " finished");
	    }

	    return result;
	}

	private HashMap<UUID, Boolean> modifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps){
		return modifyPropertiesLoadedResourcesInt(resourceTriples, communityShortName, numAttemps, false);
	}


	private HashMap<UUID, Boolean> modifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName){
		return modifyPropertiesLoadedResourcesInt(resourceTriples, communityShortName, 2, false);
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
	public void modifyTripleList(UUID resourceID, ArrayList<ModifyResourceTriple> tripleList, boolean publishHome, String mainImage, ArrayList<SemanticAttachedResource> resourceAttachedFiles, boolean endOfLoad) throws Exception{
		ModifyResourceTripleListParams model = null;
		try{
			String url = getApiUrl() + "/resource/modify-triple-list";
			model = new ModifyResourceTripleListParams();
			model.setResource_triples(tripleList);
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
			LogHelper.getInstance().Error("Error modifying resource triple list. \r\n");
			throw new Exception("Error modifying resource triple list. \r\n");
		}
	}

	/**
	 * Changes the current ontology by the indicated ontology.
	 * @param newOntology New ontology name
	 */
	public void changeOntology(String newOntology){
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
	public void loadSecondaryResourceList(ArrayList<SecondaryResource> resourceList) throws GnossAPIException{
		for(SecondaryResource rs : resourceList){
			loadSecondaryResource(rs);
		}
	}

	public boolean loadSecondaryResource(SecondaryResource resource) throws GnossAPIException{
		boolean success = false;

		try {
			createSecondaryEntity(resource.getSecondaryOntology().getOntologyUrl(), getCommunityShortName(), resource.getRdfFile());
			LogHelper.getInstance().Debug("Loaded secondary resource with ID: " + resource.getSecondaryOntology().getIdentifier());
			success = true;
			resource.setUploaded(true);
			resource.setId(resource.getSecondaryOntology().getIdentifier());
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
		obtaingGraphsUrl();

		if(!StringUtils.isEmpty(OntologyName)){
			if(!OntologyName.contains(".owl")){
				_ontologyUrl = GraphsUrl + "Ontologia/" + OntologyName + ".owl";
			}
			else{
				_ontologyUrl = GraphsUrl + "Ontologia/" + OntologyName;
			}
		}
	}

	private void prepareAttachedToLog(List<SemanticAttachedResource> resourceAttachedFiles){
		if(resourceAttachedFiles != null){
			for(SemanticAttachedResource adjunto : resourceAttachedFiles){
				adjunto.setRdf_attached_file(null);
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
	public SparqlObject virtuosoQuery(String selectPart, String wherePart, String ontologyName) throws GnossAPIException{
		LogHelper.getInstance().Trace("Entering the method VirtuosoQuery");
		return virtuosoQueryInt(selectPart, wherePart, ontologyName);
	}

	/**
	 * Allows a virtuoso query, setting the 'SELECT' and 'WHERE' parts of the query and the community identifier
	 * @param selectPart The 'SELECT' query part
	 * @param wherePart The 'WHERE' query part
	 * @param communityId Community identifier
	 * @param useMasterServer Use virtuoso master connection if true and the affinity connection if false
	 * @return SparqlObject with the query result
	 */
	public SparqlObject virtuosoQuery(String selectPart, String wherePart, UUID communityId, boolean useMasterServer) throws GnossAPIException {
	    _logHelper.Trace("Entering in the method VirtuosoQuery");
	    return virtuosoQueryInt(selectPart, wherePart, communityId.toString(), useMasterServer);
	}
	
	/**
	 * Allows a virtuoso query with master server parameter
	 * @param selectPart The 'SELECT' query part
	 * @param wherePart The 'WHERE' query part
	 * @param ontologyName Graph name where the query runs
	 * @param useMasterServer Use virtuoso master connection if true
	 * @return SparqlObject with the query result
	 */
	public SparqlObject virtuosoQuery(String selectPart, String wherePart, String ontologyName, boolean useMasterServer) throws GnossAPIException {
	    _logHelper.Trace("Entering the method VirtuosoQuery");
	    return virtuosoQueryInt(selectPart, wherePart, ontologyName, useMasterServer);
	}
	
	/**
	 * Internal method for virtuoso query with master server parameter
	 * @param selectPart The 'SELECT' query part
	 * @param wherePart The 'WHERE' query part
	 * @param graph Graph name
	 * @param useMasterServer Use virtuoso master connection if true
	 * @return SparqlObject with the query result
	 */
	private SparqlObject virtuosoQueryInt(String selectPart, String wherePart, String graph, boolean useMasterServer) throws GnossAPIException {
	    _logHelper.Trace("Entering in the method: VirtuosoQueryInt");
	    _logHelper.Trace("SELECT: " + selectPart);
	    _logHelper.Trace("Graph name: " + graph);
	    _logHelper.Trace("WHERE: " + wherePart);
	    
	    SparqlObject SO = new SparqlObject();
	    
	    try {
	        _logHelper.Trace("Query start");
	        
	        String url = getApiUrl() + "/sparql-endpoint/query";
	        
	        SparqlQuery model = new SparqlQuery();
	        model.setOntology(graph);
	        model.setCommunity_short_name(getCommunityShortName());
	        model.setQuery_select(selectPart);
	        model.setQuery_where(wherePart);
	        model.setUse_virtuoso_balancer(useMasterServer);
	        
	        String response = WebRequestPostWithJsonObject(url, model);
	        
	        Gson jsonUtilities = new Gson();
	        SO = jsonUtilities.fromJson(response, SparqlObject.class);
	        
	        _logHelper.Trace("Query end");
	    } catch(Exception wex) {
	        String resultado = wex.getMessage();
	        throw new GnossAPIException("Could not make the query to Virtuoso.\n");
	    }
	    
	    _logHelper.Trace("Leaving the method");
	    return SO;
	}
	
	/**
	 * Allows a virtuoso query, setting the 'SELECT' and 'WHERE' parts of the query and the list of graphs
	 * @param selectPart The 'SELECT' query part
	 * @param wherePart The 'WHERE' query part
	 * @param graphs List of the graphs in which you want search
	 * @param useMasterServer Use virtuoso master connection if true and the affinity connection if false
	 * @return SparqlObject with the query result
	 * @throws GnossAPIException Gnoss API Exception
	 */
	public SparqlObject virtuosoQueryMultipleGraph(String selectPart, String wherePart, List<String> graphs, boolean useMasterServer) throws GnossAPIException {
	    LogHelper.getInstance().Trace("Entering the method");
	    return virtuosoQueryMultipleGraphInt(selectPart, wherePart, graphs, useMasterServer);
	}
	
	private SparqlObject virtuosoQueryMultipleGraphInt(String selectPart, String wherePart, List<String> graph_list, boolean useMasterServer) throws GnossAPIException {
	    LogHelper.getInstance().Trace("Entering in the method: VirtuosoQueryMultipleGraphInt");
	    LogHelper.getInstance().Trace("SELECT: " + selectPart);
	    LogHelper.getInstance().Trace("Graph list: " + graph_list);
	    LogHelper.getInstance().Trace("WHERE: " + wherePart);

	    SparqlObject sparqlObject = new SparqlObject();

	    try {
	        LogHelper.getInstance().Trace("Query start");

	        String url = getApiUrl() + "/sparql-endpoint/query-multiple-graph";

	        SparqlQueryMultipleGraph model = new SparqlQueryMultipleGraph();
	        model.setOntology_list(graph_list);
	        model.setCommunity_short_name(getCommunityShortName());
	        model.setQuery_select(selectPart);
	        model.setQuery_where(wherePart);
	        model.setUse_virtuoso_balancer(useMasterServer);

	        String response = WebRequestPostWithJsonObject(url, model);

	        Gson jsonUtilities = new Gson();
	        sparqlObject = jsonUtilities.fromJson(response, SparqlObject.class);

	        LogHelper.getInstance().Trace("Query end");
	    } catch(Exception wex) {
	        String resultado = wex.getMessage();
	        throw new GnossAPIException("Could not make the query " + selectPart + " " + wherePart + " to the graphs " + String.join(",", graph_list) + ".\nError: " + resultado);
	    }

	    LogHelper.getInstance().Trace("Leaving the method");
	    return sparqlObject;
	}
	
	private SparqlObject virtuosoQueryInt(String selectPart, String wherePart, String graph) throws GnossAPIException{
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
			model.setCommunity_short_name(getCommunityShortName());
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

	/**
     * Permite ejecutar una consulta virtuoso, estableciendo las partes 'SELECT' y 'WHERE' 
     * de la consulta y el nombre del grafo
     * 
     * @param selectPart La parte 'SELECT' de la consulta
     * @param wherePart La parte 'WHERE' de la consulta
     * @param ontologiaName Nombre del grafo donde se ejecuta la consulta (sin extensión '.owl')
     * @param useMasterServer Usar conexión master de virtuoso si es true y la conexión de afinidad si es false
     * @return Map con el resultado de la consulta (equivalente a DataSet)
     * @throws GnossAPIException Si hay error en la consulta
     */
    public Map<String, List<Map<String, String>>> virtuosoQueryDataSet(
            String selectPart, 
            String wherePart, 
            String ontologiaName, 
            boolean useMasterServer) throws GnossAPIException {
        
    	LogHelper.getInstance().Info(String.format("Entering the method: %s.%s", 
                this.getClass().getName(), 
                Thread.currentThread().getStackTrace()[1].getMethodName()));
        
        return virtuosoQueryIntDataSet(selectPart, wherePart, ontologiaName, useMasterServer);
    }
    
    /**
     * Sobrecarga con useMasterServer por defecto en true
     * 
     * @param selectPart La parte 'SELECT' de la consulta
     * @param wherePart La parte 'WHERE' de la consulta
     * @param ontologiaName Nombre del grafo donde se ejecuta la consulta (sin extensión '.owl')
     * @return Map con el resultado de la consulta (equivalente a DataSet)
     * @throws GnossAPIException Si hay error en la consulta
     */
    public Map<String, List<Map<String, String>>> virtuosoQueryDataSet(
            String selectPart, 
            String wherePart, 
            String ontologiaName) throws GnossAPIException {
        
        return virtuosoQueryDataSet(selectPart, wherePart, ontologiaName, true);
    }
    
    /**
     * Permite ejecutar una consulta virtuoso, estableciendo las partes 'SELECT' y 'WHERE' 
     * de la consulta y el identificador de la comunidad
     * 
     * @param selectPart La parte 'SELECT' de la consulta
     * @param wherePart La parte 'WHERE' de la consulta
     * @param communityId Identificador de la comunidad
     * @param useMasterServer Usar conexión master de virtuoso si es true y la conexión de afinidad si es false
     * @return Map con el resultado de la consulta (equivalente a DataSet)
     * @throws GnossAPIException Si hay error en la consulta
     */
    public Map<String, List<Map<String, String>>> virtuosoQueryDataSet(
            String selectPart, 
            String wherePart, 
            UUID communityId, 
            boolean useMasterServer) throws GnossAPIException {
        
    	LogHelper.getInstance().Info(String.format("Entering the method: %s", this.getClass().getName()));
        
        return virtuosoQueryIntDataSet(selectPart, wherePart, communityId.toString(), useMasterServer);
    }
    
    /**
     * Sobrecarga con useMasterServer por defecto en true
     * 
     * @param selectPart La parte 'SELECT' de la consulta
     * @param wherePart La parte 'WHERE' de la consulta
     * @param communityId Identificador de la comunidad
     * @return Map con el resultado de la consulta (equivalente a DataSet)
     * @throws GnossAPIException Si hay error en la consulta
     */
    public Map<String, List<Map<String, String>>> virtuosoQueryDataSet(
            String selectPart, 
            String wherePart, 
            UUID communityId) throws GnossAPIException {
        
        return virtuosoQueryDataSet(selectPart, wherePart, communityId, true);
    }
	
	private Map<String, List<Map<String, String>>> virtuosoQueryIntDataSet(
            String selectPart, String wherePart, String graph, boolean useMasterServer) 
            throws GnossAPIException {
        
		LogHelper.getInstance().Trace("Entering in the method", this.getClass().getName());
		LogHelper.getInstance().Trace(String.format("SELECT: %s", selectPart), this.getClass().getName());
		LogHelper.getInstance().Trace(String.format("Grafo name: %s", graph), this.getClass().getName());
		LogHelper.getInstance().Trace(String.format("WHERE: %s", wherePart), this.getClass().getName());
        
        Map<String, List<Map<String, String>>> dataSet = new HashMap<>();
        
        try {
        	LogHelper.getInstance().Trace("Query start", this.getClass().getName());
            
            String url = String.format("%s/sparql-endpoint/querycsv", getApiUrl());
            
            SparqlQuery model = new SparqlQuery();
            model.setOntology(graph);
            model.setCommunity_short_name(getCommunityShortName());
            model.setQuery_select(selectPart);
            model.setQuery_where(wherePart);
            model.setUse_virtuoso_balancer(useMasterServer);
            
            String response = WebRequestPostWithJsonObject(url, model);
            
            synchronized (dataSet) {
                leerResultadosCSV(response, graph, dataSet);
            }
            
            LogHelper.getInstance().Trace("Query end", this.getClass().getName());
            
        } catch (IOException ex) {
            String errorDescription = "Error en la consulta";            
            throw new GnossAPIException(
                String.format("Could not make the query %s %s to the graph %s.\nError: %s",
                    selectPart, wherePart, graph, errorDescription));
        }
        
        LogHelper.getInstance().Trace("Leaving the method", this.getClass().getName());
        return dataSet;
    }
	
	private void leerResultadosCSV(String resultados, String nombreTabla, 
            Map<String, List<Map<String, String>>> facetadoDS) {       
        if (facetadoDS.containsKey(nombreTabla)) {
            facetadoDS.get(nombreTabla).clear();
        } else {
            facetadoDS.put(nombreTabla, new ArrayList<>());
        }
        
        if (resultados == null || resultados.trim().isEmpty()) {
            return;
        }
        
        String[] lineas = resultados.split("\n");
        
        if (lineas.length > 1) {
            try {
                // Usar Apache Commons CSV para parsear
                byte[] byteArray = resultados.getBytes(StandardCharsets.UTF_8);
                InputStream stream = new ByteArrayInputStream(byteArray);
                Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
                
                CSVParser csvParser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreEmptyLines()
                        .withTrim()
                        .parse(reader);
                
                List<Map<String, String>> tableData = facetadoDS.get(nombreTabla);
                
                for (CSVRecord record : csvParser) {
                    Map<String, String> row = new HashMap<>();
                    for (String header : csvParser.getHeaderNames()) {
                        row.put(header, record.get(header));
                    }
                    tableData.add(row);
                }
                
                csvParser.close();
                
            } catch (IOException e) {
            	LogHelper.getInstance().Error("Error al parsear CSV");
            }
            
        } else if (lineas.length == 1) {
            // Solo hay cabeceras, crear estructura vacía con las columnas
            String[] columnas = lineas[0].split(",");
            List<Map<String, String>> tableData = facetadoDS.get(nombreTabla);
            
            // Crear un mapa vacío con las columnas como claves
            // Esto mantiene la estructura de columnas aunque no haya datos
            Map<String, String> emptyRow = new HashMap<>();
            for (String columna : columnas) {
                String nombreCol = columna.trim().replace("\"", "");
                emptyRow.put(nombreCol, null);
            }
        }
    }
	
	
	public String loadComplexSemanticResource(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, int numAttemps) {
		return loadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, null, null);
	}

	public String loadComplexSemanticResource(ComplexOntologyResource resource) {      
		return loadComplexSemanticResourceInt(resource, false, false, 5, null, null);
	}

	private void createSecondaryEntity(String ontology_url, String community_short_name, byte[] rdf) throws IOException, GnossAPIException{
		String url = getApiUrl() + "/secondary-entity/create";

		SecondaryEntityModel model = new SecondaryEntityModel();
		model.setOntology_url(ontology_url);
		model.setCommunity_short_name(community_short_name);
		model.setRdf(rdf);

		WebRequestPostWithJsonObject(url, model);

		LogHelper.getInstance().Debug("The secondary entity has been created in the graph " + ontology_url + " of the community " + community_short_name);
	}

	private void modifySecondaryEntity(String ontology_url, String community_short_name, byte[] rdf) throws IOException, GnossAPIException{
		String url = getApiUrl() + "/secondary-entity/modify";

		SecondaryEntityModel model = new SecondaryEntityModel();
		model.setOntology_url(ontology_url);
		model.setCommunity_short_name(community_short_name);
		model.setRdf(rdf);

		WebRequestPostWithJsonObject(url, model);

		LogHelper.getInstance().Debug("The secondary entity has been modified in the graph " + getOntologyUrl() + " of the community " + community_short_name);
	}

	private void deleteSecondaryEntity(String ontology_url, String community_short_name, String entity_id) throws IOException, GnossAPIException{
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
	public void delete(UUID resourceId, boolean endOfCharge) throws Exception{
		DeleteParams model = null;
		try{
			String url = getApiUrl() + "/resource/delete";
			model = new DeleteParams();
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
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
	public void delete(UUID resourceId) throws Exception{
		delete(resourceId, false);
	}

	/**
	 * Persistent delete of the resource
	 * @param resourceId Resource identifier
	 * @param deleteAttached Indicates if the attached resources must be deleted
	 * @param endOfCharge Marks the end of the charge
	 * @return boolean If the resource was deleted
	 * @throws Exception  exception 
	 */
	public boolean persistentDelete(UUID resourceId, boolean deleteAttached, boolean endOfCharge) throws Exception{
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

	public void deleteSecondaryEntitiesList(ArrayList<String> urlList, int numAttemps){
	    int processedNumber = 0;
	    int attempNumber = 0;

	    ArrayList<String> originalResourceList = new ArrayList<String>(urlList);
	    ArrayList<String> resourcesToDelete = new ArrayList<String>(urlList);
	    
	    while(resourcesToDelete != null && resourcesToDelete.size() > 0 && attempNumber <= numAttemps){
	        attempNumber++;
	        LogHelper.getInstance().Trace("******************** Begin lap number: " + attempNumber);
	        
	        Iterator<String> iterator = resourcesToDelete.iterator();
	        processedNumber = 0;
	        
	        while(iterator.hasNext()){
	            String url = iterator.next();
	            
	            try{
	                processedNumber++;
	                deleteSecondaryEntity(getOntologyUrl(), getCommunityShortName(), url);
	                LogHelper.getInstance().Debug("Successfully deleted the resource with ID: " + url);
	                
	                iterator.remove();
	            }
	            catch(Exception ex)
	            {
	                LogHelper.getInstance().Error("ERROR deleting: " + processedNumber + " of " + resourcesToDelete.size() + "\tID: " + url + ". Message: " + ex.getMessage());
	            }
	        }
	        LogHelper.getInstance().Debug("******************** Finished lap number: " + attempNumber);	        
	    }
	}

	public void deleteSecondaryEntitiesList(ArrayList<String> urlList){
		deleteSecondaryEntitiesList(urlList, 5);
	}

	/**
	 * Deletes a list of secondary entities with retry mechanism
	 * @param resourceList List of secondary resources to delete (will be modified)
	 * @param numAttempts Maximum number of attempts (default: 5)
	 */
	public void deleteSecondaryEntitiesList(List<SecondaryResource> resourceList, int numAttempts) {
	    int processedNumber = 0;
	    int attemptNumber = 0;

	    List<SecondaryResource> resourcesToDelete = new ArrayList<>(resourceList);
	    Set<SecondaryResource> successfullyDeleted = new HashSet<>(); // ✅ Usa Set para búsquedas O(1)

	    while (!resourcesToDelete.isEmpty() && attemptNumber <= numAttempts) {
	        attemptNumber++;

	        if (this._logHelper != null) {
	            this._logHelper.Debug(String.format("******************** Begin lap number: %d", attemptNumber));
	        }

	        Iterator<SecondaryResource> iterator = resourcesToDelete.iterator();

	        while (iterator.hasNext()) {
	            SecondaryResource resource = iterator.next();
	            resource.setDeleted(false);

	            try {
	                processedNumber++;
	                deleteSecondaryEntity(
	                    resource.getSecondaryOntology().getOntologyUrl(), 
	                    getCommunityShortName(), 
	                    resource.getId()
	                );
	                resource.setDeleted(true);
	                successfullyDeleted.add(resource);

	                if (this._logHelper != null) {
	                    this._logHelper.Debug(String.format(
	                        "Successfully deleted the resource with ID: %s", 
	                        resource.getId()
	                    ));
	                }

	                iterator.remove();

	            } catch (Exception ex) {
	                if (this._logHelper != null) {
	                    this._logHelper.Error(String.format(
	                        "ERROR deleting: %d of %d\tID: %s. Message: %s",
	                        processedNumber,
	                        resourceList.size(),
	                        resource.getId(),
	                        ex.getMessage()
	                    ));
	                }
	            }
	        }

	        if (this._logHelper != null) {
	            this._logHelper.Debug(String.format("******************** Finished lap number: %d", attemptNumber));
	        }
	    }

	    resourceList.clear();
	    resourceList.addAll(successfullyDeleted);
	    resourceList.addAll(resourcesToDelete); // Los que quedaron sin eliminar
	}

	/**
	 * Overloaded method with default numAttempts = 5
	 * @param resourceList List of secondary resources to delete
	 */
	public void deleteSecondaryEntitiesList(List<SecondaryResource> resourceList) {
	    deleteSecondaryEntitiesList(resourceList, 5);
	}
	
	/**
	 * Persistent delete of the resource
	 * @param resourceId Resource identifier
	 * @param deleteAttached Indicates if the attached resources must be deleted
	 * @return boolean If the resource was deleted
	 * @throws Exception exception
	 */
	public boolean persistentDelete(UUID resourceId, boolean deleteAttached) throws Exception{
		return persistentDelete(resourceId, deleteAttached, false);
	}

	/**
	 * @param resourceId Resource identifier
	 * @return Boolean T or F
	 * @throws Exception  Exception
	 */
	public boolean persistentDelete(UUID resourceId) throws Exception{
		return persistentDelete(resourceId, false, false);
	}


	/**
	 * Modify one or more properties of an entity of a loaded resource.
	 * @param resourceTriples Contains as a key the large Gnoss identifier of secondary resource to modify and as a value a ModificarTriples list of the resource properties to modified
	 * @param numAttemps Indicates whether the home must be updated
	 * @param publishHome Number of retries loading of the failed load of a resource
	 */
	public void modifyPropertyLoadedSecondaryResource(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, int numAttemps, boolean publishHome){
		modifyPropertyLoadedSecondaryResourceInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}

	/**
	 * Modify one or more properties of an entity of a loaded resource.
	 * @param resourceTriples Contains as a key the large Gnoss identifier of secondary resource to modify and as a value a ModificarTriples list of the resource properties to modified
	 * @param numAttemps Indicates whether the home must be updated
	 */
	public void modifyPropertyLoadedSecondaryResource(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, int numAttemps){
		modifyPropertyLoadedSecondaryResourceInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	/**
	 * Modify one or more properties of an entity of a loaded resource.
	 * @param resourceTriples Contains as a key the large Gnoss identifier of secondary resource to modify and as a value a ModificarTriples list of the resource properties to modified
	 */
	public void modifyPropertyLoadedSecondaryResource(HashMap<String, ArrayList<TriplesToModify>> resourceTriples){
		modifyPropertyLoadedSecondaryResourceInt(resourceTriples, getCommunityShortName());
	}

	private void modifyPropertyLoadedSecondaryResourceInt(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
	    int processedNumber = 0;
	    int attempNumber = 0;
	    ArrayList<String[]> valuesList = new ArrayList<String[]>();
	    ArrayList<String> values = new ArrayList<String>();
	    HashMap<String, ArrayList<TriplesToModify>> toModify = new HashMap<String, ArrayList<TriplesToModify>>(resourceTriples);
	    
	    while(toModify != null && toModify.size() > 0 && attempNumber < numAttemps){
	        Iterator<String> iterator = toModify.keySet().iterator();
	        
	        while(iterator.hasNext()){
	            String secondaryEntityId = iterator.next();
	            attempNumber++;
	            processedNumber++;
	            
	            for(TriplesToModify mT : toModify.get(secondaryEntityId)){
	                String acido = "0";
	                values = new ArrayList<String>();
	                values.add(mT.getOldValue());
	                values.add(mT.getPredicate());
	                values.add(mT.getNewValue());
	                values.add(acido);
	                valuesList.add((String[])values.toArray(new String[0]));
	            }
	            
	            try{
	                String url = getApiUrl() + "/secondary-entity/modify-triple-list";
	                ModifyTripleListModel model = new ModifyTripleListModel();
	                model.setCommunity_short_name(communityShortName);
	                model.setSecondary_ontology_url(_ontologyUrl);
	                model.setSecondary_entity(secondaryEntityId);
	                model.setTriple_list((String[][])valuesList.toArray(new String[0][]));
	                WebRequestPostWithJsonObject(url, model);

	                valuesList = new ArrayList<String[]>();
	                LogHelper.getInstance().Debug(processedNumber + " of " + toModify.size() + " Object: " + secondaryEntityId + ". Resource: " + toModify.get(secondaryEntityId).toArray());
	                
	                iterator.remove();
	            }
	            catch (Exception ex){
	                LogHelper.getInstance().Error("Resource " + secondaryEntityId + " : " + ex.getMessage());
	                valuesList = new ArrayList<String[]>();
	            }
	        }
	        LogHelper.getInstance().Debug("******************** Finished lap number: " + attempNumber);
	    }
	}

	private void modifyPropertyLoadedSecondaryResourceInt(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps){
		modifyPropertyLoadedSecondaryResourceInt(resourceTriples, communityShortName, numAttemps, false);
	}

	private void modifyPropertyLoadedSecondaryResourceInt(HashMap<String, ArrayList<TriplesToModify>> resourceTriples, String communityShortName){
		modifyPropertyLoadedSecondaryResourceInt(resourceTriples, communityShortName, 2, false);
	}

	private String loadComplexSemanticResourceInt(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName) {
		return loadComplexSemanticResourceInt(resource, hierarquicalCategories, isLast, numAttemps, communityShortName, null);
	}

	private String loadComplexSemanticResourceInt(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, int numAttemps, String communityShortName, String rdfsPath) {
		BufferedWriter bw = null;
		FileWriter fw = null; 
		try {
			if (resource.getTextCategories() != null) {
				if (hierarquicalCategories) {
					if (StringUtils.isEmpty(communityShortName)) {
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					} 
					else {
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				} 
				else {
					if (StringUtils.isEmpty(communityShortName)) {
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					} 
					else {
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}

			String documentId = "";

			if (StringUtils.isEmpty(communityShortName)) {
				communityShortName = getCommunityShortName();
			}

			LoadResourceParams model = getResourceModelOfComplexOntologyResource(communityShortName, resource, false, isLast);
			documentId = createComplexOntologyResource(model);
			resource.setUploaded(true);

			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle:" + resource.getTitle() + "\tResourceID: " + resource.getOntology().getResourceId());

			if (!StringUtils.isEmpty(rdfsPath)) {
				File directory = new File(rdfsPath + "/" + getOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl()));
				if (!directory.exists()){
					directory.mkdir();
				}

				String rdfFile = rdfsPath + "/" + getOntologyNameWithOutExtensionFromUrlOntology(resource.getOntology().getOntologyUrl()) + "/" + resource.getId() + ".rdf";
				File file = new File(rdfFile);
				if (!file.exists()){					
					try {
						fw = new FileWriter(file);
						bw = new BufferedWriter(fw);
						bw.write(resource.getStringRdfFile());
					}
					catch (Exception e) {
						LogHelper.getInstance().Error("Error writing the rdf file of the resource: \tID: " + resource.getId() + ". Title: " + resource.getTitle() +". Message: " + e.getMessage());
					}
				}	
			}
			resource.setGnossId(documentId);
		} catch (GnossAPICategoryException gacex) {
			LogHelper.getInstance().Error("Error loading the resource: \tID: " + resource.getId() + ". Title: " + resource.getTitle() +". Message: " + gacex.getMessage());

		}
		catch (Exception ex) {
			LogHelper.getInstance().Error("Error loading the resource: \tID: " + resource.getId() + ". Title: " + resource.getTitle() +". Message: " + ex.getMessage());

		}
		finally {						
			try {
				if(fw != null) {
					fw.close();
				}
			}catch (IOException e) {
				// Error al cerrar el stream
			}		
			
			try {
				if(bw != null) {
					bw.close();
				}
			}catch (IOException e) {
				// Error al cerrar el stream
			}
		}
		return resource.getGnossId();
	}

	public static String getOntologyNameWithOutExtensionFromUrlOntology(String urlOntology){
		return urlOntology.substring(urlOntology.lastIndexOf(StringDelimiters.Slash) + StringDelimiters.Slash.length()).replace(".owl", "");
	}

	private LoadResourceParams getResourceModelOfComplexOntologyResource(String communityShortName, ComplexOntologyResource rec, boolean pCrearVersion, boolean pEsUltimo) throws IOException, GnossAPIException{
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
		model.setResource_file(rec.getRdfFile());

		int i = 0;
		if(rec.getAttachedFilesName().size() > 0){
			model.setResource_attached_files(new ArrayList<SemanticAttachedResource>());
			for(String nombre : rec.getAttachedFilesName()){
				SemanticAttachedResource adjunto = new SemanticAttachedResource();
				adjunto.setFile_rdf_property(nombre);
				adjunto.setFile_property_type((short)rec.getAttachedFilesType().get(i).getID());
				adjunto.setRdf_attached_file(rec.getAttachedFiles().get(i));
				adjunto.setDelete_file(rec.getAttachedFiles().get(i) == null);
				i++;
				List<SemanticAttachedResource> listaAuxiliar = model.getResource_attached_files();
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
		model.setMain_image(rec.getMainImage());
		model.setVisibility((short)rec.getVisibility().getID());
		model.setEditors_list(rec.getEditorsGroups());
		model.setReaders_list(rec.getReadersGroups());

		return model;
	}

	private ArrayList<UUID> getHierarquicalCategoriesIdentifiersList(ArrayList<String> hierarquicalCategoriesList) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
		ArrayList<UUID> categories = null;

		if (hierarquicalCategoriesList != null && hierarquicalCategoriesList.size() > 0) {
			for (String cat : hierarquicalCategoriesList) {
				ThesaurusCategory category = findHierarquicalCategoryNameInCategories(cat, getCommunityApiWrapper().getCommunityCategories());
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

	private ArrayList<UUID> getHierarquicalCategoriesIdentifiersList(ArrayList<String> hierarquicalCategoriesList, String communityShortName) throws GnossAPICategoryException, MalformedURLException, IOException, GnossAPIException {
		ArrayList<UUID> resultList = null;

		ArrayList<ThesaurusCategory> categories = null;

		if(communityShortName.equals(getCommunityShortName())){
			categories = getCommunityApiWrapper().getCommunityCategories();
		}
		else{
			categories = getCommunityApiWrapper().LoadCategories(communityShortName);
		}

		if(hierarquicalCategoriesList != null && hierarquicalCategoriesList.size() > 0){
			for(String cat : hierarquicalCategoriesList){
				ThesaurusCategory category = findHierarquicalCategoryNameInCategories(cat, categories);
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

	private ArrayList<UUID> getNotHierarquicalCategoriesIdentifiersList(ArrayList<String> notHierarquicalCategoriesList) throws GnossAPICategoryException, MalformedURLException, IOException, GnossAPIException{
		ArrayList<UUID> resultList = null;
		if(notHierarquicalCategoriesList != null && notHierarquicalCategoriesList.size() > 0){
			String[] categoryList = null;
			String[] separator = new String[]{ "|||" };

			for(ThesaurusCategory category : getCommunityApiWrapper().getCommunityCategories()){
				if(!StringUtils.isEmpty(category.getCategory_name()) && category.getCategory_name().contains("|||")){
					categoryList = category.getCategory_name().split(Arrays.toString(separator), -1);
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
								for(ThesaurusCategory thesCat : getCommunityApiWrapper().getCommunityCategories()){
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

						for(ThesaurusCategory thesCat : getCommunityApiWrapper().getCommunityCategories()){
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

	private ArrayList<UUID> getNotHierarquicalCategoriesIdentifiersList(ArrayList<String> notHierarquicalCategoriesList, String communityShortName) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException{
		ArrayList<ThesaurusCategory> categoryList = null;

		if(communityShortName.equals(getCommunityShortName())){
			categoryList = getCommunityApiWrapper().getCommunityCategories();
		}
		else{
			categoryList = getCommunityApiWrapper().LoadCategories(getCommunityShortName());
		}

		ArrayList<UUID> resultList = null;
		if(notHierarquicalCategoriesList != null && notHierarquicalCategoriesList.size() > 0){
			String[] categories = null;
			String[] separator = new String[] { "|||" };

			for(ThesaurusCategory category : categoryList){
				if(!StringUtils.isEmpty(category.getCategory_name()) && category.getCategory_name().contains("|||")){
					categories = category.getCategory_name().split(Arrays.toString(separator), -1);
					for(int i = 0; i < categories.length; i++){
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


	public boolean checkLoadIdentifier(String loadIdentifier) throws MalformedURLException, IOException, GnossAPIException{
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

	private void loadIdentifierGenerator() throws IOException, GnossAPIException{
		if(StringUtils.isEmpty(_loadIdentifier)){
			Date date = null;
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));			
			_loadIdentifier = getCommunityShortName() + "~" + cal.get(Calendar.YEAR) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "~" + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
			registerLoadIdentifier(_loadIdentifier);
		}
	}

	private void obtaingGraphsUrl(){
		try{
			String url = getApiUrl() + "/ontology/get-graphs-url";

			GraphsUrl = WebRequest("GET", url, "", "", "application/json").replaceAll("\"", "");

			LogHelper.getInstance().Debug("The url of the graphs is: " + GraphsUrl);
		}
		catch(Exception ex){
			LogHelper.getInstance().Debug("Error obtaining the intragnoss URL: " + ex.getMessage());
		}
	}
	
	public String getGraphsUrl(){
		return GraphsUrl;
	}

	private void registerLoadIdentifier(String loadIdentifier) throws IOException, GnossAPIException{
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

	private ThesaurusCategory findHierarquicalCategoryNameInCategories(String hierarchicalName, ArrayList<ThesaurusCategory> categories){
		ThesaurusCategory resultCategory = null;

		String[] path = hierarchicalName.split("|");
		for(String category : path){
			if(category != null && !category.equals("")){
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
	public HashMap<UUID, Boolean> deletePropertiesLoadedResources(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, int numAttemps, boolean publishHome){
		return deletePropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome);
	}

	/**
	 * Method for adding one or more properties of a loaded resource. In RemoveTriples can indicate whether title or description. 
	 * By default false two fields. It influences the value of the resource searches.
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a RemoveTriples list of the resource properties that will be deleted.
	 * @param numAttemps Indicates whether the home must be updated
	 * @return HashMap resource identifier and boolean indicating the successfull, or not, result of the action
	 */
	public HashMap<UUID, Boolean> deletePropertiesLoadedResources(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, int numAttemps){
		return deletePropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName(), numAttemps);
	}

	/**
	 * Method for adding one or more properties of a loaded resource. In RemoveTriples can indicate whether title or description. 
	 * By default false two fields. It influences the value of the resource searches.
	 * @param resourceTriples Contains as a key the resource guid identifier to modify and as a value a RemoveTriples list of the resource properties that will be deleted.
	 * @return HashMap resource identifier and boolean indicating the successfull, or not, result of the action
	 */
	public HashMap<UUID, Boolean> deletePropertiesLoadedResources(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples){
		return deletePropertiesLoadedResourcesInt(resourceTriples, getCommunityShortName());
	}

	private HashMap<UUID, Boolean> deletePropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome){
	    HashMap<UUID, Boolean> result = new HashMap<UUID, Boolean>();
	    int processedNumber = 0;
	    int attempNumber = 0;
	    HashMap<UUID, ArrayList<RemoveTriples>> toDelete = new HashMap<UUID, ArrayList<RemoveTriples>>(resourceTriples);
	    
	    while(toDelete != null && toDelete.size() > 0 && attempNumber < numAttemps){
	        int i = 0;
	        int contResources = toDelete.keySet().size();
	        
	        Iterator<UUID> iterator = toDelete.keySet().iterator();
	        
	        while(iterator.hasNext()){
	            UUID docID = iterator.next();
	            i++;
	            ArrayList<ModifyResourceTriple> listaValores = new ArrayList<ModifyResourceTriple>();
	            processedNumber++;
	            attempNumber++;
	            
	            for(RemoveTriples iT : toDelete.get(docID)){
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
	                modifyTripleList(docID, listaValores, publishHome, null, null, endOfLoad);

	                LogHelper.getInstance().Debug(processedNumber + " of " + toDelete.size() + " Object: " + docID + ". Resource: " + toDelete.get(docID).toArray());
	                
	                iterator.remove();

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
	private void loadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps, String communityShortName, String rdfsPath){
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
						loadComplexSemanticResourceInt(rec, hierarquicalCategories, last, numAttemps, communityShortName, rdfsPath);
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
	private void loadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps, String communityShortName){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, communityShortName, null);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Number of retries loading of the failed load of a resource
	 */
	private void loadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, numAttemps, null, null);
	}

	/**
	 * Load a complex semantic resources list
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 */
	private void loadComplexSemanticResourceListInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories){
		loadComplexSemanticResourceListInt(resourceList, hierarquicalCategories, 2, null, null);
	}

	private void loadComplexSemanticResourceListWithOntologyAndCommunityInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String ontology, String communityShortName, int numAttemps){
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
						loadComplexSemanticResourceWithOntologyAndCommunityInt(rec, hierarquicalCategories, last, ontology, communityShortName);
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

	private String loadComplexSemanticResourceWithOntologyAndCommunityInt(ComplexOntologyResource resource, boolean hierarquicalCategories, boolean isLast, String ontology, String communityShortName){
		try{
			if(resource.getTextCategories() != null && resource.getTextCategories().size() > 0){
				if(hierarquicalCategories){
					if(StringUtils.isEmpty(communityShortName)){
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
				else{
					if(StringUtils.isEmpty(communityShortName)){
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}
					else{
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}

			String documentId = "";
			ontology = ontology.toLowerCase().replace(".owl", "");
			ontology = _ontologyUrl.replace(_ontologyUrl.substring(_ontologyUrl.lastIndexOf("/") + 1), ontology + ".owl");

			LoadResourceParams model = getResourceModelOfComplexOntologyResource(communityShortName, resource, false, isLast);
			documentId = createComplexOntologyResource(model);
			resource.setUploaded(true);

			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle: " + resource.getTitle() + "\tResourceID: " + resource.getOntology().getResourceId());
			resource.setGnossId(documentId);
		}
		catch(Exception ex){
			LogHelper.getInstance().Debug("Loaded: \tID: " + resource.getId() + "\tTitle: " + resource.getTitle() + "\tResourceID: " + resource.getOntology().getResourceId());
		}
		return resource.getGnossId();
	}

	private void loadComplexSemanticResourceListWithOntologyAndCommunityInt(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String ontology, String communityShortName){
		loadComplexSemanticResourceListWithOntologyAndCommunityInt(resourceList, hierarquicalCategories, ontology, communityShortName, 1);
	}


	private HashMap<UUID, Boolean> deletePropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, String communityShortName, int numAttemps){
		return deletePropertiesLoadedResourcesInt(resourceTriples, communityShortName, numAttemps, false);
	}

	private HashMap<UUID, Boolean> deletePropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<RemoveTriples>> resourceTriples, String communityShortName){
		return deletePropertiesLoadedResourcesInt(resourceTriples, communityShortName, 2, false);
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

	public void setGraphsUrl(String graphsUrl){
		GraphsUrl = graphsUrl;
	}

	public String getLoadIdentifier() throws IOException, GnossAPIException{
		if(_loadIdentifier == null){
			loadIdentifierGenerator();
		}
		return _loadIdentifier;
	}

	public void setLoadIdentifier(String loadIdentifier) throws MalformedURLException, GnossAPIArgumentException, IOException, GnossAPIException{
		if(checkLoadIdentifier(loadIdentifier)){
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


	private HashMap<UUID, Boolean> actionsOnPropertiesLoadedResourcesInt(HashMap<UUID, List<TriplesToModify>> resourceTriples, HashMap<UUID, List<RemoveTriples>> deleteList, HashMap<UUID, List<TriplesToInclude>> insertList, HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntitiesInsertTriplesList, String communityShortName, boolean publishHome){

	    HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
	    int processedNumber=0;
	    int attempNumber=0;
	    List<ModifyResourceTriple> valuesList = new ArrayList<ModifyResourceTriple>();
	    List<String> values = new ArrayList<String> ();
	    HashMap <UUID, List<ModifyResourceTriple>> resources = new HashMap<UUID, List<ModifyResourceTriple>>();

	    if(deleteList!=null) {
	        HashMap<UUID, List<RemoveTriples>> toDelete = new HashMap<UUID, List<RemoveTriples>>(deleteList);
	        while(toDelete!=null && toDelete.size()>0) {
	            Iterator<UUID> iterator = toDelete.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();
	                processedNumber++;
	                attempNumber++;

	                for(RemoveTriples iT : toDelete.get(docID)) {
	                    ModifyResourceTriple triple= new ModifyResourceTriple();
	                    triple.setOld_object(iT.getValue());
	                    triple.setPredicate(iT.getPredicate());
	                    triple.setNew_object(null);
	                    triple.setGnoss_property(GnossResourceProperty.none);

	                    if(iT.isTitle()) {
	                        triple.setGnoss_property(GnossResourceProperty.title);
	                    }else if( iT.isDescription()) {
	                        triple.setGnoss_property(GnossResourceProperty.description);
	                    }
	                    valuesList.add(triple);
	                }
	                resources.put(docID, valuesList);
	                iterator.remove();
	            }
	        }
	    }

	    if(resourceTriples!=null) {
	        HashMap<UUID, List<TriplesToModify>> toModify = new HashMap<UUID, List<TriplesToModify>>(resourceTriples);
	        while(toModify!=null && toModify.size()>0) {
	            Iterator<UUID> iterator = toModify.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList=new ArrayList<ModifyResourceTriple>();

	                // If exists, gets the values list to add the modified values
	                if(resources.containsKey(docID)) {
	                    valuesList.addAll(resources.get(docID));
	                }
	                processedNumber++;
	                attempNumber++;
	                
	                for(TriplesToModify mT : toModify.get(docID)) {
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

	                resources.put(docID, valuesList);
	                iterator.remove();
	            }
	        }
	    }

	    if(insertList!=null) {
	        HashMap<UUID, List<TriplesToInclude>> toInsert = new HashMap<UUID, List<TriplesToInclude>>(insertList);
	        while(toInsert!=null && toInsert.size()>0) {
	            Iterator<UUID> iterator = toInsert.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();

	                if(resources.containsKey(docID)) {
	                    valuesList.addAll(resources.get(docID));
	                }
	                processedNumber++;
	                attempNumber++;

	                for(TriplesToInclude iT : toInsert.get(docID)) {
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
	                resources.put(docID, valuesList);
	                iterator.remove();
	            }
	        }
	    }


	    if(auxiliaryEntitiesInsertTriplesList!=null) {
	        HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntityTriplesToInsert= new HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>>(auxiliaryEntitiesInsertTriplesList);
	        while(auxiliaryEntityTriplesToInsert!= null && auxiliaryEntityTriplesToInsert.size()>0) {
	            Iterator<UUID> iterator = auxiliaryEntityTriplesToInsert.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();

	                if(resources.containsKey(docID)) {
	                    valuesList.addAll(resources.get(docID));
	                }
	                processedNumber++;
	                attempNumber++;

	                for(AuxiliaryEntitiesTriplesToInclude iT : auxiliaryEntityTriplesToInsert.get(docID)) { 
	                    ModifyResourceTriple triple = new ModifyResourceTriple();
	                    triple.setOld_object(null);
	                    triple.setPredicate(iT.getPredicate());
	                    triple.setNew_object(GraphsUrl+"items/"+iT.getName()+"_"+docID+"_"+iT.getIdentifier()+"|"+iT.getValue());
	                    triple.setGnoss_property(GnossResourceProperty.none);

	                    valuesList.add(triple);
	                }

	                resources.put(docID, valuesList);
	                iterator.remove();
	            }
	        }
	    }

	    int i=0;
	    int contResources= resources.keySet().size();
	    Iterator<UUID> finalIterator = resources.keySet().iterator();

	    while(finalIterator.hasNext()){
	        UUID docID = finalIterator.next();
	        i++;
	        try {
	            boolean endOfLoad=false;
	            if(i==contResources) {
	                endOfLoad=true;
	            }
	            modifyTripleList(docID, resources.get(docID), publishHome, null, null, endOfLoad, null);

	            _logHelper.Debug("Object "+ docID);
	                            
	            result.put(docID, true);				
	        }
	        catch(Exception ex) {
	            _logHelper.Error("Resource "+docID+ ": "+ex.getMessage());
	            
	            result.put(docID, false);				
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
	public HashMap<UUID, Boolean> actionsOnPropertiesLoadedResources(HashMap<UUID, List<TriplesToModify>> resourceTriplesModify, HashMap<UUID, List<RemoveTriples>> resourceTriplesDelete, HashMap<UUID, List<TriplesToInclude>> resourceTriplesInsert, HashMap<UUID, List<AuxiliaryEntitiesTriplesToInclude>> resourceTriplesAddAuxiliarEntity, boolean publishHome){
		return actionsOnPropertiesLoadedResourcesInt(resourceTriplesModify, resourceTriplesDelete, resourceTriplesInsert, resourceTriplesAddAuxiliarEntity, getCommunityShortName(), false);
	}



	//private void modifyTripleList(UUID docID, List<ModifyResourceTriple> valuesList, String loadIdentifier,
	//		boolean publishHome, Object mainImage, Object resourceAttachedFiles, boolean endOfLoad) {
	//	// TODO Auto-generated method stub

	//}



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
				ontology=getOntologyNameWithOutExtensionFromUrlOntology(ontologyURL);
			}
			String url= getApiUrl()+"/resource/get-pending-actions?ontology_name="+ontology+"&community_short_name="+getCommunityShortName();
			String response=WebRequest("GET", url, "application/json");
			int pendingActions=Integer.parseInt(response);
			_logHelper.Debug("The ontology "+ontology+ " has "+ pendingActions +" pending actions");
			return pendingActions;
		}catch(Exception ex) {
			_logHelper.Error("There has been an error trying to know if there are outstanding resources "+ex.getMessage());
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
			if(communityShortName != null) {
				communityShortName = communityShortName.trim().replace("\"", "");
			}
			_logHelper.Debug("The community short name for the resource "+resourceID+ " is "+communityShortName);
		}catch(Exception ex) {
			_logHelper.Error("Error getting the community short name "+ex.getMessage());
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
	public boolean hasUserEditingPermissionOnResourceByCommunityName(UUID resourceId, UUID userId) throws Exception {
		boolean result=false;
		if(resourceId != null  && userId != null) {
			try {
				String url=getApiUrl()+"/resource/get-user-editing-permission-on-resource-by-community-name?resource_id="+resourceId+"&user_id="+userId+"&community_short_name="+getCommunityShortName();
				String response=WebRequest("GET", url);
				Gson gson = new Gson();
				result= gson.fromJson(response, Boolean.class); 

				if(result) {
					_logHelper.Debug("The user "+userId+" is allowed to edit the resource "+resourceId+" in "+getCommunityShortName());

				}else {
					_logHelper.Debug("The user "+userId+" is not allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
				}
			}catch(Exception ex) {
				_logHelper.Error(ex.getMessage());
				throw ex;
			}
		}else {
			_logHelper.Error("Any of this are null or empty: resourceId, userId");
		}
		return result;
	}

	/**
	 * Checks whether the user has permission on the resource editing
	 * @param resourceId resource identifier
	 * @param shortNameOrEmail User short name or email
	 * @return True if the user has editing permission on the resource. False if not.
	 */
	public boolean hasUserEditingPermissionOnResourceByCommunityName(UUID resourceId, String shortNameOrEmail) throws Exception {
	    boolean result = false;
	    if(resourceId != null && !StringUtils.isEmpty(shortNameOrEmail)) {
	        try {
	            String url = getApiUrl() + "/resource/get-user-editing-permission-on-resource-by-community-name?resource_id=" + 
	                        resourceId + "&login=" + shortNameOrEmail + "&community_short_name=" + getCommunityShortName();
	            String response = WebRequest("GET", url);
	            Gson gson = new Gson();
	            result = gson.fromJson(response, Boolean.class);
	            
	            if(result) {
	                _logHelper.Debug("The user " + shortNameOrEmail + " is allowed to edit the resource " + resourceId + 
	                               " in " + getCommunityShortName());
	            } else {
	                _logHelper.Debug("The user " + shortNameOrEmail + " is not allowed to edit the resource " + resourceId + 
	                               " in " + getCommunityShortName());
	            }
	        } catch(Exception ex) {
	            _logHelper.Error(ex.getMessage());
	            throw ex;
	        }
	    } else {
	        _logHelper.Error("Any of this are null or empty: resourceId, shortNameOrEmail");
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
	public boolean hasUserEditingPermissionOnResourceByCommunityID(UUID resourceId, UUID userId, UUID communityId) throws Exception {
		boolean result=false;
		if(resourceId != null && userId != null) {
			try {
				String url=getApiUrl()+"/resource/get-user-editing-permission-on-resource?resource_id="+resourceId+"&user_id="+userId+"&community_id="+communityId;
				String response=WebRequest("GET", url);
				Gson gson = new Gson();
				result= gson.fromJson(response, Boolean.class); 

				if(result) {
					_logHelper.Debug("The user "+userId+" is allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
				}else {
					_logHelper.Debug("The user "+userId+" is not allowed to edit the resource "+resourceId+" in "+getCommunityShortName());
				}
			}catch(Exception ex) {
				_logHelper.Error(ex.getMessage());
				throw ex;
			}
		}else {
			_logHelper.Error("Any of this are null or empty: resourceId, userId");
		}
		return result;
	}

	/**
	 * Checks whether the user has permission on the resource editing
	 * @param resourceId Resource identifier
	 * @param shortNameOrEmail User short name or email
	 * @param communityId Community identifier
	 * @return True if the user has editing permission on the resource. False if not.
	 */
	public boolean hasUserEditingPermissionOnResourceByCommunityID(UUID resourceId, String shortNameOrEmail, UUID communityId) throws Exception {
	    boolean result = false;
	    if(resourceId != null && !StringUtils.isEmpty(shortNameOrEmail)) {
	        try {
	            String url = getApiUrl() + "/resource/get-user-editing-permission-on-resource?resource_id=" + 
	                        resourceId + "&login=" + shortNameOrEmail + "&community_id=" + communityId;
	            String response = WebRequest("GET", url);
	            Gson gson = new Gson();
	            result = gson.fromJson(response, Boolean.class);
	            
	            if(result) {
	                _logHelper.Debug("The user " + shortNameOrEmail + " is allowed to edit the resource " + resourceId + 
	                               " in " + getCommunityShortName());
	            } else {
	                _logHelper.Debug("The user " + shortNameOrEmail + " is not allowed to edit the resource " + resourceId + 
	                               " in " + getCommunityShortName());
	            }
	        } catch(Exception ex) {
	            _logHelper.Error(ex.getMessage());
	            throw ex;
	        }
	    } else {
	        _logHelper.Error("Any of this are null or empty: resourceId, shortNameOrEmail");
	    }
	    return result;
	}

	/**
	 * Gets the visibility of the resource
	 * @param resourceId Resource identifier
	 * @return esourceVisibility with the visibility of the resource. Null if it fails
	 * @throws Exception exception 
	 */
	public ResourceVisibility getResourceVisibility (UUID resourceId) throws Exception {
		ResourceVisibility visibilidad= null;
		try {
			String url=getApiUrl()+"/resource/get-visibility?resource_id="+resourceId;
			String result= WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			visibilidad= gson.fromJson(result, ResourceVisibility.class);

			if(visibilidad==null) {
				_logHelper.Error("Resource visibility not obtained "+resourceId);
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the visibility of "+resourceId+": "+ex.getMessage());
			throw ex;
		}
		return visibilidad;
	}

	/**
	 * Gets the related resources of a list of resources
	 * @param resourceIds Resource identifiers
	 * @return Related resources
	 */
	public HashMap<UUID, List<UUID>> getRelatedResourcesFromList(List<UUID> resourceIds) throws Exception {
	    HashMap<UUID, List<UUID>> listaIds = null;
	    try {
	        Map<String, Object> aux = new HashMap<>();
	        aux.put("resource_ids", resourceIds);
	        aux.put("community_short_name", getCommunityShortName());
	        
	        String url = getApiUrl() + "/resource/get-related-resources-from-list";
	        String result = WebRequestPostWithJsonObject(url, aux);
	        Gson gson = new Gson();
	        Type type = new TypeToken<HashMap<UUID, List<UUID>>>(){}.getType();
	        listaIds = gson.fromJson(result, type);
	    } catch(Exception ex) {
	        Gson gson = new Gson();
	        _logHelper.Error("Error getting the related resources of " + gson.toJson(resourceIds) + ": " + ex.getMessage());
	        throw ex;
	    }
	    return listaIds;
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
			Type type = new TypeToken<ArrayList<UUID>>(){}.getType();
			listaIds= gson.fromJson(result, type);

		}catch(Exception ex) {
			_logHelper.Error("Error getting the related resources of "+resourceId+ ": "+ex.getMessage());
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
			Type type = new TypeToken<HashMap<String, List<UUID>>>(){}.getType();
			listaDocs= gson.fromJson(result, type);
			_logHelper.Debug("The user id "+userId+" publisher "+result);

		}catch(Exception ex) {
			_logHelper.Error("Error getting the shared communities of  "+userId+ ": "+ex.getMessage());
			throw ex;
		}
		return  listaDocs;
	}

	/**
	 * Gets the documents publisher by user
	 * @param shortNameOrEmail User short name or email
	 * @return List of community names
	 */
	public HashMap<String, List<UUID>> getDocumentsPublishedByUser(String shortNameOrEmail) throws Exception {
	    HashMap<String, List<UUID>> listaDocs = null;
	    try {
	        String url = getApiUrl() + "/resource/get-documents-published-by-user?login=" + shortNameOrEmail;
	        String result = WebRequest("GET", url, "application/x-www-form-urlencoded");
	        Gson gson = new Gson();
	        Type type = new TypeToken<HashMap<String, List<UUID>>>(){}.getType();
	        listaDocs = gson.fromJson(result, type);
	        _logHelper.Debug("the user " + shortNameOrEmail + " published " + result);
	    } catch(Exception ex) {
	        _logHelper.Error("Error getting the shared communities of  " + shortNameOrEmail + ": " + ex.getMessage());
	        throw ex;
	    }
	    return listaDocs;
	}

	/**
	 * 
	 * @param proyectoId proyecto id 
	 * @return String valor 
	 * @throws Exception exception
	 */
	public String obtenerPathEstilos(UUID proyectoId) throws Exception {
		String valor= null;
		try {
			String url=getApiUrl()+"/resource/get-path-styles?id_proyecto="+proyectoId;
			String result=WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			valor= gson.fromJson(result, String.class);
			_logHelper.Debug("The proyect id "+proyectoId+ " have styles path "+result);
		}catch(Exception ex) {
			_logHelper.Error("Error getting the shared communities of "+proyectoId+": "+ex.getMessage());
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
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			communities= gson.fromJson(result, type);
			_logHelper.Debug("The resource "+resourceId+ " has been shared in "+result);
		}catch(Exception ex) {
			_logHelper.Error("Error getting the shared communities of "+resourceId+": "+ex.getMessage());
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
				_logHelper.Debug("Resource readers of "+resourceId+": "+response);
			}else {
				_logHelper.Error("Couldn´t get readers of:" +resourceId);
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the resources of "+resourceId+ ": "+ex.getMessage());
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
	public boolean unsharedCommunityResource(UUID resourceId, String communityShortName) throws Exception {
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
				_logHelper.Debug("Resource "+resourceId+ " unshared from "+communityShortName);
			}else {
				_logHelper.Debug("Resource "+resourceId+ " not unshared from "+communityShortName);
			}
		}
		catch(Exception ex) {
			_logHelper.Error("Error trying to unshare the resource "+resourceId+" from "+communityShortName+": "+ex.getMessage());
			throw ex;
		}
		return unshared;
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
			Type type = new TypeToken<ArrayList<KeyEditors>>(){}.getType();
			editorsList= gson.fromJson(response, type);

			if(editorsList!=null && editorsList.size()>0) {
				_logHelper.Debug("Editors of the resources "+gson1.toJson(resourceId_list)+": "+response);
			}
			else {
				_logHelper.Debug("There is no Editors for the resources "+gson1.toJson(resourceId_list));
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the editors of the resources "+gson1.toJson(resourceId_list)+". Error description: "+ex.getMessage());
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
			Type type = new TypeToken<ArrayList<ResponseGetUrl>>(){}.getType();
			urlList= gson.fromJson(response, type);

			if(urlList!=null && urlList.size()==0) {
				_logHelper.Debug("Downloads urls of the resources "+gson1.toJson(resourceId_list)+": "+response);
			}else {
				_logHelper.Debug("There is no download  url for the resources "+gson1.toJson(resourceId_list));
			}

		}catch(Exception ex) {
			_logHelper.Error("Error getting the download urls  of the resources "+gson1.toJson(resourceId_list)+": Error description "+ex.getMessage());
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
			Type type = new TypeToken<ArrayList<ResponseGetUrl>>(){}.getType();
			urlList= gson.fromJson(response, type);

			if(urlList!=null && urlList.size()==0) {
				_logHelper.Debug("Urls of the resources "+gson1.toJson(parameters)+": "+response);
			}else {
				_logHelper.Debug("There is no urls for the resources "+gson1.toJson(parameters));
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the download urls  of the resources "+gson1.toJson(resourceId_list)+": Error description "+ex.getMessage());
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
				readers.setVisibility(visibility.getID());
			}
			WebRequestPostWithJsonObject(url, readers);
			_logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			_logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description "+ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Adds the readers of the resource
	 * @param resourceId Resource identifier
	 * @param readers_list Resource readers
	 * @throws Exception exception 
	 */
	public void addReaders(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
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
			_logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			_logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description "+ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Remove the readers of the resource
	 * @param resourceId Resource identifier
	 * @param readers_list Resource readers
	 * @throws Exception exception
	 */
	public void removeReaders(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
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
			_logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			_logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description "+ex.getMessage());
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
	public void setEditors(UUID resourceId, List<ReaderEditor> readers_list, ResourceVisibility visibility, boolean publishHome) throws Exception {
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
				readers.setVisibility( visibility.ordinal());

			}
			WebRequestPostWithJsonObject(url, readers);
			_logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			_logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description "+ex.getMessage());
			throw ex;
		}
	}


	/**
	 * Add the readers of the resources
	 * @param resourceId Resource identifier
	 * @param readers_list Resource readers
	 * @throws Exception exception 
	 */
	public void addEditors(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
		SetReadersEditorsParams editors=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/add-editors";
			editors= new SetReadersEditorsParams();
			{
				editors.setResource_id(resourceId);
				editors.setCommunity_short_name(getCommunityShortName());
				editors.setReaders_list(readers_list);
			}
			WebRequestPostWithJsonObject(url, editors);
			_logHelper.Debug("Ended resource editors setting");
		}catch(Exception ex) {
			_logHelper.Error("Error setting resource "+resourceId+" editors. \r\n json:" +gson1.toJson(editors)+": Error description "+ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Remove readers of the resources
	 * @param resourceId resource id
	 * @param readers_list resource readers
	 * @throws Exception exception 
	 */
	public void removeEditors(UUID resourceId, List<ReaderEditor> readers_list) throws Exception {
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
			_logHelper.Debug("Ended resource readers setting");
		}catch(Exception ex) {
			_logHelper.Error("Error setting resource "+resourceId+" readers. \r\n json:" +gson1.toJson(readers)+": Error description "+ex.getMessage());
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
	public void voteDocument(UUID pIdentidadID, float pValorVoto, UUID pDocumentoID, UUID pProyectoID) throws Exception {
		VotedParameters vote=null;
		Gson gson1 = new Gson();
		try {
			String url=getApiUrl()+"/resource/vote-document";
			vote= new VotedParameters();
			{
				vote.setUser_id(pIdentidadID);
				vote.setVote_value(pValorVoto);
				vote.setResource_id(pDocumentoID);
				vote.setProject_id(pProyectoID);
			}
			WebRequestPostWithJsonObject(url, vote);
			_logHelper.Debug("Ended vote document setting");
		}catch(Exception ex) {
			_logHelper.Error("Error vote Document "+pDocumentoID+". \r\n json:" +gson1.toJson(vote)+": Error description "+ex.getMessage());
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
			Type type = new TypeToken<HashMap<UUID, String>>(){}.getType();
			emailsList= gson.fromJson(response, type);

			if(emailsList!=null && emailsList.size()==0) {
				_logHelper.Debug("Urls of resources "+gson1.toJson(resourceId_list)+": "+response);
			}
			else {
				_logHelper.Debug("There is no Urls of resources "+gson1.toJson(resourceId_list));
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the Urls of resources "+gson1.toJson(resourceId_list)+": "+ex.getMessage());
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
			Type type = new TypeToken<ArrayList<ResponseGetCategories>>(){}.getType();
			categoriesList= gson.fromJson(response, type);

			if(categoriesList!=null && categoriesList.size()==resourceId_list.size()) {
				_logHelper.Debug("Categories of resources "+gson1.toJson(resourceId_list)+": "+response);
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
				_logHelper.Debug("There is no categories for resources "+gson1.toJson(listaRecursosSinCategoria));

			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the categories of resources: "+gson1.toJson(resourceId_list)+". Error descripcion "+ex.getMessage());
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

			tagsList= gson.fromJson(response, new TypeToken<ArrayList<ResponseGetTags>>() {}.getType());
			
			if(tagsList!=null && tagsList.size()==resourceId_list.size()) {
				_logHelper.Debug("Tags of resources "+ gson.toJson(resourceId_list)+ ":"+ response);
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
				_logHelper.Debug("There is no tags for resources "+gson.toJson(resourceId_list));

			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the tags of resources :"+gson.toJson(resourceId_list)+" Error description "+ex.getMessage());
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
			Type type = new TypeToken<ArrayList<ResponseGetMainImage>>(){}.getType();
			mainImagesList=gson.fromJson(response, type);

			if(mainImagesList!=null && mainImagesList.size()==0) {
				_logHelper.Debug("Main images of resources "+gson.toJson(resourceId_list)+":"+response);

			}else {
				_logHelper.Debug("There is no main images for resources: "+gson.toJson(resourceId_list));
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting thhe main images of resources: "+ gson.toJson(resourceId_list)+". Error description: " +ex.getMessage());
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
			Type type = new TypeToken<HashMap<UUID, String>>(){}.getType();
			resource= gson.fromJson(response, type);

			if(resource!=null) {
				_logHelper.Debug("Increased reading obtained");
			}else {
				_logHelper.Debug("Error getting increased reading");
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the increades reading "+ex.getMessage());
			throw ex;
		}
		return resource;
	}

	/**
	 * Gets the resource by ID
	 * @param resourceId Resource identifier
	 * @return Resource object
	 */
	public Resource getResource(UUID resourceId) throws Exception {
	    return getResource(resourceId, getCommunityShortName());
	}
	
	public Resource getResource(UUID resourceId, String pCommunityShortName) throws Exception {
		Resource resource=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-resource?resource_id="+resourceId+"&community_short_name="+URLEncoder.encode(pCommunityShortName);
			String response=WebRequest("GET", url, "application/x-www-form-urlencoded");
			resource=gson.fromJson(response, Resource.class);

			if(resource!=null) {
				_logHelper.Debug("Resource "+resourceId+" obtained");

			}else {
				_logHelper.Debug("Error getting the resource "+resourceId);
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the resource "+resourceId+", "+ex.getMessage());
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
				_logHelper.Debug("Rdf obtained for the resource "+resourceId);
			}else {
				_logHelper.Debug("There is no rdf for the resource "+resourceId);
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting the rdf for the resource "+resourceId);
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
	public void insertAttribute(String graph, String value) throws Exception {
		InsertAttributeParams insertAttribute=null;

		try {
			String url=getApiUrl()+"/resource/insert-attribute";
			insertAttribute= new InsertAttributeParams();
			insertAttribute.setGraph(graph);
			insertAttribute.setValue(value);
			WebRequestPostWithJsonObject(url, insertAttribute);

			_logHelper.Debug("Ended inserting the value: "+value+" in the graph: "+graph);

		}catch(Exception ex) {
			_logHelper.Error("Error inserting value:"+value+" in the graph "+graph+". Error description: "+ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Checks whether the url exists in a resource of the community. (Searchs on the resource description)
	 * @param url link to search in the community
	 * @return True if the link exists in a resource of the community
	 * @throws Exception exception 
	 */
	public boolean existsUrl (String url) throws Exception{
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
			_logHelper.Debug("Ended resource persistent deleting");


		}
		catch(Exception ex) {
			_logHelper.Error("Error on a searching of an url. \r\n:  Json "+gson.toJson(model)+", "+ ex.getMessage());
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
	public boolean linkResource(UUID resourceId, ArrayList<UUID> resourceListToLink) throws Exception {
		boolean loaded=false;
		LinkedParams model=null;
		Gson gson = new Gson();
		try {

			String url=getApiUrl()+"/resource/link-resource";
			model= new LinkedParams();
			model.setResource_id(resourceId);
			model.setResource_list_to_link(resourceListToLink);
			model.setCommunity_short_name(getCommunityShortName());

			WebRequestPostWithJsonObject(url, model);
			loaded=true;
			_logHelper.Debug("Ended link resources");
		}
		catch(Exception ex) {
			_logHelper.Error("Error linked resource "+resourceId+". \r\n Json: "+gson.toJson(model)+", "+ex.getMessage());
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
	public boolean share(String targetCommunity, UUID resourceId, ArrayList<UUID> categories, String publisher_email) throws Exception {
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
			_logHelper.Debug("Ended resource sharing");
			shared=true;
		}
		catch(Exception ex) {
			_logHelper.Error("Error sharing resource "+resourceId+ ". \r\n: Json:"+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
		return shared;
	}

	/**
	 * Shares the resource in the target community (with userId)
	 * @param targetCommunity target community short name string
	 * @param resourceId resource identifier Guid
	 * @param categories guid list where the document is going to be shared to
	 * @param user_Id User identifier
	 * @return boolean T or F
	 */
	public boolean share(String targetCommunity, UUID resourceId, ArrayList<UUID> categories, UUID user_Id) throws Exception {
	    boolean shared = false;
	    ShareParams model = null;
	    Gson gson = new Gson();
	    try {
	        String url = getApiUrl() + "/resource/share";
	        model = new ShareParams();
	        model.setDestination_community_short_name(targetCommunity);
	        model.setResource_id(resourceId);
	        model.setCategories(categories);
	        model.setUserId(user_Id);
	        
	        WebRequestPostWithJsonObject(url, model);
	        _logHelper.Debug("Ended resource sharing");
	        shared = true;
	    } catch(Exception ex) {
	        _logHelper.Error("Error sharing resource " + resourceId + ". \r\n: Json:" + gson.toJson(model) + ", " + ex.getMessage());
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
	public boolean shareResources(List<ShareParams> parameters) throws Exception {
		boolean shared=false;
		Gson gson = new Gson();

		try {
			String url=getApiUrl()+"/resource/share-resources";
			WebRequestPostWithJsonObject(url, parameters);
			_logHelper.Debug("Ended resource sharing");
			shared=true;
		}
		catch(Exception ex) {
			_logHelper.Error("Error sharing resources. \r\n: Json: "+gson.toJson(parameters));
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
			_logHelper.Debug("Ended resource main image setting");
			setted=true;
		}
		catch(Exception ex) {
			_logHelper.Error("Error setting resource main image "+resourceId+". \r\n: Json: "+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}
		return setted;
	}


	/**
	 * Adds a comment in a resource. It can be a response of another parent comment. (with userId)
	 * @param resourceId resource identifier
	 * @param userShortName User identifier
	 * @param description Html content of the comment
	 * @param parentCommentId optional parent comment identifier Guid
	 * @param commentDate comment date
	 * @param publishHome indicates whether the home must be updated
	 * @return UUID comment identifier
	 */
	public UUID comment (UUID resourceId, String userShortName, String description, UUID parentCommentId, Date commentDate, boolean publishHome) throws Exception {
		UUID commendId=UUID.fromString("00000000-0000-0000-0000-000000000000");
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

			UUID comentId=UUID.fromString(response.replace("\"", ""));
			if(comentId!=null) {
				_logHelper.Debug("Ended resource "+resourceId+" comment: "+comentId);
			}else {
				_logHelper.Debug("Error comenting resource "+resourceId);
			}
			return comentId;
		}catch(Exception ex) {
			_logHelper.Error("Error comenting resource "+resourceId+". \r\n: Json: "+gson.toJson(model)+", "+ex.getMessage());
			throw ex;
		}

	}

	/**
	 * Adds a comment in a resource. It can be a response of another parent comment. (with userId)
	 * @param resourceId resource identifier
	 * @param user_Id User identifier
	 * @param description Html content of the comment
	 * @param parentCommentId optional parent comment identifier Guid
	 * @param commentDate comment date
	 * @param publishHome indicates whether the home must be updated
	 * @return UUID comment identifier
	 */
	public UUID comment(UUID resourceId, UUID user_Id, String description, UUID parentCommentId, Date commentDate, boolean publishHome) throws Exception {
	    UUID commentId = UUID.fromString("00000000-0000-0000-0000-000000000000");
	    CommentParams model = null;
	    Gson gson = new Gson();
	    try {
	        String url = getApiUrl() + "/resource/comment";
	        model = new CommentParams();
	        model.setResource_id(resourceId);
	        model.setCommunity_short_name(getCommunityShortName());
	        model.setUserId(user_Id);
	        model.setHtml_description(description);
	        model.setComment_date(commentDate);
	        model.setParent_comment_id(parentCommentId);
	        model.setPusblish_home(publishHome);
	        
	        String response = WebRequestPostWithJsonObject(url, model);
	        
	        commentId = UUID.fromString(response.replace("\"", ""));
	        if(commentId != null) {
	            _logHelper.Debug("Ended resource " + resourceId + " comment: " + commentId);
	        } else {
	            _logHelper.Debug("Error commenting resource " + resourceId);
	        }
	        return commentId;
	    } catch(Exception ex) {
	        _logHelper.Error("Error commenting resource " + resourceId + ". \r\n: Json: " + gson.toJson(model) + ", " + ex.getMessage());
	        throw ex;
	    }
	}
	
	/**
	 * Get metakeywords of the ontology
	 * @param resourceID Resource
	 * @return Dictionary with all the metakeywords
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<UUID, ArrayList<MetaKeyword>> getMetakeywords (UUID resourceID, String pOntologyNameWithExtension) throws Exception {
		Gson gson = new Gson();
		GetMetakeywordsModel metakeywordsModel = new GetMetakeywordsModel();
		try {
			metakeywordsModel.setCommunity_short_name(getCommunityShortName());
			metakeywordsModel.setResource_id(resourceID);
			metakeywordsModel.setOntology_name(pOntologyNameWithExtension);

			String url = getApiUrl()+"/resource/get-metakeywords";

			String response = WebRequestPostWithJsonObject(url, metakeywordsModel);
			
			HashMap<UUID, ArrayList<MetaKeyword>> metakeywords = new HashMap<UUID, ArrayList<MetaKeyword>>();

			Type fooType = new TypeToken<HashMap<UUID, ArrayList<MetaKeyword>>>() {}.getType();    
			
			
			metakeywords = gson.fromJson(response, fooType);

			return metakeywords;
		} catch(Exception ex) {
			_logHelper.Error("Error getting metakeywords from resource " + resourceID + ". \r\n: Json: "+gson.toJson(metakeywordsModel)+", "+ex.getMessage());
			throw ex;
		}
	}


	/**
	 * Creates a complex ontology resource
	 * @param parameters parameters
	 * @return resource identifier guid 
	 * @throws Exception exception 
	 */
	public String  createBasicOntologyResource(LoadResourceParams parameters) throws Exception {
		String resourceId="";
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/create-basic-ontology-resource";
			resourceId=WebRequestPostWithJsonObject(url, parameters);

			if(!resourceId.isEmpty() || !StringUtils.isBlank(resourceId)) {
				_logHelper.Debug("Basic ontology resource created: "+resourceId);
			}
			else {
				_logHelper.Debug("Basic ontology resource not created: "+gson.toJson(parameters));
			}
		}
		catch(Exception ex) {
			_logHelper.Error("Error trying to create a basic ontology resource. \r\n Error description: "+ex.getMessage()+ ".\r\n Json: "+gson.toJson(parameters));
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
	public String massiveComplexOntologyResourceCreation(List<ComplexOntologyResource> parameters, UUID pCargaID, boolean hierarquicalCategories, String communityShortName) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
		List<LoadResourceParams> listaLoadResourceParams = new ArrayList<LoadResourceParams>();
		Gson gson = new Gson();

		for(ComplexOntologyResource resource : parameters) {
			if(resource.getTextCategories()!=null) {
				if(hierarquicalCategories) {
					if(communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) {
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}else {
						resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}else {
					if(communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) {
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
					}else {
						resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories(), communityShortName));
					}
				}
			}
			String documentId="";

			if(communityShortName.isEmpty() || StringUtils.isBlank(communityShortName)) {
				communityShortName=getCommunityShortName();
			}
			LoadResourceParams resourceParam=getResourceModelOfComplexOntologyResource(communityShortName, resource, false, false);
			listaLoadResourceParams.add(resourceParam);
			resource.setUploaded(true);
		}

		MassiveResourceLoad massiveLoad= new MassiveResourceLoad();
		massiveLoad.setResources(listaLoadResourceParams);
		massiveLoad.setLoad_id(pCargaID);
		String resourceId="";

		try {
			String url=getApiUrl()+"/MassiveResource/massive-complex-ontology-resource-creation";
			WebRequestPostWithJsonObject(url, massiveLoad);

			if(!StringUtils.isBlank(resourceId) || !resourceId.isEmpty()) {
				_logHelper.Debug("Complex ontology resource created: "+resourceId);
			}else {
				_logHelper.Debug("Massive Complex ontology resource not created: "+gson.toJson(massiveLoad));
			}
		}catch(Exception ex) {
			_logHelper.Error("Error trying to create a Massive complex ontology resource. \r\n Error description: "+ex.getMessage()+". \r\n Json: "+gson.toJson(massiveLoad));
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
	public boolean modifyBasicOntologyResource(LoadResourceParams parameters) throws Exception {
		boolean modified=false;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/modify-basic-ontology-resource";
			String response=WebRequestPostWithJsonObject(url, parameters);

			_logHelper.Debug("Ended resource main image setting");
			modified=gson.fromJson(response, Boolean.class);

		}catch(Exception ex) {
			_logHelper.Error("Error modifying resource "+parameters.resource_id +". \r\n Json: "+gson.toJson(parameters)+", "+ex.getMessage());
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
	 * @param endOfLoad Indicates the resource modified is the last and it must deletes cache
	 * @param userId User that try to modify the resource
	 * @throws Exception Exception 
	 */
	public void modifyTripleList(UUID resourceId, List<ModifyResourceTriple> tripleList, boolean publishHome, String mainImage, ArrayList<SemanticAttachedResource> resourceAttachedFiles, boolean endOfLoad, UUID userId) throws Exception {

		ModifyResourceTripleListParams model=null;
		Gson gson = new Gson();

		try {
			String url=getApiUrl()+"/resource/modify-triple-list";
			model= new ModifyResourceTripleListParams();
			model.setResource_triples(tripleList);
			model.setResource_id(resourceId);
			model.setCommunity_short_name(getCommunityShortName());
			model.setPublish_home(publishHome);
			model.setMain_image(mainImage);
			model.setResource_attached_files(resourceAttachedFiles);
			model.setEnd_of_load(endOfLoad);
			model.setUser_id(userId);
			
			WebRequestPostWithJsonObject(url, model);
			_logHelper.Debug("Ended resource triples list modification");


		}catch(Exception ex) {
			_logHelper.Error("Error modifying resource triples list. \r\n: Json: " + gson.toJson(model) + ", " + ex.getMessage());
			throw ex;
		}
	}


	/**
	 * Modify a big list of triples
	 * @param parameters Parameters for the modification
	 * @throws Exception exception 
	 */
	public void masiveTriplesModify(MassiveTripleModifyParameters parameters) throws Exception {
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/masive-triple-modify";
			WebRequestPostWithJsonObject(url, parameters);
		}
		catch(Exception ex) {
			_logHelper.Error("Error modifying massive triples. \r\n: Json: "+gson.toJson(parameters)+", "+ex.getMessage());
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
	public void lockResource(UUID resourceId, int secondsTimeout, int secondsLockDuration) throws Exception {
		String url=getApiUrl()+"/resource/lock-document?community_short_name="+getCommunityShortName()+"&resource_id="+resourceId+"&lock_seconds_duration="+secondsLockDuration+"&timeout_seconds="+secondsTimeout;
		Gson gson = new Gson();
		try {
			String token=WebRequest("POST", url);
			token=gson.fromJson(token, String.class);
			SetLockTokenForResource(resourceId, token);
		}catch(Exception ex) {
			_logHelper.Error("Error, the document "+resourceId+" can´t be locked. "+ ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Unlocks a resource previously locked. 
	 * This method must be called from a finally clause, in order to be sure you unlock a locked resource if something goes wrong
	 * @param resourceId Resource identifier to unlock
	 * @throws Exception exception 
	 */
	public void unlockResource (UUID resourceId) throws Exception {
		String token=GetLockTokenForResource(resourceId);
		String url= getApiUrl()+"/resource/unlock-document?community_short_name="+getCommunityShortName()+"&resource_id="+resourceId+"&token="+token;

		try {
			WebRequest("POST", url);
			SetLockTokenForResource(resourceId, null);
		}catch(Exception ex) {
			_logHelper.Error("Error, the document "+resourceId+ " can´t be unlocked. "+ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Checks if a resource has been previously locked. 
	 * @param resourceId Resource identifier to verify
	 * @return boolean  T or F 
	 * @throws Exception exception 
	 */
	public boolean checkLockedResource (UUID resourceId) throws Exception {
		String token=GetLockTokenForResource(resourceId);
		String url=getApiUrl()+"/resource/check-document-is-locked?resource_id="+resourceId;
		Gson gson = new Gson();

		try {
			return gson.fromJson(WebRequest("GET", url), Boolean.class);
		}catch(Exception ex) {
			_logHelper.Error("Error, we couldn´t verify if resource "+resourceId+" was locked. "+ex.getMessage());
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
	public List<UUID> getModifiedResourcesFromDate(String searchDate) throws Exception{
		List<UUID> resources=null;
		Gson gson = new Gson();
		try {
			String url=getApiUrl()+"/resource/get-modified-resources?community_short_name="+getCommunityShortName()+"&search_date="+searchDate;
			String response=WebRequest("GET", url);
			Type type = new TypeToken<ArrayList<UUID>>(){}.getType();
			resources=gson.fromJson(response, type);

			_logHelper.Debug("Resources obtained of the community "+getCommunityShortName()+" from date "+searchDate);

		}catch(Exception ex) {
			_logHelper.Error("Error getting the resources of "+getCommunityShortName()+" from date "+searchDate);
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
	public ResourceNoveltiesModel getResourceNoveltiesFromDate(UUID resourceId, String searchDate) throws Exception {
		ResourceNoveltiesModel resource=null;
		Gson gson = new Gson();

		try {
			String url=getApiUrl()+"/resource/get-resource-novelties?resource_id="+resourceId+"&community_short_name="+getCommunityShortName()+"&search_date="+searchDate;
			String response = WebRequest("GET", url, "application/x-www-form-urlencoded");
			Type type = new TypeToken<ResourceNoveltiesModel>(){}.getType();
			resource=gson.fromJson(response, type);

			if(resource!=null) {
				_logHelper.Debug("Obtained the resource "+resourceId+" of the community "+getCommunityShortName()+" from date "+searchDate);
			}
			else {
				_logHelper.Debug("The resource "+resourceId+" could not be obtained of the community "+getCommunityShortName()+" from date "+searchDate);
			}
		}catch(Exception ex) {
			_logHelper.Error("Error getting  the resource "+resourceId+" of the community "+getCommunityShortName()+" from date "+searchDate);
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
	private LoadResourceParams getResourceModelOfBasicOntologyResource(String communityShortName, BasicOntologyResource rec, boolean pEsUltimo, short pTipoDoc) {

		LoadResourceParams model= new LoadResourceParams();
		model.setResource_id(rec.getShortGnossId());
		model.setCommunity_short_name(communityShortName);
		model.setTitle(rec.getTitle());
		model.setDescription(rec.getDescription());
		ArrayList<String> tags = new ArrayList<>(List.of(rec.getTags()));
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
		model.setResource_file(rec.AttachedFile);
		model.setCreator_is_author(rec.getCreatorIsAuthor());
		model.setAuthors(rec.getAuthor());
		model.setAuto_tags_title_text(rec.getAutomaticTagsTextFromTitle());
		model.setAuto_tags_description_text(rec.getAutomaticTagsTextFromDescription());
		model.setCreate_screenshot(rec.GenerateSnapshot);
		model.setUrl_screenshot(rec.DownloadUrl);

		if(rec.SnapshotSizes != null) {
			ArrayList<Integer> Snap = new ArrayList(List.of(rec.SnapshotSizes));
			model.setScreenshot_sizes(Snap);
		}
		model.setEnd_of_load(pEsUltimo);
		model.setCreation_date(rec.getCreationDate());
		model.setPublish_home(rec.getPublishInHome());
		int i = rec.getVisibility().getID();
		model.setVisibility( (short) i);
		model.setEditors_list(rec.getEditorsGroups());
		model.setReaders_list(rec.getReadersGroups());

		return model;

	}

	private LoadResourceParams getResourceModelOfCOmplexOntologyResource(String communityShortName, ComplexOntologyResource rec, boolean pCrearVersion, boolean pEsUltimo) throws IOException, GnossAPIException {
		LoadResourceParams model = new LoadResourceParams();
		model.setResource_id(rec.getShortGnossId());
		model.setCommunity_short_name(communityShortName);
		model.setTitle(rec.getTitle());
		model.setDescription(rec.getDescription());
		String vis= TiposDocumentacion.ontology.toString();
		int i=Integer.parseInt(vis);
		model.setResource_type( (short) i);

		if(rec.getTags()!=null) {
			ArrayList<String> tags = new ArrayList<>(List.of(rec.getTags()));
			model.setTags(tags);
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
		model.setResource_file(rec.getRdfFile());

		int k=0;
		if(rec.getAttachedFiles().size()>0) {
			model.setResource_attached_files(new ArrayList<SemanticAttachedResource>());

			for(String nombre : rec.getAttachedFilesName()) {
				SemanticAttachedResource adjunto = new SemanticAttachedResource();
				adjunto.setFile_rdf_property(nombre);
				int s = Integer.parseInt(Arrays.toString(rec.getAttachedFiles().get(k)));
				adjunto.setFile_property_type( (short) s);
				adjunto.setRdf_attached_file(rec.getAttachedFiles().get(k));
				adjunto.setDelete_file(rec.getAttachedFiles().get(k) == null);
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
			ArrayList screenshots = new ArrayList<>(List.of(rec.getScreenshotSizes()));
			model.setScreenshot_sizes(screenshots);
		}

		model.setEnd_of_load(pEsUltimo);
		model.setCreation_date(rec.getCreationDate());
		model.setPublisher_email(rec.getPublisherEmail());
		model.setMain_image(rec.getMainImage());
		int h = rec.getVisibility().getID();
		model.setVisibility( (short) h);
		model.setEditors_list(rec.getEditorsGroups());
		model.setReaders_list(rec.getReadersGroups());
		model.setAumented_reading(rec.getAumentedReading());
		
		return model;
	}


	//region Common methods for basic and complex ontology resources
	private HashMap<UUID, Boolean> modifyPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, String communityShortName, int numAttemps , boolean publishHome , UUID userId)
	{
	    HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
	    int procesedNumber=0;
	    int attempNumber=0;

	    ArrayList<String> valores= new ArrayList<String>();
	    HashMap<UUID, ArrayList<TriplesToModify>> toModify= new HashMap<UUID, ArrayList<TriplesToModify>>(resourceTriples);
	    
	    while(toModify!=null && toModify.size()>0 && attempNumber<numAttemps) {
	        int i=0;
	        int contResources=toModify.keySet().size();

	        Iterator<UUID> iterator = toModify.keySet().iterator();
	        
	        while(iterator.hasNext()) {
	            UUID docID = iterator.next();
	            i++;
	            ArrayList<ModifyResourceTriple> listaValores= new ArrayList<ModifyResourceTriple>();
	            attempNumber++;
	            procesedNumber++;
	            
	            for(TriplesToModify mT : toModify.get(docID)) {
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
	                modifyTripleList(docID, listaValores, publishHome, null, null, endOfLoad, userId);
	                _logHelper.Debug(procesedNumber+" of "+toModify.size()+". Object: "+docID+". Resource: "+toModify.get(docID).toArray());
	                
	                iterator.remove();

	                result.put(docID, true);
	            }catch(Exception ex) {
	                _logHelper.Error("Resource "+docID+": "+ex.getMessage());

	                result.put(docID, false);
	            }
	        }
	        _logHelper.Debug("******************** Lap number: "+attempNumber +" finished");
	    }

	    return result;
	}

	private HashMap <UUID, Boolean> insertPropertiesLoadedResourcesInt(HashMap<UUID, ArrayList<TriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome, UUID usuarioId){
	    HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
	    int procesedNumber=0;
	    int attempNumber=0;

	    HashMap<UUID, ArrayList<TriplesToInclude>> toInsert= new HashMap<UUID, ArrayList<TriplesToInclude>>(resourceTriples);
	    
	    while(toInsert!=null && toInsert.size()>0 && attempNumber<numAttemps) {
	        int i=0;
	        int contResources=toInsert.keySet().size();

	        Iterator<UUID> iterator = toInsert.keySet().iterator();
	        
	        while(iterator.hasNext()) {
	            UUID docID = iterator.next();
	            i++;
	            ArrayList<ModifyResourceTriple> listaValores= new ArrayList<ModifyResourceTriple>();
	            attempNumber++;
	            procesedNumber++;
	            
	            for(TriplesToInclude mT : toInsert.get(docID)) {
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
	                modifyTripleList(docID, listaValores, publishHome, null, null, endOfLoad, usuarioId);
	                _logHelper.Debug(procesedNumber+" of "+toInsert.size()+". Object: "+docID+". Resource: "+toInsert.get(docID).toArray());
	                
	                iterator.remove();

	                result.put(docID, true);
	            }catch(Exception ex) {
	                _logHelper.Error("Resource "+docID+": "+ex.getMessage());

	                result.put(docID, false);
	            }
	        }
	        _logHelper.Debug("******************** Lap number: "+attempNumber +" finished");
	    }
	    return result;
	}

	private HashMap<UUID, Boolean> actionsPropertiesLoadedResourcesInt (HashMap<UUID, ArrayList<TriplesToModify>> resourceTriples, HashMap<UUID, ArrayList<RemoveTriples>> deleteList, HashMap<UUID, ArrayList<TriplesToInclude>>insertList,  HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntitiesInsertTriplesList, String communityShortName, boolean publishHome){

	    HashMap<UUID, Boolean> result= new HashMap<UUID, Boolean>();
	    int procesedNumber=0;
	    int attempNumber=0;
	    ArrayList<ModifyResourceTriple> valuesList= new ArrayList<ModifyResourceTriple>();
	    ArrayList<String> values = new ArrayList<String>();
	    HashMap<UUID, ArrayList<ModifyResourceTriple>> resources = new HashMap<UUID, ArrayList<ModifyResourceTriple>>();

	    if(deleteList!=null) {
	        HashMap<UUID, ArrayList<RemoveTriples>> toDelete = new HashMap<UUID, ArrayList<RemoveTriples>>(deleteList);
	        
	        while(toDelete!=null && toDelete.size()>0) {
	            Iterator<UUID> iterator = toDelete.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();
	                procesedNumber++;
	                attempNumber++;

	                for(RemoveTriples iT : toDelete.get(docID)) {
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
	                iterator.remove();
	            }
	        }
	    }

	    if(resourceTriples!=null) {
	        HashMap<UUID, ArrayList<TriplesToModify>> toModify = new HashMap<UUID, ArrayList<TriplesToModify>>(resourceTriples);
	        
	        while(toModify!=null && toModify.size()>0) {
	            Iterator<UUID> iterator = toModify.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();

	                if(resources.containsKey(docID)) {
	                    valuesList.addAll(resources.get(docID));
	                }
	                procesedNumber++;
	                attempNumber++;

	                for(TriplesToModify iT : toModify.get(docID)) {
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
	                resources.put(docID, valuesList);
	                iterator.remove();
	            }
	        }
	    }

	    if(insertList!=null) {
	        HashMap<UUID, ArrayList<TriplesToInclude>> toInsert = new HashMap<UUID, ArrayList<TriplesToInclude>>(insertList);
	        
	        while(toInsert!=null && toInsert.size()>0) {
	            Iterator<UUID> iterator = toInsert.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();
	                
	                if(resources.containsKey(docID)) {
	                    valuesList.addAll(resources.get(docID));
	                }
	                procesedNumber++;
	                attempNumber++;
	                
	                for(TriplesToInclude iT : toInsert.get(docID)) {
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
	                resources.put(docID, valuesList);
	                iterator.remove();
	            }
	        }
	    }

	    if(auxiliaryEntitiesInsertTriplesList!=null) {

	        HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> auxiliaryEntityTriplesToInsert= new HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>>(auxiliaryEntitiesInsertTriplesList);
	        
	        while(auxiliaryEntityTriplesToInsert!=null && auxiliaryEntityTriplesToInsert.size()>0) {

	            Iterator<UUID> iterator = auxiliaryEntityTriplesToInsert.keySet().iterator();
	            
	            while(iterator.hasNext()) {
	                UUID docID = iterator.next();
	                valuesList= new ArrayList<ModifyResourceTriple>();

	                if(resources.containsKey(docID)) {
	                    valuesList.addAll(resources.get(docID));
	                }
	                procesedNumber++;
	                attempNumber++;

	                for(AuxiliaryEntitiesTriplesToInclude iT : auxiliaryEntityTriplesToInsert.get(docID)) {
	                    ModifyResourceTriple triple= new ModifyResourceTriple();
	                    triple.setOld_object(null);
	                    triple.setPredicate(iT.getPredicate());
	                    triple.setNew_object(getApiUrl()+"items/"+iT.getName()+"_"+docID+"_"+iT.getIdentifier()+"|"+iT.getValue());
	                    triple.setGnoss_property(GnossResourceProperty.none);

	                    valuesList.add(triple);
	                }
	                resources.put(docID, valuesList);
	                iterator.remove();
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

	            modifyTripleList(docID, resources.get(docID), publishHome, null, null, endOfLoad);

	            _logHelper.Debug("Object: "+docID);

	            result.put(docID, true);
	        }catch(Exception ex) {
	            _logHelper.Error("Resource "+docID+": "+ex.getMessage());
	            
	            result.put(docID, false);
	        }
	    }

	    return result;		
	}

	private boolean insertAuxiliarEntityOnPropertiesLoadedResourceInt(HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome, UUID userId) {

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
					modifyTripleList(docID, listaValores, publishHome, null, null, endOfLoad, userId);
					_logHelper.Debug(processedNumber+" of "+resourceTriples.size()+" Object: "+docID+". Resource: "+resourceTriples.get(docID).toArray());
					pDiccionarioInsertar.remove(docID);
					inserted=true;
				}
				catch(Exception ex) {
					_logHelper.Error("Resource "+docID+" : "+ex.getMessage());
				}
			}
			_logHelper.Debug("******************** Lap number: "+attempNumber+ " finished");
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
	private void loadBasicOntologyResourceInt(BasicOntologyResource resource, boolean hierarquicalCategories, TiposDocumentacion resourceType, boolean isLast) throws Exception {
		_logHelper.Trace("******************** Begin Load"+ this.getClass().getSimpleName());

		if(((resource.getCategoriesIds()!=null && resource.getCategoriesIds().size()==0) || resource.getCategoriesIds()==null )  && resource.getTextCategories()!=null && resource.getTextCategories().size()>0) {
			if(hierarquicalCategories) {
				resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
			}else {
				resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
			}
		}
		try {
			int i= Integer.parseInt(resourceType.toString());
			LoadResourceParams model= getResourceModelOfBasicOntologyResource(getCommunityShortName(), resource, isLast, (short) i);
			String documentId=createBasicOntologyResource(model);
			resource.setUploaded(true);
			_logHelper.Debug("Loaded "+resource.getGnossId()+"\t Title: "+resource.getTitle()+ "\t ResourceID : "+documentId+ this.getClass().getSimpleName());
			resource.setShortGnossId(UUID.fromString(documentId.trim().replace("\"", "")));
			if(!documentId.equals(resource.getGnossId())) {
				throw new GnossAPIException("Resource loaded with the id :"+ documentId+ "\n The IDGnpss provided to the method is different from the returned by the API");
			}

		}catch(GnossAPIException ex) {
			_logHelper.Info("Resource "+resource.getId()+ ". Title: "+resource.getTitle()+". Message: "+ex.getMessage()+ " "+this.getClass().getSimpleName());
		}
		catch(Exception ex) {
			_logHelper.Error("ERROR at : "+resource.getId()+". Titile: "+resource.getTitle()+". Message: "+ex.getMessage()+" "+this.getClass().getSimpleName());
		}
		_logHelper.Debug("******************** End Load "+this.getClass().getSimpleName());
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
	private void loadBasicOntologyResourceIntVideo(BasicOntologyResource resource, boolean hierarquicalCategories, TiposDocumentacion resourceType, boolean isLast) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
		_logHelper.Trace("******************** Begin Load "+this.getClass().getSimpleName());
		Gson gson= new Gson();

		if(hierarquicalCategories) {
			resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
		}else {
			resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
		}

		try {
			int i= Integer.parseInt(resourceType.toString());
			LoadResourceParams model= getResourceModelOfBasicOntologyResource(getCommunityShortName(), resource, isLast, (short) i);
			String documentId=createBasicOntologyResource(model);
			resource.setUploaded(true);

			try {
				documentId=gson.fromJson(documentId, String.class);
			}catch(Exception ex) {

			}
			resource.setShortGnossId(UUID.fromString(documentId));
			_logHelper.Debug("Loaded: "+resource.getId()+" \t Title: "+resource.getTitle()+" \t ResourceID: "+documentId+" "+this.getClass().getSimpleName());
		}
		catch(Exception ex) {
			_logHelper.Error("Error at: "+resource.getId()+". Title: "+resource.getTitle()+". Message: "+ex.getMessage()+" "+this.getClass().getSimpleName());
		}
		_logHelper.Debug("******************** End Load "+this.getClass().getSimpleName());
	}

	private void loadBasicOntologyResourceListInt(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, TiposDocumentacion resourceType, int numAttemps) {

		int processedNumber=0;
	    ArrayList<BasicOntologyResource> originalResourceList = new ArrayList<BasicOntologyResource>();
	    ArrayList<BasicOntologyResource> resourcesToLoad= new ArrayList<BasicOntologyResource>(resourceList); // Copia la lista
	    int attempNumber=0;

	    while(resourcesToLoad!=null && resourcesToLoad.size()>0 && attempNumber<= numAttemps) {
	        attempNumber++;
	        _logHelper.Trace("******************** Begin lap number: "+attempNumber+" "+this.getClass().getSimpleName());

	        Iterator<BasicOntologyResource> iterator = resourcesToLoad.iterator();
	        processedNumber = 0;
	        
	        while(iterator.hasNext()) {
	            BasicOntologyResource rec = iterator.next();
	            processedNumber++;
	            
	            try {
	                loadBasicOntologyResourceInt(rec, hierarquicalCategories, resourceType, processedNumber==resourceList.size());

	                if(rec.isUploaded()) {
	                    _logHelper.Debug("Loaded: "+processedNumber+" of "+resourceList.size()+" \t ID: "+rec.getId()+" \t Title: "+rec.getTitle());
	                    iterator.remove();
	                }
	            }
	            catch(Exception ex) {
	                _logHelper.Error("ERROR in : "+processedNumber+" of "+resourceList.size()+ "\t ID: "+rec.getId()+ "  Title: "+rec.getTitle()+". Message: "+ex.getMessage());
	            }
	        }
	        
	        _logHelper.Debug("******************** Finished lap number: "+attempNumber+" "+this.getClass().getSimpleName());
	    }
	}

	private void loadBasicOntologyResourceIntVideo(BasicOntologyResource resource, boolean hierarchicalCategories, TiposDocumentacion resourceType) throws MalformedURLException, IOException, GnossAPIException, GnossAPICategoryException {
	    loadBasicOntologyResourceIntVideo(resource, hierarchicalCategories, resourceType, false);
	}
	
	private void loadBasicOntologyResourceListIntVideo(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, TiposDocumentacion resourceType, int numAttemps) {
	    int proccessedNumber=0;
	    ArrayList<BasicOntologyResource> originalResourceList= new ArrayList<BasicOntologyResource>();
	    originalResourceList=resourceList;
	    
	    ArrayList<BasicOntologyResource> resourcesToLoad= new ArrayList<BasicOntologyResource>(resourceList);
	    int attempNumber=0;

	    while(resourcesToLoad!=null && resourcesToLoad.size()>0 && attempNumber<=numAttemps) {
	        attempNumber++;
	        _logHelper.Trace("******************** Begin lap number: "+attempNumber+", "+this.getClass().getSimpleName());

	        Iterator<BasicOntologyResource> iterator = resourcesToLoad.iterator();
	        proccessedNumber = 0;
	        
	        while(iterator.hasNext()) {
	            BasicOntologyResource rec = iterator.next();
	            proccessedNumber++;

	            try {
	                loadBasicOntologyResourceIntVideo(rec, hierarquicalCategories, resourceType, proccessedNumber==resourceList.size());
	                _logHelper.Debug("Loaded: "+proccessedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+" \t Title: "+rec.getTitle());
	                
	                iterator.remove();
	            }
	            catch(Exception ex) {
	                _logHelper.Error("ERROR in: "+proccessedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Mensaje: "+ex.getMessage()+", "+this.getClass().getSimpleName());
	            }
	        }
	        _logHelper.Debug("******************** Finished lap number: "+attempNumber+", "+this.getClass().getSimpleName());
	    }
	}
	
	public void modifyBasicOntologyResource(BasicOntologyResource resource, boolean hierarquicalCategories, boolean isLast) {
		try {
			if(resource.getTextCategories()!=null && resource.getTextCategories().size() > 0) {
				if(hierarquicalCategories) {
					resource.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
				else {
					resource.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(resource.getTextCategories()));
				}
			}
			LoadResourceParams model= getResourceModelOfBasicOntologyResource(getCommunityShortName(), resource, isLast, (short)-1);
			resource.setUploaded(modifyBasicOntologyResource(model));
		}
		catch(Exception ex) {
			_logHelper.Error("The basic Ontology resource with  id: "+resource.getGnossId()+" has not been modified, "+ex.getMessage());

		}
	}

	/**
	 * Modifies the basic ontology resource of the list
	 * @param resourceList It is necessary that the basic ontology resource has assigned the property. Resource list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 2. Number of retries loading of the failed load of a resource
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public void modifyBasicOntologyResourceList(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) throws GnossAPICategoryException {
	    ArrayList<BasicOntologyResource> originalResourceList= new ArrayList<BasicOntologyResource>();
	    originalResourceList=resourceList;
	    
	    ArrayList<BasicOntologyResource> resourcesToModify= new ArrayList<BasicOntologyResource>(resourceList);
	    int processedNumber=0;
	    int attempNumber=0;

	    while(resourcesToModify!=null && resourcesToModify.size()>0 && attempNumber<numAttemps) {
	        attempNumber++;
	        if(numAttemps>1) {
	            _logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
	        }
	        
	        Iterator<BasicOntologyResource> iterator = resourcesToModify.iterator();
	        processedNumber = 0;
	        
	        while(iterator.hasNext()) {
	            BasicOntologyResource rec = iterator.next();
	            processedNumber++;

	            try {
	                modifyBasicOntologyResource(rec, hierarquicalCategories, processedNumber==resourceList.size());
	                
	                iterator.remove();
	            }
	            catch(Exception ex) {
	                _logHelper.Error("ERROR at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title. "+rec.getTitle()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
	            }
	        }
	        
	        if(numAttemps>1) {
	            _logHelper.Trace("******************** Lap number: "+attempNumber+" finalizada ", this.getClass().getSimpleName());
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
	public void modifyComplexSemanticResourceListWithOntologyAndCommunity(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String ontology, String communityShortName, int numAttemps) throws GnossAPICategoryException{
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
				_logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			for(ComplexOntologyResource rec : resourceList1) {
				processedNumber++;

				try {
					modifyComplexSemanticResourceWithOntologyAndCommunity(rec, hierarquicalCategories, processedNumber==resourceList.size(), ontology, communityShortName);
					resourcesToModify--;
					_logHelper.Debug("There are "+resourcesToModify+ " resources left to modify");
				}
				catch(Exception ex) {
					_logHelper.Error("ERROR at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				_logHelper.Trace("******************** Lap number: "+attempNumber+" finalizada ", this.getClass().getSimpleName());
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
	private void modifyComplexSemanticResourceWithOntologyAndCommunity(ComplexOntologyResource rec,
			boolean hierarquicalCategories, boolean b, String ontology, String communityShortName) {

		_logHelper.Trace("******************** Begin the resource modification: "+rec.getGnossId(), this.getClass().getSimpleName(), getCommunityShortName());
		try {
			if(rec.getTextCategories()!=null && rec.getTextCategories().size() > 0) {
				if(hierarquicalCategories) {
					rec.setCategoriesIds(getHierarquicalCategoriesIdentifiersList(rec.getTextCategories()));
				}else {
					rec.setCategoriesIds(getNotHierarquicalCategoriesIdentifiersList(rec.getTextCategories()));
				}
			}
			String ontologyUrl=ontology.toLowerCase().replace(".owl", "");
			ontologyUrl=getOntologyUrl().replace((getOntologyUrl().substring(getOntologyUrl().lastIndexOf("/")+1)), ontologyUrl+".owl");
			LoadResourceParams model=getResourceModelOfComplexOntologyResource(getCommunityShortName(), rec, false, b);
			model.setResource_url(ontologyUrl);
			rec.setModified(modifyComplexOntologyResource(model));

			if(rec.isModified()) {
				_logHelper.Debug("Successfully modified the resource with id: "+rec.getId()+" and Gnoss identifier "+rec.getShortGnossId()+" belonging to the ontology '"+ontologyUrl+"' and RdfType='"+rec.getOntology().getRdfType()+"'", this.getClass().getSimpleName());
			}else {
				_logHelper.Error("The resources with id: "+rec.getShortGnossId()+" of the ontology '"+ontologyUrl+"' hhas not been modified.", this.getClass().getSimpleName());
			}
		}
		catch(Exception ex ) {
			_logHelper.Error("The resource with id "+rec.getGnossId()+" has not been modified, "+ex.getMessage());
		}
	}

	/**
	 * Modifies the basic ontology resource. It is necessary that the basic ontology resource has assigned the property
	 * @param resourceList List of resources to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 1. Number of retries loading of the failed load of a resource
	 * @throws GnossAPICategoryException Gnoss API Category Exception 
	 */
	public void modifyComplexSemanticResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) throws GnossAPICategoryException{
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
				_logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			for(ComplexOntologyResource rec : resourceList1) {
				processedNumber++;

				try {
					modifyComplexOntologyResource(rec, hierarquicalCategories, processedNumber==resourceList.size());
					resourcesToModify--;
					_logHelper.Debug("There are "+resourcesToModify+" resources left to modify.");
				}
				catch(Exception ex) {
					_logHelper.Error("Error at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Mesage: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				_logHelper.Trace("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
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
	public void modifyComplexSemanticResourceListCommunityShortName(ArrayList<ComplexOntologyResource> resourceList, boolean hierarquicalCategories, String communityShortName, int numAttemps) throws GnossAPICategoryException{
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
				_logHelper.Trace("******************** Begin lap number: "+attempNumber, this.getClass().getSimpleName());
			}
			for(ComplexOntologyResource rec : resourceList1) {
				processedNumber++;

				try {
					modifyComplexSemanticResourceCommunityShortName(rec, hierarquicalCategories, processedNumber==resourceList.size(), communityShortName);
					resourcesToModify--;
					_logHelper.Debug("There are "+resourcesToModify+" resources left to modify.");
				}
				catch(Exception ex) {
					_logHelper.Error("Error at: "+processedNumber+" of "+resourceList.size()+"\t ID: "+rec.getId()+". Title: "+rec.getTitle()+". Mesage: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			if(numAttemps>1) {
				_logHelper.Trace("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
			}

		}
	}

	public void modifyCategoriesTagsComplexOntologyResource(UUID resourceID, String property, ArrayList<String> listToModify, boolean hierarquicalCategories ) throws Exception {
		_logHelper.Debug("******************** Start modification: ", this.getClass().getSimpleName());
		List<UUID> categoriesIdentifiers=null;
		String newObject="";

		if(listToModify!=null && property.equals("skos:ConceptID")) {
			categoriesIdentifiers= new ArrayList<UUID>();

			if(hierarquicalCategories) {
				categoriesIdentifiers=getHierarquicalCategoriesIdentifiersList(listToModify);
			}
			else {
				categoriesIdentifiers=getNotHierarquicalCategoriesIdentifiersList(listToModify);
			}
		}
		else {
			if(listToModify!=null && property.equals("sioc_t:Tag")) {
				for(String identificador : listToModify) {
					if(!identificador.isEmpty()) {
						newObject+=identificador.trim()+",";
					}
				}
			}
		}
		if(categoriesIdentifiers!=null) {
			for(UUID identifier : categoriesIdentifiers) {
				if(identifier.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
					newObject=identifier.toString()+",";
				}else {
					newObject+=identifier.toString()+",";
				}
			}
		}
		modifyProperty(resourceID, property, newObject);
		Calendar c1 = Calendar.getInstance();
		_logHelper.Debug("The resource '"+resourceID+"' has been modifies correctly ..."+c1 );
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
	public void removeResourceAttachedFiles(UUID resourceId, ArrayList<RemoveTriples> removeTripleList, boolean publishHome) throws IOException, GnossAPIException, Exception{
		int numTriples=removeTripleList.size();
		String attachedValue="";
		String attachedPredicate="";
		short attachedObjectType =-1;

		if(!resourceId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
			ArrayList<ModifyResourceTriple> triplesList = new ArrayList<ModifyResourceTriple>();
			ArrayList<SemanticAttachedResource> resourceAttachedFiles= new ArrayList<SemanticAttachedResource>();

			for(RemoveTriples tripleToDelete : removeTripleList) {
				if(tripleToDelete!= null) {
					attachedValue=tripleToDelete.getValue();
					attachedPredicate=tripleToDelete.getPredicate();
					attachedObjectType=tripleToDelete.getObjectType();

					if((attachedValue != null && !attachedValue.isEmpty()) && (attachedPredicate != null && !attachedPredicate.isEmpty()) && attachedObjectType!=-1) {
						ModifyResourceTriple triple= new ModifyResourceTriple();
						triple.setOld_object(attachedValue);
						triple.setPredicate(attachedPredicate);
						triple.setNew_object("");
						triple.setGnoss_property(GnossResourceProperty.none);

						triplesList.add(triple);

						SemanticAttachedResource attach= new SemanticAttachedResource();
						attach.setFile_rdf_property(attachedValue);
						attach.setFile_property_type(attachedObjectType);
						attach.setRdf_attached_file(null);
						attach.setDelete_file(true);

						resourceAttachedFiles.add(attach);
					}
				}
			}
			if(removeTripleList.size()>0) {
				modifyTripleList(resourceId, triplesList, publishHome, null, resourceAttachedFiles, true);
				_logHelper.Debug("Modified resource with attached. ResourceId: "+resourceId);
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
	public void attachFileToResource (UUID resourceId, String filePredicate, String fileName, ArrayList<String> fileRdfPropertiesList, ArrayList<Short> filePropertiesTypeList, ArrayList<byte[]> attachedFilesList, boolean publishHome) throws IOException, GnossAPIException, Exception {
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
				attach.setRdf_attached_file(attachedFilesList.get(i));
				attach.setDelete_file(attachedFilesList.get(i) == null);
				i++;
				resourceAttachedFiles.add(attach);
			}
			
			modifyTripleList(resourceId, triplesList, publishHome, null, resourceAttachedFiles, true);
		}

		_logHelper.Debug("Modified the resource with attached file: "+resourceId);
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
	 * @param attachedFilesList List Images to attach
	 * @param publishHome Indicates whether the home must be updated
	 * @throws IOException IO Exception
	 * @throws GnossAPIException Gnoss API 
	 * @throws Exception exception 
	 */
	public void replaceResourceImage(UUID resourceId, String oldImageName, String newImageName, String imagePredicate, ArrayList<String> fileRdfPropertiesList, ArrayList<Short> filePropertiesTypeList, ArrayList<byte[]> attachedFilesList, boolean publishHome) throws IOException, GnossAPIException, Exception {
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
				attach.setRdf_attached_file(attachedFilesList.get(i));
				attach.setDelete_file(attachedFilesList.get(i) == null);
				i++;
				resourceAttachedFiles.add(attach);
			}
			modifyTripleList(resourceId, triplesList, publishHome, null, resourceAttachedFiles, true);

		}
		_logHelper.Debug("Modified the resource with attached image: "+resourceId);
	}


	//Region DELETE


	/**
	 * Delete resources list. It is necessary that the resource has assigned the properties 
	 * @param resourceList Resources list to delete
	 * @param numAttemp Default 5. Number of retries loading of the failed load resource 
	 */
	public void deleteResourceList(ArrayList<ComplexOntologyResource> resourceList, int numAttemp) {
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
			_logHelper.Trace("******************** Starts lap number: "+attempNumber, this.getClass().getSimpleName());

			for(ComplexOntologyResource resource : originalResourceList) {
				processedNumber++;
				try {
					while(!resource.isDeleted()) {
						delete(resource.getShortGnossId(), processedNumber==originalResourceList.size());
						numResourcesLeft--;

						_logHelper.Debug("Successfully deleted the resource with ID: "+resource.getGnossId()+". "+numResourcesLeft +"resources left", this.getClass().getSimpleName());
						resource.setDeleted(true);
					}
				}catch(Exception ex) {
					_logHelper.Error("ERROR deleting: "+processedNumber+" of "+originalResourceList.size()+"\t ID: "+resource.getGnossId()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			_logHelper.Debug("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
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
	public void persistentDeleteResourceIdList(ArrayList<UUID> guidList, boolean deleteAttaches) throws Exception {
		int count=guidList.size();
		for(UUID uuid : guidList) {
			count--;
			persistentDelete(uuid, deleteAttaches, count==0);
		}
	}

	/**
	 * Persistent delete of a resources list
	 * @param resourceList Resources list to delete
	 * @param deletedAttached Indicates if the attached resources must be deleted
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void persistentDeleteResourceList(ArrayList<ComplexOntologyResource> resourceList, boolean deletedAttached, int numAttemps) {
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
			_logHelper.Trace("******************** Starts lap number: "+attempNumber, this.getClass().getSimpleName());

			for(ComplexOntologyResource resource : resourceList1) {
				processedNumber++;
				try {
					if(processedNumber==j) {
						last=true;
					}

					resource.setDeleted(persistentDelete(resource.getShortGnossId(), deletedAttached, last));
					numResourcesLeft--;
					_logHelper.Debug("Successfully deleted the resource with ID: "+resource.getGnossId()+". "+numResourcesLeft+" resources left", this.getClass().getSimpleName());
				}catch(Exception ex) {
					_logHelper.Error("ERROR deleting: "+processedNumber+" of "+originalResourceList.size()+"\t ID: "+resource.getGnossId()+". Message: "+ex.getMessage(), this.getClass().getSimpleName());
				}
			}
			_logHelper.Debug("******************** Finished lap number: "+attempNumber, this.getClass().getSimpleName());
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
	public void loadBasicOntologyResourceListIntNote(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		loadBasicOntologyResourceListInt(resourceList, hierarquicalCategories, TiposDocumentacion.note, numAttemps);
		_logHelper.Debug("Resources succesfully loaded. End of load");
	}


	/**
	 * Loads a basic ontology resource
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 2. Number of retries loading of the failed load of a resource
	 */
	public void loadBasicOntologyResourceListLink(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		loadBasicOntologyResourceListInt(resourceList, hierarquicalCategories, TiposDocumentacion.hyperlink, numAttemps);
		_logHelper.Debug("Resources succesfully loaded. End of load");
	}


	/**
	 * Loads a basic ontology resource
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void loadBasicOntologyResourceListFile(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		loadBasicOntologyResourceListInt(resourceList, hierarquicalCategories, TiposDocumentacion.server_file, numAttemps);
		_logHelper.Debug("Resources succesfully loaded. End of load");
	}


	/**
	 * Loads a basic ontology resource list with a link to a Youtube video with resource type HyperLink
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void loadBasicOntologyResourceListLinkVideo(ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		loadBasicOntologyResourceListIntVideo(resourceList, hierarquicalCategories, TiposDocumentacion.hyperlink, numAttemps);
		_logHelper.Debug("Resources succesfully loaded. End of load");
	}

	
	

	/**
	 * Loads a basic ontology resource list with resource type Video
	 * @param resourceList Resources list to load
	 * @param hierarquicalCategories Indicates whether the categories has hierarchy
	 * @param numAttemps Default 5. Number of retries loading of the failed load of a resource
	 */
	public void loadBasicOntologyResourceListVideo (ArrayList<BasicOntologyResource> resourceList, boolean hierarquicalCategories, int numAttemps) {
		loadBasicOntologyResourceListInt( resourceList, hierarquicalCategories, TiposDocumentacion.video, numAttemps);
		_logHelper.Debug("Resources succesfully loaded. End of load");
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
	public boolean insertAuxiliarEntityOnPropertiesLoadedResource (HashMap<UUID, ArrayList<AuxiliaryEntitiesTriplesToInclude>> resourceTriples, String communityShortName, int numAttemps, boolean publishHome, UUID userId) {
		if(!communityShortName.isEmpty()) {
			return insertAuxiliarEntityOnPropertiesLoadedResourceInt(resourceTriples, getCommunityShortName(), numAttemps, publishHome, userId);
		}else {
			return insertAuxiliarEntityOnPropertiesLoadedResourceInt(resourceTriples, communityShortName, numAttemps, publishHome, userId);
		}
	}


	/**
	 * Gets the ontology name (with extension) from the url of the ontology
	 * @param urlOntology URL of the ontology
	 * @return String with the ontology name 
	 */
	public static String getOntologyNameWithExtensionFromUrlOntology(String urlOntology) {
		return urlOntology.substring(urlOntology.lastIndexOf(StringDelimiters.Slash)+ StringDelimiters.Slash.length());
	}


	/**
	 * Sorts the multilanguage list of title or description with the main language in the first element
	 * @param listToOrder Lista posiblemente desordenada
	 * @return The list with the main language in the first element
	 */
	public ArrayList<Multilanguage> shortMultimediaTitleDescriptionString(ArrayList<Multilanguage> listToOrder){
		String mainLanguage=getCommunityApiWrapper().getCommunityMainLanguage();

		if(mainLanguage == null || mainLanguage.isEmpty()) {
			mainLanguage=Languages.Spanish;
		}		

		Multilanguage firstElement = new Multilanguage();

		for(Multilanguage titleDescription : listToOrder) {
			if(titleDescription.getLanguage().equals(mainLanguage)) {
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
	public String getStringForUrl(String url) throws SignatureException, MalformedURLException, URISyntaxException {

		String sign=getOAuthInstance().getSignedUrl(url);
		return sign.replace("&", ",").replace("url", "");		
	}

	public void downloadFilesFromURL(String URL, String fileName) {
		//GnossWebClient webCLient= (GnossWebClient) WebClient.create();

		try {
			//webCLient.head().header("Referer", "Referer:"+URL);
			//webCLient.head().header("User-Agent", "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36 gnossspider");
			//String sign= getSignForUrl(URL);
			//webCLient.head().header("Authorization", "Authorization: OAuth \n"+ getStringForUrl(sign));
			downloadFile(URL, fileName);
		}catch(Exception ex) {

			System.out.println("File "+fileName+" not downloaded. "+ex.getMessage());
		}
	}

	/**
	 * Gets the basic ontology resource RDF
	 * @param domain Domain URL
	 * @param resourceId Resource identifier
	 * @param directoryPath Directory path where to save the temporary file
	 * @return RDF content as string
	 */
	public String getBasicOntologyResourceRdf(String domain, String resourceId, String directoryPath) {
	    String rdf = "";
	    String resourceUrl = String.format("%s/comunidad/%s/recurso/nombre/%s", domain, getCommunityShortName(), resourceId);
	    String urlRdf = resourceUrl + "?rdf";
	    String filePath = directoryPath + "basicOntologyResourceRdf.rdf";
	    
	    HttpURLConnection connection = null;
	    FileOutputStream fos = null;
	    BufferedReader reader = null;
	    
	    try {
	        // Create URL object
	        URL url = new URL(urlRdf);
	        connection = (HttpURLConnection) url.openConnection();
	        
	        // Set headers
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Referer", urlRdf);
	        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36 gnossspider");
	        connection.setRequestProperty("Authorization", "OAuth \n " + getSignForUrl(resourceUrl));	       
	        
	        // Check response code
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            // Download file
	            java.io.InputStream inputStream = connection.getInputStream();
	            fos = new FileOutputStream(filePath);
	            
	            byte[] buffer = new byte[4096];
	            int bytesRead;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                fos.write(buffer, 0, bytesRead);
	            }
	            fos.close();
	            inputStream.close();
	            
	            // Read file content
	            reader = new BufferedReader(
	                new InputStreamReader(
	                    new FileInputStream(filePath), 
	                    StandardCharsets.UTF_8
	                )
	            );
	            
	            StringBuilder content = new StringBuilder();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                content.append(line).append("\n");
	            }
	            rdf = content.toString();
	            reader.close();
	            
	            // Delete temporary file
	            File tempFile = new File(filePath);
	            if (tempFile.exists()) {
	                tempFile.delete();
	            }
	        } else {
	            if (this._logHelper != null) {
	                this._logHelper.Debug(String.format(
	                    "Error downloading file: %s. HTTP Response Code: %d", 
	                    urlRdf, responseCode
	                ));
	            }
	        }
	    } catch (Exception ex) {
	        if (this._logHelper != null) {
	            this._logHelper.Debug(String.format(
	                "Error downloading file: %s. Error: %s", 
	                urlRdf, ex.getMessage()
	            ));
	        }
	    } finally {
	        // Clean up resources
	        try {
	            if (reader != null) reader.close();
	            if (fos != null) fos.close();
	            if (connection != null) connection.disconnect();
	        } catch (IOException e) {
	            // Ignore cleanup errors
	        }
	    }
	    
	    return rdf;
	}

	public String getSignForUrl(String url) throws SignatureException, MalformedURLException, URISyntaxException
	{
		String sign = OAuthInfo.GetSignedUrl(url);
		return sign.replace("&", ",").replace(url+"?", "");
	}

	public void downloadFile(String URL, String fileName) throws IOException, GnossAPIException {

		URL url= new URL(URL);
		URLConnection urlCon=url.openConnection();
		InputStream is = urlCon.getInputStream();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			byte [] array = new byte[1000];
			int leido = is.read(array);
			while (leido > 0) {
				fos.write(array,0,leido);
				leido=is.read(array);
			}
		}
		catch (Exception e) {
			throw new GnossAPIException("Error while trying to download the file " + fileName);			
		}
		finally {
			if(is != null) {
				try {
					is.close();	
				}
				catch(IOException ex) {
				}				
			}
			
			if(fos != null) {
				try {
					fos.close();
				}
				catch(IOException ex) {					
				}
			}
		}
	}


	/**
	 * Get the community members email list
	 * @return ArrayList ArrayList
	 */
	public ArrayList<String> getCommunityMembersEmailList(){
		ArrayList<String> emails= new ArrayList<String>();
		HashMap<UUID, String> emailPerson =(HashMap<UUID, String>) getCommunityApiWrapper().getCommunityPersonIDEmail();

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
	public void setMainImageLoadedImage(UUID resourceId, String imageName, ArrayList<String> sizes) throws Exception {
		String sizeMask="[";
		for(String size : sizes) {
			sizeMask+=sizeMask +size;
		}
		sizeMask=sizeMask+"]";
		String image="";
		if(imageName != null && !imageName.isEmpty()) {
			image="[IMGPrincipal]"+sizeMask+imageName;
		}
		System.out.println("\"Image string, the first number will be the main one: "+image);
		setMainImage(resourceId, image);

		if(!image.isEmpty()) {
			_logHelper.Debug("Correct main image setting of resource "+resourceId+", of the new main image is  "+sizes.get(0)+"and itrs name is "+imageName);
		}
		else {
			_logHelper.Debug("The main image of the resources "+resourceId+ " has been deleted");
		}
	}
	
	// Métodos que faltan o necesitan actualización en ResourceApi.java

	/**
	 * Get an attached file from a semantic resource
	 * @param resource_id Identifier of the resource
	 * @param file_name Name of the file attached with extension
	 * @param language Only if the property is multilanguage. The language which we want the file. es, en, de, ca, eu, fr, gl, it, pt
	 * @return An byte array with the content of the file
	 */
	public byte[] getAttachedFileFromSemanticResource(UUID resource_id, String file_name, String language) {
	    byte[] attachedFile = null;
	    try {
	        String url = getApiUrl() + "/resource/get-attached-file-semantic-resource?resource_id=" + resource_id + 
	                     "&file_name=" + file_name + "&community_short_name=" + getCommunityShortName() + 
	                     "&language=" + language;
	        String response = WebRequest("GET", url);
	        Gson gson = new Gson();
	        attachedFile = gson.fromJson(response, byte[].class);
	    } catch(Exception ex) {
	        _logHelper.Error("Error getting the file " + file_name + " from the resource " + resource_id + 
	                        " in the community " + getCommunityShortName() + ". " + ex.getMessage());
	    }
	    return attachedFile;
	}

	/**
	 * Get an attached file from a semantic resource (without language parameter)
	 * @param resource_id Identifier of the resource
	 * @param file_name Name of the file attached with extension
	 * @return An byte array with the content of the file
	 */
	public byte[] getAttachedFileFromSemanticResource(UUID resource_id, String file_name) {
	    return getAttachedFileFromSemanticResource(resource_id, file_name, "");
	}

	/**
	 * Given a title and/or description, returns the extracted labels from EtiquetadoAutomatico
	 * @param title (Optional) Resource title
	 * @param description (Optional) Resource description
	 * @return List of strings with each of the tags returned
	 * @throws GnossAPIArgumentException if both parameters are empty
	 */
	public List<String> getAutomaticLabelingTags(String title, String description) throws GnossAPIArgumentException {
	    List<String> tagList = null;
	    
	    if(StringUtils.isEmpty(title) && StringUtils.isEmpty(description)) {
	        throw new GnossAPIArgumentException("Both parameters at GetAutomaticLabelingTags cannot be empty at the same time. At least one of them must have value.");
	    } else {
	        String url = getApiUrl() + "/resource/get-automatic-labeling";
	        TagsFromServiceModel model = new TagsFromServiceModel();
	        model.setTitle(title);
	        model.setDescription(description);
	        model.setCommunity_short_name(getCommunityShortName());
	        
	        try {
	            String response = WebRequestPostWithJsonObject(url, model);
	            String[] tags = response.split(",");
	            tagList = new ArrayList<>();
	            for(String tag : tags) {
	                if(!StringUtils.isEmpty(tag)) {
	                    tagList.add(tag.trim());
	                }
	            }
	        } catch(Exception ex) {
	            _logHelper.Error("Error getting automatic labeling tags: " + ex.getMessage());
	        }
	    }
	    
	    return tagList;
	}

	/**
	 * Given a title, returns the extracted labels from EtiquetadoAutomatico
	 * @param title Resource title
	 * @return List of strings with each of the tags returned
	 * @throws GnossAPIArgumentException if title is empty
	 */
	public List<String> getAutomaticLabelingTags(String title) throws GnossAPIArgumentException {
	    return getAutomaticLabelingTags(title, "");
	}
	

	/**
	 * FlushDb of resource cache
	 * @param pProyectoID Project ID
	 */
	public void deleteCacheResources(UUID pProyectoID) {
	    try {
	        String url = getApiUrl() + "/resource/delete-cache-resources?project_id=" + pProyectoID;
	        
	        WebRequest("POST", url, "application/json");
	    } catch(Exception ex) {
	        _logHelper.Error("Error deleting cache of resources", ex.getMessage());
	        throw new RuntimeException(ex);
	    }
	}

	/**
	 * Modify a categories resource
	 * @param pResourceID Resource identifier
	 * @param pCategoriesIDs Categories to modify
	 * @param pCommunityShortName Community short name
	 * @return True if modify correct
	 */
	public boolean modifyCategoriasRecursoInt(UUID pResourceID, List<UUID> pCategoriesIDs, String pCommunityShortName) {
	    boolean modified = false;
	    try {
	        String url = getApiUrl() + "/resource/chage-categories-resource";
	        
	        ModifyResourceCategories parameters = new ModifyResourceCategories();
	        parameters.setResource_id(pResourceID);
	        parameters.setCategories(pCategoriesIDs);
	        parameters.setCommunity_short_name(pCommunityShortName);
	        
	        String response = WebRequestPostWithJsonObject(url, parameters);
	        
	        if(!StringUtils.isEmpty(response)) {
	            Gson gson = new Gson();
	            modified = gson.fromJson(response, Boolean.class);
	            _logHelper.Debug("categories resource modified: " + pResourceID);
	        }
	    } catch(Exception ex) {
	        _logHelper.Error("Error modifying categories " + pResourceID + ".", ex.getMessage());
	        throw new RuntimeException(ex);
	    }
	    
	    return modified;
	}

	/**
	 * Method to modify the resource's subtype
	 * @param resourceId Resource identifier guid
	 * @param ontologyName The ontology name of the resource to modify
	 * @param subtype The subtype of the resource to modify
	 * @param previousType Previous type
	 * @param userId User that try to modify the resource
	 */
	public void modifySubtype(UUID resourceId, String ontologyName, String subtype, String previousType, UUID userId) {
	    ModifyResourceSubtype model = null;
	    try {
	        String url = getApiUrl() + "/resource/modify-subtype";
	        model = new ModifyResourceSubtype();
	        model.setResource_id(resourceId);
	        model.setCommunity_short_name(getCommunityShortName());
	        model.setOntology_name(ontologyName);
	        model.setSubtype(subtype);
	        model.setPrevious_type(previousType);
	        model.setUser_id(userId);
	        
	        WebRequestPostWithJsonObject(url, model);
	        
	        _logHelper.Debug("Ended resource subtype modification");
	    } catch(Exception ex) {
	        Gson gson = new Gson();
	        _logHelper.Error("Error modifying resource subtype. \r\n: Json: " + gson.toJson(model), ex.getMessage());
	        throw new RuntimeException(ex);
	    }
	}

	/**
	 * Method to modify the resource's subtype (without userId parameter)
	 * @param resourceId Resource identifier guid
	 * @param ontologyName The ontology name of the resource to modify
	 * @param subtype The subtype of the resource to modify
	 * @param previousType Previous type
	 */
	public void modifySubtype(UUID resourceId, String ontologyName, String subtype, String previousType) {
	    modifySubtype(resourceId, ontologyName, subtype, previousType, null);
	}
	
	/**
	 * Obtains the list of languages available to translate a resource
	 * @return List of language codes (ISO 639-1)
	 * @throws Exception 
	 */
	public List<String> getTranslationLanguages() throws Exception{
		try {
			 String url = getApiUrl() + "/resource/get-translation-languages";
			 String response = WebRequest("GET", url);
			 Gson jsonUtilities = new Gson();
			 Type type = new TypeToken<List<String>>(){}.getType();
			 List<String> languagesList = jsonUtilities.fromJson(response, type);
			 if(languagesList != null && languagesList.size() > 0) {
				 _logHelper.Debug("List of languages obtained");
			 }else {
				 _logHelper.Error("Error attempting to get the list of languages");
			 }
			 
			 return languagesList;
		} catch(Exception ex) {
			_logHelper.Error("Error attempting to get the list of languages", ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Initiates an asynchronous translation process of the resource to the selected languages from the original language
	 * @param resourceId Resource identifier
	 * @param originalLanguage Original language of the resource
	 * @param targetLanguages List of language codes in ISO 639-1 format that the resource will be translated to
	 * @return Identifier of the async translation progress
	 * @throws Exception
	 */
	public UUID translateResource(UUID resourceId, String originalLanguage, List<String> targetLanguages) throws Exception {
		try {
			String url = getApiUrl() + "/resource/translate-resource";
			TranslateResourceParams translateResourceParams = new TranslateResourceParams();
			translateResourceParams.setResource_id(resourceId);
			translateResourceParams.setOriginal_language(originalLanguage);
			translateResourceParams.setTarget_languages(targetLanguages);
			translateResourceParams.setCommunity_short_name(CommunityShortName);
			
			String response = WebRequestPostWithJsonObject(url, translateResourceParams).replaceAll("\"", "");
			UUID translationID = null;
			if(response != null){
				StringUtilities strUtil = new StringUtilities();
				response = strUtil.trimEnd(response, '"');
				translationID = UUID.fromString(response);
				_logHelper.Debug("The resource '"+resourceId+"' is being translated. Identifier '"+translationID+"' has been associated with the process to track the progress.");
			}else {
				_logHelper.Error("Error translating the resource '"+resourceId+"'.");
			}
			
			return translationID;
		} catch(Exception ex) {
			_logHelper.Error("Error translating the resource '"+resourceId+"'.", ex.getMessage());
			throw ex;
		}
	}
}
