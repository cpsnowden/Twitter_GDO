package com.cps15.service.DataService;

import com.cps15.api.data.DataFilter;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.DataFilterDAO;
import com.cps15.service.Database.DatabaseManager;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by ChrisSnowden on 23/06/2016.
 */
public class TwitterStreamManager implements IFilterManageMethods {

    private static final String consumerKey = "uxMqOuKT66k0ihHJ9aQRoCq2x";
    private static final String consumerSecret = "zyc5BC1voOMPmJs3G7o1hHv4jI8ii6WGctf4yzdg43xQzxPH0U";
    private static final String accessKey = "529742116-XJ1FPnd2ANhx8UDsv3espl1OAlufhzQT5G6yQDxA";
    private static final String accessSecret = "sHoqlokqwo0OPz2RRBjxBL3E8CKszf9dibi8ao4xLykMS";
    private static final Logger logger = Logger.getLogger(TwitterStreamManager.class.getName());

    private TwitterStream twitterStream;
    private DataFilterDAO dataFilterDAO;
    private DatabaseManager dbm;
    private Map<String, TwitterStreamFilter> filters = new HashMap<>();

    public TwitterStreamManager(DataFilterDAO dataFilterDAO, DatabaseManager databaseManager) {

        this.dbm = databaseManager;
        this.dataFilterDAO = dataFilterDAO;
        twitterStream = TwitterStreamFactory.getSingleton();
        twitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        twitterStream.setOAuthAccessToken(new AccessToken(accessKey, accessSecret));

    }

    public void startFilter(DataFilter dataFilter) {

        TwitterStreamFilter twitterStreamFilter = new TwitterStreamFilter(dataFilter,
                dbm.getDb().getCollection(dataFilter.getId()), this);

        handleStatus(twitterStreamFilter, Status.STATUS.ORDERED);

    }

    public boolean stopFilter(DataFilter dataFilter) {

        if (dataFilter.isRuning()) {

            TwitterStreamFilter twitterStreamFilter = filters.get(dataFilter.getId());

            if (null == twitterStreamFilter) {
                return false;
            }

            twitterStreamFilter.getDataFilter().stopped();
            twitterStreamFilter.finish();
            return true;
        }
        return false;
    }

    public boolean restartFilter(DataFilter dataFilter) {

        String id = dataFilter.getId();

        if (dataFilter.isRuning() || dataFilter.isError() || dataFilter.isError()) {
            return false;
        }

        TwitterStreamFilter twitterStreamFilter = filters.get(id);

        if (twitterStreamFilter == null) {

            twitterStreamFilter = new TwitterStreamFilter(dataFilter, dbm.getDb().getCollection(dataFilter.getId()), this);
            handleStatus(twitterStreamFilter, Status.STATUS.ORDERED);

        }

        return true;
    }

    public boolean deleteCollection(DataFilter dataFilter) {


        dataFilterDAO.deleteByID(dataFilter.getId());
        dbm.getDb().getCollection(dataFilter.getId()).drop();

        if (dbm.getDb().collectionExists(dataFilter.getId())) {
            dbm.getDb().getCollection(dataFilter.getId()).drop();
        }
        return true;
    }

    @Override
    public void handleStatus(TwitterStreamFilter twitterStreamFilter, Status.STATUS status) {

        twitterStreamFilter.getDataFilter().setStatus(status);

        switch (status) {
            case ORDERED:
                logger.info("ORDERING " + twitterStreamFilter.getDataFilter().getDescription());
                twitterStreamFilter.getDataFilter().ordered();
                dataFilterDAO.update(twitterStreamFilter.getDataFilter());
                addFilter(twitterStreamFilter);
                break;
            case ERROR:
                logger.severe("ERROR " + twitterStreamFilter.getDataFilter().getDescription());
                twitterStreamFilter.getDataFilter().error();
                removeFilter(twitterStreamFilter);
                break;
            case FINISHED:
                logger.info("FINISHING " + twitterStreamFilter.getDataFilter().getDescription());
                twitterStreamFilter.getDataFilter().finished();
                removeFilter(twitterStreamFilter);
                break;
            case STOPPED:
                logger.info("STOPPING " + twitterStreamFilter.getDataFilter().getDescription());
                twitterStreamFilter.getDataFilter().stopped();
                removeFilter(twitterStreamFilter);
                break;
            default:
                logger.severe("Why are we here!");
                return;
        }

        dataFilterDAO.update(twitterStreamFilter.getDataFilter());

    }

    private synchronized void removeFilter(TwitterStreamFilter twitterStreamFilter) {

        twitterStream.removeListener(twitterStreamFilter.getStatusListener());
        filters.remove(twitterStreamFilter.getDataFilter().getId());

        if (filters.isEmpty()) {
            twitterStream.shutdown();
        } else {
            twitterStream.filter(getFilterTerms());
        }

    }

    private synchronized void addFilter(TwitterStreamFilter twitterStreamFilter) {

        filters.put(twitterStreamFilter.getDataFilter().getId(), twitterStreamFilter);
        twitterStream.addListener(twitterStreamFilter.getStatusListener());

        Thread filterRunner = new Thread(twitterStreamFilter);
        filterRunner.start();

        twitterStream.filter(getFilterTerms());
        dataFilterDAO.update(twitterStreamFilter.getDataFilter().running());

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
