package main.java;

import java.io.*;
import java.util.Calendar;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    
    private static OkHttpClient client = new OkHttpClient();
    
    private static String fetchOnline(String countryCode, String indicator)  {
        String url = "http://api.worldbank.org/countries/" + countryCode + "/indicators/" + indicator + "?format=json";
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
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
            writer.println(countryCode + "/" + indicator + "/" + c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + "/" + rawData);
        } catch (IOException e) {
            // Ignore exception
            e.printStackTrace();
        }
    }
    
    // deletes specific query
    private static void deleteQuery(String countryCode, String indicator) throws IOException {
        File cache = new File("cache.txt"); 
        File temp = new File("temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(cache));
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split("/");
            
            if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) continue;
            writer.write(line);
        }
        writer.close();
        reader.close();
        
        boolean isOK = temp.renameTo(cache);
        System.out.println("Renamed successfully? " + isOK);
        System.out.println("=> Log.deleteQuery: QUERY REMOVED");
    }

    private static void clearCache() throws IOException {
        File file = new File("cache.txt");
        file.delete();
    }

    private static TreeMap<Integer, Double> query(String indicator, String countryCode, int startYear, int endYear) {
        
//      isQueryValid(indicator, countryCode, startYear, endYear); // if false return error and empty map
        // HANDLE ALL WORLD

        //can be null
        String rawData = fetch(countryCode, indicator);
        
        // FOR TESTING PURPOSE ARE LEFT HERE:
//      deleteQuery(countryCode, indicator);
//      clearCache();

        TreeMap<Integer, Double> map = new TreeMap<>();

        if(rawData != null) {

            JSONArray array = new JSONArray(rawData).getJSONArray(1);


            for (int o = 0; o < array.length(); ++o) {
                JSONObject object = array.getJSONObject(o);
                try {
                    Integer year = Integer.parseInt(object.getString("date"));
                    Double value = Double.parseDouble(object.getString("value"));
                    map.put(year, value);
                } catch (JSONException e) {
                    // do nothing, the entry is not inserted into tree map
                }
            }

            // IF NO startYear and endYear then ...

            // BELOW MUST BE SEPARATE METHOD WHICH PROCESS RAW DATA getRequiredYears using map if specific years requested
            if (startYear != 0 && endYear == 0) {
                // ONLY STARTING YEAR GIVEN
                // SEPARATE METHOD HERE BECAUSE THIS WILL BE USED WHEN SETTINGS ARE CHANGED IN THE INTERFACE
            } else if (startYear == 0 && endYear == 0) {
                // get all the years
            }

        } else {
            System.out.println("Incorrect URL");
        }

        return map;
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
    public static TreeMap<Integer, Double> getGDP(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getGDPGrowth(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getGDPPerCapita(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getGDPPerCapitaGrowth(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getConsumerPriceInflation(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getUnemploymentTotal(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getUnemploymentMale(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getUnemploymentYoungMale(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getUnemploymentFemale(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getUnemploymentYoungFemale(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getGDPDeflatorInflation(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getCurrentAccountBalance(String countryCode, int startYear, int endYear) {
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
    public static TreeMap<Integer, Double> getCurrentAccountBalancePercentOfGDP(String countryCode, int startYear, int endYear) {
        return query("BN.CAB.XOKA.GD.ZS", countryCode, startYear, endYear);
    }

}
