import performance.RPrecision;

import java.io.File;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_TO_GROUND_TRUTH = PATH_DEFAULT_STEMMER + "cran_Ground_Truth.tsv";
    private static final String COUNTSCORER_TEXT_AND_TITLE_DEFAULT = PATH_DEFAULT_STEMMER + "output_cstt_default.tsv";
    private static final String COUNTSCORER_TEXT_DEFAULT = PATH_DEFAULT_STEMMER + "output_cstext_default.tsv";
    private static final String COUNTSCORER_TITLE_DEFAULT = PATH_DEFAULT_STEMMER + "output_cstitle_default.tsv";
    private static final String TFIDF_TEXT_AND_TITLE_DEFAULT = PATH_DEFAULT_STEMMER + "output_tfidftt_default.tsv";
    private static final String TFIDF_TEXT_DEFAULT = PATH_DEFAULT_STEMMER + "output_tfidftext_default.tsv";
    private static final String TFIDF_TITLE_DEFAULT = PATH_DEFAULT_STEMMER + "output_tfidftitle_default.tsv";
    private static final String BM25_TEXT_AND_TITLE_DEFAULT = PATH_DEFAULT_STEMMER + "output_bm25tt_default.tsv";
    private static final String BM25_TEXT_DEFAULT = PATH_DEFAULT_STEMMER + "output_bm25text_default.tsv";
    private static final String BM25_TITLE_DEFAULT = PATH_DEFAULT_STEMMER + "output_bm25title_default.tsv";

    public static void getFilesInDir() {
        File[] files = new File(PATH_DEFAULT_STEMMER).listFiles();
        for(File file : files) {
            System.out.println(file.getName());
        }

        String filename = "output";
    }

    public static void main(String[] args) {
        RPrecision rp = new RPrecision();
        for(double d : rp.computeAll(PATH_DEFAULT_STEMMER)) {
            System.out.println(d);
        }

    }
}
