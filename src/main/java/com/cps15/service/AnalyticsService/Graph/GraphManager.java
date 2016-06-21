package com.cps15.service.AnalyticsService.Graph;

import com.cps15.service.AnalyticsService.Analytics.SentimentParser;
import com.cps15.service.Database.DatabaseManager;
import com.cps15.service.Database.StatusDAO;
import org.apache.commons.io.FilenameUtils;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.graph.GiantComponentBuilder;
import org.gephi.graph.api.*;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.mongojack.DBProjection;
import org.mongojack.DBQuery;
import org.openide.util.Lookup;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class GraphManager {

    private static final Logger logger = Logger.getLogger(GraphManager.class.getName());
    private DatabaseManager dbm;
    private ProjectController pc;
    private Workspace workspace;

    public GraphManager(String database) {

        this.dbm = new DatabaseManager(database);
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        workspace = pc.getCurrentWorkspace();
    }

    private void enrichGraph(GraphModel graphModel) {
        sentimentAnalysis(graphModel);
    }

    private void sentimentAnalysis(GraphModel graphModel) {

        SentimentParser sentimentParser = new SentimentParser("classifier.txt");

        Column edgeSentiment = graphModel.getEdgeTable().addColumn("Sentiment", int.class);
        for(Edge e: graphModel.getGraph().getEdges()) {
            int score = sentimentParser.getSentimentScore(e.getLabel());
            e.setAttribute(edgeSentiment, score);
        }

        UndirectedGraph undirectedGraph = graphModel.getUndirectedGraph();
        Column nodeSentiment = graphModel.getNodeTable().addColumn("NodeSentiment", int.class);
        for(Node n : undirectedGraph.getNodes()) {
            int score = undirectedGraph.getEdges(n).toCollection().stream().mapToInt(e ->
                (int) e.getAttribute(edgeSentiment)
            ).sum();
            n.setAttribute(nodeSentiment,score);
        }
    }


    public boolean filter(GraphModel graphModel) {

        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        GiantComponentBuilder giantFilterBuilder = new GiantComponentBuilder();
        Query query = filterController.createQuery(giantFilterBuilder.getFilter(this.workspace));
        graphModel.setVisibleView(filterController.filter(query));

        return true;
    }

    public String getRetweetGraph(String collectionName, int maxNumber, int layoutDuration) {

        StatusDAO statusDAO = new StatusDAO(dbm.getDb().getCollection(collectionName));

        logger.info("Getting graph");
        RetweetFunction retweetFunction = new RetweetFunction();

        List<String> keys = retweetFunction.getKeys();

        DBQuery.Query query = DBQuery.empty();
        for(String k:keys) {
            DBQuery.and(query,DBQuery.notEquals(k,null));
        }


        Stream<twitter4j.Status> statusStream = statusDAO.getStream(query, DBProjection.include(keys.toArray(new String[keys.size()]))
                ,maxNumber);

        GraphModel graphModel = retweetFunction.getGraphCreator(statusStream).getGraphModel(workspace);
        logger.info("Got graph");
        filter(graphModel);

        GraphLayout graphLayout = new GraphLayout(graphModel);
        graphLayout.runLayout(layoutDuration, GraphLayout.LayoutAlgorithm.FORCEATLAS2);

        enrichGraph(graphModel);

        return exportTo(GraphFormat.GRAPHML, FilenameUtils.removeExtension(collectionName));
    }

    public void close() {
        this.pc.closeCurrentWorkspace();
        this.pc.closeCurrentProject();
    }

    public String exportTo(GraphFormat format, String filePath) {

        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);

        switch (format) {
            case GRAPHML:
                return exportGraphML(exportController, filePath);
            default:
                logger.severe("Invalid format for exporter");
                return "";
        }

    }

    public enum GraphFormat {
        GRAPHML
    }

    private String exportGraphML(ExportController exportController, String filePath) {

        GraphExporter graphExporter = (GraphExporter) exportController.getExporter("graphml");
        graphExporter.setExportVisible(true);
        graphExporter.setWorkspace(this.workspace);
        String fullFilePath = filePath + ".graphml";
        try {
            exportController.exportFile(new File(fullFilePath), graphExporter);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        logger.info("Written file to " + fullFilePath);

        return fullFilePath;
    }




}
