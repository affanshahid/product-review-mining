package productreviewmining.semanticorientation;

import productreviewmining.util.POS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by affan on 4/18/17.
 */
public class PhraseExtraction {
    private static String[][] PATTERNS = {
            {"JJ", "NN|NNS", "ANYTHING"},
            {"RB|RBR|RBS", "JJ", "NOT_PROPER"},
            {"JJ", "JJ", "NOT_PROPER"},
            {"NN|NNS", "JJ", "NOT_PROPER"},
            {"RB|RBR|RBS", "VB|VBN|VBD|VBG", "ANYTHING"}
    };

    public static String[] getPhrases(String[] sentence) {
        List<String> phrases = new ArrayList<>();
        String[] tags = POS.tagger.tag(sentence);
        //loop over the words in a window of size 3 - 3rd word being optional
        for(int i = 0; i < sentence.length - 2; i++){
            String word = sentence[i];
            String[] tagWindow = new String[3];
            tagWindow[0] = tags[i];
            tagWindow[1] = tags[i + 1];
            if (i == sentence.length - 2)
                tagWindow[2] = "";
            else
                tagWindow[2] = tags[i + 2];

            for(String[] pattern: PATTERNS) {
                int matchCount = 0;
                for(String patternComponent: pattern) {
                    if (matchComponent(tagWindow[matchCount], patternComponent)) {
                        matchCount++;
                    } else {
                        break;
                    }
                }
                // All three pattern components matched - suitable phrase identified
                if (matchCount == 3) {
                    phrases.add(sentence[i] + " " + sentence[i + 1]);
                    break;
                }
            }
        }
        return phrases.toArray(new String[phrases.size()]);
    }

    // Tries to match tag with any of the tags in the component
    private static boolean matchComponent(String tag, String patternComponent) {
        for(String patternTag: patternComponent.split("\\|")) {
            switch (patternTag) {
                case "NOT_PROPER":
                    if (!tag.equals("NN") && !tag.equals("NNS")) return true;
                    break;
                case "ANYTHING": return true;
                default: if (tag.equals(patternTag)) return true;
            }
        }
        return false;
    }
}
