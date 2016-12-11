package test.java;

import main.java.api.Country;
import main.java.api.WorldBankAPI;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class represents the JUnit Tests for the Cache API class.
 *
 * @author Haaris Memon
 */
public class CountryTest {

    @Test
    public void countryNameIsCorrect() {
        assertEquals("Country name is United Kingdom", "United Kingdom", Country.getCountryName("gb"));
        assertEquals("Country name is Brazil", "Brazil", Country.getCountryName("br"));
        assertEquals("Country name is Australia", "Australia", Country.getCountryName("au"));
        assertEquals("Country name is Italy", "Italy", Country.getCountryName("it"));
        assertEquals("Country name is United States", "United States", Country.getCountryName("us"));
        assertEquals("Country name is Japan", "Japan", Country.getCountryName("jp"));
        assertEquals("Country name is China", "China", Country.getCountryName("cn"));
    }

    @Test
    public void countryNameIsIncorrect() {
        assertEquals("Country name should return null", null, Country.getCountryName("abc"));
        assertEquals("Country name should return null", null, Country.getCountryName("desert"));
        assertEquals("Country name should return null", null, Country.getCountryName("kcl"));
    }

    @Test
    public void countryCodeIsCorrect() {
        assertEquals("Country code is GB", "GB", Country.getCountryCode("United Kingdom"));
        assertEquals("Country code is BR", "BR", Country.getCountryCode("Brazil"));
        assertEquals("Country code is AU", "AU", Country.getCountryCode("Australia"));
        assertEquals("Country code is IT", "IT", Country.getCountryCode("Italy"));
        assertEquals("Country code is US", "US", Country.getCountryCode("United States"));
        assertEquals("Country code is JP", "JP", Country.getCountryCode("Japan"));
        assertEquals("Country code is CN", "CN", Country.getCountryCode("China"));
    }

    @Test
    public void countryCodeIsIncorrect() {
        assertEquals("Country code should return null", null, Country.getCountryCode("abc"));
        assertEquals("Country code should return null", null, Country.getCountryCode("desert"));
        assertEquals("Country code should return null", null, Country.getCountryCode("kcl"));
    }

    @Test
    public void getCountriesNotEmpty() {
        assertNotEquals("Get Countries should not be empty", new String[0], Country.getCountries());
    }

    @Test
    public void countryConvertIsCorrect() {
        assertEquals("Country code is GB", "GB", Country.convert("United Kingdom"));
        assertEquals("Country code is BR", "BR", Country.convert("Brazil"));
        assertEquals("Country code is AU", "AU", Country.convert("Australia"));
        assertEquals("Country code is IT", "IT", Country.convert("Italy"));
        assertEquals("Country name is United States", "United States", Country.convert("us"));
        assertEquals("Country name is Japan", "Japan", Country.convert("jp"));
        assertEquals("Country name is China", "China", Country.convert("cn"));
    }

    @Test
    public void countryConvertIsIncorrect() {
        assertEquals("Convert should return null", null, Country.convert("abc"));
        assertEquals("Convert should return null", null, Country.convert("desert"));
        assertEquals("Convert should return null", null, Country.convert("kcl"));
    }

}
