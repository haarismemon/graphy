package teamDesert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.TreeMap;

/**
 * This class represents obtaining the data from the World Bank API.
 *
 * @author Haaris Memon
 */
public class MyWorldBank {

    /**
     * Gets the data results for Indicator GDP in US Trillion Dollars.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get data for just End year
     * Make End Year 0 to get data for just Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP value in US Dollars (Trillion)
     */
    public static TreeMap<Integer, Double> getGDP(String countryCode, int startYear, int endYear) {
        return getQuery("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP Growth in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP Growth Percentage(%)
     */
    public static TreeMap<Integer, Double> getGDPGrowth(String countryCode, int startYear, int endYear) {
        return getQuery("NY.GDP.MKTP.CD", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP per Capita in US Trillion Dollars.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get data for just End year
     * Make End Year 0 to get data for just Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP per Capita value in US Dollars (Trillion)
     */
    public static TreeMap<Integer, Double> getGDPPerCapita(String countryCode, int startYear, int endYear) {
        return getQuery("NY.GDP.PCAP.CD", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP per Capita Growth in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP per Capita Growth Percentage(%)
     */
    public static TreeMap<Integer, Double> getGDPPerCapitaGrowth(String countryCode, int startYear, int endYear) {
        return getQuery("NY.GDP.PCAP.KD.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Consumer Price Inflation in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Consumer Price Inflation Percentage(%)
     */
    public static TreeMap<Integer, Double> getConsumerPriceInflation(String countryCode, int startYear, int endYear) {
        return getQuery("FP.CPI.TOTL.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, total in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static TreeMap<Integer, Double> getUnemploymentTotal(String countryCode, int startYear, int endYear) {
        return getQuery("SL.UEM.TOTL.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Male in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static TreeMap<Integer, Double> getUnemploymentMale(String countryCode, int startYear, int endYear) {
        return getQuery("SL.UEM.TOTL.MA.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Young Male in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static TreeMap<Integer, Double> getUnemploymentYoungMale(String countryCode, int startYear, int endYear) {
        return getQuery("SL.UEM.1524.MA.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Female in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static TreeMap<Integer, Double> getUnemploymentFemale(String countryCode, int startYear, int endYear) {
        return getQuery("SL.UEM.TOTL.FE.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Unemployment, Young Feale in Percentage
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Unemployment Percentage(%)
     */
    public static TreeMap<Integer, Double> getUnemploymentYoungFemale(String countryCode, int startYear, int endYear) {
        return getQuery("SL.UEM.1524.FE.ZS", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator GDP Deflator Inflation in Percentage.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP Deflator Inflation Percentage(%)
     */
    public static TreeMap<Integer, Double> getGDPDeflatorInflation(String countryCode, int startYear, int endYear) {
        return getQuery("NY.GDP.DEFL.KD.ZG", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Current Account Balance in US Billion Dollars.
     *
     * Make country code 'null' to get for All Countries
     * Make Start Year 0 to get data for just End year
     * Make End Year 0 to get data for just Start year
     * Make Start and End Year both 0 to get All the data from 1961 to present
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and Current Account Balance value in US Dollars (Billion)
     */
    public static TreeMap<Integer, Double> getCurrentAccountBalance(String countryCode, int startYear, int endYear) {
        return getQuery("BN.CAB.XOKA.CD", countryCode, startYear, endYear);
    }

    /**
     * Gets the data results for Indicator Current Account Balance in Percentage of GDP.
     *
     * Make country code 'null' to get for All Countries.
     * Make Start Year 0 to get data for just End year.
     * Make End Year 0 to get data for just Start year.
     * Make Start and End Year both 0 to get All the data from 1961 to present.
     *
     * @param countryCode The country you want to find data for
     * @param startYear The start year you want to find data for
     * @param endYear The end year you want to find data for
     * @return Map with year and Current Account Balance in Percentage(%)
     */
    public static TreeMap<Integer, Double> getCurrentAccountBalancePercentOfGDP(String countryCode, int startYear, int endYear) {
        return getQuery("BN.CAB.XOKA.GD.ZS", countryCode, startYear, endYear);
    }

    private static TreeMap<Integer, Double> getQuery(String indicator, String countryCode, int startYear, int endYear) {
        String urlString = "http://api.worldbank.org/countries/";
        if(countryCode != null) urlString += countryCode;
        else urlString += "all";
        urlString += "/indicators/" + indicator;
        if(endYear != 0 && startYear == 0) urlString += "?date=YTD:" + endYear;
        else if(endYear == 0 && startYear != 0 ) urlString += "?date=YTD:" + startYear;
        else if(endYear != 0 && startYear != 0) urlString += "?date=" + startYear + ":" + endYear;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(urlString).openStream());
            //saves result from url to file
            return getYearAndValue(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static TreeMap<Integer, Double> getYearAndValue(Document document) {
        TreeMap<Integer, Double> yearValueMap = new TreeMap<>();
        //gets the list of all the data
        NodeList dataList = document.getElementsByTagName("wb:data");
        for (int i = 0; i < dataList.getLength(); i++) {
            //data to for every year stored in a node
            Node d = dataList.item(i);
            if(d.getNodeType() == Node.ELEMENT_NODE) {
                Element data = (Element) d;
                //obtain the date
                String year = data.getElementsByTagName("wb:date").item(0).getTextContent();
                //obtain the price value in that year
                String val = data.getElementsByTagName("wb:value").item(0).getTextContent();

                //store year and value into the map
                try{ yearValueMap.put(Integer.parseInt(year), Double.parseDouble(val)); }
                catch(NumberFormatException e) { /*don't add to map if cannot convert number (if cannot find number) */ }
            }
        }

        return yearValueMap;
    }

}
