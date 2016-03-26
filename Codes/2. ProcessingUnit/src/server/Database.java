package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Database {

    private Connection conPr = null;
    private Statement stPr = null;
    private ResultSet rsPr = null;
    String construct = "";
    //int L = 3;              //L-Diversity. Total number of disease goes to database. 1 -> target Disease . (L-1)-> other randomly selected disease.
    int L = 5;
    int diseaseCount = 11;
    int targetDiseaseId;
    String snp_id[][] = new String[L][100];
    int snp_count[] = new int[15];
    String risk_allele[][] = new String[L][100];
    float beta[][] = new float[L][100];    
    int included[] = new int[L];
    
    ArrayList<int[]> subsetList=new ArrayList<>();
    int subsetP = 0;
    ArrayList<Integer> subsetDiseaseList = new ArrayList<>();
    int maxSubsetSize = 5;
    
    int maxSubBetaSize = 10;  // change
    
      
    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conPr = DriverManager.getConnection("jdbc:mysql://localhost:3306/processing", "root", "");
            stPr = (Statement) conPr.createStatement();
            
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min; // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
        return randomNum;
    }

    public static double randDouble(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }

    public String getData() {
        try {
            String query = "select * from snp_list";
            rsPr = stPr.executeQuery(query);

            //define a structure having structure of 3 collum: snp_id, risk allele, odds_ratio
            if (rsPr.next()) {
                String snp_id = rsPr.getString("snp_id");
                String risk_allele = rsPr.getString("risk_allele");
                String beta = rsPr.getString("odds_ratio");
                construct = construct + snp_id + "," + risk_allele + "," + beta;

            }
            while (rsPr.next()) {
                construct = construct + ";";
                String snp_id = rsPr.getString("snp_id");
                String risk_allele = rsPr.getString("risk_allele");
                String beta = rsPr.getString("odds_ratio");
                construct = construct + snp_id + "," + risk_allele + "," + beta;
            }
        } catch (Exception ex) {

        }
        return construct;
    }

    public String getRelatedSNPs(String diseaseName, int serialNo, int diseaseId) {
        try {
            String query;
            if (diseaseId == 0) {       //for target disease
                query = "select * from snp_list inner join disease on snp_list.disease_id = disease.id where disease.name = '" + diseaseName + "'";
            } else {                    //for dummy disease
                query = "select * from snp_list inner join disease on snp_list.disease_id = disease.id where disease.id = " + diseaseId;
            }
            rsPr = stPr.executeQuery(query);
            int i = 0;
            String tempRA = "";
            //define a structure having structure of 3 collum: snp_id, risk allele, beta_value(odds ratio)
            if (rsPr.next()) {
                targetDiseaseId = Integer.parseInt(rsPr.getString("disease_id"));
                snp_id[serialNo][i] = rsPr.getString("snp_id");
                if(rsPr.getString("risk_allele").equals("A")){
                    tempRA = "00";
                }
                else if(rsPr.getString("risk_allele").equals("C")){
                    tempRA = "01";
                }
                else if(rsPr.getString("risk_allele").equals("G")){
                    tempRA = "10";
                }
                else if(rsPr.getString("risk_allele").equals("T")){
                    tempRA = "11";
                }
                risk_allele[serialNo][i] = tempRA;
                beta[serialNo][i] = Float.parseFloat(rsPr.getString("beta"));
                construct = construct + snp_id[serialNo][i] + "," + risk_allele[serialNo][i] + "," + beta[serialNo][i];
            }
            while (rsPr.next()) {
                i++;
                construct = construct + ";";
                snp_id[serialNo][i] = rsPr.getString("snp_id");
//                risk_allele[serialNo][i] = rsPr.getString("risk_allele");
                if(rsPr.getString("risk_allele").equals("A")){
                    tempRA = "00";
                }
                else if(rsPr.getString("risk_allele").equals("C")){
                    tempRA = "01";
                }
                else if(rsPr.getString("risk_allele").equals("G")){
                    tempRA = "10";
                }
                else if(rsPr.getString("risk_allele").equals("T")){
                    tempRA = "11";
                }
                risk_allele[serialNo][i] = tempRA;
                beta[serialNo][i] = Float.parseFloat(rsPr.getString("beta"));
                construct = construct + snp_id[serialNo][i] + "," + risk_allele[serialNo][i] + "," + beta[serialNo][i];
            }

            snp_count[serialNo] = i + 1;
            System.out.println(serialNo + " " + diseaseId + " " + snp_count[serialNo]);
        } catch (Exception ex) {

        }
        return construct;
    }
    
    public String getClinicalData (String dName){
        String c = "";
        
        return c;
    }

    public String selectDisease(String dName) {
        String temp = getRelatedSNPs(dName, 0, 0);      // getRelatedSNPs(String diseaseName, int serialNo, int diseaseId)
        included[0] = targetDiseaseId;
        for (int p = 1; p < L; p++) {
            included[p] = 0;
        }
        int flag = 0;
        int r, i = 1;
        while (i < L) {
            r = randInt(1, diseaseCount);
            for (int j = 0; j < L; j++) {
                if (r == included[j]) {
                    flag = 1;
                    break;
                }
            }

            if (flag == 0) {
                String temp1 = getRelatedSNPs("dummy", i, r);
                included[i] = r;
                i++;
            }
            flag = 0;
        }
       
        for (int j = 0; j < L; j++) {
            partition (0, snp_count[j]-1, j);
        }
        return createMessage();
    }

    public void partition (int s, int e, int d){
        if ((e-s+1) <=maxSubsetSize ){
            subsetDiseaseList.add(d);
//            System.out.print("subset" + subsetP + ": ");
            int a1[] = new int [e-s+1];
            for (int i = s; i<=e; i++){
                a1[i-s] = i;
            }
            subsetList.add (a1);
//            for (int i =0; i<subsetList.get(subsetP).length;i++){
//                System.out.print(subsetList.get(subsetP)[i] + " ");
//            }
            subsetP++;
//            System.out.println("\n" + "disease serialNo:" + d);
        }
        else {            
            int r = randInt (s+2,e-1);            
//            System.out.println(s + " " + e + " " + r);
            partition (s, (r-1), d);
            partition (r, e, d);
        }
    }
    
    public String createOriginalMessage (){
        String a = "";
        for (int i= 0; i<snp_count[0]; i++){
            a += snp_id[0][i] + "," + risk_allele[0][i] + "," + beta[0][i];
        }        
        return a;
    }
    
    public String createMessage(){
        int r;
        int betaSerial[] = new int [maxSubBetaSize];
        float tempBeta[] = new float [maxSubBetaSize];        
        
        while(!subsetList.isEmpty()){
            for (int j=0;j<maxSubBetaSize; j++){
                betaSerial[j] = j;
            }        
            r = randInt (0, subsetList.size()-1);
            betaSerial = shuffle (betaSerial, maxSubBetaSize);
//            for (int k = 0 ; k<maxSubBetaSize; k++)
//                System.out.print(betaSerial[k]);
//            System.out.println();
            for (int i =0; i<subsetList.get(r).length; i++){
                Main.message += snp_id[subsetDiseaseList.get(r)][subsetList.get(r)[i]] + "," + risk_allele[subsetDiseaseList.get(r)][subsetList.get(r)[i]] ;
                tempBeta = createBeta (beta[subsetDiseaseList.get(r)][subsetList.get(r)[i]]);
                for (int k=0; k<maxSubBetaSize; k++){
                    Main.message += "," + tempBeta[betaSerial[k]];
                    if (i == 0){
                        if(subsetDiseaseList.get(r) == 0 && betaSerial[k]<5){ // beta value = 3 , change other beta  *************************************
                            Main.marker.add(Boolean.TRUE);
                        }
                        else{
                            Main.marker.add(Boolean.FALSE);
                        }
                    }
                }
                if (i<(subsetList.get(r).length -1)){
                    Main.message+= ":";
                }            
            }
            Main.message += "|";
            subsetList.remove(r);
            subsetDiseaseList.remove(r);
        }   
//        System.out.println(Main.message);
//        System.out.println(Main.marker);
        return Main.message;
    }
    
    public int[] shuffle (int ar[], int n){
        int j, tmp;
        for (int i = n-1; i>0; i--){
            j = randInt (0, i);
            tmp = ar[j];
            ar[j] = ar[i];
            ar[i] = tmp;            
        }
        return ar;
    }

    public float[] createBeta(float beta) {
        float[] dBeta = new float[maxSubBetaSize];
//        dBeta[0] = beta;
        dBeta[0] = (float) randDouble(0, beta);
        dBeta[1] = (float) randDouble(0, (beta - dBeta[0]));
        dBeta[2] = (float) randDouble(0, (beta - dBeta[1]));
        dBeta[3] = (float) randDouble(0, (beta - dBeta[2]));
        dBeta[4] = beta - (dBeta[0] + dBeta[1] + dBeta[2] + dBeta[3]);
       
        dBeta[5] = (float) randDouble(0, beta / 2);
        dBeta[6] = (float) randDouble(0, beta / 2);
        dBeta[7] = (float) randDouble(0, beta / 2);
        dBeta[4] = (float) randDouble(0, beta / 2);
        dBeta[9] = (float) randDouble(0, beta / 2);
        
        return dBeta;
    }
    
    public float genecticScoreCalculation (){
        String partialScore[];
        float sum = 0;
        for (int i=0; i<Main.returnMessages.size(); i++){
            partialScore = Main.returnMessages.get(i).split("\\:");
//            System.out.println("partial Score length " + partialScore.length);
//            System.out.println("marker size " + Main.marker.size());
            if(partialScore.length == Main.marker.size()){
                for (int j = 0; j<partialScore.length; j++){
                    if (Objects.equals(Main.marker.get(j), Boolean.TRUE)){
                        sum += Float.parseFloat(partialScore[j]);
//                        System.out.println (sum);
                    }
                }
//                System.out.println(sum);
            }
            else{
                System.out.println("marker array not matched");
            }
        }        
        return sum;
    }
}
/*
 class diseaseSnpIdDiseaseAllele {
 private String snpID;
 private int diseaseAllele;

 // constructor
 public diseaseSnpIdDiseaseAllele(String name, int code) {
 this.name = name;
 this.code = code;
 }

 // getter
 public String getName() { return name; }
 public int getCode() { return code; }
 // setter

 public void setName(String name) { this.name = name; }
 //public void setCode(String code) { this.code = code; }
 }
 */
