package main.java.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cache management: add, update and delete queries.
 * 
 * @author Vladislavs Uljanovs
 * @author Haaris Memon
 */
public class CacheAPI {
    
    /**
     * Validates existence of cache.
     *
     * @return cache cache file or null if cache file does not exist
     */
    private static File getCacheFile() {
        File cache = new File("cache.txt");
        if (!cache.exists() && !cache.isDirectory()) { // file does not exist i.e. no cached data
            System.out.println("=> Log.fetchOffline: NO CACHED DATA");
            return null;
        }
        return cache;
    }

    /**
     * Saves the query to file.
     *
     * @param query   query object holding data about query made by the user
     * @param rawData unprocessed requested data in JSON string format
     */
    protected static void cacheQuery(Query query, String rawData) {
        File cache = getCacheFile();
        if (cache == null) cache = new File("cache.txt");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(cache, true));
            checkLimit();
            writer.println(query.getIndicatorCode() + "/" 
                            + query.getCountryCode() + "/" 
                                + query.getStartYear() + "/" 
                                    + query.getEndYear() + "/" 
                                        + query.getDateOriginal() + "/" 
                                            + rawData);
            writer.close();
            System.out.println("=> Log.cache: DATA CACHED");
        } catch (IOException e) {
            System.out.println("=> Log.cache: ERROR");
        }
    }

    /**
     * Loads raw data from cache.
     *
     * @param   query   query object holding data about query made by the user
     * @return  rawData unprocessed requested data in JSON string format, OR null if:
     *                  - data stored is outdated i.e it is not cached in the same month and year
     *                  - required data is not in cache
     */
    protected static String fetchOffline(Query query) {
        String rawData = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getCacheFile()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("/");
                if (values[0].equalsIgnoreCase(query.getIndicatorCode()) 
                        && values[1].equalsIgnoreCase(query.getCountryCode()) ) { // countryCode AND indicator FOUND
                    if (!isExpired(query)) rawData = values[5];
                    break;
                } else {
                    System.out.println("=> Log.fetchOffline: DATA NOT FOUND IN CACHE");
                }
            }
            reader.close();
        } catch (IOException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("=> Log.fetchOffline: ERROR OR NO CACHE EXIST");
        }
        return rawData;
    }
    
    /**
     * Validates whether the query is valid or not,
     * i.e. not out-dated/query must be made in the same month and year to be valid
     *
     * @param   query   query object holding data about query made by the user
     * @return          {@code true} if query not expired, and {@code false} otherwise.
     */
    private static boolean isExpired(Query query) {
        String currectDate = new SimpleDateFormat("MM.yyyy").format(new Date());
        if (query.getMonthYear().equals(currectDate)) { // data must be cached in the same month to be valid
            System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE");
            return false;
        } else {
        	System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE BUT OUTDATED");
        	if (!WorldBankAPI.connectionPresent()) {
        		System.out.println("=> Log.fetchOffline: CONNECTION IS PRESENT SO DATA FOUND WILL BE DELETED");
                deleteOutdatedQuery(query);
                return true;
        	}
        }
        //TODO
        System.out.println("//////////!!!!!!!!!!! INFORM THE USER !!!!!!!!!!!!\\\\\\\\\\\\");
        System.out.println("=> Log.fetchOffline: CONNECTION IS !!NOT!! PRESENT SO USE OUTDATED QUERY");
		return false;
    }

    /**
     * Finds the number of queries stored in the cache file.
     *
     * @return  number of cached queries in the file
     */
    private static int cacheSize() {
        try {
            LineNumberReader lnr = new LineNumberReader(new FileReader(getCacheFile()));
            int size = 0;
            while (lnr.readLine() != null) size++;
            lnr.close();
            return size;
        } catch (IOException e) {
            System.out.println("=> Log.cacheSize: ERROR");
            return 0;
        }
    }

    /**
     * Checks if number of cached queries is equal to 30 records.
     * This methods makes sure the cache file is not too big.
     * If number of records equals to 30 then delete the oldest query.
     * 
     */
    private static void checkLimit() {
        if (cacheSize() == 30) { // limit of 30 queries to be store in the cache file
        	//TODO
        	System.out.println("//////////!!!!!!!!!!! INFORM THE USER THAT THE query WILL BE DELETED BECAUSE THE CACHE LIMIT REACHED, THIS IS FORCED AND THE USER HAVE NO CHOICE !!!!!!!!!!!!\\\\\\\\\\\\");
            deleteOldestQuery();
        }
    }
    
    /**
     * Deletes the oldest query in the cache.
     * 
     */
    private static void deleteOldestQuery() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getCacheFile()));
            String[] values = reader.readLine().split("/");
            reader.close();
            deleteQuery(values[0], values[1]); // delete the oldest query i.e. first
        } catch (IOException e) {
        	System.out.println("=> Log.deleteOldestQuery: ERROR");
        }
    }
    
    /**
     * Deletes the out-dated query in the cache
     * i.e. a query which not queried in the same month and year
     * 
     */
    private static void deleteOutdatedQuery(Query query) {
    	deleteQuery(query.getIndicatorCode(), query.getCountryCode());
    }
    
    // TODO check that here there is no need to check if the cache null or empty
    /**
     * Deletes specified query from the cache file.
     *
     * @param indicatorCode     indicator code of the query
     * @param countryCode       country code of the query
     */
    protected static void deleteQuery(String indicatorCode, String countryCode) {
    	try {
    		File cache = getCacheFile(); 
    		File temp = new File("temp.txt");

            BufferedReader reader1 = new BufferedReader(new FileReader(cache));
            PrintWriter writer1 = new PrintWriter(new FileWriter(temp, true));

            String line = null;
            while ((line = reader1.readLine()) != null) {
                String[] values = line.split("/");
                if (values[0].equalsIgnoreCase(indicatorCode) 
                		&& values[1].equalsIgnoreCase(countryCode)) continue; // countryCode AND indicatorCode FOUND, skip
                writer1.println(line);
            }
            writer1.close(); reader1.close();
            
            // copy updated list from temp file to cache file, cache file is overridden
            BufferedReader reader2 = new BufferedReader(new FileReader(temp));
            PrintWriter writer2 = new PrintWriter(new FileWriter(cache));
            String tempLine = null;
            while ((tempLine = reader2.readLine()) != null) writer2.println(tempLine);
            writer2.close(); reader2.close();
            
            temp.delete();

            System.out.println("=> Log.deleteQuery: QUERY REMOVED");
    	} catch (IOException e) {
    		System.out.println("=> Log.deleteQuery: ERROR");
    	}
        
    }

    /**
     * Deletes all queries from the cache file i.e. deletes file.
     * 
     */
    private static void clearCache() {
        File file = new File("cache.txt");
        file.delete();
    }
    
    public static void updateCache(Query query) {
    	try {
    		File cache = getCacheFile(); 
    		File temp = new File("temp.txt");

            BufferedReader reader1 = new BufferedReader(new FileReader(cache));
            PrintWriter writer1 = new PrintWriter(new FileWriter(temp, true));

            String line = null;
            while ((line = reader1.readLine()) != null) {
                String[] values = line.split("/");
                if (values[0].equalsIgnoreCase(query.getIndicatorCode()) 
                		&& values[1].equalsIgnoreCase(query.getCountryCode())) {
//                	System.out.println("FOUND " + values[0] + values[1]);
//                	System.out.println(line);
                	String newStartYear = Integer.toString(query.getStartYear());
                	String newEndYear = Integer.toString(query.getEndYear());
//                	System.out.println("NEW " + newStartYear);
//                	System.out.println("NEW " + newEndYear);
                	line = line.replace(values[2], newStartYear);
                	line = line.replace(values[3], newEndYear);
                	System.out.println(line);
                }//TODO
                writer1.println(line);
            }
            writer1.close(); reader1.close();
            
            // copy updated list from temp file to cache file, cache file is overridden
            BufferedReader reader2 = new BufferedReader(new FileReader(temp));
            PrintWriter writer2 = new PrintWriter(new FileWriter(cache));
            String tempLine = null;
            while ((tempLine = reader2.readLine()) != null) writer2.println(tempLine);
            writer2.close(); reader2.close();
            
            temp.delete();

            System.out.println("=> Log.deleteQuery: QUERY UPDATED");
    	} catch (IOException e) {
    		System.out.println("=> Log.deleteQuery: ERROR");
    	}
    	
    	
    	// Open a temporary file to write to.
//    	PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("d:\\book.temp")));
//
//    	// ... then inside your loop ...
//    	while ((line = br.readLine()) != null) {
//    	    if (request.getParameter("hname").equals(line)) {
//    	        line = line.replace(request.getParameter("hname"), request.getParameter("book"));
//    	    }
//    	    // Always write the line, whether you changed it or not.
//    	    writer.println(line);
//    	}
//
//    	// ... and finally ...
//
//    	File realName = new File("d:\\book.txt");
//    	realName.delete(); // remove the old file
//    	new File("d:\\book.temp").renameTo(realName); // Rename temp file
    }

    /**
     * Lists all cached queries.
     *
     * @return queries list of query object i.e. list of all cached queries
     */
    public static List<Query> listCache() {
        File cache = getCacheFile(); // if null then file does not exists
        if (cache == null) System.out.println("=> Log.deleteQuery: ERROR IS GOOD, JUST NEED TO HANDLE THIS TODO");
        List<Query> queries = new ArrayList<Query>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cache));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("/");
                Query query = new Query(values[0], values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]), Query.convertToDate(values[4]));
                queries.add(query);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("=> Log.fetchOffline: ERROR");
        }

        return queries;
    }

    public static void main(String[] args) throws IOException {
    	Query q = new Query("NY.GDP.MKTP.KD.ZG", "GB", 1990, 2001, new Date());
    	System.out.println(cacheSize());
    	updateCache(q);
    	System.out.println(cacheSize());
    	
    	
    }
}
