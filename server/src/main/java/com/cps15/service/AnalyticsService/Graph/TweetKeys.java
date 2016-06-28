package com.cps15.service.AnalyticsService.Graph;

import com.mongodb.DBObject;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Twitter_GDO
 * Created by chris on 16/06/2016.
 */
public class TweetKeys {

    private String sourceKey;
    private String targetKey;
    private String sourceLabel;
    private String targetLabel;
    private String edgeLabel;

    public TweetKeys(String sourceKey, String targetKey, String sourceLabel, String targetLabel, String edgeLabel) {
        this.sourceKey = sourceKey;
        this.targetKey = targetKey;
        this.sourceLabel = sourceLabel;
        this.targetLabel = targetLabel;
        this.edgeLabel = edgeLabel;
    }

    public TweetKeys(String sourceKey, String targetKey) {
        this(sourceKey, targetKey, null, null, null);
    }

    public List<String> getKeys() {
        return Arrays.asList(sourceKey,
                targetKey,
                sourceLabel,
                targetLabel,
                edgeLabel).stream().filter(p->p != null).collect(Collectors.toList());
    };

    private static String getValue(String key, DBObject document) {

        String[] subKeys = key.split("[.]");
        Object value = document.get(subKeys[0]);
        for (int i = 1; i < subKeys.length; i++) {
            value = ((DBObject) value).get(subKeys[i]);
        }
        return value.toString();

    }

    public TweetKeys getValues(DBObject document) {

        return new TweetKeys(getValue(sourceKey,document),
                getValue(targetKey,document),
                getValue(sourceLabel,document),
                getValue(targetLabel,document),
                getValue(edgeLabel, document));

    }

    public String getSourceKey() {
        return sourceKey;
    }

    public String getTargetKey() {
        return targetKey;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public String getTargetLabel() {
        return targetLabel;
    }

    public String getEdgeLabel() {
        return edgeLabel;
    }
}
