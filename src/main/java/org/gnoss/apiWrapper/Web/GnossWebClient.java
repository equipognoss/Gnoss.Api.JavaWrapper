package org.gnoss.apiWrapper.Web;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;


public  class GnossWebClient {

	private boolean autoRedirect;
	

	public GnossWebClient() {
		
	}
	public GnossWebClient(boolean pAutoRedirect) {
		this.autoRedirect=pAutoRedirect;
	}
	
	protected String GetWebRequest(URI address) {
		
		String request=GetWebRequest(address).toString();
		 return request;	 
	}
	

	public boolean isAutoRedirect() {
		return autoRedirect;
	}

	public void setAutoRedirect(boolean autoRedirect) {
		this.autoRedirect = autoRedirect;
	}


}
