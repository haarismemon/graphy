package main.java;

import main.java.api.WorldBankAPI;

import java.io.IOException;

/**
 * This class represents the running of the basic application which queries the API.
 *
 * @author Haaris Memon
 */
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println(WorldBankAPI.query("gdp", "gb", 2000, 2006));

    }

}
