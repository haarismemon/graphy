package test.java;

import main.java.api.Country;
import org.junit.Test;

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
