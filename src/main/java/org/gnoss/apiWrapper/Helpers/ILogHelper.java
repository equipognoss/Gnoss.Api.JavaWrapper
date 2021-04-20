/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.Helpers;

/**
 *
 * @author salopez
 */
public interface ILogHelper {
    
    /**
     * Write a trace log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void Trace(String message, String className, String memberName);
    void Trace(String message, String className);
    void Trace(String message);
    
    
    /**
     * Write a debug log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void Debug(String message, String className, String memberName);
    void Debug(String message, String className);
    void Debug(String message);
    
    /**
     * Write a information log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void Info(String message, String className, String memberName);
    void Info(String message, String className);
    void Info(String message);
    
    /**
     * Write a warn log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void Warn(String message, String className, String memberName);
    void Warn(String message, String className);
    void Warn(String message);
    
    /**
     * Write a error log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void Error(String message, String className, String memberName);
    void Error(String message, String className);
    void Error(String message);
    
    /**
     * Write a fatal log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void Fatal(String message, String className, String memberName);
    void Fatal(String message, String className);
    void Fatal(String message);
}
