package main.java.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.Calendar;

/**
 * Cache management.
 * 
 * @author Haaris Memon
 * @author Vladislavs Uljanovs
 */
public class CacheAPI {
	
    /**
     * Saves the query to file.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     */
    public static void cache(String indicator, String countryCode, String rawData) {
        Calendar c = Calendar.getInstance();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("cache.txt", true))) {
            if (cacheSize(new File("cache.txt")) == 30) { // limit of 30 queries to be store in the cache file
                BufferedReader reader = new BufferedReader(new FileReader(new File("cache.txt")));
                String[] values = reader.readLine().split("/");
                reader.close();
                deleteQuery(values[0], values[1]); // delete the oldest query
            }
            writer.println(countryCode + "/" + indicator + "/" + c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + "/" + rawData);
            System.out.println("=> Log.cache: DATA CACHED");
        } catch (IOException e) {
            // Ignore exception
            System.out.println("=> Log.cache: ERROR");
        }
    }

    private static String listCache() {
    	File cache = new File("cache.txt");
        if (!cache.exists() && !cache.isDirectory()) { // file does not exist i.e. no cached data
            System.out.println("=> Log.fetchOffline: NO CACHED DATA");
            return null;
        }

        String rawData = null;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(cache));

            String line = null;

            Calendar c = Calendar.getInstance();
            String currentMMYYYY = Integer.toString(c.get(Calendar.MONTH)) + Integer.toString(c.get(Calendar.YEAR));

            while ((line = reader.readLine()) != null) {
                
                String[] values = line.split("/");
                
                System.out.println(values[0] + " " + values[1] + " " + values[2]);
                
//                List<String[]> list;
                
            }
            reader.close();
        } catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("=> Log.fetchOffline: ERROR");
        }
        
        return rawData;
    }
    
    /**
     * Finds the number of queries stored in the cache file.
     *
     * @param file the text file that stores the queries
     * @return number of queries in the file
     */
    private static int cacheSize(File file) {
        try {
            FileReader fr = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fr);

            int size = 0;

            while(lnr.readLine() != null) size++;
            lnr.close();
            fr.close();

            return size;
        } catch (IOException e) {
            System.out.println("=> Log.cacheSize: ERROR");
            return 0;
        }
    }

    /**
     * Deletes (one) specified query from the cache file.
     *
     * @param indicator the indicator id
     * @param countryCode the country id
     */
    public static void deleteQuery(String indicator, String countryCode) throws IOException {
        File cache = new File("cache.txt");
        File temp = new File("temp.txt");

        BufferedReader reader = new BufferedReader(new FileReader(cache));
        PrintWriter writer = new PrintWriter(new FileWriter(temp, true));

        String line = null;

        while ((line = reader.readLine()) != null) {
            String[] values = line.split("/");
            // countryCode AND indicator FOUND
            if ((values[0]).equalsIgnoreCase(countryCode) && (values[1]).equalsIgnoreCase(indicator)) continue;
            writer.println(line);
        }
        writer.close();
        reader.close();

        // create source as the temp file
        BufferedReader tempReader = new BufferedReader(new FileReader(temp));
        // create the destination to be the cache file
        PrintWriter cacheWriter = new PrintWriter(new FileWriter(cache));

        String tempLine = null;

        // copy everything from temp into cache text file
        while ((tempLine = tempReader.readLine()) != null) {
            cacheWriter.println(tempLine);
        }
        cacheWriter.close();
        tempReader.close();

        temp.delete();

        System.out.println("=> Log.deleteQuery: QUERY REMOVED");
    }

    /**
     * Deletes all queries from the cache file i.e. deletes file.
     */
    private static void clearCache() throws IOException {
        File file = new File("cache.txt");
        file.delete();
    }
}
