
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
 * This class represents obtaining the data from the World Bank API.
 *
 * @author Haaris Memon
 * @author Vladislavs Uljanovs
 */
public class Main {

	private static String fetchOnline(String countryCode, String indicator) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String url = "http://api.worldbank.org/countries/" + countryCode + "/indicators/" + indicator + "?format=json";
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	private static String fetchOffline(String countryCode, String indicator) throws IOException {
		
		File f = new File("cache.txt");
		if(f.exists() && !f.isDirectory()) { 
		    // good move on
		} else {
			return null;
		}
		
    	String json = null;

    	try (BufferedReader reader = new BufferedReader(new FileReader("cache.txt"))) {

    		String line = null;

    		Calendar c = Calendar.getInstance();
    		String currentMMYYYY = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));

    		while ((line = reader.readLine()) != null) {
    			
            	String[] values = line.split("/");
            	
            	// BELOW MUST BE SEPARATE METHOD BECAUSE USED TO IN LOAD AND TO VALIDATE IF THE QUERY EXISTS TO DELETE IT
            	if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) {
            		if (values[2].equals(currentMMYYYY)) {
            			System.out.println("FOUND IN CACHE:");
            			System.out.println(values[3]);
            			json = values[3];
            		} else {
            			// fetch new data
            			System.out.println("DATA TOO OLD SO FETCH NEW DATA here");
//            			json = fetchOnline(countryCode, indicator);
            		}
    	        } else {
    	        	System.out.println("NOT FOUND IN CACHE");
    	        }
            }
    		reader.close();
    	} catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
        	System.out.println("ERROR in fetchOffline");
        }


    	return json;
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

	private static void cacheQuery(String countryCode, String indicator, String data) {
		
		Calendar c = Calendar.getInstance();
		
		try (PrintWriter writer = new PrintWriter(new FileWriter("cache.txt", true))) {
			writer.println(countryCode + "/" + indicator + "/" + c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + "/" + data);
		} catch (IOException e) {
			// Ignore exception
			e.printStackTrace();
		}
	}
	
	// deletes specific query
	private static void removeCachedQuery(String countryCode, String indicator) throws IOException {
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
		System.out.println(isOK);
	}
	
	private static void clearCache() throws IOException {
		File file = new File("cache.txt");
		file.delete();
	}

	private static TreeMap<Integer, Double> query(String indicator, String countryCode, int startYear, int endYear) throws IOException {

//		isQueryValid(indicator, countryCode, startYear, endYear); // if false return error and empty map

		if (startYear != 0 && endYear == 0) {
			// ONLY HAVE STARTING POINT and but the end
			// SEPARATE METHOD HERE BECAUSE THIS WILL BE USED WHEN SETTINGS ARE CHANGED IN THE INTERFACE
		} else if (startYear == 0 && endYear == 0) {
			// get all the years
		}

		String fetchOffline = fetchOffline(countryCode, indicator);
		String data = null;
		
		if (fetchOffline != null) {
			data = fetchOffline;
			System.out.println("/// local ///");
		} else {
			data = fetchOnline(countryCode, indicator);
			cacheQuery(countryCode, indicator, data);
			System.out.println("/// new ///");
		}
		// TODO getRequiredYears using map if specific year present
		
		
//		cacheQuery(countryCode, indicator, data);
//		fetchOffline(countryCode, indicator);
		
//		removeCachedQuery(countryCode, indicator);
//		clearCache();

		JSONArray array = new JSONArray(data).getJSONArray(1);

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

		return map;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getGDP("GB", 0, 0));
	}

	public static TreeMap<Integer, Double> getGDP(String countryCode, int startYear, int endYear) throws IOException {
		return query("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
	}

}
