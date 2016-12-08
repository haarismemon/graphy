package main.java.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the Indicator names and each of their units.
 *
 * @author Haaris Memon
 */
public class IndicatorUnits {

    private static Map<String, String> indicatorUnitsMap;
    static {
        indicatorUnitsMap = new HashMap<>();
        indicatorUnitsMap.put("GDP", "US Trillion Dollars ($)");
        indicatorUnitsMap.put("GDP Growth", "Percentage (%)");
        indicatorUnitsMap.put("GDP Per Capita", "US Trillion Dollars ($)");
        indicatorUnitsMap.put("GDP Per Capita Growth", "Percentage (%)");
        indicatorUnitsMap.put("Consumer Price Inflation", "Percentage (%)");
        indicatorUnitsMap.put("Unemployment Total", "Percentage (%)");
        indicatorUnitsMap.put("Unemployment Male", "Percentage (%)");
        indicatorUnitsMap.put("Unemployment Young Male", "Percentage (%)");
        indicatorUnitsMap.put("Unemployment Female", "Percentage (%)");
        indicatorUnitsMap.put("Unemployment Young Female", "Percentage (%)");
        indicatorUnitsMap.put("GDP Deflator Inflation", "Percentage (%)");
        indicatorUnitsMap.put("Current Account Balance", "US Billion Dollars ($)");
        indicatorUnitsMap.put("Current Account Balance Percent Of GDP", "Percentage (%)");
    }

    /**
     * Takes Indicator in english words and gives Units that the data from the Indicator is measured in.
     * For example "gdp" is passed in and "US Trillion Dollars ($)" is returned.
     *
     * @param searchIndicator - The Indicator in english words from the search
     * @return The Units that the data from the Indicator is measured in.
     */
    public static String getUnit(String searchIndicator) {
        for(String indicator : indicatorUnitsMap.keySet()) {
            if(indicator.toLowerCase().equals(searchIndicator.toLowerCase())) {
                return indicatorUnitsMap.get(indicator);
            }
        }

        return null;
    }

}
