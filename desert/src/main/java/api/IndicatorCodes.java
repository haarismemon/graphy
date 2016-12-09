package main.java.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Converts indicator name or indicator code to opposite format
 * and validates if such indicator exists.
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
     * Converts indicator name to indicator code.
     * e.g. indicator name "GDP" converts to indicator code "NY.GDP.MKTP.KD.ZG"
     *
     * @param indicatorName     indicator name to be converted to indicator code
     * @return                  indicator code or null if indicator not found
     */
    public static String getIndicatorCode(String indicatorName) {
        for (String name : indicatorQueryMap.keySet()) {
            if (name.toLowerCase().equals(indicatorName.toLowerCase())) 
                return indicatorQueryMap.get(name);
        }
        return null;
    }
    
    /**
     * Converts indicator code to indicator name.
     * e.g. indicator code "NY.GDP.MKTP.KD.ZG" converts to indicator name "GDP"
     *
     * @param indicatorCode     indicator code to be converted to indicator name
     * @return                  indicator name or null if indicator not found
     */
    public static String getIndicatorName(String indicatorCode) {
        for (Entry<String, String> entry : indicatorQueryMap.entrySet()) {
            if (entry.getValue().toLowerCase().equals(indicatorCode.toLowerCase())) 
                return entry.getKey().toString();
        }
        return null;
    }

    // EXAMPLE:
//  public static void main(String[] args) {
//      System.out.println(getIndicatorCode("GdP"));
//      System.out.println(getIndicatorName("NY.GdP.MkTP.kd.ZG"));
//  }

}