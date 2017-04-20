package productreviewmining.featureextraction;

import com.google.common.io.Files;
import productreviewmining.util.Counter;
import productreviewmining.util.POS;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;


/**
 * Created by affan on 4/19/17.
 */
public class FeatureExtractor {
    private List<String> features;

    public FeatureExtractor(Collection<List<String>> reviewDatabase, int threshold) {
        features = new ArrayList<>();
        train(reviewDatabase, threshold);
    }

    private void train(Collection<List<String>>  reviewDatabase, int threshold) {
        Counter<String> counter = new Counter<>();
        for(List<String> sent: reviewDatabase) {
            String[] tags = POS.tagger.tag((String[])sent.toArray());
            for (int i = 0; i < sent.size(); i++) {
                String word = sent.get(i);
                String tag = tags[i];
                if (tag.startsWith("NN")) {
                    counter.incrementCount(word.toLowerCase(), 1.0);
                }
            }
        }

        for(String key: counter.keySet()) {
            if (counter.getCount(key) >= threshold) {
                features.add(key);
            }
        }
    }

    public List<String> getFeatures() {
        return features;
    }
}
