/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesplitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *
 * @author Laptop
 */
public class FileSplitter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int lineTrack=0;
        BufferedReader br = null;
        PrintWriter writer1 = null;
        PrintWriter writer2 = null;
        PrintWriter writer3 = null;
        PrintWriter writer4 = null;
        PrintWriter writer5 = null;
        PrintWriter writer6 = null;
        PrintWriter writer7 = null;
        PrintWriter writer8 = null;
        PrintWriter writer9 = null;
        PrintWriter writer10 = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("SNPChrPosAllele_b129.txt"));
            //br = new BufferedReader(new FileReader("1.txt"));
            writer1 = new PrintWriter("output1.txt", "UTF-8");
            writer2 = new PrintWriter("output2.txt", "UTF-8");
            writer3 = new PrintWriter("output3.txt", "UTF-8");
            writer4 = new PrintWriter("output4.txt", "UTF-8");
            writer5 = new PrintWriter("output5.txt", "UTF-8");
            writer6 = new PrintWriter("output6.txt", "UTF-8");
            writer7 = new PrintWriter("output7.txt", "UTF-8");
            writer8 = new PrintWriter("output8.txt", "UTF-8");
            writer9 = new PrintWriter("output9.txt", "UTF-8");
            writer10 = new PrintWriter("output10.txt", "UTF-8");
            
            while((sCurrentLine = br.readLine()) != null) 
            {
                    lineTrack++;
                    
                    
                    
                    if(lineTrack>0 && lineTrack<=30000)
                    {
                        writer1.println(sCurrentLine);
                    }
                    else if(lineTrack>30000 && lineTrack<=60000)
                    {
                        writer2.println(sCurrentLine);
                    }
                    else if(lineTrack>60000 && lineTrack<=90000)
                    {
                        writer3.println(sCurrentLine);
                    }
                    else if(lineTrack>90000 && lineTrack<=120000)
                    {
                        writer4.println(sCurrentLine);
                    }
                    else if(lineTrack>120000 && lineTrack<=150000)
                    {
                        writer5.println(sCurrentLine);
                    }
                    else if(lineTrack>150000 && lineTrack<=180000)
                    {
                        writer6.println(sCurrentLine);
                    }
                    else if(lineTrack>180000 && lineTrack<=210000)
                    {
                        writer7.println(sCurrentLine);
                    }
                    else if(lineTrack>210000 && lineTrack<=240000)
                    {
                        writer8.println(sCurrentLine);
                    }
                    else if(lineTrack>240000 && lineTrack<=270000)
                    {
                        writer9.println(sCurrentLine);
                    }
                    else if(lineTrack>270000 && lineTrack<=300000)
                    {
                        writer10.println(sCurrentLine);
                    }
                    else
                    {
                        
                    }
                    
                        
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(br != null) br.close();
                writer1.close();
                writer2.close();
                writer3.close();
                writer4.close();
                writer5.close();
                writer6.close();
                writer7.close();
                writer8.close();
                writer9.close();
                writer10.close();
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
}
