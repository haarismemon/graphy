package main.java.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows to access to query data.
 * 
 * @author Vladislavs Uljanovs
 */
public class Query {
	
	/**
     * Indicator code e.g. in the following format: "NY.GDP.MKTP.KD.ZG"
     * 
     */
	private String indicatorCode;
	
	/**
     * Country code e.g. in the following format: "LV"
     * 
     */
	private String countryCode;
	
	/**
     * Requested start year of the query.
     * 
     */
	private int startYear;
	
	/**
     * Requested end year of the query.
     * 
     */
	private int endYear;
	
	/**
     * The date and time when the query was made.
     * 
     */
	private Date queryDate;
	
	/**
     * Map of pairs (year and value), containing all years for the query.
     * 
     */
	Map<Integer, Double> yearValue = new HashMap<>();
	
	/**
     * Creates a query object holding.
     *
     * @param indicatorCode	indicator code of the query
     * @param countryCode	country code of the query
     * @param startYear		requested starting year
     * @param endYear		requested end year
     * @param queryDate		date and time when the query is made
     */
	public Query(String indicatorCode, String countryCode, int startYear, int endYear, Date queryDate) {
		this.indicatorCode = indicatorCode;
		this.countryCode = countryCode;
		this.startYear = startYear;
		this.endYear = endYear;
		this.queryDate = queryDate;
	}
	
	/**
	 * Returns indicator code of the query.
	 * 
	 * @return indicatorCode indicator code
	 */
	public String getIndicatorCode() {
		return indicatorCode;
	}
	
	/**
	 * Returns indicator name of the query.
	 * 
	 * @return indicator name
	 */
	public String getIndicatorName() {
		return IndicatorCodes.getIndicatorName(this.indicatorCode);
	}
	
	/**
	 * Returns country code of the query.
	 * 
	 * @return countryCode country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Returns country name of the query.
	 * 
	 * @return country name
	 */
	public String getCountryName() {
		return Country.getCountryName(this.countryCode);
	}
	
	/**
	 * Returns start year of the query.
	 * 
	 * @return startYear start year
	 */
	public int getStartYear() {
		return startYear;
	}
	
	/**
	 * Returns end year of the query.
	 * 
	 * @return endYear end year
	 */
	public int getEndYear() {
		return endYear;
	}
	
	// TODO SET start and end year!!! possibly will require to recache
	
	/**
	 * Converts date in String type to Date type.
	 * 
	 * @return date in Date type
	 */
	public static Date convertToDate(String stringDate) {
		try {
			return new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(stringDate);
		} catch (ParseException e) {
			System.out.println("ERROR UNABLE TO CONVERT STRING TO DATE");
		}
		return null;
	}
	
	/**
	 * Returns the date of the query in Date type.
	 * 
	 * @return queryDate 	date of the query in Date type
	 */
	public Date getDateOriginal() {
		return queryDate;
	}
	
	/**
	 * Returns the date (dd.mm.yyyy hh.mm) of the query in String
	 * 
	 * @return date of the query in String type (dd.mm.yyyy hh.mm)
	 */
	public String getDateAndTime() {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(queryDate);
	}
	
	/**
	 * Returns the date (dd.mm.yyyy) of the query in String
	 * 
	 * @return date of the query in String type (dd.mm.yyyy)
	 */
	public String getDate() {
		return new SimpleDateFormat("dd.MM.yyyy").format(queryDate);
	}
	
	/**
	 * Returns the date (mm.yyyy) of the query in String
	 * 
	 * @return date of the query in String type (mm.yyyy)
	 */
	public String getMonthYear() {
		return new SimpleDateFormat("MM.yyyy").format(queryDate);
	}
	
	/**
	 * Adds pair (year and value) to map, containing all years for the query.
	 * 
	 * @param year	key - specific year 
	 * @param value	value - value that maps to the year
	 */
	public void add(int year, double value) {
		yearValue.put(year, value);
	}
	
	/**
	 * Return a map of pairs (year and value) for all the query's years
	 * 
	 * @return yearValue	map containing pairs (year and value)
	 */
	public Map<Integer, Double> getData() {
		return yearValue;
	}

	public String toString() {
		return indicatorCode + " " + countryCode + " " + startYear + " " + endYear + " " + queryDate;
	}

//	 public static void main(String[] args) {
//		 String date = "Mon Dec 12 01:22:00 GMT 2005";	 
//		 System.out.println(convertToDate(date));
//	 }

}
