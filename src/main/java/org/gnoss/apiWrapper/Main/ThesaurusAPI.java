package org.gnoss.apiWrapper.Main;

import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.gnoss.apiWrapper.ApiModel.ParamsChangeCategoryName;
import org.gnoss.apiWrapper.ApiModel.ParamsChangeName;
import org.gnoss.apiWrapper.ApiModel.ParamsCreateCategory;
import org.gnoss.apiWrapper.ApiModel.ParamsDeleteCategory;
import org.gnoss.apiWrapper.ApiModel.ParamsInsertNode;
import org.gnoss.apiWrapper.ApiModel.ParamsMoveNode;
import org.gnoss.apiWrapper.ApiModel.ParamsParentNode;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.xml.sax.SAXException;
/**
 * Wrapper for GNOSS thesaurus API
 * @author Andrea
 *
 */
public class ThesaurusAPI extends GnossApiWrapper{

	private ILogHelper _logHelper;
	
	//Region Constructors
	
	/**
	 * Constructor of ThhesaurusApi
	 * @param oauth  OAuth information to sign the API request
	 * @param communityShortName  Community short name which you want to use the API
	 */
	public ThesaurusAPI(OAuthInfo oauth, String communityShortName) {
		super(oauth, communityShortName);
		this._logHelper= LogHelper.getInstance();
		
	}
	
	/**
	 * Constructor of THesaurusAPI
	 * @param configFilePath  Configuration file path, with a structure like "http://api.gnoss.com/v3/exampleConfig.txt" 
	 * @throws GnossAPIException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public ThesaurusAPI( String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper= LogHelper.getInstance();
	}
	
	//endRegion
	
	
	//Region Public methods 
	
	/**
	 * Get the RDF of a semantic thesaurus
	 * @param thesaurusOntologyUrl  Identifier of the thesaurus
	 * @param source  RDF of a semantic thesaurus
	 * @return
	 */
	public String GetThesaurus(String thesaurusOntologyUrl, String source) {
		try {
			String url=getApiUrl()+"/thesaurus/get-thesaurus?thesaurus_ontology_url="+thesaurusOntologyUrl+"&community_short_name="+getCommunityShortName()+"&source="+source;
			String response= WebRequest("GET", url,"application/json");
			
			if(!response.isEmpty() || response!=null) {
				this._logHelper.Debug("Thesaurus obtained successfully");
			}
			return response;
		}catch(Exception ex) {
			this._logHelper.Error("There has been an error getting the thesaurus of the community"+getCommunityShortName()+" and ontology "+thesaurusOntologyUrl+". "+ ex.getMessage()); 
			return null;
		}
	}
	/**
	 * Moves category of a semantic thesaurus from its current father to another one, indicating its full path from the root.
	 * @param pURLOntologiaTesauro  URL of the semantic thesaurus ontology
	 * @param pUrlOntologiaRecursos  URL of the ontology of the resources that are linked to the semantic thesaurus
	 * @param pCategoriaMoveId  URI of the category to move
	 * @param pPath Path from the root to the last new father of the category
	 * @throws Exception
	 */
	public void MoveSemanticThesaurusNode(String pURLOntologiaTesauro, String pUrlOntologiaRecursos, String pCategoriaMoveId, String[] pPath) throws Exception {
		try {
			String url=getApiUrl()+"/thesaurus/move-node";
		ParamsMoveNode model= new ParamsMoveNode();
			{
			model.setThesaurus_ontology_url(pURLOntologiaTesauro);
			model.setResources_ontology_url(pUrlOntologiaRecursos);
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(pCategoriaMoveId);
			model.setPath(pPath);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category"+pCategoriaMoveId+" has been moved");
		}catch(Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Removes a category of a semantic thesaurus moving all resources that were linked to it to another one indicating its complete path from the root.
	 * @param pUrlOntologiaTesauro  URL of the semantic thesaurus ontology
	 * @param UrlOntologiaRecursos  URL of the ontology of the resources that are linked to the semantic thesaurus
	 * @param pCategoriaAEliminarId  Path from the root father to its last child where resources of the removed category are going to be moved to
	 * @param pPath path 
	 * @throws Exception
	 */
	public void RemoveSemanticThesaurusNode(String pUrlOntologiaTesauro, String UrlOntologiaRecursos, String pCategoriaAEliminarId, String[] pPath) throws Exception {
		try {
			String url=getApiUrl()+"/thesaurus/delete-node";
			ParamsMoveNode model= new ParamsMoveNode();
			{
				model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
				model.setResources_ontology_url(UrlOntologiaRecursos);
				model.setCommunity_short_name(getCommunityShortName());
				model.setCategory_id(pCategoriaAEliminarId);
				model.setPath(pPath);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category"+ pCategoriaAEliminarId+" has been deleted");
			
		}catch(Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Modify the category name 
	 * @param newCategoryNamed new Category named
	 * @param categoryId Category id 
	 * @throws Exception
	 */
	public void ChangeCategoryName(String newCategoryNamed, UUID categoryId) throws Exception {
		try {
			String url=getApiUrl()+ "/thesaurus/delete-node";
			ParamsChangeCategoryName model= new ParamsChangeCategoryName();
			{
				model.setCommunity_short_name(getCommunityShortName());
				model.setCategory_id(categoryId);
				model.setNew_category_name(newCategoryNamed);
			}
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The cataegory" + categoryId +"has been modifier");
			
		}catch(Exception ex){
			throw ex;
			}
	}
	
	/**
	 * Create a new category
	 * @param categoryName category name 
	 * @param parentCategoryId parent category id 
	 * @throws Exception
	 */
	public void createCategory(String categoryName, UUID parentCategoryId) throws Exception{
		try {
			String url= getApiUrl()+"/thesaurus/create-category";
			ParamsCreateCategory model= new ParamsCreateCategory();
			{
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_name(categoryName);
			model.setParent_category_id(parentCategoryId);
			
			}
			WebRequestPostWithJsonObject(url, model);
			
			this._logHelper.Debug("The category" + categoryName+"has been created");
			
		}catch(Exception ex) {
			throw ex;
		
		}
	}
	
	/**
	 * Delete a category
	 * @param categoryName category name 
	 * @param parentCategoryId parent category id 
	 * @throws Exception
	 */
	
	public void DeleteCategory(String categoryName, UUID parentCategoryId) throws Exception{
		try {
			String url= getApiUrl()+"/thesaurus/delete-category";
			ParamsDeleteCategory model= new ParamsDeleteCategory();
			{
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(parentCategoryId);
			
			
			}
			WebRequestPostWithJsonObject(url, model);
			
			this._logHelper.Debug("The category" + categoryName+"has been deleted");
			
		}catch(Exception ex) {
			throw ex;
		
		}
	}
	
	/**
	 * Adds a category as a parent of another one
	 * @param pUrlOntologiaTesauro URL of the semantic thesaurus ontology
	 * @param pCategoriaPadreId URI of the parent category
	 * @param pCategoriaHijoId  URI of the child category<
	 * @throws Exception
	 */
	public void AddFatherToSemanticThesaurusNode(String pUrlOntologiaTesauro, String pCategoriaPadreId, String pCategoriaHijoId) throws Exception {
		try {
			String url=getApiUrl()+"/thesaurus/set-node-parent";
			ParamsParentNode model= new ParamsParentNode();
			{
				model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
				model.setCommunity_short_name(getCommunityShortName());
				model.setParent_category_id(pCategoriaPadreId);
				model.setChild_category_id(pCategoriaHijoId);
			}
			WebRequestPostWithJsonObject(url, model);
			
			this._logHelper.Debug("The parent of the category" +pCategoriaHijoId+ "is now" +pCategoriaPadreId);
		}catch(Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Modifies the semantic thesaurus category name
	 * @param pUrlOntologiaTesauro  URL of the semantic thesaurus ontology
	 * @param pCategoriaId  URI of the category
	 * @param pNombre  Category name, supports multi language with the format: nombre@es|||name@en|||
	 * @throws Exception
	 */
	public void ChangeNameToSemanticThesaurusNode(String pUrlOntologiaTesauro, String pCategoriaId, String pNombre) throws Exception {
		try {
			String url=getApiUrl()+"/thesaurus/change-node-parent";
			ParamsChangeName model= new ParamsChangeName();
			{
				model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
				model.setCommunity_short_name(getCommunityShortName());
				model.setCategory_id(pCategoriaId);
				model.setCategory_name(pNombre);
			}
			WebRequestPostWithJsonObject(url, model);
			
			this._logHelper.Debug("The category" +pCategoriaId+ "has changed, and now is" +pNombre);
		}catch(Exception ex) {
			throw ex;
		}
	}
	
	
	/**
	 * Inserts a category of a semantic thesaurus.
	 * @param pUrlOntologiaTesauro  URL of the semantic thesaurus ontology
	 * @param pRdfCategoria  Inserted category Rdf
	 * @throws Exception
	 */
	public void InsertSemanticThesaurusNode(String pUrlOntologiaTesauro, byte[] pRdfCategoria) throws Exception {
		try {
			String url=getApiUrl()+"/thesaurus/insert-node";
			ParamsInsertNode model= new ParamsInsertNode();
			{
				model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
				model.setCommunity_short_name(getCommunityShortName());
				model.setRdf_category(pRdfCategoria);
				
			}
			WebRequestPostWithJsonObject(url, model);
			
			this._logHelper.Debug("A semantic category has been added to the community " +getCommunityShortName());
		}catch(Exception ex) {
			throw ex;
		}
	}
	//endRegionre
}
