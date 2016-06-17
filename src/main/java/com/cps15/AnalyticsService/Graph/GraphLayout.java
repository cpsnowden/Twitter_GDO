package com.cps15.AnalyticsService.Graph;

import org.gephi.filters.api.FilterController;
import org.gephi.filters.plugin.graph.GiantComponentBuilder;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.layout.spi.Layout;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.util.concurrent.TimeUnit;
import java.util.logging.Filter;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class GraphLayout {

    private static final Logger logger = Logger.getLogger(GraphLayout.class.getName());
    private GraphModel graphModel;

    public enum LayoutAlgorithm {
        FORCEATLAS2,
        YIFANHU
    }

    public GraphLayout(GraphModel graphModel) {

        this.graphModel = graphModel;
    }




    public boolean runLayout(long duration, LayoutAlgorithm layoutAlgorithm) {


        logger.info("Starting Layout");
        AutoLayout autoLayout = new AutoLayout(duration, TimeUnit.SECONDS);
        autoLayout.setGraphModel(graphModel);

        Layout layout = getLayout(layoutAlgorithm);

        if (null == layout) {
            logger.severe("Invalid layout algorithm specified");
            return false;
        }
        System.out.println(graphModel.getDirectedGraph().getNodeCount());
        autoLayout.addLayout(layout, 1.0f);
        autoLayout.execute();
        logger.info("Finished running graph layout on " + graphModel);

        return true;
    }

    private Layout getLayout(LayoutAlgorithm layoutAlgorithm) {
        switch (layoutAlgorithm) {
            case FORCEATLAS2:
                ForceAtlas2 forceAtlas2 = new ForceAtlas2(null);
                System.out.println(forceAtlas2.getThreadsCount());
                return forceAtlas2;
            case YIFANHU:
                YifanHuLayout yifanHu =new YifanHuLayout(null, new StepDisplacement(1f));
                yifanHu.setOptimalDistance(100.0f);
                yifanHu.setRelativeStrength(0.2f);
                yifanHu.setInitialStep(20.0f);
                yifanHu.setStepRatio(0.95f);
                yifanHu.setAdaptiveCooling(true);
                yifanHu.setConvergenceThreshold(1.0e-4f);
                yifanHu.setQuadTreeMaxLevel(10);
                yifanHu.setBarnesHutTheta(1.2f);
                return yifanHu;
            default:
                return null;
        }
    }


}
