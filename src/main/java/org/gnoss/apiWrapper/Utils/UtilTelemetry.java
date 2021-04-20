package org.gnoss.apiWrapper.Utils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.time.StopWatch;
import com.microsoft.applicationinsights.core.dependencies.xstream.io.json.AbstractJsonWriter.Type;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.SeverityLevel;
import com.microsoft.applicationinsights.telemetry.TelemetryContext;

public class UtilTelemetry {
	
	public static TelemetryClient telemetryClient=new TelemetryClient();
	public static TelemetryContext telemetryContext = telemetryClient.getContext(); 
	public static TelemetryConfiguration telemetryConfiguration= new TelemetryConfiguration();
	private static boolean EstaConfiguradaTelemetria;
	private static boolean ModoDepuracion;
	private static boolean EstaActiva;
	private static String InstrumentationKey;

	

	public static TelemetryClient getTelemetry() {
		if(telemetryClient.getContext().getInstrumentationKey().isEmpty() || telemetryClient.getContext().getInstrumentationKey()==null) {
			telemetryClient= new TelemetryClient();
			//telemetryClient.getContext().getComponent().setVersion(""); ////Obviar linea de código!!!!!!!
																
		}
		return telemetryClient;
	}

	public static void setTelemetry(TelemetryClient telemetry) {
		telemetryClient = telemetry;
	}
	
	public static void EnviarTelemetriaEvento (String pNombreEvento, Map<String, String> pPropiedades, Map<String, Double> pMetricas ) {
		if (isEstaConfiguradaTelemetria()) {
			if(isEstaConfiguradaTelemetria()) {
			telemetryClient.trackEvent(pNombreEvento, pPropiedades, pMetricas);
			}
		}
	}
	
	public static void EnviarTelemetriaTraza(String pMensajeTraza, String pNombreDependencia, StopWatch pReloj, boolean pExito) {
		if (isEstaConfiguradaTelemetria()) {
			LocalDateTime localDate = LocalDateTime.now();
			Duration duracion =null;
			
			if(pReloj!=null) {
				duracion= new Duration(pReloj.getTime());
			}
			telemetryClient.trackDependency(pNombreDependencia, pNombreDependencia, duracion, pExito);
		}
	}
	
	public static void EnviarTelemetriaExcepcion(Exception pException, String pMensajeExtra, boolean pErrorCritico) {				
		if(isEstaConfiguradaTelemetria()) {
			telemetryClient.trackException(pException);
			SeverityLevel nivelError= SeverityLevel.Information;
			if(pErrorCritico) {
				nivelError=SeverityLevel.Critical;
			}
			telemetryClient.trackTrace(pMensajeExtra, nivelError);
		}
	}
	
	
	
	//region Propiedades 
	
	public static boolean isEstaConfiguradaTelemetria() {
		return(telemetryContext.getInstrumentationKey()!= null || !telemetryContext.getInstrumentationKey().isEmpty());
	}
	
	public static void setEstaConfiguradaTelemetria(boolean estaConfiguradaTelemetria) {
		EstaConfiguradaTelemetria = estaConfiguradaTelemetria;
	}

	
	public static boolean isModoDepuracion() {
		if(telemetryConfiguration.getActive().getChannel().isDeveloperMode()) {
			return telemetryConfiguration.getActive().getChannel().isDeveloperMode();
		}else {
			return false;
		}
	}

	public static void setModoDepuracion(boolean modoDepuracion) {
		telemetryConfiguration.getActive().getChannel().setDeveloperMode(modoDepuracion);
	}
	
	public static boolean isEstaActiva() {
		return telemetryConfiguration.getActive().isTrackingDisabled();
	}

	public static void setEstaActiva(boolean estaActiva) {
		telemetryConfiguration.getActive().setTrackingIsDisabled(estaActiva);
	}
	
	public static String getInstrumentationKey() {
		return telemetryConfiguration.getActive().getInstrumentationKey();
	}

	public static void setInstrumentationKey(String instrumentationKey) {
		InstrumentationKey = instrumentationKey;
	}
	//endregion
}
