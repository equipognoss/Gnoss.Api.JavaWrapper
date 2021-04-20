package org.gnoss.apiWrapper.Utils;

public enum LogsAndTracesLocation {

		File(0),
	    ApplicationInsights(1),
	    FileAndAppInsights(2),
	    Logstash(3),
	    FileAndLogstah(4);
	    

	    private int logAndTracesLocation;

	LogsAndTracesLocation(int logAndTracesLocation) {
	        this.logAndTracesLocation = logAndTracesLocation;
	    }
}
