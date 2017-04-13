package data;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by federico on 4/1/17.
 */

public class ReadFile {

    private static ReadFile instance = null;

    private static final String PATH_TO_BM25TEXT = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25text.tsv";
    private static final String PATH_TO_BM25TITLE = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/output_bm25title.tsv";
    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer/";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer/";

    private Map<Integer, List<Integer>> groundTruth;

    private Map<Integer, Map<Integer, Double>> bm25StopwordTextRanking;

    private Map<Integer, Map<Integer, Double>> bm25StopwordTitleRanking;

    public Map<Integer, List<Integer>> getGroundTruth() {
        return groundTruth;
    }

    public Map<String, Map<Integer, List<Integer>>> getFiles() {
        return files;
    }

    public ReadFile() {
        this.groundTruth = this.getQueryIdRetrievedDocuments("/home/federico/Dropbox/intellij/wir_homework1/" +
                "Cranfield_DATASET/default/cran_Ground_Truth.tsv");
    }

    public static ReadFile getInstance() {
        if (instance == null)
                instance = new ReadFile();
        return instance;
    }

    private Map<String, Map<Integer, List<Integer>>> files;

    public void init(String path) {
        Map<Integer, List<Integer>> csTextAndTitle = this.getQueryIdRetrievedDocuments(path + "output_cstt.tsv");
        Map<Integer, List<Integer>> csText = this.getQueryIdRetrievedDocuments(path + "output_cstext.tsv");
        Map<Integer, List<Integer>> csTitle = this.getQueryIdRetrievedDocuments(path + "output_cstitle.tsv");
        Map<Integer, List<Integer>> tfidfTextAndTitle = this.getQueryIdRetrievedDocuments(path + "output_tfidftt.tsv");
        Map<Integer, List<Integer>> tfidfText = this.getQueryIdRetrievedDocuments(path + "output_tfidftext.tsv");
        Map<Integer, List<Integer>> tfidfTitle = this.getQueryIdRetrievedDocuments(path + "output_tfidftitle.tsv");
        Map<Integer, List<Integer>> bm25TextAndTitle = this.getQueryIdRetrievedDocuments(path + "output_bm25tt.tsv");
        Map<Integer, List<Integer>> bm25Text = this.getQueryIdRetrievedDocuments(path + "output_bm25text.tsv");
        Map<Integer, List<Integer>> bm25Title = this.getQueryIdRetrievedDocuments(path + "output_bm25title.tsv");
        this.files = new LinkedHashMap<>();
        this.files.put("csTextAndTitle", csTextAndTitle);
        this.files.put("csText", csText);
        this.files.put("csTitle", csTitle);
        this.files.put("tfIdfTextAndTitle", tfidfTextAndTitle);
        this.files.put("TfIdfText", tfidfText);
        this.files.put("TfIdfTitle", tfidfTitle);
        this.files.put("BM25TextAndTitle", bm25TextAndTitle);
        this.files.put("BM25Text", bm25Text);
        this.files.put("BM25Title", bm25Title);
    }

    private List<String[]> readFile(String path) {
        List<String[]> result = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            br.readLine();
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] split = currentLine.split("\\s+");
                result.add(split);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fr != null)
                    fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * For each query ID the list of retrieved documents ID is provided.
     *
     * @param path of the file containing the results.
     * @return a map whit query ID as key and a list of corresponding retrieved documents as value.
     */
    public Map<Integer, List<Integer>> getQueryIdRetrievedDocuments(String path) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (String[] line : this.readFile(path)) {
            int queryId = Integer.parseInt(line[0]);
            int relevantDocId = Integer.parseInt(line[1]);
            this.checkMap(result, queryId, relevantDocId);
        }
        return result;
    }

    public Map<Integer, Map<Integer, Double>> getQuery2ranking(String path) {
        Map<Integer, Map<Integer, Double>> query2ranking = new LinkedHashMap<>();
        for (String[] line : this.readFile(path)) {
            int queryID = Integer.parseInt(line[0]);
            if (query2ranking.containsKey(queryID)) {
                Map<Integer, Double> docId2score = query2ranking.get(queryID);
                int docId = Integer.parseInt(line[1]);
                double score = Double.parseDouble(line[3]);
                docId2score.put(docId, score);
                query2ranking.put(queryID, docId2score);
            } else {
                Map<Integer, Double> docId2score = new LinkedHashMap<>();
                int docId = Integer.parseInt(line[1]);
                double score = Double.parseDouble(line[3]);
                docId2score.put(docId, score);
                query2ranking.put(queryID, docId2score);
            }
        }
        return query2ranking;
    }

    private void checkMap(Map<Integer, List<Integer>> map, int queryId, int relevantDocId) {
        if (map.containsKey(queryId)) {
            List<Integer> documents = map.get(queryId);
            if (!documents.contains(relevantDocId))
                documents.add(relevantDocId);
            map.put(queryId, documents);
        } else {
            map.put(queryId, new ArrayList<>(relevantDocId));
        }
    }

    public Map<Integer, Map<Integer, Double>> getBm25StopwordTextRanking() {
        if (bm25StopwordTextRanking == null)
            return bm25StopwordTextRanking = this.getQuery2ranking(PATH_TO_BM25TEXT);
        else
            return bm25StopwordTextRanking;
    }

    public Map<Integer, Map<Integer, Double>> getBm25StopwordTitleRanking() {
        if (bm25StopwordTitleRanking == null)
            return bm25StopwordTitleRanking = this.getQuery2ranking(PATH_TO_BM25TITLE);
        else
            return bm25StopwordTitleRanking;
    }

    public static String getPathDefaultStemmer() {
        return PATH_DEFAULT_STEMMER;
    }

    public static String getPathEnglishStemmer() {
        return PATH_ENGLISH_STEMMER;
    }

    public static String getPathStopwordStemmer() {
        return PATH_STOPWORD_STEMMER;
    }
}
