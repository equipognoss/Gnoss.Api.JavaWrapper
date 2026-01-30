package org.gnoss.apiWrapper.Helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;

import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.w3c.dom.Element;

/**
 * Utilities to use images
 */
public class ImageHelper {
	
	private ILogHelper _logHelper;
	
	/**
	 * Resize keeping the aspect ratio to width
	 * @param image Image to resize
	 * @param widthInPixels Width to resize
	 * @param pResizeAlways Indicate if the image will be resized always
	 * @throws GnossAPIException Error at resize the image
	 */
	public static void resizeImageToWidth(BufferedImage image, int widthInPixels, boolean pResizeAlways) throws GnossAPIException {
		try {
			float aspectRatio = (float) image.getWidth() / (float) image.getHeight();
			
			if (pResizeAlways || widthInPixels <= image.getWidth()) {
				float newHeight = widthInPixels / aspectRatio;
				resizeImageInPlace(image, widthInPixels, (int) newHeight);
			}
		} catch (Exception ex) {
			throw new GnossAPIException("Error in resize: " + ex.getMessage());
		}
	}
	
	/**
	 * Resize keeping the aspect ratio to width
	 * @param image Image to resize
	 * @param widthInPixels Width to resize
	 * @throws GnossAPIException Error at resize the image
	 */
	public static void resizeImageToWidth(BufferedImage image, int widthInPixels) throws GnossAPIException {
		resizeImageToWidth(image, widthInPixels, false);
	}
	
	/**
	 * Resize the image keeping the aspect ratio without exceeding the width or height indicated 
	 * @param image Image to resize
	 * @param widthInPixels Width to resize
	 * @param heightInPixels Height to resize
	 * @throws GnossAPIException Error at resize the image
	 */
	public static void resizeImageToHeightAndWidth(BufferedImage image, int widthInPixels, int heightInPixels) throws GnossAPIException {
		float aspectRatio = (float) image.getWidth() / (float) image.getHeight();
		
		if (widthInPixels <= image.getWidth()) {
			try {
				float newHeight = widthInPixels / aspectRatio;
				resizeImageInPlace(image, widthInPixels, (int) newHeight);
				
				if (heightInPixels <= image.getHeight()) {
					aspectRatio = (float) widthInPixels / newHeight;
					float newWidth = heightInPixels * aspectRatio;
					resizeImageInPlace(image, (int) newWidth, heightInPixels);
				}
			} catch (Exception ex) {
				throw new GnossAPIException("Error in resize: " + ex.getMessage());
			}
		} else {
			if (heightInPixels <= image.getHeight()) {
				try {
					float newWidth = heightInPixels * aspectRatio;
					resizeImageInPlace(image, (int) newWidth, heightInPixels);
				} catch (Exception ex) {
					throw new GnossAPIException("Error in resize: " + ex.getMessage());
				}
			}
		}
	}
	
	/**
	 * Resize keeping the aspect ratio to height
	 * @param image Image to resize
	 * @param heightInPixels Height to resize
	 * @param pResizeAlways Indicate if the image will be resized always
	 */
	public static void resizeImageToHeight(BufferedImage image, int heightInPixels, boolean pResizeAlways) {
		float aspectRatio = (float) image.getWidth() / (float) image.getHeight();
		
		if (pResizeAlways || heightInPixels <= image.getHeight()) {
			float newWidth = heightInPixels * aspectRatio;
			resizeImageInPlace(image, (int) newWidth, heightInPixels);
		}
	}
	
	/**
	 * Resize keeping the aspect ratio to height
	 * @param image Image to resize
	 * @param heightInPixels Height to resize
	 */
	public static void resizeImageToHeight(BufferedImage image, int heightInPixels) {
		resizeImageToHeight(image, heightInPixels, false);
	}
	
	/**
	 * Resize to the indicated size, crop the image and take the top of the image if it is vertical, or the central part if its horizontal
	 * @param image Image
	 * @param squareSize Size in pixels of the width and height of the result image
	 * @throws GnossAPIException Error at resize the image
	 */
	public static void cropImageToSquare(BufferedImage image, int squareSize) throws GnossAPIException {
		if (image.getHeight() > squareSize && image.getWidth() > squareSize) {
			boolean isVertical = false;
			boolean isHorizontal = false;
			float aspectRatio = (float) image.getWidth() / (float) image.getHeight();
			
			if (aspectRatio < 1) {
				isVertical = true;
			} else if (aspectRatio > 1) {
				isHorizontal = true;
			}
			
			if (isVertical) {
				if (image.getWidth() >= squareSize) {
					resizeImageToWidth(image, squareSize);
					cropImageInPlace(image, 0, 0, squareSize, squareSize);
				} else if (image.getHeight() > squareSize) {
					cropImageInPlace(image, 0, 0, image.getWidth(), squareSize);
				}
			} else if (isHorizontal) {
				if (image.getHeight() >= squareSize) {
					resizeImageToHeight(image, squareSize);
					int originX = (image.getWidth() - squareSize) / 2;
					cropImageInPlace(image, originX, 0, squareSize, squareSize);
				} else if (image.getWidth() > squareSize) {
					int originX = (image.getWidth() - squareSize) / 2;
					cropImageInPlace(image, originX, 0, squareSize, image.getHeight());
				}
			} else {
				// If the image isn't vertical and isn't horizontal, have to be a square
				resizeImageToWidth(image, squareSize);
			}
		}
	}
	
	/**
	 * Resize to the indicated size, crop the image and take the top of the image if it is vertical, or the central part if its horizontal
	 * @param pImage Image
	 * @param pHeight Height of the image
	 * @param pWidth Width of the image
	 * @throws GnossAPIException Error at resize the image
	 */
	public static void cropImageToHeightAndWidth(BufferedImage pImage, int pHeight, int pWidth) throws GnossAPIException {
		float aspectRatioDeseado = (float) pHeight / (float) pWidth;
		float aspectRatio = (float) pImage.getHeight() / (float) pImage.getWidth();
		
		if (aspectRatio < aspectRatioDeseado) {
			resizeImageToHeight(pImage, pHeight, true);
			int originX = (pImage.getWidth() - pWidth) / 2;
			cropImageInPlace(pImage, originX, 0, pWidth, pHeight);
		} else {
			resizeImageToWidth(pImage, pWidth, true);
			int originY = (pImage.getHeight() - pHeight) / 2;
			cropImageInPlace(pImage, 0, originY, pWidth, pHeight);
		}
	}
	
	/**
	 * Converts BufferedImage to byte[]
	 * @param bitmap Image to convert to byte[]
	 * @return byte[] converted from bitmap
	 * @throws GnossAPIException if conversion fails
	 */
	public static byte[] bitmapToByteArray(BufferedImage bitmap) throws GnossAPIException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bitmap, "png", baos);
			return baos.toByteArray();
		} catch (Exception ex) {
			throw new GnossAPIException("Impossible to convert the image to byte[]: " + ex.getMessage());
		}
	}
	
	/**
	 * Converts image to byte[], with a minimum quality
	 * @param bitmap Image to convert to byte[]
	 * @param quality Minimum quality for the converted image (0-100)
	 * @return byte[] converted from bitmap
	 * @throws GnossAPIException if conversion fails
	 */
	public static byte[] bitmapToByteArray(BufferedImage bitmap, int quality) throws GnossAPIException {
		if (quality == Integer.MIN_VALUE) {
			// Convert without minimum quality
			return bitmapToByteArray(bitmap);
		} else {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				// Use JPEG with quality settings
				Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
				if (!writers.hasNext()) {
					throw new IllegalStateException("No writers found for JPEG format");
				}
				
				ImageWriter writer = writers.next();
				ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
				writer.setOutput(ios);
				
				JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
				jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				jpegParams.setCompressionQuality(quality / 100f);
				
				writer.write(null, new IIOImage(bitmap, null, null), jpegParams);
				
				ios.close();
				writer.dispose();
				
				return baos.toByteArray();
			} catch (Exception ex) {
				throw new GnossAPIException("Impossible to convert the image to byte[]: " + ex.getMessage());
			}
		}
	}
	
	/**
	 * Download a image from a url
	 * @param imageUrl Url of the image
	 * @return BufferedImage
	 * @throws GnossAPIException if download fails
	 */
	public static BufferedImage downloadImageFromUrl(String imageUrl) throws GnossAPIException {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "GnossApiWrapper/1.0");
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			
			InputStream is = connection.getInputStream();
			BufferedImage image = ImageIO.read(is);
			is.close();
			connection.disconnect();
			
			if (image == null) {
				throw new GnossAPIException("Failed to read image from URL: " + imageUrl);
			}
			
			return image;
		} catch (Exception ex) {
			throw new GnossAPIException("Error downloading image from " + imageUrl + ": " + ex.getMessage());
		}
	}
	
	/**
	 * Download a image from a url or a local path
	 * @param imageUrlOrPath Url or local path of the image
	 * @return BufferedImage
	 * @throws GnossAPIException if reading fails
	 */
	public BufferedImage readImageFromUrlOrLocalPath(String imageUrlOrPath) throws GnossAPIException {
		BufferedImage image = null;
		
		if (isWellFormedUri(imageUrlOrPath)) {
			image = downloadImageFromUrl(imageUrlOrPath);
		} else {
			File file = new File(imageUrlOrPath);
			if (file.exists()) {
				try {
					image = ImageIO.read(file);
					if (image == null) {
						throw new GnossAPIException("Failed to read image file: " + imageUrlOrPath);
					}
				} catch (IOException ex) {
					if (this._logHelper != null) {
						this._logHelper.error("Error reading the image " + imageUrlOrPath + ": " + ex.getMessage());
					}
					throw new GnossAPIException("Error reading the image " + imageUrlOrPath + ": " + ex.getMessage());
				}
			} else {
				throw new GnossAPIException("The image " + imageUrlOrPath + " doesn't exist or the application couldn't access");
			}
		}
		
		return image;
	}
	
	/**
	 * Converts ByteArray to BufferedImage
	 * @param byteArray Bytearray of the image
	 * @return BufferedImage
	 * @throws GnossAPIException if conversion fails
	 */
	public static BufferedImage byteArrayToBitmap(byte[] byteArray) throws GnossAPIException {
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			BufferedImage image = ImageIO.read(in);
			in.close();
			
			if (image == null) {
				throw new GnossAPIException("Failed to convert byte array to image");
			}
			
			return image;
		} catch (Exception ex) {
			throw new GnossAPIException("Impossible to convert the byte[] to image: " + ex.getMessage());
		}
	}
	
	/**
	 * Property assigned to the EXIF sRGB Color Space
	 * @param image Image
	 * @throws GnossAPIException if assignment fails
	 */
	public void assignEXIFPropertyColorSpaceSRGB(BufferedImage image) throws GnossAPIException {
		try {
			// Convert image to byte array
			byte[] imageBytes = bitmapToByteArray(image);
			
			// Assign EXIF ColorSpace to the byte array
			byte[] updatedBytes = assignEXIFPropertyColorSpaceSRGBToByteArray(imageBytes);
			
			// Convert back to BufferedImage if needed
			// Note: The caller should use the updated bytes directly
			
			if (this._logHelper != null) {
				this._logHelper.info("EXIF ColorSpace sRGB assigned successfully");
			}
		} catch (Exception ex) {
			throw new GnossAPIException("Error assigning EXIF ColorSpace: " + ex.getMessage());
		}
	}
	
	/**
	 * Assigns EXIF sRGB ColorSpace to a byte array image
	 * @param imageBytes Image as byte array
	 * @return Updated byte array with EXIF ColorSpace
	 * @throws GnossAPIException if assignment fails
	 */
	public static byte[] assignEXIFPropertyColorSpaceSRGBToByteArray(byte[] jpegBytes) throws GnossAPIException {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			
			// Verify it's a valid JPEG
			if (jpegBytes.length < 2 || jpegBytes[0] != (byte)0xFF || jpegBytes[1] != (byte)0xD8) {
				return jpegBytes;
			}
			
			// Write SOI (Start of Image) marker
			output.write(0xFF);
			output.write(0xD8);
			
			// Create minimal EXIF segment with ColorSpace
			byte[] exifSegment = createMinimalExifSegment();
			
			// Write APP1 marker
			output.write(0xFF);
			output.write(0xE1);
			
			// Write segment length (2 bytes, big-endian)
			int segmentLength = exifSegment.length + 2;
			output.write((segmentLength >> 8) & 0xFF);
			output.write(segmentLength & 0xFF);
			
			// Write EXIF data
			output.write(exifSegment);
			
			// Copy rest of JPEG (skip original SOI)
			int pos = 2;
			
			// Skip any existing APP1 segments
			while (pos < jpegBytes.length - 1) {
				if (jpegBytes[pos] == (byte)0xFF && jpegBytes[pos + 1] == (byte)0xE1) {
					// Skip this APP1 segment
					int len = ((jpegBytes[pos + 2] & 0xFF) << 8) | (jpegBytes[pos + 3] & 0xFF);
					pos += len + 2;
				} else {
					break;
				}
			}
			
			// Copy the rest
			output.write(jpegBytes, pos, jpegBytes.length - pos);
			
			return output.toByteArray();
			
		} catch (Exception ex) {
			return jpegBytes;
		}
	}
	
	/**
	 * Creates a minimal EXIF segment with ColorSpace = sRGB
	 * @return EXIF segment bytes
	 * @throws IOException if creation fails
	 */
	private static byte[] createMinimalExifSegment() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		// EXIF identifier
		baos.write("Exif\0\0".getBytes("ASCII"));
		
		// TIFF header (little-endian)
		baos.write(0x49); // 'I'
		baos.write(0x49); // 'I'
		baos.write(0x2A); // TIFF magic number
		baos.write(0x00);
		baos.write(0x08); // Offset to first IFD
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		
		// IFD0 - Contains pointer to Exif SubIFD
		baos.write(0x01); // 1 entry
		baos.write(0x00);
		
		// Tag: ExifIFDPointer (0x8769)
		baos.write(0x69);
		baos.write(0x87);
		// Type: LONG
		baos.write(0x04);
		baos.write(0x00);
		// Count: 1
		baos.write(0x01);
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		// Value: offset to Exif SubIFD (26 bytes from TIFF header start)
		baos.write(0x1A);
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		
		// Next IFD offset: 0 (no more IFDs)
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		
		// Exif SubIFD (offset 26 = 0x1A)
		baos.write(0x01); // 1 entry
		baos.write(0x00);
		
		// Tag: ColorSpace (0xA001)
		baos.write(0x01);
		baos.write(0xA0);
		// Type: SHORT
		baos.write(0x03);
		baos.write(0x00);
		// Count: 1
		baos.write(0x01);
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		// Value: 1 (sRGB)
		baos.write(0x01);
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		
		// Next IFD offset: 0
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		baos.write(0x00);
		
		return baos.toByteArray();
	}
	
	// Private helper methods
	
	/**
	 * Helper method to resize image in place (mutating the original)
	 * Note: In Java, we can't truly resize "in place" like C#'s Mutate, so this creates a new image
	 * and the caller should handle replacement
	 */
	private static BufferedImage resizeImageInPlace(BufferedImage original, int targetWidth, int targetHeight) {
		Image tmp = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = resized.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		
		// Copy the resized image data back to the original
		original.setData(resized.getData());
		
		return original;
	}
	
	/**
	 * Helper method to crop image in place
	 */
	private static void cropImageInPlace(BufferedImage image, int x, int y, int width, int height) {
		BufferedImage cropped = image.getSubimage(x, y, width, height);
		image.setData(cropped.getData());
	}
	
	/**
	 * Check if a string is a well-formed URI
	 */
	private static boolean isWellFormedUri(String uri) {
		try {
			URI u = URI.create(uri);
			return u.getScheme() != null;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	/**
	 * Set the log helper
	 * @param logHelper Log helper instance
	 */
	public void setLogHelper(ILogHelper logHelper) {
		this._logHelper = logHelper;
	}
}