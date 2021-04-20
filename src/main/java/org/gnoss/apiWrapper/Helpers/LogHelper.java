/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//REVISAR
package org.gnoss.apiWrapper.Helpers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;
import org.gnoss.apiWrapper.Utils.LogsAndTracesLocation;

/**
 *
 * @author salopez
 */
public class LogHelper {
    
    //Static Members
    private static String _logDirectory;
    private static ILogHelper instance;
    
    private static LogLevels LogLevel = LogLevels.WARN;
    private static String LogFileName;
    private static LogsAndTracesLocation LogLocation;
    
    //Properties
    public LogHelper(){
        LogHelper.LogLevel = LogLevels.WARN;
    }
        
    public static ILogHelper getInstance(){
        try{
            if(instance == null){
                instance = new LogHelperFile();
            }
            
            return instance;
        }
        catch(Exception logEx){
            throw logEx;
        }
    }
    
    public static String getLogDirectory(){
        return _logDirectory;
    }
    
    public static void setLogDirectory(String logDirectory){
        _logDirectory = logDirectory;
        
        if(!StringUtils.isEmpty(_logDirectory)){
            if(Paths.get(_logDirectory).getParent() == null){
                //TODO
                //_logDirectory = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + _logDirectory;
            }
            
            Path logDirectoryPath = Paths.get(_logDirectory);            
            if(!Files.exists(logDirectoryPath)){
                File directory = new File(_logDirectory);
                directory.mkdir();
            }
        }
    }
    
    public static String getLogFileName(){
        return LogFileName;
    }
    
    public static void setLogFileName(String logFileName){
        LogFileName = logFileName;
    }
    
    public void setInstance(ILogHelper instance){
        this.instance = instance;
    }
    
    public static LogLevels getLogLevel(){
        return LogLevel;
    }
    
    public static void setLogLevel(LogLevels logLevel){
        LogLevel = logLevel;
    }
    public static LogsAndTracesLocation getLogLocation() {
		return LogLocation;
	}

	public static void setLogLocation(LogsAndTracesLocation logLocation) {
		LogLocation = logLocation;
	}

	
}
