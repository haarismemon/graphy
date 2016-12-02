
//package main.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class represents obtaining the rawData from the World Bank API.
 *
 * @author Haaris Memon
 * @author Vladislavs Uljanovs
 */
public class Main {
	
	private static String fetch(String countryCode, String indicator) throws IOException {
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

	private static String fetchOnline(String countryCode, String indicator) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String url = "http://api.worldbank.org/countries/" + countryCode + "/indicators/" + indicator + "?format=json";
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	private static String fetchOffline(String countryCode, String indicator) throws IOException {
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

	private static TreeMap<Integer, Double> query(String indicator, String countryCode, int startYear, int endYear) throws IOException {

//		isQueryValid(indicator, countryCode, startYear, endYear); // if false return error and empty map

		String rawData = fetch(countryCode, indicator);
		
		// FOR TESTING PURPOSE ARE LEFT HERE:
//		deleteQuery(countryCode, indicator);
//		clearCache();

		JSONArray array = new JSONArray(rawData).getJSONArray(1);

		TreeMap<Integer, Double> map = new TreeMap<>();

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

		// BELOW MUST BE SEPARATE METHOD WHICH PROCESS RAW DATA getRequiredYears using map if specific years requested
		if (startYear != 0 && endYear == 0) {
			// ONLY STARTING YEAR GIVEN
			// SEPARATE METHOD HERE BECAUSE THIS WILL BE USED WHEN SETTINGS ARE CHANGED IN THE INTERFACE
		} else if (startYear == 0 && endYear == 0) {
			// get all the years 
		}

		return map;
	}

	public static void main(String[] args) throws IOException {
		getGDP("GB", 0, 0);
//		System.out.println(getGDP("GB", 0, 0));
	}

	public static TreeMap<Integer, Double> getGDP(String countryCode, int startYear, int endYear) throws IOException {
		return query("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
	}

}
