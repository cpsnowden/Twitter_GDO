package com.cps15.AnalyticsService.Graph;

import com.cps15.Database.DatabaseReader;
import com.cps15.Database.RetweetFunction;
import com.sun.xml.internal.ws.Closeable;
import org.apache.commons.io.FilenameUtils;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * Twitter_GDO
 * Created by chris on 09/06/2016.
 */
public class GraphManager implements Closeable {

    private static final Logger logger = Logger.getLogger(GraphManager.class.getName());
    private DatabaseReader dbm;
    private ProjectController pc;
    private Workspace workspace;



    public GraphManager(String databaseName) {

        dbm = new DatabaseReader(databaseName);
        pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        workspace = pc.getCurrentWorkspace();

    }

    public String getRetweetGraph(String collectionName) {

        GraphModel graphModel = dbm.getGraph(collectionName, workspace, new RetweetFunction());
        GraphLayout graphLayout = new GraphLayout(graphModel);
        graphLayout.runLayout(300, GraphLayout.LayoutAlgorithm.YIFANHU);

        return exportTo(GraphFormat.GRAPHML, FilenameUtils.removeExtension(collectionName));

    }

    @Override
    public void close() {
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


}
