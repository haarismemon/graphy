
import java.io.IOException;
import java.util.TreeMap;

//import com.squareup.okhttp3;
//import org,json;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONObject;

/**
* Query request implementation using JSON. This is a temperate and test class, further organisation is required.
*
* @author  Vladislavs Uljanovs
* @version 1.0
* @since   2016-11-28 19:01 
*/
public class Main {
	
	/**
	* Creates okhttp object.
	*/
	private static OkHttpClient client = new OkHttpClient();
	
	/**
	* Downloads a URL and print its contents as a string.
	* 
	* @param url
	* @return contents
	* @throws IOException
	*/
	public static String downloadJSON(String url) throws IOException {
		Request request = new Request.Builder()
				.url(url)
				.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	// Start of base URL
	private static final String WORLD_BANK_URL = "http://api.worldbank.org/countries/";
	// Indicators
	private static final String GDP = "NY.GDP.MKTP.KD.ZG?";
	private static final String GDPGrowth = "NY.GDP.MKTP.CD?";
	private static final String GDPPerCapita = "NY.GDP.PCAP.CD?";
	private static final String GDPPerCapitaGrowth = "NY.GDP.PCAP.KD.ZG?";
	private static final String ConsumerPriceInflation = "FP.CPI.TOTL.ZG?";
	private static final String UnemploymentTotal = "SL.UEM.TOTL.ZS?";
	private static final String UnemploymentMale = "SL.UEM.TOTL.MA.ZS?";
	private static final String UnemploymentYoungMale = "SL.UEM.1524.MA.ZS?";
	private static final String UnemploymentFemale = "SL.UEM.TOTL.FE.ZS?";
	private static final String UnemploymentYoungFemale = "SL.UEM.1524.FE.ZS?";
	private static final String GDPDeflatorInflation = "NY.GDP.DEFL.KD.ZG?";
	private static final String CurrentAccountBalance = "BN.CAB.XOKA.CD?";
	private static final String CurrentAccountBalancePercentOfGDP = "BN.CAB.XOKA.GD.ZS?";
	
	/**
	* Creates an URL from given parameters.
	* 
	* @param countryCode
	* @param indicator
	* @param fromYear
	* @param toYear
	* @return url
	*/
	public static String buildURL(String countryCode, String indicator, int fromYear, int toYear) {
		return WORLD_BANK_URL + countryCode + "/indicators/" + indicator + "date=" + fromYear + ":" + toYear + "&format=json"; // for example: http://api.worldbank.org/countries/GB/indicators/NY.GDP.MKTP.CD?date=2010:2013&format=json
	}
	
	/**
	* Prepares json content to be processed into Java object. More specifically it removes prefix of page information.
	* 
	* @param json content in String
	* @return json content in String
	*/
	public static String adaptJSON(String json) {
		String find = "},";
		String j = json.substring(json.indexOf(find) + find.length()); // remove page information
		String j2 = j.substring(0, j.length() - 1); // remove the last char ']'
		return "{'indicators':" + j2 + "}";
	}
	
	/**
	* Makes a query: builds the URL, download content in JSON format and converts it into Java elements.
	* 
	* @param countryCode
	* @param indicator
	* @param fromYear
	* @param toYear
	* @return TreeMap of Value and Year pair
	*/
	public static TreeMap<Double, String> makeQuary(String countryCode, String indicator, int fromYear, int toYear) {
		
		String URL = buildURL(countryCode, indicator, fromYear, toYear);
		
		String jsonReceived = null;

		try {
			jsonReceived = downloadJSON(URL);
		} catch (Exception e) {
			// TODO: Handle exception
			e.printStackTrace();
		}
		
		String json = adaptJSON(jsonReceived);
		
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonArray = jsonObject.getJSONArray("indicators");
		
		TreeMap<Double, String> valueAndYear = new TreeMap<>();
		
		for (int i = 0; i < jsonArray.length(); ++i) {
		    JSONObject o = jsonArray.getJSONObject(i);
		    Double value = Double.parseDouble(o.getString("value"));
		    String year = o.getString("date");
		    valueAndYear.put(value, year);
		}
		
		return valueAndYear;
	}
	
	/**
	* Test query for GDP, this is one of the methods of the API.
	* 
	* @param countryCode
	* @param fromYear
	* @param toYear
	* @return TreeMap of Value and Year pair
	*/
	public static TreeMap<Double, String> getGDP(String countryCode, int fromYear, int toYear) {
        return makeQuary(countryCode, GDP, fromYear, toYear);
    }

	// Test
	public static void main(String[] args) {
		System.out.println(getGDP("GB", 1990, 2010));
	}

}
