import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputAnalysis {
	
	String[] indicators = new String[]{"GDP", "GDP per capita", "Consumer Price Indices", "Retail Price Indices",
			"Unemployment", "Inflation", "Deflation", "Investment", "Production Possibility curve",
			"Aggregate Demand", "Aggregate Supply", "Current account balance"};
	
	String[] countries = new String[]{
			"Afghanistan","Argentina","Barbados","Brazil","Chile","China","Colombia","Cuba",
			"Ecuador","Finland","Fiji","France","United Kingdom","Greece","India",
			"Ireland","Indonesia","Hungary","Croatia"
	};
	
	String[] toIgnore = new String[]{
			"in", "between", "from", "since", "at", "-", "to"
	};
	
	public static void main(String[] a){
		
		new InputAnalysis().isValidCommand("Unemployment in Cuba from 1992 to 2002");
		
	}
	
	private boolean isValidCommand(String input){
		
		
		
		String indicator = "";
		String country;
		int startDate;
		int endDate;
		
		if(input == null){
			
			return false;
			
		}
			
		String[] words = ignoreConjunctives(input.split(" "));
		indicator = getIndicator(words);
		country = getCountry(words);
		
		int[] dates = getDates(words);
		
		startDate = dates[0];
		endDate = dates[1];
		
		System.out.println(indicator);
		System.out.println(country);
		System.out.println(startDate);
		System.out.println(endDate);
		
		if(indicator != null && country != null){
			
			return true;
			
		}else{
			
			return false;
			
		}
	
	}
	
	
	private String[] ignoreConjunctives(String[] s){
		
		
		
		
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

	private String getIndicator(String[] words){
		
		for(String i: words){
			
			for(String j: indicators){
				
				if(i.equals(j)){
					
					return i;
					
				}
				
			}
			
		}
		
		return null;
		
	}
	
	private String getCountry(String[] words){
		
		for(String i: words){
			
			for(String j: countries){
				
				if(i.equals(j)){
					
					return i;
					
				}
				
			}
			
		}
		
		return null;
		
	}
	
	private int[] getDates(String[] words){
		
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
	
}
