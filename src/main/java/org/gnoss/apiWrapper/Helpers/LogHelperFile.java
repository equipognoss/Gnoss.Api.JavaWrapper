/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.Helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author salopez
 */
public class LogHelperFile implements ILogHelper {
    //Member
    private boolean _isActivated;
    
    //Constructor
    public LogHelperFile(){
        if(StringUtils.isEmpty(LogHelper.getLogDirectory())){
            _isActivated = false;
            System.out.println("Warning: The log has not been configured");
        }
        else{
            if(StringUtils.isEmpty(LogHelper.getLogFileName())){
                LogHelper.setLogFileName("gnoss_api.log");
            }
            _isActivated = true;
        }
    }
    
    //Public methods
    public void trace(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.TRACE) <= 0){
        	write(LogLevels.TRACE, className, memberName, message);       
        }
    }
    
    public void trace(String message, String className){
        trace(message, className, "");
    }
    
    public void trace(String message){
        trace(message, "", "");
    }
    
    public void debug(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.DEBUG) <= 0){
        	write(LogLevels.DEBUG, className, memberName, message);
        }
    }
    
    public void debug(String message, String className){
        debug(message, className, "");
    }
    
    public void debug(String message){
        debug(message, "", "");
    }
    
    public void info(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.INFO) <= 0){
        	write(LogLevels.INFO, className, memberName, message);
        }
    }
    
    public void info(String message, String className){
        info(message, className, "");
    }
    
    public void info(String message){
        info(message, "", "");
    }
    
    public void warn(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.WARN) <= 0){
        	write(LogLevels.WARN, className, memberName, message);
        }
    }
    
    public void warn(String message, String className){
        warn(message, className, "");
    }
    
    public void warn(String message){
        warn(message, "", "");
    }
    
    public void error(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.ERROR) <= 0){
        	write(LogLevels.ERROR, className, memberName, message);
        }
    }
    
    public void error(String message, String className){
        error(message, className, "");
    }
    
    public void error(String message){
        error(message, "", "");
    }
    
    public void fatal(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.FATAL) <= 0){
        	write(LogLevels.FATAL, className, memberName, message);            
        }
    }
    
    public void fatal(String message, String className){
        fatal(message, className, "");
    }
    
    public void fatal(String message){
        fatal(message, "", "");
    }
    
    //Private methods
    
    private void write(LogLevels logLevel, String className, String memberName, String message, int numberWriteErrors) {
        if(_isActivated){
            OutputStreamWriter sw = null;
            OutputStream os = null;
            try{
                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter firstDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd");
                DateTimeFormatter secondDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter thirdDateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                String fileNameWithDate = localDateTime.format(firstDateFormat) + "_" + LogHelper.getLogFileName();
                String absolutePath = LogHelper.getLogDirectory() + "/" + fileNameWithDate;
                String currentDate = localDateTime.format(secondDateFormat);
                String currentTime = localDateTime.format(thirdDateFormat);
                String completeMessage = currentDate + "\t" + currentTime + "\t" + Thread.currentThread().getId() + "\t" + logLevel.toString() + "\t" + className + "\t" + memberName + "\t" + message;
            
                os = new FileOutputStream(absolutePath);
                sw = new OutputStreamWriter(os);
                System.out.println(completeMessage);
                sw.write(completeMessage);               
            }
            catch(Exception ex){
                try {
					Thread.sleep(500);
				} catch (Exception e) {					
					Thread.currentThread().interrupt();
				}
                if(numberWriteErrors > 0){
                    numberWriteErrors --;
                    write(logLevel, className, memberName, message, numberWriteErrors);
                }
            }
            finally {
            	cerrarFileOutputStreamWriter(sw);
            	cerrarFileOutputStream(os);
            }
        }
    }
    
    private void write(LogLevels logLevel, String className, String memberName, String message) {
        write(logLevel, className, memberName, message, 3);
    }
    
	/**
	 * Cierra y controla la excepción de los OutputStream
	 * @param outputStream
	 */
	private void cerrarFileOutputStream(OutputStream outputStream) {
		try {
			if(outputStream != null) {
				outputStream.close();	
			}
		} catch (IOException e) {
		}
	}
	
	/**
	 * Cierra y controla la excepción de los OutputStreamWriter
	 * @param outputStreamWriter
	 */
	private void cerrarFileOutputStreamWriter(OutputStreamWriter outputStreamWriter) {
		try {
			if(outputStreamWriter != null) {
				outputStreamWriter.close();	
			}
		} catch (IOException e) {
		}
	}
}
