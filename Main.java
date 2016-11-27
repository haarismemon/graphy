package teamDesert;

import java.io.IOException;

/**
 * This class represents the running of the basic application which queries the API.
 *
 * @author Haaris Memon
 */
public class Main {

    public static void main(String[] args) throws IOException {
        //GDP in Brazil between 2006 and 2015
        System.out.println("GDP for Brazil" + MyWorldBank.getGDP("br", 2006, 2015));

        //GDP in Brazil between 2006 and 2015
        System.out.println("GDP Growth for Brazil" + MyWorldBank.getGDPGrowth("gb", 1990, 2012));
    }

}
