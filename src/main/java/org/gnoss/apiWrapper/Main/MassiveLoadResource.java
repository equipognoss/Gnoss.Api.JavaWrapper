package org.gnoss.apiWrapper.Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;


import org.gnoss.apiWrapper.ApiModel.CloseMassiveDataLoadResource;
import org.gnoss.apiWrapper.ApiModel.MassiveDataLoadPackageResource;
import org.gnoss.apiWrapper.ApiModel.MassiveDataLoadResource;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.Interfaces.IGnossOCBase;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.gnoss.apiWrapper.models.OntologyCount;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class MassiveLoadResource extends ResourceApi{
	
	private ILogHelper _logHelper;
	/**
	 * Massive data load identifier
	 */
	private UUID massiveLoadId;
	/**
	 * Massive data load name
	 */
	private String loadName;
	/**
	 * Directory path of the files
	 */
	private String filesDirectory;
	/**
	 * Massive load identifier
	 */
	private UUID MassiveLoadIdentifier;
	/**
	 * Massive data load name
	 */
	private String LoadName;
	/**
	 * Directory path of the files
	 */
	private String FilesDirectory;
	/**
	 * Number of resources and files
	 */
	public Map<String, OntologyCount> counter= new HashMap<String, OntologyCount>();
	/**
	 * Virtual directory of data
	 */
	private String Uri;
	
	
	/**
	 * Constructor of MassiveLoadResourceApi
	 * @param oauth Oauth information to sign the Api request
	 * @param communityShortName Community short name which you want to use the API
	 * @param ontologyName Ontology name of the resources that you are going to query, upload or modify
	 * @param developerEmail If you want to be informed of any incident that may happends during a large load of resources, an email will be sent to this email address
	 */
	public MassiveLoadResource(OAuthInfo oauth, String communityShortName, String ontologyName, String developerEmail) {
		super(oauth, communityShortName, ontologyName, developerEmail);
		this._logHelper=LogHelper.getInstance();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor of MassiveLoadResourceApi
	 * @param oauth oauth
	 * @param configFilePath Configuration file path, with a structure like http://api.gnoss.com/v3/exampleConfig.txt 
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws ParserConfigurationException PArser Configuration Exception
	 * @throws SAXException SAX Exception 
	 * @throws IOException IO Exception 
	 */
	public MassiveLoadResource(OAuthInfo oauth, String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper=LogHelper.getInstance();
	}
	
	/**
	 * Create a new massive data load
	 * @param pName Massive data load name
	 * @param pFilesDirectory  Path directory of the massive data load files
	 * @param pOrganizationID  Organization identifier
	 * @return Identifier of the load
	 * @throws GnossAPIException Gnoss API Exception 
	 */
	public UUID MassiveDataLoad(String pName, String pFilesDirectory, UUID pOrganizationID) throws GnossAPIException {
		this.MassiveLoadIdentifier= UUID.randomUUID();
		try {
			this.FilesDirectory=pFilesDirectory;
			this.Uri=pName;
			CreateMassiveDataLoad(pOrganizationID);
			
			this._logHelper.Debug("Massive data load create with the idetifier" +this.MassiveLoadIdentifier);
		}catch(Exception ex) {
			this._logHelper.Error("Error creating the massive data load" +this.MassiveLoadIdentifier+ ex.getMessage());
			throw new GnossAPIException("Error creating the massive data load"+ this.MassiveLoadIdentifier+ ex.getMessage() );
		}
		return MassiveLoadIdentifier;
		
	}

	

	public boolean CreateMassiveDataLoad(UUID organizationID) {
		boolean created=false;
		MassiveDataLoadResource model=null;
		if(organizationID==null) {
			organizationID=UUID.fromString("11111111-1111-1111-1111-111111111111");
		}
		try {
			String url=getApiUrl()+"/resource/create-massive-load";
			model= new MassiveDataLoadResource();
			{
				model.setLoad_id(getMassiveLoadIdentifier());
				model.setName(getLoadName());
				model.setCommunity_name(getCommunityShortName());
				model.setOrganization_id(organizationID);
			}
			WebRequestPostWithJsonObject(url, model);
			created=true;
			this._logHelper.Debug("Massive data load created");
			
		}catch(Exception ex) {
			 Gson jsonUtilities = new Gson();
		        String json = jsonUtilities.toJson(model);
			this._logHelper.Error("Error creating massive data load" +this.MassiveLoadIdentifier+". \r\n " +json+ " "+ ex.getMessage());
		}
		return created;
		
		
	}
	
	public UUID CreateLastMassiveDataLoadPackage(IGnossOCBase resource) {
		UUID identifier= UUID.randomUUID();
		
		try {
			if(counter.containsKey(super.getOntologyNameWithoutExtension())) {
				counter.put(super.getOntologyNameWithoutExtension(), new OntologyCount(0,0));
			}
			List<String> ontologyTriples=resource.ToOntologyGnossTriples(this);
			List<String> searchTriples= resource.ToSearchGraphTriples(this);
			Map<UUID, String[]> acidData= resource.ToAcidData(this);
			
			//REVISAR!!!!!!!!!!!
			String pathOntology=getFilesDirectory()+"\\"+getOntologyNameWithoutExtension()+ "_"+getMassiveLoadIdentifier()+ "_"+counter.get(getOntologyNameWithoutExtension().toString()).getFileCount()+".nq";
			String pathSearch=getFilesDirectory()+"\\"+getOntologyNameWithoutExtension()+ "_search_"+getMassiveLoadIdentifier()+ "_"+counter.get(getOntologyNameWithoutExtension().toString()).getFileCount()+".nq";
			String pathAcid=getFilesDirectory()+"\\"+getOntologyNameWithoutExtension()+ "_acid_"+getMassiveLoadIdentifier()+ "_"+counter.get(getOntologyNameWithoutExtension().toString()).getFileCount()+".txt";
			
			File filePathOntology= new File(pathOntology);
			File filePathSearch= new File(pathSearch);
			File filePathAcid= new File(pathAcid);
			
			FileWriter fileWriter = new FileWriter (filePathOntology);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(ontologyTriples.toString());
			
			FileWriter fileWriter2 = new FileWriter (filePathSearch);
			BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
			bufferedWriter2.write(searchTriples.toString());
			
			FileWriter fileWriter3 = new FileWriter (filePathAcid);
			BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter2);
			bufferedWriter3.write(acidData.keySet()+ "|||" +acidData.values());
			
			if(counter.get(getOntologyNameWithoutExtension().toString()).getResourcesCount()>999) {
				MassiveDataLoadPackageResource model= new MassiveDataLoadPackageResource();
				model.setPackage_id(identifier);
				model.setLoad_id(MassiveLoadIdentifier);
				model.setOntology_rute(pathOntology);
				model.setSearch_rute(pathSearch);
				model.setSql_rute(pathAcid);
				model.setOntology(getOntologyUrl());
				model.setLast(true);
				
				CreatePackageMassiveDataLoad(model);
				this._logHelper.Debug("Package massive data load create with the identifier" +identifier);
				
				counter.get(getOntologyNameWithoutExtension()).setResourcesCount(0);
				int c=counter.get(getOntologyNameWithoutExtension()).getFileCount()+1;
				counter.get(getOntologyNameWithoutExtension()).setFileCount(c);
				
			}else {
				int c=counter.get(getOntologyNameWithoutExtension()).getResourcesCount()+1;
				counter.get(getOntologyNameWithoutExtension()).setResourcesCount(c);
			}
			return identifier;
		}catch(Exception ex) {
			this._logHelper.Error("Error creating the package of massive data load"+identifier + ex.getMessage());
			return UUID.fromString("");
		}	
	}

	public boolean CreatePackageMassiveDataLoad(MassiveDataLoadPackageResource model) {
		boolean created=false;
		try {
			String url=getApiUrl()+"/resource/create-massive-load-package";
			WebRequestPostWithJsonObject(url, model);
			created=true;
			this._logHelper.Debug("Massive data load package created");
		}catch(Exception ex) {
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
			this._logHelper.Error("Error creating massive data load package"+model.getPackage_id()+". \r\n Json:" +json+ model+ ", "+ex.getMessage());
			
		}
		return created;	
	}

	public boolean CloseDataLoad(UUID identifier) {
		return CloseMassiveDataLoad(identifier);
	}
	public boolean CloseMassiveDataLoad(UUID identifier) {
		String url=getApiUrl()+"/resource/close-massive-load";
		CloseMassiveDataLoadResource model=null;
		boolean closed= false;
		try {
			model= new CloseMassiveDataLoadResource();
			{
				model.setDataLoadIdentifier(identifier);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Data load is closed");
			closed=true;
		}catch(Exception ex) {
			Gson jsonUtilities = new Gson();
	        String json = jsonUtilities.toJson(model);
			this._logHelper.Error("Error closing the data load"+identifier+ ". \r\n json:" +json +"," + ex.getMessage());
			
		}
		return closed;
	}
	/**
	 * Create a new package massive data load
	 * @param resource  Interface of the Gnoss Methods
	 * @param isLast  The last resource
	 * @return  Identifier of the package
	 */
	public UUID MassiveDataLoadPackage(IGnossOCBase resource, boolean isLast) {
		UUID identifier = UUID.randomUUID();
		try {
			if(!counter.containsKey(getOntologyNameWithoutExtension())) {
				counter.put(getOntologyNameWithoutExtension(), new OntologyCount(0,0));	
			}
			List<String> ontologyTriples= resource.ToOntologyGnossTriples(this);
			List<String> searchTriples= resource.ToSearchGraphTriples(this);
			Map<UUID, String[]> acidData=resource.ToAcidData(this);
			
			String pathOntology= getFilesDirectory()+"\\"+getOntologyNameWithoutExtension()+"_"+getMassiveLoadIdentifier()+"_"+counter.get(getOntologyNameWithoutExtension()).getFileCount()+".nq";
			String uriOntology=getUri()+"/"+getOntologyNameWithoutExtension()+"_"+getMassiveLoadIdentifier()+"_"+counter.get(getOntologyNameWithoutExtension()).getFileCount()+".nq";
			
			String pathSearch=getFilesDirectory()+"\\"+getOntologyNameWithoutExtension()+"_search_"+getMassiveLoadIdentifier()+"_"+counter.get(getOntologyNameWithoutExtension()).getFileCount()+".nq";
			String uriSearch=getUri()+"/"+getOntologyNameWithoutExtension()+"_search_"+getMassiveLoadIdentifier()+"_"+counter.get(getOntologyNameWithoutExtension()).getFileCount()+".nq";
			
			String pathAcid=getFilesDirectory()+"\\"+getOntologyNameWithoutExtension()+"_acid_"+getMassiveLoadIdentifier()+"_"+counter.get(getOntologyNameWithoutExtension()).getFileCount()+".nq";
			String uriAcid=getUri()+"/"+getOntologyNameWithoutExtension()+"_acid_"+getMassiveLoadIdentifier()+"_"+counter.get(getOntologyNameWithoutExtension()).getFileCount()+".nq";
		
			File fileOntology= new File(pathOntology);
			File fileSearchh= new File(pathSearch);
			File fileAcid= new File(pathAcid);
			
			FileWriter fileWriter = new FileWriter (fileOntology);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(ontologyTriples.toString());
			
			FileWriter fileWriter2 = new FileWriter (fileSearchh);
			BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter2);
			bufferedWriter2.write(searchTriples.toString());
			
			FileWriter fileWriter3 = new FileWriter (fileAcid);
			BufferedWriter bufferedWriter3 = new BufferedWriter(fileWriter2);
			bufferedWriter3.write(acidData.keySet()+ "|||" +acidData.values());
			
			
			if(counter.get(getOntologyNameWithoutExtension().toString()).getResourcesCount()>999) {
				MassiveDataLoadPackageResource model= new MassiveDataLoadPackageResource();
				model.setPackage_id(identifier);
				model.setLoad_id(MassiveLoadIdentifier);
				model.setOntology_rute(pathOntology);
				model.setSearch_rute(pathSearch);
				model.setSql_rute(pathAcid);
				model.setOntology(getOntologyUrl());
				model.setLast(true);
				
				CreatePackageMassiveDataLoad(model);
				this._logHelper.Debug("Package massive data load create with the identifier" +identifier);
				
				counter.get(getOntologyNameWithoutExtension()).setResourcesCount(0);
				
				int c=counter.get(getOntologyNameWithoutExtension()).getFileCount()+1;
				counter.get(getOntologyNameWithoutExtension()).setFileCount(c);
			}
			else {
				int c=counter.get(getOntologyNameWithoutExtension()).getResourcesCount()+1;
				counter.get(getOntologyNameWithoutExtension()).setResourcesCount(c);
			}
			return identifier;
			
		}catch(Exception ex) {
			this._logHelper.Error("Error creating the package of massive data load"+identifier + ex.getMessage());
			return UUID.fromString("");
		}	
	}

	public String getLoadName() {
		return LoadName;
	}



	public void setLoadName(String loadName) {
		this.LoadName = loadName;
	}



	public UUID getMassiveLoadIdentifier() {
		return MassiveLoadIdentifier;
	}



	public void setMassiveLoadIdentifier(UUID massiveLoadIdentifier) {
		MassiveLoadIdentifier = massiveLoadIdentifier;
	}



	public String getFilesDirectory() {
		return FilesDirectory;
	}



	public void setFilesDirectory(String filesDirectory) {
		FilesDirectory = filesDirectory;
	}



	public String getUri() {
		return Uri;
	}



	public void setUri(String uri) {
		Uri = uri;
	}
	
	

}
