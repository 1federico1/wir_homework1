import performance.NMCGD;
import performance.RPrecision;

import java.io.File;

/**
 * Created by federico on 4/1/17.
 */
public class Main {

    private static final String PATH_DEFAULT_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/default/";
    private static final String PATH_ENGLISH_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stemmer";
    private static final String PATH_STOPWORD_STEMMER = "/home/federico/Dropbox/intellij/wir_homework1/" +
            "Cranfield_DATASET/stopword_stemmer";

    public static void main(String[] args) {
        System.out.println(PATH_DEFAULT_STEMMER);
        NMCGD nmcgd = new NMCGD();
        nmcgd.getResults(PATH_DEFAULT_STEMMER,1,3,5,10);
        RPrecision rp = new RPrecision();
        rp.computeAll(PATH_DEFAULT_STEMMER);
        System.out.println(PATH_ENGLISH_STEMMER);
        nmcgd.getResults(PATH_ENGLISH_STEMMER,1,3,5,10);
        rp.computeAll(PATH_ENGLISH_STEMMER);
        System.out.println(PATH_STOPWORD_STEMMER);
        nmcgd.getResults(PATH_STOPWORD_STEMMER,1,3,5,10);
        rp.computeAll(PATH_STOPWORD_STEMMER);

    }
}
