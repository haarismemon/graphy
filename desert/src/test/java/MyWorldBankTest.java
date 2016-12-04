package test.java;

import main.java.MyWorldBank;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * This class represents the JUnit Tests for the World Bank Class.
 *
 * @author Haaris Memon
 */
public class MyWorldBankTest {

    @Test
    public void countryNameIsIncorrect() {
        Map<Integer, Double> map = MyWorldBank.getGDP("abcd",2005,2008);
        assertEquals("Map returned should be null", null, map);
    }

    @Test
    public void countryNameIsEmptyString() {
        Map<Integer, Double> map = MyWorldBank.getGDP("",2005,2008);
        assertEquals("Map returned should be null", null, map);
    }

    @Test
    public void startDateIsInvalid() {
        Map<Integer, Double> map = MyWorldBank.getGDP("gb",1920,2008);
        assertEquals("Map returned should be null", null, map);
    }

    @Test
    public void endDateIsInvalid() {
        Map<Integer, Double> map = MyWorldBank.getGDP("gb",2002,2020);
        assertEquals("Map returned should be null", null, map);
    }

    @Test
    public void queryShouldReturnMap() {
        Map unemploymentMap = new HashMap<Integer, Double>();
        unemploymentMap.put(2000, 9.5);
        assertEquals("Map returned should be {2000=9.5}", unemploymentMap, MyWorldBank.getUnemploymentTotal("br", 2000, 2000));

        Map gdpMap = new HashMap<Integer, Double>();
        gdpMap.put(1961, 10.275911554301);
        gdpMap.put(1962, 5.21605942017888);
        gdpMap.put(1963, 0.87467259240843);
        assertEquals("Map returned should be {2000=9.5}", gdpMap, MyWorldBank.getGDP("br", 0, 1963));
    }

}
