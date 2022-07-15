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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void Trace(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.TRACE) <= 0){
            try {
                Write(LogLevels.TRACE, className, memberName, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogHelperFile.class.getName()).log(Level.SEVERE, null, ex);            
				ex.printStackTrace();
            } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public void Trace(String message, String className){
        Trace(message, className, "");
    }
    
    public void Trace(String message){
        Trace(message, "", "");
    }
    
    public void Debug(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.DEBUG) <= 0){
            try {
                Write(LogLevels.DEBUG, className, memberName, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogHelperFile.class.getName()).log(Level.SEVERE, null, ex);
            	ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public void Debug(String message, String className){
        Debug(message, className, "");
    }
    
    public void Debug(String message){
        Debug(message, "", "");
    }
    
    public void Info(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.INFO) <= 0){
            try {
                Write(LogLevels.INFO, className, memberName, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogHelperFile.class.getName()).log(Level.SEVERE, null, ex);
            	ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public void Info(String message, String className){
        Info(message, className, "");
    }
    
    public void Info(String message){
        Info(message, "", "");
    }
    
    public void Warn(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.WARN) <= 0){
            try {
                Write(LogLevels.WARN, className, memberName, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogHelperFile.class.getName()).log(Level.SEVERE, null, ex);            
				ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public void Warn(String message, String className){
        Warn(message, className, "");
    }
    
    public void Warn(String message){
        Warn(message, "", "");
    }
    
    public void Error(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.ERROR) <= 0){
            try {
                Write(LogLevels.ERROR, className, memberName, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogHelperFile.class.getName()).log(Level.SEVERE, null, ex);            
				ex.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public void Error(String message, String className){
        Error(message, className, "");
    }
    
    public void Error(String message){
        Error(message, "", "");
    }
    
    public void Fatal(String message, String className, String memberName){
        if(LogHelper.getLogLevel().compareTo(LogLevels.FATAL) <= 0){
            try {
                Write(LogLevels.FATAL, className, memberName, message);
            } catch (InterruptedException ex) {
                Logger.getLogger(LogHelperFile.class.getName()).log(Level.SEVERE, null, ex);
            	ex.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    
    public void Fatal(String message, String className){
        Fatal(message, className, "");
    }
    
    public void Fatal(String message){
        Fatal(message, "", "");
    }
    
    //Private methods
    
    private void Write(LogLevels logLevel, String className, String memberName, String message, int numberWriteErrors) throws InterruptedException, IOException{
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
                Thread.sleep(500);
                if(numberWriteErrors > 0){
                    numberWriteErrors --;
                    Write(logLevel, className, memberName, message, numberWriteErrors);
                }
            }
            finally {
            	if(sw != null) {
            		sw.flush();
            		sw.close();
            	}
            	if(os != null) {
                    os.flush();
                    os.close();            		
            	}
            }
        }
    }
    
    private void Write(LogLevels logLevel, String className, String memberName, String message) throws InterruptedException, IOException {
        Write(logLevel, className, memberName, message, 3);
    }
    
    
}
