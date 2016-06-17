package com.cps15.AnalyticsService.Analytics;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;
import com.cps15.AnalyticsService.Graph.GraphLayout;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 16/06/2016.
 */
public class SentimentParser {

    private LMClassifier cls;
    private static final Logger logger = Logger.getLogger(SentimentParser.class.getName());

    public SentimentParser(String trainingPath) {

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(trainingPath).getFile());
            System.out.println(file.getAbsolutePath());
            cls = (LMClassifier) AbstractExternalizable.readObject(file);
            String[] categories = cls.categories();

            for(int i = 0; i < categories.length; ++i) {
                System.out.println(categories[i]);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        logger.info("Trained");
    }

    private String getSentimentClass(String subject) {
        ConditionalClassification classification = cls.classify(subject);
        return classification.bestCategory();

    }

    public int getSentimentScore(String subject) {
        switch (getSentimentClass(subject)) {
            case "neg":
                return -1;
            case "pos":
                return 1;
            case "neu":
                return 0;
            default:
                return 0;
        }
    }
}
