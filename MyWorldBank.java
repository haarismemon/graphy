package teamDesert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.TreeMap;

/**
 * This class represents obtaining the data from the World Bank API.
 *
 * @author Haaris Memon
 */
public class MyWorldBank {

    /**
     * Gets the data results for Indicator GDP
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP value in US Dollars (Trillion)
     */
    public static TreeMap<Integer, Double> getGDP(String countryCode, int startYear, int endYear) {
        Document doc = getQuery("NY.GDP.MKTP.KD.ZG", countryCode, startYear, endYear);
        if(doc != null) return getYearAndValue(doc);
        else return null;
    }

    /**
     * Gets the data results for Indicator GDP Growth
     *
     * @param countryCode The country you want to find data for (null to get for All countries)
     * @param startYear The start year you want to find data for (0 to get data for just end year)
     * @param endYear The end year you want to find data for
     * @return Map with year and GDP Growth Percentage(%)
     */
    public static TreeMap<Integer, Double> getGDPGrowth(String countryCode, int startYear, int endYear) {
        Document doc = getQuery("NY.GDP.MKTP.CD", countryCode, startYear, endYear);
        if(doc != null) return getYearAndValue(doc);
        else return null;
    }

    private static Document getQuery(String indicator, String countryCode, int startYear, int endYear) {
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
            saveToFile(urlString);
            return doc;
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
                yearValueMap.put(Integer.parseInt(year), Double.parseDouble(val));
            }
        }

        return yearValueMap;
    }

    private static void saveToFile(String s) throws Exception{
        URL url = new URL(s);
        String target = "test.txt";
        Path path = Paths.get(target);

        InputStream in = url.openStream();
        Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
    }

}
