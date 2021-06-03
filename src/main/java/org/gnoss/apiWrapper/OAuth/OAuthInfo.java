/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.OAuth;

import org.gnoss.apiWrapper.Utilities.StringUtilities;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author salopez
 */
public class OAuthInfo {
    
    //Miembros
    private static String Token;
    private static String TokenSecret;
    private static String ConsumerKey;
    private static String ConsumerSecret;
    private String ApiUrl;
    private String DeveloperEmail;
    
    //Propiedades
    private String OAuthSignedUrl;
    
    public OAuthInfo(String apiUrl, String token, String tokenSecret, String consumerKey, 
            String consumerSecret, String developerEmail){
        StringUtilities strngUtl = new StringUtilities();
        ApiUrl = strngUtl.trimEnd(apiUrl, '/');
        Token = token;
        TokenSecret = tokenSecret;
        ConsumerKey = consumerKey;
        ConsumerSecret = consumerSecret;
        DeveloperEmail = developerEmail;
    }
    
    public String getSignedUrl(String url) throws SignatureException, URISyntaxException, MalformedURLException{
        OAuthBase oauthBase = new OAuthBase(ConsumerKey, ConsumerSecret);
        HashMap<String, String> parameters = oauthBase.GetOAuthParametersWithoutEncode("GET", url, Token, TokenSecret, null, null);
        
        String urlOauth = url + "?";
        
        for(String key : parameters.keySet()){
            urlOauth += key + "=" + parameters.get(key) + "&";
        }
        
        urlOauth = urlOauth.substring(0, urlOauth.length() - 1);
        
        return urlOauth;
    }
    
    /**
     * Get a signed url for the API     
     * @throws MalformedURLException 
     */
    public String getOAuthSignedUrl() throws SignatureException, URISyntaxException, MalformedURLException{
        return getSignedUrl(ApiUrl);
    }
    
    public String getToken() {
        return Token;
    }

    public String getTokenSecret() {
        return TokenSecret;
    }

    public String getConsumerKey() {
        return ConsumerKey;
    }

    public String getConsumerSecret() {
        return ConsumerSecret;
    }

    public String getApiUrl() {
        return ApiUrl;
    }

    public String getDeveloperEmail() {
        return DeveloperEmail;
    }
    
    public String getSignedUrl() throws MalformedURLException{
        try{
            return GetSignedUrl(ApiUrl);
        }
        catch(URISyntaxException | SignatureException ex){
            throw new Error("Error while try to get 'ApiUrl'");
        }        
    }
    
    public static  String GetSignedUrl(String url) throws SignatureException, URISyntaxException, MalformedURLException{
    	OAuthBase oauthBase = new OAuthBase(ConsumerKey, ConsumerSecret);
    	LinkedHashMap<String, String> parameters = oauthBase.GetOAuthParametersWithoutEncode("GET", url, Token, TokenSecret, null, null);
    	
    	String urlOauth = url + "?";
    	
    	for(String key : parameters.keySet()){
    		urlOauth += key + "=" + parameters.get(key) + "&";
    	}
    	
    	urlOauth = urlOauth.substring(0, urlOauth.length() - 1);
    	
    	return urlOauth;
    }
}
