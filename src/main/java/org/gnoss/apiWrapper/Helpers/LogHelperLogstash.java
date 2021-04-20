package org.gnoss.apiWrapper.Helpers;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import com.microsoft.applicationinsights.core.dependencies.apachecommons.logging.LogFactory;
import com.microsoft.applicationinsights.core.dependencies.http.client.protocol.HttpClientContext;
import com.microsoft.applicationinsights.core.dependencies.http.protocol.BasicHttpContext;
import com.microsoft.applicationinsights.core.dependencies.http.protocol.HttpContext;
import com.microsoft.applicationinsights.core.dependencies.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.Object;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHelperLogstash implements ILogHelper{
	
	private static Logger LOG; 
	private static DatosRequest request;
	private RequestAttributes _httpContextAccessor= RequestContextHolder.getRequestAttributes();
	private ILogHelper mLog;
	private static String EndPoint;

	

	@Override
	public void Trace(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().compareTo(LogLevels.TRACE) <= 0){
            EnviarLog(LogLevels.TRACE, className, memberName, message);
        }
	}


	@Override
	public void Trace(String message, String className) {
		// TODO Auto-generated method stub
		Trace(message, className, "");
	}

	@Override
	public void Trace(String message) {
		// TODO Auto-generated method stub
		Trace(message, "", "");
	}

	@Override
	public void Debug(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().compareTo(LogLevels.DEBUG) <= 0){
            EnviarLog(LogLevels.DEBUG, className, memberName, message);
        }
	}

	@Override
	public void Debug(String message, String className) {
		// TODO Auto-generated method stub
		Debug(message, className, "");
	}

	@Override
	public void Debug(String message) {
		// TODO Auto-generated method stub
		Debug(message, "", "");
	}

	@Override
	public void Info(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().compareTo(LogLevels.INFO) <= 0){
            EnviarLog(LogLevels.INFO, className, memberName, message);
        }
	}

	@Override
	public void Info(String message, String className) {
		// TODO Auto-generated method stub
		Info(message, className, "");
	}

	@Override
	public void Info(String message) {
		// TODO Auto-generated method stub
		Info(message, "", "");
	}

	@Override
	public void Warn(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().compareTo(LogLevels.WARN) <= 0){
            EnviarLog(LogLevels.WARN, className, memberName, message);
        }
	}

	@Override
	public void Warn(String message, String className) {
		// TODO Auto-generated method stub
		Warn(message, className, "");
	}

	@Override
	public void Warn(String message) {
		// TODO Auto-generated method stub
		Warn(message, "", "");
	}

	@Override
	public void Error(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().compareTo(LogLevels.ERROR) <= 0){
            EnviarLog(LogLevels.ERROR, className, memberName, message);
        }
	}

	@Override
	public void Error(String message, String className) {
		// TODO Auto-generated method stub
		Error(message, className, "");
	}

	@Override
	public void Error(String message) {
		// TODO Auto-generated method stub
		Error(message, "", "");
	}

	@Override
	public void Fatal(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().compareTo(LogLevels.FATAL) <= 0){
            EnviarLog(LogLevels.FATAL, className, memberName, message);
        }
	}

	@Override
	public void Fatal(String message, String className) {
		// TODO Auto-generated method stub
		Fatal(message, className, "");
	}

	@Override
	public void Fatal(String message) {
		// TODO Auto-generated method stub
		Fatal(message, "", "");
	}

	private void EnviarLog(LogLevels logLevels, String className, String memberName, String message) {
		// TODO Auto-generated method stub
		int numberWriteErrors=3;
		try {
		
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter secondDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter thirdDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentDate= localDateTime.format(secondDateFormat);
		String currentTime=localDateTime.format(thirdDateFormat);
		String completeMessage=MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}", currentDate, currentTime, Thread.currentThread(), logLevels.toString(), className, memberName, message);
		
		request= new DatosRequest();
		
		if(_httpContextAccessor.getSessionId()!=null) {
			HttpServletRequest currentRequest= ((ServletRequestAttributes) _httpContextAccessor).getRequest();;
			Cookie[] listaCookies=currentRequest.getCookies();
			
			String headers="";
			Enumeration enumeration= currentRequest.getHeaderNames();
			List<String> arrayHeaders= Collections.list(enumeration);
			
			for(String clave : arrayHeaders){
				String valor=currentRequest.getHeader(clave);
				headers= headers + MessageFormat.format("{0} : {1}\t",clave, valor);
			}
			request.setHeaders(headers);
			request.setHttpMethod(currentRequest.getMethod());
			request.setRawURL(currentRequest.getRequestURL().toString());
			request.setURL(currentRequest.getRequestURL().toString());
			request.setUserAgent("User-Agent");
			request.setDomain(currentRequest.getRemoteHost());
			
			if(currentRequest.getContentLength()!=0) {
				InputStreamReader sr = null;
				
			}
			
			String ip= currentRequest.getHeader("X-FORWARDED-FOR");
			if (ip==null) {
				ip=_httpContextAccessor.getSessionId().toString();
				request.setIp(ip);
			}
			
		}
		
		
		String data= currentDate+" "+currentTime+" "+Thread.currentThread()+" "+logLevels.toString()+" "+className+" "+memberName+" "+message;
		Logger LOG = Logger.getLogger("Mi Logger"); 
		LOG.config(completeMessage);
		
		
		String filePath=""; //Definir ruta
		String logPath=""; //Definir directorio
		File file= new File(filePath);
		
        this.mLog = LogHelper.getInstance();
		
		 new LogHelper().setLogDirectory(logPath);
         LogHelper.setLogFileName(file.getName());
         
         switch (logLevels) {
             case TRACE:
                 LogHelper.setLogLevel(LogLevels.TRACE);
                 LOG.config("TRACE "+ data + "\r\n");
                 break;
             case DEBUG:
                 LogHelper.setLogLevel(LogLevels.DEBUG);
                 LOG.config("DEBUG "+ data + "\r\n");
                 break;
             case INFO:
                 LogHelper.setLogLevel(LogLevels.INFO);
                 LOG.config("INFO "+ data+ "\r\n");
                 break;
             case WARN:
                 LogHelper.setLogLevel(LogLevels.WARN);
                 LOG.config("WARN "+ data+ "\r\n");
                 break;
             case ERROR:
                 LogHelper.setLogLevel(LogLevels.ERROR);
                 LOG.config("ERROR "+ data+ "\r\n");
                 break;
             case FATAL:
                 LogHelper.setLogLevel(LogLevels.FATAL);
                 LOG.config("FATAL "+ data+ "\r\n");
                 break;
             case OFF:
                 LogHelper.setLogLevel(LogLevels.OFF);
                 LOG.config("OFF "+ data+ "\r\n");
                 break;
         }
         System.out.println("Mensaje enviado" + completeMessage);
		
	}
	catch(Exception e ) {
		e.printStackTrace();
		if(numberWriteErrors>0) {
			numberWriteErrors--;
			EnviarLog(logLevels, className, memberName, message);
		}
	}

}
	public static String getEndPoint() {
		return EndPoint;
	}


	public static void setEndPoint(String endPoint) {
		EndPoint = endPoint;
	}

}
