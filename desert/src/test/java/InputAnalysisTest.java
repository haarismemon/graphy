package test.java;

import main.java.nlp.InputAnalysis;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class represents the JUnit Tests for the Cache API class.
 *
 * @author Haaris Memon
 */
public class InputAnalysisTest {

    @Test
    public void indicatorNameIsCorrect() {
        assertEquals("Indicator name is GDP", "GDP", InputAnalysis.isValidCommand("gdp in united kingdom between 2010 and 2015").get(0));
        assertEquals("Indicator name is Current Account Balance", "Current Account Balance", InputAnalysis.isValidCommand("current account balance in brazil between 2010 and 2015").get(0));
        assertEquals("Indicator name is Unemployment Male", "Unemployment Male", InputAnalysis.isValidCommand("unemployment male in brazil between 2010 and 2015").get(0));
        assertEquals("Indicator name is Consumer Price Inflation", "Unemployment Male", InputAnalysis.isValidCommand("unemployment male in brazil between 2010 and 2015").get(0));
        assertEquals("Indicator name is GDP Deflator Inflation", "GDP Deflator Inflation", InputAnalysis.isValidCommand("hello my Name is gdp deFlaTor inFlAtion").get(0));
    }

    @Test
    public void indicatorNameIsIncorrect() {
        assertEquals("Indicator name is null", null, InputAnalysis.isValidCommand("hello my name is haaris"));
        assertEquals("Indicator name is null", null, InputAnalysis.isValidCommand("King's College London"));
        assertEquals("Indicator name is null", null, InputAnalysis.isValidCommand("what is macroeconomics?"));
    }

    @Test
    public void countryNameIsCorrect() {
        assertEquals("Country name is United Kingdom", "United Kingdom", InputAnalysis.isValidCommand("gdp in united kingdom between 2010 and 2015").get(1));
        assertEquals("Country name is Brazil", "Brazil", InputAnalysis.isValidCommand("current account balance in brazil between 2010 and 2015").get(1));
        assertEquals("Country name is all (not found in input)", "all", InputAnalysis.isValidCommand("unemployment male between 2010 and 2015").get(1));
        assertEquals("Country name is Spain", "Spain", InputAnalysis.isValidCommand("unemployment male in spain between 2010 and 2015").get(1));
        assertEquals("Country name is United States", "United States", InputAnalysis.isValidCommand("gdp deFlaTor inFlAtion hello my Name is united states").get(1));
    }

    @Test
    public void countryNameIsIncorrect() {
        assertEquals("Country name is all", "all", InputAnalysis.isValidCommand("gdp in my garden").get(1));
        assertEquals("Country name is all", "all", InputAnalysis.isValidCommand("current account balance in King's College London").get(1));
        assertEquals("Country name is all", "all", InputAnalysis.isValidCommand("unemployment male las vegas").get(1));
    }

    @Test
    public void twoYearsGiven() {
        assertEquals("Start Year is 2010", "2010", InputAnalysis.isValidCommand("unemployment male in canada between 2010 and 2015").get(2));
        assertEquals("End Year is 2015", "2015", InputAnalysis.isValidCommand("unemployment male in canada between 2010 and 2015").get(3));

        assertEquals("Start Year is 2001", "2001", InputAnalysis.isValidCommand("gdp deflator inflation in brazil between 2001 and 2012").get(2));
        assertEquals("End Year is 2012", "2012", InputAnalysis.isValidCommand("gdp deflator inflation in brazil between 2001 and 2012").get(3));

        assertEquals("Start Year is 1990", "1990", InputAnalysis.isValidCommand("current account balance in brazil between 1990 and 1995").get(2));
        assertEquals("End Year is 1995", "1995", InputAnalysis.isValidCommand("current account balance in brazil between 1990 and 1995").get(3));
    }

    @Test
    public void onlyOneYearGiven() {
        assertEquals("Start Year is 2004", "2004", InputAnalysis.isValidCommand("gdp in brazil since 2004").get(2));
        assertEquals("End Year is 0", "0", InputAnalysis.isValidCommand("gdp in brazil since 2004").get(3));

        assertEquals("Start Year is 0", "0", InputAnalysis.isValidCommand("gdp growth in united kingdom till 2007").get(2));
        assertEquals("End Year is 2007", "2007", InputAnalysis.isValidCommand("gdp growth in united kingdom till 2007").get(3));

        assertEquals("Start Year is 2006", "2006", InputAnalysis.isValidCommand("gdp per capita in spain in 2006").get(2));
        assertEquals("End Year is 2006", "2006", InputAnalysis.isValidCommand("gdp per capita in spain in 2006").get(3));
    }

    @Test
    public void noYearGiven() {
        assertEquals("Start Year is 0", "0", InputAnalysis.isValidCommand("gdp per capita growth in italy").get(2));
        assertEquals("End Year is 0", "0", InputAnalysis.isValidCommand("gdp per capita growth in italy").get(3));
    }

    @Test
    public void notValidInputStatement() {
        assertEquals("Should return null", null, InputAnalysis.isValidCommand("Hello my name is Haaris"));
        assertEquals("Should return null", null, InputAnalysis.isValidCommand("What is macroeconomics?"));
        assertEquals("Should return null", null, InputAnalysis.isValidCommand("How much money did I earn in 2005?"));
    }

    // 		System.out.println(isValidCommand("gdp in brazil since 2004"));
// 		System.out.println(isValidCommand("gdp growth in united kingdom till 2007"));
// 		System.out.println(isValidCommand("gdp per capita in spain in 2006"));
// 		System.out.println(isValidCommand("gdp per capita growth in italy"));
// 		System.out.println(isValidCommand("consumer price inflation"));
// 		System.out.println(isValidCommand("Hello my name is Haaris"));
// 		System.out.println(isValidCommand("unemployment male in canada between 2010 and 2015"));
// 		System.out.println(isValidCommand("unemployment young male in brazil between 2010 and 2015"));
// 		System.out.println(isValidCommand("unemployment female in brazil between 2010 and 2015"));
// 		System.out.println(isValidCommand("unemployment young female in brazil between 2010 and 2015"));
// 		System.out.println(isValidCommand("gdp deflator inflation in brazil between 2010 and 2015"));
// 		System.out.println(isValidCommand("current account balance in brazil between 2010 and 2015"));
// 		System.out.println(isValidCommand("Current Account Balance Percent Of GDP in brazil between 2010 and 2015"));

}
