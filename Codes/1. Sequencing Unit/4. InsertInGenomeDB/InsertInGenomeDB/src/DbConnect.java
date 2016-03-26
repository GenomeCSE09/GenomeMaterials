

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



public class DbConnect {
    private Connection con1=null;
    private Statement st1=null;
    private ResultSet rs1=null;
    
    //private Connection con2=null;
    //private Statement st2=null;
    //private ResultSet rs2=null;
    
    //private Connection conPa=null;
    //private Statement stPa=null;
    //private ResultSet rsPa=null;
    
    
    public DbConnect()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db2","root","");
            st1 = (Statement) con1.createStatement();
            
            
      }catch(Exception ex){
            System.out.println("Error: " + ex);
      }
    }
    
    public void setData()
    {
        
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("output55_2.txt"));
            
            while ((sCurrentLine = br.readLine()) != null) {
                    
                String[] parts = sCurrentLine.split(";");
                String query = "DELETE FROM snp WHERE snp_id = '" + parts[1] + "'";
                st1.executeUpdate(query);
                
                
                
                if(parts.length > 1)  //to avoid the last blank line exception error
                {
                    String sql = "INSERT INTO snp " +  "VALUES ('" + parts[0] +"', '"+ parts[1] + "', b'"+ parts[2] + "', "+ parts[3] + ", b'"+ parts[4] +  "', "+ parts[5] +")";
                    System.out.println(sql);
                    st1.executeUpdate(sql);
                }
                
                
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
        /*
        Random randomGenerator = new Random();
        for(int i = 0 ; i< 5 ; i++)
        {
            float randomfloat = (randomGenerator.nextFloat() * 2) - 1;
            System.out.println(randomfloat);
            DecimalFormat df = new DecimalFormat("#.#");
            //df.format(randomfloat);
            String newvalue = df.format(randomfloat);
            float newFloat = Float.parseFloat(newvalue);
            System.out.println(newFloat);
            
        }
        */
        
        /*
        try
        {
            String query = "select * from db1";
            rs1 = st1.executeQuery(query);
            System.out.println("Records from database");
            
            while(rs1.next())
            {
                //String disease = rs1.getString("disease");
                //String col1 = rs1.getString("col1");
                //String col2 = rs1.getString("col2");
                //System.out.println("disease " + disease + " " + col1+ " " + col2);
            }
        }catch(Exception ex)
        {
         }
        
        */
    }
    
}
