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
     * @param  query   query object holding data about query made by the user
     * @return unprocessed requested data in JSON string format
     */
    private static String fetch(Query query) {
        String fetchOffline = CacheAPI.fetchOffline(query);

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
     * @param  query   query object holding data about query made by the user
     * @return URL     link to request data from api.worldbank.org
     */
    private static URL buildURL(Query query) {
        URL assemledURL = null;
        String url = "http://api.worldbank.org/countries/";
        //TODO this should be part of country indicator? world == 1w
        if (query.getCountryCode() == null) url += "1W";
        else url += query.getCountryCode();
        url += "/indicators/" + query.getIndicatorCode() + "?format=json&per_page=250"; // data per page increased to insure all data is in one page
        try { 
            assemledURL = new URL(url); 
        } catch (IOException e) {
        
        }
        return assemledURL;
    }
    
    /**
     * Validates the obtained data from api.worldbank.org
     *
     * @param  response    obtained data from api.worldbank.org in JSON string format
     * @return {@code true} if the response is valid, and {@code false} otherwise.
     */
    private static boolean isResponseValid(String response) {
        //TODO more checks?
        if (!response.contains("parameter value is not valid")) return true;
        else return false;
    }
    
    /**
     * Downloads raw data from online World Bank API.
     *
     * @param   query   query object holding data about query made by the user
     * @return          unprocessed requested data in JSON string format 
     */
    private static String fetchOnline(Query query) {
        try {
            URL request = buildURL(query);
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.openStream()));
            String response = reader.readLine();
            reader.close();
            
            if (isResponseValid(response)) { // cache only valid response
                CacheAPI.cacheQuery(query, response);
            } else {
                System.out.println("=> Log.fetch: INVALID RETURNED JSON thus NOT CACHED");
            }

            return response;
        } catch (IOException e) {
            System.out.println("=> Log.fetchOnline: ERROR");
            return null;
        }
    }

    /**
     * Validates the input parameters.
     *
     * @param   query   query object holding data about query made by the user
     * @return          {@code true} if the query parameters are valid, and {@code false} otherwise.
     */
    private static Boolean isValid(Query query) {
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        int startYear = query.getStartYear();
        int endYear = query.getEndYear();

        if (((startYear < 1960 || startYear > currentYear) && query.getStartYear() != 0)
                || ((endYear < 1960 || endYear > currentYear) && endYear != 0)) {
            return false;
        } else if (query.getIndicatorCode() == null || query.getCountryCode() == null) {
            return false;
        } else if (query.getIndicatorCode().equals("") || query.getCountryCode().equals("")) {
            return false;
        }

        return true;
    }
    
    /**
     * Parses JSON string to usable data, and filters out user-requested year range.
     *
     * @param query		query object holding data about query made by the user
     * @param rawData	unprocessed requested data in JSON string format
     */
    private static void parse(Query query, String rawData) {
        JSONArray array = new JSONArray(rawData).getJSONArray(1);
        
        for (int i = 0; i < array.length(); ++i) {
            JSONObject object = array.getJSONObject(i);
            try {
                Integer year = Integer.parseInt(object.getString("date"));
                Double value = Double.parseDouble(object.getString("value"));
                query.addToYearValue(year, value);
            } catch (JSONException e) {
                // do nothing, the entry is not inserted into tree map
            }
        }
    }
    
    /**
     * Main query-process procedure: 
     * validate input, fetches data, parses and filter data and more.
     *
     * @param   query   query object holding data about query made by the user
     * @return  query   final processed version of the data to be used in the application
     * 					or null if it is not valid
     */
    private static Query process(Query query) {
        if (!isValid(query)) return null;
        String rawData = fetch(query); // can be null
        if (rawData != null) {
            parse(query, rawData);
            query.filter();
            return query;
        }
        return null;
    }

    /**
     * Returns queried data.
     *
     * @param indicatorName     indicator name to be queried e.g. GDP
     * @param countryName       country name to be queried, input null to query all world i.e. all countries
     * @param startYear         start year of the data range, input 0 to start with the most oldest possible year
     * @param endYear           end year of the data range, input 0 to end at current year
     * @return query that store all the details about the query
     */
    public static Query query(String indicatorName, String countryName, int startYear, int endYear) {
        Query query = new Query(Indicator.getCode(indicatorName), Country.getCode(countryName), startYear, endYear, new Date());
        return process(query);
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

//    public static void main(String[] args) {
//    	Query q = query("GDP", "Latvia", 2000, 2015);
//    	System.out.println(q.getData());
//    }

}