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
//        //GDP in Brazil (all data)
//        System.out.println("GDP for Brazil" + WorldBankAPI.getGDP("br", 0, 1963));
//
//        //GDP Growth in Brazil between 1990 and 2012
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getGDPGrowth("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getGDPPerCapita("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getGDPPerCapitaGrowth("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getConsumerPriceInflation("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getUnemploymentTotal("br", 2000, 2000));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getUnemploymentMale("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getUnemploymentYoungMale("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getUnemploymentFemale("gb", 1990, 2012));
//
        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getUnemploymentYoungFemale("br", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getGDPDeflatorInflation("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getCurrentAccountBalance("gb", 1990, 2012));
//
//        System.out.println("GDP Growth for Brazil" + WorldBankAPI.getCurrentAccountBalancePercentOfGDP("gb", 1990, 2012));

    }

}
