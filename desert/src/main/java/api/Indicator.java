package main.java.api;

import java.util.*;
import java.util.Map.Entry;

/**
 * Gives access to indicator name, code 
 * and list of valid indicators codes, units and descriptions.
 *
 * @author Haaris Memon
 */
public class Indicator {

	/**
	 * A list of all indicators, maps indicator name to indicator code.
	 * 
	 */
	private final static Map<String, String> indicatorsNameCode;
	static {
		indicatorsNameCode = new TreeMap<>();
		indicatorsNameCode.put("GDP", "NY.GDP.MKTP.KD.ZG");
		indicatorsNameCode.put("GDP Growth", "NY.GDP.MKTP.CD");
		indicatorsNameCode.put("GDP Per Capita", "NY.GDP.PCAP.CD");
		indicatorsNameCode.put("GDP Per Capita Growth", "NY.GDP.PCAP.KD.ZG");
		indicatorsNameCode.put("Consumer Price Inflation", "FP.CPI.TOTL.ZG");
		indicatorsNameCode.put("Unemployment Total", "SL.UEM.TOTL.ZS");
		indicatorsNameCode.put("Unemployment Male", "SL.UEM.TOTL.MA.ZS");
		indicatorsNameCode.put("Unemployment Young Male", "SL.UEM.1524.MA.ZS");
		indicatorsNameCode.put("Unemployment Female", "SL.UEM.TOTL.FE.ZS");
		indicatorsNameCode.put("Unemployment Young Female", "SL.UEM.1524.FE.ZS");
		indicatorsNameCode.put("GDP Deflator Inflation", "NY.GDP.DEFL.KD.ZG");
		indicatorsNameCode.put("Current Account Balance", "BN.CAB.XOKA.CD");
		indicatorsNameCode.put("Current Account Balance Percent Of GDP", "BN.CAB.XOKA.GD.ZS");
	}

	/**
	 * A list of all indicators, maps indicator name to indicator unit.
	 * 
	 */
	private final static Map<String, String> indicatorsNameUnit;
	static {
		indicatorsNameUnit = new TreeMap<>();
		indicatorsNameUnit.put("GDP", "US Trillion Dollars ($)");
		indicatorsNameUnit.put("GDP Growth", "Percentage (%)");
		indicatorsNameUnit.put("GDP Per Capita", "US Trillion Dollars ($)");
		indicatorsNameUnit.put("GDP Per Capita Growth", "Percentage (%)");
		indicatorsNameUnit.put("Consumer Price Inflation", "Percentage (%)");
		indicatorsNameUnit.put("Unemployment Total", "Percentage (%)");
		indicatorsNameUnit.put("Unemployment Male", "Percentage (%)");
		indicatorsNameUnit.put("Unemployment Young Male", "Percentage (%)");
		indicatorsNameUnit.put("Unemployment Female", "Percentage (%)");
		indicatorsNameUnit.put("Unemployment Young Female", "Percentage (%)");
		indicatorsNameUnit.put("GDP Deflator Inflation", "Percentage (%)");
		indicatorsNameUnit.put("Current Account Balance", "US Billion Dollars ($)");
		indicatorsNameUnit.put("Current Account Balance Percent Of GDP", "Percentage (%)");
	}

	/**
	 * A list of all indicators, maps indicator name to indicator description.
	 * 
	 */
	private final static Map<String, String> indicatorsNameInfo;
	static {
		indicatorsNameInfo = new TreeMap<>();

		indicatorsNameInfo.put("GDP",
				"(Unit 2)3.2.3\nGross domestic product (GDP) is the overall market value of all the goods/services a country produces. "
						+ "GDP is usually calculated on an Annual basis but sometimes on a quarterly basis within a year. "
						+ "This is used to determine the economic performance of a whole country or region and to make international comparisons." +
						"\nSyllabus: Unit 2 Section 3.2.3 ");

		indicatorsNameInfo.put("GDP Growth",
				"Indicates how much a country's production has increased (or decreased, if the growth rate is negative) compared to the previous year." +
						"\nSyllabus: Unit 2 Section 3.2.3");
		indicatorsNameInfo.put("GDP Per Capita",
				"Per capita GDP is a measure of the total output of a country that takes gross domestic product (GDP) and divides it by the number of people in the country. "
						+ "The per capita GDP is especially useful when comparing one country to another, because it shows the relative performance of the countries. "
						+ "A rise in per capita GDP signals growth in the economy and tends to reflect an increase in productivity." +
						"\nSyllabus: Unit 2 Section 3.2.3");

		indicatorsNameInfo.put("GDP Per Capita Growth",
				"GDP Per Capita Growth is the growth in Per Capita GDP year-on-year, often expressed a percentage increase/decrease." +
						"\nSyllabus: Unit 2 Section 3.2.3");
		indicatorsNameInfo.put("Consumer Price Inflation",
				"Inflation is a sustained increase in the cost of living or the general price level leading to a fall in the purchasing power of money. "
						+ "The rate of inflation is measured by the annual percentage change in consumer prices. The main measure of inflation is the consumer price index (CPI)." +
						"\nSyllabus: Unit 4 Section 3.4.1");

		String unemployment = "The national unemployment rate is defined as the percentage of unemployed workers in the total labor force. "
				+ "It is widely recognized as a key indicator of labor market performance. "
				+ "Unemployed workers lose their purchasing power, which can lead to unemployment for other workers, creating a cascading effect that ripples through the economy." +
				"\nSyllabus: Unit 2 Section 3.2.3";

		indicatorsNameInfo.put("Unemployment Total", unemployment);
		indicatorsNameInfo.put("Unemployment Male", unemployment);
		indicatorsNameInfo.put("Unemployment Young Male", unemployment);
		indicatorsNameInfo.put("Unemployment Female", unemployment);
		indicatorsNameInfo.put("Unemployment Young Female", unemployment);
		indicatorsNameInfo.put("GDP Deflator Inflation",
				"A measure of inflation/deflation in an economy with respect to a specific base year. The equation is GDP deflator = Nominal GDP/Real GDP x 100" +
						"\nSyllabus: Unit 2 Section 3.2.3");
		indicatorsNameInfo.put("Current Account Balance",
				"The current account balance is comprised of the balance of trade (net exports and imports) and net cash transfers, that take place over a given year." +
						"\nSyllabus: Unit 2 Section 3.2.3");
		indicatorsNameInfo.put("Current Account Balance Percent Of GDP",
				"The current account balance as a percentage of GDP" +
						"\nSyllabus: Unit 2 Section 3.2.3");

	}

	/**
	 * Returns indicator code.
	 *
	 * @param indicatorName		indicator name
	 * @return indicator code or null if indicator not found
	 */
	public static String getCode(String indicatorName) {
		for (String name : indicatorsNameCode.keySet()) {
			if (name.toLowerCase().equals(indicatorName.toLowerCase()))
				return indicatorsNameCode.get(name);
		}
		return null;
	}

	/**
	 * Returns indicator name.
	 *
	 * @param indicatorCode 	indicator code
	 * @return indicator name or null if indicator not found
	 */
	static String getName(String indicatorCode) {
		for (Entry<String, String> entry : indicatorsNameCode.entrySet()) {
			if (entry.getValue().toLowerCase().equals(indicatorCode.toLowerCase()))
				return entry.getKey().toString();
		}
		return null;
	}

	/**
	 * Returns indicator data unit, e.g. US Trillion Dollars ($)
	 *
	 * @param indicatorName		indicator name
	 * @return indicator unit or null if indicator not found
	 */
	public static String getUnit(String indicatorName) {
		for (String indicator : indicatorsNameUnit.keySet()) {
			if (indicator.toLowerCase().equals(indicatorName.toLowerCase())) {
				return indicatorsNameUnit.get(indicator);
			}
		}
		return null;
	}

	/**
	 * Returns description of the indicator.
	 * 
	 * @param indicatorName		indicator name
	 * @return indicator description
	 */
	public static String getInfo(String indicatorName) {
		for (String indicator : indicatorsNameInfo.keySet()) {
			if (indicator.toLowerCase().equals(indicatorName.toLowerCase())) {
				return indicatorsNameInfo.get(indicator);
			}
		}
		return null;
	}

	/**
	 * Returns a list of all the valid indicator names as an array.
	 *
	 * @return list of all valid indicator names
	 */
	public static String[] getAllNames() {
		Set<String> keys = indicatorsNameCode.keySet();
		String[] indicatorArray = keys.toArray(new String[keys.size()]);
		Arrays.sort(indicatorArray);
		return indicatorArray;
	}

	/**
	 * Gets all Indicator names that are similar to the input string.
	 *
	 * @param input - String that is the input by the user in the textfield.
	 * @return List of Indicator name Strings that are similar to the input.
	 */
	public static List<String> getAutocomplete(String input) {
		List<String> similar = new ArrayList<>();
		for(String string : getAllNames()) {
			if(string.toLowerCase().contains(input.toLowerCase())) {
				similar.add(string);
			}
		}
		return similar;
	}

	/**
 	 * Checks if there is an Indicator that matches the input.
 	 *
 	 * @param input - input from the searchbar to check if it is a indicator.
 	 * @return true if an indicator exists that is the same as the input.
 	 */
 	public static boolean hasIndicator(String input) {
 		for(String indicator : getAllNames()) {
 			if(indicator.toLowerCase().equals(input.toLowerCase())) {
 				return true;
 			}
 		}
 
 		return false;
 	}

}