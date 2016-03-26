/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBProcess {
    private Connection con1=null;
    private Statement st1=null;
    private ResultSet rs1=null;
   
    
    
    public DBProcess()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1","root","");
            st1 = (Statement) con1.createStatement();
      
        }catch(Exception ex){
            System.out.println("Error: " + ex);
      }
    }
    
    public String getAggregatedData(String snp_oddsRatio_DiseaseAllele)
    {
        
        String CalculatedString = "";
        //first split
        String list[] = snp_oddsRatio_DiseaseAllele.trim().split("\\|");
        for(int i = 0 ; i< list.length ; i++)
        {
            int calculation_total_weight = 0;
            //System.out.println(list.length);
            calculation_total_weight = 0;  
            //second split
            String subsets[] = list[i].split("\\:") ;
            String how_many_b_division[] = subsets[0].split(",");
            
            int total = how_many_b_division.length-2;
            //System.out.println(total);
            int weight_array[] = new int[subsets.length];
            
            for(int j=0 ; j<subsets.length; j++)
            {
                
                String singleElement[] = subsets[j].split(",");
                if(singleElement.length < 3) System.out.println("insersion error occured.please check");
                else
                {
                    calculation_total_weight = 0;
                    String snp_id = singleElement[0].trim();
                    String riskAlleleConvert = singleElement[1].trim();
                    String riskAllele = "";
                    //bit value from database come as form 0,1,2,3 rather than 00,01,10,11
                    if(riskAlleleConvert.equals("00")) riskAllele = "0";
                    else if(riskAlleleConvert.equals("01")) riskAllele = "1";
                    else if(riskAlleleConvert.equals("10")) riskAllele = "2";
                    else if(riskAlleleConvert.equals("11")) riskAllele = "3";
                    try {
                            String query = "SELECT * FROM snp WHERE snp_id = '" + snp_id+ "'";
                            rs1 = st1.executeQuery(query);
                            int weight1,weight2;
                            if(rs1.next())
                            {
                                //String snp_id = rs1.getString("snp_id");
                                String allele1 = rs1.getString("allele1");
                                if(riskAllele.equals(allele1))
                                {
                                    
                                    weight1 = rs1.getInt("weight1");
                                    calculation_total_weight = calculation_total_weight + weight1;
                                    
                                    //System.out.println(weight1);
                                    //System.out.println(PartsBetaValue);
                                }
                                String allele2 = rs1.getString("allele2");
                                //System.out.println(riskAllele + allele1 + allele2);
                                if(riskAllele.equals(allele2))
                                {
                                    weight2 = rs1.getInt("weight2");
                                    calculation_total_weight = calculation_total_weight + weight2;
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(DBProcess.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    weight_array[j] = calculation_total_weight;
                    //System.out.println(calculation_total_weight);
                }
            }
            
            
            
            for(int p = 0 ; p<total ; p++)
            {
                float calculation = 0;
                for(int j=0 ; j<subsets.length; j++)
                {
                    String element[] = subsets[j].split(",");
                    float PartsBetaValue = Float.parseFloat(element[p+2].trim());
                    calculation = calculation + PartsBetaValue * weight_array[j]/100;
                    
                }
               CalculatedString = CalculatedString + calculation + ": "; 
                
            }
        }
        CalculatedString = CalculatedString.substring(0,CalculatedString.length() - 2);
        return CalculatedString;
        
    }
}
