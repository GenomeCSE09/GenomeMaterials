package server;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Thread.yield;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Processing implements Runnable {

    private Socket socket;//SOCKET INSTANCE VARIABLE
    Scanner in ;//GET THE SOCKETS INPUT STREAM (THE STREAM THAT YOU WILL GET WHAT THEY TYPE FROM)
    PrintWriter out;//GET THE SOCKETS OUTPUT STREAM (THE STREAM YOU WILL SEND INFORMATION TO THEM FROM)
    Database ps ;
    int client ;
    float S ;
    long comOverhead = 0;
    
    long startTime, endTime, duration;

    public Processing(Socket s) {
        socket = s;//INSTANTIATE THE SOCKET
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            ps = new Database();
            client=0;
            S=0;
        } catch (IOException ex) {
            Logger.getLogger(Processing.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public void run() //(IMPLEMENTED FROM THE RUNNABLE INTERFACE)
    {
        try //HAVE TO HAVE THIS FOR THE in AND out VARIABLES
        {         
            for (int p = 0; ; p++)//WHILE THE PROGRAM IS RUNNING
            {
               if(p==0){
                    if (in.hasNext()) {
                        String input = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
                        System.out.println(socket.getLocalAddress().getHostAddress() + " Said: " + input);//PRINT IT OUT TO THE SCREEN

                        if (input.equals("patient")){
                            client = 1;
                            System.out.println("patient found");
                            //save patient socket number
                            while (true){
                                if (in.hasNext()){
                                    input = in.nextLine(); //read disease name
                                    System.out.println(input);
                                    //receive clininal data
                                    startTime = System.nanoTime();
                                    Main.M = ps.selectDisease(input);
                                    endTime = System.nanoTime();
                                    duration = (endTime - startTime)/1000000; 
                                    System.out.println("time needed for M: " + duration + " ms");
                                    comOverhead = Main.M.length() - ps.createOriginalMessage().length();
                                    comOverhead  = comOverhead * 8;
//                                    comOverhead /= 1024;
//                                    comOverhead /= 1024; //megabyte
                                    
                                    
//                                    comOverhead /= 1024;
//                                    comOverhead = (comOverhead/ps.createOriginalMessage().length()) * 100;
                                    System.out.println ("communication overhead: " + comOverhead);
                                    break;
                                }  
                            }                                                  
                        }
                        System.out.println(Main.M);
                        while (!Main.M.isEmpty()){
                            out.println(Main.M);//RESEND IT TO THE CLIENT
                            out.flush();//FLUSH THE STREAM
                            if (in.hasNext()) {
                                String R = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
                                System.out.println(socket.getLocalAddress().getHostName() + " Sends " + R);//PRINT IT OUT TO THE SCREEN
                                Main.Roverhead += R.length();
                                
                                Main.returnMessages.add(R);
                                System.out.println("returnMessages Size " + Main.returnMessages.size());
                                break;
                            }
                        }
                        
                    }
                }
               
//               while (!Main.M.isEmpty()){
//                            out.println(Main.M);//RESEND IT TO THE CLIENT
//                            out.flush();//FLUSH THE STREAM
//                            if (in.hasNext()) {
//                                String R = in.nextLine();//IF THERE IS INPUT THEN MAKE A NEW VARIABLE input AND READ WHAT THEY TYPED
//                                System.out.println(socket.getLocalAddress().getHostName() + " Sends " + R);//PRINT IT OUT TO THE SCREEN
//                                Main.returnMessages.add(R);
//                                System.out.println("returnMessages Size " + Main.returnMessages.size());
//                                break;
//                            }
//                        }  
                System.out.print("");
                

                if (Main.returnMessages.size() == Main.NUM_OF_DB) {                      
                    if (client == 1)
                    {   
                        startTime = System.nanoTime();
                        S = ps.genecticScoreCalculation();
                        endTime = System.nanoTime();
                        duration = (endTime - startTime)/1000000;
                        System.out.println("time needed for final calculation: " + duration + " ms");
                        Main.Roverhead *= 8;
//                        Main.Roverhead /= 1024;
//                        Main.Roverhead /= 1024;
                        System.out.println ("R overhead " + Main.Roverhead);
//                        System.out.println(S + " client value = " + client);
                        out.println ("genetic score is " + S);
                        out.flush();
                        client = 0;
                        break;
                    }
                    else{
                        try {
                            Thread.sleep(1000);
                            System.out.println(client + " sent to sleep");
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } 
//                Thread.sleep(1000);
//                System.out.println(client + " sent to sleep");
            }
        } catch (Exception e) {
            e.printStackTrace();//MOST LIKELY THERE WONT BE AN ERROR BUT ITS GOOD TO CATCH
        }
    }
}
