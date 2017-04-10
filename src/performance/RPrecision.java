package performance;

import data.ReadFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * R-Precision: Requires a set of known relevant documents Rel, from which we calculate the precision of the top Rel
 * documents returned.
 * <p>
 */
public class RPrecision extends Query {
    private ReadFile rf;
    private static final String PATH_TO_GROUND_TRUTH = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/cran_Ground_Truth.tsv";
    private Map<Integer, List<Integer>> query2relevantDocuments;
    private static final Logger LOGGER = Logger.getLogger(RPrecision.class.getName());


    public RPrecision() {
        this.rf = new ReadFile();
        this.query2relevantDocuments = this.rf.getQueryIdRetrievedDocuments(PATH_TO_GROUND_TRUTH);
    }

    /**
     * Computes the R-precision for a single performance, compared with the Ground Truth (relevant documents) performance results.
     * @param pathToQueryFile
     * @param queryId
     * @return the value of the R-precision
     */
    private double computeSingleQuery(String pathToQueryFile, int queryId) {
        Map<Integer, List<Integer>> query2retrievedDocuments = rf.getQueryIdRetrievedDocuments(pathToQueryFile);
        List<Integer> relevantDocuments = query2relevantDocuments.get(queryId);
        int numberOfRelevantDocuments = relevantDocuments.size();
        List<Integer> retrievedDocuments = query2retrievedDocuments.get(queryId);
        /*
        Cutoff the list of returned documents at the position indicated by the size of the relevant documents set.
         */
        retrievedDocuments = retrievedDocuments.subList(0, numberOfRelevantDocuments);
        /*
        For each retrieved document check if it is present in the relevant document set
         */
        int numberOfRetrievedDocuments = 0;
        for (int docId : retrievedDocuments) {
            numberOfRetrievedDocuments += relevance(docId, relevantDocuments);
        }
        if (numberOfRelevantDocuments == 0)
            return 0.0;
        else
            return (double) numberOfRetrievedDocuments / (double) numberOfRelevantDocuments;
    }


    /**
     * Computes the R-precision for all the queries in the file passed as parameter, compared with the Ground Truth
     * performance results.
     *
     * @param pathToQueryFile
     * @return the average of all the R-precisions computed among the retrieved documents.
     */
    private double averageRPrecision(String pathToQueryFile) {
        double count = 0.0;
        for (int queryId : query2relevantDocuments.keySet()) {
            count += this.computeSingleQuery(pathToQueryFile, queryId);
        }
        return count / (double) query2relevantDocuments.keySet().size();
    }

    /**
     * Computes the average R-precision of all the query results in the given stemmer folder.
     *
     * @param pathToStemmer
     * @return
     */
    public Map<String, Double> computeAll(String pathToStemmer) {
        Map<String, Double> results = new HashMap<>();
        for (String filePath : this.rf.getQueryFiles(pathToStemmer)) {
            double rprecision = this.averageRPrecision(filePath);
            String fileName = filePath.replaceAll(pathToStemmer, "");
            System.out.println("Computing R-Precision of " + fileName + ": " + rprecision);
            results.put(fileName, rprecision);
        }
        return results;
    }

    public void printMeans(String pathToStemmer) {
        System.out.println("AVERAGE R-PRECISION OF " + pathToStemmer);
        double csMean = 0.;
        double tfidfMean = 0.;
        double bm25Mean = 0.;
        Map<String, Double> results = this.computeAll(pathToStemmer);
        for (String fileName : results.keySet()) {
            if (fileName.contains("cs")) {
                csMean += results.get(fileName);
            } else if (fileName.contains("tfidf")) {
                tfidfMean += results.get(fileName);
            } else if (fileName.contains("bm25")) {
                bm25Mean += results.get(fileName);
            }
        }

        System.out.println("COUNTER SCORER = " + (csMean / 3.));
        System.out.println("TFIDF = " + (tfidfMean / 3.));
        System.out.println("BM25 = " + (bm25Mean / 3.));
    }

}
