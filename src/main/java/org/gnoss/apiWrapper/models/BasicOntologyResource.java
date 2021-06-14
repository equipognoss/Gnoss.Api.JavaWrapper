package org.gnoss.apiWrapper.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.gnoss.apiWrapper.Excepciones.GnossAPIException;

public class BasicOntologyResource extends BaseResource{
	//Properties
	public String PropertiesName;
	public String DownloadUrl;
	public byte[] AttachedFile;
	public int[] SnapshotSizes;
	public boolean GenerateSnapshot;
	
	//Public methods
	
	/**
	 * Read an image from the local file system or the internet and asigns it to the AttachedFile property
	 * @param downloadUrl Absolute local path or url of the image
	 * @param size Size to resize the image after download it
	 * @throws IOException  IOException
	 * @throws GnossAPIException GnossAPIException 
	 */
	public void AttachImage(String downloadUrl, int size) throws GnossAPIException, IOException{
		byte[] image = ReadFile(downloadUrl);
		
		BufferedImage resizedImage = null;
		GenerateSnapshot = true;
		
		if(size != 0){
			//TODO Descomentar
			//resizedImage = ImageHelper.ResizeImageToWidth(ImageHelper.ByteArrayToBitmap(image), size);
			//AttachedFile = ImageHelper.BitmapToByteArray(resizedImage);
		}
	}
	
	/**
	 * Read a file (not an image, to attach an image use AttachImage from the local file system or the internet and asigns it to the AttachedFile property
	 * @param downloadUrl Absolute local path or url of the image
	 * @throws IOException IOException
	 * @throws GnossAPIException GnossAPIException
	 */
	public void AttachFile(String downloadUrl) throws GnossAPIException, IOException{
		ReadFile(downloadUrl);
	}
	
	//Protected methods
	@Override
	protected byte[] ReadFile(String downloadUrl) throws GnossAPIException, IOException{
		AttachedFile = super.ReadFile(downloadUrl);
		
		String[] schemes  = {"http", "https"};
		File file = new File(downloadUrl);
		if(isValid(downloadUrl) && file.exists()){
			if(downloadUrl.contains("/")){
				PropertiesName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);	
			}
			else if(downloadUrl.contains("\\")){
				PropertiesName = downloadUrl.substring(downloadUrl.lastIndexOf("\\") + 1);
			}
		}
		return AttachedFile;
	}
	
	private static boolean isValid(String url) 
    { 
        try { 
            new URL(url).toURI(); 
            return true; 
        }           
        catch (Exception e) { 
            return false; 
        } 
    } 
}
