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
    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer/";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/";
    private static final Logger LOGGER = Logger.getLogger(RPrecision.class.getName());


    public RPrecision() {
        this.rf = new ReadFile();
    }

    /**
     * Computes the R-precision for a single performance, compared with the Ground Truth (relevant documents) performance results.
     * @param queryId
     * @return the value of the R-precision
     */
    private double computeSingleQuery(Map<Integer, List<Integer>> map, int queryId) {
        List<Integer> relevantDocuments = this.rf.getGroundTruth().get(queryId);
        List<Integer> retrievedDocuments = map.get(queryId);
        int numberOfRelevantDocuments = relevantDocuments.size();
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

    public void computeAllTheValues() {
        System.out.println("DEFAULT STEMMER");
        this.rf.init(PATH_DEFAULT_STEMMER);
        computeAll();
        System.out.println("ENGLISH STEMMER");
        this.rf.init(PATH_ENGLISH_STEMMER);
        computeAll();
        System.out.println("STOPWORD STEMMER");
        this.rf.init(PATH_STOPWORD_STEMMER);
        computeAll();
    }

    /*public void printMeans(String pathToStemmer) {
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
    }*/

    @Override
    public Map<String, Integer> compute(String pathToStemmer, int... k) {
        return null;
    }

    @Override
    public double computeSingle(String pathToStememr, int queryId, int... k) {
        return 0;
    }
}
