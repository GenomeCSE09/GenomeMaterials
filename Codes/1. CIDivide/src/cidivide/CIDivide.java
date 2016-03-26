/*
Input: (for n=3)
Pseudonym;SNP_ID;Patient_Possesed_Allele;Alternate_Allele;Orientation
*/
package cidivide;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class CIDivide {

    static int dbNo = 4;
    static int sum[] = new int[2];
    static int extra[] = new int[2];
    static char a[][] = new char[2][dbNo];
    static int w[][] = new int[2][dbNo];

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min; // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
        return randomNum; // return Integer between min and max, inclusive.
    }

    public static void main(String[] args) {

        BufferedReader br = null;
        PrintWriter writer = null;

        try {
            String sCurrentLine;
            String inputfile = "output1010";
            br = new BufferedReader(new FileReader(inputfile + ".txt"));

            while ((sCurrentLine = br.readLine()) != null) {

                String[] parts = sCurrentLine.split(";");
                //char c = s.charAt(0);
                char C = parts[2].charAt(0);
                char V = parts[3].charAt(0);;
                int orient = Integer.parseInt(parts[4]); // 0 - same 1 - different
                char A[] = new char[2];
                int flag[] = new int[2];
                A[0] = C;
                if (orient == 0) {
                    A[1] = C;
                } else {
                    A[1] = V;
                }
                System.out.println(A[0] + " " + A[1]);
                for (int i = 0; i < 2; i++) {
                    sum[i] = 0;
                    extra[i] = 0;
                    flag[i] = 0;
                }
                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < dbNo - 2; i++) {
                        if (flag[j] == 1) {
                            a[j][i] = a[j][i - 1];
                            flag[j] = 0;
                        } else {
                            int r = randInt(0, 100);
                            if (r > 50) {
                                a[j][i] = C;
                            } else {
                                a[j][i] = V;
                            }
                        }
                        //System.out.println("a[" + j + "][" + i + "] = " + a[j][i]);

                        w[j][i] = randInt(-100, 100);
                        //System.out.println("w[" + j + "][" + i + "] = " + w[j][i]);
                        int changed = 0;

                        if (a[j][i] == A[j]) {
                            while (changed != 1) {
                                if ((sum[j] + w[j][i] <= 200) && (sum[j] + w[j][i] >= 0)) {
                                    sum[j] += w[j][i];
                                    //System.out.println("new sum between[0,200] ; sum[" + j + "] = " + sum[j]);
                                    changed = 1;
                                } else if ((sum[j] < 0) && (sum[j] >= -100)) {
                                    w[j][i] = randInt(-sum[j], 100);
                                    //System.out.println("old sum between[-100,0] sum[" + j + "] = " + sum[j]);
                                    sum[j] += w[j][i];
                                //System.out.println("take new w [-sum[j],100] w[" + j + "][" + i + "] = " + w[j][i]);
                                    //System.out.println(" sum[" + j + "] = " + sum[j]);
                                    changed = 1;
                                } else if ((sum[j] + w[j][i] < 0) && (sum[j] + w[j][i] >= -100)) {
                                    if (i != dbNo - 3) {
                                        sum[j] += w[j][i];
                                        //System.out.println("new sum between[0,-100] ; i != dbNo-3 ; new sum[" + j + "] = " + sum[j]);
                                        flag[j] = 1;
                                        changed = 1;
                                    } else {
                                        //System.out.println("new sum between[0,-100] ; i = dbNo-3 ; new sum[" + j + "] = " + (sum[j] + w[j][i]));
                                        w[j][i] = randInt(0, 100);
                                        sum[j] += w[j][i];
                                    //System.out.println("take new w [0,100] w[" + j + "][" + i + "] = " + w[j][i]);
                                        //System.out.println(" sum[" + j + "] = " + sum[j]);
                                        changed = 1;
                                    }
                                } else {
                                    w[j][i] = randInt(-100, 100);
                                    //System.out.println("no condition, not changed ; take new w [-sum[j],100] w[" + j + "][" + i + "] = " + w[j][i]);
                                }
                            }
                        } else {
                            changed = 0;
                            while (changed != 1) {
                                if ((extra[j] + w[j][i] <= 100) && (extra[j] + w[j][i] >= -100)) {
                                    extra[j] += w[j][i];
                                    //System.out.println("new extra between[-100,100] ; extra[" + j + "] = " + extra[j]);
                                    changed = 1;
                                } else {
                                    w[j][i] = randInt(-100, 100);
                                    //System.out.println("no condition, not changed for extra; take new w [-100,100] w[" + j + "][" + i + "] = " + w[j][i]);
                                }
                            }
                        }
                    }
                }

                int last[] = new int[2];
                int last2[] = new int[2];

                for (int j = 0; j < 2; j++) {
                    if (sum[j] <= 100) {
                        last[j] = 100 - sum[j];
                    } else {
                        last[j] = -(sum[j] - 100);
                    }
                    last2[j] = -extra[j];

                    int rand = randInt(0, 100);
                    if (rand > 50) {
                        a[j][dbNo - 2] = A[j];
                        w[j][dbNo - 2] = last[j];
                        if (j == 0) {
                            a[j][dbNo - 1] = V;
                        } else {
                            if (orient == 0) {
                                a[j][dbNo - 1] = V;
                            } else {
                                a[j][dbNo - 1] = C;
                            }
                        }
                        w[j][dbNo - 1] = last2[j];
                    //System.out.println("a[" + j + "][" + (dbNo - 2) + "] = " + a[j][dbNo - 2]);
                        //System.out.println("w[" + j + "][" + (dbNo - 2) + "] = " + w[j][dbNo - 2]);
                        //System.out.println("a[" + j + "][" + (dbNo - 1) + "] = " + a[j][dbNo - 1]);
                        //System.out.println("w[" + j + "][" + (dbNo - 1) + "] = " + w[j][dbNo - 1]);
                    } else {
                        a[j][dbNo - 1] = A[j];
                        w[j][dbNo - 1] = last[j];
                        if (j == 0) {
                            a[j][dbNo - 2] = V;
                        } else {
                            if (orient == 0) {
                                a[j][dbNo - 2] = V;
                            } else {
                                a[j][dbNo - 2] = C;
                            }
                        }
                        w[j][dbNo - 2] = last2[j];
                    //System.out.println("a[" + j + "][" + (dbNo - 2) + "] = " + a[j][dbNo - 2]);
                        //System.out.println("w[" + j + "][" + (dbNo - 2) + "] = " + w[j][dbNo - 2]);
                        //System.out.println("a[" + j + "][" + (dbNo - 1) + "] = " + a[j][dbNo - 1]);
                        //System.out.println("w[" + j + "][" + (dbNo - 1) + "] = " + w[j][dbNo - 1]);
                    }
                }

                String allele1 ="",allele2="";

                for (int j = 0; j < dbNo; j++)
                {
                    String str = inputfile + "_" + (j+1) + ".txt";
                        writer = new PrintWriter(new BufferedWriter(new FileWriter(str, true)));
                        //writer = new PrintWriter(str, "UTF-8");
                        if(a[0][j] == 'A') allele1 = "00";
                        else if(a[0][j] == 'C') allele1 = "01";
                        else if(a[0][j] == 'G') allele1 = "10";
                        else if(a[0][j] == 'T') allele1 = "11";

                        if(a[1][j] == 'A') allele2 = "00";
                        else if(a[1][j] == 'C') allele2 = "01";
                        else if(a[1][j] == 'G') allele2 = "10";
                        else if(a[1][j] == 'T') allele2 = "11";

                        writer.println(parts[0] + ";" + parts[1] + ";" + allele1 + ";" + w[0][j] + ";" + allele2 + ";"+w[1][j] );
                        //System.out.print(a[i][j] + " " + w[i][j] + " ** ");
                        writer.close();


                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
