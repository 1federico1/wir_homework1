import performance.NMDCG;
import performance.RPrecision;

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
        System.out.println("DEFAULT STEMMER");
        NMDCG NMDCG = new NMDCG();
        NMDCG.getResults(PATH_DEFAULT_STEMMER,1,3,5,10);
        RPrecision rp = new RPrecision();
        rp.printMeans(PATH_DEFAULT_STEMMER);
        System.out.println("ENGLISH STEMMER");
        NMDCG.getResults(PATH_ENGLISH_STEMMER,1,3,5,10);
        rp.printMeans(PATH_ENGLISH_STEMMER);
        System.out.println("STOPWORDS STEMMER");
        NMDCG.getResults(PATH_STOPWORD_STEMMER,1,3,5,10);
        rp.printMeans(PATH_STOPWORD_STEMMER);

    }
}
