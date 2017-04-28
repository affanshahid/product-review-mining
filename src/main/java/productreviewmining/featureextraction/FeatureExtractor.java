package productreviewmining.featureextraction;

import com.google.common.io.Files;

import opennlp.tools.formats.ad.ADSentenceStream.Sentence;
import productreviewmining.util.Counter;
import productreviewmining.util.POS;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * Created by affan on 4/19/17.
 */
public class FeatureExtractor {
    private List<String> features;

    public FeatureExtractor(Collection<List<String>> reviewDatabase, int threshold) {
        features = new ArrayList<>();
        train(reviewDatabase, threshold);
    }

    private void train(Collection<List<String>> reviewDatabase, int threshold) {
        Counter<String> counter = new Counter<>();
        for (List<String> sent : reviewDatabase) {
            String[] tags = POS.tagger.tag((String[]) sent.toArray());
            for (int i = 0; i < sent.size(); i++) {
                String word = sent.get(i);
                String tag = tags[i];
                if (tag.equals("NN")) {
                    counter.incrementCount(word.toLowerCase(), 1.0);
                }
            }
        }

        for (String key : counter.keySet()) {
            if (counter.getCount(key) >= threshold) {
                features.add(key);
            }
        }
    }

    public List<String> getFeatures() {
        return features;
    }

    // Extracts frequent feature/noun along with nearby opinion word/adjective
    // sentence: Tokenized sentence from which to extract features
    // returns: null if either noun or an opinion word is not found
    //          otherwise an array of indices with the first index representing the position of the noun
    //          and the second index locates the opinion word
    //
    // Logic: The adjective will always follow the noun, so look for a noun then an
    //        adjective in the next BOUND words.
    //        
    //        CURRENTLY DISABLED:
    //        If a sentence contains no feature nouns but any opinion words we select the closest noun as a feature
    public String[] extract(List<String> sentence, int bound) {

        String[] tags = POS.tagger.tag((String[]) sentence.toArray());
        // Assume no nouns or adjectives, init heurisitc vals

        List<Integer> adjecsFound = new ArrayList<>();
        List<Integer> nounsFound = new ArrayList<>();
        int nounIndex = -1;
        int adjIndex = -1;
        for (int i = 0; i < sentence.size(); i++) {
            String word = sentence.get(i);
            String tag = tags[i];

            // Store indices if noun or adj for use if no feature nouns are found
            if (tag.equals("NN")) {
                nounsFound.add(i);
            }

            if (tag.equals("JJ")) {
                adjecsFound.add(i);
            }

            // First look for a noun
            if (nounIndex == -1 && tag.equals("NN") && features.contains(word.toLowerCase())) {
                nounIndex = i;
            }

            // If a noun has been found and an adjective is found...
            if (nounIndex != -1 && tag.equals("JJ")) {
                // ensure it is within the bounds
                // then select it
                if (i - nounIndex < bound) {
                    adjIndex = i;

                    // otherwise discard both the noun and the adjective
                } else {
                    nounIndex = -1;
                }
            }
            if (adjIndex != -1 && nounIndex != -1) {
                return new String[] { sentence.get(nounIndex), sentence.get(adjIndex) };
            }
        }

        // BAD RESULTS
        // no frequent-noun/adj pair found. Select noun closest to any adj and consider it feature
        // for (Integer nounI: nounsFound) {
        //     for (Integer adjI: adjecsFound) {
        //         if (Math.abs(adjI - nounI) < bound) {
        //             return new String[] { sentence.get(nounI), sentence.get(adjI) };
        //         }
        //     }
        // }

        return null;
    }
}
