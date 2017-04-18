package productreviewmining.util;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Created by affan on 4/17/17.
 */
public class Tokenizers {
    private static String RESOURCE_PATH = "./build/resources/main/";

    public static Tokenizer tokenizer;
    public static SentenceDetector sentenceDetector;

    static {
        try {
            tokenizer =
                    new TokenizerME(new TokenizerModel(new File(RESOURCE_PATH + "models/en-token.bin")));

            sentenceDetector =
                    new SentenceDetectorME(new SentenceModel(new File(RESOURCE_PATH + "models/en-sent.bin")));

        } catch(IOException ex) {
            System.err.println("Error during tokenizer initialization. Unrecoverable quitting.");
            System.err.println(ex);
            System.exit(1);
        }
    }

    private Tokenizers() {}
}
