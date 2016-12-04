package test.java;

import main.java.MyWorldBank;
import org.junit.Test;

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

}
