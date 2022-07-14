package org.gnoss.apiWrapper.Helpers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.gnoss.apiWrapper.Excepciones.GnossAPIException;

public class ImageHelper {
	
	private ILogHelper _logHelper;
	
	/**
	 * Resize keeping the aspect ratio to width
	 * @param image Image to resize
	 * @param widthInPixels Width to resize
	 * @param pResizeAlways New image with width = widthInPixels
	 * @return BufferedImage resultImage
	 * @throws GnossAPIException  Error at resize the image
	 */
	public static BufferedImage ResizeImageToWidth(BufferedImage image, int widthInPixels, boolean pResizeAlways) throws GnossAPIException{
		try{
			int width = image.getHeight();
			int height = image.getWidth();
			int aspectRatio = height / width;			
			BufferedImage resultImage = null;
			
			if(pResizeAlways || widthInPixels <= height){
				int newHeight = widthInPixels / aspectRatio;
				resultImage = new BufferedImage(widthInPixels, newHeight, BufferedImage.TYPE_INT_RGB);
			}
			else{
				resultImage = image;
				LogHelper.getInstance().Info("The original image has less width than widthInPixels. Return the original image");
			}
			return resultImage;
		}
		catch(Exception ex){
			throw new GnossAPIException("Error in resize: " + ex.getMessage());			
		}
	}
	
	/**
	 * Resize keeping the aspect ratio to width
	 * @param image Image to resize
	 * @param widthInPixels Width to resize
	 * @return BufferedImage resultImage
	 * @throws GnossAPIException  Error at resize the image
	 */
	public static BufferedImage ResizeImageToWidth(BufferedImage image, int widthInPixels) throws GnossAPIException{
		return ResizeImageToWidth(image, widthInPixels, false);
	}
	
	/**
	 * Resize the image keeping the aspect ratio without exceeding the width or height indicated 
	 * @param image Image to resize
	 * @param widthInPixels Width to resize
	 * @param heightInPixels Height to resize
	 * @return New image with width and height
	 * @throws GnossAPIException GnossAPIException
	 */
	public static BufferedImage ResizeImageToHeightAndWidth(BufferedImage image, int widthInPixels, int heightInPixels) throws GnossAPIException{
		int height = image.getHeight();
		int width = image.getWidth();
		int aspectRatio = width / height;
		BufferedImage resultImage = null;
		if(widthInPixels <= width){
			try{
				int newHeight = widthInPixels / aspectRatio;
				resultImage = new BufferedImage(widthInPixels, newHeight, BufferedImage.TYPE_INT_RGB);
				height = resultImage.getHeight();
				width = resultImage.getWidth();
				if(heightInPixels <= height){
					aspectRatio = width / height;
					int nuevoAncho = heightInPixels * aspectRatio;
					resultImage = new BufferedImage(nuevoAncho, heightInPixels, BufferedImage.TYPE_INT_RGB);
				}
			}
			catch(Exception ex){
				throw new GnossAPIException("Error in resize: " + ex.getMessage());
			}
		}
		else{
			if(heightInPixels <= height){
				try{
					int newWidth = heightInPixels * aspectRatio;
					resultImage = new BufferedImage(newWidth, heightInPixels, BufferedImage.TYPE_INT_RGB);
				}
				catch(Exception ex){
					throw new GnossAPIException("Error in resize: " + ex.getMessage());
				}
			}
			else{
				resultImage = image;
				LogHelper.getInstance().Info("The original image has less width and height than widthInPixels and heightInPixels. Return the original image");
			}
		}
		return resultImage;
	}
	
	/**
	 * Resize keeping the aspect ratio to height
	 * @param image Image to resize
	 * @param heightInPixels Height to resize
	 * @param pResizeAlways boolean
	 * @return New image with height
	 */
	public static BufferedImage ResizeImageToHeight(BufferedImage image, int heightInPixels, boolean pResizeAlways){
		int height = image.getHeight();
		int width = image.getWidth();
		int aspectRatio = width / height;
		BufferedImage resultImage = null;
		
		if(pResizeAlways || heightInPixels <= height){
			int newWidth = heightInPixels * aspectRatio;
			resultImage = new BufferedImage(newWidth, heightInPixels, BufferedImage.TYPE_INT_RGB);
		}
		else{
			resultImage = image;
			LogHelper.getInstance().Info("The original image has less height than heightInPixels. Return the original image");
		}
		
		return resultImage;
	}
	
	/**
	 * Resize keeping the aspect ratio to height
	 * @param image Image to resize
	 * @param heightInPixels Height to resize
	 * @return New image with height
	 */
	public static BufferedImage ResizeImageToHeight(BufferedImage image, int heightInPixels){
		return ResizeImageToHeight(image, heightInPixels, false);
	}
	
	/**
	 * Resize to the indicated size, crop the image and take the top of the image if it is vertical, or the central part if its horizontal
	 * @param image Image
	 * @param squareSize Size in pixels of the width and height of the result image
	 * @return Square image
	 * @throws GnossAPIException Error at resize the image
	 */
	public static BufferedImage CropImageToSquare(BufferedImage image, int squareSize) throws GnossAPIException{
		BufferedImage resultImage = null;
		int height = image.getHeight();
		int width = image.getWidth();
		if(height < squareSize && width < squareSize){
			LogHelper.getInstance().Info("The original image is smaller than the required. Returns the original image");
			resultImage = image;
			return resultImage;			
		}
		else{
			boolean isVertical = false;
			boolean isHorizontal = false;
			boolean isSquare = false;
			int aspectRatio = width / height;
			if(aspectRatio == 1){
				isSquare = true;
			}
			else if(aspectRatio > 1){
				isHorizontal = true;
			}
			else if(aspectRatio < 1){
				isVertical = true;
			}
			if(isVertical){
				if(width >= squareSize){
					BufferedImage resizedImage = ResizeImageToWidth(image, squareSize);
					BufferedImage cropedImage = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_RGB);
					Graphics graphics = cropedImage.createGraphics();
					graphics.drawImage(resizedImage, 0, 0, null);
					resultImage = cropedImage;
				}
				else if(width <= squareSize && height > squareSize){
					BufferedImage cropedImage = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_RGB);
					Graphics graphics = cropedImage.createGraphics();
					graphics.drawImage(image, 0, 0, null);
					resultImage = cropedImage;
				}
			}
			if(isHorizontal){
				if(height >= squareSize){
					BufferedImage resizedImage = ResizeImageToWidth(image, squareSize);
					BufferedImage cropedImage = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_RGB);
					Graphics graphics = cropedImage.createGraphics();
					graphics.drawImage(resizedImage, (resizedImage.getWidth() - squareSize) / 2, 0, null);
					resultImage = cropedImage;
				}
				else if(height <= squareSize && width > squareSize){
					BufferedImage cropedImage = new BufferedImage(squareSize, height, BufferedImage.TYPE_INT_RGB);
					Graphics graphics = cropedImage.createGraphics();
					graphics.drawImage(image, (image.getWidth() - squareSize) / 2, 0, null);
					resultImage = cropedImage;
				}
			}
			if(isSquare){
				BufferedImage squareImage = ResizeImageToWidth(image, squareSize);
				resultImage = squareImage;
			}
			return resultImage;
		}
	}
	
	/**
	 * Resize to the indicated size, crop the image and take the top of the image if it is vertical, or the central part if its horizontal
	 * @param image Image
	 * @param pHeight Height
	 * @param pWidth Width
	 * @return Image BufferedImage
	 * @throws GnossAPIException  Error at resize the image
	 */
	public static BufferedImage CropImageToHeightAndWidth(BufferedImage image, int pHeight, int pWidth) throws GnossAPIException{
		int aspcetRatioDeseado = pHeight / pWidth;
		
		BufferedImage resultImage = null;
		int height = image.getHeight();
		int width = image.getWidth();
		int aspcetRatio = height / width;
		
		if(aspcetRatio < aspcetRatioDeseado){
			BufferedImage resizedImage = ResizeImageToWidth(image, pHeight);
			BufferedImage cropedImage = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = cropedImage.createGraphics();
			graphics.drawImage(resizedImage, (resizedImage.getWidth() - pWidth) / 2, 0, null);
			resultImage = cropedImage;
		}
		else{
			BufferedImage resizedImage = ResizeImageToWidth(image, pWidth);
			BufferedImage cropedImage = new BufferedImage(pWidth, pHeight, BufferedImage.TYPE_INT_RGB);
			Graphics graphics = cropedImage.createGraphics();
			graphics.drawImage(resizedImage, 0, (resizedImage.getHeight() - pHeight) / 2,  null);
			resultImage = cropedImage;
		}
		
		return resultImage;
	}
	
	
	public static byte[] BitmapToByteArray(BufferedImage bitmap){
		byte[] buffer = null;
		
		try {
			ByteArrayOutputStream baos= new ByteArrayOutputStream();
			ImageIO.write(bitmap, "bmp", baos);
			buffer=baos.toByteArray();
			
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return buffer;
	}
	
	public static BufferedImage DownloadImageFromUrl(String imagenUrl) {
		BufferedImage bi= new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		
		try {
			URL url= new URL(imagenUrl);
			URLConnection urlCon= url.openConnection();
			InputStream is= urlCon.getInputStream();
			
			
			byte[] array = new byte[1000];
			int leido=is.read(array);
			InputStream input= new ByteArrayInputStream(array);
			while(leido>0) {
				bi=ImageIO.read(input);
				leido=is.read(array);
				
			}
			is.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return bi;
	}
	
	
	public BufferedImage ReadImageFromUrlOrLocalPath(String imageUrlOrPath) throws GnossAPIException {
		BufferedImage bi= new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		URI uri = URI.create(imageUrlOrPath);
		if(uri != null) {
			bi=ImageHelper.DownloadImageFromUrl(imageUrlOrPath);
		}else {
			File file= new File(imageUrlOrPath);
			if(file.exists()) {
				try {
					bi=ImageHelper.DownloadImageFromUrl(imageUrlOrPath);
				}catch(Exception ex) {
					this._logHelper.Error("Error reading the image "+ imageUrlOrPath +": "+ex.getMessage());
				}
			}else {
				throw new GnossAPIException("The image "+imageUrlOrPath+" doesn´t exist or the application couldn´t access");
			}
		}
		return bi;
	}
	
	 
	public static BufferedImage ByteArrayToBitmap(byte[] byteArray) {
		BufferedImage bi= new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		
		try {
			InputStream in= new ByteArrayInputStream(byteArray);
			bi=ImageIO.read(in);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return bi;
	}
	
}
