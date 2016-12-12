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
	
	
	final static String[] indicators = Indicator.getAllNames();
	
	//Get a complete list of all the countries
	final static String[] countries = Country.getAllNames();
	
	static String[] toIgnore = new String[]{
			"in", "between", "from", "since", "at", "-", "to", "till"
	};


    /**
     * Takes string input, and returns query information in a List.
     *
     * If no indicator found, method returns null.
     * If no country found, country will be "all".
     * If no dates found, start and end year will be 0.
     * If one date found, one of start or end year will be 0, depending on query.
     * Else bothd dates will be given.
     *
     * @param input String input from the search bar
     * @return List of Strings with query information. [Indicator Name, Country Name, Start Year, End Year]
     */
	public static List<String> isValidCommand(String input){
		String indicator = "";
		String country;
		
		if(input == null){
			
			return null;
			
		}
			
		String[] words = ignoreConjunctives(input.split(" "));

		
		indicator = getIndicator(input);
		country = getCountry(input);

		if(country == null) country = "all";
		
		int[] dates = getDates(words);
		
		if(indicator != null) {
			if(dates.length == 1) {
				if(wordsContain(words, "since")) {
				    return Arrays.asList(indicator, country, "" + dates[0], "0");

				}
				else if(wordsContain(words, "till") || wordsContain(words, "until")) {
                    return Arrays.asList(indicator, country, "0", "" + dates[0]);
				}
				else {
                    return Arrays.asList(indicator, country, "" + dates[0], "" + dates[0]);
                }
			}
            if(dates.length == 0) {
                return Arrays.asList(indicator, country, "0", "0");
            }

            return Arrays.asList(indicator, country, "" + dates[0], "" + dates[1]);
		} else {
			return null;
		}
	
	}
	
	
	private static String[] ignoreConjunctives(String[] s){
		for(String i: s){
			
			for(String j: toIgnore){
				
				if(i.equals(j)){
					
					i = " ";
					
				}
				
			}
			
		}
		
		ArrayList<String> list = new ArrayList<>();
		
		for(String i: s){
			
			if(!i.equals(" ")){
				
				list.add(i);
			}
			
		}
		
		String[] output = new String[list.size()];
		
		return list.toArray(output);
		
	}


	private static String getIndicator(String input){
		
		ArrayList<String> matches = new ArrayList<>();
		
		for(String s: indicators){
			
			if(input.toLowerCase().contains(s.toLowerCase())){
				
				matches.add(s);
				
			}
			
		}
		
		if(matches.size() == 1){
			
			return matches.get(0);
			
		}

		if(matches.size() == 0) return null;
		
		int length = matches.get(0).length();
		String longest = matches.get(0);

		
		for(int i=0; i < matches.size(); i++){
			
		    if(matches.get(i).length() > length){
		    	
		        length = matches.get(i).length();
		        longest = matches.get(i);
		        
		    }      
		} 

		
		return longest;
		
	}

	
	private static String getCountry(String input){
		
		ArrayList<String> matches = new ArrayList<>();
		
		for(String s: countries){
			
			if(input.toLowerCase().contains(s.toLowerCase())){
				
				matches.add(s);
				
			}
			
		}
		
		if(matches.size() == 0){
			
			return null;
			
		}
		
		if(matches.size() == 1){
			
			
			return matches.get(0);
			
		}
		
		int length = matches.get(0).length();
		String longest = matches.get(0);

		for(int i=0; i < matches.size(); i++){
			
		    if(matches.get(i).length() > length){
		    	
		        length = matches.get(i).length();
		        longest = matches.get(i);
		        
		    }      
		} 

		
		return longest;
		
	}
	
	private static int[] getDates(String[] words){
		
		ArrayList<Integer> dates = new ArrayList<>();
		
		for(String s: words){
			
			if(s.length() == 4){
				
				Pattern pattern = Pattern.compile("\\d{4}");
				
				Matcher matcher = pattern.matcher(s);
				
				if(matcher.find()){
					
					dates.add(Integer.parseInt(s));
					
				}
				
			}
			
		}
		
		int[] output = dates.stream().mapToInt(i -> i).toArray();
		
		return output;
	}
	
	private static boolean wordsContain(String[] words, String containWord) {
		for(String word: words) {
			if(word.toLowerCase().equals(containWord.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

// 	public static void main(String[] args) {
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

// //		String input = "gdp in brazil between 2016";
// //		ignoreConjunctives(input.split(" "));
// //		getDates(ignoreConjunctives(input.split(" ")));
// 	}
	
	
	
}