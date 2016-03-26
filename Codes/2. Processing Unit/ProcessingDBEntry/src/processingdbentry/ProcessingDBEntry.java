/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processingdbentry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laptop
 */
public class ProcessingDBEntry {

    
    public static void main(String[] args) {
        int lineNum = 0;
        Connection con1=null;
        Statement st1=null;
        ResultSet rs1=null;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/processingTest","root","");
            st1 = (Statement) con1.createStatement();
            
            BufferedReader br = null;
            try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("disease_snp_list.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                lineNum++;    
                String[] parts = sCurrentLine.split(";");
                if(parts.length == 6)  //to avoid the last blank line exception error
                {
                    String sql = "INSERT INTO snp_list " +  "VALUES ('" + parts[0] +"', '"+ parts[1] + "', '"+ parts[2] + "', '"+ parts[3] + "', '"+ parts[4] +  "', "+ Math.log(Float.parseFloat(parts[5])) +")";
                    System.out.println(sql);
                    st1.executeUpdate(sql);
                }
                else
                {
                    System.out.println("error on line " + lineNum);
                }
            }
            }catch (FileNotFoundException ex) {
            
        }
            
      }catch(Exception ex){
            System.out.println("Error: " + ex);
      }
    }
    
}
