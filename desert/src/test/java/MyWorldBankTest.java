package test.java;

import main.java.api.Query;
import main.java.api.WorldBankAPI;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class represents the JUnit Tests for the World Bank Class.
 *
 * @author Haaris Memon
 */
public class MyWorldBankTest {

    // @Test
    // public void countryNameIsIncorrect() {
    //     Query query = WorldBankAPI.query("gdp", "abcd",2005,2008);
    //     assertEquals("Query should return null", null, query);
    // }

    // @Test
    // public void countryNameIsEmptyString() {
    //     Query query = WorldBankAPI.query("gdp","",2005,2008);
    //     assertEquals("Map returned should be null", null, query);
    // }

    // @Test
    // public void startDateIsInvalid() {
    //     Query query = WorldBankAPI.query("gdp","united kingdom",1920,2008);
    //     assertEquals("Map returned should be null", null, query);
    // }

    // @Test
    // public void endDateIsInvalid() {
    //     Query query = WorldBankAPI.query("gdp","united kingdom",2002,2020);
    //     assertEquals("Map returned should be null", null, query);
    // }

    // @Test
    // public void queryShouldReturnMap() {
    //     Map unemploymentMap = new HashMap<Integer, Double>();
    //     unemploymentMap.put(2000, 9.5);
    //     assertEquals("Map returned should be {2000=9.5}", unemploymentMap, WorldBankAPI.query("unemployment total","brazil", 2000, 2000).getData());

    //     Map gdpMap = new HashMap<Integer, Double>();
    //     gdpMap.put(1961, 10.275911554301);
    //     gdpMap.put(1962, 5.21605942017888);
    //     gdpMap.put(1963, 0.87467259240843);
    //     assertEquals("Map returned should be {2000=9.5}", gdpMap, WorldBankAPI.query("gdp","brazil", 0, 1963).getData());
    // }

    // @Test
    // public void checkConnectionIsCorrect() {
    //     boolean isConnected;
    //     try {
    //         final URL url = new URL("http://api.worldbank.org/countries/");
    //         final URLConnection conn = url.openConnection();
    //         conn.connect();
    //         isConnected =  true;
    //     } catch (MalformedURLException e) {
    //         throw new RuntimeException(e);
    //     } catch (IOException e) {
    //         isConnected = false;
    //     }

    //     assertEquals("The Check Connection should return" + isConnected, isConnected, WorldBankAPI.checkConnectionStatus());
    // }

}
