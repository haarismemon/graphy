

import java.util.Locale;

/**
 * Gives access to country name, code and list of valid countries.
 * 
 * @author Vladislavs Uljanovs
 */
public class Country {
	
	/**
     * A list of all 2-letter country codes defined in ISO 3166
     * 
     */
	private final static String[] countries = Locale.getISOCountries();
	
	/**
     * Returns country code.
     * 
     * @param 	countryName		country name
     * @return 	2-letter country code or null if country not found
     */
	static String getCode(String countryName) {
		for (String code : countries) {
			if (countryName.toLowerCase().equals(new Locale("en", code).getDisplayCountry().toLowerCase())) 
				return code;
		}
		return null;
    }
	
	/**
     * Returns country name.
     * 
     * @param 	countryCode 	2-letter country code
     * @return 	country name or null if country not found
     */
	static String getName(String countryCode) {
		for (String code : countries) {
			if (countryCode.toLowerCase().equals(code.toLowerCase())) 
				return new Locale("en", code).getDisplayCountry();
		}
		return null;
	}

	/**
	 * Returns a list of all the valid country names as an array.
	 *
	 * @return countryNames	list of all valid country names
	 */
	public static String[] getAllNames() {
		String[] countryNames = new String[countries.length];
		for (int countryCode = 0; countryCode < countryNames.length; ++countryCode) {
			Locale locale = new Locale("en", countries[countryCode]);
			countryNames[countryCode] = locale.getDisplayCountry();
		}
		return countryNames;
	}
}
