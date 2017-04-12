package performance;

import data.ReadFile;
import data.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static data.Utility.*;

/**
 * R-Precision: Requires a set of known relevant documents Rel, from which we calculate the precision of the top Rel
 * documents returned.
 * <p>
 */
public class RPrecision {
    private ReadFile rf;

    public RPrecision() {
        this.rf = new ReadFile();
    }

    /**
     * Computes the R-precision for a single performance, compared with the Ground Truth (relevant documents) performance results.
     *
     * @param queryId
     * @return the value of the R-precision
     */
    public double computeSingleQuery(Map<Integer, List<Integer>> map, int queryId) {
        List<Integer> relevantDocuments = this.rf.getGroundTruth().get(queryId);
        List<Integer> retrievedDocuments = map.get(queryId);
        double numberOfRelevantDocuments = relevantDocuments.size();
        /*
        Cutoff the list of returned documents at the position indicated by the size of the relevant documents set.
         */
        retrievedDocuments = retrievedDocuments.subList(0, (int)numberOfRelevantDocuments);
        /*
        For each retrieved document check if it is present in the relevant document set
         */
        double numberOfRetrievedDocuments = 0.;
        for (int docId : retrievedDocuments) {
            numberOfRetrievedDocuments += relevance(docId, relevantDocuments);
        }
        if (numberOfRelevantDocuments == 0.)
            return 0.;
        else {
            return numberOfRetrievedDocuments / numberOfRelevantDocuments;
        }
    }


    /**
     * Computes the R-precision for all the queries in the file passed as parameter, compared with the Ground Truth
     * performance results.
     *
     * @return the average of all the R-precisions computed among the retrieved documents.
     */
    private double averageRPrecision(Map<Integer, List<Integer>> map) {
        double count = 0.0;
        for (int queryId : this.rf.getGroundTruth().keySet()) {
            count += this.computeSingleQuery(map, queryId);
        }
        return count / (double) this.rf.getGroundTruth().keySet().size();
    }

    /**
     * Computes the average R-precision of all the query results in the given stemmer folder.
     *
     * @return
     */
    public Map<String, Double> computeAll() {
        Map<String, Double> results = new HashMap<>();
        for (String file : this.rf.getFiles().keySet()) {
            double rprecision = this.averageRPrecision(this.rf.getFiles().get(file));
            System.out.println("Computing R-Precision of " + file + ": " + rprecision);
            results.put(file, rprecision);
        }
        return results;
    }

    public void computeValuesForAllTheStemmers() {
        System.out.println("DEFAULT STEMMER");
        this.rf.init(Utility.PATH_DEFAULT_STEMMER);
        computeAll();
        System.out.println("ENGLISH STEMMER");
        this.rf.init(PATH_ENGLISH_STEMMER);
        computeAll();
        System.out.println("STOPWORD STEMMER");
        this.rf.init(PATH_STOPWORD_STEMMER);
        computeAll();
    }

}
