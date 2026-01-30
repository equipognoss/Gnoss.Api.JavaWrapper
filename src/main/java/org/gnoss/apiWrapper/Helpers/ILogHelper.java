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
    void trace(String message, String className, String memberName);
    void trace(String message, String className);
    void trace(String message);
    
    
    /**
     * Write a debug log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void debug(String message, String className, String memberName);
    void debug(String message, String className);
    void debug(String message);
    
    /**
     * Write a information log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void info(String message, String className, String memberName);
    void info(String message, String className);
    void info(String message);
    
    /**
     * Write a warn log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void warn(String message, String className, String memberName);
    void warn(String message, String className);
    void warn(String message);
    
    /**
     * Write a error log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void error(String message, String className, String memberName);
    void error(String message, String className);
    void error(String message);
    
    /**
     * Write a fatal log message
     * @param message Message to write
     * @param className (Optional) Class name who invokes this method
     * @param memberName (Optional) Method name who infoes this
     */
    void fatal(String message, String className, String memberName);
    void fatal(String message, String className);
    void fatal(String message);
}
