package com.cps15.service.AnalyticsService.Graph;

import com.cps15.api.data.Analytics;
import com.cps15.api.data.Status;
import com.cps15.api.persistence.AnalyticsDAO;
import com.cps15.service.AnalyticsService.Analytics.SentimentParser;
import com.cps15.service.Database.DatabaseManager;
import com.cps15.service.Database.StatusDAO;
import com.google.common.io.ByteStreams;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.gephi.project.api.Project;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import javax.ws.rs.core.StreamingOutput;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ChrisSnowden on 25/06/2016.
 */
public class TwitterGraphManager implements Runnable {

    private Map<String, GraphManager> runners;
    private DatabaseManager databaseManager;
    private AnalyticsDAO analyticsDAO;
    private ProjectController projectController;
    private SentimentParser sentimentParser;
    private Project project;
    private GridFS gridFS;

    private BlockingQueue<Analytics> analyticsQueue = new LinkedBlockingQueue<>();

    public TwitterGraphManager(AnalyticsDAO analyticsDAO, DatabaseManager databaseManager) {

        this.analyticsDAO = analyticsDAO;
        this.databaseManager = databaseManager;
        this.projectController = Lookup.getDefault().lookup(ProjectController.class);
        this.projectController.newProject();
        this.project = this.projectController.getCurrentProject();
        this.sentimentParser = new SentimentParser("classifier.txt");
        gridFS = new GridFS(databaseManager.getAltDb("Analytics"),"Graphs");
    }

    public void orderGraph(Analytics analytics) {

        try {
            analyticsQueue.put(analytics);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        while(true) {

            Analytics analytics;
            try {
                analytics = analyticsQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            analytics.setStatus(Status.STATUS.RUNNING);
            analyticsDAO.update(analytics);


            StatusDAO statusDAO = new StatusDAO(databaseManager.getDb().getCollection(analytics.getDatasetId()));

            Workspace workspace = projectController.newWorkspace(project);
            projectController.openWorkspace(workspace);

            GraphManager graphManager = new GraphManager(statusDAO, workspace, analytics, sentimentParser);
            File file = graphManager.getRetweetGraph();

            try {
                GridFSInputFile gridFSInputFile = gridFS.createFile(file);
                gridFSInputFile.save();

            } catch (IOException e) {
                e.printStackTrace();
            }


            analytics.setDataPath(file.getName());
            analytics.setStatus(Status.STATUS.FINISHED);
            analyticsDAO.update(analytics);
        }

    }

    public StreamingOutput getGraph(Analytics analytics) {

        System.out.println(analytics.getDataPath());
        GridFSDBFile file = gridFS.findOne(analytics.getDataPath());
        return outputStream -> ByteStreams.copy(file.getInputStream(), outputStream);


    }


    public boolean delete(Analytics analytics) {

        gridFS.remove(analytics.getDataPath());
        return analyticsDAO.deleteByID(analytics.getId()) > 0;

    }



}
