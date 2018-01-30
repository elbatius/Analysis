/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ie3100m;

import Logic.*;
import Model.Stats.*;
import Model.Product.*;
import Model.Order;
import Utils.BinScanner;
import ilog.concert.IloException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin-Notebook
 */
public class OrderPacker {

    public static final double MAX_WEIGHT = 30;
    
    
    public static PackingConfig packOrder(Order order) {
        /**
         * TODO: put order stats as input
         */
        int numOrderedBox = order.getQuantity();
        Level2_Box box = new Level2_Box(order.getBox());
        
        ArrayList<Level3_Bin> binList = new ArrayList<>();
        ArrayList<BinStats> allBinStats = new ArrayList<>();
        ArrayList<PackingConfig> configs = new ArrayList<>();
        
        try {
            binList = BinScanner.loadBinTypes("bins.csv");
        } catch (IOException ex) {
            Logger.getLogger(OrderPacker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Level3_Bin bin : binList) {
            allBinStats.add(new BinStats(box, bin));
        }
        BinStatsCalculator.setStatsForAllBins(allBinStats);
        
        for (BinStats binStat : allBinStats) {
            if (binStat.getTotalQuantity() > 0) {
                configs.add(new PackingConfig(order, binStat));
            }
        }
        PackingConfigCalculator.setAllConfigs(configs, allBinStats);
        
        PackingConfig bestConfig = determineBestConfig(configs);
        
        if (bestConfig == null) {
            System.out.println("No suitable config found");
        } else {
            System.out.println("Chosen config:");
            System.out.println(bestConfig);
        }
        
        return bestConfig;
    }
    
    /**
     * Determine the best packing configuration based on the sorting order
     * @param packingConfigs the array of possible packing configurations
     * @return the most desired packing configuration
     */
    private static PackingConfig determineBestConfig(ArrayList<PackingConfig> packingConfigs) {
        //might use a PQ for this instead
        Collections.sort(packingConfigs, (a, b) -> {
            if (a.getTotalBinsInclRemainder() == b.getTotalBinsInclRemainder()) {
                return a.getTotalEmptyVol() <= b.getTotalEmptyVol() ? -1 : 1;
            } else {
                return a.getTotalBinsInclRemainder() - b.getTotalBinsInclRemainder();
            }
        });
        
        for (PackingConfig config : packingConfigs) {
            if (config.getMainBinStats().getTotalQuantity() != 1) {
                return config;
            }
            //System.out.println(config);
            //System.out.println();
        }
        
        return null;
    }

    private static int calcUpperBound(Level2_Box box, Level3_Bin bin) {
        return bin.getBaseArea() / box.getBaseArea();
    }
}
