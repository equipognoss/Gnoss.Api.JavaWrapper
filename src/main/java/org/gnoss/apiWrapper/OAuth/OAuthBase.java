/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.OAuth;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

import org.gnoss.apiWrapper.Helpers.LogHelper;

/**
 *
 * @author salopez
 */
public class OAuthBase {

    //Constantes
    private final String OAuthVersion = "1.0";  //OAuthVersion
    private final String OAuthParameterPrefix = "oauth_";  //OAuthParameterPrefix

    //
    // List of know and used oauth parameters' names
    //        
    private static final String OAuthConsumerKeyKey = "oauth_consumer_key";
    private static final String OAuthCallbackKey = "oauth_callback";
    private static final String OAuthVersionKey = "oauth_version";
    private static final String OAuthSignatureMethodKey = "oauth_signature_method";
    private static final String OAuthSignatureKey = "oauth_signature";
    private static final String OAuthTimestampKey = "oauth_timestamp";
    private static final String OAuthNonceKey = "oauth_nonce";
    private static final String OAuthTokenKey = "oauth_token";
    private static final String OAuthTokenSecretKey = "oauth_token_secret";

    private static final String HMACSHA1SignatureType = "HMAC-SHA1";
    private static final String PlainTextSignatureType = "PLAINTEXT";
    private static final String RSASHA1SignatureType = "RSA-SHA1";

    private static final String UnreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";

    //Miembros
    private Random _random;

    //Propiedades
    private String ConsumerKey;
    private String ConsumerSecret;

    //Constructor    
    /**
     * Constructor
     *
     * @param consumerKey
     * @param consumerSecret
     */
    public OAuthBase(String consumerKey, String consumerSecret) {
        ConsumerKey = consumerKey;
        ConsumerSecret = consumerSecret;
        _random = new Random();
    }

    //Métodos
    public HashMap<String, String> GetOAuthParameters(String httpMethod, String url, String requestToken,
            String tokenSecret, String verifier, String callback) throws URISyntaxException, SignatureException, MalformedURLException {

        HashMap<String, String> oAuthParameters = new HashMap<String, String>();
        oAuthParameters.put("oauth_timestamp", GenerateTimeStamp());
        oAuthParameters.put("oauth_nonce", GenerateNonce());
        oAuthParameters.put("oauth_version", "1.0");
        oAuthParameters.put("oauth_signature_method", "HMAC-SHA1");
        oAuthParameters.put("oauth_consumer_key", ConsumerKey);

        if (StringUtils.isEmpty(requestToken)) {
            oAuthParameters.put("oauth_token", requestToken);
        }

        if (StringUtils.isEmpty(verifier)) {
            oAuthParameters.put("oauth_verifier", verifier);
        }

        if (StringUtils.isEmpty(callback)) {
            oAuthParameters.put("oauth_callback", callback);
        }

        String signatureBase = GetSignatureBase(httpMethod, NormalizeUrl(url), oAuthParameters);
        String signature = GetSignature(ConsumerSecret, signatureBase, tokenSecret);
        oAuthParameters.put("oauth_signature", signature);

        return oAuthParameters;
    }

    public LinkedHashMap<String, String> GetOAuthParametersWithoutEncode(String httpMethod, String url, String requestToken, String tokenSecret, String verifier, String callback) throws SignatureException, URISyntaxException, MalformedURLException {
        LinkedHashMap<String, String> oAuthParameters = new LinkedHashMap<String, String>();
        oAuthParameters.put("oauth_consumer_key", UrlEncode(ConsumerKey));

        LinkedHashMap<String, String> oAuthParametersEncode = new LinkedHashMap<String, String>();
        oAuthParametersEncode.put("oauth_consumer_key", UrlEncode(ConsumerKey));

        if (!StringUtils.isEmpty(requestToken)) {
            oAuthParameters.put("oauth_token", requestToken);
            oAuthParametersEncode.put("oauth_token", UrlEncode(requestToken));
        }

        oAuthParameters.put("oauth_signature_method", "HMAC-SHA1");
        oAuthParametersEncode.put("oauth_signature_method", "HMAC-SHA1");

        oAuthParameters.put("oauth_timestamp", GenerateTimeStamp());
        oAuthParametersEncode.put("oauth_timestamp", oAuthParameters.get("oauth_timestamp"));

        oAuthParameters.put("oauth_nonce", GenerateNonce());
        oAuthParametersEncode.put("oauth_nonce", oAuthParameters.get("oauth_nonce"));

        oAuthParameters.put("oauth_version", "1.0");
        oAuthParametersEncode.put("oauth_version", "1.0");

        if (!StringUtils.isEmpty(verifier)) {
            oAuthParameters.put("oauth_verifier", verifier);
            oAuthParametersEncode.put("oauth_verifier", UrlEncode(verifier));
        }

        if (!StringUtils.isEmpty(callback)) {
            oAuthParameters.put("oauth_callback", callback);
            oAuthParametersEncode.put("oauth_callback", callback);
        }

        String signatureBase = GetSignatureBaseEncoded(httpMethod, NormalizeUrl(url), oAuthParametersEncode);
        String signature = UrlEncode(GetSignature(UrlEncode(ConsumerSecret), signatureBase, UrlEncode(tokenSecret)));

        oAuthParameters.put("oauth_signature", signature);
        oAuthParametersEncode.put("oauth_signature", signature);

        return oAuthParametersEncode;
    }
    
    /**
     * Gets signature base encoded
     * @param httpMehod Http method
     * @param url Url
     * @param oAuthParameters OAuth parameters
     * @return 
     */
    private static String GetSignatureBaseEncoded(String httpMehod, String url, HashMap<String, String> oAuthParameters){
        HashMap<String, String> parameters = new HashMap<>();
        for(String key : oAuthParameters.keySet()){
            parameters.put(key, oAuthParameters.get(key));
        }
        
        String normalizedParameters = NormalizeParameters(parameters);
        String signatureBase = httpMehod + "&" + UrlEncode(url) + "&" + UrlEncode(normalizedParameters);
        
        return signatureBase;
    }

    public static String UrlEncode(String value) {      
    	String encodedUrl = "";
		try {
			encodedUrl = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			LogHelper.getInstance().Error("The URL can't be encoded");
			e.printStackTrace();
		}    
        	        
        return encodedUrl;
    }

    private static String GetSignature(String consumerSecret, String signatureBase, String tokenSecret) throws SignatureException {
        //Si no funciona este método probar OAuthClientLibrary Java
        //Example https://github.com/SAP-samples/jam-collaboration-sample/blob/master/OAuth_1_HMAC-SHA1/jam_java_oauth1_client/src/com/sap/jam/oauth/client/OAuthClientHelper.java
        try {
            String algorithm = "HmacSHA1";
            Charset charset = Charset.forName("utf-8");
            byte[] byteBuffer = (consumerSecret + "&" + tokenSecret).getBytes("UTF-8");
            SecretKey signingKey = new SecretKeySpec(byteBuffer, "HmacSHA1");
            //ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(consumerSecret + "&" + tokenSecret);
            //SecretKeySpec signingKey = new SecretKeySpec(byteBuffer.array(), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(signingKey);
            return new String(Base64.encodeBase64(mac.doFinal(signatureBase.getBytes(charset))), charset);
        } catch (Exception ex) {
            throw new SignatureException("Failed to generate HMAC: " + ex.getMessage());
        }
    }

    private static String GetSignatureBase(String httpMehod, String url, HashMap<String, String> oAuthParameters) throws URISyntaxException {
        HashMap<String, String> parameters = new HashMap<String, String>();
        for (String key : oAuthParameters.keySet()) {
            parameters.put(key, oAuthParameters.get(key));
        }

        String normalizedParameters = NormalizeParameters(parameters);

        String signatureBase = "";
        try {
            URL tempURL = new URL(url);
            signatureBase = httpMehod + "&" + tempURL.toURI().toString() + "&" + normalizedParameters.replace("=", "%3D").replace("&", "%26");
        } catch (MalformedURLException ex) {
            Logger.getLogger(OAuthBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return signatureBase;
    }

    private static String NormalizeParameters(HashMap<String, String> parameters) {
        String normalizedParameters = "";

        ArrayList<String> sortedKeys = new ArrayList<String>(parameters.keySet());
        Collections.sort(sortedKeys);

        for (String parameter : sortedKeys) {
            if (normalizedParameters.length() > 0) {
                normalizedParameters += "&";
            }
            normalizedParameters += parameter + "=" + parameters.get(parameter);
        }

        return normalizedParameters;
    }

    public static String NormalizeUrl(String url) throws URISyntaxException, MalformedURLException {        
    	URI uri = new URI(url);
        String port = "";

        if (uri.getScheme().equals("http") && uri.getPort() != 80 && uri.getPort() != -1
                || uri.getScheme().equals("https") && uri.getPort() != 443 && uri.getPort() != -1
                || uri.getScheme().equals("ftp") && uri.getPort() != 20 && uri.getPort() != -1) {
            port = ":" + uri.getPort();
        }

        String normalizeUrl = uri.getScheme() + "://" + uri.getHost() + port + uri.getPath();

        return normalizeUrl;
    }

    /**
     * Generate the timestamp for the signature
     *
     * @return Timestamp
     */
    private String GenerateTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime baseDate = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);
        Duration duration = Duration.between(baseDate, now);
        Long diff = duration.getSeconds();
        return diff.toString();
    }

    /**
     * Generate a nonce
     *
     * @return Nonce
     */
    public String GenerateNonce() {
        UUID guid = UUID.randomUUID();
        return guid.toString();
    }
}
