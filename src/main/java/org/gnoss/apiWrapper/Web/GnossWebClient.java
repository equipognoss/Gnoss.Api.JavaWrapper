package org.gnoss.apiWrapper.Web;

import org.springframework.web.reactive.function.client.WebClient;
import java.net.URI;



public abstract  class GnossWebClient implements WebClient{

	private boolean autoRedirect;
	

	public GnossWebClient() {
		
	}
	public GnossWebClient(boolean pAutoRedirect) {
		this.autoRedirect=pAutoRedirect;
	}
	
	protected String GetWebRequest(URI address) {
		//String request=GetWebRequest(address).toString();
		//return request;
		return "";
	}
	

	public boolean isAutoRedirect() {
		return autoRedirect;
	}

	public void setAutoRedirect(boolean autoRedirect) {
		this.autoRedirect = autoRedirect;
	}


}
