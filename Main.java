package teamDesert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class represents the running of the basic application which queries the API.
 *
 * @author Haaris Memon
 */
public class Main {

    public static void main(String[] args) throws IOException {
        TreeMap<Integer, Double> yearValueMap = new TreeMap<>();

        //GDP in UK between 1971 and 2002
        String urlString = "http://api.worldbank.org/countries/br/indicators/NY.GDP.MKTP.CD?end=2002&start=1971";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new URL(urlString).openStream());
            //gets the list of all the data
            NodeList dataList = doc.getElementsByTagName("wb:data");
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
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*
		 *
		 * https://datahelpdesk.worldbank.org/knowledgebase/articles/898581-api-basic-call-structure
		 *
		 * Page detailing how to build queries using both techniques.
		 *
		 */

        try {
            saveToFile(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void saveToFile(String s) throws Exception{

        URL url = new URL(s);
        String target = "test.txt";
        Path path = Paths.get(target);

        InputStream in = url.openStream();
        Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);

    }

}
