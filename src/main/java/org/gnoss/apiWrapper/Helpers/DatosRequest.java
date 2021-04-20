package org.gnoss.apiWrapper.Helpers;

public class DatosRequest {
	
	private String HttpMethod;
	private String RawURL;
	private String URL;
	private String Domain;
	private String UserAgent;
	private String Ip;
	private String CookieSessionId;
	private String Headers;
	private String Body;
	
	
	public String getHttpMethod() {
		return HttpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		HttpMethod = httpMethod;
	}
	public String getRawURL() {
		return RawURL;
	}
	public void setRawURL(String rawURL) {
		RawURL = rawURL;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String domain) {
		Domain = domain;
	}
	public String getUserAgent() {
		return UserAgent;
	}
	public void setUserAgent(String userAgent) {
		UserAgent = userAgent;
	}
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public String getCookieSessionId() {
		return CookieSessionId;
	}
	public void setCookieSessionId(String cookieSessionId) {
		CookieSessionId = cookieSessionId;
	}
	public String getHeaders() {
		return Headers;
	}
	public void setHeaders(String headers) {
		Headers = headers;
	}
	public String getBody() {
		return Body;
	}
	public void setBody(String body) {
		Body = body;
	}
	

}
