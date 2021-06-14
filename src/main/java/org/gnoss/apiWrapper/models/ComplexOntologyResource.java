/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.models;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.AttachedResourceFilePropertyTypes;
import org.gnoss.apiWrapper.ApiModel.ResourceVisibility;
import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.Constants;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.net.URI;

/**
 *
 * @author jruiz
 */
public class ComplexOntologyResource extends BaseResource {
    //Members
    private String _rdfFile;
    private String _stringRdfFile;
    private Ontology _ontology;
    private ImageHelper _imageHelper;
    private ILogHelper _logHelper;
    
    //Properties
    private String GnossId;
    private String RdFile;
    private String StringRdfFile;
    private String PublisherEmail;
    private String MainImage;
    private UUID ShortGnossId;
    public String Author;
    
    //Attached files
    private ArrayList<String> AttachedFilesName;
    private ArrayList<AttachedResourceFilePropertyTypes> AttachedFilesType;
    public ArrayList<byte[]> AttachedFiles;
    
    //Screenshots
    private boolean MustGenerateScreenshot;
    private String ScreenshotUrl;
    private String ScreenshotPredicate;
    private int[] ScreenshotSizes;
    
    
    public ComplexOntologyResource(String largeGnossId) throws GnossAPIArgumentException{
    	Initialize();
    	if(largeGnossId != null){
    		GnossId = largeGnossId;
    	}
    	else{
    		throw new GnossAPIArgumentException("Required. LargeGnossId can't be null or empty");
    	}
    }
    
    public ComplexOntologyResource(UUID shortGnossId){
    	Initialize();
    	ShortGnossId = shortGnossId;
    }
    
    
    public ComplexOntologyResource(){
    	Initialize();
    }
    
    private void Initialize(){
    	Author = null;
    	MustGenerateScreenshot = false;
    	ScreenshotUrl = "";
    	ScreenshotSizes = null;
    	ScreenshotPredicate = "";
    	AttachedFilesName = new ArrayList<String>();
    	AttachedFilesType = new ArrayList<AttachedResourceFilePropertyTypes>();
    	AttachedFiles = new ArrayList<byte[]>();
    	_ontology = null;
    	setAutomaticTagsTextFromTitle("");
    	setAutomaticTagsTextFromDescription("");
    	setDescription("");
    	setPublishInHome(false);
    	setUploaded(false);
    	setPublisherEmail("");
    	setVisibility(ResourceVisibility.open);
    }
    
    @Override
    public String getGnossId() {       
        return super.getGnossId();
    }

    @Override
    public void setGnossId(String GnossId) throws GnossAPIArgumentException {
        super.setGnossId(GnossId);
    }

    public String getRdFile() throws IOException, GnossAPIException {
        if(_rdfFile == null || _rdfFile.length() == 0){
            _rdfFile = this._ontology.GenerateRDF();
        }
        return _rdfFile;
    }

    public void setRdFile(String RdFile) {
        this._rdfFile = RdFile;
    }

    public boolean getMustGenerateScreenshot(){
    	return MustGenerateScreenshot;
    }
    public void setMustGenerateScreenshot(boolean b){
    	MustGenerateScreenshot=b;;
    }
    
    public String getScreenshotUrl(){
    	return ScreenshotUrl;
    }
    
    public void setScreenshotUrl(String s){
    	 ScreenshotUrl=s;
    }
    
    public String getScreenshotPredicate(){
    	return ScreenshotPredicate;
    }
    public void setScreenshotPredicate(String s){
    	ScreenshotPredicate=s;
    }
    
    public int[] getScreenshotSizes(){
    	return ScreenshotSizes;
    }
    public void setScreenshotSizes(int[] n){
    	 ScreenshotSizes=n;
    }
    
    public String getStringRdfFile() throws IOException, GnossAPIException {
        if(_rdfFile == null || _rdfFile.length() == 0){
            _rdfFile = this._ontology.GenerateRDF();
        }
         return new String(_rdfFile);
    }

    public String getPublisherEmail() {
        return PublisherEmail;
    }

    public ArrayList<byte[]> getAttachedFiles(){    	
    	return AttachedFiles;
    }
    
    public  void setAttachedFiles(ArrayList<byte[]> att){    	
    	 AttachedFiles=att;
    }
    
    public ArrayList<AttachedResourceFilePropertyTypes> getAttachedFilesType(){
    	return AttachedFilesType;
    }
    
    public ArrayList<String> getAttachedFilesName(){
    	return AttachedFilesName;    	
    }
    
    public void setPublisherEmail(String PublisherEmail) {
        this.PublisherEmail = PublisherEmail;
    }

    public String getMainImage() {
        return MainImage;
    }

    public void setMainImage(String MainImage) {
        this.MainImage = MainImage;
    }

    public Ontology getOntology() {
        return _ontology;
    }
    
    public UUID getShortGnossId(){
    	return ShortGnossId;
    }
    
    public void setShortGnossId(UUID shortGnossId){
    	ShortGnossId = shortGnossId;
    }

    public void setOntology(Ontology Ontology) throws IOException, GnossAPIException {
        this._ontology = Ontology;
        _rdfFile = _ontology.GenerateRDF();
        ShortGnossId = _ontology.getResourceId();
        setGnossId(_ontology.getIdentifier());
    }
    
    /**
     * Prepares the screenshot of a url with the specified sizes. The screenshot will be asigned to imagePredicate property
     * @param screenshotUrl Url to generate the screenshot
     * @param imagePredicate Predicate where the screenshot url will be saved
     * @param screenshotSizes Screenshot sizes. It will generate as many screenshots as sizes in this array
     */
    public void GenerateScreenshot(String screenshotUrl, String imagePredicate, int[] screenshotSizes) {
    	setMustGenerateScreenshot(true);
    	setScreenshotUrl(screenshotUrl);
    	setScreenshotPredicate(imagePredicate);
    	setScreenshotSizes(screenshotSizes);
    }
    
    /**
     * Returns a String with the information of this ComplexOntologyResource
     * @return String stringBuilder
     */
    public String toString() {
    	StringBuilder sb= new StringBuilder();
    	
    	sb.append("-------------------------------------");
    	sb.append("\n");
    	sb.append("Resource:");
    	sb.append("\n");
    	sb.append("\t Id"+ getId());
    	sb.append("\n");
    	sb.append("\t GnossId"+ getGnossId());
    	sb.append("\n");
    	sb.append("\t Description"+ getDescription());
    	sb.append("\n");
    	sb.append("\t Tags"+ getTags().toString());
    	sb.append("\n");
    	sb.append("\t TextCategories"+ getCategoriesIds().toString());
    	sb.append("\n");
    	sb.append("\t Author"+ getAuthor());
    	sb.append("\n");
    	sb.append("------------------------------------");
    	
    	return sb.toString();
    }
    
    /**
     * Attach a file (not an image, to attach an image use AttachImage method) to the resource. 
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param entity Auxiliary entity which would have the reference to the file<
     * @param fileIdentifier Unique identifier of the file. Only neccesary if there is more than one file with the same name<
     * @param language The file language
     * @throws GnossAPIException GnossAPIException
     * @throws IOException IOException
     */
    public void AttachFile(String downloadUrl, String filePredicate, OntologyEntity entity, String fileIdentifier, String language) throws GnossAPIException, IOException {
    	if(!downloadUrl.isEmpty()) {
    		byte[] file = ReadFile(downloadUrl);
    		String fileName="";
    		URI uri = URI.create(downloadUrl);
    		if(uri != null) {
    			fileName=(!(fileIdentifier).isEmpty() ? "fileIdentifier_" : "")  + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    		}
    		else {
    			File fichero= new File(downloadUrl);
    			if(fichero.exists()) {
    				if(downloadUrl.contains("/")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    				}else if(downloadUrl.contains("\\")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("\\")+1);
    				}
    			}else {
    				throw new GnossAPIArgumentException("The file "+downloadUrl+" doesn´t exist or is inaccessible");
    			}
    		}
    		AttachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, false, language);
    	}
    	else {
    		throw new GnossAPIArgumentException("Required. It can´t be null or empty");
    	}
    }
   
    /**
     * Attach a file (not an image, to attach an image use AttachImage method) to the resource. 
     * @param file The file bytes
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param fileName The file name
     * @param entity Auxiliary entity which would have the reference to the file
     * @throws GnossAPIException  GnossAPIException
     * @throws IOException IOException
     */
	public void AttachFile(byte[] file, String filePredicate, String fileName, OntologyEntity entity) throws IOException, GnossAPIException {
    	AttachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, (Boolean) null, null);
    }
    
	/**
	 * Attach a file (not an image, to attach an image use AttachImage method) to the resource that can be accessed from the Web. It means that the file won't be encripted in the server
	 * @param file Archivo a adjuntar (no imagen)
	 * @param filePredicate Predicate of the ontological property where the file reference will be inserted
	 * @param fileName Auxiliary entity which would have the reference to the file
	 * @param entity The file name
	 * @param language The file language
	 * @throws GnossAPIException GnossAPIException
	 * @throws IOException IOException
	 */
	public void AttachDownloadableFile(byte[] file, String filePredicate, String fileName, OntologyEntity entity, String language ) throws IOException, GnossAPIException {
		AttachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.downloadableFile, entity, false, language);
	}
	 
	/**
	 * Attach a reference to a file (not an image, to attach an image use AttachImageWithoutReference method) previously uploaded using the 
	 * @param downloadUrl Download Url. It can be a local path or a internet url
	 * @param filePredicate Predicate of the ontological property where the file reference will be inserted
	 * @param entity Auxiliary entity which would have the reference to the file
	 * @param fileIdentifier Unique identifier of the file. Only neccesary if there is more than one file with the same name
	 * @param language The file language
	 * @throws GnossAPIException GnossAPIException
	 * @throws IOException IOException
	 */
	public void AttachReferenceToFile(String downloadUrl, String filePredicate, OntologyEntity entity, String fileIdentifier, String language) throws IOException, GnossAPIException {
		if(!downloadUrl.isEmpty()) {
			String fileName="";
			URI uri = URI.create(downloadUrl);
    		if(uri != null) {
    			fileName=(!(fileIdentifier).isEmpty() ? "fileIdentifier_" : "")  + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    		}
    		else {
    			File fichero= new File(downloadUrl);
    			if(fichero.exists()) {
    				if(downloadUrl.contains("/")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    				}else if(downloadUrl.contains("\\")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("\\")+1);
    				}
    			}else {
    				throw new GnossAPIArgumentException("The file "+downloadUrl+" doesn´t exist or is inaccessible");
    			}
    		}
    		if(language!=null) {
    			fileName=fileName+"@"+language;
    		}
    		AttachFileInternal(null, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, true, null);
    	}
    	else {
    		throw new GnossAPIArgumentException("Required. It can´t be null or empty");
    	}
	}
	
	/**
	 * Attach a reference to a file (not an image, to attach an image use AttachImageWithoutReference method) previusly uploaded using the 
	 * A downloadable file it's a file that can be accessed from the Web. It means that the file won't be encripted in the server
	 * @param downloadUrl Download Url. It can be a local path or a internet url
	 * @param filePredicate Predicate of the ontological property where the file reference will be inserted
	 * @param entity Auxiliary entity which would have the reference to the file
	 * @param fileIdentifier Unique identifier of the file. Only neccesary if there is more than one file with the same name
	 * @param language The file language
	 * @throws GnossAPIException GnossAPIException
	 * @throws IOException IOException
	 */
	public void AttachReferenceToDownloadableFile(String downloadUrl, String filePredicate, OntologyEntity entity, String fileIdentifier, String language) throws IOException, GnossAPIException {
		if(!downloadUrl.isEmpty()) {
			String fileName="";
			URI uri = URI.create(downloadUrl);
    		if(uri != null) {
    			fileName=(!(fileIdentifier).isEmpty() ? "fileIdentifier_" : "")  + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    		}
    		else {
    			File fichero= new File(downloadUrl);
    			if(fichero.exists()) {
    				if(downloadUrl.contains("/")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    				}else if(downloadUrl.contains("\\")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("\\")+1);
    				}
    			}else {
    				throw new GnossAPIArgumentException("The file "+downloadUrl+" doesn´t exist or is inaccessible");
    			}
    		}
    		if(language!=null) {
    			fileName=fileName+"@"+language;
    		}
    		AttachFileInternal(null, filePredicate, fileName, AttachedResourceFilePropertyTypes.downloadableFile, entity, true, null);
    	}
    	else {
    		throw new GnossAPIArgumentException("Required. It can´t be null or empty");
    	}
	}
	
	/**
	 * Uploads a file to the server, but it's not referenced by the resource
	 * @param downloadUrl Download Url. It can be a local path or a internet url
	 * @param fileIdentifier Unique identifier of the file. Only neccesary if there is more than one file with the same name<
	 * @param language The file language
	 * @throws GnossAPIException GnossAPIException
	 * @throws IOException IOException
	 */
	public void AttachFileWithoutReference(String downloadUrl, String fileIdentifier, String language) throws GnossAPIException, IOException {
		if(!downloadUrl.isEmpty()) {
			String fileName="";
			byte[] file = ReadFile(downloadUrl);
            
			URI uri = URI.create(downloadUrl);
    		if(uri != null) {
    			fileName=(!(fileIdentifier).isEmpty() ? "fileIdentifier_" : "")  + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    		}
    		else {
    			File fichero= new File(downloadUrl);
    			if(fichero.exists()) {
    				if(downloadUrl.contains("/")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    				}else if(downloadUrl.contains("\\")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("\\")+1);
    				}
    			}else {
    				throw new GnossAPIArgumentException("The file "+downloadUrl+" doesn´t exist or is inaccessible");
    			}
    		}
    		if(language!=null) {
    			fileName=fileName+"@"+language;
    		}
    		AttachFileInternal(file, null, fileName, AttachedResourceFilePropertyTypes.downloadableFile, null, true, null);
    	}
    	else {
    		throw new GnossAPIArgumentException("Required. It can´t be null or empty");
    	}
	}
	
	/**
	 * Uploads a file to the server, but it's not referenced by the resource. 
	 * A downloadable file it's a file that can be accessed from the Web. It means that the file won't be encripted in the server
	 * @param downloadUrl Download Url. It can be a local path or a internet url
	 * @param fileIdentifier Unique identifier of the file. Only neccesary if there is more than one file with the same name
	 * @param language The file language
	 * @throws GnossAPIException GnossAPIException
	 * @throws IOException IOException
	 */
	public void AttachDownloadableFileWithoutRederence(String downloadUrl, String fileIdentifier, String language) throws GnossAPIException, IOException {
		if(!downloadUrl.isEmpty()) {
			String fileName="";
			byte[] file = ReadFile(downloadUrl);
            
			URI uri = URI.create(downloadUrl);
    		if(uri != null) {
    			fileName=(!(fileIdentifier).isEmpty() ? "fileIdentifier_" : "")  + downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    		}
    		else {
    			File fichero= new File(downloadUrl);
    			if(fichero.exists()) {
    				if(downloadUrl.contains("/")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
    				}else if(downloadUrl.contains("\\")) {
    					fileName=downloadUrl.substring(downloadUrl.lastIndexOf("\\")+1);
    				}
    			}else {
    				throw new GnossAPIArgumentException("The file "+downloadUrl+" doesn´t exist or is inaccessible");
    			}
    		}
    		if(language!=null) {
    			fileName=fileName+"@"+language;
    		}
    		AttachFileInternal(file, null, fileName, AttachedResourceFilePropertyTypes.downloadableFile, null, (Boolean) null, null);
    	}
    	else {
    		throw new GnossAPIArgumentException("Required. It can´t be null or empty");
    	}
	}
	
	
	
    private void AttachFileInternal(byte[] file, String filePredicate, String fileName,
			AttachedResourceFilePropertyTypes fileType, OntologyEntity entity, boolean onlyReference, String language) throws IOException, GnossAPIException {
		// TODO Auto-generated method stub
		if(file!=null) {
			if(onlyReference==false) {
				AttachedFiles.add(file);
				
				String temporalFileName=fileName;
				
				if(!language.isEmpty()) {
					temporalFileName+= "@"+language;
				}
				AttachedFilesName.add(temporalFileName);
				
				if(fileType==AttachedResourceFilePropertyTypes.file) {
					AttachedFilesType.add(AttachedResourceFilePropertyTypes.file);
				}else {
					AttachedFilesType.add(AttachedResourceFilePropertyTypes.downloadableFile);
				}
			}
		}
		if(entity!=null) {
			if(getOntology().getEntities()==null) {
				getOntology().setEntities(new ArrayList<OntologyEntity>());
				getOntology().getEntities().add(entity);
			}
			if(AttachedFilesType.size()>0 && AttachedFiles.size()>0 && AttachedFilesType.size()>0) {
				entity.getProperties().add(new StringOntologyProperty(filePredicate, fileName, language));
			}
		}
		else {
			if(filePredicate!=null) {
				if(getOntology().getProperties()==null) {
					getOntology().setProperties(new ArrayList<OntologyProperty>());
				}
				getOntology().getProperties().add(new StringOntologyProperty(filePredicate, fileName, language));
			}
		}
		_rdfFile=getOntology().GenerateRDF();
	}
    
    
    public boolean AttachImage (String downloadUrl, ArrayList<ImageAction> actions, String predicate, boolean mainImage, String extension, OntologyEntity entity, boolean saveOriginalImage, boolean saveMaxSizedImage) throws GnossAPIException, IOException {
    	BufferedImage imagenOriginal = _imageHelper.ReadImageFromUrlOrLocalPath(downloadUrl);
    	boolean imagenAdjuntada=false;
    	
    	if(imagenOriginal!=null) {
    		imagenAdjuntada=AttachImageInternal(ImageHelper.BitmapToByteArray(imagenOriginal), actions, predicate, mainImage, UUID.fromString(""), false, entity, extension, saveOriginalImage, saveMaxSizedImage, (Boolean) null);
    	}
    	
    	return imagenAdjuntada;
    }
    
    public boolean AttachReferenceToImage(ArrayList<ImageAction> actions, String predicate, boolean mainImage, UUID imageId, String extension, OntologyEntity entity ) throws GnossAPIException, IOException
    {
        return AttachImageInternal(null, actions, predicate, mainImage, imageId, true, entity, extension, false, false, false);
    }
    

	
	
	public boolean AttachImageWithoutReference(String downloadUrl, ArrayList<ImageAction> actions, boolean mainImage, UUID imageId, String extension, boolean saveOriginalImage, boolean saveMaxSizedImage, boolean saveMainImage) throws GnossAPIException {
		BufferedImage orginalImage= _imageHelper.ReadImageFromUrlOrLocalPath(downloadUrl);
		boolean success = false;
		
		if(orginalImage!=null) {
			try {
				success=AttachImageInternal(ImageHelper.BitmapToByteArray(orginalImage), actions, "", mainImage, imageId, false, null, extension, saveOriginalImage, saveMaxSizedImage, saveMainImage);
				
			}catch(Exception ex) {
				_logHelper.Error("Error attaching image "+downloadUrl+": "+ex.getMessage());
			}
		}
		return success;
	}
	
	public boolean AttachImageWithoutReference(byte[] originalImage, ArrayList<ImageAction> actions, boolean mainImage, UUID imageId, String extension, boolean saveOriginalImage , boolean saveMaxSizedImage) throws GnossAPIException, IOException
    {
        boolean imagenAdjuntada = false;
        if (originalImage != null)
        {
            imagenAdjuntada = AttachImageInternal(originalImage, actions, "", mainImage, imageId, false, null, extension, saveOriginalImage, saveMaxSizedImage, (Boolean) null);
        }

        return imagenAdjuntada;
    }

	
    
	 public boolean AttachImage(byte[] originalImage, ArrayList<ImageAction> actions, String predicate, boolean mainImage, String extension, OntologyEntity entity , boolean saveOriginalImage , boolean saveMaxSizedImage ) throws GnossAPIException, IOException
     {
         boolean success = false;
         if (originalImage != null)
         {
             success = AttachImageInternal(originalImage, actions, predicate, mainImage, UUID.fromString(""), false, entity, extension, saveOriginalImage, saveMaxSizedImage, (Boolean) null);
         }
         return success;
     }
	 
	 public  boolean AttachImageInternal(byte[] originalImage, ArrayList<ImageAction> actions, String predicate,
				boolean mainImage, UUID imageID, boolean onlyReference, OntologyEntity entity, String extension,
				boolean saveOriginalImage, boolean saveMaxSizedImage, boolean saveMainImage) throws GnossAPIException, IOException {
			// TODO Auto-generated method stub
		 
		 if(!extension.isEmpty()) {
			 extension=".jpg";
		 }
		 else {
			 if(!extension.startsWith(".")) {
				 extension="."+extension;
			 }
		 }
		 if(imageID.equals(UUID.fromString(""))) {
			 imageID=UUID.randomUUID();
		 }
		 
		 boolean attachedImage=false;
		 
		 try {
			 ArrayList<String> errorList= new ArrayList<String>();
			 if(actions==null) {
				 if(mainImage && saveOriginalImage) {
					 setMainImage(String.format("[IMGPrincipal][240,]{0}"+extension, imageID));
					 AttachedFilesName.add(imageID +extension );
					 AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
					 AttachedFilesName.add(imageID+"_240"+ extension);
					 AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
					 
					 if(!onlyReference) {
						 AttachedFiles.add(originalImage);
                         AttachedFiles.add(originalImage);
                         AttachedFiles.add(originalImage);
					 }
					 attachedImage = true;
				 }
				 else if(saveOriginalImage) {
					 AttachedFilesName.add(imageID + extension);
                     AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);

                     if (!onlyReference)
                     {
                         AttachedFiles.add(originalImage);
                     }
                     attachedImage = true;
				 }
				 else
                 {
                     throw new GnossAPIException("Actions can be null only when the image is a main image and the original image is set to be saved");
                 }
			 }else {
				 boolean originalImageSaved= false;
				 BufferedImage maxSizeImage= null;
				 boolean imageModificadorError= false;
				 
				 for(ImageAction action : actions) {
					 imageModificadorError = false;
					 BufferedImage resizedImage = null;
					 
					 if(!onlyReference) {
						 switch(action.getImegeTransformationType()) {
						 case Crop:
							 resizedImage=_imageHelper.CropImageToSquare(ImageHelper.ByteArrayToBitmap(originalImage), action.getSize());
							 break;
						 case ResizeToHeight:
							 resizedImage=_imageHelper.ResizeImageToHeight(ImageHelper.ByteArrayToBitmap(originalImage), (int) action.getHeight());
							 break;
						 case ResizeToWidth:
							 resizedImage=_imageHelper.ResizeImageToWidth(ImageHelper.ByteArrayToBitmap(originalImage), (int) action.getWidth());
							 break;
						 case ResizeToHeightAndWidth:
							 resizedImage=_imageHelper.ResizeImageToHeightAndWidth(ImageHelper.ByteArrayToBitmap(originalImage),(int) action.getWidth(), (int) action.getHeight());
							 break;
						 case CropToHeightAndWidth:
							 resizedImage=_imageHelper.CropImageToHeightAndWidth(ImageHelper.ByteArrayToBitmap(originalImage), (int) action.getHeight(), (int) action.getWidth());
							 break;
							 
						default : 
							throw new GnossAPIException("The ImageTransformationType is not valid");
						 }
					 }
					 if(mainImage && imageModificadorError) {
						 mainImage=false;
					 }
					 if(!imageModificadorError && !onlyReference) {
						 if(mainImage) {
							 setMainImage(String.format("[IMGPrincipal][{0},]{1}" + extension, action.getSize(), imageID));
							 mainImage=false;
						 }
						 AttachedFilesName.add(("imageId"+"_"+action.getSize()+extension));
						 AttachedFiles.add(ImageHelper.BitmapToByteArray(resizedImage));
						 AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
						 
						 if(saveOriginalImage && !originalImageSaved) {
							 if(saveMainImage) {
								 AttachedFilesName.add(("imageId"+"_"+action.getSize()+extension));
								 AttachedFiles.add(ImageHelper.BitmapToByteArray(ImageHelper.ByteArrayToBitmap(originalImage)));
								 AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
							 }
							 
							 originalImageSaved=true;
							 attachedImage=true;
						 }
					 }
					 else {
						 if(mainImage) {
							 setMainImage(String.format("[IMGPrincipal][{0},]{1}" + extension, action.getSize(), imageID));
							 mainImage=false;
						 }
						 attachedImage=true;
					 }
				 }
				 if(saveMaxSizedImage) {
					 if(ImageHelper.ByteArrayToBitmap(originalImage).getWidth()> Constants.MAXIMUM_WIDTH_GNOSS_IMAGE){
						 maxSizeImage=ImageHelper.ResizeImageToWidth(ImageHelper.ByteArrayToBitmap(originalImage), Constants.MAXIMUM_WIDTH_GNOSS_IMAGE);
						 
						 AttachedFilesName.add(imageID+"_"+Constants.MAXIMUM_WIDTH_GNOSS_IMAGE+extension);
						 AttachedFiles.add(ImageHelper.BitmapToByteArray(maxSizeImage));
						 AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
						 attachedImage=true;
					 }
					 else {
						 AttachedFilesName.add(imageID+"_"+Constants.MAXIMUM_WIDTH_GNOSS_IMAGE+extension);
						 AttachedFiles.add(ImageHelper.BitmapToByteArray(ImageHelper.ByteArrayToBitmap(originalImage)));
						 AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
						 attachedImage=true;
					 }
				 }
			 }
			 if(entity!=null && !predicate.isEmpty() && !StringUtils.isBlank(predicate)) {
				 if(getOntology().getEntities()==null) {
					 getOntology().setEntities(new ArrayList<OntologyEntity>());
					 getOntology().getEntities().add(entity);
				 }
				 if(!onlyReference) {
					 if(AttachedFiles.size()>0 && AttachedFilesType.size()>0) {
						 if(entity.getProperties()==null) {
						 entity.setProperties(new ArrayList<OntologyProperty>());
					 }
						
					 entity.getProperties().add(new ImageOntologyProperty(predicate, imageID+extension));
					}
				} else {
					if(attachedImage) {
						if(entity.getProperties()==null) {
							entity.setProperties(new ArrayList<OntologyProperty>());
						}
						entity.getProperties().add(new ImageOntologyProperty(predicate, imageID+extension));
					}

				}
			 }
			 else {
				 if(!predicate.isEmpty() && !StringUtils.isBlank(predicate)) {
					 if(getOntology().getProperties()==null) {
						 getOntology().setProperties(new ArrayList<OntologyProperty>());
					 }
					 if(!onlyReference) {
						 if(AttachedFilesType.size()>0 && AttachedFiles.size()>0) {
							 ImageOntologyProperty pOntImg = new ImageOntologyProperty(predicate, imageID+extension);
							 getOntology().getProperties().add(pOntImg);
						 }
					 }else {
						 if(attachedImage) {
							 getOntology().getProperties().add(new ImageOntologyProperty(predicate, imageID+extension));
						 }
					 }
				 }
			 }
			 if(errorList!=null && errorList.size()>0) {
				 String message=null;
				 for(String error : errorList) {
					 if(message==null) {
						 message=error;
					 }else {
						 message=message+"\n"+error;
					 }
				 }
				 throw new GnossAPIException(message);
			 }
			 
		 }catch(GnossAPIException ex) {
			 throw new GnossAPIException("Error");
			 
		 }
		 	_rdfFile=getOntology().GenerateRDF();
			return attachedImage;
		}
	 
}
    
    
    
    
    
    
    
    
    
    
    
    

