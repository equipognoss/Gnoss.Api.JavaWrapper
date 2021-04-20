package org.gnoss.apiWrapper.models;

import java.util.Calendar;

public class DateOntologyProperty extends OntologyProperty{

	public DateOntologyProperty(String label, Calendar date){
		if(date != null)
			setName(label);
			setValue(ConvertIntToStringWithLimit(date.YEAR, 4) + ConvertIntToStringWithLimit(date.MONTH, 2) + ConvertIntToStringWithLimit(date.DAY_OF_MONTH, 2) +
			ConvertIntToStringWithLimit(date.HOUR_OF_DAY, 2) + ConvertIntToStringWithLimit(date.MINUTE, 2) + ConvertIntToStringWithLimit(date.SECOND, 2));
		}
	
	private String ConvertIntToStringWithLimit(int number, int limit){
		String num = Integer.toString(number);
		
		while(num.length() < limit){
			num = "0" + num;
		}
		return num;
	}
}
