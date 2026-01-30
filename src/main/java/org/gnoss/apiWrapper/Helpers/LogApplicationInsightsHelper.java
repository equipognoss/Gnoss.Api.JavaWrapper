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
	public void trace(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if (LogHelper.getLogLevel().ordinal()<=LogLevels.TRACE.ordinal()) {
			write(LogLevels.TRACE, className, memberName, message,3);
		}
		
	}


	@Override
	public void trace(String message, String className) {
		// TODO Auto-generated method stub
		trace(message,className, "");
	}

	@Override
	public void trace(String message) {
		// TODO Auto-generated method stub
		trace(message, "", "");
	}

	@Override
	public void debug(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.DEBUG.ordinal()) {
			write(LogLevels.DEBUG, className, memberName, message,3);
		}
	}

	@Override
	public void debug(String message, String className) {
		// TODO Auto-generated method stub
		debug(message,className, "");
	}

	@Override
	public void debug(String message) {
		// TODO Auto-generated method stub
		debug(message, "", "");
	}

	@Override
	public void info(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.INFO.ordinal()) {
			write(LogLevels.INFO, className, memberName, message,3);
		}
	}

	@Override
	public void info(String message, String className) {
		// TODO Auto-generated method stub
		info(message, className, "");
	}

	@Override
	public void info(String message) {
		// TODO Auto-generated method stub
		info(message, "", "");
	}

	@Override
	public void warn(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.ERROR.ordinal()) {
			write(LogLevels.ERROR, className, memberName, message, 3);
		}
	}

	@Override
	public void warn(String message, String className) {
		// TODO Auto-generated method stub
		warn(message, className, "");
	}

	@Override
	public void warn(String message) {
		// TODO Auto-generated method stub
		warn(message, "", "");
	}

	@Override
	public void error(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.ERROR.ordinal()) {
			write(LogLevels.ERROR, className, memberName, message,3);
		}
	}

	@Override
	public void error(String message, String className) {
		// TODO Auto-generated method stub
		error(message, className, "");
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		error(message, "", "");
	}

	@Override
	public void fatal(String message, String className, String memberName) {
		// TODO Auto-generated method stub
		if(LogHelper.getLogLevel().ordinal()<=LogLevels.FATAL.ordinal()) {
			write(LogLevels.FATAL, className, memberName, message,3);
		}
	}

	@Override
	public void fatal(String message, String className) {
		// TODO Auto-generated method stub
		fatal(message, className, "");
	}

	@Override
	public void fatal(String message) {
		fatal(message, "", "");
	}
	
	
	private void write(LogLevels logLevels, String className, String memberName, String message, int numberWriteErrors) {
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
			try {
				fichero= new File (absolutePath);
				bw= new BufferedWriter (new FileWriter(fichero));
				System.out.println(completeMessage);
				bw.write(completeMessage);
				bw.flush();
				
			}catch(IOException ex){
				ex.printStackTrace();
				if(numberWriteErrors>0) {
					numberWriteErrors--;
					write(logLevels, className, memberName, message, numberWriteErrors);
				}
			}			
			finally {
				try {
					if(bw != null) {
						bw.close();	
					}					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (UtilTelemetry.isEstaConfiguradaTelemetria() && !LogHelper.getLogLocation().equals(LogsAndTracesLocation.File)) {
				try {
					if (logLevels.ordinal()<LogLevels.ERROR.ordinal()) {
						UtilTelemetry.enviarTelemetriaTraza(completeMessage, null, null, false);
						
					}else {
						Exception ex = new Exception(completeMessage);
						UtilTelemetry.enviarTelemetriaExcepcion(ex, completeMessage, false);
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
