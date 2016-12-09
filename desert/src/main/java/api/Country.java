package main.java.api;

import java.util.Locale;

/**
 * Converts country name or country code to opposite format
 * and validates if such country exists.
 * 
 * @author Vladislavs Uljanovs
 */
public class Country {
	
	// a list of all 2-letter country codes defined in ISO 3166
	private static String[] countries = Locale.getISOCountries();
	
	/**
     * Converts country name to 2-letter country code defined in ISO 3166
     * e.g. country name "Latvia" converts to country code "LV"
     * 
     * @param 	countryName		country name to be converted to country code
     * @return 				 	2-letter country code or null if country not found
     */
	public static String getCountryCode(String countryName) {
		for (String code : countries) {
			if (countryName.toLowerCase().equals(new Locale("en", code).getDisplayCountry().toLowerCase())) 
				return code;
		}
		return null;
    }
	
	/**
     * Converts 2-letter country code defined in ISO 3166 to country name
     * e.g. country code "LV" converts to country name "Latvia"
     * 
     * @param 	countryCode 	2-letter country code to be converted to country name
     * @return 					country name or null if country not found
     */
	public static String getCountryName(String countryCode) {
		for (String code : countries) {
			if (countryCode.toLowerCase().equals(code.toLowerCase())) 
				return new Locale("en", code).getDisplayCountry();
		}
		return null;
	}

	/**
	 * TODO TEMP JAVADOCS:
     * 2 in 1 method, this method can replace the getCountryName and getCountryCode.
     * Simply, input country code OR country name to convert to opposite.
     * This eliminates the code duplication.
     * 
     * @param 	country		country code OR country name to be converted to opposite	
     * @return 				country code OR country name depending what was inputed
     */
	public static String convert(String country) {
		for (String code : countries) {
			Locale locale = new Locale("en", code);
			if (country.length() == 2) {
				if (country.toLowerCase().equals(code.toLowerCase())) 
					return locale.getDisplayCountry();
			} else {
				if (country.toLowerCase().equals(locale.getDisplayCountry().toLowerCase())) 
					return code;
			}
		}
		return null;
	}

	/**
	 * Returns a list of all the valid country names as an array.
	 *
	 * @return countries	list of all valid countries
	 */
	public static String[] getCountries() {
		String[] countryList = new String[countries.length];
		for (int countryCode = 0; countryCode < countryList.length; ++countryCode) {
			Locale locale = new Locale("en", countries[countryCode]);
			countryList[countryCode] = locale.getDisplayCountry();
		}
		return countryList;
	}
	
	// EXAMPLE:
//	public static void main(String[] args) {
//		System.out.println(getCountryCode("LaTvia"));
//		System.out.println(getCountryName("lV"));
//		System.out.println(convert("LatVia"));
//		System.out.println(convert("Lv"));		
//	}
	
}
