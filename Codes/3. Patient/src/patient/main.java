/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patient;

import java.io.IOException;
import java.net.Socket;
/**
 *
 * @author Laptop
 */
public class main {
    
    String disease;
    private final static int PORT = 6677;//SET A CONSTANT VARIABLE PORT
    private final static String HOST = "172.20.50.103";//SET A CONSTANT VARIABLE HOST
    public main(String disease_name)
    {
        disease = disease_name;
        
        
    }
    
    public void connection()
    {
        try
        {
            Socket s = new Socket(HOST, PORT);//CONNECT TO THE SERVER
            
            System.out.println("You connected to " + HOST);//IF CONNECTED THEN PRINT IT OUT
             
            Client client = new Client(s,disease);//START NEW CLIENT OBJECT
            Thread t = new Thread(client);//INITIATE NEW THREAD
            t.start();//START THREAD
        
        }

        catch (Exception noServer)//IF DIDNT CONNECT PRINT THAT THEY DIDNT

        {

            System.out.println("The server might not be up at this time.");
            System.out.println("Please try again later.");
        }
    }
}
