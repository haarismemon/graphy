package test.java;

import main.java.api.Indicator;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * This class represents the JUnit Tests for the Indicator class.
 *
 * @author Haaris Memon
 */
public class IndicatorTest {

    @Test
    public void getAllIndicatorNamesNotEmpty() {
        assertNotEquals("Get all Indicators should not be empty", new String[0], Indicator.getAllNames());
    }

}
