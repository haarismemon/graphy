package main.java.nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.api.Indicator;
import main.java.api.Country;

/**
 * This class represents analysing the input of the search and obtaining query information.
 *
 * @author Thomas Medvedev
 * @author Haaris Memon
 */
public class InputAnalysis {
	
	//the complete list of all indicators in the applicaiton
	final static String[] indicators = Indicator.getAllNames();
	
	//the complete list of all the countries
	final static String[] countries = Country.getAllNames();

	//list of words to ignore in the input string
	static String[] toIgnore = new String[]{
			"in", "between", "from", "since", "at", "-", "to", "till"
	};


    /**
     * Takes string input, and returns the query information in a List.
	 * The query information is: [Indicator Name, Country Name, Start Year, End Year]
	 * Use of 'from' and 'since' will give the end year as 0.
	 * Use of 'till' and 'until' will give the start year as 0.
     *
     * If no indicator found, method returns null.
     * If no country found, country will be "all" - search data for world.
     * If no dates found, start and end year will be 0.
     * If one date found, one of start or end year will be 0, depending on query.
     * Else both dates will be given.
     *
     * @param input String input from the search bar to validate
     * @return List of Strings with query information. [Indicator Name, Country Name, Start Year, End Year]
     */
	public static List<String> isValidCommand(String input){
		String indicator = "";
		String country;

		//input invalid if it is null
		if(input == null) return null;

		//the words in the input that do not have the words to be ignored.
		String[] words = ignoreConjunctives(input.split(" "));

		//gets the indicator and country name from the input string
		indicator = getIndicator(input);
		country = getCountry(input);

		//set the country name to 'all' to represent the world
		if(country == null) country = "all";

		//gets the dats in the input string
		int[] dates = getDates(words);

		//if the indicator is not null, the input is valid
		if(indicator != null) {
			//if there is only one date found
			if(dates.length == 1) {
				if(wordsContain(words, "since") || wordsContain(words, "from")) {
					//if input has 'since' or 'from', then set year to start year and set end year to 0
					return Arrays.asList(indicator, country, "" + dates[0], "0");

				}
				else if(wordsContain(words, "till") || wordsContain(words, "until")) {
					//if input has 'till' or 'until', then set year to end year and set start year to 0
					return Arrays.asList(indicator, country, "0", "" + dates[0]);
				}
				else {
					//else set year to both start and end year, searching for just one year
                    return Arrays.asList(indicator, country, "" + dates[0], "" + dates[0]);
                }
			}
            else if(dates.length == 0) {
				//if there is no dates found, then search for all dates
				return Arrays.asList(indicator, country, "0", "0");
            }

            //returns a list of query information to be passed into a query object
            return Arrays.asList(indicator, country, "" + dates[0], "" + dates[1]);
		} else {
			return null;
		}
	
	}

	/**
	 * Removes all the words in the input array that needs to be ignored e.g. in, between
	 *
	 * @param s - Array of Strings that hold all the words from the input string
	 * @return Array of Strings that do not have the words to be ignored.
	 */
	private static String[] ignoreConjunctives(String[] s) {
		//loop through all the words in the input
		for(String i: s){
			//loop through each word in the list of words to ignore
			for(String j: toIgnore){
				//if a word in the input is one from the list of words to ignore, then remove word from input
				if(i.equals(j)){

					i = " ";

				}

			}

		}
		
		ArrayList<String> list = new ArrayList<>();
		
		for(String i: s){
			//adds all the none space words in the input array into a list of words
			if(!i.equals(" ")){
				list.add(i);
			}
			
		}
		
		String[] output = new String[list.size()];

		//return the resulting words after filtering out the words to ignore
		return list.toArray(output);
		
	}


	/**
	 * Extracts the Indicator name from the input string
	 *
	 * @param input - Input String from the search bar
	 * @return Indicator name that is in the input string, may return null
	 */
	private static String getIndicator(String input){
		//list of all matched Indicators found in the input
		ArrayList<String> matches = new ArrayList<>();
		
		for(String s: indicators){
			//if the input matches any of the Indicators, then add indicator to list
			if(input.toLowerCase().contains(s.toLowerCase())){
				
				matches.add(s);
				
			}
			
		}

		//if there is only one matched Indicator return it
		if(matches.size() == 1) return matches.get(0);

		//if there is no matched Indicators, then search input is invalid
		if(matches.size() == 0) return null;

		//sets the max length and the word initially to the first word's
		int length = matches.get(0).length();
		String longest = matches.get(0);

		//go through all the matched Indicators
		for(int i=0; i < matches.size(); i++){
			//if the matched Indicator length is bigger than the current max length
		    if(matches.get(i).length() > length){
		    	//update the length and word
		        length = matches.get(i).length();
		        longest = matches.get(i);
		        
		    }      
		} 

		//returns the longest matched Indicator in the string input
		return longest;
		
	}

	/**
	 * Extracts the Country name from the input string
	 *
	 * @param input - Input String from the search bar
	 * @return Country name that is in the input string, may return null
	 */
	private static String getCountry(String input){
		//list of all matched Countries found in the input
		ArrayList<String> matches = new ArrayList<>();
		
		for(String s: countries){
			//if the input matches any of the Countries, then add country to list
			if(input.toLowerCase().contains(s.toLowerCase())){
				
				matches.add(s);
				
			}
			
		}

		//if there is only one matched Country return it
		if(matches.size() == 0) return null;

		//if there is no matched Countries, then search input is invalid
		if(matches.size() == 1) return matches.get(0);

		//sets the max length and the word initially to the first word's
		int length = matches.get(0).length();
		String longest = matches.get(0);

		//go through all the matched Countries
		for(int i=0; i < matches.size(); i++){
			//if the matched Country length is bigger than the current max length
		    if(matches.get(i).length() > length){
				//update the length and word
		        length = matches.get(i).length();
		        longest = matches.get(i);
		        
		    }      
		}

		//returns the longest matched Country in the string input
		return longest;
		
	}

	/**
	 * Extracts the Dates from the input string
	 *
	 * @param words - Array of strings that hold all the words from the input string
	 * @return int array of dates from the input
	 */
	private static int[] getDates(String[] words){
		//list of all the dates found in the input
		ArrayList<Integer> dates = new ArrayList<>();
		
		for(String s: words){
			//if the length of the string is 4
			if(s.length() == 4){

				Pattern pattern = Pattern.compile("\\d{4}");
				Matcher matcher = pattern.matcher(s);

				//if the matcher finds string with 4 digits, add date to list
				if(matcher.find()){

					dates.add(Integer.parseInt(s));
					
				}
				
			}
			
		}

		//converts the Integers to int array
		int[] output = dates.stream().mapToInt(i -> i).toArray();
		
		return output;
	}

	/**
	 * Check if a word matches any word in an array of strings
	 * @param words - Array of strings that hold all the words that needs to be matched with
	 * @param containWord - String that needs to be matched with
	 * @return true if word is matched with a word in the array of strings
	 */
	private static boolean wordsContain(String[] words, String containWord) {
		for(String word: words) {
			//if the current word from the array equals to the word to match, return true
			if(word.toLowerCase().equals(containWord.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
}