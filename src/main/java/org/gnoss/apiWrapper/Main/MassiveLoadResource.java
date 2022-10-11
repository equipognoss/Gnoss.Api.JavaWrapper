package org.gnoss.apiWrapper.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.gnoss.apiWrapper.models.EstadoCargaModel;
import org.gnoss.apiWrapper.models.OntologyCount;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class MassiveLoadResource extends ResourceApi {

	private ILogHelper _logHelper;
	
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
	 * Number of resources and files
	 */
	public Map<String, OntologyCount> counter = new HashMap<String, OntologyCount>();
	
	/**
	 * Virtual directory of data
	 */
	private String Uri;

	/**
	 * Max num of resources per packages
	 */
	private int maxResourcePerPackage;

	private OutputStreamWriter streamData;
	private OutputStreamWriter streamOntology;
	private OutputStreamWriter streamSearch;

	private boolean isDebugMode;
	private final static int DEBUG_PACKAGE_SIZE = 5;
	private boolean onlyPrepareMassiveLoad;

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth                 Oauth information to sign the Api request
	 * @param communityShortName    Community short name which you want to use the
	 *                              API
	 * @param isDebugMode           Only for debugging
	 * @param maxResourcePerPackage Num max of resources per package
	 * @param ontologyName          (Optional) Ontology name of the resources that
	 *                              you are going to query, upload or modify
	 * @param developerEmail        (Optional) If you want to be informed of any
	 *                              incident that may happends during a large load
	 *                              of resources, an email will be sent to this
	 *                              email address
	 */
	public MassiveLoadResource(OAuthInfo oauth, String communityShortName, boolean isDebugMode,
			int maxResourcePerPackage, String ontologyName, String developerEmail) {
		super(oauth, communityShortName, ontologyName, developerEmail);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcePerPackage = maxResourcePerPackage;
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth                 Oauth information to sign the Api request
	 * @param communityShortName    Community short name which you want to use the
	 *                              API
	 * @param isDebugMode           Only for debugging
	 * @param maxResourcePerPackage Num max of resources per package
	 * @param ontologyName          (Optional) Ontology name of the resources that
	 *                              you are going to query, upload or modify
	 */
	public MassiveLoadResource(OAuthInfo oauth, String communityShortName, boolean isDebugMode,
			int maxResourcePerPackage, String ontologyName) {
		super(oauth, communityShortName, ontologyName);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcePerPackage = maxResourcePerPackage;
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth                 Oauth information to sign the Api request
	 * @param communityShortName    Community short name which you want to use the
	 *                              API
	 * @param isDebugMode           Only for debugging
	 * @param maxResourcePerPackage Num max of resources per package
	 */
	public MassiveLoadResource(OAuthInfo oauth, String communityShortName, boolean isDebugMode,
			int maxResourcePerPackage) {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcePerPackage = maxResourcePerPackage;
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth              Oauth information to sign the Api request
	 * @param communityShortName Community short name which you want to use the API
	 * @param isDebugMode        Only for debugging
	 */
	public MassiveLoadResource(OAuthInfo oauth, String communityShortName, boolean isDebugMode) {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth              Oauth information to sign the Api request
	 * @param communityShortName Community short name which you want to use the API
	 * @throws GnossAPIException            Exception controlled by the Api
	 * @throws ParserConfigurationException Exception given at try convert dates
	 * @throws SAXException                 SaxException
	 * @throws IOException                  IOException
	 */
	public MassiveLoadResource(OAuthInfo oauth, String communityShortName)
			throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
	}

	// MÉTODOS

	/**
	 * Create a new massive data load
	 * 
	 * @param pName                   Massive data load name
	 * @param pFilesDirectory         Path directory of the massive data load files
	 * @param pUrl                    Url where the file directory should be
	 *                                resopinding
	 * @param pOnlyPrepareMassiveLoad True if the massive data load should not be
	 *                                uploaded
	 * @return Identifier of the load
	 * @throws GnossAPIException Exception controlled by the Api
	 */
	public UUID CreateMassiveDataLoad(String pName, String pFilesDirectory, String pUrl,
			boolean pOnlyPrepareMassiveLoad) throws GnossAPIException {
		try {
			if (pOnlyPrepareMassiveLoad && isDebugMode) {
				throw new Exception(
						"MassiveDataLoad can not be prepared when debug mode is activated. Please turn off debug mode and try again");
			}

			this.onlyPrepareMassiveLoad = pOnlyPrepareMassiveLoad;
			filesDirectory = pFilesDirectory;
			Uri = pUrl;
			loadName = pName;
			MassiveLoadIdentifier = UUID.randomUUID();

			CreateMassiveDataLoad();

			LogHelper.getInstance()
					.Debug("Massive data load create with the identifier ".concat(MassiveLoadIdentifier.toString()));
			return MassiveLoadIdentifier;
		} catch (Exception ex) {
			LogHelper.getInstance().Error("Error creating massive data load.\n".concat(MassiveLoadIdentifier.toString())
					.concat(ex.getMessage()));
			throw new GnossAPIException("Error creating massive data load.\n".concat(MassiveLoadIdentifier.toString())
					.concat(ex.getMessage()));
		}
	}

	/**
	 * Create a new massive data load
	 * 
	 * @param pName           Massive data load name
	 * @param pFilesDirectory Path directory of the massive data load files
	 * @param pUrl            Url where the file directory should be resopinding
	 * @return Identifier of the load
	 * @throws GnossAPIException Exception controlled by the Api
	 */
	public UUID CreateMassiveDataLoad(String pName, String pFilesDirectory, String pUrl) throws GnossAPIException {
		return CreateMassiveDataLoad(pName, pFilesDirectory, pUrl, false);
	}

	/**
	 * Uploads an existing massive data load
	 * 
	 * @param pMassiveLoadIdentifier Massive data load identifier
	 * @throws Exception Exception
	 */
	public void UploadPrepareMassiveLoad(UUID pMassiveLoadIdentifier) throws Exception {
		File directory = new File(filesDirectory);
		if (directory.exists()) {
			String[] fileList = directory.list();
			String fileName = "";
			String searchedFileName = getOntologyNameWithoutExtension().concat("_acid_")
					.concat(pMassiveLoadIdentifier.toString());
			for (String file : fileList) {
				if (file.contains(searchedFileName)) {
					fileName = file;
					break;
				}
			}

			int length = fileName.length();
			counter.put(getOntologyNameWithoutExtension(), new OntologyCount(0, 0));

			if (length == 0) {
				throw new Exception("The massive load doesn't exists.");
			}

			for (int i = 0; i < length; i++) {
				SendPackage(pMassiveLoadIdentifier);
				int count = counter.get(getOntologyNameWithoutExtension()).getFileCount();
				counter.get(getOntologyNameWithoutExtension()).setFileCount(++count);
			}
		} else {
			throw new Exception("The massive load directory doesn't exists.");
		}
	}

	/**
	 * Create a new package massive data load
	 * 
	 * @param resource Interface of the Gnoss Methods
	 * @throws IOException 
	 */
	public void AddResourceToPackage(IGnossOCBase resource) throws IOException {
	
		try {
			if (!counter.keySet().contains(getOntologyNameWithoutExtension())) {
				counter.put(getOntologyNameWithoutExtension(), new OntologyCount(0, 0));
			}

			ArrayList<String> ontologyTriples = resource.ToOntologyGnossTriples(this);
			ArrayList<String> searchTriples = resource.ToSearchGraphTriples(this);
			HashMap<UUID, String> acidData = resource.ToAcidData(this);

			String pathOntology = filesDirectory.concat(File.separator).concat(getOntologyNameWithoutExtension())
					.concat("_").concat(MassiveLoadIdentifier.toString()).concat("_")
					.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".nq");
			String pathSearch = filesDirectory.concat(File.separator).concat(getOntologyNameWithoutExtension())
					.concat("_search_").concat(MassiveLoadIdentifier.toString()).concat("_")
					.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".nq");
			String pathAcid = filesDirectory.concat(File.separator).concat(getOntologyNameWithoutExtension())
					.concat("_acid_").concat(MassiveLoadIdentifier.toString()).concat("_")
					.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".txt");

			if (streamData == null || streamOntology == null || streamSearch == null) {
				streamData = new OutputStreamWriter(new FileOutputStream(pathAcid), Charset.forName("UTF-8"));
				streamOntology = new OutputStreamWriter(new FileOutputStream(pathOntology), Charset.forName("UTF-8"));
				streamSearch = new OutputStreamWriter(new FileOutputStream(pathSearch), Charset.forName("UTF-8"));				
			}		
			for (String triple : ontologyTriples) {
				streamOntology.append(triple.concat("\r\n"));
			}
			for (String triple : searchTriples) {
				streamSearch.append(triple.concat("\r\n"));
			}	
			for (UUID clave : acidData.keySet()) {
				streamData.append(clave.toString().concat("|||").concat(acidData.get(clave)).concat("\r\n"));
			}

			if (counter.get(getOntologyNameWithoutExtension()).getResourcesCount() >= maxResourcePerPackage
					|| (isDebugMode && counter.get(getOntologyNameWithoutExtension())
							.getResourcesCount() >= DEBUG_PACKAGE_SIZE)) {
				if (isDebugMode) {
					this._logHelper.Warn(
							"DebugMode On, use it only for testing purpose. Please turn DebugMode off as soon as posible.");
				}

				SendPackage();

				counter.get(getOntologyNameWithoutExtension()).setResourcesCount(0);
				int count = counter.get(getOntologyNameWithoutExtension()).getFileCount();
				counter.get(getOntologyNameWithoutExtension()).setFileCount(++count);
			} else {
				int count = counter.get(getOntologyNameWithoutExtension()).getResourcesCount();
				counter.get(getOntologyNameWithoutExtension()).setResourcesCount(++count);
			}
		} catch (Exception ex) {
			LogHelper.getInstance()
					.Error("Error creating the package of massive data load. \n".concat(ex.getMessage()));
		}		
	}

	/**
	 * Close a massive data load
	 * 
	 * @return True if the data load is closed
	 * @throws Exception Exception
	 */
	public boolean CloseMassiveDataLoad() throws Exception {
		String url = getApiUrl().concat("/resource/close-massive-load");
		CloseMassiveDataLoadResource model = null;
		boolean closed = false;
		try {
			SendPackage();

			model = new CloseMassiveDataLoadResource();
			model.setDataLoadIdentifier(MassiveLoadIdentifier);

			WebRequestPostWithJsonObject(url, model);
			LogHelper.getInstance().Debug("Data load is closed");
			closed = true;
		} catch (Exception ex) {
			Gson gson = new Gson();
			LogHelper.getInstance().Error("Error closing the data load".concat(MassiveLoadIdentifier.toString())
					.concat(". \r\n Json: ").concat(gson.toJson(model)), ex.getMessage());
			throw ex;
		}
		return closed;
	}

	/**
	 * Create the massive data load
	 * 
	 * @return True if the load is correctly created
	 * @throws Exception Exception
	 */
	private boolean CreateMassiveDataLoad() throws Exception {
		boolean created = false;
		MassiveDataLoadResource model = null;

		try {
			String url = getApiUrl().concat("/resource/create-massive-load");

			model = new MassiveDataLoadResource();
			model.setLoad_id(MassiveLoadIdentifier);
			model.setName(loadName);
			model.setCommunity_name(getCommunityShortName());

			WebRequestPostWithJsonObject(url, model);
			created = true;
			LogHelper.getInstance().Debug("Massive data load created");
		} catch (Exception ex) {
			Gson gson = new Gson();
			LogHelper.getInstance().Error("Error creating massive data load".concat(MassiveLoadIdentifier.toString())
					.concat(". \r\n Json: ").concat(gson.toJson(model)), ex.getMessage());
			throw ex;
		}
		return created;
	}

	/**
	 * Send package with the number of elements indicated in the constructor
	 * 
	 * @param pMassiveLoadIdentifier (Optional) Massive data load identifier
	 */
	private void SendPackage(UUID pMassiveLoadIdentifier) {
		try {
			if (onlyPrepareMassiveLoad) {
				return;
			}

			UUID massiveLoadFilesIdentifier = MassiveLoadIdentifier;
			if (pMassiveLoadIdentifier != null) {
				massiveLoadFilesIdentifier = pMassiveLoadIdentifier;
			}

			String uriOntology = getUri().concat("/").concat(getOntologyNameWithoutExtension()).concat("_")
					.concat(massiveLoadFilesIdentifier.toString()).concat("_")
					.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".nq");
			String uriSearch = getUri().concat("/").concat(getOntologyNameWithoutExtension()).concat("_search_")
					.concat(massiveLoadFilesIdentifier.toString()).concat("_")
					.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".nq");
			String uriAcid = getUri().concat("/").concat(getOntologyNameWithoutExtension()).concat("_acid_")
					.concat(massiveLoadFilesIdentifier.toString()).concat("_")
					.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".txt");

			MassiveDataLoadPackageResource model = new MassiveDataLoadPackageResource();
			model.setPackage_id(UUID.randomUUID());
			model.setLoad_id(MassiveLoadIdentifier);

			// Si es modo debug, enviamos los bytes de los ficheros directamente
			if (isDebugMode) {
				CloseStreams();

				File ontologyFile = new File(filesDirectory.concat(File.separator).concat(getOntologyNameWithoutExtension())
						.concat("_").concat(massiveLoadFilesIdentifier.toString()).concat("_")
						.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".nq"));
				File searchFile = new File(filesDirectory.concat(File.separator).concat(getOntologyNameWithoutExtension())
						.concat("_search_").concat(massiveLoadFilesIdentifier.toString()).concat("_")
						.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".nq"));
				File acidFile = new File(filesDirectory.concat(File.separator).concat(getOntologyNameWithoutExtension())
						.concat("_acid_").concat(massiveLoadFilesIdentifier.toString()).concat("_")
						.concat(counter.get(getOntologyNameWithoutExtension()).getFileCount() + "").concat(".txt"));

				model.setOntology_byte(Files.readAllBytes(ontologyFile.toPath()));
				model.setSearch_bytes(Files.readAllBytes(searchFile.toPath()));
				model.setSql_bytes(Files.readAllBytes(acidFile.toPath()));
			}

			model.setOntology_rute(uriOntology);
			model.setSearch_rute(uriSearch);
			model.setSql_rute(uriAcid);
			model.setOntology(getOntologyUrl());
			model.setLast(false);

			CreatePackageMassiveDataLoad(model);

			if (!isDebugMode) {
				CloseStreams();
			}

			LogHelper.getInstance().Debug("Package massive data load create with the identifier"
					.concat(getMassiveLoadIdentifier().toString()));
		} catch (Exception ex) {
			LogHelper.getInstance().Error("Error creating the package of massive data load ".concat(ex.getMessage()));
		}
	}

	/**
	 * Send package with the number of elements indicated in the constructor
	 */
	private void SendPackage() {
		SendPackage(null);
	}

	/**
	 * Close the streams to prepare new package
	 * 
	 * @throws IOException IOException
	 */
	private void CloseStreams() throws IOException {
		if (streamData != null) {
			streamData.flush();
			streamData.close();
			streamData = null;
		}

		if (streamOntology != null) {
			streamOntology.flush();
			streamOntology.close();
			streamOntology = null;
		}

		if (streamSearch != null) {
			streamSearch.flush();
			streamSearch.close();
			streamSearch = null;
		}
	}

	/**
	 * Creates a new package in a massive data load
	 * 
	 * @param model Data model of the package massive data load
	 * @return True if the new massive data load package was created succesfully
	 * @throws Exception Exception
	 */
	private boolean CreatePackageMassiveDataLoad(MassiveDataLoadPackageResource model) throws Exception {
		boolean created = false;
		try {
			String url = getApiUrl().concat("/resource/create-massive-load-package");
			WebRequestPostWithJsonObject(url, model);
			created = true;
			LogHelper.getInstance().Debug("Massive data load package created");
		} catch (Exception ex) {
			Gson gson = new Gson();
			LogHelper.getInstance().Error("Error creating massive data load package "
					.concat(model.getPackage_id().toString()).concat(". \r\n Json: ").concat(gson.toJson(model)),
					ex.getMessage());
			throw ex;
		}
		return created;
	}

	/**
	 * Return actual load state
	 * 
	 * @param pLoadId Load id
	 * @return Actual load state
	 * @throws Exception Exception
	 */
	public EstadoCargaModel LoadState(UUID pLoadId) throws Exception {
		EstadoCargaModel estadoCarga;
		try {
			String url = getApiUrl().concat("/resource/load-state");
			String response = WebRequestPostWithJsonObject(url, pLoadId);

			Gson gson = new Gson();
			estadoCarga = gson.fromJson(response, EstadoCargaModel.class);
		} catch (Exception ex) {
			throw ex;
		}

		return estadoCarga;
	}

	/**
	 * Name of the ontology to load
	 * 
	 * @return LoadName Name of the ontology to load
	 */
	public String getLoadName() {
		return loadName;
	}

	/**
	 * Set the ontology name to load
	 * 
	 * @param loadName New ontology's name
	 */
	public void setLoadName(String loadName) {
		this.loadName = loadName;
	}

	/**
	 * Return massive load identifier
	 * 
	 * @return Massive load identifier
	 */
	public UUID getMassiveLoadIdentifier() {
		return MassiveLoadIdentifier;
	}

	/**
	 * Return path where the files will be
	 * 
	 * @return Pathe where the files will be
	 */
	public String getFilesDirectory() {
		return filesDirectory;
	}

	/**
	 * Set the path where the files will be
	 * 
	 * @param filesDirectory New path where the files will be
	 */
	public void setFilesDirectory(String filesDirectory) {
		this.filesDirectory = filesDirectory;
	}

	/**
	 * Url where the massiveDataLoad api will download the files
	 * 
	 * @return Url where the massiveDataLoad api will download the files
	 */
	public String getUri() {
		return Uri;
	}

	/**
	 * Set the url where the massive data load will be downloaded
	 * 
	 * @param uri New url where the massiveDataLoad api will download the files
	 */
	public void setUri(String uri) {
		Uri = uri;
	}
}
