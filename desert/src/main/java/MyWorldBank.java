package main.java;

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

    private static String fetch(String countryCode, String indicator) throws IOException  {
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
    
    private static String fetchOnline(String countryCode, String indicator) throws IOException  {
        URL request = new URL("http://api.worldbank.org/countries/" + countryCode + "/indicators/" + indicator + "?format=json&per_page=250");
        String response;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.openStream()));
            response = reader.readLine();
            reader.close();
        } catch (IOException e) {
            return null;
        }
        return response;
    }
    
    private static String fetchOffline(String countryCode, String indicator) {
        File cache = new File("cache.txt");
        if(!cache.exists() && !cache.isDirectory()) {
            System.out.println("=> Log.fetchOffline: NO CACHED DATA");
            return null;
        }

        String rawData = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(cache))) {

            String line = null;

            Calendar c = Calendar.getInstance();
            String currentMMYYYY = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));

            while ((line = reader.readLine()) != null) {
                
                String[] values = line.split("/");

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
                System.out.println("indicator " + values[1] + " country: " + values[0]);
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

        System.out.println("new cache txt: " + cache.renameTo(new File("newCache.txt")));

        boolean isOK = temp.renameTo(new File("cache.txt"));
        System.out.println("Renamed successfully? " + isOK);
        System.out.println("=> Log.deleteQuery: QUERY REMOVED");
    }

    private static void clearCache() throws IOException {
        File file = new File("cache.txt");
        file.delete();
    }

    private static Map<Integer, Double> query(String indicator, String countryCode, int startYear, int endYear) {
        
//      isQueryValid(indicator, countryCode, startYear, endYear); // if false return error and empty map
        // HANDLE ALL WORLD

        //can be null
        String rawData = fetch(countryCode, indicator);
        
        // FOR TESTING PURPOSE ARE LEFT HERE:
//      deleteQuery(countryCode, indicator);
//      clearCache();

        Map<Integer, Double> yearValueMap = new HashMap<>();

        if(rawData != null) {

            JSONArray array = new JSONArray(rawData).getJSONArray(1);


            for (int o = 0; o < array.length(); ++o) {
                JSONObject object = array.getJSONObject(o);
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
                    if((yearKey > startYear && yearKey < endYear) || yearKey == startYear || yearKey == endYear) {
                        filteredMap.put(yearKey, yearValueMap.get(yearKey));
                    }
                } else if(startYear == 0 && endYear != 0) { //"until [end year]"
                    if(yearKey < endYear || yearKey == endYear) {
                        filteredMap.put(yearKey, yearValueMap.get(yearKey));
                    }
                } else if(startYear != 0 && endYear == 0) { //since [start year]
                    if(yearKey > startYear || yearKey == startYear) {
                        filteredMap.put(yearKey, yearValueMap.get(yearKey));
                    }
                }
            }

            yearValueMap = filteredMap;

        } else {
            System.out.println("Incorrect URL");
        }

        return yearValueMap;
    }

    /**
     * Gets the data results for Indicator GDP in US Trillion Dollars.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get data for just End year
     * Make End Year 0 to get data for just Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP value in US Dollars (Trillion)
     */
    public static Map<Integer, Double> getGDP(String countryCode, int startYear, int endYear) {
        return query("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP Growth in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP Growth Percentage(%)
     */
    public static Map<Integer, Double> getGDPGrowth(String countryCode, int startYear, int endYear) {
        return query("NY.GDP.MKTP.CD", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP per Capita in US Trillion Dollars.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get data for just End year
     * Make End Year 0 to get data for just Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP per Capita value in US Dollars (Trillion)
     */
    public static Map<Integer, Double> getGDPPerCapita(String countryCode, int startYear, int endYear) {
        return query("NY.GDP.PCAP.CD", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP per Capita Growth in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP per Capita Growth Percentage(%)
     */
    public static Map<Integer, Double> getGDPPerCapitaGrowth(String countryCode, int startYear, int endYear) {
        return query("NY.GDP.PCAP.KD.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Consumer Price Inflation in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Consumer Price Inflation Percentage(%)
     */
    public static Map<Integer, Double> getConsumerPriceInflation(String countryCode, int startYear, int endYear) {
        return query("FP.CPI.TOTL.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, total in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static Map<Integer, Double> getUnemploymentTotal(String countryCode, int startYear, int endYear) {
        return query("SL.UEM.TOTL.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Male in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static Map<Integer, Double> getUnemploymentMale(String countryCode, int startYear, int endYear) {
        return query("SL.UEM.TOTL.MA.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Young Male in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static Map<Integer, Double> getUnemploymentYoungMale(String countryCode, int startYear, int endYear) {
        return query("SL.UEM.1524.MA.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Female in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static Map<Integer, Double> getUnemploymentFemale(String countryCode, int startYear, int endYear) {
        return query("SL.UEM.TOTL.FE.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Young Feale in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static Map<Integer, Double> getUnemploymentYoungFemale(String countryCode, int startYear, int endYear) {
        return query("SL.UEM.1524.FE.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP Deflator Inflation in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP Deflator Inflation Percentage(%)
     */
    public static Map<Integer, Double> getGDPDeflatorInflation(String countryCode, int startYear, int endYear) {
        return query("NY.GDP.DEFL.KD.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Current Account Balance in US Billion Dollars.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get data for just End year
     * Make End Year 0 to get data for just Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and Current Account Balance value in US Dollars (Billion)
     */
    public static Map<Integer, Double> getCurrentAccountBalance(String countryCode, int startYear, int endYear) {
        return query("BN.CAB.XOKA.CD", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Current Account Balance in Percentage of GDP.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Current Account Balance in Percentage(%)
     */
    public static Map<Integer, Double> getCurrentAccountBalancePercentOfGDP(String countryCode, int startYear, int endYear) {
        return query("BN.CAB.XOKA.GD.ZS", countryCode, startYear, endYear);
    }

}
