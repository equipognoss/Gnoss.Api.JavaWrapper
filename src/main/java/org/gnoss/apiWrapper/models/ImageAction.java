package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Helpers.ImageTransformationType;

/**
 * Represents a transformation to do over an image
 * @author Andrea
 */
public class ImageAction {

    private int size;
    private float height;
    private float width;
    private ImageTransformationType imageTransformationType;
    private long imageQualityPercentage;
    private boolean embedsRGB;
    
    
    // Constructors
    
    /**
     * Constructor of ImageAction
     * @param size Size in pixels of the width and height of the result image 
     * @param imageTransformationType Transformation to apply to the image
     * @param embedsRGB Embed color space
     */
    public ImageAction(int size, ImageTransformationType imageTransformationType, boolean embedsRGB) {
        this.size = size;
        this.width = size;
        this.height = size;
        this.imageTransformationType = imageTransformationType;
        this.imageQualityPercentage = 100;
        this.embedsRGB = embedsRGB;
    }
    
    /**
     * Constructor of ImageAction with default embedsRGB = false
     * @param size Size in pixels of the width and height of the result image 
     * @param imageTransformationType Transformation to apply to the image
     */
    public ImageAction(int size, ImageTransformationType imageTransformationType) {
        this(size, imageTransformationType, false);
    }
    
    /**
     * Constructor of ImageAction
     * @param size Size in pixels of the width and height of the result image 
     * @param imageTransformationType Transformation to apply to the image
     * @param imageQualityPercentage Minimum quality for the converted image (between 0 and 100)
     * @param embedsRGB Embed color space
     */
    public ImageAction(int size, ImageTransformationType imageTransformationType, long imageQualityPercentage, boolean embedsRGB) {
        this.size = size;
        this.width = size;
        this.height = size;
        this.imageTransformationType = imageTransformationType;
        this.imageQualityPercentage = imageQualityPercentage;
        this.embedsRGB = embedsRGB;
    }
    
    /**
     * Constructor of ImageAction with default embedsRGB = false
     * @param size Size in pixels of the width and height of the result image 
     * @param imageTransformationType Transformation to apply to the image
     * @param imageQualityPercentage Minimum quality for the converted image (between 0 and 100)
     */
    public ImageAction(int size, ImageTransformationType imageTransformationType, long imageQualityPercentage) {
        this(size, imageTransformationType, imageQualityPercentage, false);
    }
    
    /**
     * Constructor of ImageAction
     * @param width Width to resize
     * @param height Height to resize
     * @param imageTransformationType Transformation to apply to the image
     * @param embedsRGB Embed color space
     */
    public ImageAction(float width, float height, ImageTransformationType imageTransformationType, boolean embedsRGB) {
        this.size = (int) width;
        this.width = width;
        this.height = height;
        this.imageTransformationType = imageTransformationType;
        this.imageQualityPercentage = 100;
        this.embedsRGB = embedsRGB;
    }
    
    /**
     * Constructor of ImageAction with default embedsRGB = false
     * @param width Width to resize
     * @param height Height to resize
     * @param imageTransformationType Transformation to apply to the image
     */
    public ImageAction(float width, float height, ImageTransformationType imageTransformationType) {
        this(width, height, imageTransformationType, false);
    }
    
    /**
     * Constructor of ImageAction
     * @param width Width to resize
     * @param height Height to resize
     * @param imageTransformationType Transformation to apply to the image
     * @param imageQualityPercentage Minimum quality for the converted image (between 0 and 100)
     * @param embedsRGB Embed color space
     */
    public ImageAction(float width, float height, ImageTransformationType imageTransformationType, long imageQualityPercentage, boolean embedsRGB) {
        this.size = (int) width;
        this.width = width;
        this.height = height;
        this.imageTransformationType = imageTransformationType;
        this.imageQualityPercentage = imageQualityPercentage;
        this.embedsRGB = embedsRGB;
    }
    
    /**
     * Constructor of ImageAction with default embedsRGB = false
     * @param width Width to resize
     * @param height Height to resize
     * @param imageTransformationType Transformation to apply to the image
     * @param imageQualityPercentage Minimum quality for the converted image (between 0 and 100)
     */
    public ImageAction(float width, float height, ImageTransformationType imageTransformationType, long imageQualityPercentage) {
        this(width, height, imageTransformationType, imageQualityPercentage, false);
    }
    
    
    // Getters and Setters
    
    /**
     * Get the size in pixels of the width and height of the result image
     * @return int size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Set the size in pixels of the width and height of the result image
     * @param size size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Height, in pixels, that must have the image after the transformation
     * @return float height
     */
    public float getHeight() {
        return height;
    }
    
    /**
     * Height, in pixels, that must have the image after the transformation
     * @param height float
     */
    public void setHeight(float height) {
        this.height = height;
    }
    
    /**
     * Width, in pixels, that must have the image after the transformation
     * @return float width
     */
    public float getWidth() {
        return width;
    }
    
    /**
     * Width, in pixels, that must have the image after the transformation
     * @param width width
     */
    public void setWidth(float width) {
        this.width = width;
    }
    
    /**
     * Transformation to apply to the image
     * @return ImageTransformationType imageTransformationType
     */
    public ImageTransformationType getImageTransformationType() {
        return imageTransformationType;
    }
    
    /**
     * Transformation to apply to the image
     * @param imageTransformationType image Transformation Type 
     */
    public void setImageTransformationType(ImageTransformationType imageTransformationType) {
        this.imageTransformationType = imageTransformationType;
    }
    
    /**
     * Minimum quality for the converted image (between 0 and 100)
     * @return long imageQualityPercentage
     */
    public long getImageQualityPercentage() {
        return imageQualityPercentage;
    }
    
    /**
     * Minimum quality for the converted image (between 0 and 100)
     * Note: In C# this property is read-only (only getter), but in Java we provide a setter for flexibility
     * @param imageQualityPercentage image quality percentage 
     */
    public void setImageQualityPercentage(long imageQualityPercentage) {
        this.imageQualityPercentage = imageQualityPercentage;
    }
    
    /**
     * Embed color space
     * @return boolean embedsRGB
     */
    public boolean isEmbedsRGB() {
        return embedsRGB;
    }
    
    /**
     * Embed color space
     * @return boolean embedsRGB (alias for isEmbedsRGB)
     */
    public boolean getEmbedsRGB() {
        return embedsRGB;
    }
    
    /**
     * Embed color space
     * Note: In C# this property is read-only (only getter), but in Java we provide a setter for flexibility
     * @param embedsRGB EmbededRGB
     */
    public void setEmbedsRGB(boolean embedsRGB) {
        this.embedsRGB = embedsRGB;
    }
}