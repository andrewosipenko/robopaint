package org.ao.robopaint;

import org.ao.robopaint.export.*;
import org.ao.robopaint.transform.LineImageTransformer;
import org.ao.robopaint.transform.indexed.RandomBruteForceSpeedLineImageTransformer;

import java.io.IOException;
import java.util.Map;

public class Application {
    private int scale;
    private int width;
    private int height;
    private Colorer lineColorer;
    private ReportGenerator reportGenerator;
    private ExportFacade exportFacade;
    private ExportState exportState;
    private LineImageTransformer lineImageTransformer;


    public Application(int scale, int width, int height, String transform) throws IOException {
        lineColorer = new FixedColorer("#000000");
        reportGenerator = new ReportGenerator(scale, width, height, transform);
        Colorer rainbowColorer = new RainbowColorer();
        exportFacade = new ExportFacade(
                new SvgImageExporter(width, height, lineColorer,  Colorer.NOOP_COLORER, true),
                Map.of(
                        Rendering.MOVE, new SvgImageExporter(width, height, lineColorer,  new FixedColorer("#7CFC00"), false),
                        Rendering.GRADIENT, new SvgImageExporter(width, height, new NoOpColorer(), new GradientColorer(), true),
                        Rendering.RAINBOW, new SvgImageExporter(width, height, rainbowColorer, rainbowColorer, true)
                ),
                reportGenerator
        );
        exportState = exportFacade.createState();
        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(1000, 20000, width, height, 0.1, 5, exportFacade, exportState);
    }

    public int getScale() {
        return scale;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Colorer getLineColorer() {
        return lineColorer;
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public ExportFacade getExportFacade() {
        return exportFacade;
    }

    public ExportState getExportState() {
        return exportState;
    }

    public LineImageTransformer getLineImageTransformer() {
        return lineImageTransformer;
    }
}
