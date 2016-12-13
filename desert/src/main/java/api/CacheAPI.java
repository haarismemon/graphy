//package main.java.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cache management: add, update and delete queries.
 * 
 * @author Vladislavs Uljanovs
 * @author Haaris Memon
 */
public class CacheAPI {
	
	/**
     * List of queries in memory cache.
     * 
     */
	private static List<Query> memoryCache = new ArrayList<>();

    /**
     * Adds a query to the list of memory cache.
     *
     */
    protected static void addQuery(Query query) {
    	int size = memoryCache.size();
    	if (size > 30) {
    		deleteOldestQuery();
    	}
    	memoryCache.add(query);
    }
    
    /**
     * Updates a query in the list of queries in the memory cache.
     *
     */
    protected static void updateQuery(Query query) {
    	for (Query q : memoryCache) {
            if (q.equals(query)) {
            	int index = memoryCache.indexOf(q);
            	memoryCache.remove(q);
            	memoryCache.add(index, query);
                return;
            }
        }
    	System.out.println("=> Log.updateQuery: QU");
        System.out.println("QUERY NOT FOUND");
    }
    
    /**
     * Verifies whether the query is in the list of queries in the memory cache or not.
     *
     * @param  query object to verify
     * @return {@code true} if the query exists, and {@code false} otherwise.
     */
    private static boolean isPresent(Query query) {
        for (Query q : memoryCache) {
        	if (q.equals(query)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Finds the number of queries stored in the memory cache.
     *
     * @return  number of cached queries in the file
     */
    private static int cacheSize() {
    	return memoryCache.size();
    }
    
    /**
     * Lists all queries from memory cache.
     *
     * @return list of queries in the memory cache.
     */
    public static List<Query> listCache() {
    	return memoryCache;
    }
    
    /**
     * Deletes the oldest query in the cache.
     * 
     */
    private static void deleteOldestQuery() {
    	memoryCache.remove(cacheSize() - 1);
    }
    
    /**
     * Deletes the out-dated query in the cache
     * i.e. a query which not queried in the same month and year
     * 
     */
    private static void deleteOutdatedQuery(Query query) {
    	deleteQuery(query);
    }
    
    /**
     * Removes a query from the list of queries in memory cache.
     *
     */
    protected static void deleteQuery(Query query) {
        for (Query q : memoryCache) {
            if (q.equals(query)) {
            	memoryCache.remove(q);
                return;
            }
        }
        // TODO
        System.out.println("//////// QUERY NOT FOUND");
    }
    
    /**
     * Removes all the queries from the list of queries in memory cache.
     * 
     */
    public static void clearCache() {
        if (memoryCache.size() != 0) memoryCache.clear();
    }
    
    /**
     * Verifies whether the query is valid or not,
     * i.e. not out-dated, query must be made in the same month and year to be valid
     *
     * @param  query object holding details about query made by the user
     * @return {@code true} if query not expired, and {@code false} otherwise.
     */
    private static boolean isExpired(Query query) {
        String currectDate = new SimpleDateFormat("MM.yyyy").format(new Date());
        if (query.getMonthYear().equals(currectDate)) { // data must be cached in the same month to be valid
            System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE");
            return false;
        } else {
        	System.out.println("=> Log.fetchOffline: DATA FOUND IN CACHE BUT OUTDATED");
        	if (WorldBankAPI.connectionPresent()) {
        		System.out.println("=> Log.fetchOffline: CONNECTION IS PRESENT SO DATA FOUND WILL BE DELETED");
                deleteOutdatedQuery(query);
                return true;
        	} else {
        		//TODO
        		System.out.println("//////////!!!!!!!!!!! INFORM THE USER !!!!!!!!!!!!\\\\\\\\\\\\");
                System.out.println("=> Log.fetchOffline: CONNECTION IS !!NOT!! PRESENT SO USE OUTDATED QUERY i.e. QUERY FROM NOT THE SAME MONTH");
        	}
        }
		return false;
    }
    
    /**
     * Returns query holding data with unprocessed year range from cache.
     *
     * @param  query object holding details about query made by the user
     * @return query object holding data with unprocessed year range
     */
    protected static Query fetchOffline(Query query) {
    	for (Query q : memoryCache) {
            if (q.equals(query)) {
            	if (!isExpired(q)) {
            		q.setStartYear(query.getStartYear());
                	q.setEndYear(query.getEndYear());
            		return q;
            	}
            }
        }
        // TODO
        System.out.println("//////// QUERY NOT FOUND OR DELETED BECAUSE OUTDATED");
    	return null;
    }
    
    /**
     * Verifies existence of cache.
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
     * Parses String from cache file to query's map (yearValue) of not filtered data
     * i.e. all years for this (indicator and country)
     *
     * @param  query object holding details about query made by the user
     * @return query object holding data with unprocessed year range
     */
    private static Map<Integer, Double> parseStringToMap(String mapInString) {
    	String str = mapInString;
    	str = str.substring(1, str.length() - 1);
        String[] pairs = str.split(",");

        Map<Integer, Double> yearValue = new HashMap<>();               

        for (String pair : pairs) {
            String[] index = pair.split("=");
            String key = index[0].trim();
            String value = index[1].trim();
            yearValue.put(Integer.parseInt(key), Double.parseDouble(value));
        }
        
        return yearValue;
    }
	
	/**
     * Loads the list of queries from the cache file. Each line contains a query.
     *
     */
    public static void loadFromFile() {
    	List<Query> queries = new ArrayList<Query>();
    	
    	File cache = getCacheFile(); // if null then file does not exists
        if (cache != null) {
        	try (BufferedReader reader = new BufferedReader(new FileReader(getCacheFile()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split("/");
                    Query query = new Query(values[0], values[1], Integer.parseInt(values[2]), Integer.parseInt(values[3]), Query.convertToDate(values[4]));
                    query.setData(parseStringToMap(values[5]));
                    query.setTitle(values[6]);
                    query.setColour(values[7]);
                    queries.add(query);
                }
                reader.close();
            } catch (IOException e) {
                System.out.println("=> Log.fetchOffline: ERROR");
            }
        }
        memoryCache = queries;
    }
    
    /**
     * Saves the list of queries in memory cache to cache file. Each line contains a query.
     * 
     */
    public static void saveToFile() {
    	File cache = getCacheFile();
    	if (cache == null) cache = new File("cache.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cache))) {
            for (Query query : memoryCache) {
                writer.write(query.toString());
                writer.newLine();
            }
        } catch (IOException e) {
        	System.out.println("saveToFile ERROR");
        }
    }
    
}
