package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by federico on 4/1/17.
 */

public class ReadFile {

    private Map<Integer, List<Integer>> groundTruth;

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

    private Map<String,Map<Integer, List<Integer>>> files;

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

    public Map<Integer, Double> getDocIdScore(String path, int queryId) {
        Map<Integer, Double> result = new LinkedHashMap<>();
        for (String[] line : this.readFile(path)) {
            if (queryId == Integer.parseInt(line[0])) {
                int docId = Integer.parseInt(line[1]);
                double score = Double.parseDouble(line[3]);
                result.put(docId, score);
            }
        }
        return result;
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


    /**
     * All files are named in the same way, this method returns all the files in the stemmer folder that start with the
     * nameToSearch parameter (usually the naming convention is 'output_scorerFunctionFieldWhereIsApplied.tsv)
     *
     * @param pathToStemmer
     * @return
     */
    public List<String> getQueryFiles(String pathToStemmer) {
        List<String> result = new ArrayList<>();
        File[] files = new File(pathToStemmer).listFiles();
        for (File file : files) {
            if (file.getName().contains("output")) {
                result.add(file.getAbsolutePath());
            }
        }
        return result;
    }
}
