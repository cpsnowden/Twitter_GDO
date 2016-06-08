package com.cps15;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.layout.spi.Layout;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 08/06/2016.
 */
public class LayoutEngine {
    private Workspace workspace;
    private static final Logger logger = Logger.getLogger(LayoutEngine.class.getName());

    public enum Format {
        GRAPHML
    }

    public enum LayoutAlgorithm {
        FORCEATLAS2,
        YIFANHU
    }

    public LayoutEngine(Workspace workspace) {

        this.workspace = workspace;
    }

    public String exportTo(Format format, String filePath) {

        ExportController exportController = Lookup.getDefault().lookup(ExportController.class);

        switch (format) {
            case GRAPHML:
                return exportGraphML(exportController, filePath);
            default:
                logger.severe("Invalid format for exporter");
                return "";
        }
    }


    private String exportGraphML(ExportController exportController, String filePath) {

        Exporter exporter = exportController.getExporter("graphml");
        exporter.setWorkspace(this.workspace);
        StringWriter stringWriter = new StringWriter();
        exportController.exportWriter(stringWriter, (CharacterExporter) exporter);
        String fullFilePath = filePath + ".graphml";
        try (FileWriter fw = new FileWriter(fullFilePath)) {
            fw.write(stringWriter.toString());
        } catch (IOException ex) {
            logger.severe("Error when exporting GraphML");
            ex.printStackTrace();
            return "";
        }

        logger.info("Written file to " + fullFilePath);

        return fullFilePath;
    }

    public boolean runLayout(long duration, LayoutAlgorithm layoutAlgorithm) {

        AutoLayout autoLayout = new AutoLayout(duration, TimeUnit.SECONDS);

        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();
        autoLayout.setGraphModel(graphModel);

        Layout layout = getLayout(layoutAlgorithm);

        if (null == layout) {
            logger.severe("Invalid layout algorithm specified");
            return false;
        }

        autoLayout.addLayout(layout, 1.0f);

        autoLayout.execute();
        System.out.println("Finished running layout algorithm");

        System.out.println(graphModel.getDirectedGraph().getNodeCount());


        return true;
    }

    private Layout getLayout(LayoutAlgorithm layoutAlgorithm) {
        switch (layoutAlgorithm) {
            case FORCEATLAS2:
                return new ForceAtlasLayout(null);
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


