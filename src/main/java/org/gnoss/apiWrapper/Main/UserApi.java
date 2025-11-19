package org.gnoss.apiWrapper.Main;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.ParamsAddUserGroups;
import org.gnoss.apiWrapper.ApiModel.ParamsAddUserOrg;
import org.gnoss.apiWrapper.ApiModel.ParamsChangeVisibility;
import org.gnoss.apiWrapper.ApiModel.ParamsDeleteUserOrgGroup;
import org.gnoss.apiWrapper.ApiModel.ParamsLoginPassword;
import org.gnoss.apiWrapper.ApiModel.ParamsUserCommunity;
import org.gnoss.apiWrapper.ApiModel.User;
import org.gnoss.apiWrapper.ApiModel.Userlite;
import org.gnoss.apiWrapper.ApiModel.UserNovertiesModel;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.AdministrationPageType;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Wrapper for GNOSS user API
 */
public class UserApi extends GnossApiWrapper {
	
	private ILogHelper _logHelper;
	private Gson gson;

	/**
	 * Constructor of UserAPI
	 * @param oauth OAuth
	 * @param communityShortName community short name 
	 */
	public UserApi(OAuthInfo oauth, String communityShortName) {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
		this.gson = createGsonWithDateFormat();
	}
	
	/**
	 * Constructor of UserAPI
	 * @param configFilePath Configuration file path, with a structure like http://api.gnoss.com/v3/exampleConfig.txt 
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws ParserConfigurationException Parser Configuration Exception 
	 * @throws SAXException SAX Exception 
	 * @throws IOException IO Exception 
	 */
	public UserApi(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper = LogHelper.getInstance();
		this.gson = createGsonWithDateFormat();
	}
	
	//region Public Methods
	
	/**
	 * Get the data user by user short name
	 * @param userShortName User short name you want to get data
	 * @return User data that has been requested
	 * @throws Exception Exception 
	 */
	public User getUserByShortName(String userShortName) throws Exception {
		User user = null;
		try {
			String url = getApiUrl() + "/user/get-by-short-name?user_short_name=" + userShortName + "&community_short_name=" + getCommunityShortName();
			String response = WebRequest("GET", url, "application/json");
			user = gson.fromJson(response, User.class);
			if (user != null) {
				this._logHelper.Debug("The user " + user.getName() + " " + user.getLast_name() + " has been obtained successfully");
			} else {
				this._logHelper.Error("Couldn't get the user " + userShortName + "\r\n" + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Couldn't get the user: " + userShortName + "\r\n" + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Get the data by user identifier
	 * @param userId User identifier you want to get data
	 * @return User data that has been required
	 * @throws Exception exception 
	 */
	public User getUserById(UUID userId) throws Exception {
		User user = null;
		try {
			String url = getApiUrl() + "/user/get-by-id?user_ID=" + userId + "&community_short_name=" + getCommunityShortName();
			String response = WebRequest("GET", url, "application/json");
			user = gson.fromJson(response, User.class);
	        if (user != null) {
	        	this._logHelper.Debug("The user " + user.getName() + " " + user.getLast_name() + " has been obtained successfully");
	        } else {
	        	this._logHelper.Error("Couldn't get the user " + userId + "\r\n" + response);
	        }
		} catch (Exception ex) {
			this._logHelper.Error("Couldn't get the user: " + userId + "\r\n" + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Get the data a user by user email
	 * @param email User email you want to get data
	 * @return User data that has been requested
	 * @throws Exception exception 
	 */
	public User getUserByEmail(String email) throws Exception {
		User user = null;
		try {
			String url = getApiUrl() + "/user/get-by-email?email=" + email + "&community_short_name=" + getCommunityShortName();
			String response = WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			user = gson.fromJson(response, User.class);
			if (user != null) {
				this._logHelper.Debug("The user " + user.getName() + " " + user.getLast_name() + " has been obtained successfully");
			} else {
				this._logHelper.Error("Couldn't get the user " + email + "\r\n" + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Couldn't get the user: " + email + "\r\n" + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Validate the user password
	 * @param user User email
	 * @param password password
	 * @return True if the password is valid
	 * @throws MalformedURLException Mal formed URL Exception 
	 * @throws IOException IO Exception 
	 * @throws GnossAPIException Gnoss API Exception 
	 */
	public boolean ValidatePassword(String user, String password) throws MalformedURLException, IOException, GnossAPIException {
		boolean validPassword = false;
		
		if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
			this._logHelper.Error("The user and the password can't be null or empty");
			return validPassword;
		}
		
		try {
			String url = getApiUrl() + "/user/validate-password";
			ParamsLoginPassword model = new ParamsLoginPassword();
			model.setLogin(user);
			model.setPassword(password);
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
	        
			String respuesta = WebRequest("POST", url, postData, "application/json");
			Gson gson = new Gson();
			validPassword = gson.fromJson(respuesta, boolean.class);
			
			if (validPassword) {
				this._logHelper.Debug("The password for the user " + user + " is correct");
			} else {
				this._logHelper.Debug("The password for the user " + user + " isn't correct");
			}
		} catch (Exception ex) {
			this._logHelper.Error(ex.getMessage());
			throw ex;
		}
		return validPassword;
	}
	
	/**
	 * Gets the position of an organization profile in a community
	 * @param profileId Organization profile ID
	 * @return Position of the organization profile in a community
	 * @throws Exception Exception 
	 */
	public String getProfileRoleInOrganization(UUID profileId) throws Exception {
		String profileRol = "";
		try {
			String url = getApiUrl() + "/user/get-profile-role-in-organization?profile_id=" + profileId + "&community_short_name=" + getCommunityShortName();
			profileRol = WebRequest("GET", url);
			profileRol = profileRol.trim().replaceAll("\"", "");
			this._logHelper.Debug("The profile role of " + profileId + " in " + getCommunityShortName() + " is " + profileRol);
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the profile role of " + profileId + ": " + ex.getMessage());
			throw ex;
		}
		return profileRol;
	}
	
	/**
	 * Create a user awaiting activation
	 * @param user User data you want to create
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
			createdUser = gson.fromJson(response, User.class);
			if (createdUser != null) {
				this._logHelper.Debug("User created: " + createdUser.getName() + " " + createdUser.getLast_name());
			} else {
				this._logHelper.Error("Error creating user " + json + ": " + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Error creating user " + json + ": \r\n" + ex.getMessage());
			throw ex;
		}
		return createdUser;
	}
	
	/**
	 * Create user
	 * @param user User data you want to create
	 * @return User data
	 * @throws Exception exception 
	 */
	public User CreateUser(User user) throws Exception {
		Gson jsonUtilities = new Gson();
		String json = jsonUtilities.toJson(user);
		User createdUser = null;

		try {
			String url = getApiUrl() + "/user/create-user";
			String response = WebRequest("POST", url, json, "application/json");
			Gson gson = new Gson();
			createdUser = gson.fromJson(response, User.class);
			if (createdUser != null) {
				this._logHelper.Debug("User created: " + createdUser.getName() + " " + createdUser.getLast_name());
			} else {
				this._logHelper.Error("Error creating user " + json + ": " + response);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Error creating user " + json + ": \r\n" + ex.getMessage());
			throw ex;
		}
		return createdUser;
	}
	
	/**
	 * Delete the user given from an organization group
	 * @param userId Identifier of the user to delete
	 * @param organizationShortName Short name of the organization which the user will be deleted
	 * @param groupShortName Short name of the group which the user will be deleted
	 * @throws Exception exception
	 */
	public void DeleteUserFromOrganizationGroup(UUID userId, String organizationShortName, String groupShortName) throws Exception {
		try {
			String url = getApiUrl() + "/user/delete-user-from-organization-group";
			ParamsDeleteUserOrgGroup model = new ParamsDeleteUserOrgGroup();
			model.setUser_id(userId);
			model.setOrganization_short_name(organizationShortName);
			model.setGroup_short_name(groupShortName);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The user " + userId + " has been deleted from the group " + groupShortName + " of the organization " + organizationShortName);
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting the user " + userId + " from the group " + groupShortName + " of the organization " + organizationShortName + ": \n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete the user given from an organization group
	 * @param shortNameOrEmail Short name or email of the user to delete
	 * @param organizationShortName Short name of the organization which the user will be deleted
	 * @param groupShortName Short name of the group which the user will be deleted
	 * @throws Exception exception
	 */
	public void DeleteUserFromOrganizationGroup(String shortNameOrEmail, String organizationShortName, String groupShortName) throws Exception {
		try {
			String url = getApiUrl() + "/user/delete-user-from-organization-group";
			ParamsDeleteUserOrgGroup model = new ParamsDeleteUserOrgGroup();
			model.setLogin(shortNameOrEmail);
			model.setOrganization_short_name(organizationShortName);
			model.setGroup_short_name(groupShortName);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The user " + shortNameOrEmail + " has been deleted from the group " + groupShortName + " of the organization " + organizationShortName);
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting the user " + shortNameOrEmail + " from the group " + groupShortName + " of the organization " + organizationShortName + ": \n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Gets the userID by login or email
	 * @param login Login or email of the user
	 * @return User identifier
	 * @throws Exception exception
	 */
	public UUID GetUserIdByLogin(String login) throws Exception {
		try {
			String url = getApiUrl() + "/user/get-user-id-by-login?pLogin=" + login;
			String response = WebRequest("GET", url);
			Gson gson = new Gson();
			UUID userId = gson.fromJson(response, UUID.class);
			return userId;
		} catch (Exception ex) {
			this._logHelper.Error("Error getting user's ID for user " + login + " from the community " + getCommunityShortName() + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Get a person by accreditation document
	 * @param accreditationDocument Person accreditation document
	 * @return User data
	 * @throws Exception exception
	 */
	public User GetUserByAccreditationDocument(String accreditationDocument) throws Exception {
		try {
			String url = getApiUrl() + "/user/get-user-by-accreditation-document?pValorDocumentoAcreditativo=" + accreditationDocument + "&pNombreCorto=" + getCommunityShortName();
			String response = WebRequest("GET", url);
			Gson gson = new Gson();
			return gson.fromJson(response, User.class);
		} catch (Exception ex) {
			this._logHelper.Error("Error getting person by accreditation document " + accreditationDocument + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Set accreditation document of a person
	 * @param accreditationDocument Person accreditation document
	 * @param userId Person ID
	 * @return True if the operation was successful
	 * @throws Exception exception
	 */
	public boolean SetAccreditationDocumentByUser(String accreditationDocument, UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/set-accreditation-document-by-user?pValorDocumentoAcreditativo=" + accreditationDocument + "&pUserID=" + userId;
			String response = WebRequest("POST", url);
			Gson gson = new Gson();
			return gson.fromJson(response, boolean.class);
		} catch (Exception ex) {
			this._logHelper.Error("Error setting accreditation document " + accreditationDocument + " to person with id " + userId + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Set accreditation document of a person
	 * @param accreditationDocument Person accreditation document
	 * @param shortNameOrEmail User's short name or email
	 * @return True if the operation was successful
	 * @throws Exception exception
	 */
	public boolean SetAccreditationDocumentByUser(String accreditationDocument, String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/set-accreditation-document-by-user?pValorDocumentoAcreditativo=" + accreditationDocument + "&pLogin=" + shortNameOrEmail;
			String response = WebRequest("POST", url);
			Gson gson = new Gson();
			return gson.fromJson(response, boolean.class);
		} catch (Exception ex) {
			this._logHelper.Error("Error setting accreditation document " + accreditationDocument + " to person " + shortNameOrEmail + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Verify user
	 * @param loginOrEmail Login or email of the user
	 * @throws Exception exception 
	 */
	public void VerificarUsuario(String loginOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/verify-user?login=" + loginOrEmail;
			WebRequest("POST", url, "", "application/json");
		} catch (Exception ex) {
			this._logHelper.Error("Error validation user " + loginOrEmail + " from the community " + getCommunityShortName() + ": \r\n " + ex.getMessage());
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
		String link = "";
		if (!StringUtils.isBlank(loginOrEmail)) {
			try {
				String url = getApiUrl() + "/user/generate-forgotten-password-url?login=" + loginOrEmail + "&community_short_name=" + getCommunityShortName();
				link = WebRequest("GET", url);
				link = link.trim().replaceAll("\"", "");
				this._logHelper.Debug("Forgotten password url generated " + link);
			} catch (Exception ex) {
				this._logHelper.Error("Error generating forgotten password url for user " + loginOrEmail + ": " + ex.getMessage());
				throw ex;
			}
		}
		return link;
	}
	
	/**
	 * Gets the URL to recover the password of a user
	 * @param userId User identifier
	 * @return URL to recover the password of a user
	 * @throws Exception exception 
	 */
	public String GenerateForgottenPasswordUrl(UUID userId) throws Exception {
		String link = "";
		if (userId != null) {
			try {
				String url = getApiUrl() + "/user/generate-forgotten-password-url?user_id=" + userId + "&community_short_name=" + getCommunityShortName();
				link = WebRequest("GET", url);
				link = link.trim().replaceAll("\"", "");
				this._logHelper.Debug("Forgotten password url generated " + link);
			} catch (Exception ex) {
				this._logHelper.Error("Error generating forgotten password url for user " + userId + ": " + ex.getMessage());
				throw ex;
			}
		}
		return link;
	}
	
	/**
	 * Modify a user
	 * @param user User data
	 * @throws Exception exception
	 */
	public void ModifyUser(User user) throws Exception {
		Gson jsonUtilities = new Gson();
		String json = jsonUtilities.toJson(user);
		
		try {
			String url = getApiUrl() + "/user/modify-user";
			WebRequest("POST", url, json, "application/json");
			this._logHelper.Debug("User modify successfully " + json);
		} catch (Exception ex) {
			this._logHelper.Error("Error trying to modify user " + json + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a user from a community
	 * @param userShortName User short name to delete
	 * @throws Exception exception
	 */
	public void DeleteUserFromCommunity(String userShortName) throws Exception {
		try {
			String url = getApiUrl() + "/user/delete-user-from-community";
			ParamsUserCommunity model = new ParamsUserCommunity();
			model.setUser_short_name(userShortName);
			model.setCommunity_short_name(getCommunityShortName());
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + userShortName + " deleted successfully from the community " + getCommunityShortName());
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting user " + userShortName + " from the community " + getCommunityShortName() + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a user from a community
	 * @param userId User's identifier
	 * @throws Exception exception
	 */
	public void DeleteUserFromCommunity(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/delete-user-from-community";
			ParamsUserCommunity model = new ParamsUserCommunity();
			model.setUser_id(userId);
			model.setCommunity_short_name(getCommunityShortName());
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + userId + " deleted successfully from the community " + getCommunityShortName());
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting user " + userId + " from the community " + getCommunityShortName() + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a user
	 * @param userId User identifier to delete
	 * @throws Exception exception
	 */
	public void DeleteUser(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/delete-user?user_ID=" + userId;
			WebRequestPostWithJsonObject(url, userId);
			this._logHelper.Debug("User " + userId + " deleted successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting user " + userId + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Add a user to an organization
	 * @param userId User ID
	 * @param organizationShortName Organization's short name
	 * @param position Organization's position
	 * @param communitiesShortNames Communities' short names that will be included
	 * @throws Exception exception
	 */
	public void AddUserToOrganization(UUID userId, String organizationShortName, String position, List<String> communitiesShortNames) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-user-to-organization";
			ParamsAddUserOrg model = new ParamsAddUserOrg();
			model.setUser_id(userId);
			model.setPosition(position);
			model.setOrganization_short_name(organizationShortName);
			model.setCommunities_short_names(communitiesShortNames);
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + userId + " added successfully to organization " + organizationShortName + " in the communities: " + String.join(",", communitiesShortNames));
		} catch (Exception ex) {
			this._logHelper.Error("Error adding user " + userId + " to organization " + organizationShortName + " in the communities: " + String.join(",", communitiesShortNames) + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Add a user to an organization
	 * @param shortNameOrEmail User short name or email
	 * @param organizationShortName Organization's short name
	 * @param position Organization's position
	 * @param communitiesShortNames Communities' short names that will be included
	 * @throws Exception exception
	 */
	public void AddUserToOrganization(String shortNameOrEmail, String organizationShortName, String position, List<String> communitiesShortNames) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-user-to-organization";
			ParamsAddUserOrg model = new ParamsAddUserOrg();
			model.setLogin(shortNameOrEmail);
			model.setPosition(position);
			model.setOrganization_short_name(organizationShortName);
			model.setCommunities_short_names(communitiesShortNames);
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + shortNameOrEmail + " added successfully to organization " + organizationShortName + " in the communities: " + String.join(",", communitiesShortNames));
		} catch (Exception ex) {
			this._logHelper.Error("Error adding user " + shortNameOrEmail + " to organization " + organizationShortName + " in the communities: " + String.join(",", communitiesShortNames) + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Changes user visibility in a community 
	 * @param userId User ID
	 * @param communitiesId Communities' Id 
	 * @param visibility User's visibility
	 * @throws Exception exception
	 */
	public void ChangeVisibilityUserCommunities(UUID userId, List<UUID> communitiesId, boolean visibility) throws Exception {
		try {
			String url = getApiUrl() + "/user/change-user-visibility";
			ParamsChangeVisibility model = new ParamsChangeVisibility();
			model.setUser_id(userId);
			model.setCommunities_Id(communitiesId);
			model.setVisibility(visibility);
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + userId + " changed visibility in the communities: " + communitiesId.toString());
		} catch (Exception ex) {
			this._logHelper.Error("Error changing user " + userId + " visibility in the communities: " + communitiesId.toString() + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Add a user to organization groups
	 * @param userId User ID
	 * @param organizationShortName Organization short name 
	 * @param groupsShortNames Groups short names that will be included 
	 * @throws Exception exception
	 */
	public void AddUserToOrganizationGroup(UUID userId, String organizationShortName, List<String> groupsShortNames) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-user-to-organization-group";
			ParamsAddUserGroups model = new ParamsAddUserGroups();
			model.setUser_id(userId);
			model.setOrganization_short_name(organizationShortName);
			model.setGroups_short_names(groupsShortNames);
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + userId + " added successfully to organization " + organizationShortName + " in the groups: " + String.join(",", groupsShortNames));
		} catch (Exception ex) {
			this._logHelper.Error("Error adding user " + userId + " to organization " + organizationShortName + " in the groups: " + String.join(",", groupsShortNames) + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Add a user to organization groups
	 * @param shortNameOrEmail User short name or email
	 * @param organizationShortName Organization short name 
	 * @param groupsShortNames Groups short names that will be included 
	 * @throws Exception exception
	 */
	public void AddUserToOrganizationGroup(String shortNameOrEmail, String organizationShortName, List<String> groupsShortNames) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-user-to-organization-group";
			ParamsAddUserGroups model = new ParamsAddUserGroups();
			model.setLogin(shortNameOrEmail);
			model.setOrganization_short_name(organizationShortName);
			model.setGroups_short_names(groupsShortNames);
			
			Gson jsonUtilities = new Gson();
			String postData = jsonUtilities.toJson(model);
			
			WebRequest("POST", url, postData, "application/json");
			this._logHelper.Debug("User " + shortNameOrEmail + " added successfully to organization " + organizationShortName + " in the groups: " + String.join(",", groupsShortNames));
		} catch (Exception ex) {
			this._logHelper.Error("Error adding user " + shortNameOrEmail + " to organization " + organizationShortName + " in the groups: " + String.join(",", groupsShortNames) + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Get the data user by user id
	 * @param listaIds User's Id list
	 * @return Users map
	 * @throws Exception exception 
	 */
	public Map<UUID, Userlite> getUserByIds(List<UUID> listaIds) throws Exception {
		Map<UUID, Userlite> users = null;
		try {
			String url = getApiUrl() + "/user/get-users-by-id";
			String result = WebRequestPostWithJsonObject(url, listaIds);
			
			Gson gson = new Gson();
			Type type = new TypeToken<HashMap<UUID, Userlite>>(){}.getType();
			users = gson.fromJson(result, type);
			this._logHelper.Debug("Users obtained by Ids");
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the users: " + ex.getMessage());
			throw ex;
		}
		return users;
	}
	
	/**
	 * Get the data user by user short name or email
	 * @param lista List of users short name or email
	 * @return Users map
	 * @throws Exception exception 
	 */
	public Map<UUID, Userlite> getUsersByShortNameOrEmail(List<String> lista) throws Exception {
		Map<UUID, Userlite> users = null;
		try {
			String url = getApiUrl() + "/user/get-users-by-shortname-or-email";
			String result = WebRequestPostWithJsonObject(url, lista);
			
			Gson gson = new Gson();
			Type type = new TypeToken<HashMap<UUID, Userlite>>(){}.getType();
			users = gson.fromJson(result, type);
			this._logHelper.Debug("Users obtained by short name or email");
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the users: " + ex.getMessage());
			throw ex;
		}
		return users;
	}
	
	/**
	 * Get the modified users from a datetime in a community
	 * @param searchDate Start search date time in ISO8601
	 * @return Modified users identifiers list
	 * @throws Exception exception 
	 */
	public List<UUID> getModifiedUsersFromDate(String searchDate) throws Exception {
		List<UUID> users = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			boolean esFecha = false;
			try {
				sdf.parse(searchDate);
				esFecha = true;
			} catch (Exception e) {
				// Not a valid date
			}
			
			if (!esFecha) {
				throw new Exception("The search date string is not in the ISO8601 format: " + searchDate);
			}
			
			String url = getApiUrl() + "/user/get-modified-users?community_short_name=" + getCommunityShortName() + "&search_date=" + searchDate;
			String response = WebRequest("GET", url);
			
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<UUID>>(){}.getType();
			users = gson.fromJson(response, type);
			
			this._logHelper.Debug("Users obtained of the community " + getCommunityShortName() + " from date " + searchDate);
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the users of " + getCommunityShortName() + " from date " + searchDate + ": " + ex.getMessage());
			throw ex;
		}
		return users;
	}
	
	/**
	 * Get the communities' short names managed by the user 
	 * @param login User's login
	 * @return Communities' short name managed by the user
	 * @throws Exception exception 
	 */
	public List<String> getManagedCommunity(String login) throws Exception {
		List<String> communities = null;
		try {
			String url = getApiUrl() + "/user/get-admin-communities?login=" + login;
			String response = WebRequest("GET", url);
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			communities = gson.fromJson(response, type);
			this._logHelper.Debug("Communities obtained for user " + login);
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the communities of " + login + ": " + ex.getMessage());
			throw ex;
		}
		return communities;
	}
	
	/**
	 * Get the communities' short names managed by the user 
	 * @param userId User identifier
	 * @return Communities' short name managed by the user
	 * @throws Exception exception 
	 */
	public List<String> getManagedCommunity(UUID userId) throws Exception {
		List<String> communities = null;
		try {
			String url = getApiUrl() + "/user/get-admin-communities?user_id=" + userId;
			String response = WebRequest("GET", url);
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			communities = gson.fromJson(response, type);
			this._logHelper.Debug("Communities obtained for user " + userId);
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the communities of " + userId + ": " + ex.getMessage());
			throw ex;
		}
		return communities;
	}
	
	/**
	 * Gets the novelties of the user from a datetime
	 * @param userId user identifier
	 * @param searchDate Start search datetime in ISO8601 format
	 * @return UserNoveltiesModel with the novelties of the user from search date
	 * @throws Exception exception 
	 */
	public UserNovertiesModel getUserNoveltiesFromDate(UUID userId, String searchDate) throws Exception {
		UserNovertiesModel user = null;
		try {
			if (searchDate.contains(" ") || !searchDate.contains("T")) {
				this._logHelper.Error("The search date string is not in the ISO8601 format: " + searchDate);
				return null;
			}
			String url = getApiUrl() + "/user/get-user-novelties?user_id=" + userId + "&community_short_name=" + getCommunityShortName() + "&search_date=" + searchDate;
			String response = WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			user = gson.fromJson(response, UserNovertiesModel.class);
			
			if (user != null) {
				this._logHelper.Debug("Obtained the user " + userId + " of the community " + getCommunityShortName() + " from the date " + searchDate);
			} else {
				this._logHelper.Debug("The user " + userId + " could not be obtained of the community " + getCommunityShortName() + " from the date " + searchDate);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the user " + userId + " of the community " + getCommunityShortName() + " from the date " + searchDate + ": " + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Gets the novelties of the user from a datetime
	 * @param shortNameOrEmail User short name or email
	 * @param searchDate Start search datetime in ISO8601 format
	 * @return UserNoveltiesModel with the novelties of the user from search date
	 * @throws Exception exception 
	 */
	public UserNovertiesModel getUserNoveltiesFromDate(String shortNameOrEmail, String searchDate) throws Exception {
		UserNovertiesModel user = null;
		try {
			if (searchDate.contains(" ") || !searchDate.contains("T")) {
				this._logHelper.Error("The search date string is not in the ISO8601 format: " + searchDate);
				return null;
			}
			String url = getApiUrl() + "/user/get-user-novelties?login=" + shortNameOrEmail + "&community_short_name=" + getCommunityShortName() + "&search_date=" + searchDate;
			String response = WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			user = gson.fromJson(response, UserNovertiesModel.class);
			
			if (user != null) {
				this._logHelper.Debug("Obtained the user " + shortNameOrEmail + " of the community " + getCommunityShortName() + " from the date " + searchDate);
			} else {
				this._logHelper.Debug("The user " + shortNameOrEmail + " could not be obtained of the community " + getCommunityShortName() + " from the date " + searchDate);
			}
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the user " + shortNameOrEmail + " of the community " + getCommunityShortName() + " from the date " + searchDate + ": " + ex.getMessage());
			throw ex;
		}
		return user;
	}
	
	/**
	 * Gets the UserID from Cookie
	 * @param cookie cookie
	 * @return UserID from cookie
	 * @throws Exception exception
	 */
	public UUID GetUserIDFromCookie(String cookie) throws Exception {
		UUID userID = null;
		try {
			String url = getApiUrl() + "/user/get-user-cookie?pCookie=" + cookie;
			String response = WebRequest("GET", url, "application/x-www-form-urlencoded");
			Gson gson = new Gson();
			userID = gson.fromJson(response, UUID.class);
		} catch (Exception ex) {
			this._logHelper.Error("Error getting the user from the cookie: " + ex.getMessage());
			throw ex;
		}
		return userID;
	}
	
	/**
	 * Gets a single use token or a long live token to use it in a login action
	 * @param email user's email
	 * @param longLiveToken True if the token is going to be used more than one time
	 * @return String of token 
	 * @throws GnossAPIException Gnoss API Exception
	 * @throws MalformedURLException Mal formed URL Exception
	 * @throws IOException IO Exception 
	 */
	public String getLoginTokenForEmail(String email, boolean longLiveToken) throws GnossAPIException, MalformedURLException, IOException {
		if (StringUtils.isBlank(email)) {
			throw new GnossAPIException("The email can't be null or empty");
		}
		
		String url = getApiUrl() + "/user/generate-login-token-for-email?email=" + email + "&longLiveToken=" + longLiveToken;
		String result = WebRequest("POST", url, "application/json");
		
		Gson gson = new Gson();
		String token = gson.fromJson(result, String.class);
		return token;
	}
	
	/**
	 * Gets a single use token or a long live token to use it in a login action
	 * @param userId user's id
	 * @param longLiveToken True if the token is going to be used more than one time
	 * @return String of token 
	 * @throws GnossAPIException Gnoss API Exception
	 * @throws MalformedURLException Mal formed URL Exception
	 * @throws IOException IO Exception 
	 */
	public String getLoginTokenForEmail(UUID userId, boolean longLiveToken) throws GnossAPIException, MalformedURLException, IOException {
		if (userId == null) {
			throw new GnossAPIException("The user id can't be null");
		}
		
		String url = getApiUrl() + "/user/generate-login-token-for-email?user_id=" + userId + "&longLiveToken=" + longLiveToken;
		String result = WebRequest("POST", url, "application/json");
		
		Gson gson = new Gson();
		String token = gson.fromJson(result, String.class);
		return token;
	}
	
	/**
	 * Gets the email associated to a token
	 * @param token token 
	 * @param deleteSingleToken True if it is a single use token to delete it from the database
	 * @return Email associated to the token
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws MalformedURLException Mal Formed URL Exception 
	 * @throws IOException IO Exception 
	 */
	public String getEmailByToken(UUID token, boolean deleteSingleToken) throws GnossAPIException, MalformedURLException, IOException {
		if (token == null) {
			throw new GnossAPIException("The token can't be null");
		}
		
		String url = getApiUrl() + "/user/get-email-by-token?token=" + token + "&deleteSingleUseToken=" + deleteSingleToken;
		String result = WebRequest("GET", url, "application/json");
		
		Gson gson = new Gson();
		String email = gson.fromJson(result, String.class);
		return email;
	}
	
	/**
	 * Blocks a user
	 * @param userId User's identifier
	 * @throws Exception Exception 
	 */
	public void BlockUser(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/block?user_id=" + userId;
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The user '" + userId + "' could not be blocked");
			throw ex;
		}
	}
	
	/**
	 * Blocks a user
	 * @param shortNameOrEmail User's short name or email
	 * @throws Exception Exception 
	 */
	public void BlockUser(String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/block?login=" + shortNameOrEmail;
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The user '" + shortNameOrEmail + "' could not be blocked");
			throw ex;
		}
	}
	
	/**
	 * Unblocks a user
	 * @param userId user's identifier
	 * @throws Exception exception 
	 */
	public void UnblockUser(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/unblock?user_id=" + userId;
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The user '" + userId + "' could not be unblocked");
			throw ex;
		}
	}
	
	/**
	 * Unblocks a user
	 * @param shortNameOrEmail user's short name or email
	 * @throws Exception exception 
	 */
	public void UnblockUser(String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/unblock?login=" + shortNameOrEmail;
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The user '" + shortNameOrEmail + "' could not be unblocked");
			throw ex;
		}
	}
	
	/**
	 * Add a social network login to a user
	 * @param userId User identifier
	 * @param socialNetworkUserId Social network user's identifier
	 * @param socialNetwork Social Network (like Facebook, Instagram, Twitter ...)
	 * @throws Exception exception
	 */
	public void AddSocialNetworkLogin(UUID userId, String socialNetworkUserId, String socialNetwork) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-social-network-login?user_id=" + userId + 
					"&social_network_user_id=" + URLEncoder.encode(socialNetworkUserId, StandardCharsets.UTF_8) + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The social network login " + socialNetworkUserId + " at " + socialNetwork + " could not be added to user '" + userId + "'");
			throw ex;
		}
	}
	
	/**
	 * Add a social network login to a user
	 * @param shortNameOrEmail User short name or email
	 * @param socialNetworkUserId Social network user's identifier
	 * @param socialNetwork Social Network (like Facebook, Instagram, Twitter ...)
	 * @throws Exception exception
	 */
	public void AddSocialNetworkLogin(String shortNameOrEmail, String socialNetworkUserId, String socialNetwork) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-social-network-login?login=" + shortNameOrEmail + 
					"&social_network_user_id=" + URLEncoder.encode(socialNetworkUserId, StandardCharsets.UTF_8) + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The social network login " + socialNetworkUserId + " at " + socialNetwork + " could not be added to user '" + shortNameOrEmail + "'");
			throw ex;
		}
	}
	
	/**
	 * Modify a social network login to a user
	 * @param userId user identifier
	 * @param socialNetworkUserId New social network user's identifier
	 * @param socialNetwork Social network (Like Facebook, Twitter, Instagram...)
	 * @throws Exception exception
	 */
	public void ModifySocialNetworkLogin(UUID userId, String socialNetworkUserId, String socialNetwork) throws Exception {
		try {
			String url = getApiUrl() + "/user/modify-social-network-login?user_id=" + userId + 
					"&social_network_user_id=" + URLEncoder.encode(socialNetworkUserId, StandardCharsets.UTF_8) + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The social network login " + socialNetworkUserId + " at " + socialNetwork + " could not be modified for user '" + userId + "'");
			throw ex;
		}
	}
	
	/**
	 * Modify a social network login to a user
	 * @param shortNameOrEmail user short name or email
	 * @param socialNetworkUserId New social network user's identifier
	 * @param socialNetwork Social network (Like Facebook, Twitter, Instagram...)
	 * @throws Exception exception
	 */
	public void ModifySocialNetworkLogin(String shortNameOrEmail, String socialNetworkUserId, String socialNetwork) throws Exception {
		try {
			String url = getApiUrl() + "/user/modify-social-network-login?login=" + shortNameOrEmail + 
					"&social_network_user_id=" + URLEncoder.encode(socialNetworkUserId, StandardCharsets.UTF_8) + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The social network login " + socialNetworkUserId + " at " + socialNetwork + " could not be modified for user '" + shortNameOrEmail + "'");
			throw ex;
		}
	}
	
	/**
	 * Gets a user by a social network login
	 * @param socialNetworkUserId Social network user's identifier
	 * @param socialNetwork Social network (Facebook, twitter, instagram...)
	 * @return User UUID
	 * @throws Exception exception
	 */
	public UUID getUserBySocialNetworkLogin(String socialNetworkUserId, String socialNetwork) throws Exception {
		UUID user_id = null;
		try {
			String url = getApiUrl() + "/user/get-user_id-by-social-network-login?social_network_user_id=" + 
					URLEncoder.encode(socialNetworkUserId, StandardCharsets.UTF_8) + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			String result = WebRequest("GET", url, "application/json");
			
			Gson gson = new Gson();
			user_id = gson.fromJson(result, UUID.class);
		} catch (Exception ex) {
			this._logHelper.Error("The social network login " + socialNetworkUserId + " at " + socialNetwork + " could not be found");
			throw ex;
		}
		return user_id;
	}
	
	/**
	 * Checks if a user ID in a social network exists in the system
	 * @param socialNetworkUserId Social network user's identifier
	 * @param socialNetwork Social network (Facebook, Instagram, Twitter...)
	 * @return Boolean True if the user exists 
	 * @throws Exception exception
	 */
	public boolean ExistsSocialNetworkLogin(String socialNetworkUserId, String socialNetwork) throws Exception {
		boolean exists = false;
		try {
			String url = getApiUrl() + "/user/exists-social-network-login?social_network_user_id=" + 
					URLEncoder.encode(socialNetworkUserId, StandardCharsets.UTF_8) + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			String result = WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			exists = gson.fromJson(result, boolean.class);
		} catch (Exception ex) {
			this._logHelper.Error("The social network login " + socialNetworkUserId + " at " + socialNetwork + " could not be found");
			throw ex;
		}
		return exists;
	}

	/**
	 * Checks if the emails already exist in the database
	 * @param emails Email list that you want to check
	 * @return Email list that already exists in the database
	 * @throws Exception exception
	 */
	public List<String> ExistsEmails(List<String> emails) throws Exception {
		List<String> lista = null;
		try {
			String url = getApiUrl() + "/user/exists-email-in-database";
			String result = WebRequestPostWithJsonObject(url, emails);
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			lista = gson.fromJson(result, type);
		} catch (Exception ex) {
			this._logHelper.Error("Impossible to check the emails " + String.join(",", emails));
			throw ex;
		}
		return lista;
	}
	
	/**
	 * Return short path of the personal profile of the user
	 * @param userId Identifier of the user
	 * @return Photo path or null if the user has no photo
	 * @throws Exception exception
	 */
	public String GetUserPhoto(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/get-user-photo?user_id=" + userId;
			return WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error(ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Return short path of the personal profile of the user
	 * @param shortNameOrEmail Short name or email of the user
	 * @return Photo path or null if the user has no photo
	 * @throws Exception exception
	 */
	public String GetUserPhoto(String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/get-user-photo?login=" + shortNameOrEmail;
			return WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error(ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Gets the user's groups in a community
	 * @param userId User identifier
	 * @return List of group short names
	 * @throws Exception exception
	 */
	public List<String> getGroupsPerCommunity(UUID userId) throws Exception {
		List<String> lista = null;
		try {
			String url = getApiUrl() + "/user/get-groups-per-community?user_id=" + userId + "&community_short_name=" + getCommunityShortName();
			String result = WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			lista = gson.fromJson(result, type);
		} catch (Exception ex) {
			this._logHelper.Error("Impossible to get groups of " + userId + " from community " + getCommunityShortName());
			throw ex;
		}
		return lista;
	}
	
	/**
	 * Gets the user's groups in a community
	 * @param shortNameOrEmail User short name or email
	 * @return List of group short names
	 * @throws Exception exception
	 */
	public List<String> getGroupsPerCommunity(String shortNameOrEmail) throws Exception {
		List<String> lista = null;
		try {
			String url = getApiUrl() + "/user/get-groups-per-community?login=" + shortNameOrEmail + "&community_short_name=" + getCommunityShortName();
			String result = WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			Type type = new TypeToken<ArrayList<String>>(){}.getType();
			lista = gson.fromJson(result, type);
		} catch (Exception ex) {
			this._logHelper.Error("Impossible to get groups of " + shortNameOrEmail + " from community " + getCommunityShortName());
			throw ex;
		}
		return lista;
	}
	
	/**
	 * Gets a social network login by a user id
	 * @param socialNetwork Social network short name
	 * @param userId User identifier
	 * @return Social network login of the user
	 * @throws Exception exception
	 */
	public String GetSocialNetworkLoginByUserId(String socialNetwork, UUID userId) throws Exception {
		String socialNetworkLogin = null;
		try {
			String url = getApiUrl() + "/user/get-social-network-login-by-user_id?user_id=" + userId + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			socialNetworkLogin = WebRequest("GET", url, "application/json");
			socialNetworkLogin = socialNetworkLogin.trim().replaceAll("\"", "");
		} catch (Exception ex) {
			this._logHelper.Error("The user " + userId + " at " + socialNetwork + " could not be found.");
			throw ex;
		}
		return socialNetworkLogin;
	}
	
	/**
	 * Gets a social network login by a user short name or email
	 * @param socialNetwork Social network short name
	 * @param shortNameOrEmail User short name or email
	 * @return Social network login of the user
	 * @throws Exception exception
	 */
	public String GetSocialNetworkLoginByShortNameOrEmail(String socialNetwork, String shortNameOrEmail) throws Exception {
		String socialNetworkLogin = null;
		try {
			String url = getApiUrl() + "/user/get-social-network-login-by-user_id?login=" + shortNameOrEmail + 
					"&social_network=" + URLEncoder.encode(socialNetwork, StandardCharsets.UTF_8);
			socialNetworkLogin = WebRequest("GET", url, "application/json");
			socialNetworkLogin = socialNetworkLogin.trim().replaceAll("\"", "");
		} catch (Exception ex) {
			this._logHelper.Error("The user " + shortNameOrEmail + " at " + socialNetwork + " could not be found.");
			throw ex;
		}
		return socialNetworkLogin;
	}
	
	/**
	 * Adds the community CMS admin rol to a user
	 * @param userId User identifier
	 * @throws Exception exception
	 */
	public void AddCmsAdminRolToUser(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-permission?user_id=" + userId + "&community_short_name=" + getCommunityShortName() + "&admin_page_type=" + AdministrationPageType.Page.getValue();
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The community CMS admin rol could not be added to user '" + userId + "'");
			throw ex;
		}
	}
	
	/**
	 * Adds the community CMS admin rol to a user
	 * @param shortNameOrEmail User short name or email
	 * @throws Exception exception
	 */
	public void AddCmsAdminRolToUser(String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/add-permission?login=" + shortNameOrEmail + "&community_short_name=" + getCommunityShortName() + "&admin_page_type=" + AdministrationPageType.Page.getValue();
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The community CMS admin rol could not be added to user '" + shortNameOrEmail + "'");
			throw ex;
		}
	}
	
	/**
	 * Removes the community CMS Admin rol from a user
	 * @param userId User identifier
	 * @throws Exception exception
	 */
	public void RemoveCmsAdminRolToUser(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/user/remove-permission?user_id=" + userId + "&community_short_name=" + getCommunityShortName() + "&admin_page_type=" + AdministrationPageType.Page.getValue();
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The community CMS admin rol could not be removed from user '" + userId + "'");
			throw ex;
		}
	}
	
	/**
	 * Removes the community CMS Admin rol from a user
	 * @param shortNameOrEmail User short name or email
	 * @throws Exception exception
	 */
	public void RemoveCmsAdminRolToUser(String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/user/remove-permission?login=" + shortNameOrEmail + "&community_short_name=" + getCommunityShortName() + "&admin_page_type=" + AdministrationPageType.Page.getValue();
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("The community CMS admin rol could not be removed from user '" + shortNameOrEmail + "'");
			throw ex;
		}
	}
	
	/**
	 * Clear caches of a person
	 * @param personId Person identifier
	 * @throws Exception exception
	 */
	public void ClearPersonCache(UUID personId) throws Exception {
		try {
			String url = getApiUrl() + "/cache/invalidar-caches-locales?pPersonaID=" + personId;
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("Error while trying to clean cache of person: '" + personId + "'");
			throw ex;
		}
	}
	
	/**
	 * Clear caches of a person
	 * @param shortNameOrEmail Person short name or email
	 * @throws Exception exception
	 */
	public void ClearPersonCache(String shortNameOrEmail) throws Exception {
		try {
			String url = getApiUrl() + "/cache/invalidar-caches-locales?login=" + shortNameOrEmail;
			WebRequest("POST", url);
		} catch (Exception ex) {
			this._logHelper.Error("Error while trying to clean cache of person: '" + shortNameOrEmail + "'");
			throw ex;
		}
	}
	
	//endregion
	
	private Gson createGsonWithDateFormat() {
		return new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
				.create();
	}
}