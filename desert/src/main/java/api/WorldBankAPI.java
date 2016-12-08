package main.java.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents getting from the World Bank API and data being cached by user.
 *
 * @author Haaris Memon
 * @author Vladislavs Uljanovs
 */
public class MyWorldBank {

    /**
     * Makes a decision whether to download the data from online or load from cache.
     * i.e. if cache unable to provide the requested data then download data from online.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     * @return rawData unprocessed requested data in JSON string format
     */
    private static String fetch(String indicator, String countryCode)  {
        String rawData = null;
        String fetchOffline = fetchOffline(indicator, countryCode);

        if (fetchOffline != null) {
            rawData = fetchOffline;
            System.out.println("=> Log.fetch: /// FETCH OFFLINE ///");
        } else {
            rawData = fetchOnline(indicator, countryCode);
            System.out.println("=> Log.fetch: /// FETCH ONLINE ///");
        }
        return rawData;
    }

    /**
     * Builds URL which is used to request data from World Bank API.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     * @return URL to request data from World Bank API
     */
    private static URL buildURL(String indicator, String countryCode) throws IOException {
         String url = "http://api.worldbank.org/countries/";
         if (countryCode == null) url += "1W";
         else url += countryCode;

         url += "/indicators/" + indicator
                 + "?format=json&per_page=250"; // data per page increased to insure all data is in one page

         return new URL(url);
    }
    
    /**
     * Downloads raw data from online World Bank API.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     * @return rawData unprocessed requested data in JSON string format
     */
    private static String fetchOnline(String indicator, String countryCode) {
        try {
            URL request = buildURL(indicator, countryCode);
            String response;
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.openStream()));
            response = reader.readLine();
            reader.close();

            if (!response.contains("parameter value is not valid")) { // do not cache invalid data
                cache(indicator, countryCode, response);
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
     * Loads raw data from cache.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     * @return rawData unprocessed requested data in JSON string format, OR null if:
     *          - data stored is outdated i.e it is not cached in the same month
     *          - required data is not in cache
     */
    private static String fetchOffline(String indicator, String countryCode) {
        File cache = new File("cache.txt");
        if (!cache.exists() && !cache.isDirectory()) { // file does not exist i.e. no cached data
            System.out.println("=> Log.fetchOffline: NO CACHED DATA");
            return null;
        }

        String rawData = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(cache));

            String line = null;

            Calendar c = Calendar.getInstance();
            String currentMMYYYY = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));

            while ((line = reader.readLine()) != null) {
                
                String[] values = line.split("/");

                // countryCode AND indicator FOUND
                if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) {
                    // must be cached in the same month to be valid
                    if (values[2].equals(currentMMYYYY)) {
                        System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE");
                        rawData = values[3];
                    } else {
                        System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE BUT OUTDATED");
                        deleteQuery(indicator, countryCode); // delete outdated data from cache file
                    }
                    break;
                } else {
                    System.out.println("=> Log.fetchOffline: DATA NOT FOUND IN CACHE");
                }
            }
            reader.close();
        } catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("=> Log.fetchOffline: ERROR");
        }
        
        return rawData;
    }

    /**
     * Saves the query to file.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     */
    private static void cache(String indicator, String countryCode, String rawData) {
        Calendar c = Calendar.getInstance();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("cache.txt", true))) {
            if (cacheSize(new File("cache.txt")) == 30) { // limit of 30 queries to be store in the cache file
                BufferedReader reader = new BufferedReader(new FileReader(new File("cache.txt")));
                String[] values = reader.readLine().split("/");
                reader.close();
                deleteQuery(values[0], values[1]); // delete the oldest query
            }
            writer.println(countryCode + "/" + indicator + "/" + c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + "/" + rawData);
            System.out.println("=> Log.cache: DATA CACHED");
        } catch (IOException e) {
            // Ignore exception
            System.out.println("=> Log.cache: ERROR");
        }
    }

    /**
     * Finds the number of queries stored in the cache file.
     *
     * @param file the text file that stores the queries
     * @return number of queries in the file
     */
    private static int cacheSize(File file) {
        try {
            FileReader fr = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fr);

            int size = 0;

            while(lnr.readLine() != null) size++;
            lnr.close();
            fr.close();

            return size;
        } catch (IOException e) {
            System.out.println("=> Log.cacheSize: ERROR");
            return 0;
        }
    }

    /**
     * Deletes (one) specified query from the cache file.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     */
    private static void deleteQuery(String indicator, String countryCode) throws IOException {
        File cache = new File("cache.txt");
        File temp = new File("temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(cache));
        PrintWriter writer = new PrintWriter(new FileWriter(temp, true));

        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split("/");
            // countryCode AND indicator FOUND
            if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) continue;
            writer.println(line);
        }
        writer.close();
        reader.close();

        // create source as the temp file
        BufferedReader tempReader = new BufferedReader(new FileReader(temp));
        // create the destination to be the cache file
        PrintWriter cacheWriter = new PrintWriter(new FileWriter(cache));

        String tempLine = null;

        // copy everything from temp into cache text file
        while ((tempLine = tempReader.readLine()) != null) {
            cacheWriter.println(tempLine);
        }
        cacheWriter.close();
        tempReader.close();

        temp.delete();

        System.out.println("=> Log.deleteQuery: QUERY REMOVED");
    }

    /**
     * Deletes all queries from the cache file i.e. deletes file.
     */
    private static void clearCache() throws IOException {
        File file = new File("cache.txt");
        file.delete();
    }

    /**
     * Validates the input parameters.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     * @param startYear the start of the year range to output
     * @param endYear the end of the year range to output
     * @return {@code true} if the query parameters are valid, and {@code false} otherwise.
     */
    private static Boolean isValid(String indicator, String countryCode, int startYear, int endYear) {
        Calendar c = Calendar.getInstance();
        int currentYYYY = c.get(Calendar.YEAR);

        if (((startYear < 1960 || startYear > currentYYYY) && startYear != 0)
                || ((endYear < 1960 || endYear > currentYYYY) && endYear != 0)) {
            return false;
        } else if (countryCode.equals("") || indicatorCode.equals("")) {
            return false;
        } else if (indicator == null || countryCode == null) {
            return false;
        }

        // if (!(indicatorArray.contains(indicator))) return false;
        // if (!(countryArray.contains(countryCode))) return false;

        return true;
    }

    /**
     * Filters out the required year range.
     *
     * @param yearValueMap processed data containing all the years
     * @param startYear the start of the year range to output
     * @param endYear the end of the year range to output
     * @return map containing only required year range
     */
    private static Map<Integer, Double> filter(Map<Integer, Double> yearValueMap, int startYear, int endYear) {

        Map<Integer, Double> filteredMap = new HashMap<>();

        for (Integer yearKey : yearValueMap.keySet()) {
            //case in which both start and end year is given
            if (startYear != 0 && endYear != 0) {   // "between [start year] to [end year]"
                if ((yearKey >= startYear && yearKey <= endYear)) {
                    filteredMap.put(yearKey, yearValueMap.get(yearKey));
                }
            } else if (startYear == 0 && endYear != 0) { // "until [end year]"
                if(yearKey <= endYear) {
                    filteredMap.put(yearKey, yearValueMap.get(yearKey));
                }
            } else if (startYear != 0 && endYear == 0) { // since [start year]
                if (yearKey > startYear || yearKey == startYear) {
                    filteredMap.put(yearKey, yearValueMap.get(yearKey));
                }
            } else if (startYear == 0 && endYear == 0) {
                filteredMap.put(yearKey, yearValueMap.get(yearKey));
            }
        }

        return filteredMap;
    }

    /**
     * Gets the data results for Indicator Name passed in.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get all data till the End year
     * Make End Year 0 to get all data from the Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param indicator The Economic Indicator Name in English words e.g. "Consumer Price Inflation"
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP value in US Dollars (Trillion)
     */
    public static Map<Integer, Double> query(String indicator, String countryCode, int startYear, int endYear) {

        if (!isValid(indicator, countryCode, startYear, endYear)) {
            return null;
        }

        String rawData = fetch(indicator, countryCode); // can be null

        //if the data returned says that the parameter was not valid, then return null
        if(rawData.contains("parameter value is not valid")) return null;

        Map<Integer, Double> yearValueMap = new HashMap<>();

        if (rawData != null) {

            JSONArray array = new JSONArray(rawData).getJSONArray(1);

            for (int i = 0; i < array.length(); ++i) {
                JSONObject object = array.getJSONObject(i);
                try {
                    Integer year = Integer.parseInt(object.getString("date"));
                    Double value = Double.parseDouble(object.getString("value"));
                    yearValueMap.put(year, value);
                } catch (JSONException e) {
                    // do nothing, the entry is not inserted into tree map
                }
            }

            return filter(yearValueMap, startYear, endYear);

        } else {
            System.out.println("Incorrect URL");
            return null;
        }
    }

}
