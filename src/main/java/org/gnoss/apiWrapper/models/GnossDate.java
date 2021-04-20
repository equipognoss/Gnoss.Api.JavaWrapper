package org.gnoss.apiWrapper.models;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gnoss.apiWrapper.Excepciones.GnossAPIDateException;
import org.gnoss.apiWrapper.Helpers.DateTypes;

public class GnossDate {
	
	//region Members
	private String _millenniumRoman;
    private String _centuryRoman;
    private String _day;
    private String _month;
    private String _year;
    
    private int millenium;
    private int century;
    private String milleniumRoman;
    private String normDate;
    private String precisionDate;
    private String typeDate;
    private String centuryRoman;
    private String day;
    private String month;
    private String year;
    private String hour;
    private String minutes;
    private String seconds;
    //endRegion Members
    
    //region Patterns
    private String _patternCenturyStartByS = "^S[\\.]*"; // century or century range
    private String _patternContainsRomano = "^[IVXLCDMivxlcdm]+";
    private String _patternDateTypeAC = "a[\\.]*C[\\.]*";
    private String _patternDateTypeDC = "d[\\.]*C[\\.]*";
    private String _patternDateTypeBP = "B[\\.]*P[\\.]*";
    private String _patternDateAccurancyTypeAprox = "aprox\\s*\\.*";
    private String _patternDateAccurancyCA = "(\\[)+ca(\\])+";
    private String _patternDateAccurancyDoubtful = "\\¿|\\?";
    private String _patternMillennium = "milenio";
    private String _patternDDMMYYYY = "^([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0]?[1-9]|[1][0-2])[./-]([0-9]{3,4})$";
    private String _patternMMDDYYYY = "^([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0]?[1-9]|[1|2][0-9]|[3][0|1])[./-]([0-9]{3,4})$";
    private String _patternMMYYYY = "^([0]?[1-9]|[1][0-2])[./-]([0-9]{3,4})$";
    private String _patternYYYY = "^([0-9]{1,4})$";
    private String _patternYYYYMMDD = "^([0-9]{3,4})[./-]([0]?[1-9]|[1][0-2])[./-]([0]?[1-9]|[1|2][0-9]|[3][0|1])$";
    private String _patternYYYYMM = "^([0-9]{3,4})[./-]([0]?[1-9]|[1][0-2])$";
    private String _patternDDLettersMonthYYYY = "^([0]?[1-9]|[1|2][0-9]|[3][0|1])[\s]*[de]*[\s]*(Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)[\s]*[de|del]*[\s]*([0-9]{3,4})$";

    private String _patternLettersMonthDDYYYY = "^((Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)*[0]?[1-9]|[1|2][0-9]|[3][0|1])[\s][de]*[\s]*[\s]*[de|del]*[\s]*([0-9]{3,4})$";

    private String _patternLettersMonthYYYY = "^(Enero|Febrero|Marzo|Abril|Mayo|Junio|Julio|Agosto|Septiembre|Octubre|Noviembre|Diciembre)[\s]*[de|del]*[\s]*([0-9]{3,4})$";
    //endRegion Patterns
    
    
    //Constructors
    
    /**
     * Constructor of GnossDate
     */
    public GnossDate() {
    	Initialize();
    }
    
    /**
     * Constructor of GnossDate
     * @param date
     * @param americanFormat 
     * @throws GnossAPIDateException
     */
    public GnossDate(String date, boolean americanFormat) throws GnossAPIDateException {
    	Initialize();
    	
    	this.typeDate=this.getDateType(date);
    	date= CleanDateType(date);
    	
    	this.precisionDate=GetDateAccurancy(date);
    	date=CleanDateAccurancy(date);
    	
    	NormalizeDate(date, americanFormat);
    	
    }
    
    //region Private methods
    
    private void NormalizeDate(String date, boolean americanFormat) throws GnossAPIDateException {
    	Pattern pattern=null;
    	if(americanFormat) {
    		_patternDDMMYYYY = _patternMMDDYYYY;
            _patternDDLettersMonthYYYY = _patternLettersMonthDDYYYY;
    	}
    	pattern= Pattern.compile(_patternDDMMYYYY);
    	if(Pattern.matches(date, _patternDDMMYYYY)) {
    		//List<String> allMatches = new ArrayList<String>();
    		
    		List<Matcher> lista = new ArrayList<Matcher>();
   		 	Matcher matcher= pattern.matcher(date);
   		 
   		 	while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
   		 	for(Matcher  s: lista) {
   		 		if(americanFormat) {
   		 			this.setMonth(s.group(1).toString());
   		 			this.setDay(s.group(2).toString());
   		 			this.setDay(s.group(3).toString());
   		 		}
   		 		else {
   		 		this.setMonth(s.group(1).toString());
		 			this.setDay(s.group(2).toString());
		 			this.setDay(s.group(3).toString());
   		 		}
   		 	}
    	}
    	else if(Pattern.matches(date, _patternMMYYYY)) {
    		pattern=Pattern.compile(_patternMMYYYY);
    		List<Matcher> lista= new ArrayList<Matcher>();
    		Matcher matcher=pattern.matcher(date);
    		
    		while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
    		for(Matcher m :lista) {
    			this.setMonth(m.group(1).toString());
    			this.setYear(m.group(2).toString());
    		}
    	}else if(Pattern.matches(date, _patternYYYY)) {
    		pattern=Pattern.compile(_patternYYYY);
    		List<Matcher> lista= new ArrayList<Matcher>();
    		Matcher matcher=pattern.matcher(date);
    		
    		while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
    		for(Matcher m :lista) {
    			
    			this.setYear(m.group(1).toString());
    		}
    	}else if(Pattern.matches(date, _patternYYYYMM)) {
    		pattern=Pattern.compile(_patternYYYYMM);
    		List<Matcher> lista= new ArrayList<Matcher>();
    		Matcher matcher=pattern.matcher(date);
    		
    		while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
    		for(Matcher m :lista) {
    			
    			this.setYear(m.group(1).toString());
    			this.setMonth(m.group(2).toString());
    		}
    	}else if(Pattern.matches(date, _patternYYYYMMDD)) {
    		pattern=Pattern.compile(_patternYYYYMMDD);
    		List<Matcher> lista= new ArrayList<Matcher>();
    		Matcher matcher=pattern.matcher(date);
    		
    		while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
    		for(Matcher m :lista) {
    			
    			this.setYear(m.group(1).toString());
    			this.setMonth(m.group(2).toString());
    			this.setDay(m.group(3).toString());
    		}
    	}else if(Pattern.matches(date, _patternDDLettersMonthYYYY)) {
    		pattern=Pattern.compile(_patternDDLettersMonthYYYY);
    		List<Matcher> lista= new ArrayList<Matcher>();
    		Matcher matcher=pattern.matcher(date);
    		
    		while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
    		for(Matcher m :lista) {
    			
    			if(americanFormat) {
    				this.setYear(m.group(3).toString());
    				this.setMonth(m.group(1).toString());
    				this.setDay(m.group(2).toString());
    			}else {
    				this.setDay(m.group(1).toString());
    				this.setMonth(m.group(2).toString());
    				this.setYear(m.group(3).toString());
    			}
    		}
    	}else if(Pattern.matches(date, _patternLettersMonthYYYY)) {
    		pattern=Pattern.compile(_patternLettersMonthYYYY);
    		List<Matcher> lista= new ArrayList<Matcher>();
    		Matcher matcher=pattern.matcher(date);
    		
    		while(matcher.find()) {
   		 		lista.add(matcher);
   		 	}
    		for(Matcher m :lista) {
    			
    			this.setYear(m.group(2).toString());
    			this.setMonth(m.group(1).toString());
    			
    		}
    	}else if(Pattern.matches(date, _patternMillennium)) {
    		date=CleanMillenium(date);
    		this.setMilleniumRoman(date);
    	}
    	else if(Pattern.matches(date, _patternCenturyStartByS)|| Pattern.matches(date, _patternContainsRomano)) {
    		date=CleanCentury(date);
    		this.setCenturyRoman(date);
    	}else {
    		throw new GnossAPIDateException ("Couldn't normalize date");    	
    	}
    }
    
    private void Initialize() {
    	 _day = "00";
         _month = "00";
         _year = "0000";
         setHour("00"); 
         setMinutes("00"); 
         setSeconds("00");

         setCentury(0); 
         setMillenium(0);

         _millenniumRoman = null;
         setNormDate(null); 
         setPrecisionDate(null); 
         setTypeDate(null); 
         _centuryRoman = null;
    }
    
    /**
     * Converts a roman number to decimal number
     * Code gets from http://my.opera.com/RAJUDASA/blog/2013/04/08/c-convert-roman-numbers-to-decimal-numbers
     * 
     * Explicación:
     * code process:
     *convert string to char array.
     *convert each char (roman number) to its equivalent decimal number and stores them in array.
     *Iterating over array, check whether low number is left side next to high number if yes, remove that low number from high number else add both numbers.
     *Assumes only one low number comes left side and max 3 low numbers right side at a time of high number.(ex: IV and VIII)
     * 
     * 
     * @param romanNum
     * @return
     */
    private int RomanToDecimal(String romanNum) {
    	char [] chars = romanNum.toCharArray();
    	int [] nums=null;
    	int i=0;
    	int calc=0;
    	for (char c: chars) {
    			if(c=='I') {
    				nums[i]=1; i++;
    			}else if(c=='V') {
    				nums[i]=5; i++;
    			}else if(c=='X') {
    				 nums[i]=10; i++;
    			}else if(c=='L'){
    				nums[i]=50; i++;
    			}else if(c=='C'){
    				nums[i]=100; i++;
    			}else if(c=='D'){
    				nums[i]=500; i++;
    			}else if(c=='M'){
    				nums[i]=1000; i++;
    			}else{
    				nums[i]=0; i++;
    			}
    	int mult;
    	for(int j=0; j<i; j++) {
    		if ((j+1<i)  && (nums[j+1]> nums[j])) {
    			mult=-1;
    		}else {
    			mult=1;
    		}
    		calc+= mult*nums[j];
    	}
    			
    	}	
    	return calc;
    	
    }
    
    private String CompleteStringZerosAtLeftWithLimit(String number, int limit) {
    	while(number.length()<limit) {
    		number= "0"+ number;		
    	}
    	return number;
    }
    
    private void UpdateNormalizeDate() {
    	this.setNormDate(this.getYear()+ this.getDay()+ this.getHour()+ this.getMinutes()+this.getSeconds());
    	
    	if(this.getTypeDate()!=null && this.getTypeDate().equals(DateTypes.AC))
    	{
    		this.setNormDate("-"+this.getNormDate());
    	}   
    }
    //endRegion
    
    
    
    //region Extraction and cleaning methods
    
    private String getDateType(String text)
    {
    	String dateType=null;
    	boolean ac= Pattern.matches(text, _patternDateTypeAC);
    	boolean dc= Pattern.matches(text, _patternDateTypeDC);
    	boolean bp= Pattern.matches(text, _patternDateTypeBP);
    	
    	if (ac)
        {
            dateType = DateTypes.AC;
        }
        else if (dc)
        {
            dateType = DateTypes.DC;
        }
        else if (bp)
        {
            dateType = DateTypes.BP;
        }
        else if (text.startsWith("-"))
        {
            dateType = DateTypes.AC;
        }
        return dateType;
    }  
    
    
    private String GetDateAccurancy(String text) {
    	String accurancy=null;
    	boolean aprox=Pattern.matches(text, _patternDateAccurancyTypeAprox) || Pattern.matches(text, _patternDateAccurancyCA);
    	boolean dudosa= Pattern.matches(text, _patternDateAccurancyDoubtful);
    	
    	 if (aprox)
         {
             accurancy = "ca";
         }
         else if (dudosa)
         {
             accurancy = "?";
         }
         return accurancy;
    }
    
    private String CleanMillenium(String text) {
    	Pattern pattern = Pattern.compile(_patternMillennium);
    	while(Pattern.matches(text, _patternMillennium)) {
    		 List<Matcher> allMatches = new ArrayList<Matcher>();
    		 Matcher matcher= pattern.matcher(text);
    		 
    		 while(matcher.matches()) {
    			 allMatches.add(matcher);
    		 }
    		 int mIdx=0;
    		 for(Matcher s: allMatches) {
    			 for(int gIdx=0; gIdx<allMatches.size(); gIdx++) {
    				 text=text.replace(s.group(gIdx).toString().trim(), "").trim();
    			 }
    			 mIdx++;
    		 }
    	}
    	return text;
    }
    
    private String CleanCentury(String text) {
    	Pattern pattern = Pattern.compile(_patternCenturyStartByS);
    	while(Pattern.matches(text, _patternCenturyStartByS)) {
    		 List<Matcher> allMatches = new ArrayList<Matcher>();
    		 Matcher matcher= pattern.matcher(text);
    		 
    		 while(matcher.matches()) {
    			 allMatches.add(matcher);
    		 }
    		 int mIdx=0;
    		 for(Matcher s: allMatches) {
    			 for(int gIdx=0; gIdx<allMatches.size(); gIdx++) {
    				 text=text.replace(s.group(gIdx).trim(), "").trim();
    			 }
    			 mIdx++;
    		 }
    	}
    	return text;
    }
    
    private String CleanDateType(String text) {
    	List<String> dateTypePatterns= new ArrayList<String>();
    	dateTypePatterns.add(_patternDateTypeAC);
    	dateTypePatterns.add(_patternDateTypeBP);
    	dateTypePatterns.add(_patternDateTypeDC);
    	
    	for(String dateTypePattern: dateTypePatterns) {
    		Pattern pattern= Pattern.compile(dateTypePattern);
    		if(Pattern.matches(dateTypePattern, text)) {
    			List<Matcher> allMatches = new ArrayList<Matcher>();
    			Matcher matcher= pattern.matcher(text);
    			
    			 while(matcher.matches()) {
        			 allMatches.add(matcher);
        		 }
    			
    			int mIdx=0;
    			for(Matcher s : allMatches) {
    				 for(int gIdx=0; gIdx<allMatches.size(); gIdx++) {
        				 text=text.replace(s.group(gIdx).trim(), "").trim();
        			 }
        			 mIdx++;
    			}
    		}
    	}
    	if(text.startsWith("-")){
    		text=text.substring(1);
    	}
    	return text;
    }
    
    private String CleanDateAccurancy(String text) {
    	List<String> dateTypePatterns= new ArrayList<String>();
    	dateTypePatterns.add(_patternDateAccurancyTypeAprox);
    	dateTypePatterns.add(_patternDateAccurancyDoubtful);
    	dateTypePatterns.add(_patternDateAccurancyCA);
    	
    	for(String dateTypePattern: dateTypePatterns) {
    		Pattern pattern= Pattern.compile(dateTypePattern);
    		if(Pattern.matches(dateTypePattern, text)) {
    			List<Matcher> allMatches = new ArrayList<Matcher>();
    			Matcher matcher= pattern.matcher(text);
    			
    			 while(matcher.matches()) {
        			 allMatches.add(matcher);
        		 }
    			
    			int mIdx=0;
    			for(Matcher s : allMatches) {
    				 for(int gIdx=0; gIdx<allMatches.size(); gIdx++) {
        				 text=text.replace(s.group(gIdx), "").trim();
        			 }
        			 mIdx++;
    			}
    		}
    	}
    	return text;
    }
    
    //endRegion
    
    //region Public methods
	/**
	 * Gets a GnossDate as a String
	 * @return a String
	 */
    public String ToString() {
    	
    	StringBuilder sb= new StringBuilder();
    	
    	sb.append("---------- GnossDate ------------");
    	sb.append("Millennium:" +this.getMillenium());
    	sb.append("Century:" +this.getCentury());
    	sb.append("Type Date :" +this.getTypeDate());
    	sb.append("Accurancy:" + getPrecisionDate());
    	sb.append("Normalized date:" +this.getNormDate());
    	sb.append("----------------------------------");
    	
    	return sb.toString();
    	
    }
    
    /**
     * Gets if this date is not initialized
     * @return true if the date is empty, false in other cases 
     */
    public boolean isEmpty() {
    	return (this.millenium==0 && this.century==0 && this.normDate==null && this.day.equals("00") && this.month.equals("00") && this.year.equals("0000")  && this.hour.equals("00") && this.minutes.equals("00") && this.seconds.equals("00") && this.precisionDate== null && this.typeDate==null);
    }
    //endRegion
    
    //Properties
    /**
     * Gets the millennium
     * @return millenium
     */
    public int getMillenium() {
		return millenium;
	}
    /**
     * Sets the millenium
     * @param millenium
     */
	public void setMillenium(int millenium) {
		this.millenium = millenium;
	}
	/**
	 * Gets the century
	 * @return century
	 */
	public int getCentury() {
		return century;
	}
	/**
	 * Sets the century
	 * @param century
	 */
	public void setCentury(int century) {
		this.century = century;
	}
	/**
	 * Gets the millenium in roman numbers
	 * @return _milleniumRoman
	 */
	public String getMilleniumRoman() {
		return _millenniumRoman;
	}
	/**
	 * Sets the millenium in roman numbers
	 * @param milleniumRoman
	 */
	public void setMilleniumRoman(String milleniumRoman) {
		this._millenniumRoman=getMilleniumRoman();
		this.millenium = RomanToDecimal(_millenniumRoman);
	}
	/**
	 * Gets the normalized date with the pattern yyyyMMddhhmmss
	 * @return normDate
	 */
	public String getNormDate() {
		return normDate;
	}
	/**
	 * Sets the normalized date with the pattern yyyyMMddhhmmss
	 * @param normDate
	 */
	public void setNormDate(String normDate) {
		this.normDate = normDate;
	}
	/**
	 * Gets the accurancy
	 * @return precisionDate
	 */
	public String getPrecisionDate() {
		return precisionDate;
	}
	/**
	 * Sets the accurancy
	 * @param precisionDate
	 */
	public void setPrecisionDate(String precisionDate) {
		this.precisionDate = precisionDate;
	}
	/**
	 * Gets the type of dates
	 * @return typeDate
	 */
	public String getTypeDate() {
		return typeDate;
	}
	/**
	 * sets the type of date
	 * @param typeDate
	 */
	public void setTypeDate(String typeDate) {
		this.typeDate = typeDate;
	}
	/**
	 * Gets the century in roman numbers
	 * @return _centuryRoman
	 */
	public String getCenturyRoman() {
		return _centuryRoman;
	}
	/**
	 * Sets the century in roman numbers
	 * @param centuryRoman
	 */
	public void setCenturyRoman(String centuryRoman) {
		this._centuryRoman=getCenturyRoman();
		this.century = RomanToDecimal(_centuryRoman);
	}
	/**
	 * Gets the day (2 digits)
	 * @return _day
	 */
	public String getDay() {
		return _day;
	}
	/**
	 * Sets the day (2 digits)
	 * @param day
	 */
	public void setDay(String day) {
		this._day=CompleteStringZerosAtLeftWithLimit(getDay(), 2);
		UpdateNormalizeDate();
	}
	/**
	 * Get the month (2 digits)
	 * @return _month
	 */
	public String getMonth() {
		return _month;
	}
	/**
	 * Set the month (2 digits)
	 * @param month
	 */
	public void setMonth(String month) {
		if (getMonth().toLowerCase().equals("enero"))
        {
            _month = "01";
        }
        else if (getMonth().toLowerCase().equals("febrero"))
        {
            _month = "02";
        }
        else if (getMonth().toLowerCase().equals("marzo"))
        {
            _month = "03";
        }
        else if (getMonth().toLowerCase().equals("abril"))
        {
            _month = "04";
        }
        else if (getMonth().toLowerCase().equals("mayo"))
        {
            _month = "05";
        }
        else if (getMonth().toLowerCase().equals("junio"))
        {
            _month = "06";
        }
        else if (getMonth().toLowerCase().equals("julio"))
        {
            _month = "07";
        }
        else if (getMonth().toLowerCase().equals("agosto"))
        {
            _month = "08";
        }
        else if (getMonth().toLowerCase().equals("septiembre"))
        {
            _month = "09";
        }
        else if (getMonth().toLowerCase().equals("octubre"))
        {
            _month = "10";
        }
        else if (getMonth().toLowerCase().equals("noviembre"))
        {
            _month = "11";
        }
        else if (getMonth().toLowerCase().equals("diciembre"))
        {
            _month = "12";
        }else {
        	_month=CompleteStringZerosAtLeftWithLimit(getMonth(), 2);
        }
		UpdateNormalizeDate();
	}
	/**
	 * Get the year( 4 digits)
	 * @return _year
	 */
	public String getYear() {
		return _year;
	}
	/**
	 * Set the year (4 digits)
	 * @param year
	 */
	public void setYear(String year) {
		this._year=CompleteStringZerosAtLeftWithLimit(getYear(), 4);
		UpdateNormalizeDate();
	}
	/**
	 * Get the hour (2 digits)
	 * @return hour
	 */
	public String getHour() {
		return hour;
	}
	/**
	 * Set the hour (2 digits )
	 * @param hour
	 */
	public void setHour(String hour) {
		this.hour = hour;
	}
	/**
	 * Get the minutes (2digits)
	 * @return minutes
	 */
	public String getMinutes() {
		return minutes;
	}
	/**
	 * Set the minutes (2 digits)
	 * @param minutes
	 */
	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}
	/**
	 * Get the seconds (2 digits)
	 * @return seconds
	 */
	public String getSeconds() {
		return seconds;
	}
	/**
	 * Sets the seconds (2 digits)
	 * @param seconds
	 */
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
   
    
    
    
    
    
    
}
