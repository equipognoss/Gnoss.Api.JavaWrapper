package org.gnoss.apiWrapper.Helpers;

import java.util.logging.Logger;
import java.io.File;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHelperLogstash implements ILogHelper{

	private static String EndPoint;

	@Override
	public void trace(String message, String className, String memberName) {
		if(LogHelper.getLogLevel().compareTo(LogLevels.TRACE) <= 0){
			enviarLog(LogLevels.TRACE, className, memberName, message);
		}
	}


	@Override
	public void trace(String message, String className) {
		trace(message, className, "");
	}

	@Override
	public void trace(String message) {
		trace(message, "", "");
	}

	@Override
	public void debug(String message, String className, String memberName) {
		if(LogHelper.getLogLevel().compareTo(LogLevels.DEBUG) <= 0){
			enviarLog(LogLevels.DEBUG, className, memberName, message);
		}
	}

	@Override
	public void debug(String message, String className) {
		debug(message, className, "");
	}

	@Override
	public void debug(String message) {
		debug(message, "", "");
	}

	@Override
	public void info(String message, String className, String memberName) {
		if(LogHelper.getLogLevel().compareTo(LogLevels.INFO) <= 0){
			enviarLog(LogLevels.INFO, className, memberName, message);
		}
	}

	@Override
	public void info(String message, String className) {
		info(message, className, "");
	}

	@Override
	public void info(String message) {
		info(message, "", "");
	}

	@Override
	public void warn(String message, String className, String memberName) {
		if(LogHelper.getLogLevel().compareTo(LogLevels.WARN) <= 0){
			enviarLog(LogLevels.WARN, className, memberName, message);
		}
	}

	@Override
	public void warn(String message, String className) {
		warn(message, className, "");
	}

	@Override
	public void warn(String message) {
		warn(message, "", "");
	}

	@Override
	public void error(String message, String className, String memberName) {
		if(LogHelper.getLogLevel().compareTo(LogLevels.ERROR) <= 0){
			enviarLog(LogLevels.ERROR, className, memberName, message);
		}
	}

	@Override
	public void error(String message, String className) {
		error(message, className, "");
	}

	@Override
	public void error(String message) {
		error(message, "", "");
	}

	@Override
	public void fatal(String message, String className, String memberName) {
		if(LogHelper.getLogLevel().compareTo(LogLevels.FATAL) <= 0){
			enviarLog(LogLevels.FATAL, className, memberName, message);
		}
	}

	@Override
	public void fatal(String message, String className) {
		fatal(message, className, "");
	}

	@Override
	public void fatal(String message) {
		fatal(message, "", "");
	}

	private void enviarLog(LogLevels logLevels, String className, String memberName, String message) {
		int numberWriteErrors=3;
		try {

			LocalDateTime localDateTime = LocalDateTime.now();
			DateTimeFormatter secondDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			DateTimeFormatter thirdDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
			String currentDate= localDateTime.format(secondDateFormat);
			String currentTime=localDateTime.format(thirdDateFormat);
			String completeMessage=MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}", currentDate, currentTime, Thread.currentThread(), logLevels.toString(), className, memberName, message);

			String data= currentDate+" "+currentTime+" "+Thread.currentThread()+" "+logLevels.toString()+" "+className+" "+memberName+" "+message;
			Logger LOG = Logger.getLogger("Mi Logger"); 
			LOG.config(completeMessage);


			String filePath=""; //Definir ruta
			String logPath=""; //Definir directorio
			File file= new File(filePath);

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
				enviarLog(logLevels, className, memberName, message);
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
