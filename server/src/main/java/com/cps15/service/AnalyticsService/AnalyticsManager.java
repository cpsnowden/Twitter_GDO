package com.cps15.service.AnalyticsService;

import com.cps15.api.data.Analytics;
import com.cps15.api.data.AnalyticsRequest;
import com.cps15.api.persistence.AnalyticsDAO;
import com.cps15.service.AnalyticsService.Graph.TwitterGraphManager;
import com.cps15.service.Database.DatabaseManager;

import javax.ws.rs.core.StreamingOutput;
import java.util.List;

/**
 * Created by ChrisSnowden on 27/06/2016.
 */
public class AnalyticsManager {


    private TwitterGraphManager twitterGraphManager;
    private AnalyticsDAO analyticsDAO;

    public AnalyticsManager(AnalyticsDAO analyticsDAO,
                            DatabaseManager databaseManager) {

        this.analyticsDAO = analyticsDAO;
        twitterGraphManager = new TwitterGraphManager(analyticsDAO, databaseManager);
        Thread thread = new Thread(twitterGraphManager);
        thread.start();

    }

    public Analytics processRequest(AnalyticsRequest analyticsRequest, String dsid) {

        Analytics analytics = new Analytics(analyticsRequest, dsid);

        switch (analytics.getType()){
            case GRAPH:
                twitterGraphManager.orderGraph(analytics);
                break;
            default:

        }

        analyticsDAO.add(analytics);
        return analytics;

    }

    public List<Analytics> getAnalyticsByDSID(String id){

        return analyticsDAO.findByDSID(id);

    }

    public Analytics getAnalyticsById(String id, String dsid) {

        Analytics analytics = analyticsDAO.findByID(id, dsid);

        return analytics;

    }

    public StreamingOutput getDataByID(String id, String dsid) {

        Analytics analytics = analyticsDAO.findByID(id, dsid);

        switch(analytics.getType()) {
            case GRAPH:
                return twitterGraphManager.getGraph(analytics);
            default:
                return null;
        }
    }

    public boolean deleteAnalytics(String id, String dsid) {

        Analytics analytics = analyticsDAO.findByID(id, dsid);
        switch (analytics.getType()) {
            case GRAPH:
                return twitterGraphManager.delete(analytics);
            default:

        }

        return false;
    }


}
