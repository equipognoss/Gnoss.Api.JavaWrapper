package org.gnoss.apiWrapper.Main;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.ParamsAddUserGroups;
import org.gnoss.apiWrapper.ApiModel.ParamsAddUserOrg;
import org.gnoss.apiWrapper.ApiModel.ParamsChangeVisibility;
import org.gnoss.apiWrapper.ApiModel.ParamsLoginPassword;
import org.gnoss.apiWrapper.ApiModel.ParamsUserCommunity;
import org.gnoss.apiWrapper.ApiModel.User;
import org.gnoss.apiWrapper.ApiModel.UserNovertiesModel;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.AdministrationPageType;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

public class UserApi extends GnossApiWrapper{
	
	private ILogHelper _logHelper;

	/**
	 * Constructor of UserAPI
	 * @param oauth OAuth
	 * @param communityShortName community short name 
	 */
	public UserApi(OAuthInfo oauth, String communityShortName) {
		super(oauth, communityShortName);
		this._logHelper=LogHelper.getInstance();
	}
	
	/**
	 * Constructor of UserAPI
	 * @param configFilePath  Configuration file path, with a structure like http://api.gnoss.com/v3/exampleConfig.txt 
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws ParserConfigurationException Parser Configuration Exception 
	 * @throws SAXException SAX Exception 
	 * @throws IOException IO Exception 
	 */
	public UserApi( String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper=LogHelper.getInstance();
	}
	
	//region Public Methods
	
	/**
	 * Get the data user by user short name
	 * @param userShortName  User short name you want to get data
	 * @return  User data that has been requested
	 * @throws Exception  Exception 
	 */
	public User getUserByShortName(String userShortName ) throws Exception {

		User user = null;

		try {
			String url = getApiUrl() + "/user/get-by-short-name?user_short_name=" + userShortName + "&community_short_name="+ getCommunityShortName();
			String response = WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			user = gson.fromJson(response, new User().getClass());
			if (user != null) {
				this._logHelper.Debug(
						"The user " + user.getName() + " " + user.getLas_name() + " has been obteined succesfully");
			} else {
				this._logHelper.Error("Couldn´t get the user " + userShortName + "r\n" + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Couldn´t get the user :" + userShortName + "\r\n" + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Get the data by user identifier
	 * @param userId  User identifier you want to get data
	 * @return User data that has been required
	 * @throws Exception exception 
	 */
	public User getUserById(UUID userId) throws Exception {
		
		User user=null;
		
		try {
			String url= getApiUrl()+"/user/get-by-id?user_ID="+userId+"&community_short_name="+getCommunityShortName();
			String response= WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			user= gson.fromJson(response, new User().getClass());
	        if(user!=null) {
	        	this._logHelper.Debug("The user "+ user.getName()+ " "+ user.getLas_name()+ " has been obteined succesfully");
	        }else {
	        	this._logHelper.Error("Couldn´t get the user "+userId+ "r\n" + response);
	        }
		}catch(Exception ex) {
			this._logHelper.Error("Couldn´t get the user :"+userId+ "\r\n" + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Get the data a user by user email
	 * @param email  User email you want to get data
	 * @return User data that has been requested
	 * @throws Exception exception 
	 */
	public User getUserByEmail(String email) throws Exception {

		User user = null;

		try {
			String url = getApiUrl() + "/user/get-by-id?user_ID=" + email + "&community_short_name="
					+ getCommunityShortName();
			String response = WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			user = gson.fromJson(response, new User().getClass());
			if (user != null) {
				this._logHelper.Debug(
						"The user " + user.getName() + " " + user.getLas_name() + " has been obteined succesfully");
			} else {
				this._logHelper.Error("Couldn´t get the user " + email + "r\n" + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Couldn´t get the user :" + email + "\r\n" + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Validate the user password
	 * @param user  User email
	 * @param password  password
	 * @return True if the password is valid
	 * @throws MalformedURLException Mal formed URL Exception 
	 * @throws IOException IO Exception 
	 * @throws GnossAPIException Gnoss API Exception 
	 */
	public boolean ValidatePassword(String user, String password) throws MalformedURLException, IOException, GnossAPIException {
		boolean validPassword=false;
		try {
		if(!user.isEmpty() || !StringUtils.isBlank(user) && !password.isEmpty() ||!StringUtils.isBlank(password)) {
			String url=getApiUrl()+"/user/validate-password";
			ParamsLoginPassword model = new ParamsLoginPassword();
			{
				model.setLogin(user);
				model.setPassword(password);
			}
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
	        
			String respuesta=WebRequest("POST", url, "application/json");
			Gson gson = new Gson();
			validPassword=gson.fromJson(respuesta, boolean.class);
			
			if(validPassword) {
				this._logHelper.Debug("The password for the user "+user+ " is correct");
				
			}else {
				this._logHelper.Debug("The password for the user "+user+ " isn´t correct" );
			}
		}
		}catch(Exception ex) {
			this._logHelper.Error(ex.getMessage());
			throw ex;
		}
		return validPassword;
	}
	
	/**
	 * Gets the position of an organization profile in a community
	 * @param profileId  Organization profile ID
	 * @return Position of the organization profile in a community
	 * @throws Exception Exception 
	 */
	public String getProfileRoleInOrganization(UUID profileId) throws Exception {
		String profileRol="";
		try {
			String url= getApiUrl()+"/user/get-profile-role-in-organization?profile_id="+profileId+"&community_short_name="+getCommunityShortName();
			profileRol=WebRequest("GET", url);
			profileRol.trim();
			this._logHelper.Debug("The profile role of "+profileId+ " in" +getCommunityShortName()+ " is"+ profileRol);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the profile role of "+profileId+ ": " + ex.getMessage());
			throw ex;
		}
		return profileRol;
	}
	
	/**
	 * Create a user awaiting activation
	 * @param user  User data you want to create
	 * @return User data
	 * @throws Exception exception 
	 */
	public User CreateUserWaitingForActivate(User user) throws Exception {

		Gson jsonUtilities = new Gson();
		String json = jsonUtilities.toJson(user);
		
		User createdUser = null;

		try {
			String url = getApiUrl() + "/user/create-user-waiting-for-activate";
					
			String response = WebRequest("POST", url, json, "application/json");
			Gson gson = new Gson();
			user = gson.fromJson(response, new User().getClass());
			if (user != null) {
				this._logHelper.Debug(
						"The user " + createdUser.getName() + " " + createdUser.getLas_name() + " has been obteined succesfully");
			} else {
				this._logHelper.Error("Couldn´t get the user " + json + "r\n" + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Couldn´t get the user :" + json + "\r\n" + ex.getMessage());
			throw ex;
		}
		return createdUser;
	}
	
	/**
	 * Create user
	 * @param user  User data you want to create
	 * @return User data
	 * @throws Exception  exception 
	 */
	public User CreateUser(User user) throws Exception {
		Gson jsonUtilities = new Gson();
		String json = jsonUtilities.toJson(user);
		
		User createdUser = null;

		try {
			String url = getApiUrl() + "/user/create-user-waiting-for-activate";
					
			String response = WebRequest("POST", url, json, "application/json");
			Gson gson = new Gson();
			user = gson.fromJson(response, new User().getClass());
			if (user != null) {
				this._logHelper.Debug(
						"The user " + createdUser.getName() + " " + createdUser.getLas_name() + " has been obteined succesfully");
			} else {
				this._logHelper.Error("Couldn´t get the user " + json + "r\n" + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Couldn´t get the user :" + json + "\r\n" + ex.getMessage());
			throw ex;
		}
		return createdUser;
	}
	
	/**
	 * Verify user
	 * @param loginOrEmail  Login or email of the user
	 * @throws Exception exception 
	 */
	public void VerificarUsuario(String loginOrEmail) throws Exception {
		try {
			String url= getApiUrl()+"/user/verify-user?login="+loginOrEmail;
			WebRequest("POST", url, "", "application/json");
		}catch(Exception ex) {
			this._logHelper.Error("Error validation user"+ loginOrEmail+ " from the community" +getCommunityShortName() +": \r\n "+ ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets the URL to recover the password of a user
	 * @param loginOrEmail Login or email of the user
	 * @return URL to recover the password of a user
	 * @throws Exception exception 
	 */
	public String GenerateForgottenPasswordUrl(String loginOrEmail) throws Exception {
		String link= "";
		if(!loginOrEmail.isEmpty() || !StringUtils.isBlank(loginOrEmail)) {
			try {
				String url= getApiUrl()+"/user/generate-forgotten-password-url?login="+loginOrEmail+"&community_short_name="+getCommunityShortName();
				link= WebRequest("GET", url);
				link.trim();
				this._logHelper.Debug("Forgotten password url generated" +link);
			}catch(Exception ex) {
				this._logHelper.Error("Error generationg forgotten password url for user "+loginOrEmail+":" +ex.getMessage() );
				throw ex;
			}
		}
		return link;
	}
	
	/**
	 * Modify a user
	 * @param user  User data
	 * @throws Exception exception
	 */
	public void ModifyUser(User user) throws Exception {
		Gson jsonUtilities = new Gson();
		String json = jsonUtilities.toJson(user);
		
		try {
			String url=getApiUrl()+"/user/modify-user";
			WebRequest("POST", url, json, "application/json");
			
			this._logHelper.Debug("User modify successfully "+json);
		}catch(Exception ex) {
			this._logHelper.Error("Error trying to modify user "+json+ ": "+ ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a user from a community
	 * @param userShortName  User short name to delete
	 * @throws Exception exception
	 */
	public void DeleteUserFromCommunity(String userShortName) throws Exception {
		try {
			String url=getApiUrl()+"/user/delete-user-from-community";
			ParamsUserCommunity model= new ParamsUserCommunity();
			{
				model.setUser_short_name(userShortName);
				model.setCommunity_short_name(getCommunityShortName());
			}
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User "+userShortName+ " deleted succesfully from the community "+getCommunityShortName());
			
		}catch(Exception ex) {
			this._logHelper.Error("Error deleting user "+userShortName+ " from the community "+getCommunityShortName()+ ": \r\n" +ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a user
	 * @param user  User identifier to delete
	 * @throws Exception exception
	 */
	public void DeleteUser(UUID user) throws Exception {
		try {
			String url=getApiUrl()+"/user/delete-user?user_ID="+user;
			WebRequestPostWithJsonObject( url, user);
			
			this._logHelper.Debug("User "+user + "deleted successfully");
		}catch(Exception ex) {
			this._logHelper.Error("Error deleting user "+user+ ": "+ ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Add a user to an organization
	 * @param userId  User ID to delete
	 * @param organizationShortName  Organization´s short name
	 * @param position  Organization´s position
	 * @param communitiesShortNames Communities´ short names that will be included
	 * @throws Exception exception
	 */
	public void AddUserToOrganization(UUID userId, String organizationShortName, String position, List<String> communitiesShortNames) throws Exception {
		try {
			String url= getApiUrl()+"/user/add-user-to-organization";
			ParamsAddUserOrg model= new ParamsAddUserOrg();
			{
				model.setUder_id(userId);
				model.setPosition(position);
				model.setOrganization_short_name(organizationShortName);
				model.setCommunities_short_names(communitiesShortNames);
			}
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User "+userId+ " added succesfully to organization "+organizationShortName + " in the communities: " +communitiesShortNames.toString());
		}catch(Exception ex) {
			this._logHelper.Error("Error adding user "+userId+ " to organization "+organizationShortName +" in the communities: " +communitiesShortNames.toString());
			throw ex;
		}
	}
	
	/**
	 * Changes user visibility in a community 
	 * @param userId User ID
	 * @param pComunidadesID Communities´ Id 
	 * @param pvisibilidad User´s visibility
	 * @throws Exception exception
	 */
	public void ChangeVisibilityUserCommunities(UUID userId, List<UUID> pComunidadesID, boolean pvisibilidad) throws Exception {
		try {
			String url=getApiUrl()+"/user/change-user-visibility";
			ParamsChangeVisibility model= new ParamsChangeVisibility();
			{
				model.setUser_id(userId);
				model.setCommunities_Id(pComunidadesID);
				model.setVisibility(pvisibilidad);
			}
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User "+userId+ " change visibility in the communities: " +pComunidadesID.toString());
			
		}catch(Exception ex) {
			this._logHelper.Debug("Error adding user "+userId+ " change visibility in the communities: " +pComunidadesID.toString());
			throw ex;
		}
	}
	
	/**
	 * Add a user to an organization
	 * @param userId User ID to delete
	 * @param organizationShortName  Organization short name 
	 * @param groupsShortNames  Organization short names that will be included 
	 * @throws Exception exception
	 */
	public void AddUserToOrganizationGroup(UUID userId, String organizationShortName, List<String> groupsShortNames) throws Exception {
		try {
			String url= getApiUrl()+"/user/add-user-to-organization-group";
			ParamsAddUserGroups model= new ParamsAddUserGroups();
			{
				model.setUder_id(userId);
				model.setOrganization_short_name(organizationShortName);
				model.setGroups_short_names(groupsShortNames);
				
			}
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User "+userId+ " added successfylly to organization "+organizationShortName + " in the groups: "+groupsShortNames.toString());
		}catch(Exception ex) {
			this._logHelper.Debug("Error adding user "+userId+ "  to organization "+organizationShortName + " in the groups: "+groupsShortNames.toString()+ " "+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Get the data user by user id
	 * @param listaIds  User´s Id list
	 * @return Users list
	 * @throws Exception exception 
	 */
	public Map<UUID, User> getUserByIds(List<UUID> listaIds) throws Exception{
		Map<UUID, User> users=null;
		try {
			String url= getApiUrl()+"/user/get-users-by-id";
			String result= WebRequestPostWithJsonObject(url, listaIds);
			
			Gson gson = new Gson();
			users = gson.fromJson(result, (Type) new HashMap<UUID, User>());
			this._logHelper.Debug("Users obtained by Ids");
		}
		catch(Exception ex) {
			this._logHelper.Error("Error getting the users ," +ex.getMessage());
			throw ex;
		}
		return users;
		
	}
	
	/**
	 * Get the modified users from a datetime in a community
	 * @param communityShortName  Community short name 
	 * @param searchDate Start search date time in ISO8601
	 * @return Modified users identifiers list
	 * @throws Exception exception 
	 */
	public List<UUID> getModifiedUsersFromDate(String communityShortName, String searchDate) throws Exception{
		List<UUID> users=null;
		
		try {
			Date date= new Date();
			SimpleDateFormat sdf= new SimpleDateFormat("dd-MM-yyyy");
			sdf.format(date);
			
			boolean esFecha=sdf.parse(searchDate) != null;
			
			if(!esFecha) {
				throw new Exception("The search date string is not in the ISO8601 format "+searchDate);
				}
			
			String url=getApiUrl()+"/user/get-modified-users?community_short_name="+communityShortName+"&search_date="+searchDate;
			String response=WebRequest("GET", url);
			
			Gson gson = new Gson();
			users = gson.fromJson(response, (Type) new ArrayList<UUID>());
			
			this._logHelper.Debug("Users obtained of the community"+communityShortName+"from date"+searchDate);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the users of "+communityShortName+ " from date "+searchDate+" "+ex.getMessage());
			throw ex;
		}
		return users;
		
	}
	
	/**
	 * Get the communities´ short names  managed by the user 
	 * @param login User´s login
	 * @return Communities´ short name managed by the user
	 * @throws Exception exception 
	 */
	public List<String> getManagedCommunity(String login) throws Exception{
		List<String> communities=null;
		try {
			String url=getApiUrl()+"/user/get-admin-communities?login="+login;
			String response=WebRequest("GET", url);
			Gson gson = new Gson();
			communities = gson.fromJson(response, (Type) new ArrayList<UUID>());
			this._logHelper.Debug("Communities obtained for user "+login);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the communities of "+login+ ", "+ ex.getMessage());
			throw ex;
		}
		return communities;
	}
	
	/**
	 * Gets the novelties of the user from a datetime
	 * @param userId  user identifier
	 * @param communityShortName  community short name
	 * @param searchDate  Start search datetime in ISO8601 format
	 * @return UserNoveltiesModel with the novelties of the user from search date
	 * @throws Exception exception 
	 */
	public UserNovertiesModel getUserNoveltiesFromDate(UUID userId, String communityShortName, String searchDate) throws Exception {
		UserNovertiesModel user= null;
		try {
			if(searchDate.contentEquals(" ")|| searchDate.contentEquals("T")) {
				this._logHelper.Error("The search date string is not in the ISO8601 format "+searchDate);
				return null;
			}
			String url=getApiUrl()+"/user/get-user-novelties?user_id="+userId+"&community_short_name="+communityShortName+"&search_date="+searchDate;
			String response=WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			user = gson.fromJson(response, (Type) new UserNovertiesModel ());
			
			if(user!=null) {
				this._logHelper.Debug("Obtained the user "+userId+ "of the community" +communityShortName+ "from the date "+searchDate );
			}else {
				this._logHelper.Debug("The user "+userId+ "could not be obtained of the community" +communityShortName+ "from the date "+searchDate);
			}
		}
		catch(Exception ex) {
			this._logHelper.Error("Error getting the user "+userId+  "of the community" +communityShortName+ "from the date "+searchDate +", " +ex.getMessage());
			throw ex;
		}
		return user;
		
	}
	
	
	/**
	 * Gets a single use token or a long live token to use it in a login action
	 * @param email  user´s email
	 * @param longLiveToken  True if the token is going to be used more than one time
	 * @return String of token 
	 * @throws GnossAPIException Gnoss API Exception
	 * @throws MalformedURLException Mal formed URL Exception
	 * @throws IOException IO Exception 
	 */
	public String getLoginTokenForEmail(String email, boolean longLiveToken) throws GnossAPIException, MalformedURLException, IOException {
		if(!StringUtils.isBlank(email) || !email.isEmpty()) {
			throw new GnossAPIException("The email can´t be null or empty");
		}else {
			String url=getApiUrl()+"/user/generate-login-token-for-email?email="+email+"&longLiveToken="+longLiveToken;
			String result= WebRequest("POST", url, "application/json");
			
			Gson gson = new Gson();
			String token = gson.fromJson(result, String.class);
			return token;
		}
	}
	
	/**
	 * Gets the email asociated to a token
	 * @param token token 
	 * @param deleteSingleToken delete single token 
	 * @return True if it is a single use token to delete it from the database
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws MalformedURLException Mal Formed URL Exception 
	 * @throws IOException IO Exception 
	 */
	public String getEmailByToken(UUID token, boolean deleteSingleToken) throws GnossAPIException, MalformedURLException, IOException {
		if(token.equals("")) {
			throw new GnossAPIException("The token can´t be null or empty");
		}else {
			String url=getApiUrl()+"/user/get-email-by-token?token="+token+"&deleteSingleUseToken="+deleteSingleToken;
			String result= WebRequest("GET", url, "application/json");
			
			Gson gson = new Gson();
			String email = gson.fromJson(result, String.class);
			return email;
		}
	}
	
	/**
	 * Blocks a user
	 * @param userId  User´s identifier
	 * @throws Exception Exception 
	 */
	public void BlockUser(UUID userId) throws Exception {
		try {
			String url=getApiUrl()+"/user/block?user_id="+userId;
			WebRequestPostWithJsonObject(url, userId);
			WebRequest("POST", url);
			
			}catch(Exception ex) {
				this._logHelper.Error("The user "+userId+ "could not be blocked");
				throw ex;
			}
	}
	//endRegion
	
	/**
	 * Unblocks a  user
	 * @param userId  user´s identifier
	 * @throws Exception exception 
	 */
	public void UnblockUser(UUID userId) throws Exception {
		try {
			String url=getApiUrl()+"/user/unblock?user_id="+userId;
			WebRequest("Post", url);
			
		}catch(Exception ex) {
			this._logHelper.Error("The user"+userId+ "could not be ublocked");
			throw ex;
		}
	}
	
	/**
	 * Add a social network login to a user
	 * @param userId  User identifier
	 * @param socialNetworkUserId  Social network user´s identifier
	 * @param socialNetwork  Social Nertwork (like Facebook, instagram, Twitter ...)
	 * @throws Exception exception
	 */
	public void AddSocialNetworkLogin(UUID userId, String socialNetworkUserId, String socialNetwork) throws Exception {
		try {
			String url=getApiUrl()+"/user/add-social-network-login?user_id="+userId+"&social_network_user_id="+URLEncoder.encode(socialNetwork) +"&social_network="+URLEncoder.encode(socialNetwork);
			WebRequest("POST", url);
			
		}catch(Exception ex) {
			this._logHelper.Error("The social network login"+socialNetworkUserId+ " at " +socialNetwork+ " could not be added to user '"+userId+"'");
			throw ex;
		}
	}
	
	
	/**
	 * Modify a social network login to a user
	 * @param userId  user identifier
	 * @param socialNetworkUserId  New social network user´s identifier
	 * @param socialNetwork  Social network (Like Facebook, Twitter, Instagram...)
	 * @throws Exception  exception
	 */
	public void ModifySocialNetworkLogin(UUID userId, String socialNetworkUserId, String socialNetwork) throws Exception {
		try {
			String url=getApiUrl()+"/user/modify-social-network-login?user_id="+userId+"&social_network_user_id="+URLEncoder.encode(socialNetwork) +"&social_network="+URLEncoder.encode(socialNetwork);
			WebRequest("POST", url);
			
		}catch(Exception ex) {
			this._logHelper.Error("The social network login"+socialNetworkUserId+ " at " +socialNetwork+ " could not be added to user '"+userId+"'");
			throw ex;
		}
	}
	
	/**
	 * Gets a user by a social network login
	 * @param socialNetworkUserId  Social network user´s identifier
	 * @param socialNetwork  Social network (Facebook, twitter, instagram...)
	 * @return UUID uuid
	 * @throws Exception exception
	 */
	public UUID getUserBySocialNetworkLogin(String socialNetworkUserId, String socialNetwork) throws Exception {
		UUID user_id = null;
		try {
			String url=getApiUrl()+"/user/get-user_id-by-social-network-login?social_network_user_id="+URLEncoder.encode(socialNetworkUserId) +"&social_network="+URLEncoder.encode(socialNetwork);
			String result=WebRequest("GET", url, "application/json");
			
			Gson gson = new Gson();
			user_id = gson.fromJson(result, UUID.class);
		}catch(Exception ex) {
			this._logHelper.Error("The social network login"+ socialNetworkUserId + " at"+ socialNetwork + "could not be found");
			throw ex;
		}
		return user_id;
	}
	
	/**
	 * Checks if a user ID in a social network exists in the system
	 * @param socialNetworkUserId  Social network user´s identifier
	 * @param socialNetwork  Social network (Facebook, Instagram, Twitter...)
	 * @return Boolean True if the user exists 
	 * @throws Exception exception
	 */
	public boolean ExistsSocialNetworkLogin(String socialNetworkUserId, String socialNetwork) throws Exception {
		boolean b=false;
		try {
			String url=getApiUrl()+"/user/exists-social-network-login?social_network_user_id="+URLEncoder.encode(socialNetworkUserId) +"&social_network="+URLEncoder.encode(socialNetwork);
			String result=WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			b= gson.fromJson(result, boolean.class);
				
		}catch(Exception ex) {
			this._logHelper.Error("The social network login "+socialNetworkUserId+ " at"+ socialNetwork+ " could not be found" );
			throw ex;
		}
		return b;
	}

	/**
	 * Adds the community CMS admin rol to a user
	 * @param emails emails
	 * @return List List of Strings 
	 * @throws Exception  exception
	 */
	public List<String> ExistsEmails (List<String> emails) throws Exception{
		List<String> lista = null;
		try {
			String url=getApiUrl()+"/user/exists-email-in-database";
			String result=WebRequestPostWithJsonObject(url, emails);
			Gson gson = new Gson();
			lista=gson.fromJson(result, (Type) new ArrayList<String>());
		}
		catch(Exception ex) {
			this._logHelper.Error("Impossible to check the emails "+emails.toString());
			throw ex;
		}
		return lista;
	}
	
	/**
	 * Gets the user´s group in a community
	 * @param userId  User identifier
	 * @param communityShortName  Community short name 
	 * @return List List of Strings
	 * @throws Exception  exception
	 */
	public List<String> getGroupsPerCommunity(UUID userId, String communityShortName) throws Exception{
		List<String> lista=null;
		try {
			String url=getApiUrl()+"/user/get-groups-per-community?user_id="+userId+"&community_short_name="+communityShortName;
			String result=WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			lista=gson.fromJson(result, (Type) new ArrayList<String>());
		}
		catch(Exception ex) {
			this._logHelper.Error("Impossible to get groups of" +userId+ " from community " +communityShortName );
			throw ex;
		}
		return lista;
	}
	
	
	/**
	 * Adds the community CMS Admin rol to a user
	 * @param userId user id 
	 * @param communityShortName community short name 
	 * @throws Exception exception
	 */
	public void AddCmsAdminRolToUser(UUID userId, String communityShortName) throws Exception {
		try {
			String url=getApiUrl()+"/user/add-permission?user_id="+userId+"&community_short_name="+communityShortName+"&admin_page_type="+AdministrationPageType.Page;
			WebRequest("POST", url);
			
		}catch(Exception ex) {
			this._logHelper.Error("The community CMS admin rol could not be added to user '" +userId+"'" );
			throw ex;
		}
	}
	
	/**
	 * Removes the community CMS Admin rol to a user
	 * @param userId user id 
	 * @param communityShortName community short name 
	 * @throws Exception exception
	 */
	public void RemoveCmsAdminRolToUser(UUID userId, String communityShortName) throws Exception {
		try {
			String url=getApiUrl()+"/user/remove-permission?user_id="+userId+"&community_short_name="+communityShortName+"&admin_page_type="+AdministrationPageType.Page;
			WebRequest("POST", url);
		}
		catch(Exception ex) {
			this._logHelper.Error("The community CMS admin rol could not be removed from user '"+userId+"'");
			throw ex;
		}
	}
}
