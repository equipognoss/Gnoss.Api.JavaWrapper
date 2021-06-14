package org.gnoss.apiWrapper.Main;

import org.gnoss.apiWrapper.ApiModel.LoadResourceParams;
import org.gnoss.apiWrapper.ApiModel.ModifyResourceProperty;
import org.gnoss.apiWrapper.ApiModel.ModifyResourceTripleListParams;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelperLogstash;
import org.gnoss.apiWrapper.Helpers.LogLevels;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author salopez
 */
public class GnossApiWrapper {
    // Configuración variables entorno

    /**
     * Variables de entorno a definir (¡¡IMPORTANTE!! respetar mayúsuclas y
     * minúsculas) si no se van a usar los archivos de configuración. Las
     * variables definidas en los archivos Oauth.config y log4csharp.config son:
     *
     * - Variable para usar variables de entorno useEnvironmentVariables =
     * true/false
     *
     * - Variables del OAuth consumerKey consumerSecret apiEndpointV2
     * apiEndpointV3 tokenKey tokenSecret communityShortName ontologyName
     * communityId = (VALOR OPCIONAL, si no se le va a dar valor puede no
     * declararse) localImagesRoute = (VALOR OPCIONAL, si no se le va a dar
     * valor puede no declararse) loadIdentifier = (VALOR OPCIONAL, si no se le
     * va a dar valor puede no declararse) developerEmail
     *
     * - Variables del OAuth Segundo (sólo API V2) Las mismas que las anteriores
     * pero añadiéndole al final "Second" consumerKeySecond consumerSecretSecond
     * ...
     *
     * - Variables del Log logLevel fileRoute (Ésta es la que usa el API V2)
     * logPath = (Ésta la usa el API V3) logFileName = (Ésta la usa el API V3)
     *
     * - Variables de ApplicationInsights useApplicationInsights = true/false
     * implementationKey logLocation
     */
    //Members
    private static final String _affinityTokenKey = "AffinityTokenGnossAPI";
    private static final ConcurrentHashMap<Integer, String> _affinityTokenPerProcess = new ConcurrentHashMap<Integer, String>();
    private HashMap<UUID, String> _resourceLockTokens = new HashMap<>();

    //The executable location
    protected String executableLocation = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

    private OAuthInfo _oauth = null;
    private ILogHelper mLog;

    //Properties
    private String CommunityShortName;
    private boolean UsarVariablesEntorno;
    private String AffinityToken;

    //Constructors    
    /**
     * Constructor of GnossApiWrapper
     *
     * @param oauth  OAuth information to sign the Api requests
     * @param communityShortName  Community shirt name which you want to use
     * the API
     */
    public GnossApiWrapper(OAuthInfo oauth, String communityShortName) {
        _oauth = oauth;
        CommunityShortName = communityShortName;
        this.mLog = LogHelper.getInstance();
    }

    /**
     * Constructor of GnossApiWrapper
     *
     * @param configFilePath  Configuration file path, with a structure like
     * http://api.gnoss.com/v3/
     * @throws IOException  IO Exception 
     * @throws SAXException SAX Exception 
     * @throws ParserConfigurationException Parser Configuration Exception  
     * @throws GnossAPIException GnossAPIException
     */
    public GnossApiWrapper(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
        LoadConfigFile(configFilePath);
        this.mLog = LogHelper.getInstance();
    }

    //Methods
    private void LoadConfigFile(String filePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
        if (getUsarVariablesEntorno()) {
            LoadEnvironmentVariables();
        }
        else {
        	File file = new File(filePath);
        	if(file.exists()){
        		ReadConfigFile(filePath);
        	}
        	//Leer xml
        }
    }
    
    protected void ReadConfigFile(String filePath) throws ParserConfigurationException, SAXException, IOException, GnossAPIException{
    	String apiUrl, consumerKey, consumerSecret, tokenKey, tokenSecret;
    	String developerEmail = "";
    	
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	Document xmlDocument = builder.parse(new File(filePath));
    	
    	NodeList consumerNode = xmlDocument.getElementsByTagName("consumerKey");
    	if(consumerNode.getLength() > 0){
    		consumerKey = consumerNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains consumerKey");
    	}
    	
    	NodeList consumerSecretNode = xmlDocument.getElementsByTagName("consumerSecret");
    	if(consumerSecretNode.getLength() > 0){
    		consumerSecret = consumerSecretNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains consumerSecret");
    	}
    	
    	NodeList tokenNode = xmlDocument.getElementsByTagName("tokenKey");
    	if(tokenNode.getLength() > 0){
    		tokenKey = tokenNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains tokenKey");
    	}
    	
    	NodeList tokenSecretNode = xmlDocument.getElementsByTagName("tokenSecret");
    	if(tokenSecretNode.getLength() > 0){
    		tokenSecret = tokenSecretNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains tokenSecret");
    	}
    	
    	NodeList endpoindNode = xmlDocument.getElementsByTagName("apiEndpoint");
    	if(endpoindNode.getLength() > 0){
    		apiUrl = endpoindNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains apiEndpoint");
    	}
    	
    	NodeList communityShortNameNode = xmlDocument.getElementsByTagName("communityShortName");
    	if(communityShortNameNode.getLength() > 0){
    		CommunityShortName = communityShortNameNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains communityShortName");
    	}
    	
    	NodeList developerEmailNode = xmlDocument.getElementsByTagName("developerEmail");
    	if(developerEmailNode.getLength() > 0){
    		developerEmail = developerEmailNode.item(0).getTextContent();
    	}
    	else{
    		throw new GnossAPIException("The config file doesn´t contains developerEmail");
    	}
    	
    	 ConfigureLogFromConfigFile(filePath);
    	 
    	 setOAuthInstance(new OAuthInfo(apiUrl, tokenKey, tokenSecret, consumerKey, consumerSecret, developerEmail)); 
    }

    private void ConfigureLogFromConfigFile(String filePath){
    	
    	File file= new File(filePath);
    	String logPath="";
    	String logFileName="";
    	String logLevel="";
    	
    	String useApplicationInsights="";
    	String implementationKey="";
    	String logLocation="";
    	
    	
    	
		if (UsarVariablesEntorno) {
			logPath = System.getenv("useApplicationInsights");
			logFileName = System.getenv("implementationKey");
			logLevel = System.getenv("logLevel");

			// Application insights

			useApplicationInsights = System.getenv("useApplicationInsights");
			implementationKey = System.getenv("implementationKey");
			logLocation = System.getenv("logLocation");
			LogHelperLogstash.setEndPoint(System.getenv("logstashEndpoint"));
		} else {

			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(file);
				
				

			} catch (Exception ex) {

			}
			// TODO
		}
    }
    
    protected void LoadEnvironmentVariables() throws GnossAPIException {
        String apiUrl = "", consumerKey = "", consumerSecret = "", tokenKey = "", tokenSecret = "";
        String developerEmail = "";
        Map<String, String> variablesEntorno = System.getenv();

        if (variablesEntorno.containsKey("consumerKey")) {
            consumerKey = System.getenv("consumerKey");
        } else {
            throw new GnossAPIException("The enviroment variables doesn't contains consumerKey");
        }

        if (variablesEntorno.containsKey("consumerSecret")) {
            consumerSecret = System.getenv("consumerSecret");
        } else {
            throw new GnossAPIException("The enviroment variables doesn't contains consumerSecret");
        }

        if (variablesEntorno.containsKey("tokenKey")) {
            tokenKey = System.getenv("tokenKey");
        } else {
            throw new GnossAPIException("The enviroment variables doesn't contains tokenKey");
        }

        if (variablesEntorno.containsKey("tokenSecret")) {
            tokenKey = System.getenv("tokenSecret");
        } else {
            throw new GnossAPIException("The enviroment variables doesn't contains tokenSecret");
        }

        if (variablesEntorno.containsKey("apiEndpointV3")) {
            tokenKey = System.getenv("apiEndpointV3");
        } else {
            throw new GnossAPIException("The enviroment variables doesn't contains apiEndpointV3");
        }

        if (variablesEntorno.containsKey("communityShortName")) {
            tokenKey = System.getenv("communityShortName");
        } else {
            throw new GnossAPIException("The enviroment variables doesn't contains communityShortName");
        }

        if (variablesEntorno.containsKey("developerEmail")) {
            developerEmail = System.getenv("developerEmail");
        }

        //TODO
        //ConfigureLogFromConfigFile());

        _oauth = new OAuthInfo(apiUrl, tokenKey, tokenSecret, consumerKey, consumerSecret, developerEmail);
    }

    protected String GetResponse(HttpURLConnection conexion) throws IOException{
    	BufferedReader in = new BufferedReader(
				  new InputStreamReader(conexion.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while((inputLine = in.readLine()) != null){
			content.append(inputLine);
		}
		
		return content.toString();
    }
    
    protected String WebRequest(String httpMethod, String url, String postData, String contentType, String acceptHeader, HashMap<String, String> otherHeaders) throws IOException, GnossAPIException{
    	HttpURLConnection webRequest = PrepareWebRequest(httpMethod, url, postData, contentType, acceptHeader, otherHeaders);
    	
    	try{
    		String respuesta = GetResponse(webRequest);
    		
    		return respuesta;
    		
    	}
    	catch(IOException ex){
    		String message = null;
    		try{
    			message = ex.getMessage();
    			LogHelper.getInstance().Error(message + ".\r\nResponse: " + message);
    		}
    		catch(Exception e){ }
    		
    		if(message == null || message.equals("")){
    			throw new GnossAPIException(message);
    		}
    		
    		return "ERROR at throw the exception:" + ex.getMessage();
    	}
    }
    
    /*TODO
    private void UpdateLockTokenForResource(String respuesta, Object model){
    	if(!StringUtils.isEmpty(respuesta) &&  )
    }
    */
    protected String WebRequest(String httpMethod, String url, String postData, String contentType, String acceptHeader) throws MalformedURLException, IOException, GnossAPIException{
    	return WebRequest(httpMethod, url, postData, contentType, acceptHeader, null);
    }
    
    protected String WebRequest(String httpMethod, String url, String postData, String contentType) throws MalformedURLException, IOException, GnossAPIException{
    	return WebRequest(httpMethod, url, postData, contentType, "", null);
    }

    protected String WebRequest(String httpMethod, String url, String postData) throws MalformedURLException, IOException, GnossAPIException{
    	return WebRequest(httpMethod, url, postData, "", "", null);
    }

    protected String WebRequest(String httpMethod, String url) throws MalformedURLException, IOException, GnossAPIException{
    	return WebRequest(httpMethod, url, "", "", "", null);
    }
    
    protected String WebRequestPostWithJsonObject(String url, Object model, String acceptHeader) throws IOException, GnossAPIException {
        HashMap<String, String> otherHeaders = new HashMap<>();
        SendLockTokenForResource(model, otherHeaders);

        Gson jsonUtilities = new Gson();
        String json = jsonUtilities.toJson(model);

        
        
        HttpURLConnection webRequest = PrepareWebRequest("POST", url, json, "application/json; charset=UTF-8", acceptHeader, otherHeaders);

        return ReadWebResponse(webRequest.getInputStream());
    }

    protected String ReadWebResponse(InputStream inputStream) throws GnossAPIException, IOException {
    	String message = "";
    	try{
        	message = WebResponseGet(inputStream);
        }
        catch(Exception ex){
        	InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            message = response.toString();

            if (!StringUtils.isEmpty(message)) {
                throw new GnossAPIException(message + "\n" + ex.getMessage());
            }
        }
    	
        return message;
    }

    protected String WebResponseGet(InputStream streamReader) throws GnossAPIException, IOException{
    	InputStreamReader inputStreamReader = new InputStreamReader(streamReader, "utf-8");
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        String message = response.toString();

        if (StringUtils.isEmpty(message)) {
            throw new GnossAPIException("No se ha podido leer la respuesta");
        }

        return message;
    }
    
    protected HttpURLConnection PrepareWebRequest(String httpMethod, String url, String postData, String contentType, String acceptHeader, HashMap<String, String> otherHeaders) throws MalformedURLException, IOException {
        HttpURLConnection webRequest = null;
        OutputStreamWriter requestWriter = null;

        URL iurl = new URL(url);
        webRequest = (HttpURLConnection) iurl.openConnection();
        webRequest.setRequestMethod(httpMethod);
        webRequest.setConnectTimeout(800000);

        SetHeaders(webRequest, contentType, acceptHeader, otherHeaders);
        //webRequest.send("Content-Length", postData.length());
        //webRequest.setFixedLengthStreamingMode(postData.length());
        webRequest.setRequestProperty("Accept-Charset", "UTF-8");
        if (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
        	webRequest.setDoOutput(true);
            //requestWriter = new OutputStreamWriter(webRequest.getOutputStream(), "UTF-8");
        	requestWriter = new OutputStreamWriter(webRequest.getOutputStream(), "UTF-8");
            try {          	            	
                requestWriter.write(postData);
            } finally {
                requestWriter.close();
                requestWriter = null;
            }
        }

        return webRequest;
    }

    private void SetHeaders(HttpURLConnection webRequest, String contentType, String acceptHeader, HashMap<String, String> otherHeaders) throws MalformedURLException {
        //TODO  Getters   
        //webRequest.setRequestProperty("X-Request-ID", AffinityToken);
        SetOauthHeader(webRequest);

        if (otherHeaders != null) {
            for (String header : otherHeaders.keySet()) {
                webRequest.setRequestProperty(header, otherHeaders.get(header));
            }
        }

        if (!StringUtils.isEmpty(contentType)) {
            webRequest.setRequestProperty("Content-Type", contentType);
        }
        if (!StringUtils.isEmpty(acceptHeader)) {
            webRequest.setRequestProperty("Accept", acceptHeader);
        }
    }

    private void SetOauthHeader(HttpURLConnection webRequest) throws MalformedURLException {
        //TODO        
        String singUrl = webRequest.getURL().toString();
        if (!StringUtils.isEmpty(webRequest.getURL().getQuery())) {
            singUrl = webRequest.getURL().getPath().replace(webRequest.getURL().getQuery(), "");
        }
        OAuthInfo OAuth2 = new OAuthInfo(singUrl, getOAuthInstance().getToken(), getOAuthInstance().getTokenSecret(), getOAuthInstance().getConsumerKey(),
        		getOAuthInstance().getConsumerSecret(), getOAuthInstance().getDeveloperEmail());
        String[] partesUrlOAuth = OAuth2.getSignedUrl().split("\\?");
        partesUrlOAuth = partesUrlOAuth[1].split("&");
        String consumer_key = partesUrlOAuth[0].split("=")[1];
        String token = partesUrlOAuth[1].split("=")[1];
        String method = partesUrlOAuth[2].split("=")[1];
        String timestamp = partesUrlOAuth[3].split("=")[1];
        String nonce = partesUrlOAuth[4].split("=")[1];
        String version = partesUrlOAuth[5].split("=")[1];
        String signature = partesUrlOAuth[6].split("=")[1];
        String oauth = "OAuth realm=\"Example\", oauth_consumer_key=\"" + consumer_key + "\", oauth_token=\""
                + token + "\", oauth_signature_method=\"" + method + "\", oauth_signature=\""
                + signature + "\", oauth_timestamp=\"" + timestamp + "\", oauth_nonce=\"" + nonce
                + "\", oauth_version=\"" + version + "\"";

        webRequest.setRequestProperty("Authorization", oauth);
    }

    private void SendLockTokenForResource(Object model, HashMap<String, String> otherHeaders) {
        UUID resourceID = GetResourceIdFromModel(model);
        if (resourceID != null) {
            String token = GetLockTokenForResource(resourceID);
            if (!StringUtils.isEmpty(token)) {
                otherHeaders.put("X-Correlation-ID", token);
            }
        }
    }

    private UUID GetResourceIdFromModel(Object model) {
        if (model.getClass().equals(new ModifyResourceTripleListParams().getClass())) {
            return ((ModifyResourceTripleListParams) model).getResource_id();
        } else if (model.getClass().equals(ModifyResourceProperty.class)) {
            return ((ModifyResourceProperty) model).getResource_id();
        } else if (model.getClass().equals(LoadResourceParams.class)) {
            return ((LoadResourceParams) model).getResource_id();
        }
        return null;
    }

    protected String GetLockTokenForResource(UUID resourceID) {
        if (_resourceLockTokens.containsKey(resourceID)) {
            return _resourceLockTokens.get(resourceID);
        }

        return null;
    }

    protected String WebRequestPostWithJsonObject(String url, Object model) throws IOException, GnossAPIException {
        return WebRequestPostWithJsonObject(url, model, "");
    }

    private void ConfigureLogFromConfigFile(Document docXml) throws GnossAPIException {
        String logPath = "";
        String logFileName = "";
        String logLevel = "";

        if (UsarVariablesEntorno) {
            logPath = System.getenv("logPath");
            logFileName = System.getenv("logFileName");
            logLevel = System.getenv("logLevel");
        } else {
            try {
                XPath logPathNodePath = XPath.newInstance("config/log/logPath");
                Element logPathNode = (Element) logPathNodePath.selectSingleNode(docXml);
                if (logPathNode != null) {
                    logPath = logPathNode.getTextTrim();
                }

                XPath logFileNameNodePath = XPath.newInstance("config/log/logFileName");
                Element logFileNameNode = (Element) logFileNameNodePath.selectSingleNode(docXml);
                if (logFileNameNode != null) {
                    logFileName = logFileNameNode.getTextTrim();
                }

                XPath logLevelNodePath = XPath.newInstance("config/log/logLevel");
                Element logLevelNode = (Element) logLevelNodePath.selectSingleNode(docXml);
                if (logLevelNode != null) {
                    logLevel = logLevelNode.getTextTrim();
                }
            } catch (JDOMException ex) {
                Logger.getLogger(GnossApiWrapper.class.getName()).log(Level.SEVERE, null, ex);
                throw new GnossAPIException("The xml document can't be readed");
            }
        }

        if (!StringUtils.isEmpty(logPath) && !StringUtils.isEmpty(logFileName) && !StringUtils.isEmpty(logLevel)) {
            if (logPath.startsWith("\\")) {
//TODO                
//logPath = HttpContext.Current.Server.MapPath(logPath);
            }

            new LogHelper().setLogDirectory(logPath);
            LogHelper.setLogFileName(logFileName);
            switch (logLevel) {
                case "TRACE":
                    LogHelper.setLogLevel(LogLevels.TRACE);
                    break;
                case "DEBUG":
                    LogHelper.setLogLevel(LogLevels.DEBUG);
                    break;
                case "INFO":
                    LogHelper.setLogLevel(LogLevels.INFO);
                    break;
                case "WARN":
                    LogHelper.setLogLevel(LogLevels.WARN);
                    break;
                case "ERROR":
                    LogHelper.setLogLevel(LogLevels.ERROR);
                    break;
                case "FATAL":
                    LogHelper.setLogLevel(LogLevels.FATAL);
                    break;
                case "OFF":
                    LogHelper.setLogLevel(LogLevels.OFF);
                    break;
            }
        }
    }

    protected void LoadApi() {
    }

    //Getters and Setters
    public ILogHelper getLog() {
        return mLog;
    }

    public void setLog(ILogHelper log) {
        mLog = log;
    }
    
    public String getApiUrl(){
		return getOAuthInstance().getApiUrl();
	}

    public String getCommunityShortName() {
        return CommunityShortName;
    }

    public void setCommunityShortName(String communityShortName) {
        CommunityShortName = communityShortName;
    }

    public boolean getUsarVariablesEntorno() {
        return (System.getenv("useEnvironmentVariables") != null && System.getenv("useEnvironmentVariables").toLowerCase().equals("true"));
    }

    public OAuthInfo getOAuthInstance() {
        return _oauth;
    }

    public void setOAuthInstance(OAuthInfo oAuthInfo) {
        _oauth = oAuthInfo;
        LoadApi();
    }

    public OAuthInfo getOAuthInfo() {
        return _oauth;
    }

    public void setOAuthInfo(OAuthInfo oAuth) {
        _oauth = oAuth;
        LoadApi();
    }
    
    protected void SetLockTokenForResource(UUID resourceId, String token) {
    	if(this._resourceLockTokens.containsKey(resourceId)) {
    		if(token==null) {
    			this._resourceLockTokens.remove(resourceId);
    		}else {
    			this._resourceLockTokens.put(resourceId, token);
    		}
    	}
    	else {
    		_resourceLockTokens.put(resourceId, token);
    	}
    }

}
