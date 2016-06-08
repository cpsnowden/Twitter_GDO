package com.cps15;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.*;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public class GraphConstructor {

    public static final Logger logger = Logger.getLogger(GraphConstructor.class.getName());
    private Container container;
    private ContainerLoader containerLoader;


    public GraphConstructor() {
        this.container = Lookup.getDefault().lookup(Container.Factory.class).newContainer();
        this.containerLoader = container.getLoader();
    }

    public void addNode(String key) {
        addNode(key, key);
    }

    public void addNode(String key, String label) {

        if (!containerLoader.nodeExists(key)) {
            NodeDraft node = containerLoader.factory().newNodeDraft(key);
            node.setLabel(label);
            this.containerLoader.addNode(node);
            logger.fine("New node: " + node.toString());
        } else {
            logger.fine("Duplicate node");
        }
    }


    public void addEdge(String source, String target) {


        if (!containerLoader.edgeExists(source, target)) {


            NodeDraft sourceNode;
            if (!containerLoader.nodeExists(source)) {
                sourceNode = containerLoader.factory().newNodeDraft(source);
                this.containerLoader.addNode(sourceNode);
            } else {
                sourceNode = containerLoader.getNode(source);
            }

            NodeDraft targetNode;
            if (!containerLoader.nodeExists(source)) {
                targetNode = containerLoader.factory().newNodeDraft(target);
                this.containerLoader.addNode(targetNode);
            } else {
                targetNode = containerLoader.getNode(target);
            }

            EdgeDraft edge = containerLoader.factory().newEdgeDraft();
            edge.setSource(sourceNode);
            edge.setTarget(targetNode);
            containerLoader.addEdge(edge);

            logger.fine("Added edge: " + edge.toString());
        }
    }

    public GraphModel getGraphModel(Workspace workspace) {

        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        importController.process(container, new DefaultProcessor(), workspace);
        return Lookup.getDefault().lookup(GraphController.class).getGraphModel(workspace);

    }
}
