package main.java.nlp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import main.java.nlp.QueryData;
import main.java.api.Country;

public class InputAnalysis {

	static String[] indicators = new String[] { "GDP", "GDP per capita", "Consumer Price Indices",
			"Retail Price Indices", "Unemployment", "Inflation", "Deflation", "Investment",
			"Production Possibility curve", "Aggregate Demand", "Aggregate Supply", "Current account balance" };

	// Get a complete list of all the countries
	final static String[] countries = Country.getCountries();

	static String[] toIgnore = new String[] { "in", "between", "from", "since", "at", "-", "to" };

	public static QueryData isValidCommand(String input) {
		String indicator = "";
		
		String country;
		
		if (input == null) {
			return null;
		}

		String[] words = ignoreConjunctives(input.split(" "));

		indicator = getIndicator(input);
		country = getCountry(input);

		int[] dates = getDates(words);

		if (indicator != null && country != null) {

			return new QueryData(indicator, country, dates);

		} else {

			return null;

		}

	}

	private static String[] ignoreConjunctives(String[] s) {

		for (String i : s) {

			for (String j : toIgnore) {

				if (i.equals(j)) {

					i = " ";

				}

			}

		}

		ArrayList<String> list = new ArrayList<>();

		for (String i : s) {

			if (!i.equals(" ")) {

				list.add(i);
			}

		}

		String[] output = new String[list.size()];

		return list.toArray(output);

	}

	private static String getIndicator(String input) {

		ArrayList<String> matches = new ArrayList<>();

		for (String s : indicators) {

			if (input.toLowerCase().contains(s.toLowerCase())) {

				matches.add(s);

			}

		}

		if (matches.size() == 1) {

			return matches.get(0);

		}

		int length = matches.get(0).length();
		String longest = matches.get(0);

		for (int i = 0; i < matches.size(); i++) {

			if (matches.get(i).length() > length) {

				length = matches.get(i).length();
				longest = matches.get(i);

			}
		}

		return longest;

	}

	private static String getCountry(String input) {

		ArrayList<String> matches = new ArrayList<>();

		for (String s : countries) {

			if (input.toLowerCase().contains(s.toLowerCase())) {

				matches.add(s);

			}

		}

		if (matches.size() == 0) {

			return null;

		}

		if (matches.size() == 1) {

			return matches.get(0);

		}

		int length = matches.get(0).length();
		String longest = matches.get(0);

		for (int i = 0; i < matches.size(); i++) {

			if (matches.get(i).length() > length) {

				length = matches.get(i).length();
				longest = matches.get(i);

			}
		}

		return longest;

	}

	private static int[] getDates(String[] words) {

		ArrayList<Integer> dates = new ArrayList<>();

		for (String s : words) {

			if (s.length() == 4) {

				Pattern pattern = Pattern.compile("\\d{4}");

				Matcher matcher = pattern.matcher(s);

				if (matcher.find()) {

					dates.add(Integer.parseInt(s));

				}

			}

		}

		int[] output = dates.stream().mapToInt(i -> i).toArray();

		return output;
	}

}
