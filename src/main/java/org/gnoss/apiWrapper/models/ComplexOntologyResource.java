package org.gnoss.apiWrapper.models;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.ApiModel.AttachedResourceFilePropertyTypes;
import org.gnoss.apiWrapper.ApiModel.AumentedReading;
import org.gnoss.apiWrapper.ApiModel.ResourceVisibility;
import org.gnoss.apiWrapper.Excepciones.GnossAPIArgumentException;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.Constants;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.ImageHelper;
import org.gnoss.apiWrapper.Helpers.ImageTransformationType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.net.URI;

/**
 * Represents a resource based on a complex ontology
 * @author jruiz
 */
public class ComplexOntologyResource extends BaseResource {
    
    // Members
    private byte[] _rdfFile;
    private String _stringRdfFile;
    private Ontology _ontology;
    private ImageHelper _imageHelper;
    private ILogHelper _logHelper;
    private AumentedReading _aumentedReading;
    
    // Properties
    private String PublisherEmail;
    private String MainImage;
    private Boolean permitir_compartidos;
    
    // Attached files
    private ArrayList<String> AttachedFilesName;
    private ArrayList<AttachedResourceFilePropertyTypes> AttachedFilesType;
    private ArrayList<byte[]> AttachedFiles;
    
    // Screenshots
    private boolean MustGenerateScreenshot;
    private String ScreenshotUrl;
    private String ScreenshotPredicate;
    private int[] ScreenshotSizes;
    
    
    
    // Constructors
    
    /**
     * Empty constructor
     */
    public ComplexOntologyResource() {
        initialize();
    }
    
    /**
     * Constructor of ComplexOntologyResource, assigning only the GnossId property
     * @param largeGnossId Gnoss identifier used as subject in the ontology graph
     * @throws GnossAPIArgumentException if largeGnossId is null
     */
    public ComplexOntologyResource(String largeGnossId) throws GnossAPIArgumentException {
        initialize();
        if (largeGnossId != null) {
            setGnossId(largeGnossId);
        } else {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "largeGnossId");
        }
    }
    
    /**
     * Constructor of ComplexOntologyResource, assigning only the ShortGnossId property
     * @param shortGnossId Internal GNOSS identifier used as subject in the search graph
     */
    public ComplexOntologyResource(UUID shortGnossId) {
        initialize();
        setShortGnossId(shortGnossId);
    }
    
    
    // Getters and Setters
    
    public byte[] getRdfFile() throws IOException, GnossAPIException {
        if (_rdfFile == null || _rdfFile.length == 0) {
            _rdfFile = this._ontology.generateRDF();
        }
        return _rdfFile;
    }

    public void setRdfFile(byte[] rdfFile) {
        this._rdfFile = rdfFile;
    }

    public String getStringRdfFile() throws IOException, GnossAPIException {
        if (_rdfFile == null || _rdfFile.length == 0) {
            _rdfFile = this._ontology.generateRDF();
        }
        return new String(_rdfFile);
    }

    public String getPublisherEmail() {
        return PublisherEmail;
    }

    public void setPublisherEmail(String publisherEmail) {
        this.PublisherEmail = publisherEmail;
    }

    public String getMainImage() {
        return MainImage;
    }

    public void setMainImage(String mainImage) {
        this.MainImage = mainImage;
    }

    public Ontology getOntology() {
        return _ontology;
    }

    public void setOntology(Ontology ontology) throws IOException, GnossAPIException {
        this._ontology = ontology;
        _rdfFile = _ontology.generateRDF();
        setShortGnossId(_ontology.getResourceId());
        setGnossId(_ontology.getIdentifier());
    }

    public void setAumentedReading(AumentedReading aumentedReading) {
    	this._aumentedReading = aumentedReading;
    }
    
    public Boolean getPermitirCompartidos() {
        return permitir_compartidos;
    }

    public void setPermitirCompartidos(Boolean permitirCompartidos) {
        this.permitir_compartidos = permitirCompartidos;
    }

    public ArrayList<String> getAttachedFilesName() {
        return AttachedFilesName;
    }

    public ArrayList<AttachedResourceFilePropertyTypes> getAttachedFilesType() {
        return AttachedFilesType;
    }

    public ArrayList<byte[]> getAttachedFiles() {
        return AttachedFiles;
    }

    public boolean getMustGenerateScreenshot() {
        return MustGenerateScreenshot;
    }

    public String getScreenshotUrl() {
        return ScreenshotUrl;
    }

    public String getScreenshotPredicate() {
        return ScreenshotPredicate;
    }

    public int[] getScreenshotSizes() {
        return ScreenshotSizes;
    }

    public AumentedReading getAumentedReading() {
    	return _aumentedReading;
    }
    
    // Public methods
    
    /**
     * Prepares the screenshot of a url with the specified sizes. The screenshot will be assigned to imagePredicate property
     * @param screenshotUrl Url to generate the screenshot
     * @param imagePredicate Predicate where the screenshot url will be saved
     * @param screenshotSizes Screenshot sizes. It will generate as many screenshots as sizes in this array
     */
    public void generateScreenshot(String screenshotUrl, String imagePredicate, int[] screenshotSizes) {
        MustGenerateScreenshot = true;
        ScreenshotUrl = screenshotUrl;
        ScreenshotPredicate = imagePredicate;
        ScreenshotSizes = screenshotSizes;
    }
    
    /**
     * Gets a ComplexOntologyResource as a string
     * @return String with the information of this ComplexOntologyResource
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("-------------------------------------------------------------\n");
        sb.append("Resource:\n");
        sb.append("\t\tId: ").append(getShortGnossId()).append("\n");
        sb.append("\t\tGnossId: ").append(getGnossId()).append("\n");
        sb.append("\t\tDescription: ").append(getDescription()).append("\n");
        sb.append("\t\tTags: ").append(Arrays.toString(getTags())).append("\n");
        sb.append("\t\tTextCategories: ").append(getCategoriesIds().toString()).append("\n");
        sb.append("\t\tAuthor: ").append(getAuthor()).append("\n");
        sb.append("-------------------------------------------------------------\n");
        
        return sb.toString();
    }
    
    /**
     * Attach a file (not an image, to attach an image use AttachImage method) to the resource. 
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param entity (Optional) Auxiliary entity which would have the reference to the file
     * @param fileIdentifier (Optional) Unique identifier of the file. Only necessary if there is more than one file with the same name
     * @param language (Optional) The file language
     * @throws GnossAPIException if the file doesn't exist
     * @throws IOException if there's an I/O error
     */
    public void attachFile(String downloadUrl, String filePredicate, OntologyEntity entity, String fileIdentifier, String language) throws GnossAPIException, IOException {
        if (!StringUtils.isEmpty(downloadUrl)) {
            byte[] file = readFile(downloadUrl);
            String fileName = "";
            
            if (isWellFormedUri(downloadUrl)) {
                fileName = (!StringUtils.isEmpty(fileIdentifier) ? fileIdentifier + "_" : "") + 
                          downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            } else {
                File fichero = new File(downloadUrl);
                if (fichero.exists()) {
                    if (downloadUrl.contains("/")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                    } else if (downloadUrl.contains("\\")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("\\") + 1);
                    }
                } else {
                    throw new GnossAPIException("The file: " + downloadUrl + " doesn't exist or is inaccessible");
                }
            }
            
            attachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, false, language);
        } else {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "downloadUrl");
        }
    }
   
    /**
     * Attach a file (not an image, to attach an image use AttachImage method) to the resource. 
     * @param file The file bytes
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param fileName The file name
     * @param entity (Optional) Auxiliary entity which would have the reference to the file
     * @throws GnossAPIException if there's an error
     * @throws IOException if there's an I/O error
     */
    public void attachFile(byte[] file, String filePredicate, String fileName, OntologyEntity entity) throws IOException, GnossAPIException {
        attachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, false, null);
    }
    
    /**
     * Attach a file (not an image, to attach an image use AttachImage method) to the resource. 
     * @param file The file bytes
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param fileName The file name
     * @param entity (Optional) Auxiliary entity which would have the reference to the file
     * @throws GnossAPIException if there's an error
     * @throws IOException if there's an I/O error
     */
    public void attachFile(byte[] file, String filePredicate, String fileName, OntologyEntity entity, String language) throws IOException, GnossAPIException {
        attachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, false, language);
    }
    
    /**
     * Attach a file (not an image, to attach an image use AttachImage method) to the resource that can be accessed from the Web. 
     * It means that the file won't be encrypted in the server
     * @param file File to attach (not image)
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param fileName The file name
     * @param entity (Optional) Auxiliary entity which would have the reference to the file
     * @param language (Optional) The file language
     * @throws GnossAPIException if there's an error
     * @throws IOException if there's an I/O error
     */
    public void attachDownloadableFile(byte[] file, String filePredicate, String fileName, OntologyEntity entity, String language) throws IOException, GnossAPIException {
        attachFileInternal(file, filePredicate, fileName, AttachedResourceFilePropertyTypes.downloadableFile, entity, false, language);
    }
     
    /**
     * Attach a reference to a file (not an image, to attach an image use AttachImageWithoutReference method) previously uploaded using the AttachFileWithoutReference method
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param entity (Optional) Auxiliary entity which would have the reference to the file
     * @param fileIdentifier (Optional) Unique identifier of the file. Only necessary if there is more than one file with the same name
     * @param language (Optional) The file language
     * @throws GnossAPIException if the file doesn't exist
     * @throws IOException if there's an I/O error
     */
    public void attachReferenceToFile(String downloadUrl, String filePredicate, OntologyEntity entity, String fileIdentifier, String language) throws IOException, GnossAPIException {
        if (!StringUtils.isEmpty(downloadUrl)) {
            String fileName = "";
            
            if (isWellFormedUri(downloadUrl)) {
                fileName = (!StringUtils.isEmpty(fileIdentifier) ? fileIdentifier + "_" : "") + 
                          downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            } else {
                File fichero = new File(downloadUrl);
                if (fichero.exists()) {
                    if (downloadUrl.contains("/")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                    } else if (downloadUrl.contains("\\")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("\\") + 1);
                    }
                } else {
                    throw new GnossAPIException("The file: " + downloadUrl + " doesn't exist or is inaccessible");
                }
            }
            
            if (language != null) {
                fileName = fileName + "@" + language;
            }
            
            attachFileInternal(null, filePredicate, fileName, AttachedResourceFilePropertyTypes.file, entity, true, null);
        } else {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "downloadUrl");
        }
    }
    
    /**
     * Attach a reference to a file (not an image, to attach an image use AttachImageWithoutReference method) previously uploaded using the AttachFileWithoutReference method
     * A downloadable file is a file that can be accessed from the Web. It means that the file won't be encrypted in the server
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param filePredicate Predicate of the ontological property where the file reference will be inserted
     * @param entity (Optional) Auxiliary entity which would have the reference to the file
     * @param fileIdentifier (Optional) Unique identifier of the file. Only necessary if there is more than one file with the same name
     * @param language (Optional) The file language
     * @throws GnossAPIException if the file doesn't exist
     * @throws IOException if there's an I/O error
     */
    public void attachReferenceToDownloadableFile(String downloadUrl, String filePredicate, OntologyEntity entity, String fileIdentifier, String language) throws IOException, GnossAPIException {
        if (!StringUtils.isEmpty(downloadUrl)) {
            String fileName = "";
            
            if (isWellFormedUri(downloadUrl)) {
                fileName = (!StringUtils.isEmpty(fileIdentifier) ? fileIdentifier + "_" : "") + 
                          downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            } else {
                File fichero = new File(downloadUrl);
                if (fichero.exists()) {
                    if (downloadUrl.contains("/")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                    } else if (downloadUrl.contains("\\")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("\\") + 1);
                    }
                } else {
                    throw new GnossAPIException("The file: " + downloadUrl + " doesn't exist or is inaccessible");
                }
            }
            
            if (language != null) {
                fileName = fileName + "@" + language;
            }
            
            attachFileInternal(null, filePredicate, fileName, AttachedResourceFilePropertyTypes.downloadableFile, entity, true, null);
        } else {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "downloadUrl");
        }
    }
    
    /**
     * Uploads a file to the server, but it's not referenced by the resource
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param fileIdentifier (Optional) Unique identifier of the file. Only necessary if there is more than one file with the same name
     * @param language (Optional) The file language
     * @throws GnossAPIException if the file doesn't exist
     * @throws IOException if there's an I/O error
     */
    public void attachFileWithoutReference(String downloadUrl, String fileIdentifier, String language) throws GnossAPIException, IOException {
        if (!StringUtils.isEmpty(downloadUrl)) {
            byte[] file = readFile(downloadUrl);
            String fileName = "";
            
            if (isWellFormedUri(downloadUrl)) {
                fileName = (!StringUtils.isEmpty(fileIdentifier) ? fileIdentifier + "_" : "") + 
                          downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            } else {
                File fichero = new File(downloadUrl);
                if (fichero.exists()) {
                    if (downloadUrl.contains("/")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                    } else if (downloadUrl.contains("\\")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("\\") + 1);
                    }
                } else {
                    throw new GnossAPIException("The file: " + downloadUrl + " doesn't exist or is inaccessible");
                }
            }
            
            if (language != null) {
                fileName = fileName + "@" + language;
            }
            
            attachFileInternal(file, null, fileName, AttachedResourceFilePropertyTypes.file, null, false, null);
        } else {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "downloadUrl");
        }
    }
    
    /**
     * Uploads a file to the server, but it's not referenced by the resource. 
     * A downloadable file is a file that can be accessed from the Web. It means that the file won't be encrypted in the server
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param fileIdentifier (Optional) Unique identifier of the file. Only necessary if there is more than one file with the same name
     * @param language (Optional) The file language
     * @throws GnossAPIException if the file doesn't exist
     * @throws IOException if there's an I/O error
     */
    public void attachDownloadableFileWithoutReference(String downloadUrl, String fileIdentifier, String language) throws GnossAPIException, IOException {
        if (!StringUtils.isEmpty(downloadUrl)) {
            byte[] file = readFile(downloadUrl);
            String fileName = "";
            
            if (isWellFormedUri(downloadUrl)) {
                fileName = (!StringUtils.isEmpty(fileIdentifier) ? fileIdentifier + "_" : "") + 
                          downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
            } else {
                File fichero = new File(downloadUrl);
                if (fichero.exists()) {
                    if (downloadUrl.contains("/")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                    } else if (downloadUrl.contains("\\")) {
                        fileName = downloadUrl.substring(downloadUrl.lastIndexOf("\\") + 1);
                    }
                } else {
                    throw new GnossAPIException("The file: " + downloadUrl + " doesn't exist or is inaccessible");
                }
            }
            
            if (language != null) {
                fileName = fileName + "@" + language;
            }
            
            attachFileInternal(file, null, fileName, AttachedResourceFilePropertyTypes.downloadableFile, null, false, null);
        } else {
            throw new GnossAPIArgumentException("Required. It can't be null or empty", "downloadUrl");
        }
    }
    
    /**
     * Attach an image to the resource. It generates as many images as actions contains the actions parameter
     * @param downloadUrl Download url of the image. It can be a local path or a internet url
     * @param actions List of transformations to do over the image
     * @param predicate Predicate of the ontological property where the image reference will be inserted
     * @param mainImage True if this image is the resource main image
     * @param extension Image extension
     * @param entity (Optional) Auxiliary entity which would have the reference to the image
     * @param saveOriginalImage True if the original file must be saved
     * @param saveMaxSizedImage True if the max sized image must be saved
     * @return True if the image has been attached successfully
     * @throws Exception 
     */
    public boolean attachImage(String downloadUrl, ArrayList<ImageAction> actions, String predicate, boolean mainImage, String extension, OntologyEntity entity, boolean saveOriginalImage, boolean saveMaxSizedImage) throws Exception {
        BufferedImage imagenOriginal = _imageHelper.readImageFromUrlOrLocalPath(downloadUrl);
        boolean imagenAdjuntada = false;
        
        if (imagenOriginal != null) {
            imagenAdjuntada = attachImageInternal(ImageHelper.bitmapToByteArray(imagenOriginal), actions, predicate, mainImage, null, false, entity, extension, saveOriginalImage, saveMaxSizedImage, true);
        }
        
        return imagenAdjuntada;
    }
    
    /**
     * Attach an image to the resource previously uploaded using the AttachImageWithoutReference method 
     * @param actions List of transformations to do over the image
     * @param predicate Predicate of the ontological property where the image reference will be inserted
     * @param mainImage True if this image is the resource main image
     * @param imageId Image identifier. If it's null, it would be automatically generated
     * @param extension Image extension
     * @param entity (Optional) Auxiliary entity which would have the reference to the image
     * @return True if the image reference has been attached successfully
     * @throws Exception 
     */
    public boolean attachReferenceToImage(ArrayList<ImageAction> actions, String predicate, boolean mainImage, UUID imageId, String extension, OntologyEntity entity) throws Exception {
        return attachImageInternal(null, actions, predicate, mainImage, imageId, true, entity, extension, false, false, false);
    }
    
    /**
     * Uploads an image to the server, but it's not referenced by the resource. It generates as many images as actions contains the actions parameter
     * @param downloadUrl Download Url. It can be a local path or a internet url
     * @param actions List of transformations to do over the image
     * @param mainImage True if this image is the resource main image
     * @param imageId Image identifier. If it's null, it would be automatically generated
     * @param extension Image extension
     * @param saveOriginalImage True if the original file must be saved
     * @param saveMaxSizedImage True if the max sized image must be saved
     * @param saveMainImage True if the main image must be saved
     * @return True if the image has been uploaded successfully
     * @throws GnossAPIException if there's an error
     */
    public boolean attachImageWithoutReference(String downloadUrl, ArrayList<ImageAction> actions, boolean mainImage, UUID imageId, String extension, boolean saveOriginalImage, boolean saveMaxSizedImage, boolean saveMainImage) throws GnossAPIException {
        BufferedImage originalImage = _imageHelper.readImageFromUrlOrLocalPath(downloadUrl);
        boolean success = false;
        
        if (originalImage != null) {
            try {
                success = attachImageInternal(ImageHelper.bitmapToByteArray(originalImage), actions, "", mainImage, imageId, false, null, extension, saveOriginalImage, saveMaxSizedImage, saveMainImage);
            } catch (Exception ex) {
                throw new GnossAPIException("Error attaching image " + downloadUrl + ": " + ex.getMessage());
            }
        }
        return success;
    }
    
    /**
     * Uploads an image to the server, but it's not referenced by the resource. It generates as many images as actions contains the actions parameter
     * @param originalImage Image to upload
     * @param actions List of transformations to do over the image
     * @param mainImage True if this image is the resource main image
     * @param imageId Image identifier. If it's null, it would be automatically generated
     * @param extension Image extension
     * @param saveOriginalImage True if the original file must be saved
     * @param saveMaxSizedImage True if the max sized image must be saved
     * @return True if the image has been uploaded successfully
     * @throws Exception 
     */
    public boolean attachImageWithoutReference(byte[] originalImage, ArrayList<ImageAction> actions, boolean mainImage, UUID imageId, String extension, boolean saveOriginalImage, boolean saveMaxSizedImage) throws Exception {
        boolean imagenAdjuntada = false;
        if (originalImage != null) {
            imagenAdjuntada = attachImageInternal(originalImage, actions, "", mainImage, imageId, false, null, extension, saveOriginalImage, saveMaxSizedImage, true);
        }
        return imagenAdjuntada;
    }
    
    /**
     * Attach an image to the resource. It generates as many images as actions contains the actions parameter
     * @param originalImage Image to upload, null if it would be only a reference
     * @param actions List of ImageAction
     * @param predicate Ontology predicate
     * @param mainImage True if the image is the resource main image
     * @param extension Image extension
     * @param entity (Optional) The auxiliary entity that owns the image property
     * @param saveOriginalImage True if the original file must be saved
     * @param saveMaxSizedImage True if the max sized image must be saved
     * @return True if the image reference has been attached successfully
     * @throws Exception 
     */
    public boolean attachImage(byte[] originalImage, ArrayList<ImageAction> actions, String predicate, boolean mainImage, String extension, OntologyEntity entity, boolean saveOriginalImage, boolean saveMaxSizedImage) throws Exception {
        boolean success = false;
        if (originalImage != null) {
            success = attachImageInternal(originalImage, actions, predicate, mainImage, null, false, entity, extension, saveOriginalImage, saveMaxSizedImage, true);
        }
        return success;
    }

    
    // Private methods
    
    private void initialize() {
        setAuthor(null);
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
    }
    
    /**
     * Attach a file
     * @param file File to attach
     * @param filePredicate Predicate of the ontological property
     * @param fileName File name
     * @param fileType File type
     * @param entity (Optional) The auxiliary entity that owns the file property
     * @param onlyReference True if the file mustn't be saved, only as a reference
     * @param language language of the file
     * @throws IOException if there's an I/O error
     * @throws GnossAPIException if there's an error
     */
    private void attachFileInternal(byte[] file, String filePredicate, String fileName, AttachedResourceFilePropertyTypes fileType, OntologyEntity entity, boolean onlyReference, String language) throws IOException, GnossAPIException {
        if (file != null) {
            if (!onlyReference) {
                AttachedFiles.add(file);
                
                String temporalFileName = fileName;
                if (!StringUtils.isEmpty(language)) {
                    temporalFileName += "@" + language;
                }
                
                AttachedFilesName.add(temporalFileName);
                
                if (fileType == AttachedResourceFilePropertyTypes.file) {
                    AttachedFilesType.add(AttachedResourceFilePropertyTypes.file);
                } else {
                    AttachedFilesType.add(AttachedResourceFilePropertyTypes.downloadableFile);
                }
            }
        }
        
        if (entity != null) {
            if (getOntology().getEntities() == null) {
                getOntology().setEntities(new ArrayList<OntologyEntity>());
                getOntology().getEntities().add(entity);
            }
            if (AttachedFilesType.size() > 0 && AttachedFiles.size() > 0 && AttachedFilesName.size() > 0) {
                entity.getProperties().add(new StringOntologyProperty(filePredicate, fileName, language));
            }
        } else {
            if (filePredicate != null) {
                if (getOntology().getProperties() == null) {
                    getOntology().setProperties(new ArrayList<OntologyProperty>());
                }
                getOntology().getProperties().add(new StringOntologyProperty(filePredicate, fileName, language));
            }
        }
        
        if (getOntology() != null) {
            _rdfFile = getOntology().generateRDF();
        }
    }
    
    /**
     * Attach an image
     * @param originalImage Image to upload, null if it would be only a reference
     * @param actions List of ImageAction
     * @param predicate Ontology predicate
     * @param mainImage True if the image is the resource main image
     * @param imageId Image identifier. If it's null, it would be automatically generated
     * @param onlyReference True if the image mustn't be saved, only as a reference
     * @param entity (Optional) The auxiliary entity that owns the image property
     * @param extension Image extension
     * @param saveOriginalImage True if the original file must be saved
     * @param saveMaxSizedImage True if the max sized image must be saved
     * @param saveMainImage True if the main image must be saved
     * @return True if the image reference has been attached successfully
     * @throws Exception 
     */
    private boolean attachImageInternal(byte[] originalImage, ArrayList<ImageAction> actions, String predicate, 
                                       boolean mainImage, UUID imageId, boolean onlyReference, 
                                       OntologyEntity entity, String extension, boolean saveOriginalImage, 
                                       boolean saveMaxSizedImage, boolean saveMainImage) 
                                       throws Exception {
        
        boolean attachedImage = false;
        
        if (extension.toLowerCase().equals(".png") || extension.toLowerCase().equals(".jpg") || 
            extension.toLowerCase().equals(".jpeg") || extension.toLowerCase().equals(".gif")) {
            
            if (StringUtils.isEmpty(extension)) {
                extension = ".jpg";
            } else {
                if (!extension.startsWith(".")) {
                    extension = "." + extension;
                }
            }
            
            if (imageId == null) {
                imageId = UUID.randomUUID();
            }
            
            try {
                ArrayList<String> errorList = new ArrayList<String>();
                
                if (actions == null || actions.size() == 0) {
                    // Without actions
                    if (mainImage && saveOriginalImage) {
                        // The image to upload has lower size than 240 pixels. The image quality would be 100%
                        MainImage = String.format("[IMGPrincipal][240,]%s%s", imageId, extension);
                        
                        AttachedFilesName.add(imageId + extension);
                        AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                        
                        AttachedFilesName.add(imageId + "_240" + extension);
                        AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                        
                        if (!onlyReference) {
                            AttachedFiles.add(originalImage);
                            AttachedFiles.add(originalImage);
                            AttachedFiles.add(originalImage);
                        }
                        attachedImage = true;
                    } else if (saveOriginalImage) {
                        AttachedFilesName.add(imageId + extension);
                        AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                        
                        if (!onlyReference) {
                            AttachedFiles.add(originalImage);
                        }
                        attachedImage = true;
                    } else {
                        throw new GnossAPIException("Actions can be null only when the image is a main image and the original image is set to be saved");
                    }
                } else {
                    // With actions
                    boolean originalImageSaved = false;
                    BufferedImage maxSizeImage = null;
                    boolean imageModificationError = false;
                    
                    for (ImageAction action : actions) {
                        imageModificationError = false;
                        BufferedImage resizedImage = null;
                        
                        if (!onlyReference) {
                            // Modify the image
                            switch (action.getImageTransformationType()) {
                                case Crop:
                                    try {
                                        resizedImage = ImageHelper.byteArrayToBitmap(originalImage);
                                        _imageHelper.cropImageToSquare(resizedImage, action.getSize());
                                    } catch (GnossAPIException gaex) {
                                        imageModificationError = true;
                                        errorList.add(gaex.getMessage());
                                    }
                                    break;
                                case ResizeToHeight:
                                    try {
                                        resizedImage = ImageHelper.byteArrayToBitmap(originalImage);
                                        _imageHelper.resizeImageToHeight(resizedImage, (int) action.getHeight());
                                    } catch (GnossAPIException gaex) {
                                        imageModificationError = true;
                                        errorList.add(gaex.getMessage());
                                    }
                                    break;
                                case ResizeToWidth:
                                    try {
                                        resizedImage = ImageHelper.byteArrayToBitmap(originalImage);
                                        _imageHelper.resizeImageToWidth(resizedImage, (int) action.getWidth());
                                    } catch (GnossAPIException gaex) {
                                        imageModificationError = true;
                                        errorList.add(gaex.getMessage());
                                    }
                                    break;
                                case ResizeToHeightAndWidth:
                                    try {
                                        resizedImage = ImageHelper.byteArrayToBitmap(originalImage);
                                        _imageHelper.resizeImageToHeightAndWidth(resizedImage, (int) action.getWidth(), (int) action.getHeight());
                                    } catch (GnossAPIException gaex) {
                                        imageModificationError = true;
                                        errorList.add(gaex.getMessage());
                                    }
                                    break;
                                case CropToHeightAndWidth:
                                    try {
                                        resizedImage = ImageHelper.byteArrayToBitmap(originalImage);
                                        _imageHelper.cropImageToHeightAndWidth(resizedImage, (int) action.getHeight(), (int) action.getWidth());
                                    } catch (GnossAPIException gaex) {
                                        imageModificationError = true;
                                        errorList.add(gaex.getMessage());
                                    }
                                    break;
                                default:
                                    throw new GnossAPIException("The ImageTransformationType is not valid");
                            }
                        }
                        
                        if (mainImage && imageModificationError) {
                            mainImage = false;
                        }
                        
                        if (!imageModificationError && !onlyReference) {
                            if (mainImage) {
                                MainImage = String.format("[IMGPrincipal][%d,]%s%s", action.getSize(), imageId, extension);
                                mainImage = false;
                            }
                            
                            AttachedFilesName.add(imageId + "_" + action.getSize() + extension);
                            AttachedFiles.add(ImageHelper.bitmapToByteArray(resizedImage, (int) action.getImageQualityPercentage()));
                            AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                            attachedImage = true;
                            
                            if (action.isEmbedsRGB()) {
                                _imageHelper.assignEXIFPropertyColorSpaceSRGB(resizedImage);
                            }
                            
                            // Save original image
                            if (saveOriginalImage && !originalImageSaved) {
                                if (saveMainImage) {
                                    AttachedFilesName.add(imageId + extension);
                                    int maxQuality = 0;
                                    for (ImageAction a : actions) {
                                        if (a.getImageQualityPercentage() > maxQuality) {
                                            maxQuality = (int) a.getImageQualityPercentage();
                                        }
                                    }
                                    AttachedFiles.add(ImageHelper.bitmapToByteArray(ImageHelper.byteArrayToBitmap(originalImage), maxQuality));
                                    AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                                }
                                
                                originalImageSaved = true;
                                attachedImage = true;
                            }
                        } else {
                            if (mainImage) {
                                MainImage = String.format("[IMGPrincipal][%d,]%s%s", action.getSize(), imageId, extension);
                                mainImage = false;
                            }
                            attachedImage = true;
                        }
                    }
                    
                    // Save the image at the max size allowed
                    if (saveMaxSizedImage) {
                        try {
                            // The quality percentage of the max size image is the highest quality percentage
                            if (ImageHelper.byteArrayToBitmap(originalImage).getWidth() > Constants.MAXIMUM_WIDTH_GNOSS_IMAGE) {
                                maxSizeImage = ImageHelper.byteArrayToBitmap(originalImage);
                                _imageHelper.resizeImageToWidth(maxSizeImage, Constants.MAXIMUM_WIDTH_GNOSS_IMAGE);
                                
                                int maxQuality = 0;
                                for (ImageAction a : actions) {
                                    if (a.getImageQualityPercentage() > maxQuality) {
                                        maxQuality = (int) a.getImageQualityPercentage();
                                    }
                                }
                                
                                AttachedFilesName.add(imageId + "_" + Constants.MAXIMUM_WIDTH_GNOSS_IMAGE + extension);
                                AttachedFiles.add(ImageHelper.bitmapToByteArray(maxSizeImage, maxQuality));
                                AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                                
                                attachedImage = true;
                            } else {
                                int maxQuality = 0;
                                for (ImageAction a : actions) {
                                    if (a.getImageQualityPercentage() > maxQuality) {
                                        maxQuality = (int) a.getImageQualityPercentage();
                                    }
                                }
                                
                                AttachedFilesName.add(imageId + "_" + Constants.MAXIMUM_WIDTH_GNOSS_IMAGE + extension);
                                AttachedFiles.add(ImageHelper.bitmapToByteArray(ImageHelper.byteArrayToBitmap(originalImage), maxQuality));
                                AttachedFilesType.add(AttachedResourceFilePropertyTypes.image);
                                
                                attachedImage = true;
                            }
                        } catch (GnossAPIException gaex) {
                            imageModificationError = true;
                            errorList.add(gaex.getMessage());
                        }
                    }
                }
                
                if (entity != null && !StringUtils.isEmpty(predicate)) {
                    // The image is from an auxiliary entity
                    if (getOntology().getEntities() == null) {
                        getOntology().setEntities(new ArrayList<OntologyEntity>());
                        getOntology().getEntities().add(entity);
                    }
                    
                    if (!onlyReference) {
                        if (AttachedFiles.size() > 0 && AttachedFilesType.size() > 0) {
                            if (entity.getProperties() == null) {
                                entity.setProperties(new ArrayList<OntologyProperty>());
                            }
                            entity.getProperties().add(new ImageOntologyProperty(predicate, imageId + extension));
                        }
                    } else {
                        if (attachedImage) {
                            if (entity.getProperties() == null) {
                                entity.setProperties(new ArrayList<OntologyProperty>());
                            }
                            entity.getProperties().add(new ImageOntologyProperty(predicate, imageId + extension));
                        }
                    }
                } else {
                    // The image is an ontology property
                    if (!StringUtils.isEmpty(predicate)) {
                        if (getOntology().getProperties() == null) {
                            getOntology().setProperties(new ArrayList<OntologyProperty>());
                        }
                        
                        if (!onlyReference) {
                            if (AttachedFilesType.size() > 0 && AttachedFiles.size() > 0) {
                                ImageOntologyProperty pOntImg = new ImageOntologyProperty(predicate, imageId + extension);
                                getOntology().getProperties().add(pOntImg);
                            }
                        } else {
                            if (attachedImage) {
                                getOntology().getProperties().add(new ImageOntologyProperty(predicate, imageId + extension));
                            }
                        }
                    }
                }
                
                if (errorList != null && errorList.size() > 0) {
                    StringBuilder message = new StringBuilder();
                    for (String error : errorList) {
                        if (message.length() == 0) {
                            message.append(error);
                        } else {
                            message.append("\n").append(error);
                        }
                    }
                    throw new GnossAPIException(message.toString());
                }
            } catch (GnossAPIException ex) {
                throw ex;
            }
            
            if (getOntology() != null) {
                _rdfFile = getOntology().generateRDF();
            }
            
        } else {
            throw new Exception("Los formatos permitidos son png, jpg, jpeg y gif. La extensión recibida es " + extension + ".");
        }
        
        return attachedImage;
    }
    
    private boolean isWellFormedUri(String uri) {
        try {
            URI u = URI.create(uri);
            return u.getScheme() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}