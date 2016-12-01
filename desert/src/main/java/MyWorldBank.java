package main.java;

import java.io.IOException;
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
 * @version 3.0
 * @since 2016-12-01 16:37
 */
public class MyWorldBank {

    private static OkHttpClient client = new OkHttpClient();

    private static String buildURL(String indicator, String countryCode, int startYear, int endYear) {
        String urlString = "http://api.worldbank.org/countries/";
        if(countryCode != null) urlString += countryCode;
        else urlString += "all";
        urlString += "/indicators/" + indicator + "?format=json";
        if(endYear != 0 && startYear == 0) urlString += "&date=YTD:" + endYear;
        else if(endYear == 0 && startYear != 0 ) urlString += "&date=YTD:" + startYear;
        else if(endYear != 0 && startYear != 0) urlString += "&date=" + startYear + ":" + endYear;

        return urlString;
    }

    private static String downloadJSON(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static TreeMap<Integer, Double> makeQuery(String indicator, String countryCode, int startYear, int endYear) {
        String urlString = buildURL(indicator, countryCode, startYear, endYear);

        String jsonReceived = null;

        try {
            jsonReceived = downloadJSON(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(jsonReceived).getJSONArray(1);

        TreeMap<Integer, Double> yearValueMap = new TreeMap<>();

        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject o = jsonArray.getJSONObject(i);
            try{
                Integer year = Integer.parseInt(o.getString("date"));
                Double value = Double.parseDouble(o.getString("value"));
                yearValueMap.put(year, value);
            } catch(JSONException e) {
                //do nothing, the entry is not inserted into tree map
            }
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
    public static TreeMap<Integer, Double> getGDP(String countryCode, int startYear, int endYear) {
        return makeQuery("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
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
        return makeQuery("NY.GDP.MKTP.CD", countryCode, startYear, endYear);
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
        return makeQuery("NY.GDP.PCAP.CD", countryCode, startYear, endYear);
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
        return makeQuery("NY.GDP.PCAP.KD.ZG", countryCode, startYear, endYear);
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
        return makeQuery("FP.CPI.TOTL.ZG", countryCode, startYear, endYear);
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
        return makeQuery("SL.UEM.TOTL.ZS", countryCode, startYear, endYear);
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
        return makeQuery("SL.UEM.TOTL.MA.ZS", countryCode, startYear, endYear);
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
        return makeQuery("SL.UEM.1524.MA.ZS", countryCode, startYear, endYear);
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
        return makeQuery("SL.UEM.TOTL.FE.ZS", countryCode, startYear, endYear);
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
        return makeQuery("SL.UEM.1524.FE.ZS", countryCode, startYear, endYear);
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
        return makeQuery("NY.GDP.DEFL.KD.ZG", countryCode, startYear, endYear);
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
        return makeQuery("BN.CAB.XOKA.CD", countryCode, startYear, endYear);
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
        return makeQuery("BN.CAB.XOKA.GD.ZS", countryCode, startYear, endYear);
    }

}
