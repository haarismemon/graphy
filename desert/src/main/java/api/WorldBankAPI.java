package main.java.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Serves as a facade to WorldBank API.
 * 
 * @author Vladislavs Uljanovs
 * @author Haaris Memon
 */
public class WorldBankAPI {
    
    /**
     * Makes a decision whether to download the data from online or load from cache.
     * i.e. if cache unable to provide the requested data then download data from online.
     *
     * @param  query	object holding data details query made by the user
     * @return query 	object holding data with unprocessed year range
     */
    private static Query fetch(Query query) {
    	Query fetchOffline = CacheAPI.fetchOffline(query);

        if (fetchOffline != null) {
            System.out.println("=> Log.fetch: /// FETCH OFFLINE ///");
            return fetchOffline;
        }
        
        System.out.println("=> Log.fetch: /// FETCH ONLINE ///");
        return fetchOnline(query);
    }

    /**
     * Builds URL which is used to request data from api.worldbank.org
     *
     * @param  query   query object holding details about query made by the user
     * @return URL     link to request data from api.worldbank.org
     */
    private static URL buildURL(Query query) {
        URL assembledURL = null;
        String url = "http://api.worldbank.org/countries/";
        if (query.getCountryCode() == null) url += "1W";
        else url += query.getCountryCode();
        url += "/indicators/" + query.getIndicatorCode() + "?format=json&per_page=250"; // data per page increased to insure all data is in one page
        try { 
            assembledURL = new URL(url);
        } catch (IOException e) {
        	System.out.println("=> Log.buildURL: " + e);
        }
        return assembledURL;
    }
    
    /**
     * Downloads raw data from online World Bank API.
     *
     * @param  query object holding details about query made by the user
     * @return query object holding data with unprocessed year range
     */
    private static Query fetchOnline(Query query) {
        try {
            URL request = buildURL(query);
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.openStream()));
            String response = reader.readLine();
            reader.close();
            
            if (isResponseValid(response)) { // cache only valid responses
            	Query q = parseJsonToMap(query, response);
            	CacheAPI.addQuery(query);
            	return q;
            }
        } catch (IOException e) {
            System.out.println("=> Log.fetchOnline: " + e);
            return null;
        }
        return null;
    }
    
    /**
     * Validates the obtained data from api.worldbank.org
     *
     * @param  response obtained data from api.worldbank.org in JSON string format
     * @return {@code true} if the response is valid, and {@code false} otherwise.
     */
    private static boolean isResponseValid(String response) {

        if (!response.contains("parameter value is not valid")) {
        	return true;
        } else {
        	System.out.println("=> Log.isResponseValid: INVALID RETURNED JSON thus NOT CACHED");
        	return false;
        }
    }

    /**
     * Validates the input parameters.
     *
     * @param  query object holding details about query made by the user
     * @return {@code true} if the query parameters are valid, and {@code false} otherwise.
     */
    private static Boolean isValid(Query query) {
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int startYear = query.getStartYear();
        int endYear = query.getEndYear();
        if (((startYear < 1960 || startYear > currentYear) && query.getStartYear() != 0)
                || ((endYear < 1960 || endYear > currentYear) && endYear != 0)) {
            //if the start or end year is out of range and is not 0, then query is invalid
            System.out.println("=> Log.isValid: INVALID YEAR RANGE");
            return false;
        } else if (query.getIndicatorCode() == null || query.getCountryCode() == null) {
            //if the indicator or country code is invalid, then query is invalid
        	System.out.println("=> Log.isValid: NO INDICATOR OR COUNTRY PRESENT AS SET TO NULL");
            return false;
        } else if (query.getIndicatorCode().equals("") || query.getCountryCode().equals("")) {
            //if the indicator or country code is empty string, then query is invalid
        	System.out.println("=> Log.isValid: NO INDICATOR OR COUNTRY PRESENT AS FIELDS ARE EMPTY");
            return false;
        }
        return true;
    }
    
    /**
     * Parses JSON String to query's map (yearValue) of not filtered data
     * i.e. all years for this (indicator and country)
     *
     * @param  query object holding details about query made by the user
     * @return query object holding data with unprocessed year range
     */
    private static Query parseJsonToMap(Query query, String rawData) {
        JSONArray array = new JSONArray(rawData).getJSONArray(1);
        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            Integer year = null;
            try {
                //convert year and value, add to map
                year = Integer.parseInt(object.getString("date"));
                Double value = Double.parseDouble(object.getString("value"));
                query.addYearValue(year, value);
            } catch (JSONException e) {
                //if the value for the year is invalid, the year is added to list of invalid years
            	query.addInvalidYear(year);
            }
        }
        return query;
    }
    
    /**
     * Main query-process procedure: 
     * validate input, fetches data and filter required year range.
     *
     * @param  query object holding details about query made by the user
     * @return query object holding data with unprocessed year range
     */
    private static Query process(Query query) {
        if (!isValid(query)) return null;
        Query q = fetch(query);
        if (q != null) q.filter();
        return q;
    }

    /**
     * Returns queried query with processed data.
     *
     * @param indicatorName     indicator name to be queried e.g. GDP
     * @param countryName       country name to be queried, input null to query all world i.e. all countries
     * @param startYear         start year of the data range, input 0 to start with the most oldest possible year
     * @param endYear           end year of the data range, input 0 to end at current year
     * @return query that store all the details about the query (including data, processed and unprocessed)
     */
    public static Query query(String indicatorName, String countryName, int startYear, int endYear) {
        Query query = new Query(Indicator.getCode(indicatorName), Country.getCode(countryName), startYear, endYear, new Date());
        
        Query q = process(query);
        return q;
    }

    /**
     * Checks Internet and api.worldbank.org connection.
     * 
     * @return {@code true} if connection exists, and {@code false} otherwise.
     */
    public static boolean connectionPresent() {
        try {
            final URL url = new URL("http://api.worldbank.org/countries/");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

}