package productreviewmining.util;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;

import java.io.File;
import java.io.IOException;

/**
 * Created by affan on 4/18/17.
 */
public class POS {
    private static String RESOURCE_PATH = "./build/resources/main/";

    public static POSTagger tagger;

    static {
        try {
            tagger =
                    new POSTaggerME(new POSModel(new File(RESOURCE_PATH + "/models/en-pos-maxent.bin")));

        } catch(IOException ex) {
            System.err.println("Error during POS initialization. Unrecoverable quitting.");
            System.err.println(ex);
            System.exit(1);
        }
    }

    private POS() {}
}
