package com.cps15.service.DataService.TwitterStreams;

import com.cps15.api.data.DatasetInfo;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DatasetInfoDAO;
import com.cps15.service.DataService.IDataCollector;
import com.cps15.service.Database.DatabaseManager;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by ChrisSnowden on 23/06/2016.
 */
public class TwitterStreamManager implements IFilterManageMethods, IDataCollector {


    private static final Logger logger = Logger.getLogger(TwitterStreamManager.class.getName());
    private TwitterStream twitterStream;
    private DatasetInfoDAO datasetInfoDAO;
    private DatabaseManager dbm;
    private Map<String, TwitterStreamFilter> filters = new HashMap<>();

    public TwitterStreamManager(DatasetInfoDAO datasetInfoDAO, DatabaseManager databaseManager, Twitter4JAuth twitter4JAuth) {

        System.out.println(twitter4JAuth.getAccessKey());
        System.out.println(twitter4JAuth.getAccessSecret());
        System.out.println(twitter4JAuth.getConsumerKey());
        System.out.println(twitter4JAuth.getConsumerSecret());

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setOAuthConsumerKey(twitter4JAuth.getConsumerKey())
                .setOAuthConsumerSecret(twitter4JAuth.getConsumerSecret())
                .setOAuthAccessToken(twitter4JAuth.getAccessKey())
                .setOAuthAccessTokenSecret(twitter4JAuth.getAccessSecret())
                .setJSONStoreEnabled(true);

        this.dbm = databaseManager;
        this.datasetInfoDAO = datasetInfoDAO;
        twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();

    }

    @Override
    public void start(DatasetInfo datasetInfo) {

        TwitterStreamFilter twitterStreamFilter = new TwitterStreamFilter(datasetInfo,
                dbm.getDb().getCollection(datasetInfo.getId()), this);

        handleStatus(twitterStreamFilter, Status.STATUS.ORDERED);

    }

    @Override
    public boolean stop(DatasetInfo datasetInfo) {

        if (datasetInfo.isRunning()) {

            TwitterStreamFilter twitterStreamFilter = filters.get(datasetInfo.getId());

            if (null == twitterStreamFilter) {
                return false;
            }

            twitterStreamFilter.getDatasetInfo().stopped();
            twitterStreamFilter.finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean restart(DatasetInfo datasetInfo) {

        String id = datasetInfo.getId();

        if (datasetInfo.isRunning() || datasetInfo.isError() || datasetInfo.isError()) {
            return false;
        }

        TwitterStreamFilter twitterStreamFilter = filters.get(id);

        if (twitterStreamFilter == null) {
            start(datasetInfo);
        } else {
            handleStatus(twitterStreamFilter, Status.STATUS.ORDERED);
        }
        return true;
    }

    @Override
    public boolean delete(DatasetInfo datasetInfo) {


        datasetInfoDAO.deleteByID(datasetInfo.getId());
        dbm.getDb().getCollection(datasetInfo.getId()).drop();

        if (dbm.getDb().collectionExists(datasetInfo.getId())) {
            dbm.getDb().getCollection(datasetInfo.getId()).drop();
        }
        return true;
    }

    @Override
    public void handleStatus(TwitterStreamFilter twitterStreamFilter, Status.STATUS status) {

        twitterStreamFilter.getDatasetInfo().setStatus(status);

        switch (status) {
            case ORDERED:
                logger.info("ORDERING " + twitterStreamFilter.getDatasetInfo().getDescription());
                twitterStreamFilter.getDatasetInfo().ordered();
                datasetInfoDAO.update(twitterStreamFilter.getDatasetInfo());
                addFilter(twitterStreamFilter);
                break;
            case ERROR:
                logger.severe("ERROR " + twitterStreamFilter.getDatasetInfo().getDescription());
                twitterStreamFilter.getDatasetInfo().error();
                removeFilter(twitterStreamFilter);
                twitterStreamFilter.getDatasetInfo().setFilterSize(twitterStreamFilter.getStatusDAO().getCollection().count());
                break;
            case FINISHED:
                logger.info("FINISHING " + twitterStreamFilter.getDatasetInfo().getDescription());
                twitterStreamFilter.getDatasetInfo().finished();
                twitterStreamFilter.getDatasetInfo().setFilterSize(twitterStreamFilter.getStatusDAO().getCollection().count());
                removeFilter(twitterStreamFilter);
                break;
            case STOPPED:
                logger.info("STOPPING " + twitterStreamFilter.getDatasetInfo().getDescription());
                twitterStreamFilter.getDatasetInfo().stopped();
                removeFilter(twitterStreamFilter);
                twitterStreamFilter.getDatasetInfo().setFilterSize(twitterStreamFilter.getStatusDAO().getCollection().count());
                break;
            default:
                logger.severe("Why are we here!");
                return;
        }

        datasetInfoDAO.update(twitterStreamFilter.getDatasetInfo());

    }

    private synchronized void removeFilter(TwitterStreamFilter twitterStreamFilter) {

        twitterStream.removeListener(twitterStreamFilter.getStatusListener());
        filters.remove(twitterStreamFilter.getDatasetInfo().getId());

        if (filters.isEmpty()) {
            twitterStream.shutdown();
        } else {
            twitterStream.filter(getFilterTerms());
        }

    }

    private synchronized void addFilter(TwitterStreamFilter twitterStreamFilter) {

        filters.put(twitterStreamFilter.getDatasetInfo().getId(), twitterStreamFilter);
        twitterStream.addListener(twitterStreamFilter.getStatusListener());

        Thread filterRunner = new Thread(twitterStreamFilter);
        filterRunner.start();

        twitterStream.filter(getFilterTerms());
        datasetInfoDAO.update(twitterStreamFilter.getDatasetInfo().running());

    }

    private String[] getFilterTerms() {

        List<String> filterTerms = filters.values().stream()
                .map(TwitterStreamFilter::getStreamingTerms)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        logger.warning("Stream terms " + filterTerms);

        return filterTerms.toArray(new String[filterTerms.size()]);
    }


}
