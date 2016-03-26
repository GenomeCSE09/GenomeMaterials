
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;



public class DatabaseInsert {
    private Connection con1=null;
    private Statement st1=null;
    private ResultSet rs1=null;
    
    private Connection con2=null;
    private Statement st2=null;
    private ResultSet rs2=null;
    
    private Connection con3=null;
    private Statement st3=null;
    private ResultSet rs3=null;
    
    private Connection conPa=null;
    private Statement stPa=null;
    private ResultSet rsPa=null;
    
    private Connection conPr=null;
    private Statement stPr=null;
    private ResultSet rsPr=null;
    
    
    public DatabaseInsert()
    {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1","root","");
            st1 = (Statement) con1.createStatement();
            
            con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db2","root","");
            st2 = (Statement) con2.createStatement();
            
            con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/db3","root","");
            st3 = (Statement) con3.createStatement();
            
            conPr = DriverManager.getConnection("jdbc:mysql://localhost:3306/processing","root","");
            stPr = (Statement) conPr.createStatement();
            
            conPa = DriverManager.getConnection("jdbc:mysql://localhost:3306/patient","root","");
            stPa = (Statement) conPa.createStatement();
            
            
      }catch(Exception ex){
            System.out.println("Error: " + ex);
      }
    }
    
    public void getData() throws SQLException
    {
        int lineTrack=0;
        BufferedReader br = null;
        PrintWriter writer = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("output10.txt"));
            //br = new BufferedReader(new FileReader("1.txt"));
            writer = new PrintWriter("output1010.txt", "UTF-8");
            while((sCurrentLine = br.readLine()) != null) 
            {
                    
                    lineTrack++;
                    //sCurrentLine = "sharmin-afrose";
                    String[] parts = sCurrentLine.split(";");
                    
                    String pseudoname = "Karim";
                    if(parts.length >= 5)
                    {
                        //System.out.println("hello");
                        String snp_id = parts[3];
                        String alleleHave = parts[4];
                        String alleleAlt = parts[5];
                        String orient = parts[7];
                        if(alleleHave.equals("A") || alleleHave.equals("C") || alleleHave.equals("G") || alleleHave.equals("T"))
                        {
                            if(alleleAlt.equals("A") || alleleAlt.equals("C") || alleleAlt.equals("G") ||alleleAlt.equals("T"))
                            {
                                writer.println(pseudoname + ";" + snp_id + ";" + alleleHave + ";" + alleleAlt + ";" + orient);
                            }
                        }
                        
                        
                        //System.out.println(pseudoname + " " + snp_id + " " + allele1 + " " + allele2);
                    }
                    else
                    {
                        System.out.println("not printing the line" + lineTrack);
                    }
                    
                    //System.out.println(parts[0] + parts[1]);
                    
                    //System.out.println("INSERT into snp VALUES(" + pseudoname + "," + snp_id + "," + allele1 + "," + weight1 + "," + allele2 + "," + weight2 + ")");
                    //int val = st2.executeUpdate("INSERT into snp VALUES('" + pseudoname + "','" + snp_id + "'," + allele1 + "," + weight1 + "," + allele2 + "," + weight2 + ")");
                    
                    
                    //writer.println("The first line");
                    //writer.println("The second line");
                    
                    
                    //if(val==1)
                    //    System.out.print("Successfully inserted value");
            }
        }catch(IOException e) {
            e.printStackTrace();
        }finally{
            try{
                if(br != null) br.close();
                writer.close();
            }catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}