package com.cps15.service.AnalyticsService.Graph;

import com.cps15.api.data.Analytics;
import com.cps15.service.AnalyticsService.Analytics.SentimentParser;
import com.cps15.service.Database.StatusDAO;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.graph.GiantComponentBuilder;
import org.gephi.graph.api.*;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class GraphManager{

    private static final Logger logger = Logger.getLogger(GraphManager.class.getName());

    private Workspace workspace;
    private StatusDAO statusDAO;
    private Analytics analytics;
    private SentimentParser sentimentParser;

    public GraphManager(StatusDAO statusDAO, Workspace workspace, Analytics analytics, SentimentParser sentimentParser) {

        this.statusDAO = statusDAO;
        this.workspace = workspace;
        this.analytics = analytics;
        this.sentimentParser = sentimentParser;
    }

    public File getRetweetGraph() {

        logger.info("Getting retweet graph for " + analytics.getDatasetId());

        RetweetFunction retweetFunction = new RetweetFunction();

        Stream<twitter4j.Status> statusStream = statusDAO.getStream(retweetFunction.getQuery(), analytics.getNodeLimit());
        GraphModel graphModel = retweetFunction.getGraphCreator(statusStream).getGraphModel(workspace);

        logger.info("Constructed retweet graph for " + analytics.getDatasetId());

        if(analytics.isFilterGiant()) {
            filter(graphModel);
        }

        logger.info("Filtered retweet graph for " + analytics.getDatasetId() + " running layout");

        GraphLayout.runLayout(graphModel, analytics.getLayoutTime(), GraphLayout.LayoutAlgorithm.FORCEATLAS2);

        sentimentAnalysis(graphModel);

        return exportTo(GraphFormat.GRAPHML, analytics.getId());
    }

    private void sentimentAnalysis(GraphModel graphModel) {

        Column edgeSentiment = graphModel.getEdgeTable().addColumn("Sentiment", int.class);
        for(Edge e: graphModel.getGraph().getEdges()) {
            e.setAttribute(edgeSentiment, sentimentParser.getSentimentScore(e.getLabel()));
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

    public void filter(GraphModel graphModel) {

        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
        Query query = filterController.createQuery(new GiantComponentBuilder().getFilter(this.workspace));
        graphModel.setVisibleView(filterController.filter(query));

    }


    public File exportTo(GraphFormat format, String filePath) {

        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);

        switch (format) {
            case GRAPHML:
                return exportGraphML(exportController, filePath);
            default:
                logger.severe("Invalid format for exporter");
                return null;
        }

    }


    public enum GraphFormat {
        GRAPHML
    }

    private File exportGraphML(ExportController exportController, String filePath) {

        GraphExporter graphExporter = (GraphExporter) exportController.getExporter("graphml");
        graphExporter.setExportVisible(true);
        graphExporter.setWorkspace(this.workspace);
        String fullFilePath = filePath + ".graphml";

        File file = new File(fullFilePath);
        try {
            exportController.exportFile(file, graphExporter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        logger.info("Written file to " + fullFilePath);

        return file;
    }




}
