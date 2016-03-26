
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Laptop
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        DatabaseInsert connect = new DatabaseInsert();
        connect.getData();
        
        /*
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("test.txt"));
            while((sCurrentLine = br.readLine()) != null) 
            {
                    sCurrentLine = "sharmin;afrose";
                    String[] parts = sCurrentLine.split(";");
                    System.out.println(parts[0] + parts[1]);
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(br != null) br.close();
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        */
    }
}
