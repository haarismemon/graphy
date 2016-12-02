
//package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
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
 * @version 3.0
 * @since 2016-12-01 16:37
 */
public class Main {

	private static OkHttpClient client = new OkHttpClient();

	private static String buildURL(String countryCode, String indicator) {
		return "http://api.worldbank.org/countries/" + countryCode + "/indicators/" + indicator + "?format=json";
	}

	private static String downloadJSON(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
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

	private static void saveData(String countryCode, String indicator, String jsonReceived) {
//		Timestamp dateAdded = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
		
		Calendar c = Calendar.getInstance();
		
		try (PrintWriter writer = new PrintWriter(new FileWriter("test.txt", true))) {
			writer.println(countryCode + "/" + indicator + "/" + c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + "/" + jsonReceived);
		} catch (IOException e) {
			// Ignore exception
			e.printStackTrace();
		}
	}

	private static String loadData(String countryCode, String indicator) throws IOException {
    	String result = null;

    	try (BufferedReader reader = new BufferedReader(new FileReader("test.txt"))) {

    		String line = null;

    		Calendar c = Calendar.getInstance();
    		String currentMMYYYY = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));
//    		System.out.println(currentMMYYYY);

    		while ((line = reader.readLine()) != null) {
            	String[] values = line.split("/");
            	
            	// BELOW MUST BE SEPARATE METHOD BECAUSE USED TO IN LOAD AND TO VALIDATE IF THE QUERY EXISTS TO DELETE IT
            	if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) {
            		if (values[2].equals(currentMMYYYY)) {
            			System.out.println("FOUND IN CACHE");
            			System.out.println(values[3]);
            		} else {
            			// fetch new data
            			System.out.println("DATA TOO OLD SO FETCH NEW DATA here");
            		}
    	        } else {
    	        	System.out.println("NOT FOUND IN CACHE");
    	        }
            }
    		reader.close();
    	} catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
        	System.out.println("ERROR in loadData");
        }


    	return result;
    }

	private static TreeMap<Integer, Double> makeQuery(String indicator, String countryCode, int startYear, int endYear)
			throws IOException {

		isQueryValid(indicator, countryCode, startYear, endYear); // if false return error and empty map

		if (startYear != 0 && endYear == 0) {
			// ONLY HAVE STARTING POINT and but the end
			// SEPARATE METHOD HERE BECAUSE THIS WILL BE USED WHEN SETTINGS ARE CHANGED IN THE INTERFACE
		} else if (startYear == 0 && endYear == 0) {
			// get all the years
		}

		// if (query exists) {
		// getJson
		// } else {
		// buildURL
		// downloadJSON
		// }
		// getRequiredYears using map if specific year present

		String urlString = buildURL(countryCode, indicator);

		String jsonReceived = null;

		try {
			jsonReceived = downloadJSON(urlString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveData(countryCode, indicator, jsonReceived);

		loadData(countryCode, indicator);

		JSONArray jsonArray = new JSONArray(jsonReceived).getJSONArray(1);

		TreeMap<Integer, Double> map = new TreeMap<>();

		for (int o = 0; o < jsonArray.length(); ++o) {
			JSONObject object = jsonArray.getJSONObject(o);
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
		return makeQuery("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
	}

}
