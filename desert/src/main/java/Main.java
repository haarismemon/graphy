package main.java;

import java.io.IOException;

/**
 * This class represents the running of the basic application which queries the API.
 *
 * @author Haaris Memon
 */
public class Main {

    public static void main(String[] args) throws IOException {
 /*       //GDP in Brazil (all data)
        System.out.println("GDP for Brazil" + MyWorldBank.getGDP("br", 2001, 0));


        //GDP Growth in Brazil between 1990 and 2012
        System.out.println("GDP Growth for Brazil" + MyWorldBank.getGDPGrowth("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getGDPPerCapita("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getGDPPerCapitaGrowth("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getConsumerPriceInflation("gb", 1990, 2012));
*/
        System.out.println("GDP Growth for Brazil" + MyWorldBank.getUnemploymentTotal(null, 2000, 2004));
/*
        System.out.println("GDP Growth for Brazil" + MyWorldBank.getUnemploymentMale("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getUnemploymentYoungMale("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getUnemploymentFemale("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getUnemploymentYoungFemale("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getGDPDeflatorInflation("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getCurrentAccountBalance("gb", 1990, 2012));

        System.out.println("GDP Growth for Brazil" + MyWorldBank.getCurrentAccountBalancePercentOfGDP("gb", 1990, 2012));


        */
    }

}
