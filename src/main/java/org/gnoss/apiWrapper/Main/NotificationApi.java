package org.gnoss.apiWrapper.Main;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.gnoss.apiWrapper.ApiModel.MailConfigurationModel;
import org.gnoss.apiWrapper.ApiModel.MailStateModel;
import org.gnoss.apiWrapper.ApiModel.NotificationModel;
import org.gnoss.apiWrapper.Excepciones.GnossAPIException;
import org.gnoss.apiWrapper.Helpers.ILogHelper;
import org.gnoss.apiWrapper.Helpers.LogHelper;
import org.gnoss.apiWrapper.OAuth.OAuthInfo;
import org.xml.sax.SAXException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

/**
 * Wrapper for GNOSS notification API
 * @author API Wrapper Team
 */
public class NotificationApi extends GnossApiWrapper {

    private ILogHelper _logHelper;
    
    /**
     * Constructor of NotificationApi
     * @param oauth OAuth information to sign the Api requests
     * @param communityShortName Community short name which you want to use the API
     */
    public NotificationApi(OAuthInfo oauth, String communityShortName) {
        super(oauth, communityShortName);
        this._logHelper = LogHelper.getInstance();
    }
    
    /**
     * Constructor of NotificationApi
     * @param configFilePath Configuration file path
     * @throws GnossAPIException Gnoss Api Exception
     * @throws ParserConfigurationException Parser Configuration Exception
     * @throws SAXException SAX Exception 
     * @throws IOException IO Exception 
     */
    public NotificationApi(String configFilePath) throws GnossAPIException, ParserConfigurationException, SAXException, IOException {
        super(configFilePath);
        this._logHelper = LogHelper.getInstance();
    }
    
    /**
     * Send an e-mail notification
     * @param subject Subject of the notification
     * @param message Message of the notification
     * @param receivers Receivers of the notification
     * @param isHTML It indicates whether the content is html
     * @param senderMask Mask sender of the notification
     * @return Mail ID
     * @throws Exception exception
     */
    public int sendEmail(String subject, String message, List<String> receivers, boolean isHTML, String senderMask) throws Exception {
        try {
            String url = getApiUrl() + "/notification/send-email";
            
            NotificationModel model = new NotificationModel();
            model.setSubject(subject);
            model.setMessage(message);
            model.setReceivers(receivers);
            model.setIs_html(isHTML);
            model.setSender_mask(senderMask);
            model.setCommunity_short_name(getCommunityShortName());
            
            String result = webRequestPostWithJsonObject(url, model);
            
            int mailID = 0;
            try {
                mailID = Integer.parseInt(result.trim().replaceAll("^\"|\"$", ""));
            } catch (NumberFormatException e) {
                // Si no se puede parsear, devolver 0
            }
            
            this._logHelper.debug("Email '" + subject + "' sent to " + String.join(",", receivers));
            
            return mailID;
            
        } catch (Exception ex) {
            this._logHelper.error("Error sending mail '" + subject + "' to " + String.join(",", receivers) + ": \r\n" + ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * Send an e-mail notification with default values
     * @param subject Subject of the notification
     * @param message Message of the notification
     * @param receivers Receivers of the notification
     * @return Mail ID
     * @throws Exception exception
     */
    public int sendEmail(String subject, String message, List<String> receivers) throws Exception {
        return sendEmail(subject, message, receivers, false, "");
    }
    
    /**
     * Send an e-mail notification with custom SMTP configuration
     * @param subject Subject of the notification
     * @param message Message of the notification
     * @param receivers Receivers of the notification
     * @param transmitterMailConfiguration Transmitter mail configuration
     * @param isHTML It indicates whether the content is html
     * @param senderMask Mask sender of the notification
     * @return Mail ID
     * @throws Exception exception
     */
    public int sendEmailSMTPDefined(String subject, String message, List<String> receivers, MailConfigurationModel transmitterMailConfiguration, boolean isHTML, String senderMask) throws Exception {
        try {
            String url = getApiUrl() + "/notification/send-email";
            
            NotificationModel model = new NotificationModel();
            model.setSubject(subject);
            model.setMessage(message);
            model.setReceivers(receivers);
            model.setIs_html(isHTML);
            model.setSender_mask(senderMask);
            model.setCommunity_short_name(getCommunityShortName());
            model.setTransmitter_mail_configuration(transmitterMailConfiguration);
            
            String result = webRequestPostWithJsonObject(url, model);
            
            int mailID = 0;
            try {
                mailID = Integer.parseInt(result.trim().replaceAll("^\"|\"$", ""));
            } catch (NumberFormatException e) {
                // Si no se puede parsear, devolver 0
            }
            
            this._logHelper.debug("Email '" + subject + "' sent to " + String.join(",", receivers));
            
            return mailID;
            
        } catch (Exception ex) {
            this._logHelper.error("Error sending mail '" + subject + "' to " + String.join(",", receivers) + ": \r\n" + ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * Send an e-mail notification with custom SMTP configuration and default values
     * @param subject Subject of the notification
     * @param message Message of the notification
     * @param receivers Receivers of the notification
     * @param transmitterMailConfiguration Transmitter mail configuration
     * @return Mail ID
     * @throws Exception exception
     */
    public int sendEmailSMTPDefined(String subject, String message, List<String> receivers, MailConfigurationModel transmitterMailConfiguration) throws Exception {
        return sendEmailSMTPDefined(subject, message, receivers, transmitterMailConfiguration, false, "");
    }
    
    /**
     * Check the status of a sent e-mail with its identifier (mail_id)
     * @param mailID Mail sent identifier
     * @return MailStateModel with pending and error emails
     * @throws Exception exception
     */
    public MailStateModel mailState(int mailID) throws Exception {
        try {
        	String url = getApiUrl() + "/notification/mail-state?mail_id=" + mailID;            
            String response = null;
            try {
                response = webRequest("GET", url, "application/json");
            } catch (Exception webEx) {
                System.err.println("Error en WebRequest: " + webEx.getMessage());
                throw webEx;
            }

            if (response == null || response.trim().isEmpty()) {
                return new MailStateModel();
            }

            Gson gson = new Gson();
            MailStateModel mailStateModel = gson.fromJson(response, MailStateModel.class);

            this._logHelper.debug("Get mails sent with ID: " + mailID);

            return mailStateModel;

        } catch (Exception ex) {
            System.err.println("ERROR MailState: " + ex.getMessage());
            ex.printStackTrace();
            this._logHelper.error("Error getting mails with ID: " + mailID + ": \r\n" + ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * Ingest notification HTML
     * @param usuario User identifier
     * @param comunidad Community identifier
     * @param contenidoNotificacion Notification content
     * @param fechaNotificacion Notification date
     * @throws Exception exception
     */
    public void ingestNotificationHtml(UUID usuario, UUID comunidad, String contenidoNotificacion, Date fechaNotificacion) throws Exception {
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String fechaFormateada = sdf.format(fechaNotificacion);

            String url = getApiUrl() + "/notification/ingest-notifications-html?" +
                         "usuario=" + usuario.toString() +
                         "&comunidad=" + comunidad.toString() +
                         "&contenidoNotificacion=" + URLEncoder.encode(contenidoNotificacion, StandardCharsets.UTF_8) +
                         "&fechaNotificacion=" + URLEncoder.encode(fechaFormateada, StandardCharsets.UTF_8);
            webRequest("POST", url);
            this._logHelper.debug("Default notification ingested for user: " + usuario);
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
            this._logHelper.error("Error ingesting HTML notification for user: " + usuario + ": \r\n" + ex.getMessage());
            throw ex;
        }
    }
    
    /**
     * Ingest notification default
     * @param usuario User identifier
     * @param comunidad Community identifier
     * @param contenidoNotificacion Notification content
     * @param urlNotificacion Notification URL
     * @param fechaNotificacion Notification date
     * @throws Exception exception
     */
    public void ingestNotificationDefault(UUID usuario, UUID comunidad, String contenidoNotificacion, String urlNotificacion, Date fechaNotificacion) throws Exception {
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            String fechaFormateada = sdf.format(fechaNotificacion);
            
            String url = getApiUrl() + "/notification/ingest-notifications-default?" +
                         "usuario=" + usuario.toString() +
                         "&comunidad=" + comunidad.toString() +
                         "&contenidoNotificacion=" + URLEncoder.encode(contenidoNotificacion, StandardCharsets.UTF_8) +
                         "&urlNotificacion=" + URLEncoder.encode(urlNotificacion, StandardCharsets.UTF_8) +
                         "&fechaNotificacion=" + URLEncoder.encode(fechaFormateada, StandardCharsets.UTF_8);
            
            webRequest("POST", url);
            
            this._logHelper.debug("Default notification ingested for user: " + usuario);
            
        } catch (Exception ex) {
            System.err.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
            this._logHelper.error("Error ingesting HTML notification for user: " + usuario + ": \r\n" + ex.getMessage());
            throw ex;
        }
    }
}