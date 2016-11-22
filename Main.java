import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

	public static void main(String[] args) throws IOException {
		
		
		//TEST USING URL QUERY TYPE
		
		String urlString = "http://api.worldbank.org/incomeLevels/LIC/countries";
		URL url = new URL(urlString);
		BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String line;
		
		while((line = input.readLine()) != null){
			
			System.out.println(line);
			
		}
		
		//TEST USING ARGUEMENT BASED QUERY
		
		String urlString2 = "http://api.worldbank.org/countries?per_page=10&incomeLevel=LIC";
		URL url2 = new URL(urlString);
		BufferedReader input2 = new BufferedReader(new InputStreamReader(url2.openStream()));
		
		String line2;
		
		while((line2 = input2.readLine()) != null){
			
			System.out.println(line2);
			
		}
		
		/*
		 * 
		 * https://datahelpdesk.worldbank.org/knowledgebase/articles/898581-api-basic-call-structure
		 * 
		 * Page detailing how to build queries using both techniques.
		 * 
		 */
		
		try {
			saveToFile(urlString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void saveToFile(String s) throws Exception{
		
		URL url = new URL(s);
		String target = "test.txt";
		Path path = Paths.get(target);
		
		InputStream in = url.openStream();
		Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
		
	}

}
