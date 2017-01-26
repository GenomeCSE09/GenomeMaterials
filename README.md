# GenomeMaterials
In codes folder, there are four subfolders
1. CIDivide: It works as the certified sequencing institute. CIDivide.java file takes input from the dataset files, 
             finds out the SNP_value of the patient and creates weights of SNPs to be distributed among 3 DDBs.
2. Patient: It works as a patient who wants to know the probability to develop any particular target disease. 
            Genetic score determines this probability. It has several GUI forms to interact with the patient. Apart from these, it has
            - Main.java: Starts new 'Client' type object and takes the input disease name from the patient.
            - Client.java: Starts a 'DBProcess' type object and calls 'getAggregatedData' function in DBProcess.
                           Returns the partial genetic score calculated in patient device.
            - DBProcess: getAggregatedData function calculates the patial genetic score by multiplying the weight value stored in
                         patient database with the corresponding beta_division values.
3. DDBs: It works as the distributed databases that are used for secret sharing. It is similar to Patient except that
         it does not contain the GUI files and in Main.java file it does not take any input disease name.
         Other files and functions are same as the Patient folder.
2. Processing Unit: It works as the Medical unit which calculates the genetic score of the patient.
            - Main.java: Starts a thread for 'Processing' type object.
            - Processing.java: Receives the name of the target disaese from patient, initiates an 'Database' type object,
                               and calls 'selectDisease' function of Database.java. It also calculates the time and communication overhead.
            - Database.java: It has several important functions.
                            - selectDisease: uses 'getRelatedSNPs' function to find the SNPs of the target disease and the dummy diseases
                                             and calls 'createMessage' function.
                            - getRelatedSNPs: Finds out the related SNPs, risk allele, and beta values from the database.
                            - createMessage: creates the query message to be sent to the DDBs and the patient by partitioning
                                             all the related SNPs (dummy and target disease) into subsets with various size. 
                            - createBeta: divides original beta values into several parts to ensure privacy of beta values.
                            - geneticScoreCalculation: Receives the partial genetic scores from the patient and the DDBs and
                                             calculates the total genetic score.
