package org.gnoss.apiWrapper.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateOntologyProperty extends OntologyProperty {

	public DateOntologyProperty(String label, Calendar date) {
		if (date != null) {
			setName(label);
			setValue(ConvertirCalendarFormatoGnoss(date));
		}
	}

	private String ConvertirCalendarFormatoGnoss(Calendar pFecha) {		
		return new SimpleDateFormat("yyyyMMddHHmmss").format(pFecha.getTime());

	}
}
