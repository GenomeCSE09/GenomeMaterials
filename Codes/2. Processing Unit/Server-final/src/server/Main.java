package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    public static int NUM_OF_DB = 4;   // number of database
    public static ArrayList<Socket> ConnectionArray = new ArrayList<Socket>();
    public static String M = "";
    public static ArrayList<String> returnMessages = new ArrayList<>();
    
    
    public static String message = "";
    public static ArrayList<Boolean> marker = new ArrayList<>();
    public static long Roverhead = 0;
    
    public static void main(String[] args) throws IOException {
        try {
            final int PORT = 6677;//SET NEW CONSTANT VARIABLE: PORT
            ServerSocket server = new ServerSocket(PORT); //SET PORT NUMBER
            System.out.println("Waiting for clients...");//AT THE START PRINT THIS

            while (true)//WHILE THE PROGRAM IS RUNNING
            {
               // System.out.println("in while loop");
                Socket s = server.accept();//ACCEPT SOCKETS(CLIENTS) TRYING TO CONNECT
                System.out.println("client trying to connect");
                ConnectionArray.add(s);
                System.out.println("Client connected from " + s.getLocalAddress().getHostName());   //  TELL THEM THAT THE CLIENT CONNECTED

                Processing chat = new Processing(s);//CREATE A NEW CLIENT OBJECT
                Thread t = new Thread(chat);//MAKE A NEW THREAD
                t.start();//START THE THREAD
            }
        } catch (Exception e) {
            System.out.println("An error occured.");//IF AN ERROR OCCURED THEN PRINT IT
            e.printStackTrace();
        }
    }
}
