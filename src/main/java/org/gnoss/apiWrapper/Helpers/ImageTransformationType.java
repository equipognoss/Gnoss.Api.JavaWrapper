package org.gnoss.apiWrapper.Helpers;

public enum ImageTransformationType {
	/**
	 * Resize keeping the aspect ratio to height
	 */
	ResizeToHeight(0),
	/**
	 * Resize keeping the aspect ratio to width
	 */
	ResizeToWidth(1),
	/**
	 * Resize to the indicated size, crop the image and take the top of the image if it is vertical, or the central part if its horizontal
	 */
	Crop(2),
	/**
	 * Resize the image keeping the aspect ratio without exceeding the width or height indicated
	 */
	ResizeToHeightAndWidth(3),
	/**
	 * Resize to the indicated size, crop the image and take the top of the image if it is vertical, or the central part if its horizontal
	 */
	CropToHeightAndWidth(4);
	
	
	private int resize;

	ImageTransformationType(int resize) {
		// TODO Auto-generated constructor stub
	} 

}
