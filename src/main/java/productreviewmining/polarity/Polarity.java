package productreviewmining.polarity;

import productreviewmining.langmodels.EmpiricalUnigramLanguageModel;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import productreviewmining.util.Tokenizers;



/**
 * Created by affan on 4/17/17.
 */
public class Polarity {
    private static String CORPUS_PATH = "./build/resources/main/corpus/";

    private static EmpiricalUnigramLanguageModel objModel;
    private static EmpiricalUnigramLanguageModel subModel;

    static {
        try {
            objModel =
                    createUnigramLanguageModel(Paths.get(CORPUS_PATH, "objective.txt"), StandardCharsets.UTF_8);

            subModel =
                    createUnigramLanguageModel(Paths.get(CORPUS_PATH, "subjective.txt"), StandardCharsets.ISO_8859_1);


        } catch(IOException ex) {
            System.err.println("Error during polarity model initialization. Unrecoverable quitting.");
            System.err.println(ex);
            System.exit(1);
        }
    }

    public static double calculateSentencePolarity(List<String> sent) {
        return Math.log(subModel.getSentenceProbability(sent)/objModel.getSentenceProbability(sent));
    }

    public static double calculateSentencePolarity(String sent) {
        List<String> tokenizedSent = Arrays.asList(Tokenizers.tokenizer.tokenize(sent));
        return calculateSentencePolarity(tokenizedSent);
    }

    private static EmpiricalUnigramLanguageModel createUnigramLanguageModel(Path filepath, Charset charset) throws IOException{
        List<String> lines = Files.readAllLines(filepath, charset);
        Collection<List<String>> sentences = new ArrayList<>();
        for (String line : lines) {
            sentences.add(Arrays.asList(Tokenizers.tokenizer.tokenize(line)));
        }
        return new EmpiricalUnigramLanguageModel(sentences);
    }



    // prevent object initialization
    private Polarity() {}
}
