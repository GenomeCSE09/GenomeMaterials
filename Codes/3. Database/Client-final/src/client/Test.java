/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;

/**
 *
 * @author Laptop
 */
public class Test {
    public static void main(String[] args) throws IOException
    {
        String snp_oddsRatio_riskAllele = "rs3798220, 11, 1, 2, 1, 2  : rs4977574, 00, 2, 1, 1, 3  | rs3798220, 01, 2, 1, 1, 2 | rs17465637, 00, 1, 1, 1, 1 : rs3798220, 01, 1, 1, 1, 1 | rs17465637, 00, 1, 1, 1, 1 : rs3798220, 01, 1, 1, 1, 1|"; 
        DBProcess process1 = new DBProcess();
        String db2 = process1.getAggregatedData(snp_oddsRatio_riskAllele);
        System.out.println(db2);
    }
}
