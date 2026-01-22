package org.gnoss.apiWrapper.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.CloseMassiveDataLoadResource;
import org.gnoss.apiWrapper.ApiModel.LoadResourceParams;
import org.gnoss.apiWrapper.ApiModel.MassiveDataLoadPackageResource;
import org.gnoss.apiWrapper.ApiModel.MassiveDataLoadResource;
import org.gnoss.apiWrapper.ApiModel.MassiveDataLoadTestResource;
import org.gnoss.apiWrapper.ApiModel.MassiveResourceLoad;
import org.gnoss.apiWrapper.Excepciones.GnossAPICategoryException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.Interfaces.IGnossOCBase;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.gnoss.apiWrapper.models.ComplexOntologyResource;
import org.gnoss.apiWrapper.models.EstadoCargaModel;
import org.gnoss.apiWrapper.models.OntologyCount;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

/**
 * Resource Api for Massive Data Load Compatible with Java 25 and Maven
 * 
 * @author Andrea
 */
public class MassiveLoadResourceApi extends ResourceApi {

	private ILogHelper _logHelper;

	/**
	 * Massive load identifier
	 */
	private UUID massiveLoadIdentifier;

	/**
	 * Massive data load name
	 */
	private String loadName;

	/**
	 * Directory path of the files
	 */
	private String filesDirectory;

	/**
	 * Number of resources and files
	 */
	public Map<String, OntologyCount> counter = new HashMap<>();

	/**
	 * Virtual directory of data
	 */
	private String uri;

	/**
	 * Debug mode flag
	 */
	private boolean isDebugMode;

	/**
	 * Max num of resources per packages
	 */
	private int maxResourcesPerPackage;

	private OutputStreamWriter streamData;
	private OutputStreamWriter streamOntology;
	private OutputStreamWriter streamSearch;

	private static final int DEBUG_PACKAGE_SIZE = 10;
	private static final int MAX_RESOURCE_PER_PACKAGE_STANDARD_SIZE = 50;
	private boolean onlyPrepareMassiveLoad;

	public MassiveLoadResourceApi(String configFilePath)
			throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper = LogHelper.getInstance();
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth                 OAuth information to sign the Api request
	 * @param communityShortName    Community short name which you want to use the
	 *                              API
	 * @param isDebugMode           Only for debugging
	 * @param maxResourcePerPackage Num max of resources per package
	 * @param ontologyName          (Optional) Ontology name of the resources that
	 *                              you are going to query, upload or modify
	 * @param developerEmail        (Optional) If you want to be informed of any
	 *                              incident that may happens during a large load of
	 *                              resources, an email will be sent to this email
	 *                              address
	 */
	public MassiveLoadResourceApi(OAuthInfo oauth, String communityShortName, boolean isDebugMode,
			int maxResourcePerPackage, String ontologyName, String developerEmail) {
		super(oauth, communityShortName, ontologyName, developerEmail);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcesPerPackage = maxResourcePerPackage;
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth                 OAuth information to sign the Api request
	 * @param communityShortName    Community short name which you want to use the
	 *                              API
	 * @param isDebugMode           Only for debugging
	 * @param maxResourcePerPackage Num max of resources per package
	 * @param ontologyName          (Optional) Ontology name of the resources that
	 *                              you are going to query, upload or modify
	 */
	public MassiveLoadResourceApi(OAuthInfo oauth, String communityShortName, boolean isDebugMode,
			int maxResourcePerPackage, String ontologyName) {
		super(oauth, communityShortName, ontologyName);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcesPerPackage = maxResourcePerPackage;
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth                 OAuth information to sign the Api request
	 * @param communityShortName    Community short name which you want to use the
	 *                              API
	 * @param isDebugMode           Only for debugging
	 * @param maxResourcePerPackage Num max of resources per package
	 */
	public MassiveLoadResourceApi(OAuthInfo oauth, String communityShortName, boolean isDebugMode,
			int maxResourcePerPackage) {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcesPerPackage = maxResourcePerPackage;
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth              OAuth information to sign the Api request
	 * @param communityShortName Community short name which you want to use the API
	 * @param isDebugMode        Only for debugging
	 */
	public MassiveLoadResourceApi(OAuthInfo oauth, String communityShortName, boolean isDebugMode) {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
		this.isDebugMode = isDebugMode;
		this.maxResourcesPerPackage = MAX_RESOURCE_PER_PACKAGE_STANDARD_SIZE;
	}

	/**
	 * Constructor of MassiveLoadResourceApi
	 * 
	 * @param oauth              OAuth information to sign the Api request
	 * @param communityShortName Community short name which you want to use the API
	 * @throws GnossAPIException            Exception controlled by the Api
	 * @throws ParserConfigurationException Exception given at try convert dates
	 * @throws SAXException                 SaxException
	 * @throws IOException                  IOException
	 */
	public MassiveLoadResourceApi(OAuthInfo oauth, String communityShortName)
			throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
		this.maxResourcesPerPackage = MAX_RESOURCE_PER_PACKAGE_STANDARD_SIZE;
	}
	
	/**
	 * Create a new massive data load
	 * 
	 * @param pName                   Massive data load name
	 * @param pFilesDirectory         Path directory of the massive data load files
	 * @param pUrl                    Url where the file directory should be
	 *                                responding
	 * @param pOnlyPrepareMassiveLoad True if the massive data load should not be
	 *                                uploaded
	 * @return Identifier of the load
	 * @throws GnossAPIException Exception controlled by the Api
	 */
	public UUID createMassiveDataLoad(String pName, String pFilesDirectory, String pUrl,
			boolean pOnlyPrepareMassiveLoad) throws GnossAPIException {
		try {
			if (pOnlyPrepareMassiveLoad && isDebugMode) {
				throw new Exception(
						"MassiveDataLoad can not be prepared when debugMode is activated. Please turn off debugMode and try again.");
			}

			this.onlyPrepareMassiveLoad = pOnlyPrepareMassiveLoad;
			this.filesDirectory = pFilesDirectory;
			this.uri = pUrl;
			this.loadName = pName;
			this.massiveLoadIdentifier = UUID.randomUUID();

			createMassiveDataLoadInternal();

			LogHelper.getInstance()
					.Debug("Massive data load create with the identifier " + massiveLoadIdentifier.toString());
			return massiveLoadIdentifier;
		} catch (Exception ex) {
			LogHelper.getInstance().Error("Error creating the massive data load " + massiveLoadIdentifier
					+ ex.getMessage() + " " + getStackTraceAsString(ex));
			throw new GnossAPIException("Error creating the massive data load " + massiveLoadIdentifier
					+ ex.getMessage() + " " + getStackTraceAsString(ex));
		}
	}

	/**
	 * Create a new massive data load
	 * 
	 * @param pName           Massive data load name
	 * @param pFilesDirectory Path directory of the massive data load files
	 * @param pUrl            Url where the file directory should be responding
	 * @return Identifier of the load
	 * @throws GnossAPIException Exception controlled by the Api
	 */
	public UUID createMassiveDataLoad(String pName, String pFilesDirectory, String pUrl) throws GnossAPIException {
		return createMassiveDataLoad(pName, pFilesDirectory, pUrl, false);
	}

	/**
	 * Test the connection with a nq file
	 * 
	 * @throws Exception Exception
	 */
	private void testConnection() throws Exception {
		try {
			File directory = new File(filesDirectory);
			if (!directory.exists()) {
				directory.mkdirs();
			}

			String testFilePath = filesDirectory + File.separator + "test.nq";
			String downloadedTestFilePath = filesDirectory + File.separator + "downloadedtest.nq";

			// nq file creation
			String testContent = "Testing file... " + System.currentTimeMillis();
			Files.write(new File(testFilePath).toPath(), testContent.getBytes(StandardCharsets.UTF_8));

			// summary of nq file
			byte[] fileHash = computeMD5Hash(Files.readAllBytes(new File(testFilePath).toPath()));

			// download nq file
			downloadFile(uri + "/test.nq", downloadedTestFilePath);

			// summary of downloaded nq file
			byte[] downloadedFileHash = computeMD5Hash(Files.readAllBytes(new File(downloadedTestFilePath).toPath()));

			// if the summaries are different, something is wrong
			if (!Arrays.equals(fileHash, downloadedFileHash)) {
				String errorMsg = "The connection to the server could not be established or nq files are not supported.";
				System.err.println(errorMsg);
				throw new Exception(errorMsg);
			}

			// Test resource api
			String url = getApiUrl() + "/massiveresource/test-massive-load";

			MassiveDataLoadTestResource resource = new MassiveDataLoadTestResource();
			resource.setFileHash(fileHash);
			resource.setUrl(uri + "/test.nq");

			WebRequestPostWithJsonObject(url, resource);
		} catch (Exception ex) {
			throw new Exception(
					"The connection to the server could not be established or nq files are not supported. " + uri, ex);
		}
	}

	/**
	 * Uploads an existing massive data load
	 * 
	 * @param pMassiveLoadIdentifier Massive data load identifier
	 * @throws Exception Exception
	 */
	public void uploadPreparedMassiveLoad(UUID pMassiveLoadIdentifier) throws Exception {
		File directory = new File(filesDirectory);
		if (directory.exists()) {
			File[] files = directory.listFiles((dir,
					name) -> name.startsWith(
							getOntologyNameWithoutExtension() + "_acid_" + pMassiveLoadIdentifier.toString())
							&& name.endsWith(".txt"));

			int length = files != null ? files.length : 0;
			counter.put(getOntologyNameWithoutExtension(), new OntologyCount(0, 0));

			if (length == 0) {
				throw new Exception("The massive load doesn't exists.");
			}

			for (int i = 0; i < length; i++) {
				sendPackage(pMassiveLoadIdentifier);
				OntologyCount count = counter.get(getOntologyNameWithoutExtension());
				count.setFileCount(count.getFileCount() + 1);
			}
		} else {
			throw new Exception("The massive load directory doesn't exists.");
		}
	}

	/**
	 * Create a new package massive data load
	 * 
	 * @param resource Interface of the Gnoss Methods
	 * @throws IOException IOException
	 */
	public void addResourceToPackage(IGnossOCBase resource) throws IOException {
		try {
			if (!counter.containsKey(getOntologyNameWithoutExtension())) {
				counter.put(getOntologyNameWithoutExtension(), new OntologyCount(0, 0));
			}

			ArrayList<String> ontologyTriples = resource.ToOntologyGnossTriples(this);
			ArrayList<String> searchTriples = resource.ToSearchGraphTriples(this);
			HashMap<UUID, String> acidData = resource.ToAcidData(this);

			String pathOntology = filesDirectory + File.separator + getOntologyNameWithoutExtension() + "_"
					+ massiveLoadIdentifier.toString() + "_"
					+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".nq";
			String pathSearch = filesDirectory + File.separator + getOntologyNameWithoutExtension() + "_search_"
					+ massiveLoadIdentifier.toString() + "_"
					+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".nq";
			String pathAcid = filesDirectory + File.separator + getOntologyNameWithoutExtension() + "_acid_"
					+ massiveLoadIdentifier.toString() + "_"
					+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".txt";

			if (streamData == null || streamOntology == null || streamSearch == null) {
				streamData = new OutputStreamWriter(new FileOutputStream(pathAcid), StandardCharsets.UTF_8);
				streamOntology = new OutputStreamWriter(new FileOutputStream(pathOntology), StandardCharsets.UTF_8);
				streamSearch = new OutputStreamWriter(new FileOutputStream(pathSearch), StandardCharsets.UTF_8);
			}

			for (String triple : ontologyTriples) {
				streamOntology.write(triple + System.lineSeparator());
			}
			for (String triple : searchTriples) {
				streamSearch.write(triple + System.lineSeparator());
			}
			for (UUID key : acidData.keySet()) {
				streamData.write(key.toString() + "|||" + acidData.get(key) + System.lineSeparator());
			}

			OntologyCount currentCount = counter.get(getOntologyNameWithoutExtension());
			if ((currentCount.getResourcesCount() >= maxResourcesPerPackage && !isDebugMode)
					|| (isDebugMode && currentCount.getResourcesCount() >= DEBUG_PACKAGE_SIZE)) {
				if (isDebugMode) {
					this._logHelper.Warn(
							"DebugMode On, use it only for testing purpose. Please turn DebugMode off as soon as possible.");
				}

				sendPackage();

				currentCount.setResourcesCount(0);
				currentCount.setFileCount(currentCount.getFileCount() + 1);
			} else {
				currentCount.setResourcesCount(currentCount.getResourcesCount() + 1);
			}
		} catch (Exception ex) {
			LogHelper.getInstance().Error("Error creating the package of massive data load " + ex.getMessage() + " "
					+ getStackTraceAsString(ex));
		}
	}

	/**
	 * Close a massive data load
	 * 
	 * @return True if the data load is closed
	 * @throws Exception Exception
	 */
	public boolean closeMassiveDataLoad() throws Exception {
		String url = getApiUrl() + "/massiveresource/close-massive-load";
		CloseMassiveDataLoadResource model = null;
		boolean closed = false;
		try {
			sendPackage();
			initializeResourceCounter();

			model = new CloseMassiveDataLoadResource();
			model.setDataLoadIdentifier(massiveLoadIdentifier);

			WebRequestPostWithJsonObject(url, model);
			LogHelper.getInstance().Debug("Data load is closed");
			closed = true;
		} catch (Exception ex) {
			Gson gson = new Gson();
			LogHelper.getInstance().Error("Error closing the data load " + massiveLoadIdentifier.toString()
					+ ". \r\n Json: " + gson.toJson(model), ex.getMessage());
			throw ex;
		}
		return closed;
	}

	/**
	 * Initialize the resource counter and the file ontology counter
	 */
	private void initializeResourceCounter() {
		OntologyCount count = counter.get(getOntologyNameWithoutExtension());
		if (count != null) {
			count.setResourcesCount(0);
			count.setFileCount(0);
		}
	}

	/**
	 * Create the massive data load
	 * 
	 * @return True if the load is correctly created
	 * @throws Exception Exception
	 */
	private boolean createMassiveDataLoadInternal() throws Exception {
		boolean created = false;
		MassiveDataLoadResource model = null;

		try {
			String url = getApiUrl() + "/massiveresource/create-massive-load";

			model = new MassiveDataLoadResource();
			model.setLoad_id(massiveLoadIdentifier);
			model.setName(loadName);
			model.setCommunity_name(getCommunityShortName());
			model.setOntology(getOntologyUrl());

			WebRequestPostWithJsonObject(url, model);
			created = true;
			LogHelper.getInstance().Debug("Massive data load created");
		} catch (Exception ex) {
			Gson gson = new Gson();
			LogHelper.getInstance().Error("Error creating massive data load " + massiveLoadIdentifier.toString()
					+ ". \r\n Json: " + gson.toJson(model), ex.getMessage());
			throw ex;
		}
		return created;
	}

	/**
	 * Send package with the number of elements indicated in the constructor
	 * 
	 * @param pMassiveLoadIdentifier (Optional) Massive data load identifier
	 */
	private void sendPackage(UUID pMassiveLoadIdentifier) {
		try {
			closeStreams();

			if (onlyPrepareMassiveLoad) {
				return;
			}

			UUID massiveLoadFilesIdentifier = massiveLoadIdentifier;
			if (pMassiveLoadIdentifier != null) {
				massiveLoadFilesIdentifier = pMassiveLoadIdentifier;
			}

			String uriOntology = uri + "/" + getOntologyNameWithoutExtension() + "_"
					+ massiveLoadFilesIdentifier.toString() + "_"
					+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".nq";
			String uriSearch = uri + "/" + getOntologyNameWithoutExtension() + "_search_"
					+ massiveLoadFilesIdentifier.toString() + "_"
					+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".nq";
			String uriAcid = uri + "/" + getOntologyNameWithoutExtension() + "_acid_"
					+ massiveLoadFilesIdentifier.toString() + "_"
					+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".txt";

			MassiveDataLoadPackageResource model = new MassiveDataLoadPackageResource();
			model.setPackage_id(UUID.randomUUID());
			model.setLoad_id(massiveLoadIdentifier);

			// Si es modo debug, enviamos los bytes de los ficheros directamente
			if (isDebugMode) {
				File ontologyFile = new File(filesDirectory + File.separator + getOntologyNameWithoutExtension() + "_"
						+ massiveLoadFilesIdentifier.toString() + "_"
						+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".nq");
				File searchFile = new File(filesDirectory + File.separator + getOntologyNameWithoutExtension()
						+ "_search_" + massiveLoadFilesIdentifier.toString() + "_"
						+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".nq");
				File acidFile = new File(filesDirectory + File.separator + getOntologyNameWithoutExtension() + "_acid_"
						+ massiveLoadFilesIdentifier.toString() + "_"
						+ counter.get(getOntologyNameWithoutExtension()).getFileCount() + ".txt");

				model.setOntology_bytes(Files.readAllBytes(ontologyFile.toPath()));
				model.setSearch_bytes(Files.readAllBytes(searchFile.toPath()));
				model.setSql_bytes(Files.readAllBytes(acidFile.toPath()));
			}

			model.setOntology_rute(uriOntology);
			model.setSearch_rute(uriSearch);
			model.setSql_rute(uriAcid);
			model.setLast(false);

			createPackageMassiveDataLoad(model);

			LogHelper.getInstance().Debug(
					"Package massive data load create with the identifier " + getMassiveLoadIdentifier().toString());
		} catch (Exception ex) {
			LogHelper.getInstance().Error("Error creating the package of massive data load " + ex.getMessage());
		}
	}

	/**
	 * Send package with the number of elements indicated in the constructor
	 */
	private void sendPackage() {
		sendPackage(null);
	}

	/**
	 * Close the streams to prepare new package
	 * 
	 * @throws IOException IOException
	 */
	private void closeStreams() throws IOException {
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
	 * @return True if the new massive data load package was created successfully
	 * @throws Exception Exception
	 */
	private boolean createPackageMassiveDataLoad(MassiveDataLoadPackageResource model) throws Exception {
		boolean created = false;
		try {
			String url = getApiUrl() + "/massiveresource/create-massive-load-package";
			WebRequestPostWithJsonObject(url, model);
			created = true;
			LogHelper.getInstance().Debug("Massive data load package created");
		} catch (Exception ex) {
			Gson gson = new Gson();
			LogHelper.getInstance().Error("Error creating massive data load package " + model.getPackage_id().toString()
					+ ". \r\n Json: " + gson.toJson(model), ex.getMessage());
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
	public EstadoCargaModel loadState(UUID pLoadId) throws Exception {
		EstadoCargaModel estadoCarga;
		try {
			String url = getApiUrl() + "/massiveresource/load-state";
			String response = WebRequestPostWithJsonObject(url, pLoadId);

			Gson gson = new Gson();
			estadoCarga = gson.fromJson(response, EstadoCargaModel.class);
		} catch (Exception ex) {
			throw ex;
		}

		return estadoCarga;
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

	// Utility methods

	/**
	 * Compute MD5 hash of byte array
	 * 
	 * @param data Data to hash
	 * @return MD5 hash
	 * @throws NoSuchAlgorithmException NoSuchAlgorithmException
	 */
	private byte[] computeMD5Hash(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(data);
	}

	/**
	 * Get stack trace as string
	 * 
	 * @param ex Exception
	 * @return Stack trace as string
	 */
	private String getStackTraceAsString(Exception ex) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : ex.getStackTrace()) {
			sb.append(element.toString()).append("\n");
		}
		return sb.toString();
	}

	// Getters and Setters

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
		return massiveLoadIdentifier;
	}

	/**
	 * Return path where the files will be
	 * 
	 * @return Path where the files will be
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
		return uri;
	}

	/**
	 * Set the url where the massive data load will be downloaded
	 * 
	 * @param uri New url where the massiveDataLoad api will download the files
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Get debug mode status
	 * 
	 * @return True if debug mode is enabled
	 */
	public boolean isDebugMode() {
		return isDebugMode;
	}

	/**
	 * Set debug mode
	 * 
	 * @param isDebugMode Debug mode flag
	 */
	public void setDebugMode(boolean isDebugMode) {
		this.isDebugMode = isDebugMode;
	}

	/**
	 * Get max resources per package
	 * 
	 * @return Max resources per package
	 */
	public int getMaxResourcesPerPackage() {
		return maxResourcesPerPackage;
	}

	/**
	 * Set max resources per package
	 * 
	 * @param maxResourcesPerPackage Max resources per package
	 */
	public void setMaxResourcesPerPackage(int maxResourcesPerPackage) {
		this.maxResourcesPerPackage = maxResourcesPerPackage;
	}
}