package org.gnoss.apiWrapper.Helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.Utils.LogsAndTracesLocation;
import org.gnoss.apiWrapper.Utils.UtilTelemetry;



public class LogApplicationInsightsHelper implements ILogHelper {
	
	private static String _logDirectory;
	private static String ImplementationKey;
	private static boolean UsarVariablesEntorno;
	private static boolean UsarApplicacionInsights;
	/**
	 *  Constructor
	 */
	public LogApplicationInsightsHelper() {
		String string=LogHelper.getLogDirectory();
		if(string.isEmpty() || string==null) {
			System.out.println("Warning: The log has not been configured");
			
		}else {
			
			String string2=LogHelper.getLogFileName();
			if(string2.isEmpty()|| string2==null ) {
				string2="gnoss_api.log";
			}
		}		
	}

	public static String getLogDirectory() {
		return _logDirectory;
	}

	public static void setLogDirectory(String logDirectory) {
		_logDirectory = logDirectory;

		if (!StringUtils.isEmpty(_logDirectory)) {
			if (Paths.get(_logDirectory).getParent() == null) {
				// TODO
				// _logDirectory =
				// getClass().getProtectionDomain().getCodeSource().getLocation().getPath() +
				// _logDirectory;
			}

			Path logDirectoryPath = Paths.get(_logDirectory);
			if (!Files.exists(logDirectoryPath)) {
				File directory = new File(_logDirectory);
				directory.mkdir();
			}
		}
	}

	@Override
	public void Trace(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if (LogHelper.getLogLevel().ordinal()<=LogLevels.TRACE.ordinal()) {
			Write(LogLevels.TRACE, className, memberName, message,3);
		}
		
	}


	@Override
	public void Trace(String message, String className) {
		// TODO Auto-generated method stub
		Trace(message,className, "");
	}

	@Override
	public void Trace(String message) {
		// TODO Auto-generated method stub
		Trace(message, "", "");
	}

	@Override
	public void Debug(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.DEBUG.ordinal()) {
			Write(LogLevels.DEBUG, className, memberName, message,3);
		}
	}

	@Override
	public void Debug(String message, String className) {
		// TODO Auto-generated method stub
		Debug(message,className, "");
	}

	@Override
	public void Debug(String message) {
		// TODO Auto-generated method stub
		Debug(message, "", "");
	}

	@Override
	public void Info(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.INFO.ordinal()) {
			Write(LogLevels.INFO, className, memberName, message,3);
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
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.ERROR.ordinal()) {
			Write(LogLevels.ERROR, className, memberName, message, 3);
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
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.ERROR.ordinal()) {
			Write(LogLevels.ERROR, className, memberName, message,3);
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
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.FATAL.ordinal()) {
			Write(LogLevels.FATAL, className, memberName, message,3);
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
	
	
	private void Write(LogLevels logLevels, String className, String memberName, String message, int numberWriteErrors) {
		// TODO Auto-generated method stub
		LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter firstDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        DateTimeFormatter secondDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter thirdDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
		String logHelper= LogHelper.getLogFileName();
		String fileNameWithDate=MessageFormat.format("{0} _ {1}", localDateTime.format(firstDateFormat), logHelper);
		String absolutePath=MessageFormat.format("{0}/{1}", LogHelper.getLogDirectory(), fileNameWithDate);
		String currentDate= localDateTime.format(secondDateFormat);
		String currentTime=localDateTime.format(thirdDateFormat);
		String completeMessage=MessageFormat.format("{0}\t{1}\t{2}\t{3}\t{4}\t{5}\t{6}", currentDate, currentTime, Thread.currentThread(), logLevels.toString(), className, memberName, message);
		
		
		
		if (LogHelper.getLogLocation().equals(LogsAndTracesLocation.ApplicationInsights)) {
			File fichero=null;
			BufferedWriter bw=null;
			Thread thread=null;
			try {
				fichero= new File (absolutePath);
				bw= new BufferedWriter (new FileWriter(fichero));
				System.out.println(completeMessage);
				bw.write(completeMessage);
				bw.flush();
				bw.close();
				
			}catch(IOException ex){
				ex.printStackTrace();
				try {
					thread.sleep(500);
					if(numberWriteErrors>0) {
						numberWriteErrors--;
						Write(logLevels, className, memberName, message, numberWriteErrors);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (UtilTelemetry.isEstaConfiguradaTelemetria() && !LogHelper.getLogLocation().equals(LogsAndTracesLocation.File)) {
				try {
					if (logLevels.ordinal()<LogLevels.ERROR.ordinal()) {
						UtilTelemetry.EnviarTelemetriaTraza(completeMessage, null, null, false);
						
					}else {
						Exception ex = new Exception(completeMessage);
						UtilTelemetry.EnviarTelemetriaExcepcion(ex, completeMessage, false);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		
	}
	//Properties region
	//Get or Set the ApplicationInsights ImplementationKey
	
	public static String getImplementationKey() {
		return ImplementationKey;
	}

	public static void setImplementationKey(String implementationKey) {
		ImplementationKey = implementationKey;
	}

	public static boolean isUsarVariablesEntorno() {
		return (System.getenv("useEnvironmentVariables") != null && System.getenv("useEnvironmentVariables").toLowerCase().equals("true"));
	}

	public static void setUsarVariablesEntorno(boolean usarVariablesEntorno) {
		UsarVariablesEntorno = usarVariablesEntorno;
	}

	public static boolean isUsarApplicacionInsights() {
		return (System.getenv("useEnvironmentVariables") != null && System.getenv("useEnvironmentVariables").toLowerCase().equals("true"));
	}

	public static void setUsarApplicacionInsights(boolean usarApplicacionInsights) {
		UsarApplicacionInsights = usarApplicacionInsights;
	}
	
	
	//endregion

}
