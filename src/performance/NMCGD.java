package performance;

import data.ReadFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by federico on 4/3/17.
 */
public class NMCGD extends Query {
    private ReadFile rf;
    private Map<Integer, List<Integer>> query2relevantDocuments;
    private static final String PATH_TO_GROUND_TRUTH = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/cran_Ground_Truth.tsv";
    private static final Logger LOGGER = Logger.getLogger(NMCGD.class.getName());

    public NMCGD (){
        rf = new ReadFile();
        this.query2relevantDocuments = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    private double computeSingleQuery(String pathToQueryFile, int queryId, int k) {
        Map<Integer, List<Integer>> query2retrievedDocuments = rf.getQueryIdRetrievedDocuments(pathToQueryFile);
        List<Integer> relevantDocuments = query2relevantDocuments.get(queryId);
        List<Integer> retrievedDocuments = query2retrievedDocuments.get(queryId);
        double MDCG = relevance(retrievedDocuments.get(0),relevantDocuments);
        // aggiungere gestione caso in cui la query non ritorna risultati
        double logBaseTwoK = Math.log10(k)/Math.log(2.);
        for( int i = 2 ; i<= k; i++){
            MDCG += relevance(retrievedDocuments.get(i), relevantDocuments) / logBaseTwoK;
        }
        double MaximumMDCG = 1 + (double)(k-1)/logBaseTwoK;// k/log2k
        return MDCG/MaximumMDCG;

    }

    //La media va fatta?
/*    public double averageNMCGD(String pathToGroundTruth, String pathToQueryFile, int k) {
        double count = 0.;
        for(int queryId : this.query2relevantDocuments.keySet()) {
            count += this.computeSingleQuery(pathToGroundTruth,pathToQueryFile,queryId, k);
        }
        return count / (double) this.query2relevantDocuments.keySet().size();
    }*/

   /* public List<Double> computeAll(String pathToStemmer, int k) {
        List<Double> results = new ArrayList<>();
        String relevantDocumentsFileName = this.rf.getRelevantDocumentsQueryPath(pathToStemmer, "Ground_Truth");
        for(String fileName : this.rf.getQueryFiles(pathToStemmer, "output")) {
            double nmcgd = this.averageNMCGD(relevantDocumentsFileName, fileName, k);
            Logger.getAnonymousLogger().info("Computing NMCGD of"+fileName+": "+nmcgd);
            results.add(nmcgd);
        }
        return results;
    }*/

   private Map<Integer, Double> computeNMCDGForAllTheQueries(String pathToStemmer, int k) {
       Map<Integer, Double> result = null;
       double nmcgd;
       for(String pathToQuery : this.rf.getQueryFiles(pathToStemmer)) {
           result = new HashMap<>();
           LOGGER.info("Computing NMCGD of"+pathToQuery+", k = "+k);
           for(Integer queryId : this.query2relevantDocuments.keySet()) {
               nmcgd = this.computeSingleQuery(pathToQuery, queryId, k);
               result.put(queryId, nmcgd);
           }

           // compute the mean for all the queries in the file
           double mean = 0.;
           for(int key:result.keySet()) {
              // LOGGER.info(key+":"+result.get(key)); per vedere tutti i valori per ciascuna query
               mean += result.get(key);
           }
           LOGGER.info(String.valueOf((mean/(double)result.keySet().size())));
       }
       return result;
   }

   public void getResults(String pathToStemmer, int... val) {
       for(int k : val) {
           this.computeNMCDGForAllTheQueries(pathToStemmer, k);
       }
   }



}

