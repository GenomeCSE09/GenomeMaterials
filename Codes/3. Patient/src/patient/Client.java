/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patient;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
 
public class Client implements Runnable {
 
    private Socket socket;//MAKE SOCKET INSTANCE VARIABLE
    String disease; 
    public Client(Socket s, String disease_name)
    {
        socket = s;//INSTANTIATE THE INSTANCE VARIABLE
        disease = disease_name;
        
    }
     
    public void run()//INHERIT THE RUN METHOD FROM THE Runnable INTERFACE
    {
        
        try
        {
            /* network connector initialze  */
            Scanner chat = new Scanner(System.in);//GET THE INPUT FROM THE CMD
            Scanner in = new Scanner(socket.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
            PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVER)
            
            /* database calling and processing start */
            DBProcess process1 = new DBProcess();   //add
        
             
            while (true)//WHILE THE PROGRAM IS RUNNING
            {   
                //System.out.println("enter");
                /* write "ready" and start  --- you can write anything now at this moment */
                //String input = chat.nextLine();   // write "ready"   //SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
                String input = "ready";
                if(input.equals("ready"))
                {
                    out.println("patient");//SEND IT TO THE SERVER After writing its ready
                    out.flush();//FLUSH THE STREAM

                    out.println(disease);
                    out.flush();
                    
                    if(in.hasNext())//IF THE SERVER SENT US SOMETHING
                    {
                        String snp_oddsRatio_riskAllele = in.nextLine();  //get "message"
                        System.out.println(snp_oddsRatio_riskAllele);
                        long startTime = System.nanoTime();
                        String db1 = process1.getAggregatedData(snp_oddsRatio_riskAllele);
                        long endTime = System.nanoTime();
                        long duration = (endTime - startTime)/1000000;
                        System.out.println("Duration to generate Message: " + duration);
                        
                        //String db1 = "partial value";    //remove
                        System.out.println(db1);
                        //System.out.println(in.nextLine());//PRINT IT OUT
                        out.println(db1);//SEND IT TO THE SERVER
                        out.flush();//FLUSH THE STREAM
                        
                        if(in.hasNext())  //final result found
                        {
                            String str = in.nextLine();
                            System.out.println(str);
                        }
                    }
                    
                }
                else if(input.equals("exit"))
                {
                    break;
                }
                else
                    System.out.println("command not recognize");
                         
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
        }
    }
}
