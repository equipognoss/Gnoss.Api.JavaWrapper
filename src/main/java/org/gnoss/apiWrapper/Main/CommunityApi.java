package org.gnoss.apiWrapper.Main;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.CategoryNames;
import org.gnoss.apiWrapper.ApiModel.CertificationLevelModel;
import org.gnoss.apiWrapper.ApiModel.ChangeNameCommunityModel;
import org.gnoss.apiWrapper.ApiModel.CommunityCategoryModel;
import org.gnoss.apiWrapper.ApiModel.CommunityInfoModel;
import org.gnoss.apiWrapper.ApiModel.CommunityModel;
import org.gnoss.apiWrapper.ApiModel.ConfigurationModel;
import org.gnoss.apiWrapper.ApiModel.CreateGroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.DeleteGroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.ExtraRegisterData;
import org.gnoss.apiWrapper.ApiModel.GroupCommunityModel;
import org.gnoss.apiWrapper.ApiModel.GroupOrgCommunityModel;
import org.gnoss.apiWrapper.ApiModel.LinkParentCommunityModel;
import org.gnoss.apiWrapper.ApiModel.MemberModel;
import org.gnoss.apiWrapper.ApiModel.MembersGroupCommunityModel;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.gson.Gson;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salopez
 */
public class CommunityApi extends GnossApiWrapper{
    
    //Members
    private ArrayList<ThesaurusCategory> _communityCategories;
    private OAuthInfo _oauth = null;
    private ILogHelper _logHelper;
    
    //Properties
    private ArrayList<ThesaurusCategory> CommunityCategories;
    private String CommunityShortName;
    private String ApiUrl;
    private OAuthInfo OAuthInstance;
    
    
    
    //Public methods
    
    /**
     * Load categories
     * @param community_short_name Community short name
     * @return List of categories
     * @throws MalformedURLException
     * @throws IOException
     * @throws GnossAPIException
     */
    public ArrayList<ThesaurusCategory> LoadCategories(String community_short_name) throws MalformedURLException, IOException, GnossAPIException{
    	String url = _oauth.getApiUrl() + "/community/get-categories?community_short_name=" + community_short_name;
    	
    	String response = WebRequest("GET", url, "", "", "aplication0/json");
    	Gson gson = new Gson();
    	ArrayList<ThesaurusCategory> communityCategoriesWithoutHierarchy = gson.fromJson(response, new ArrayList<ThesaurusCategory>().getClass());
    	
    	LogHelper.getInstance().Debug("Loaded the categories of the community " + community_short_name);
    	LoadChildrenCategories(communityCategoriesWithoutHierarchy);
    	
    	return communityCategoriesWithoutHierarchy;
    }
    
    
    //Private methods	
   /**
    * Load children categories in their parent category
    * @param categories List of categories to load
    */
    private void LoadChildrenCategories(ArrayList<ThesaurusCategory> categories){
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
    
    public CommunityApi(OAuthInfo oauth, String communityShortName){
        super(oauth, communityShortName);
    }
    
    public CommunityApi(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException{
        super(configFilePath);
    }

    public ArrayList<ThesaurusCategory> getCommunityCategories() throws MalformedURLException, IOException, GnossAPIException {
        if(_communityCategories == null){
            _communityCategories = LoadCategories(CommunityShortName);
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
    	LoadApi();
    }
    
    @Override
    protected void LoadApi() {
    	if(this._communityCategories!=null) {
    		try {
				this._communityCategories=LoadCategories(CommunityShortName);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GnossAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    
    /**
     * Create community
     * @param communityName-> Community name 
     * @param communityShortName -> Short name of the community
     * @param description -> <description of the community
     * @param tagList -> Tags of the community
     * @param type -> Type of the community
     * @param accessType -> Access type of the community
     * @param parentCommunityShortName -> Parent community short name 
     * @param administratorUserId -> Admin ID of the community
     * @param organizationShortName -> Admin organization short name of the community
     * @param logo -> Logo of the community
     * @throws Exception 
     */
    public void CreateCommunity(String communityName, String communityShortName, String description, List<String> tagList, int type, int accessType, String parentCommunityShortName, UUID administratorUserId, String organizationShortName, byte[] logo ) throws Exception {
    	
    	try {
			String tags=URLEncoder.encode(String.join(",", tagList), "UTF-8");
			CommunityModel community = new CommunityModel();
			{
				community.setCommunity_name(communityName);
				community.setCommunity_short_name(communityShortName);
				community.setDescription(description);
				community.setTags(tags);
				community.setType(type);
				community.setAccess_type(accessType);
				community.setParent_community_short_name(parentCommunityShortName);
				community.setAdmin_id(administratorUserId);
				community.setOrganization_short_name(organizationShortName);
				community.setLogo(logo);
				
				CreateCommunity(community);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    /**
     * Create a community
     * @param communityShortName -> Short name of the community
     * @param parentCommunityShortName -> Parent Community short name of the community
     * @param administratorUserId -> Admin ID of the community
     * @throws Exception 
     */
    public void VincularComunidadPadre(String communityShortName, String parentCommunityShortName, UUID administratorUserId) throws Exception {
    	LinkParentCommunityModel linkparent= new LinkParentCommunityModel();
    	{
    		linkparent.setParent_community_short_name(parentCommunityShortName);
    		linkparent.setShort_name(communityShortName);
    		linkparent.setAdmin_id(administratorUserId);
    		
    		LinkParentCommunity(linkparent);
    	}
    }
    
    
    public UUID CreateCategory(String categoryName, String communityShortName, UUID parentCategoryID) throws Exception {
    	UUID uuid = null;
    	try {
    		CommunityCategoryModel communityModel= new CommunityCategoryModel();
    		{
    			communityModel.setCategory_name(categoryName);
    			communityModel.setCommunity_short_name(communityShortName);
    			communityModel.setParent_category_id(parentCategoryID);
    		}
    		
    		String url=getApiUrl()+"/community/create-category";
    		String response= WebRequestPostWithJsonObject(url, communityModel);
    		
    		Gson gson = new Gson();
			return uuid = gson.fromJson(response, UUID.class);
    		
    		
    	}catch(Exception ex) {
    		this._logHelper.Error("Error creating category "+categoryName+ ":\r\n"+ ex.getMessage());
    		throw ex;
    	}
    	
    }

    /**
     * 
     * @param pmodel
     * @throws Exception 
     */
    public void UploadContentFile(UploadContentModel pmodel) throws Exception {
    	try {
    		String url=getApiUrl()+"/community/uploadd-content-file";
    		WebRequestPostWithJsonObject(url, pmodel);
    		this._logHelper.Debug("File upload");
    		
    	}catch(Exception ex) {
    		this._logHelper.Error("Error File update");
    		throw ex;
    	}
    }
    
    /**
     * Create community
     * @param communityModel -> Community model
     * @throws Exception 
     */
    public void CreateCommunity(CommunityModel communityModel) throws Exception {
    	String json=null;
    	try {
    		String url=getApiUrl()+"/community/create-community";
    		WebRequestPostWithJsonObject(url, communityModel);
    		this._logHelper.Debug("Community created "+json);
    		
    	}catch(Exception ex) {
    		this._logHelper.Error("Error creating community" +json+ ":\r\n" + ex.getMessage());
    		throw ex;
    	}
    }
    
    
    /**
     * Create community
     * @param linkparent -> Community model
     * @throws Exception 
     */
	private void LinkParentCommunity(LinkParentCommunityModel linkparent) throws Exception {
		String json= null;
		try {
			String url= getApiUrl()+"/community/link-parent-community";
			WebRequestPostWithJsonObject(url, linkparent);
			this._logHelper.Debug("Community created "+json);
		}catch(Exception ex) {
			this._logHelper.Error("Error creating community " +json +":\r\n" +ex.getMessage());
			throw ex;
		}
		
	}
	
	
	/**
	 * Get the data a user email by user email and the community short name
	 * @param number_resources -> boolean to return number of resources
	 * @param number_comments -> boolean to return number of comments
	 * @param groups -> boolean to return groups
	 * @param pFechaInit -> date init
	 * @param pFechaFin -> date end
	 * @return
	 * @throws Exception 
	 */
	public List<UserCommunity> getUsersByCommunityShortName(boolean number_resources, boolean number_comments, boolean groups, Date pFechaInit, Date pFechaFin ) throws Exception{
		String json=null;
		List<UserCommunity> lista= null;
		try {
			long fechainit= pFechaInit!=null ? pFechaInit.getTime() : 0;
			long fechafin= pFechaFin!=null ? pFechaFin.getTime() : 0;
			
			String url= getApiUrl()+"/community/get-users-by-community-short-name?community_short_name="+CommunityShortName+"&number_resources="+number_resources+"&number_comments="+number_comments+"&groups="+groups+"&pFechaInit="+fechainit+"&pFechaFin="+fechafin;   
			String response=WebRequest("GET", url);
			
			Gson gson = new Gson();
			return lista=gson.fromJson(response,  (Type) new ArrayList<String>());
			
		}catch(Exception ex) {
			this._logHelper.Error("Error creating community "+json+ ":\r\n" +ex.getMessage());
			throw ex;
		}
		
	}
	
	
	/**
	 * Create a thesaurus for  a community
	 * @return  Thesaurus to create
	 * @throws Exception
	 */
	public String getThesaurus() throws Exception {
		
		try {
			String url=getApiUrl()+"/community/get-thesaurus?community_short_name="+CommunityShortName;
			String response= WebRequest("GET", url);
			Gson gson = new Gson();
			return gson.fromJson(response, String.class);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error getting the thesaurus from " +CommunityShortName+ ": \r\n" +ex.getMessage());
			throw ex;
		}
		
	}
	
	/**
	 * Create thesaurus for a community
	 * @param thesaurusXml -> Thesaurus to create
	 * @throws Exception
	 */
	public void CreateThesaurus(String thesaurusXml) throws Exception {
		try {
			String url=getApiUrl()+"/community/create-thesaurus";
			ThesaurusModel model= new ThesaurusModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setThesaurus(thesaurusXml);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Thesaurus created in "+CommunityShortName);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error creating thesaurus "+thesaurusXml+ " in "+ CommunityShortName +":\r\n" +ex.getMessage());
			throw ex;
		}
	}
	

	/**
	 * Open an existing community
	 * @param communityShortName
	 * @throws Exception
	 */
	public void OpenCommunity(String communityShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/open-community";
			
			if(url.isEmpty() || StringUtils.isBlank(url)) {
				communityShortName=CommunityShortName;
			}
			WebRequestPostWithJsonObject(url, communityShortName);
			this._logHelper.Debug("Community opened" +communityShortName);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error opening community "+communityShortName+ ": " +ex.getMessage());
			throw ex;
		}
	}
    
	/**
	 * Config graph of community
	 * @throws Exception 
	 */
	public void ConfigGraphCommunity() throws Exception {
		try {
			String url=getApiUrl()+"/community/config-graph-community";
			WebRequestPostWithJsonObject(url, CommunityCategories);
			this._logHelper.Debug("Config graph community "+CommunityShortName);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error graph community "+CommunityShortName+ ": "+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Upload the community settings
	 * @param settingsXml -> Community settings in XML format
	 * @throws Exception 
	 */
	public void UploadConfiguration(String settingsXml) throws Exception {
		try {
			String url=getApiUrl()+"/community/upload-configuration-xml";
			ConfigurationModel model= new ConfigurationModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setSettings(settingsXml);
				
				WebRequestPostWithJsonObject(url, model);
				this._logHelper.Debug("Settings uploaded succesfully to"+ CommunityShortName);
			}
			
		}catch(Exception ex) {
			this._logHelper.Error("Error uploading settings to community"+ CommunityShortName +settingsXml +": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Upload CMS community settings
	 * @param settingsCmsXml -> Community CMS settings in XML format
	 * @throws Exception
	 */
	public void UploadCMSConfiguration(String settingsCmsXml) throws Exception {
		try {
			String url=getApiUrl()+"/community/upload-cms-configuration-xml";
			ConfigurationModel model= new ConfigurationModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setSettings(settingsCmsXml);
				
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("CMS settings uploaded succesfully to "+CommunityShortName);
			
			
		}catch(Exception ex) {
			this._logHelper.Error("Error uploading the CMS settings to community "+CommunityShortName+ " "+ settingsCmsXml +": \r\n"+ex.getMessage());
			throw ex;
			
		}
	}
	
	
	/**
	 * Register a user in a community
	 * @param userID -> User identifier that we will register in the community
	 * @param organizationShortName -> Short name of the user organization
	 * @param identityType -> Type of user identity in the community
	 * @throws Exception
	 */
	public void AddMember(UUID userID, String organizationShortName, int identityType) throws Exception {
		try {
			String url=getApiUrl()+"/community/add-member";
			
			MemberModel model= new MemberModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userID);
				model.setIdentity_type(identityType);
				model.setOrganization_short_name(organizationShortName);
			}
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("user "+userID+ " added to "+CommunityShortName);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error adding member "+userID+ " to "+CommunityShortName);
			throw ex;
		}
	}
	
	
	/**
	 * Delete a user from a Community
	 * @param userID -> User identifier that we will delete from the community
	 * @throws Exception
	 */
	public void DeleteMember(UUID userID) throws Exception {
		try {
			String url=getApiUrl()+"/community/delete-member";
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userID);
				
			}
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The member "+userID+ " deleted from "+CommunityShortName);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error deleting member "+ userID+ " from "+CommunityShortName+ ": "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Register the users of a organization group in a community
	 * @param organizationShortName -> Short name of the user organization
	 * @param groupShortName -> Short name of the group
	 * @param identityType -> Type of user identity in the community
	 * @throws Exception
	 */
	public void AddMememberOrganizationGroupToCommunity(String organizationShortName, String groupShortName, int identityType) throws Exception {
		try {
			String url=getApiUrl()+"/community/add-members-organization-group-to-community";
			
			GroupCommunityModel model= new GroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setOrganization_short_name(organizationShortName);
				model.setGroup_short_name(groupShortName);
				model.setIdentity_type(identityType);
			}
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The group "+groupShortName+ " of "+organizationShortName+ " has been added to "+ CommunityShortName );
			
			
		}catch(Exception ex) {
			this._logHelper.Error("Error adding the group " +groupShortName+ " of "+organizationShortName+ " from " +CommunityShortName +": \r\n" + ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete the users of a organization group in a company
	 * @param organizationShortName -> Short name of the group
	 * @param groupShortName -> Short name of the organization
	 * @throws Exception 
	 */
	public void DeleteMemberOrganizationGroupFromCommunity(String organizationShortName, String groupShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/delete-group";
			
			GroupOrgCommunityModel model= new GroupOrgCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setOrganization_short_name(organizationShortName);
				model.setGroup_short_name(groupShortName);
			}
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("All the members from the group "+groupShortName+ " of "+organizationShortName+ " has been deleteed to "+CommunityShortName);
		}catch(Exception ex) {
			this._logHelper.Error("Error deleting the group members "+groupShortName+ " of "+organizationShortName + " from "+CommunityShortName+ ": "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Upgrade a user changing is role to community administrator
	 * @param userId
	 * @throws Exception 
	 */
	public void UpgradeMemberToAdministrator(UUID userId) throws Exception {
		
		try {
			String url=getApiUrl()+"/community/add-administrator-member";
			UserCommunityModel model = new UserCommunityModel(); 
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userId);
				
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The member "+userId+ " has been upgraded to administrator of "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error upgrading member "+userId+ " to administrator in "+ CommunityShortName +": "+ex.getMessage());
			throw ex;
		}
		
	}
	
	
	/**
	 * Add the users of a organization group as administrator in a company
	 * @param organizationShortName -> Short name of the user organization
	 * @param groupShortName -> Short name of the group
	 * @throws Exception
	 */
	public void UpgradeMembersOrganizationGroupToAdministrators(String organizationShortName, String groupShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/add-administrator-group";
			GroupOrgCommunityModel model= new GroupOrgCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setOrganization_short_name(organizationShortName);
				model.setGroup_short_name(groupShortName);
				
			}
			
			String result= WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("All the members from the group "+groupShortName+ " of "+organizationShortName+ " has been upgraded to administrator in "+CommunityShortName+ ". +\r\n"+result);
		
		}catch(Exception ex) {
			
			this._logHelper.Error("Error upgrading to administrator the members from the group "+groupShortName+ " of "+ organizationShortName+ " in "+ CommunityShortName +": "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Add a group to the community
	 * @param groupName -> name of the group
	 * @param groupShortName -> Short name of the group
	 * @param description -> Description of the group
	 * @param tags -> Tags of the group
	 * @param members -> List uses that want to add
	 * @param sendNotification -> It indicates whether an email is going to be sent to users telling them that has been added to the group
	 * @throws Exception
	 */
	public void CreateCommunityGroup (String groupName, String groupShortName, String description, List<String> tags, List<UUID> members, boolean sendNotification) throws Exception {
		try {
			String url=getApiUrl()+"/community/create-community-group";
			CreateGroupCommunityModel model = new CreateGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_name(groupName);
				model.setGroup_short_name(groupShortName);
				model.setTagas(tags);
				model.setMembers(members);
				model.setDescription(description);
				model.setSend_notification(sendNotification);
			}
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Group "+groupName+ " created in "+ CommunityShortName);
		}catch(Exception ex) {
			this._logHelper.Error("Error creating group "+groupName+ " in "+CommunityShortName+ ": "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Delete a community group from the community
	 * @param groupShortName -> Short name of the community group
	 * @throws Exception
	 */
	public void DeleteCommunityGroup(String groupShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/delete-community-group";
			DeleteGroupCommunityModel model= new DeleteGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_short_name(groupShortName);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Group "+groupShortName+ " deleted in "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error deleting group "+groupShortName+ " in "+ CommunityShortName+ ": "+ex.getMessage());
			throw ex; 
		}
	}
	
	
	/**
	 * Add a list of users to a community group
	 * @param groupShortName -> Short name of the group
	 * @param members -> List users that want to add
	 * @param sendNotification -> It indicates wheter a message is going to be sent to users telling them has been added to the group
	 * @throws Exception
	 */
	public void AddMembersToGroup(String groupShortName, List<UUID> members, boolean sendNotification) throws Exception {
		String miembros="";
		
		try {
			String url=getApiUrl()+"/community/add-members-to-community-group";
			MembersGroupCommunityModel model= new MembersGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_short_name(groupShortName);
				model.setMembers(members);
				model.setSend_notification(sendNotification);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The users has been added to the group "+groupShortName+ " of the community "+CommunityShortName);
		}catch(Exception ex) {
			this._logHelper.Error("Error adding users "+ (String.join(",", members.toString()))+ " to group "+ groupShortName+ " of the community "+ CommunityShortName+": \r\n"+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Create a list of users to a community group
	 * @param certificationLevelsDescription 
	 * @param certificationPolitics
	 * @throws Exception 
	 */
	public void CreateCertificationLevels(List<String> certificationLevelsDescription, String certificationPolitics) throws Exception {
		String miembros="";
		try {
			String url=getApiUrl()+"}/community/create-certification-levels";
			CertificationLevelModel model= new CertificationLevelModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setCertification_levels(certificationLevelsDescription);
				model.setCertification_politics(certificationPolitics);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The certification levels has been added to the community" +CommunityShortName);
		}catch(Exception ex) {
			this._logHelper.Error("Error creating certification levels of the community "+CommunityShortName+ ": \r\n"+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Delete a list of users from a community group
	 * @param groupShortName -> Short name of the group
	 * @param members -> List users that want to add
	 * @throws Exception
	 */
	public void DeleteMembersFromGroup(String groupShortName, List<UUID> members) throws Exception {
		try {
			String url=getApiUrl()+"/community/delete-members-of-community-group";
			MembersGroupCommunityModel model= new MembersGroupCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setGroup_short_name(groupShortName);
				model.setMembers(members);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The users has been deleted from the group "+groupShortName+ " of the community "+CommunityShortName);
			
		}catch(Exception ex) {
			this._logHelper.Error("Error deleting users "+(String.join(",", members.toString()))+ " from group "+groupShortName+ " of the community "+CommunityShortName+" : \r\n"+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets the members of a company group
	 * @param groupShortName-> Short name of the group
	 * @return -> List of members identifiers of the community group
	 * @throws Exception
	 */
	public List<UUID> getGroupMembers(String groupShortName) throws Exception {
		List<UUID> members = null;
		try {
			String url=getApiUrl()+"/community/get-members-from-community-group?community_short_name="+CommunityShortName+"&group_short_name="+groupShortName;
			String response=WebRequest("GET", url);
			Gson gson = new Gson();
			members= gson.fromJson(response,  (Type) new ArrayList<String>());
			this._logHelper.Debug("Users obtained from group "+groupShortName+ " of the community "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error obtaining users from group "+groupShortName+ " of the community "+CommunityShortName+ ": \r\n "+ex.getMessage());
			throw ex;
		}
		return members;
	}
	
	/**
	 * Gets the members of an organization group
	 * @param organizationShortName -> Organization short name of the user
	 * @param groupShortName -> Short name of the group 
	 * @return List of members identifiers of the organization group
	 * @throws Exception
	 */
	public List<UUID> getOrganizationGroupMembers(String organizationShortName, String groupShortName) throws Exception{
		List<UUID> members=null;
		try {
			String url=getApiUrl()+"/community/get-members-organization-group?organization_short_name="+organizationShortName+"&group_short_name="+groupShortName;
			String response= WebRequest("GET", url);
			Gson gson = new Gson();
			members= gson.fromJson(response,  (Type) new ArrayList<String>());
			this._logHelper.Debug("Users obtained from group "+groupShortName+ "of the organization "+organizationShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error obtaining users from group "+groupShortName+ " of the organization "+ organizationShortName+ ": \r\n "+ ex.getMessage());
			throw ex;
		}
		return members;
	}
	
	/**
	 * Expel a user from a community
	 * @param userId -> User identifier to expel from the community
	 * @throws Exception
	 */
	public void ExpelMember(UUID userId) throws Exception {
		try {
			String url=getApiUrl()+"/community/expel-member";
			UserCommunityModel model= new UserCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userId);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("User "+userId+ " expelled from "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error expelling member "+userId+ " from "+CommunityShortName+ ": \r\n "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Close a community
	 * @throws Exception
	 */
	public void CloseCommunity() throws Exception {
		try {
			String url=getApiUrl()+"/community/close-community";
			WebRequestPostWithJsonObject(url, CommunityShortName);
			this._logHelper.Debug("COmmunity "+CommunityShortName+ " closed");
		}
		catch(Exception ex) {
			this._logHelper.Error("Error closing "+CommunityShortName+ ": "+ex.getMessage());
			throw ex;
		}
	}
	
	/**
	 * Change the community name 
	 * @param newName -> new Community name 
	 * @throws Exception
	 */
	public void ChangeCommunityName(String newName) throws Exception {
		try {
			String url= getApiUrl()+"/community/change-community-name";
			ChangeNameCommunityModel model= new ChangeNameCommunityModel();
			{
				model.setCommunity_name(newName);
				model.setCommunity_short_name(CommunityShortName);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("Community name of "+CommunityShortName+ " changed to "+newName);
		}catch(Exception ex) {
			this._logHelper.Error("Error changing name of "+CommunityShortName+ " to "+newName+ ": "+ex.getMessage());
			throw ex;
		}
	}
	
	
	/**
	 * Gets a list with the groups of the community
	 * @return List with the groups of the community
	 * @throws Exception
	 */
	public List<GroupCommunityModel> getCommunityGroups() throws Exception{
		List<GroupCommunityModel> groups= null;
		try {
			String url=getApiUrl()+"/community/get-community-groups?community_short_name="+CommunityShortName;
			String response=WebRequest("GET", url);
			Gson gson = new Gson();
			groups= gson.fromJson(response,  (Type) new ArrayList<GroupCommunityModel>());
			this._logHelper.Debug("Groups obteined from "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error obtaining groups from "+CommunityShortName+ ": \r\n" +ex.getMessage());
			throw ex;
		}
		return groups;
	}
	
	
	/**
	 * Gets the organization name of a user in a community
	 * @param userId -> User identifier
	 * @return Organization name of a user in a community
	 * @throws Exception
	 */
	public String getOrganizationShortNameFromMember(String userId) throws Exception {
		String organizationShortName=null;
		try {
			String url = getApiUrl()+"/community/get-organization-short-name-from-member?community_short_name="+CommunityShortName+"&user_id="+userId;
			organizationShortName=WebRequest("GET", url);
			this._logHelper.Debug("Organization short name obtained from "+userId+" in "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error obtaining the organization short name from "+userId+" in "+CommunityShortName+": \r\n"+ex.getMessage());
			throw ex;
		}
		return organizationShortName;
	}
	
	
	/**
	 * Get the extra register data of a community
	 * @return List of ExtraRegisterData
	 * @throws Exception
	 */
	public List<ExtraRegisterData> getExtraRegisterData() throws Exception{
		List<ExtraRegisterData> extraRegisterData=null;
		try {
			String url=getApiUrl()+"/community/get-extra-register-data?community_short_name="+CommunityShortName;
			String response=WebRequest("GET", url);
			Gson gson = new Gson();
			extraRegisterData= gson.fromJson(response,  (Type) new ArrayList<ExtraRegisterData>());
			this._logHelper.Debug("Extra register data obtained from "+CommunityShortName);
		}
		catch(Exception ex) {
			this._logHelper.Error("Error obtaining extra register data from "+CommunityShortName+ ": \r\n" +ex.getMessage());
			throw ex;
		}
		return extraRegisterData;
	}
	
	/**
	 * Gets the community main language. Empty if it is not defined
	 * @return Community main language
	 */
	public String getCommunityMainLanguage() {
		try {
			String url=getApiUrl()+"/community/get-main-language?community_short_name="+CommunityShortName;
			String response=WebRequest("GET", url, "application/json");
			return response;
		}
		catch(Exception ex) {
			this._logHelper.Error("Imposible to obtain tha main language");
			return null;
		}
	}
	
	/**
	 * Get the person identifier and his email from the community members
	 * @return Dictionary with the person identifier and his email
	 */
	public Map<UUID, String> getCommunityPersonIDEmail(){
		try {
			String url=getApiUrl()+"/community/get-community-personid-email?community_short_name="+CommunityShortName;
			String response=WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response,  (Type) new HashMap<UUID, String>());
			
		}catch(Exception ex) {
			this._logHelper.Error("The person identifiers and emails of the community members '"+CommunityShortName+"' could not be obtained");
			return null;
		}
	}
	
	
	/**
	 * Gets the community identifier
	 * @param communityShortName -> Community short name 
	 * @return
	 * @throws Exception
	 */
	public UUID getCommunityId(String communityShortName) throws Exception {
		try {
			String url= getApiUrl()+"/community/get-community-id?community_short_name="+URLEncoder.encode(communityShortName);
			String response=WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, UUID.class); 
		}
		catch(Exception ex) {
			this._logHelper.Error("The community "+communityShortName+ " could not be found");
			throw ex;
		}
		
	}
	
	/**
	 * Blocks a member in a community
	 * @param userId -> User´s identifier 
	 * @param communityShortName -> Community short name 
	 * @throws Exception
	 */
	public void BlockMember(UUID userId, String communityShortName) throws Exception {
		try {
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setUser_id(userId);
				model.setCommunity_short_name(communityShortName);
			}
			String url=getApiUrl()+"/community/block-member";
			WebRequestPostWithJsonObject(url, model);
		} catch(Exception ex) {
			this._logHelper.Error("The user "+userId+ " of the community members '"+communityShortName+"' could not be blocked");
			throw ex;
		}
	}
	
	
	/**
	 * Unblocks a member in a community
	 * @param userId -> User´s identifier
	 * @param communityShortName -> Community short name 
	 * @throws Exception
	 */
	public void UnblockMember(UUID userId, String communityShortName) throws Exception {
		try {
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setUser_id(userId);
				model.setCommunity_short_name(communityShortName);
			}
			String url=getApiUrl()+"/community/block-member";
			WebRequestPostWithJsonObject(url, model);
		} catch(Exception ex) {
			this._logHelper.Error("The user "+userId+ " of the community members '"+communityShortName+"' could not be blocked");
			throw ex;
		}
	}
	
	/**
	 * Refresh the cache of a CMS component 
	 * @param componentId -> Component id to refresh
	 * @param communityShortName -> Community short name
	 * @throws Exception
	 */
	public void RefreshCMSComponent(UUID componentId, String communityShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/refresh-cms-component?component_id="+componentId.toString()+"&community_short_name="+URLEncoder.encode(communityShortName);
			WebRequest("POST", url);
		}
		catch(Exception ex) {
			this._logHelper.Error("The component id "+componentId+" of the community '"+communityShortName+"' could not be refreshed");
			throw ex;
		}
	}
	
	
	/**
	 * Refresh the cache of all community´s CMS components 
	 * @param communityShortName -> Community short name 
	 * @throws Exception
	 */
	public void RefreshAllCMSComponent( String communityShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/refresh-all-cms-components?community_short_name="+URLEncoder.encode(communityShortName);
			WebRequest("POST", url);
		}
		catch(Exception ex) {
			this._logHelper.Error("The components of the  of the community '"+communityShortName+"' could not be refreshed");
			throw ex;
		}
	}
	
	
	/**
	 * Gets the basic information of a community
	 * @param communityShortName -> Community short name 
	 * @return
	 * @throws Exception
	 */
	public CommunityInfoModel GetCommunityInfo(String communityShortName) throws Exception {
		try {
			String url=getApiUrl()+"/community/get-community-information?community_short_name="+URLEncoder.encode(communityShortName);
			String response= WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, CommunityInfoModel.class); 
		}
		catch(Exception ex ) {
			this._logHelper.Error("The community "+communityShortName+ " could not be found");
			throw ex;
		}
	}
	
	
	/**
	 * Gets the basic information of a community
	 * @param communityId -> Community identifier
	 * @return
	 * @throws Exception
	 */
	public CommunityInfoModel GetCommunityInfo(UUID communityId) throws Exception {
		try {
			String url=getApiUrl()+"/community/get-community-information?community_id="+communityId;
			String response= WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, CommunityInfoModel.class); 
		}
		catch(Exception ex ) {
			this._logHelper.Error("The community "+communityId+ " could not be found");
			throw ex;
		}
	}
	
	
	/**
	 * Gets the name of a category in all of the languages that has been defined
	 * @param categoryId -> Category identifier
	 * @return
	 * @throws Exception
	 */
	public CategoryNames getCategoryName(UUID categoryId) throws Exception {
		try {
			String url=getApiUrl()+"/community/get-category-name?category_id="+categoryId;
			String response=WebRequest("GET", url, "application/json");
			Gson gson = new Gson();
			return gson.fromJson(response, CategoryNames.class); 
		}
		catch(Exception ex) {
			this._logHelper.Error("The category "+categoryId+ " could not be found");
			throw ex;
		}
	}
	
	/**
	 * 
	 * @param pShortName
	 * @param pUserID
	 * @return
	 * @throws IOException
	 * @throws GnossAPIException
	 */
	public boolean CheckIsAdminCommunity(String pShortName, UUID pUserID) throws IOException, GnossAPIException {
		
		UserCommunityModel model= new UserCommunityModel();
		model.setCommunity_short_name(pShortName);
		model.setUser_id(pUserID);
		String url=getApiUrl()+"/community/check-administrator-community";
		String response=WebRequestPostWithJsonObject(url, model);
		Gson gson = new Gson();
		return gson.fromJson(response, Boolean.class); 
	
	}
	
	
	public void DowngradeMemberFromAdministrator(UUID userId) {
		try {
			String url=getApiUrl()+"/community/delete-administrator-permission";
			UserCommunityModel model = new UserCommunityModel();
			{
				model.setCommunity_short_name(CommunityShortName);
				model.setUser_id(userId);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The member "+userId+ " has been upgraded to adminitrator of "+CommunityShortName);
		}catch(Exception ex) {
			this._logHelper.Error("Error upgrading member "+userId+ " to administrator in "+CommunityShortName+ ": "+ex.getMessage());
		}
	}
}
