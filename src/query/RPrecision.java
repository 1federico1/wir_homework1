package query;

import data.ReadFile;

import java.util.List;
import java.util.Map;

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
     * Computes the R-precision for a single query, compared with the Ground Truth (relevant documents) query results.
     * @param pathToGroundTruth
     * @param pathToQueryFile
     * @param queryId
     * @return the value of the R-precision
     */
    public double computeRPrecisionForSingleQuery(String pathToGroundTruth, String pathToQueryFile, int queryId) {
        Map<Integer, List<Integer>> groundTruth = rf.getQuery2relevantDoc(pathToGroundTruth);
        Map<Integer, List<Integer>> query = rf.getQuery2relevantDoc(pathToQueryFile);
        List<Integer> groundTruthQuery = groundTruth.get(queryId);
        int numberOfRelevantDocuments = groundTruthQuery.size();
        List<Integer> retrievedDocuments = query.get(queryId);
        /*
        Cutoff the list of returned documents at the position indicated by the size of the relevant documents set.
         */
        retrievedDocuments = retrievedDocuments.subList(0, numberOfRelevantDocuments);
        int numberOfRetrievedDocuments = 0;
        /*
        For each retrieved document check if it is present in the relevant document set
         */
        for (int docId : retrievedDocuments) {
            if (groundTruthQuery.contains(docId)) {
                numberOfRetrievedDocuments++;
            }
        }
        if (numberOfRelevantDocuments == 0.0) {
            return 0.0;
        } else
            return (double)numberOfRetrievedDocuments / (double)numberOfRelevantDocuments;
    }

    /**
     * Computes the R-precision for all the queries in the file passed as parameter, compared with the Ground Truth
     * query results.
     * @param pathToGroundTruth
     * @param pathToQueryFile
     * @return the average of all the R-precisions computed among the retrieved documents.
     */
    public double averageRPrecision(String pathToGroundTruth, String pathToQueryFile) {
        ReadFile rf = new ReadFile();
        Map<Integer, List<Integer>> groundTruth = rf.getQuery2relevantDoc(pathToGroundTruth);
        double count = 0.0;
        for (int queryId : groundTruth.keySet()) {
            count += this.computeRPrecisionForSingleQuery(pathToGroundTruth, pathToQueryFile, queryId);
        }
        return count / (double) groundTruth.keySet().size();
    }

}
