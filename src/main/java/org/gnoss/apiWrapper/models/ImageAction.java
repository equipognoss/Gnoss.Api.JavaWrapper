package org.gnoss.apiWrapper.models;

import org.gnoss.apiWrapper.Helpers.ImageTransformationType;
/**
 * Represents a tranformation to do over an image
 * @author Andrea
 *
 */
public class ImageAction {

	private int size;
	private float height;
	private float width;
	private ImageTransformationType imageTransformationType;
	private long imageQualityPercentage;
	private boolean embedsRGB;
	
	
	/**
	 * Constructor of ImageAction
	 * @param size Size in pixels of the width and height of the result image 
	 * @param imageTransformationType  Transformation to apply to the image
	 * @param embedsRGB  embed color space
	 */
	public ImageAction(int size, ImageTransformationType imageTransformationType, boolean embedsRGB) {
		this.size=size;
		this.width=size;
		this.height=size;
		this.imageTransformationType=imageTransformationType;
		this.imageQualityPercentage=Long.MIN_VALUE;
		this.embedsRGB=embedsRGB;
	}
	
	
	/**
	 * Constructor of imageAction
	 * @param size Size in pixels of the width and height of the result image 
	 * @param imageTransformationType Transformation to apply to the image
	 * @param imageQualityPercentage Minimum quality for the converted image (between 0 and 100)
	 * @param embedsRGB embed color space
	 */
	public ImageAction(int size, ImageTransformationType imageTransformationType, long imageQualityPercentage, boolean embedsRGB) {
		this.size=size;
		this.width=size;
		this.height=size;
		this.imageTransformationType=imageTransformationType;
		this.imageQualityPercentage=imageQualityPercentage;
		this.embedsRGB=embedsRGB;
	}
	
	/**
	 * Constructor of imageAction
	 * @param width width to resize
	 * @param heiht height to resize
	 * @param imageTransformationType Transformation to apply to the image
	 * @param embedsRGB embed color space
	 */
	public ImageAction(float width, float heiht, ImageTransformationType imageTransformationType, boolean embedsRGB) {
		this.size=(int) width;
		this.width=width;
		this.height=heiht;
		this.imageTransformationType=imageTransformationType;
		this.imageQualityPercentage=Long.MIN_VALUE;
		this.embedsRGB=embedsRGB;
	}
	
	/**
	 * Constructor of imageAction
	 * @param width width to resize
	 * @param heiht height to resize
	 * @param imageTransformationType Transformation to apply to the image
	 * @param imageQualityPercentage Minimum quality for the converted image (between 0 and 100)
	 * @param embedsRGB embed color space
	 */
	public ImageAction(float width, float heiht, ImageTransformationType imageTransformationType, long imageQualityPercentage, boolean embedsRGB) {
		this.size=(int) width;
		this.width=width;
		this.height=heiht;
		this.imageTransformationType=imageTransformationType;
		this.imageQualityPercentage=imageQualityPercentage;
		this.embedsRGB=embedsRGB;
	}
	
	
	
	/**
	 * Get the size in pixels of the width and height of the result image
	 * @return int 
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
	 * @return float 
	 */
	public float getHeight() {
		return height;
	}
	/**
	 * Height, in pixels, that must have the image after the transformation
	 * @param height
	 */
	public void setHeight(float height) {
		this.height = height;
	}
	/**
	 * Width, in pixels, that must have the image after the transformation
	 * @return float
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
	 * @return ImageTransformationType 
	 */
	public ImageTransformationType getImegeTransformationType() {
		return imageTransformationType;
	}
	/**
	 * Transformation to apply to the image
	 * @param imegeTransformationType image Transformation Type 
	 */
	public void setImegeTransformationType(ImageTransformationType imegeTransformationType) {
		this.imageTransformationType = imegeTransformationType;
	}
	/**
	 * Minimum quality for the converted image (between 0 and 100)
	 * @return long 
	 */
	public long getImageQualityPercentage() {
		return imageQualityPercentage;
	}
	/**
	 * Minimum quality for the converted  image (between 0 and 100)
	 * @param imageQualityPercentage image quality percentage 
	 */
	public void setImageQualityPercentage(long imageQualityPercentage) {
		this.imageQualityPercentage = imageQualityPercentage;
	}
	/**
	 * Embed color space
	 * @return boolean 
	 */
	public boolean isEmbedsRGB() {
		return embedsRGB;
	}
	/**
	 * Embed color space
	 * @param embedsRGB EmbededRGB
	 */
	public void setEmbedsRGB(boolean embedsRGB) {
		this.embedsRGB = embedsRGB;
	}
	
}
