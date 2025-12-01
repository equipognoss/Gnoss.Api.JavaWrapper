package org.gnoss.apiWrapper.Main;

import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.gnoss.apiWrapper.ApiModel.Concept;
import org.gnoss.apiWrapper.ApiModel.ConceptToAddModel;
import org.gnoss.apiWrapper.ApiModel.ConceptToDeleteModel;
import org.gnoss.apiWrapper.ApiModel.ConceptToModifyModel;
import org.gnoss.apiWrapper.ApiModel.ParamsChangeCategoryName;
import org.gnoss.apiWrapper.ApiModel.ParamsChangeName;
import org.gnoss.apiWrapper.ApiModel.ParamsCreateCategory;
import org.gnoss.apiWrapper.ApiModel.ParamsDeleteCategory;
import org.gnoss.apiWrapper.ApiModel.ParamsInsertNode;
import org.gnoss.apiWrapper.ApiModel.ParamsMoveNode;
import org.gnoss.apiWrapper.ApiModel.ParamsParentNode;
import org.gnoss.apiWrapper.ApiModel.Thesaurus;
import org.gnoss.apiWrapper.ApiModel.ThesaurusToDeleteModel;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.Constants;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.xml.sax.SAXException;

/**
 * Wrapper for GNOSS thesaurus API
 * @author Gnoss
 */
public class ThesaurusAPI extends GnossApiWrapper {

	private ILogHelper _logHelper;
	
	//Region Constructors
	
	/**
	 * Constructor of ThesaurusAPI
	 * @param oauth OAuth information to sign the API request
	 * @param communityShortName Community short name which you want to use the API
	 */
	public ThesaurusAPI(OAuthInfo oauth, String communityShortName) {
		super(oauth, communityShortName);
		this._logHelper = LogHelper.getInstance();
	}
	
	/**
	 * Constructor of ThesaurusAPI
	 * @param configFilePath Configuration file path 
	 * @throws GnossAPIException Gnoss API Exception 
	 * @throws ParserConfigurationException Parser Configuration Exception 
	 * @throws SAXException SAX Exception 
	 * @throws IOException IO Exception 
	 */
	public ThesaurusAPI(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper = LogHelper.getInstance();
	}
	
	//endRegion
	
	//Region Public methods 
	
	/**
	 * Get the RDF of a semantic thesaurus
	 * @param thesaurusOntologyUrl Ontology URL of the thesaurus
	 * @param source Identifier of the thesaurus
	 * @return RDF of a semantic thesaurus
	 */
	public String getThesaurus(String thesaurusOntologyUrl, String source) throws Exception{
		try {
			String url = getApiUrl() + "/thesaurus/get-thesaurus?thesaurus_ontology_url=" + thesaurusOntologyUrl + 
					"&community_short_name=" + getCommunityShortName() + "&source=" + source;
			String response = WebRequest("GET", url, "application/json");
			
			if (response != null && !response.isEmpty()) {
				this._logHelper.Debug("Thesaurus obtained successfully");
			}
			return response;
		} catch (Exception ex) {
			this._logHelper.Error("There has been an error getting the thesaurus of the community " + 
					getCommunityShortName() + " and ontology " + thesaurusOntologyUrl + ". " + ex.getMessage()); 
			throw ex;
		}
	}
	
	/**
	 * Moves category of a semantic thesaurus from its current father to another one, indicating its full path from the root.
	 * @param pURLOntologiaTesauro URL of the semantic thesaurus ontology
	 * @param pUrlOntologiaRecursos URL of the ontology of the resources that are linked to the semantic thesaurus
	 * @param pCategoriaMoveId URI of the category to move
	 * @param pPath Path from the root to the last new father of the category
	 * @throws Exception Exception 
	 * @deprecated Use modifyCategory method instead
	 */
	@Deprecated
	public void moveSemanticThesaurusNode(String pURLOntologiaTesauro, String pUrlOntologiaRecursos, 
			String pCategoriaMoveId, String[] pPath) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/move-node";
			ParamsMoveNode model = new ParamsMoveNode();
			model.setThesaurus_ontology_url(pURLOntologiaTesauro);
			model.setResources_ontology_url(pUrlOntologiaRecursos);
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(pCategoriaMoveId);
			model.setPath(pPath);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category " + pCategoriaMoveId + " has been moved");
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Removes a category of a semantic thesaurus moving all resources that were linked to it to another one indicating its complete path from the root.
	 * @param pUrlOntologiaTesauro URL of the semantic thesaurus ontology
	 * @param pUrlOntologiaRecursos URL of the ontology of the resources that are linked to the semantic thesaurus
	 * @param pCategoriaAEliminarId URI of the category to remove
	 * @param pPath Path from the root father to its last child where resources of the removed category are going to be moved to
	 * @throws Exception Exception 
	 * @deprecated Use deleteCategory method instead
	 */
	@Deprecated
	public void removeSemanticThesaurusNode(String pUrlOntologiaTesauro, String pUrlOntologiaRecursos, 
			String pCategoriaAEliminarId, String[] pPath) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/delete-node";
			ParamsMoveNode model = new ParamsMoveNode();
			model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
			model.setResources_ontology_url(pUrlOntologiaRecursos);
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(pCategoriaAEliminarId);
			model.setPath(pPath);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category " + pCategoriaAEliminarId + " has been deleted");
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Modify the category name 
	 * @param newCategoryName new Category name
	 * @param categoryId Category id 
	 * @throws Exception Exception 
	 * @deprecated 
	 */
	@Deprecated
	public void changeCategoryName(String newCategoryName, UUID categoryId) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/change-category-name";
			ParamsChangeCategoryName model = new ParamsChangeCategoryName();
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(categoryId);
			model.setNew_category_name(newCategoryName);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category " + categoryId + " has been modified");
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Create a new category
	 * @param categoryName category name 
	 * @param parentCategoryId parent category id 
	 * @throws Exception Exception 
	 * @deprecated 
	 */
	@Deprecated
	public void createCategory(String categoryName, UUID parentCategoryId) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/create-category";
			ParamsCreateCategory model = new ParamsCreateCategory();
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_name(categoryName);
			model.setParent_category_id(parentCategoryId);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category " + categoryName + " has been created");
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Create a new category
	 * @param categoryName category name 
	 * @throws Exception parent category id 
	 * @deprecated 
	 */
	@Deprecated
	public void createCategory(String categoryName) throws Exception {
		createCategory(categoryName, Constants.UUID_EMPTY);
	}
	
	/**
	 * Delete a category
	 * @param categoryName category name 
	 * @param categoryId category id 
	 * @throws Exception Exception 
	 * @deprecated 
	 */
	@Deprecated
	public void deleteCategory(String categoryName, UUID categoryId) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/delete-category";
			ParamsDeleteCategory model = new ParamsDeleteCategory();
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(categoryId);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category " + categoryName + " has been deleted");
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Adds a category as a parent of another one
	 * @param pUrlOntologiaTesauro URL of the semantic thesaurus ontology
	 * @param pCategoriaPadreId URI of the parent category
	 * @param pCategoriaHijoId URI of the child category
	 * @throws Exception Exception 
	 * @deprecated Use addCategory or modifyCategory method instead
	 */
	@Deprecated
	public void addFatherToSemanticThesaurusNode(String pUrlOntologiaTesauro, String pCategoriaPadreId, 
			String pCategoriaHijoId) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/set-node-parent";
			ParamsParentNode model = new ParamsParentNode();
			model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
			model.setCommunity_short_name(getCommunityShortName());
			model.setParent_category_id(pCategoriaPadreId);
			model.setChild_category_id(pCategoriaHijoId);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The parent of the category " + pCategoriaHijoId + " is now " + pCategoriaPadreId);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Modifies the semantic thesaurus category name
	 * @param pUrlOntologiaTesauro URL of the semantic thesaurus ontology
	 * @param pCategoriaId URI of the category
	 * @param pNombre Category name, supports multi language with the format: nombre@es|||name@en|||
	 * @throws Exception Exception 
	 * @deprecated Use modifyCategory method instead
	 */
	@Deprecated
	public void changeNameToSemanticThesaurusNode(String pUrlOntologiaTesauro, String pCategoriaId, 
			String pNombre) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/change-node-name";
			ParamsChangeName model = new ParamsChangeName();
			model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
			model.setCommunity_short_name(getCommunityShortName());
			model.setCategory_id(pCategoriaId);
			model.setCategory_name(pNombre);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("The category " + pCategoriaId + " has changed, and now is " + pNombre);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Inserts a category of a semantic thesaurus.
	 * @param pUrlOntologiaTesauro URL of the semantic thesaurus ontology
	 * @param pRdfCategoria Inserted category RDF
	 * @throws Exception Exception 
	 * @deprecated Use addCategory method instead
	 */
	@Deprecated
	public void insertSemanticThesaurusNode(String pUrlOntologiaTesauro, byte[] pRdfCategoria) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/insert-node";
			ParamsInsertNode model = new ParamsInsertNode();
			model.setThesaurus_ontology_url(pUrlOntologiaTesauro);
			model.setCommunity_short_name(getCommunityShortName());
			model.setRdf_category(pRdfCategoria);
			
			WebRequestPostWithJsonObject(url, model);
			this._logHelper.Debug("A semantic category has been added to the community " + getCommunityShortName());
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * Create a Thesaurus with the Collections and Concepts in the parameters
	 * @param pThesaurus Thesaurus that will be loaded
	 * @throws Exception Exception
	 */
	public void createThesaurus(Thesaurus pThesaurus) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/create-thesaurus";
			
			if (pThesaurus.getCommunityShortName() == null || pThesaurus.getCommunityShortName().isEmpty()) {
				pThesaurus.setCommunityShortName(getCommunityShortName());
			}
			
			WebRequestPostWithJsonObject(url, pThesaurus);
			this._logHelper.Debug("Thesaurus created successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error creating thesaurus: " + ex.getMessage());
			throw new Exception(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Modify the indicated Thesaurus. Replace current data with the list of Collection and Concept given.
	 * @param pThesaurus Thesaurus that will be loaded
	 * @throws Exception Exception
	 */
	public void modifyThesaurus(Thesaurus pThesaurus) throws Exception {
		try {
			String url = getApiUrl() + "/thesaurus/modify-thesaurus";
			
			if (pThesaurus.getCommunityShortName() == null || pThesaurus.getCommunityShortName().isEmpty()) {
				pThesaurus.setCommunityShortName(getCommunityShortName());
			}
			
			WebRequestPostWithJsonObject(url, pThesaurus);
			this._logHelper.Debug("Thesaurus modified successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error modifying thesaurus: " + ex.getMessage());
			throw new Exception(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Add new category with its narrowers at the thesaurus
	 * @param pConcept Concept to add
	 * @param pSource Source of the thesaurus
	 * @param pOntology Ontology of the thesaurus
	 * @param pParentCategorySubject Subject of the broader of the concept to load (empty string for top level)
	 * @throws Exception Exception
	 */
	public void addCategory(Concept pConcept, String pSource, String pOntology, String pParentCategorySubject) 
			throws Exception {
		try {
			ConceptToAddModel conceptToAddModel = new ConceptToAddModel();
			conceptToAddModel.setConcept(pConcept);
			conceptToAddModel.setSource(pSource);
			conceptToAddModel.setOntology(pOntology);
			conceptToAddModel.setParentCategorySubject(pParentCategorySubject);
			conceptToAddModel.setCommunityShortName(getCommunityShortName());
			
			String url = getApiUrl() + "/thesaurus/add-thesaurus-category";
			
			WebRequestPostWithJsonObject(url, conceptToAddModel);
			this._logHelper.Debug("Category added successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error adding category: " + ex.getMessage());
			throw new Exception(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Add new category with its narrowers at the thesaurus (without parent)
	 * @param pConcept Concept to add
	 * @param pSource Source of the thesaurus
	 * @param pOntology Ontology of the thesaurus
	 * @throws Exception Exception
	 */
	public void addCategory(Concept pConcept, String pSource, String pOntology) throws Exception {
		addCategory(pConcept, pSource, pOntology, "");
	}
	
	/**
	 * Modify the concept given by parameter and its narrowers if you indicated it
	 * @param pConcept Concept to modify
	 * @param pSource Source of the thesaurus
	 * @param pOntology Ontology of the thesaurus
	 * @param pModifyNarrower Indicates if the method has to modify the narrowers
	 * @param pParentCategorySubject Subject of the parent, if empty the concept will have no parent and will be a first level concept
	 * @throws Exception Exception
	 */
	public void modifyCategory(Concept pConcept, String pSource, String pOntology, boolean pModifyNarrower, 
			String pParentCategorySubject) throws Exception {
		try {
			ConceptToModifyModel conceptToModify = new ConceptToModifyModel();
			conceptToModify.setConcept(pConcept);
			conceptToModify.setSource(pSource);
			conceptToModify.setOntology(pOntology);
			conceptToModify.setCommunityShortName(getCommunityShortName());
			conceptToModify.setModifyNarrower(pModifyNarrower);
			conceptToModify.setParentCategorySubject(pParentCategorySubject);
			
			String url = getApiUrl() + "/thesaurus/modify-thesaurus-category";
			
			WebRequestPostWithJsonObject(url, conceptToModify);
			this._logHelper.Debug("Category modified successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error modifying category: " + ex.getMessage());
			throw new Exception(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Modify the concept given by parameter and its narrowers if you indicated it (without parent change)
	 * @param pConcept Concept to modify
	 * @param pSource Source of the thesaurus
	 * @param pOntology Ontology of the thesaurus
	 * @param pModifyNarrower Indicates if the method has to modify the narrowers
	 * @throws Exception Exception
	 */
	public void modifyCategory(Concept pConcept, String pSource, String pOntology, boolean pModifyNarrower) 
			throws Exception {
		modifyCategory(pConcept, pSource, pOntology, pModifyNarrower, "");
	}
	
	/**
	 * Delete the thesaurus indicated by the source given by parameter
	 * @param pSource Source of the thesaurus to delete
	 * @param pOntology Ontology of the thesaurus to delete
	 * @throws Exception Exception
	 */
	public void deleteThesaurus(String pSource, String pOntology) throws Exception {
		try {
			ThesaurusToDeleteModel thesaurusToDelete = new ThesaurusToDeleteModel();
			thesaurusToDelete.setSource(pSource);
			thesaurusToDelete.setOntology(pOntology);
			thesaurusToDelete.setCommunityShortName(getCommunityShortName());
			
			String url = getApiUrl() + "/thesaurus/delete-thesaurus";
			
			WebRequestPostWithJsonObject(url, thesaurusToDelete);
			this._logHelper.Debug("Thesaurus deleted successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting thesaurus: " + ex.getMessage());
			throw new Exception(ex.getMessage(), ex);
		}
	}
	
	/**
	 * Delete the concept indicated and it's children
	 * @param pConceptSubject Subject of the Concept to delete
	 * @param pOntology Ontology of the thesaurus
	 * @throws Exception Exception
	 */
	public void deleteCategory(String pConceptSubject, String pOntology) throws Exception {
		try {
			ConceptToDeleteModel conceptToDelete = new ConceptToDeleteModel();
			conceptToDelete.setConceptSubject(pConceptSubject);
			conceptToDelete.setOntology(pOntology);
			conceptToDelete.setCommunityShortName(getCommunityShortName());
			
			String url = getApiUrl() + "/thesaurus/delete-thesaurus-category";
			
			WebRequestPostWithJsonObject(url, conceptToDelete);
			this._logHelper.Debug("Category deleted successfully");
		} catch (Exception ex) {
			this._logHelper.Error("Error deleting category: " + ex.getMessage());
			throw new Exception(ex.getMessage(), ex);
		}
	}
	
	//endRegion
}