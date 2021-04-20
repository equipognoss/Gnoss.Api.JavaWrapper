package org.gnoss.apiWrapper.Helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.regex.Pattern;

import jdk.internal.util.xml.impl.Input;

/**
 * Utilities to use strings
 * @author Andrea
 *
 */
public class StringHelper {
	
	private static Pattern mReplace_a_Accents = Pattern.compile("[á|à|ä|â]");
	private static Pattern mReplace_e_Accents = Pattern.compile("[é|è|ë|ê]");
	private static Pattern mReplace_i_Accents= Pattern.compile("[í|ì|ï|î]");
	private static Pattern mReplace_o_Accents= Pattern.compile("[ó|ò|ö|ô]");
	private static Pattern mReplace_u_Accents= Pattern.compile("[ú|ù|ü|û]");
	private static Pattern mReplace_A_Accents = Pattern.compile("[Á|À|Ä|Â]");
	private static Pattern mReplace_E_Accents = Pattern.compile("[É|È|Ë|Ê]");
	private static Pattern mReplace_I_Accents= Pattern.compile("[Í|Ì|Ï|Î]");
	private static Pattern mReplace_O_Accents= Pattern.compile("[Ó|Ò|Ö|Ô]");
	private static Pattern mReplace_U_Accents= Pattern.compile("[Ú|Ù|Ü|Û]");
	private static Pattern mRegexQuitarHtml= Pattern.compile("<(.|\n)*?>");
	
	
	/**
	 * REmove reserved caracters for urls in a string 
	 * @param <inputString> String to remove reserved characters
	 * @return The inputString without reserved characters
	 */
	
	public static String RemoveReserveCharactersFouUrl(String inputString) {
		
		int languageSeparatorIndex= inputString.indexOf("|||");
		
		if(languageSeparatorIndex!=-1 && inputString.charAt(languageSeparatorIndex-3)=='@' ) {
			inputString = inputString.substring(0,languageSeparatorIndex);
			inputString=inputString.substring(0, inputString.lastIndexOf('@'));
		}
		inputString= Normalizer.normalize(inputString, Normalizer.Form.NFD);
		inputString = inputString.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		
		Pattern notAlfanumeric= Pattern.compile("[^a-zA-Z0-9 ]");
		
		inputString =notAlfanumeric.toString().replaceAll(inputString, "");
		
		while(inputString.indexOf("  ")>0) {
			inputString= inputString.replaceAll("  ", " ");
		}
		
		if(inputString.length()>50) {
			inputString= inputString.substring(0, 50);
			
			int ultimoEspacio= inputString.lastIndexOf(" ");
			if(ultimoEspacio>0){
				inputString = inputString.substring(0, ultimoEspacio);
			}
			
		}
		Pattern space= Pattern.compile("[ ]");
		inputString= space.toString().replaceAll(inputString, "-");
		
		return inputString.toLowerCase();
		
	}
	
	/**
	 * Remove accents from the input string
	 * @param <pInputString> String to remove accents 
	 * @return  InputString without accents 
	 */
	public static String RemoveAccentsWithRegEx(String pInputString) {
		pInputString= mReplace_a_Accents.toString().replaceAll(pInputString, "a");
		pInputString= mReplace_e_Accents.toString().replaceAll(pInputString, "e");
		pInputString= mReplace_i_Accents.toString().replaceAll(pInputString, "i");
		pInputString= mReplace_o_Accents.toString().replaceAll(pInputString, "o");
		pInputString= mReplace_u_Accents.toString().replaceAll(pInputString, "u");
		
		pInputString= mReplace_A_Accents.toString().replaceAll(pInputString, "A");
		pInputString= mReplace_E_Accents.toString().replaceAll(pInputString, "E");
		pInputString= mReplace_I_Accents.toString().replaceAll(pInputString, "I");
		pInputString= mReplace_O_Accents.toString().replaceAll(pInputString, "O");
		pInputString= mReplace_U_Accents.toString().replaceAll(pInputString, "U");
		
		return pInputString;
	}
	
	/**
	 * Gets a url encoded as UTF-8
	 * @param <url> Url to encode
	 */
	public static String urlEncoderUTF8(String url) {
		try {
			return URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
		}catch(UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}
}
