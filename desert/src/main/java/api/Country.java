package main.java.api;

import java.util.Locale;

/**
 * Converts country name to country code and validates if such country exists.
 * 
 * @author Vladislavs Uljanovs
 */
public class Country {
	
	// a list of all 2-letter country codes defined in ISO 3166
	private static String[] isoCountries = Locale.getISOCountries();
	
	/**
     * Converts country name to 2-letter country code defined in ISO 3166
     * 
     * @param 	countryName		country name to be converted to country code
     * @return 	countryCode 	2-letter country code (ISO 3166) or null if country not found
     */
	public static String getCountryCode(String countryName) {
		String countryCode = null;
		for (String country : isoCountries) {
			Locale locale = new Locale("en", country);
			if (countryName == locale.getDisplayCountry()) {
				countryCode = country;
				break;
			}
		}
		return countryCode;
    }

	/**
	 * Gets all the valid Country names as an Array.
	 *
	 * @return 	String array of all valid Countries.
	 */
	public static String[] getCountries() {
		String[] countryNamesArray = new String[isoCountries.length];

		for(int i = 0; i < isoCountries.length; ++i) {
			String countryCode = isoCountries[i];
			Locale locale = new Locale("en", countryCode);
			countryNamesArray[i] = locale.getDisplayCountry();
		}

		return countryNamesArray;
	}
}
