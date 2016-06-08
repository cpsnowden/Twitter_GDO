package com.cps15;

import org.apache.commons.io.FilenameUtils;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
class GraphManager {

    private static final Logger logger = Logger.getLogger(GraphManager.class.getName());
    private DatabaseManager dbm;
    private ProjectController pc;

    public GraphManager(String databaseName) {

        dbm = new DatabaseManager(databaseName);
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
    }

    public String getRetweetGraph(String collectionName) {
        return getRetweetGraph(collectionName, LayoutEngine.LayoutAlgorithm.FORCEATLAS2, 60);
    }

    public String getRetweetGraph(String collectionName, LayoutEngine.LayoutAlgorithm algorithm, long duration) {

        GraphModel graphModel = dbm.getRetweetGraph(collectionName, pc.getCurrentWorkspace());

        LayoutEngine layoutEngine = new LayoutEngine(pc.getCurrentWorkspace());
        layoutEngine.runLayout(duration, algorithm);

        return layoutEngine.exportTo(LayoutEngine.Format.GRAPHML, FilenameUtils.removeExtension(collectionName));
    }

    public static void printGraphDetails(GraphModel graphModel){

        DirectedGraph directedGraph = graphModel.getDirectedGraph();
        System.out.println("Nodes: " + directedGraph.getNodeCount() + " Edges: " + directedGraph.getEdgeCount());

    }

    public void shutdown() {
        this.pc.closeCurrentProject();
    }


}
