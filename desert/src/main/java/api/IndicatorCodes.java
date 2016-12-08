package main.java.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the Indicator names and its query.
 *
 * @author Haaris Memon
 */
public class IndicatorCodes {

    private static Map<String, String> indicatorQueryMap;
    static {
        indicatorQueryMap = new HashMap<>();
        indicatorQueryMap.put("GDP", "NY.GDP.MKTP.KD.ZG");
        indicatorQueryMap.put("GDP Growth", "NY.GDP.MKTP.CD");
        indicatorQueryMap.put("GDP Per Capita", "NY.GDP.PCAP.CD");
        indicatorQueryMap.put("GDP Per Capita Growth", "NY.GDP.PCAP.KD.ZG");
        indicatorQueryMap.put("Consumer Price Inflation", "FP.CPI.TOTL.ZG");
        indicatorQueryMap.put("Unemployment Total", "SL.UEM.TOTL.ZS");
        indicatorQueryMap.put("Unemployment Male", "SL.UEM.TOTL.MA.ZS");
        indicatorQueryMap.put("Unemployment Young Male", "SL.UEM.1524.MA.ZS");
        indicatorQueryMap.put("Unemployment Female", "SL.UEM.TOTL.FE.ZS");
        indicatorQueryMap.put("Unemployment Young Female", "SL.UEM.1524.FE.ZS");
        indicatorQueryMap.put("GDP Deflator Inflation", "NY.GDP.DEFL.KD.ZG");
        indicatorQueryMap.put("Current Account Balance", "BN.CAB.XOKA.CD");
        indicatorQueryMap.put("Current Account Balance Percent Of GDP", "BN.CAB.XOKA.GD.ZS");
    }

    /**
     * Takes Indicator in english words and gives the Indicator code.
     * For example "gdp" is passed in and "NY.GDP.MKTP.KD.ZG" is returned.
     *
     * @param searchIndicator - The Indicator in english words from the search
     * @return The Indicator Code used inside the query method.
     */
    public static String getIndicatorCode(String searchIndicator) {
        for(String indicator : indicatorQueryMap.keySet()) {
            if(indicator.toLowerCase().equals(searchIndicator.toLowerCase())) {
                return indicatorQueryMap.get(indicator);
            }
        }

        return null;
    }

}
