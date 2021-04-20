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
public enum LogLevels {

    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5),
    OFF(6);

    private int logLevel;

    LogLevels(int logLevel) {
        this.logLevel = logLevel;
    }
}
