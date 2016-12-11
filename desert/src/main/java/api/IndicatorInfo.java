package main.java.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the Indicator and each of their Information.
 *
 * @author Haaris Memon
 */
public class IndicatorInfo {

    private static Map<String, String> indicatorInfoMap;
    static {
        indicatorInfoMap = new HashMap<>();

        indicatorInfoMap.put("GDP", "Gross domestic product (GDP) is the overall market value of all the goods/services a country produces. " +
                "GDP is usually calculated on an Annual basis but sometimes on a quarterly basis within a year. " +
                "This is used to determine the economic performance of a whole country or region and to make international comparisons.");

        indicatorInfoMap.put("GDP Growth", "Indicates how much a country's production has increased (or decreased, if the growth rate is negative) compared to the previous year.");
        indicatorInfoMap.put("GDP Per Capita", "Per capita GDP is a measure of the total output of a country that takes gross domestic product (GDP) and divides it by the number of people in the country. " +
                "The per capita GDP is especially useful when comparing one country to another, because it shows the relative performance of the countries. " +
                "A rise in per capita GDP signals growth in the economy and tends to reflect an increase in productivity.");

        indicatorInfoMap.put("GDP Per Capita Growth", "GDP Per Capita Growth is the growth in Per Capita GDP year-on-year, often expressed a percentage increase/decrease.");
        indicatorInfoMap.put("Consumer Price Inflation", "Inflation is a sustained increase in the cost of living or the general price level leading to a fall in the purchasing power of money. " +
                "The rate of inflation is measured by the annual percentage change in consumer prices. The main measure of inflation is the consumer price index (CPI).");

        String unemployment = "The national unemployment rate is defined as the percentage of unemployed workers in the total labor force. " +
                "It is widely recognized as a key indicator of labor market performance. " + "Unemployed workers lose their purchasing power, which can lead to unemployment for other workers, creating a cascading effect that ripples through the economy.";

        indicatorInfoMap.put("Unemployment Total", unemployment);
        indicatorInfoMap.put("Unemployment Male", unemployment);
        indicatorInfoMap.put("Unemployment Young Male", unemployment);
        indicatorInfoMap.put("Unemployment Female", unemployment);
        indicatorInfoMap.put("Unemployment Young Female", unemployment);
        indicatorInfoMap.put("GDP Deflator Inflation", "A measure of inflation/deflation in an economy with respect to a specific base year. The equation is GDP deflator = Nominal GDP/Real GDP x 100");
        indicatorInfoMap.put("Current Account Balance", "The current account balance is comprised of the balance of trade (net exports and imports) and net cash transfers, that take place over a given year.");
        indicatorInfoMap.put("Current Account Balance Percent Of GDP", "The current account balance as a percentage of GDP");
    }

    /**
     * Takes Indicator in english words and gives Information of the Indicator.
     *
     * @param indicatorName - The Indicator name in english words from the search
     * @return The Information about the Indicator.
     */
    public static String getInfo(String indicatorName) {
        for(String indicator : indicatorInfoMap.keySet()) {
            if(indicator.toLowerCase().equals(indicatorName.toLowerCase())) {
                return indicatorInfoMap.get(indicator);
            }
        }

        return null;
    }

}
