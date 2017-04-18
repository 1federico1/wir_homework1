package performance;

import data.ReadFile;
import java.util.HashMap;
import java.util.LinkedList;
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
        this.rf = ReadFile.getInstance();
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
        int min = Math.min(retrievedDocuments.size(), relevantDocuments.size());
        List<Integer> cutOff = new LinkedList();
        for(int i = 1; i <= min; i++)
            cutOff.add(retrievedDocuments.get(i-1));
        /*
        For each retrieved document check if it is present in the relevant document set
         */
        double numberOfRetrievedDocuments = 0.;
        for (int docId : cutOff) {
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
    public double averageRPrecision(Map<Integer, List<Integer>> map) {
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
        this.rf.init(ReadFile.getPathDefaultStemmer());
        computeAll();
        System.out.println("ENGLISH STEMMER");
        this.rf.init(ReadFile.getPathEnglishStemmer());
        computeAll();
        System.out.println("STOPWORD STEMMER");
        this.rf.init(ReadFile.getPathStopwordStemmer());
        computeAll();
    }

}
