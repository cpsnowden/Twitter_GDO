package com.cps15.service.DataService.TwitterStreams.Filters;

import org.apache.commons.lang3.StringUtils;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ChrisSnowden on 23/06/2016.
 */
public class HashtagFilter implements IStatusFilter {

    private Set<String> terms = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private List<String> hashtags = new ArrayList<>();

    public HashtagFilter(List<String> hashtags) {

        this.hashtags = hashtags;

        terms.addAll(hashtags.stream().map(h->{
            return StringUtils.uncapitalize(h.replace("#",""));
        }).collect(Collectors.toList()));

    }

    @Override
    public boolean consumeStatus(Status status) {

        Set<String> statusHashtags = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        statusHashtags.addAll(Arrays.asList(status.getHashtagEntities())
                                .stream().map(HashtagEntity::getText)
                                .collect(Collectors.toList()));

        return !Collections.disjoint(statusHashtags, terms);
    }

    @Override
    public String[] getStreamingTerms() {
        return hashtags.toArray(new String[hashtags.size()]);
    }

    @Override
    public List<String> getStreamingTermList() {
        return hashtags;
    }
}
