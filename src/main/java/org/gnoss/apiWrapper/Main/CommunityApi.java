package org.gnoss.apiWrapper.Main;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.AddSearchToCacheModel;
import org.gnoss.apiWrapper.ApiModel.AddToCacheModel;
import org.gnoss.apiWrapper.ApiModel.CategoryNames;
import org.gnoss.apiWrapper.ApiModel.CertificationLevelModel;
import org.gnoss.apiWrapper.ApiModel.ChangeNameCommunityModel;
import org.gnoss.apiWrapper.ApiModel.CommunityCategoryModel;
import org.gnoss.apiWrapper.ApiModel.CommunityInfoModel;
import org.gnoss.apiWrapper.ApiModel.CommunityModel;
import org.gnoss.apiWrapper.ApiModel.ConfigurationModel;
import org.gnoss.apiWrapper.ApiModel.ConsultaCacheModel;
import org.gnoss.apiWrapper.ApiModel.CreateGroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.DeleteGroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.EditCommunityImageModel;
import org.gnoss.apiWrapper.ApiModel.ExtraRegisterData;
import org.gnoss.apiWrapper.ApiModel.GetTextByLanguageModel;
import org.gnoss.apiWrapper.ApiModel.GroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.GroupOrgCommunityModel;
import org.gnoss.apiWrapper.ApiModel.LinkParentCommunityModel;
import org.gnoss.apiWrapper.ApiModel.MemberModel;
import org.gnoss.apiWrapper.ApiModel.MembersGroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.TextosTraducidosIdioma;
import org.gnoss.apiWrapper.ApiModel.ThesaurusCategory;
import org.gnoss.apiWrapper.ApiModel.ThesaurusModel;
import org.gnoss.apiWrapper.ApiModel.UploadContentModel;
import org.gnoss.apiWrapper.ApiModel.UserCommunity;
import org.gnoss.apiWrapper.ApiModel.UserCommunityModel;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Wrapper for GNOSS community API
 * @author salopez
 */
public class CommunityApi extends GnossApiWrapper{
    
    //Members
    private ArrayList<ThesaurusCategory> _communityCategories;
    //private OAuthInfo _oauth = null;
    //private ILogHelper _logHelper;
    
    //Properties
    private ArrayList<ThesaurusCategory> CommunityCategories;
    //private String CommunityShortName;  
    //private String ApiUrl;
    //private OAuthInfo OAuthInstance;
    
    
    //Constructors
    
    /**
     * Constructor of CommunityApi
     * @param oauth OAuth information to sign the Api requests
     * @param communityShortName Community short name which you want to use the API
     */
    public CommunityApi(OAuthInfo oauth, String communityShortName){
        super(oauth, communityShortName);
    }
    
    /**
     * Constructor of CommunityApi
     * @param configFilePath Configuration file path
     * @throws GnossAPIException exception
     * @throws ParserConfigurationException exception
     * @throws SAXException exception
     * @throws IOException exception
     */
    public CommunityApi(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException{
        super(configFilePath);
    }


    
    //Public methods
    
    /**
     * Load categories
     * @param community_short_name Community short name
     * @return List of categories
     * @throws MalformedURLException exception
     * @throws IOException exception
     * @throws GnossAPIException exception
     */
    public ArrayList<ThesaurusCategory> loadCategories(String community_short_name) throws MalformedURLException, IOException, GnossAPIException{
    	String url = _oauth.getApiUrl() + "/community/get-categories?community_short_name=" + community_short_name;
    	
    	String response = webRequest("GET", url, "", "", "application/json");
    	Gson gson = new Gson();
    	Type listType = new TypeToken<ArrayList<ThesaurusCategory>>(){}.getType();
    	ArrayList<ThesaurusCategory> communityCategoriesWithoutHierarchy = gson.fromJson(response, listType);
    	
    	LogHelper.getInstance().debug("Loaded the categories of the community " + community_short_name);
    	loadChildrenCategories(communityCategoriesWithoutHierarchy);
    	
    	return communityCategoriesWithoutHierarchy;
    }
    
    
    //Private methods	
   /**
    * Load children categories in their parent category
    * @param categories List of categories to load
    */
    private void loadChildrenCategories(ArrayList<ThesaurusCategory> categories){
    	for(ThesaurusCategory category : categories){
    		if(!category.getParent_category_id().equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))){
    			UUID parentCategoryID = category.getParent_category_id();
    			for(ThesaurusCategory subCategory : categories){
    				if(subCategory.getCategory_id().equals(parentCategoryID)){
    					subCategory.getChildren().add(category);
    				}
    			}
    		}
    	}
    }

    public ArrayList<ThesaurusCategory> getCommunityCategories() throws MalformedURLException, IOException, GnossAPIException {
        if(_communityCategories == null){
            _communityCategories = loadCategories(CommunityShortName);
        }
        return _communityCategories;
    }
    
    public String getCommunityShortName(){
    	return CommunityShortName;
    }

    public void setCommunityShortName(String communityShortName){
    	this.CommunityShortName = communityShortName;
    }
    
    public void setCommunityCategories(ArrayList<ThesaurusCategory> CommunityCategories) {
        _communityCategories = CommunityCategories;
    }
    
    public OAuthInfo getOAuthInstance(){
    	return _oauth;
    }
    
    public void setOAuthInstance(OAuthInfo oAuth){
    	this._oauth = oAuth;
    	loadApi();
    }
    
    @Override
    protected void loadApi() {
    	if(this._communityCategories!=null) {
    		try {
				this._communityCategories=loadCategories(CommunityShortName);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (GnossAPIException e) {
				e.printStackTrace();
			}
    	}
    }
    
    
    /**
     * Create community
     * @param communityName Community name 
     * @param communityShortName Short name of the community
     * @param description Description of the community
     * @param tagList Tags of the community
     * @param type Type of the community
     * @param accessType Access type of the community
     * @param parentCommunityShortName Parent community short name 
     * @param administratorUserId Admin ID of the community
     * @param organizationShortName Admin organization short name of the community
     * @param communityDefaultLanguage Default language of the community
     * @param logo Logo of the community
     * @param domain Domain of the community
     * @throws Exception exception 
     */
    public void createCommunity(String communityName, String communityShortName, String description, List<String> tagList, int type, int accessType, String parentCommunityShortName, UUID administratorUserId, String organizationShortName, String communityDefaultLanguage, byte[] logo, String domain) throws Exception {
    	
    	try {
			String tags = URLEncoder.encode(String.join(",", tagList), "UTF-8");
			CommunityModel community = new CommunityModel();
			{
				community.setCommunity_name(communityName);
				community.setCommunity_short_name(communityShortName);
				community.setCommunity_default_language(communityDefaultLanguage != null ? communityDefaultLanguage : "es");
				community.setDescription(description);
				community.setTags(tags);
				community.setType(type);
				community.setAccess_type(accessType);
				community.setParent_community_short_name(parentCommunityShortName);
				community.setAdmin_id(administratorUserId);
				community.setOrganization_short_name(organizationShortName);
				community.setLogo(logo);
				community.setDomain(domain);
				
				createCommunity(community);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

    /**
     * Create community with default language "es"
     * @param communityName Community name 
     * @param communityShortName Short name of the community
     * @param description Description of the community
     * @param tagList Tags of the community
     * @param type Type of the community
     * @param accessType Access type of the community
     * @param parentCommunityShortName Parent community short name 
     * @param administratorUserId Admin ID of the community
     * @param organizationShortName Admin organization short name of the community
     * @param logo Logo of the community
     * @throws Exception exception 
     */
    public void createCommunity(String communityName, String communityShortName, String description, List<String> tagList, int type, int accessType, String parentCommunityShortName, UUID administratorUserId, String organizationShortName, byte[] logo) throws Exception {
        createCommunity(communityName, communityShortName, description, tagList, type, accessType, parentCommunityShortName, administratorUserId, organizationShortName, "es", logo, null);
    }

    /**
     * Modify Logo
     * @param pEditCommunityImageModel Logo of the community and community name
     * @throws Exception exception
     */
    public void modifyLogo(EditCommunityImageModel pEditCommunityImageModel) throws Exception {
        String json = null;
        try {
            String url = getApiUrl() + "/community/modify-community-image";
            webRequestPostWithJsonObject(url, pEditCommunityImageModel);
            this._logHelper.debug("Modify Logo " + json);
        } catch (Exception ex) {
            this._logHelper.error("Error Modify Logo " + json + ": \r\n" + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Create a community
     * @param communityShortName Short name of the community
     * @param parentCommunityShortName Parent Community short name of the community
     * @param administratorUserId Admin ID of the community
     * @throws Exception exception 
     */
    public void vincularComunidadPadre(String communityShortName, String parentCommunityShortName, UUID administratorUserId) throws Exception {
    	LinkParentCommunityModel linkparent= new LinkParentCommunityModel();
    	{
    		linkparent.setParent_community_short_name(parentCommunityShortName);
    		linkparent.setShort_name(communityShortName);
    		linkparent.setAdmin_id(administratorUserId);
    		
    		linkParentCommunity(linkparent);
    	}
    }
    
    /**
     * Create a category
     * @param categoryName Category name
     * @param parentCategoryID Parent category ID
     * @param categoryImage Category image
     * @return UUID Category identifier
     * @throws Exception exception
     */
    public UUID createCategory(String categoryName, UUID parentCategoryID, byte[] categoryImage) throws Exception {
    	UUID uuid = null;
    	try {
    		CommunityCategoryModel communityModel = new CommunityCategoryModel();
    		{
    			communityModel.setCategory_name(categoryName);
    			communityModel.setCommunity_short_name(CommunityShortName);
    			communityModel.setParent_category_id(parentCategoryID);
    			communityModel.setCategory_image(categoryImage);
    		}
    		
    		String url = getApiUrl() + "/community/create-category";
    		
    		this._logHelper.fatal("Inicio llamada 1." + communityModel.getCategory_name() + " | 2." + communityModel.getCommunity_short_name() + " | 3." + communityModel.getParent_category_id());
    		
    		String response = webRequestPostWithJsonObject(url, communityModel);
    		
    		Gson gson = new Gson();
			return uuid = gson.fromJson(response, UUID.class);
    		
    	} catch(Exception ex) {
    		this._logHelper.error("Error creating category " + categoryName + ":\r\n" + ex.getMessage());
    		throw ex;
    	}
    }

    /**
     * Create a category without image
     * @param categoryName Category name
     * @param parentCategoryID Parent category ID
     * @return UUID Category identifier
     * @throws Exception exception
     */
    public UUID createCategory(String categoryName, UUID parentCategoryID) throws Exception {
        return createCategory(categoryName, parentCategoryID, null);
    }

    /**
     * Upload content File 
     * @param pmodel model 
     * @throws Exception exception 
     */
    public void uploadContentFile(UploadContentModel pmodel) throws Exception {
    	try {
    		String url = getApiUrl() + "/community/uploaded-content-file";
    		webRequestPostWithJsonObject(url, pmodel);
    		this._logHelper.debug("File upload");
    		
    	} catch(Exception ex) {
    		this._logHelper.error("Error File update");
    		throw ex;
    	}
    }
    
    /**
     * Create community
     * @param communityModel Community model
     * @throws Exception exception 
     */
    public void createCommunity(CommunityModel communityModel) throws Exception {
    	String json = null;
    	try {
    		String url = getApiUrl() + "/community/create-community";
    		webRequestPostWithJsonObject(url, communityModel);
    		this._logHelper.debug("Community created " + json);
    		
    	} catch(Exception ex) {
    		this._logHelper.error("Error creating community" + json + ":\r\n" + ex.getMessage());
    		throw ex;
    	}
    }
    
    
    /**
     * Create community
     * @param linkparent Community model
     * @throws Exception exception 
     */
	private void linkParentCommunity(LinkParentCommunityModel linkparent) throws Exception {
		String json = null;
		try {
			String url = getApiUrl() + "/community/link-parent-community";
			webRequestPostWithJsonObject(url, linkparent);
			this._logHelper.debug("Community created " + json);
		} catch(Exception ex) {
			this._logHelper.error("Error creating community " + json + ":\r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Get the data a user email by user email and the community short name
	 * @param number_resources boolean to return number of resources
	 * @param number_comments boolean to return number of comments
	 * @param groups boolean to return groups
	 * @param pFechaInit date initial
	 * @param pFechaFin date end
	 * @return list list of UserCommunity 
	 * @throws Exception exception 
	 */
	public List<UserCommunity> getUsersByCommunityShortName(boolean number_resources, boolean number_comments, boolean groups, Date pFechaInit, Date pFechaFin) throws Exception{
		String json = null;
		List<UserCommunity> lista = null;
		try {
			long fechainit = pFechaInit != null ? pFechaInit.getTime() : 0;
			long fechafin = pFechaFin != null ? pFechaFin.getTime() : 0;
			
			String url = getApiUrl() + "/community/get-users-by-community-short-name?community_short_name=" + CommunityShortName + "&number_resources=" + number_resources + "&number_comments=" + number_comments + "&groups=" + groups + "&pFechaInit=" + fechainit + "&pFechaFin=" + fechafin;   
			String response = webRequest("GET", url);
			
			Gson gson = new Gson();
			Type tipo = new TypeToken<ArrayList<UserCommunity>>(){}.getType();
			return lista = gson.fromJson(response, tipo);
			
		} catch(Exception ex) {
			this._logHelper.error("Error creating community " + json + ":\r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Create a thesaurus for a community
	 * @return String Thesaurus to create
	 * @throws Exception exception 
	 */
	public String getThesaurus() throws Exception {
		
		try {
			String url = getApiUrl() + "/community/get-thesaurus?community_short_name=" + CommunityShortName;
			String response = webRequest("GET", url);
			 if (response != null) {
		            response = response.trim();
		            if (response.startsWith("\"") && response.endsWith("\"")) {
		                response = response.substring(1, response.length() - 1);
		            }
		        }
			return response;
			
		} catch(Exception ex) {
			this._logHelper.error("Error getting the thesaurus from " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Create thesaurus for a community
	 * @param thesaurusXml Thesaurus to create
	 * @throws Exception exception
	 */
	public void createThesaurus(String thesaurusXml) throws Exception {
		try {
			String url = getApiUrl() + "/community/create-thesaurus";
			ThesaurusModel model = new ThesaurusModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setThesaurus(thesaurusXml);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("Thesaurus created in " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error creating thesaurus " + thesaurusXml + " in " + CommunityShortName + ":\r\n" + ex.getMessage());
			throw ex;
		}
	}
	

	/**
	 * Open an existing community
	 * @throws Exception exception 
	 */
	public void openCommunity() throws Exception {
		try {
			String url = getApiUrl() + "/community/open-community";
			webRequestPostWithJsonObject(url, CommunityShortName);
			this._logHelper.debug("Community opened " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error opening community " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
    
	/**
	 * Config graph of community
	 * @throws Exception exception
	 */
	public void configGraphCommunity() throws Exception {
		try {
			String url = getApiUrl() + "/community/config-graph-community";
			webRequestPostWithJsonObject(url, CommunityShortName);
			this._logHelper.debug("Config graph community " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error graph community " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Upload the community settings
	 * @param settingsXml Community settings in XML format
	 * @throws Exception exception
	 */
	public void uploadConfiguration(String settingsXml) throws Exception {
		try {
			String url = getApiUrl() + "/community/upload-configuration-xml";
			ConfigurationModel model = new ConfigurationModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setSettings(settingsXml);
				
				webRequestPostWithJsonObject(url, model);
				this._logHelper.debug("Settings uploaded succesfully to " + CommunityShortName);
			}
			
		} catch(Exception ex) {
			this._logHelper.error("Error uploading settings to community " + CommunityShortName + settingsXml + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Upload CMS community settings
	 * @param settingsCmsXml Community CMS settings in XML format
	 * @throws Exception exception
	 */
	public void uploadCMSConfiguration(String settingsCmsXml) throws Exception {
		try {
			String url = getApiUrl() + "/community/upload-cms-configuration-xml";
			ConfigurationModel model = new ConfigurationModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setSettings(settingsCmsXml);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("CMS settings uploaded succesfully to " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error uploading the CMS settings to community " + CommunityShortName + " " + settingsCmsXml + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Register a user in a community
	 * @param userID User identifier that we will register in the community
	 * @param organizationShortName Short name of the user organization
	 * @param identityType Type of user identity in the community
	 * @throws Exception exception
	 */
	public void addMember(UUID userID, String organizationShortName, int identityType) throws Exception {
		try {
			String url = getApiUrl() + "/community/add-member";
			
			MemberModel model = new MemberModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userID);
				model.setIdentity_type(identityType);
				model.setOrganization_short_name(organizationShortName);
			}
			
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("user " + userID + " added to " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error adding member " + userID + " to " + CommunityShortName);
			throw ex;
		}
	}

	/**
	 * Register a user in a community with default values
	 * @param userID User identifier that we will register in the community
	 * @throws Exception exception
	 */
	public void addMember(UUID userID) throws Exception {
		addMember(userID, null, 0);
	}
	
	
	/**
	 * Delete a user from a Community
	 * @param userID User identifier that we will delete from the community
	 * @throws Exception exception
	 */
	public void deleteMember(UUID userID) throws Exception {
		try {
			String url = getApiUrl() + "/community/delete-member";
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userID);
			}
			
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The member " + userID + " deleted from " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error deleting member " + userID + " from " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Register the users of a organization group in a community
	 * @param organizationShortName Short name of the user organization
	 * @param groupShortName Short name of the group
	 * @param identityType Type of user identity in the community
	 * @throws Exception exception 
	 */
	public void addMemberOrganizationGroupToCommunity(String organizationShortName, String groupShortName, int identityType) throws Exception {
		try {
			String url = getApiUrl() + "/community/add-members-organization-group-to-community";
			
			GroupOrgCommunityModel model = new GroupOrgCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setOrganization_short_name(organizationShortName);
				model.setGroup_short_name(groupShortName);
				model.setIdentity_type(identityType);
			}
			
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The group " + groupShortName + " of " + organizationShortName + " has been added to " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error adding the group " + groupShortName + " of " + organizationShortName + " from " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete the users of a organization group in a company
	 * @param organizationShortName Short name of the group
	 * @param groupShortName Short name of the organization
	 * @throws Exception exception
	 */
	public void deleteMemberOrganizationGroupFromCommunity(String organizationShortName, String groupShortName) throws Exception {
		try {
			String url = getApiUrl() + "/community/delete-group";
			
			GroupOrgCommunityModel model = new GroupOrgCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setOrganization_short_name(organizationShortName);
				model.setGroup_short_name(groupShortName);
			}
			
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("All the members from the group " + groupShortName + " of " + organizationShortName + " has been deleteed to " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error deleting the group members " + groupShortName + " of " + organizationShortName + " from " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Upgrade a user changing is role to community administrator
	 * @param userId user id
	 * @throws Exception exception
	 */
	public void upgradeMemberToAdministrator(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/community/add-administrator-member";
			UserCommunityModel model = new UserCommunityModel(); 
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userId);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The member " + userId + " has been upgraded to administrator of " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error upgrading member " + userId + " to administrator in " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Add the users of a organization group as administrator in a company
	 * @param organizationShortName Short name of the user organization
	 * @param groupShortName Short name of the group
	 * @throws Exception exception
	 */
	public void upgradeMembersOrganizationGroupToAdministrators(String organizationShortName, String groupShortName) throws Exception {
		try {
			String url = getApiUrl() + "/community/add-administrator-group";
			GroupOrgCommunityModel model = new GroupOrgCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setOrganization_short_name(organizationShortName);
				model.setGroup_short_name(groupShortName);
			}
			
			String result = webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("All the members from the group " + groupShortName + " of " + organizationShortName + " has been upgraded to administrator in " + CommunityShortName + ". +\r\n" + result);
		
		} catch(Exception ex) {
			this._logHelper.error("Error upgrading to administrator the members from the group " + groupShortName + " of " + organizationShortName + " in " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Add a group to the community
	 * @param groupName name of the group
	 * @param groupShortName Short name of the group
	 * @param description Description of the group
	 * @param tags Tags of the group
	 * @param members List uses that want to add
	 * @param sendNotification It indicates whether an email is going to be sent to users telling them that has been added to the group
	 * @throws Exception exception 
	 */
	public void createCommunityGroup(String groupName, String groupShortName, String description, List<String> tags, List<UUID> members, boolean sendNotification) throws Exception {
		try {
			String url = getApiUrl() + "/community/create-community-group";
			CreateGroupCommunityModel model = new CreateGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_name(groupName);
				model.setGroup_short_name(groupShortName);
				model.setTags(tags);
				model.setMembers(members);
				model.setDescription(description);
				model.setSend_notification(sendNotification);
			}
			
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("Group " + groupName + " created in " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error creating group " + groupName + " in " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Delete a community group from the community
	 * @param groupShortName Short name of the community group
	 * @throws Exception exception
	 */
	public void deleteCommunityGroup(String groupShortName) throws Exception {
		try {
			String url = getApiUrl() + "/community/delete-community-group";
			DeleteGroupCommunityModel model = new DeleteGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_short_name(groupShortName);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("Group " + groupShortName + " deleted in " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error deleting group " + groupShortName + " in " + CommunityShortName + ": " + ex.getMessage());
			throw ex; 
		}
	}
	
	
	/**
	 * Add a list of users to a community group
	 * @param groupShortName Short name of the group
	 * @param members List users that want to add
	 * @param sendNotification It indicates whatever a message is going to be sent to users telling them has been added to the group
	 * @throws Exception exception 
	 */
	public void addMembersToGroup(String groupShortName, List<UUID> members, boolean sendNotification) throws Exception {
		String miembros = "";
		
		try {
			String url = getApiUrl() + "/community/add-members-to-community-group";
			MembersGroupCommunityModel model = new MembersGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_short_name(groupShortName);
				model.setMembers(members);
				model.setSend_notification(sendNotification);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The users has been added to the group " + groupShortName + " of the community " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error adding users " + (String.join(",", members.toString())) + " to group " + groupShortName + " of the community " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Create a list of users to a community group
	 * @param certificationLevelsDescription certification level description
	 * @param certificationPolitics Certification politics
	 * @throws Exception exception 
	 */
	public void createCertificationLevels(List<String> certificationLevelsDescription, String certificationPolitics) throws Exception {
		String miembros = "";
		try {
			String url = getApiUrl() + "/community/create-certification-levels";
			CertificationLevelModel model = new CertificationLevelModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setCertification_levels(certificationLevelsDescription);
				model.setCertification_politics(certificationPolitics);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The certification levels has been added to the community " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error creating certification levels of the community " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a list of users from a community group
	 * @param groupShortName Short name of the group
	 * @param members List users that want to add
	 * @throws Exception exception 
	 */
	public void deleteMembersFromGroup(String groupShortName, List<UUID> members) throws Exception {
		try {
			String url = getApiUrl() + "/community/delete-members-of-community-group";
			MembersGroupCommunityModel model = new MembersGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_short_name(groupShortName);
				model.setMembers(members);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The users has been deleted from the group " + groupShortName + " of the community " + CommunityShortName);
			
		} catch(Exception ex) {
			this._logHelper.error("Error deleting users " + (String.join(",", members.toString())) + " from group " + groupShortName + " of the community " + CommunityShortName + " : \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets the members of a company group
	 * @param groupShortName Short name of the group
	 * @return List of members identifiers of the community group
	 * @throws Exception exception 
	 */
	public List<UUID> getGroupMembers(String groupShortName) throws Exception {
		List<UUID> members = null;
		try {
			String url = getApiUrl() + "/community/get-members-from-community-group?community_short_name=" + CommunityShortName + "&group_short_name=" + groupShortName;
			String response = webRequest("GET", url);
			Gson gson = new Gson();
			Type tipo = new TypeToken<ArrayList<UUID>>(){}.getType();
			members = gson.fromJson(response, tipo);
			this._logHelper.debug("Users obtained from group " + groupShortName + " of the community " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error obtaining users from group " + groupShortName + " of the community " + CommunityShortName + ": \r\n " + ex.getMessage());
			throw ex;
		}
		return members;
	}
	
	/**
	 * Gets the members of an organization group
	 * @param organizationShortName Organization short name of the user
	 * @param groupShortName Short name of the group 
	 * @return List of members identifiers of the organization group
	 * @throws Exception exception 
	 */
	public List<UUID> getOrganizationGroupMembers(String organizationShortName, String groupShortName) throws Exception{
		List<UUID> members = null;
		try {
			String url = getApiUrl() + "/community/get-members-organization-group?organization_short_name=" + organizationShortName + "&group_short_name=" + groupShortName;
			String response = webRequest("GET", url);
			Gson gson = new Gson();
			Type tipo = new TypeToken<ArrayList<UUID>>(){}.getType();
			members = gson.fromJson(response, tipo);
			this._logHelper.debug("Users obtained from group " + groupShortName + "of the organization " + organizationShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error obtaining users from group " + groupShortName + " of the organization " + organizationShortName + ": \r\n " + ex.getMessage());
			throw ex;
		}
		return members;
	}
	
	/**
	 * Expel a user from a community
	 * @param userId User identifier to expel from the community
	 * @throws Exception exception
	 */
	public void expelMember(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/community/expel-member";
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userId);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("User " + userId + " expelled from " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error expelling member " + userId + " from " + CommunityShortName + ": \r\n " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Close a community
	 * @throws Exception exception
	 */
	public void closeCommunity() throws Exception {
		try {
			String url = getApiUrl() + "/community/close-community";
			webRequestPostWithJsonObject(url, CommunityShortName);
			this._logHelper.debug("Community " + CommunityShortName + " closed");
		} catch(Exception ex) {
			this._logHelper.error("Error closing " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Change the community name 
	 * @param newName new Community name 
	 * @throws Exception exception 
	 */
	public void changeCommunityName(String newName) throws Exception {
		try {
			String url = getApiUrl() + "/community/change-community-name";
			ChangeNameCommunityModel model = new ChangeNameCommunityModel();
			{
				model.setCommunity_name(newName);
				model.setCommunity_short_name(CommunityShortName);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("Community name of " + CommunityShortName + " changed to " + newName);
		} catch(Exception ex) {
			this._logHelper.error("Error changing name of " + CommunityShortName + " to " + newName + ": " + ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets a list with the groups of the community
	 * @return List with the groups of the community
	 * @throws Exception exception
	 */
	public List<GroupCommunityModel> getCommunityGroups() throws Exception{
		List<GroupCommunityModel> groups = null;
		try {
			String url = getApiUrl() + "/community/get-community-groups?community_short_name=" + CommunityShortName;
			String response = webRequest("GET", url);
			Gson gson = new Gson();
			Type tipo = new TypeToken<ArrayList<GroupCommunityModel>>(){}.getType();
			groups = gson.fromJson(response, tipo);
			this._logHelper.debug("Groups obteined from " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error obtaining groups from " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
		return groups;
	}
	
	
	/**
	 * Gets the organization name of a user in a community
	 * @param userId User identifier
	 * @return Organization name of a user in a community
	 * @throws Exception exception
	 */
	public String getOrganizationShortNameFromMember(String userId) throws Exception {
		String organizationShortName = null;
		try {
			String url = getApiUrl() + "/community/get-organization-short-name-from-member?community_short_name=" + CommunityShortName + "&user_id=" + userId;
			organizationShortName = webRequest("GET", url);
			if(organizationShortName != null) {
				organizationShortName = organizationShortName.trim().replaceAll("^\"|\"$", "");
			}
			this._logHelper.debug("Organization short name obtained from " + userId + " in " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error obtaining the organization short name from " + userId + " in " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
		return organizationShortName;
	}
	
	
	/**
	 * Get the extra register data of a community
	 * @return List of ExtraRegisterData
	 * @throws Exception exception
	 */
	public List<ExtraRegisterData> getExtraRegisterData() throws Exception{
		List<ExtraRegisterData> extraRegisterData = null;
		try {
			String url = getApiUrl() + "/community/get-extra-register-data?community_short_name=" + CommunityShortName;
			String response = webRequest("GET", url);
			Gson gson = new Gson();
			Type tipo = new TypeToken<ArrayList<ExtraRegisterData>>(){}.getType();
			extraRegisterData = gson.fromJson(response, tipo);
			this._logHelper.debug("Extra register data obtained from " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error obtaining extra register data from " + CommunityShortName + ": \r\n" + ex.getMessage());
			throw ex;
		}
		return extraRegisterData;
	}
	
	/**
	 * Gets the community main language. Empty if it is not defined
	 * @return String Community main language
	 */
	public String getCommunityMainLanguage() {
		try {
			String url = getApiUrl() + "/community/get-main-language?community_short_name=" + CommunityShortName;
			String response = webRequest("GET", url, "application/json");
			if(response != null) {
				response = response.trim().replaceAll("^\"|\"$", "");
			}
			return response;
		} catch(Exception ex) {
			this._logHelper.error("Imposible to obtain the main language");
			return null;
		}
	}
	
	/**
	 * Get the person identifier and his email from the community members
	 * @return Dictionary with the person identifier and his email
	 */
	public Map<UUID, String> getCommunityPersonIDEmail(){
		try {
			String url = getApiUrl() + "/community/get-community-personid-email?community_short_name=" + CommunityShortName;
			String response = webRequest("GET", url, "application/json");
			Gson gson = new Gson();
			Type tipo = new TypeToken<HashMap<UUID, String>>(){}.getType();
			return gson.fromJson(response, tipo);
			
		} catch(Exception ex) {
			this._logHelper.error("The person identifiers and emails of the community members '" + CommunityShortName + "' could not be obtained");
			return null;
		}
	}
	/**
	 * Check if a load identifier is already registered
	 * @param communityID Identifier of the community
	 * @param organizationID Identifier of the organization
	 * @return True if the load identifier is already registered
	 */
	public boolean refreshHeavyCache(UUID communityID, UUID organizationID) {
	    try {
	        String url = getApiUrl() + "/community/refresh-heavy-cache?community_id=" + communityID + 
	                     "&organization_id=" + organizationID;
	        
	        webRequestPostWithJsonObject(url, "");
	        
	        _logHelper.trace("community " + communityID + ". Organization: " + organizationID);
	        
	        return true;
	    } catch(Exception ex) {
	        _logHelper.error(ex.getMessage());
	        return false;
	    }
	}
	/**
	 * Get translations for a community
	 * @param community_id Community identifier
	 * @param community_short_name Community short name
	 * @param language Language
	 * @return List of TextosTraducidosIdiomas
	 */
	public List<TextosTraducidosIdioma> getTranslations(UUID community_id, String community_short_name, String language) {
		try {
			String url = getApiUrl() + "/community/get-language-translations?community_id=" + community_id + "&community_short_name=" + community_short_name + "&language=" + language;
			String response = webRequest("GET", url, "application/json");
			Gson gson = new Gson();
	        Type listType = new TypeToken<ArrayList<TextosTraducidosIdioma>>(){}.getType();
	        return gson.fromJson(response, listType);
		} catch(Exception ex) {
			this._logHelper.error("The proyect could not be obtained");
			return null;
		}
	}

	/**
	 * Get a specific translation for a community
	 * @param community_id Community identifier
	 * @param community_short_name Community short name
	 * @param language Language
	 * @param text_id Text identifier
	 * @return Translation string
	 */
	public String getTranslation(UUID community_id, String community_short_name, String language, String text_id) {
		try {
			String url = getApiUrl() + "/community/get-language-translation?community_id=" + community_id + "&community_short_name=" + community_short_name + "&language=" + language + "&text_id=" + text_id;
			String response = webRequest("GET", url, "application/json");
			// Eliminar comillas dobles al inicio y final si existen
	        if (response != null) {
	            response = response.trim();
	            if (response.startsWith("\"") && response.endsWith("\"")) {
	                response = response.substring(1, response.length() - 1);
	            }
	        }
	        
	        return response;
		} catch(Exception ex) {
			this._logHelper.error("The proyect could not be obtained");
			return null;
		}
	}
	
	
	/**
	 * Gets the community identifier
	 * @return UUID UUID ID 
	 * @throws Exception exception 
	 */
	public UUID getCommunityId() throws Exception {
		try {
			String url = getApiUrl() + "/community/get-community-id?community_short_name=" + URLEncoder.encode(CommunityShortName, StandardCharsets.UTF_8);
			String response = webRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, UUID.class); 
		} catch(Exception ex) {
			this._logHelper.error("The community " + CommunityShortName + " could not be found");
			throw ex;
		}
	}
	
	/**
	 * Blocks a member in a community
	 * @param userId User's identifier 
	 * @throws Exception exception
	 */
	public void blockMember(UUID userId) throws Exception {
		try {
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setUser_id(userId);
				model.setCommunity_short_name(CommunityShortName);
			}
			String url = getApiUrl() + "/community/block-member";
			webRequestPostWithJsonObject(url, model);
		} catch(Exception ex) {
			this._logHelper.error("The user " + userId + " of the community members '" + CommunityShortName + "' could not be blocked");
			throw ex;
		}
	}
	
	
	/**
	 * Unblocks a member in a community
	 * @param userId User's identifier
	 * @throws Exception exception 
	 */
	public void unblockMember(UUID userId) throws Exception {
		try {
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setUser_id(userId);
				model.setCommunity_short_name(CommunityShortName);
			}
			String url = getApiUrl() + "/community/unblock-member";
			webRequestPostWithJsonObject(url, model);
		} catch(Exception ex) {
			this._logHelper.error("The user " + userId + " of the community members '" + CommunityShortName + "' could not be unblocked");
			throw ex;
		}
	}
	
	/**
	 * Refresh the cache of a CMS component 
	 * @param componentId Component id to refresh
	 * @throws Exception exception 
	 */
	public void refreshCMSComponent(UUID componentId) throws Exception {
		try {
			String url = getApiUrl() + "/community/refresh-cms-component?component_id=" + componentId.toString() + "&community_short_name=" + URLEncoder.encode(CommunityShortName, StandardCharsets.UTF_8);
			webRequest("POST", url);
		} catch(Exception ex) {
			this._logHelper.error("The component id " + componentId + " of the community '" + CommunityShortName + "' could not be refreshed");
			throw ex;
		}
	}
	
	
	/**
	 * Refresh the cache of all community's CMS components 
	 * @throws Exception exception 
	 */
	public void refreshAllCMSComponents() throws Exception {
		try {
			String url = getApiUrl() + "/community/refresh-all-cms-components?community_short_name=" + URLEncoder.encode(CommunityShortName, StandardCharsets.UTF_8);
			webRequest("POST", url);
		} catch(Exception ex) {
			this._logHelper.error("The components of the community '" + CommunityShortName + "' could not be refreshed");
			throw ex;
		}
	}
	
	
	/**
	 * Gets the basic information of a community
	 * @return CommunityInfoModel Community info model 
	 * @throws Exception Exception 
	 */
	public CommunityInfoModel getCommunityInfo() throws Exception {
		try {
			String url = getApiUrl() + "/community/get-community-information?community_short_name=" + URLEncoder.encode(CommunityShortName, StandardCharsets.UTF_8);
			String response = webRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, CommunityInfoModel.class); 
		} catch(Exception ex) {
			this._logHelper.error("The community " + CommunityShortName + " could not be found");
			throw ex;
		}
	}
	
	/**
	 * Gets the basic information of a community
	 * @param communityId Community identifier
	 * @return CommunityInfoModel Community Info model 
	 * @throws Exception Exception 
	 */
	public CommunityInfoModel getCommunityInfo(UUID communityId) throws Exception {
		try {
			String url = getApiUrl() + "/community/get-community-information?community_id=" + communityId;
			String response = webRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, CommunityInfoModel.class); 
		} catch(Exception ex) {
			this._logHelper.error("The community " + communityId + " could not be found");
			throw ex;
		}
	}
	
	
	/**
	 * Gets the name of a category in all of the languages that has been defined
	 * @param categoryId Category identifier
	 * @return CategoryNames category names 
	 * @throws Exception Exception 
	 */
	public CategoryNames getCategoryName(UUID categoryId) throws Exception {
		try {
			String url = getApiUrl() + "/community/get-category-name?category_id=" + categoryId;
			String response = webRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, CategoryNames.class); 
		} catch(Exception ex) {
			this._logHelper.error("The category " + categoryId + " could not be found");
			throw ex;
		}
	}
	
	/**
	 * Check is Admin community
	 * @param pShortName P short name 
	 * @param pUserID P user ID
	 * @return boolean T or F 
	 * @throws IOException IO Exception 
	 * @throws GnossAPIException GnossAPIException 
	 */
	public boolean checkIsAdminCommunity(String pShortName, UUID pUserID) throws IOException, GnossAPIException {
		UserCommunityModel model = new UserCommunityModel();
		model.setCommunity_short_name(pShortName);
		model.setUser_id(pUserID);
		String url = getApiUrl() + "/community/check-administrator-community";
		String response = webRequestPostWithJsonObject(url, model);
		Gson gson = new Gson();
		return gson.fromJson(response, Boolean.class); 
	}
	
	/**
	 * Downgrade a member from administrator
	 * @param userId User identifier
	 * @throws Exception exception
	 */
	public void downgradeMemberFromAdministrator(UUID userId) throws Exception {
		try {
			String url = getApiUrl() + "/community/delete-administrator-permission";
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userId);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The member " + userId + " has been downgraded from administrator of " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error downgrading member " + userId + " from administrator in " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Add the value sent to cache
	 * @param key Key to add to cache
	 * @param queryValue Value to add to cache
	 * @param duration Duration of the cache expiration in seconds
	 * @throws Exception exception
	 */
	public void addSearchToCache(String key, ConsultaCacheModel queryValue, double duration) throws Exception {
		try {
			String url = getApiUrl() + "/community/add-search-to-cache";
			AddSearchToCacheModel model = new AddSearchToCacheModel();
			{
				model.setKey(key);
				model.setValue(queryValue);
				model.setCommunity_short_name(CommunityShortName);
				model.setDuration(duration);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The value was added correctly to cache to the community: " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error adding the cache to " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Add the value sent to cache
	 * @param key Key to add to cache
	 * @param value Value to add to cache
	 * @param duration Duration of the cache expiration in seconds
	 * @throws Exception exception
	 */
	public void addToCache(String key, String value, double duration) throws Exception {
		try {
			String url = getApiUrl() + "/community/add-to-cache";
			AddToCacheModel model = new AddToCacheModel();
			{
				model.setKey(key);
				model.setValue(value);
				model.setCommunity_short_name(CommunityShortName);
				model.setDuration(duration);
			}
			webRequestPostWithJsonObject(url, model);
			this._logHelper.debug("The value was added correctly to cache to the community: " + CommunityShortName);
		} catch(Exception ex) {
			this._logHelper.error("Error adding the cache to " + CommunityShortName + ": " + ex.getMessage());
			throw ex;
		}
	}

	/**
	 * Get a text in other language
	 * @param language Language of the text
	 * @param textoID ID of the text
	 * @return String with the text in the specified language
	 * @throws Exception exception
	 */
	public String getTextByLanguage(String language, String textoID) throws Exception {
	    try {
	        String url = getApiUrl() + "/community/get-text-by-language?community_short_name=" + URLEncoder.encode(CommunityShortName, StandardCharsets.UTF_8) + "&language=" + URLEncoder.encode(language, StandardCharsets.UTF_8) + "&texto_id=" + URLEncoder.encode(textoID, StandardCharsets.UTF_8);
	        String response = webRequest("GET", url, "application/json");
	        if (response != null) {
	            response = response.trim().replaceAll("^\"|\"$", "");
	        }
	        return response;
	    } catch(Exception ex) {
	        this._logHelper.error("Error getting the text by the data given: " + ex.getMessage());
	        throw ex;
	    }
	}
}