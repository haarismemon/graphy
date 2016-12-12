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
    public void getCountriesNotEmpty() {
        assertNotEquals("Get Countries should not be empty", new String[0], Country.getAllNames());
    }

}
