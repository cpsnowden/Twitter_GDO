package com.cps15.AnalyticsService.Analytics;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;

/**
 * Twitter_GDO
 * Created by chris on 16/06/2016.
 */
public class SentimentParser {

    private LMClassifier cls;


    public SentimentParser(String trainingPath) {

        try {
            cls = (LMClassifier) AbstractExternalizable.readObject(new File(trainingPath));
            String[] categories = cls.categories();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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
