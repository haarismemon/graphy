package test.java;

import main.java.api.IndicatorCodes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This class represents the JUnit Tests for the Indicator Codes class.
 *
 * @author Haaris Memon
 */
public class IndicatorCodesTest {

    @Test
    public void getIndicatorCodeIsCorrect() {
        assertEquals("Indicator code is NY.GDP.MKTP.KD.ZG", "NY.GDP.MKTP.KD.ZG", IndicatorCodes.getIndicatorCode("GDP"));
        assertEquals("Indicator code is NY.GDP.MKTP.CD", "NY.GDP.MKTP.CD", IndicatorCodes.getIndicatorCode("GDP Growth"));
        assertEquals("Indicator code is NY.GDP.PCAP.CD", "NY.GDP.PCAP.CD", IndicatorCodes.getIndicatorCode("GDP Per Capita"));
        assertEquals("Indicator code is NY.GDP.PCAP.KD.ZG", "NY.GDP.PCAP.KD.ZG", IndicatorCodes.getIndicatorCode("GDP Per Capita Growth"));
        assertEquals("Indicator code is FP.CPI.TOTL.ZG", "FP.CPI.TOTL.ZG", IndicatorCodes.getIndicatorCode("Consumer Price Inflation"));
        assertEquals("Indicator code is SL.UEM.TOTL.ZS", "SL.UEM.TOTL.ZS", IndicatorCodes.getIndicatorCode("Unemployment Total"));
        assertEquals("Indicator code is SL.UEM.TOTL.MA.ZS", "SL.UEM.TOTL.MA.ZS", IndicatorCodes.getIndicatorCode("Unemployment Male"));
        assertEquals("Indicator code is SL.UEM.1524.MA.ZS", "SL.UEM.1524.MA.ZS", IndicatorCodes.getIndicatorCode("Unemployment Young Male"));
        assertEquals("Indicator code is SL.UEM.TOTL.FE.ZS", "SL.UEM.TOTL.FE.ZS", IndicatorCodes.getIndicatorCode("Unemployment Female"));
        assertEquals("Indicator code is SL.UEM.1524.FE.ZS", "SL.UEM.1524.FE.ZS", IndicatorCodes.getIndicatorCode("Unemployment Young Female"));
        assertEquals("Indicator code is NY.GDP.DEFL.KD.ZG", "NY.GDP.DEFL.KD.ZG", IndicatorCodes.getIndicatorCode("GDP Deflator Inflation"));
        assertEquals("Indicator code is BN.CAB.XOKA.CD", "BN.CAB.XOKA.CD", IndicatorCodes.getIndicatorCode("Current Account Balance"));
        assertEquals("Indicator code is BN.CAB.XOKA.GD.ZS", "BN.CAB.XOKA.GD.ZS", IndicatorCodes.getIndicatorCode("Current Account Balance Percent Of GDP"));
    }

    @Test
    public void getIndicatorCodeIsIncorrect() {
        assertEquals("Indicator code is null", null, IndicatorCodes.getIndicatorCode("My name is Haaris"));
        assertEquals("Indicator code is null", null, IndicatorCodes.getIndicatorCode("King's College London"));
        assertEquals("Indicator code is null", null, IndicatorCodes.getIndicatorCode("Team Desert"));
    }

    @Test
    public void getIndicatorNameIsCorrect() {
        assertEquals("Indicator code is GDP", "GDP", IndicatorCodes.getIndicatorName("NY.GDP.MKTP.KD.ZG"));
        assertEquals("Indicator code is GDP Growth", "GDP Growth", IndicatorCodes.getIndicatorName("NY.GDP.MKTP.CD"));
        assertEquals("Indicator code is GDP Per Capita", "GDP Per Capita", IndicatorCodes.getIndicatorName("NY.GDP.PCAP.CD"));
        assertEquals("Indicator code is GDP Per Capita Growth", "GDP Per Capita Growth", IndicatorCodes.getIndicatorName("NY.GDP.PCAP.KD.ZG"));
        assertEquals("Indicator code is Consumer Price Inflation", "Consumer Price Inflation", IndicatorCodes.getIndicatorName("FP.CPI.TOTL.ZG"));
        assertEquals("Indicator code is Unemployment Total", "Unemployment Total", IndicatorCodes.getIndicatorName("SL.UEM.TOTL.ZS"));
        assertEquals("Indicator code is Unemployment Male", "Unemployment Male", IndicatorCodes.getIndicatorName("SL.UEM.TOTL.MA.ZS"));
        assertEquals("Indicator code is Unemployment Young Male", "Unemployment Young Male", IndicatorCodes.getIndicatorName("SL.UEM.1524.MA.ZS"));
        assertEquals("Indicator code is Unemployment Female", "Unemployment Female", IndicatorCodes.getIndicatorName("SL.UEM.TOTL.FE.ZS"));
        assertEquals("Indicator code is Unemployment Young Female", "Unemployment Young Female", IndicatorCodes.getIndicatorName("SL.UEM.1524.FE.ZS"));
        assertEquals("Indicator code is GDP Deflator Inflation", "GDP Deflator Inflation", IndicatorCodes.getIndicatorName("NY.GDP.DEFL.KD.ZG"));
        assertEquals("Indicator code is Current Account Balance", "Current Account Balance", IndicatorCodes.getIndicatorName("BN.CAB.XOKA.CD"));
        assertEquals("Indicator code is Current Account Balance Percent Of GDP", "Current Account Balance Percent Of GDP", IndicatorCodes.getIndicatorName("BN.CAB.XOKA.GD.ZS"));
    }

    @Test
    public void getIndicatornNameIsIncorrect() {
        assertEquals("Indicator code is null", null, IndicatorCodes.getIndicatorName("ABC.DEG.GHI.JKL.MNO"));
        assertEquals("Indicator code is null", null, IndicatorCodes.getIndicatorName("123.456.789.101.121"));
        assertEquals("Indicator code is null", null, IndicatorCodes.getIndicatorName("HI.HELLO.MY.NAME.IS"));
    }

    @Test
    public void getAllIndicatorNamesNotEmpty() {
        assertNotEquals("Get all Indicators should not be empty", new String[0], IndicatorCodes.getAllIndicatorNames());
    }

}
