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
 * This class represents obtaining the data from the World Bank API.
 *
 * @author Haaris Memon
 * @author Vladislavs Uljanovs
 */
public class MyWorldBank {

    private static String fetch(String countryCode, String indicator)  {
        String rawData = null;
        String fetchOffline = fetchOffline(countryCode, indicator);

        if (fetchOffline != null) {
            rawData = fetchOffline;
            System.out.println("=> Log.fetch: /// FETCH OFFLINE ///");
        } else {
            rawData = fetchOnline(countryCode, indicator);
            cache(countryCode, indicator, rawData);
            System.out.println("=> Log.fetch: /// FETCH ONLINE ///");
        }
        return rawData;
    }

    private static URL urlBuilder(String countryCode, String indicator) throws IOException {
         String url = "http://api.worldbank.org/countries/";
         if(countryCode == null) {
             url += "1W";
         } else url += countryCode;
         url += "/indicators/" + indicator + "?format=json&per_page=250";

         return new URL(url);
    }
    
    private static String fetchOnline(String countryCode, String indicator) {
        try {
            URL request = urlBuilder(countryCode, indicator);
            String response;
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.openStream()));
            response = reader.readLine();
            reader.close();
            return response;
        } catch (IOException e) {
            System.out.println("error");
            return null;
        }
    }
    
    private static String fetchOffline(String countryCode, String indicator) {
        File cache = new File("cache.txt");
        if(!cache.exists() && !cache.isDirectory()) {
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

                if(countryCode == null) countryCode = "null";

                if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) {
                    if (values[2].equals(currentMMYYYY)) {
                        System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE");
                        rawData = values[3];
                    } else {
                        System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE BUT OUTDATED");
                        deleteQuery(countryCode, indicator);
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

    private static Boolean isQueryValid(String indicator, String countryCode, int startYear, int endYear) {
        if (indicator == null || countryCode == null)
            return false;
        if (startYear == 0 && endYear != 0)
            return false;
        // if (!(indicatorArray.contains(indicator))) return false;
        // if (!(countryArray.contains(countryCode))) return false;
        return true;
    }

    private static void cache(String countryCode, String indicator, String rawData) {
        Calendar c = Calendar.getInstance();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("cache.txt", true))) {
//            System.out.println(cacheSize(new File("cache.txt")));
            if(cacheSize(new File("cache.txt")) == 5) {
                //remove first line
                BufferedReader reader = new BufferedReader(new FileReader(new File("cache.txt")));
                String[] values = reader.readLine().split("/");
                reader.close();
                deleteQuery(values[0], values[1]);
            }
            writer.println(countryCode + "/" + indicator + "/" + c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + "/" + rawData);
        } catch (IOException e) {
            // Ignore exception
            e.printStackTrace();
        }
    }

    private static int cacheSize(File file) {
        try{
            FileReader fr = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fr);

            int lineNumber = 0;

            while(lnr.readLine() != null) lineNumber++;
            lnr.close();
            fr.close();

            return lineNumber;

        } catch (IOException e) {
            System.out.println("File given is not found");
            return 0;
        }
    }
    
    // deletes specific query
    private static void deleteQuery(String countryCode, String indicator) throws IOException {
        File cache = new File("cache.txt");
        File temp = new File("temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(cache));
        PrintWriter writer = new PrintWriter(new FileWriter(temp, true));

        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split("/");
            
            if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) continue;
            writer.println(line);
        }
        writer.close();
        reader.close();


        //create source as the temp file
        BufferedReader tempReader = new BufferedReader(new FileReader(temp));
        //create the destination to be the cache file
        PrintWriter cacheWriter = new PrintWriter(new FileWriter(cache));

        String tempLine = null;

        //copy everything from temp into cache txt
        while ((tempLine = tempReader.readLine()) != null) {
            cacheWriter.println(tempLine);
        }
        cacheWriter.close();
        tempReader.close();

        temp.delete();

        System.out.println("=> Log.deleteQuery: QUERY REMOVED");
    }

    private static void clearCache() throws IOException {
        File file = new File("cache.txt");
        file.delete();
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

        String indicatorCode = Indicators.getIndicator(indicator);

        //the start and end year passed must be valid i.e. between 1960 and 2016
        if(((startYear < 1960 || startYear > 2016) && startYear != 0) || ((endYear < 1960 || endYear > 2016) && endYear != 0)) return null;
        if(countryCode.equals("") || indicatorCode.equals("")) return null;

        //can be null
        String rawData = fetch(countryCode, indicatorCode);

        //if the data returned says that the parameter was not valid, then return null
        if(rawData.contains("parameter value is not valid")) return null;

        Map<Integer, Double> yearValueMap = new HashMap<>();

        if(rawData != null) {

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

            Map<Integer, Double> filteredMap = new HashMap<>();

            for(Integer yearKey : yearValueMap.keySet()) {
                //case in which both start and end year is given
                if(startYear != 0 && endYear != 0) {   //"between [start year] to [end year]"
                    if((yearKey >= startYear && yearKey <= endYear)) {
                        filteredMap.put(yearKey, yearValueMap.get(yearKey));
                    }
                } else if(startYear == 0 && endYear != 0) { //"until [end year]"
                    if(yearKey <= endYear) {
                        filteredMap.put(yearKey, yearValueMap.get(yearKey));
                    }
                } else if(startYear != 0 && endYear == 0) { //since [start year]
                    if(yearKey > startYear || yearKey == startYear) {
                        filteredMap.put(yearKey, yearValueMap.get(yearKey));
                    }
                } else if(startYear == 0 && endYear == 0) {
                    filteredMap.put(yearKey, yearValueMap.get(yearKey));
                }
            }

             yearValueMap = filteredMap;

             return yearValueMap;
        } else {
            System.out.println("Incorrect URL");
            return null;
        }
    }

}
