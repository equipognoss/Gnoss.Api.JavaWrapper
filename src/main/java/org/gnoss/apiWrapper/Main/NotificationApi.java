package org.gnoss.apiWrapper.Main;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.gnoss.apiWrapper.ApiModel.NotificationModel;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.xml.sax.SAXException;

public class NotificationApi extends GnossApiWrapper{

	private ILogHelper _logHelper;
	
	/**
	 * COnstructor of NotificationAPI
	 * @param oauth
	 * @param communityShortName
	 */
	public NotificationApi(OAuthInfo oauth, String communityShortName) {
		super(oauth, communityShortName);
		// TODO Auto-generated constructor stub
		this._logHelper=LogHelper.getInstance();
	}
	
	/**
	 * COnstructor of NotificationApi
	 * @param configFilePath
	 * @throws GnossAPIException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public NotificationApi(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
		super(configFilePath);
		this._logHelper=LogHelper.getInstance();
	}
	
	public void SendEmail(String subject, String message, List<String> receivers, boolean isHTML, String senderMask, String communityShortName) throws Exception {
		try {
			
			String url=super.getApiUrl()+"/notification/send-email";
			NotificationModel model= new NotificationModel();
			model.setObject(subject);
			model.setMessage(message);
			model.setReceivers(receivers);
			model.setIs_html(isHTML);
			model.setSender_mask(senderMask);
			model.setCommunity_short_name(communityShortName);
			
			super.WebRequestPostWithJsonObject(url, model);
			
			this._logHelper.Debug("Email" +subject+ "sended to" + receivers.toString());
			
		}catch(Exception ex) {
			this._logHelper.Error("Error sending mail" + subject+ "to " +"," + receivers.toString() + ":" +message );
			throw ex;
		}
	}

}
