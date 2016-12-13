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
	private final String indicatorCode;
	
	/**
     * Country code e.g. in the following format: "LV"
     * 
     */
	private final String countryCode;
	
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
	private final Date queryDate;
	
	/**
     * The title of the query.
     * 
     */
	private String title;
	
	/**
     * The colour of the query.
     * 
     */
	private String colour;
	
	/**
     * Map of pairs (year and value), containing all years for the query.
     * 
     */
	private Map<Integer, Double> yearValue = new HashMap<>();
	
	/**
     * Map of pairs (year and value), containing requested year range for the query.
     * 
     */
	private Map<Integer, Double> yearValueFiltered = new HashMap<>();
	
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
		this.title = getIndicatorName() + " in " + getCountryName() + " for " + startYear + " - " + endYear;
		this.colour = "RED";
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
		return Indicator.getName(indicatorCode);
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
		return Country.getName(countryCode);
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
	
	/**
	 * Sets/Updates start year of the query.
	 * 
	 */
	private void setStartYear(int startYear) {
		this.startYear = startYear;
	}
	
	/**
	 * Sets/Updates end year of the query.
	 * 
	 */
	private void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	/**
     * Filters out the user-requested year range.
     *
     * @return  filteredMap     containing only required year range list
     */
    protected void filter() {

        Map<Integer, Double> filteredMap = new HashMap<>();

        for (Integer yearKey : getRawData().keySet()) {
            //case in which both start and end year is given
            if (getStartYear() != 0 && getEndYear() != 0) {   // "between [start year] to [end year]"
                if ((yearKey >= getStartYear() && yearKey <= getEndYear() )) {
                    filteredMap.put(yearKey, getRawData().get(yearKey));
                }
            } else if (getStartYear() == 0 && getEndYear() != 0) { // "until [end year]"
                if(yearKey <= getEndYear()) {
                    filteredMap.put(yearKey, getRawData().get(yearKey));
                }
            } else if (getStartYear() != 0 && getEndYear() == 0) { // since [start year]
                if (yearKey > getStartYear() || yearKey == getStartYear()) {
                    filteredMap.put(yearKey, getRawData().get(yearKey));
                }
            } else if (getStartYear() == 0 && getEndYear() == 0) {
                filteredMap.put(yearKey, getRawData().get(yearKey));
            }
        }

        yearValueFiltered = filteredMap;
    }
	
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
	public void addToYearValue(int year, double value) {
		yearValue.put(year, value);
	}
	
	/**
	 * Return a map of pairs (year and value) for all year range
	 * 
	 * @return yearValue	map of unprocessed data containing pairs (year and value)
	 */
	private Map<Integer, Double> getRawData() {
		return yearValue;
	}
	
	/**
	 * Return a map of pairs (year and value) for requested year range
	 * 
	 * @return yearValue	map of filtered data containing pairs (year and value)
	 * 						only requested year range
	 */
	public Map<Integer, Double> getData() {
		return yearValueFiltered;
	}
	
	/**
	 * Returns indicator data unit, e.g. US Trillion Dollars ($)
	 * 
	 * @return indicator unit or null if indicator not found
	 */
	public String getUnit() {
		return Indicator.getUnit(getIndicatorName());
	}
	
	/**
	 * Returns description of the indicator.
	 * 
	 * @return indicator description
	 */
	public String getInfo() {
		return Indicator.getInfo(getIndicatorName());
	}
	
	/**
	 * Returns title of the graph for the query.
	 * 
	 * @return title title of the graph
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns colour of the line in the graph for the query.
	 * 
	 * @return colour colour of the line in the graph
	 */
	public String getColour() {
		return colour;
	}
	
	/**
	 * Sets the title of the graph for the query.
	 * 
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Sets the colour of the line in the graph for the query.
	 * 
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	/**
	 * Updates year range of the query to display.
	 * 
	 * @param startYear	start year to update
	 * @param endYear	end year to update
	 * @param title		title to set/update
	 * @param colour	colour to set/update
	 * @return the new list containing only required year range
	 */
	public Map<Integer, Double> getNewRange(int startYear, int endYear, String title, String colour) {
		setStartYear(startYear); setEndYear(endYear); setTitle(title); setColour(colour);
		filter();
		return getData();
	}
	
	/**
	 * Updates the cache to ensure the latest parameter changes are saved to cache.
	 * NB This must be called when graph is closed
	 * (also call close all graphs when closing application,
	 * each call on close graph will trigger this method)
	 * 
	 */
	public void updateCache() {
		CacheAPI.updateCache(indicatorCode, countryCode, startYear, endYear, title, colour);
	}
	
	/**
	 * Deletes this query from cache.
	 * 
	 */
	public void delete() {
		CacheAPI.deleteQuery(indicatorCode, countryCode);
	}
	
	/**
	 * FOR TESTING PURPOSE ONLY 
	 * Prints all information about the query.
	 * 
	 * @return indicatorCode, countryCode, startYear, endYear, queryDate, title, colour
	 */
	public String toString() {
		return indicatorCode + " " + countryCode + " " + startYear + " " + endYear + " " + queryDate + " " + title + " " + colour;
	}
	
	/**
	 * Compares Query objects to check if indicator and country code is the same.
	 * 
	 * @return {@code true} if query's indicator and country code is the same, and {@code false} otherwise.
	 */
	@Override
    public boolean equals(Object query) {
        return this.indicatorCode.equals(countryCode) && this.countryCode.equals(countryCode);
    }

//	 public static void main(String[] args) {
//		 String date = "Mon Dec 12 01:22:00 GMT 2005";	 
//		 System.out.println(convertToDate(date));
//		 Query q = new Query("NY.GDP.MKTP.KD.ZG", "GB", 1990, 2001, new Date());
//		 System.out.println(q); 
//	 }

}
