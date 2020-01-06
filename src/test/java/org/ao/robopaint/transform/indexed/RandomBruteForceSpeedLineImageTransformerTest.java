package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.export.*;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class RandomBruteForceSpeedLineImageTransformerTest {
    LineImageTransformer lineImageTransformer;
    ExportFacade exportFacade;
    ExportState exportState;
    ReportGenerator reportGenerator;

    @Before
    public void setUp() throws Exception {
        int width = 100;
        int height = 4;
        Colorer blackColorer = new FixedColorer("#000000");
        reportGenerator = new ReportGenerator(4, width, height);
        exportFacade = new ExportFacade(
                new SvgImageExporter(width, height, blackColorer,  Colorer.NOOP_COLORER),
                Map.of(
                        Rendering.MOVE, new SvgImageExporter(width, height, blackColorer,  new FixedColorer("#00FF00")),
                        Rendering.GRADIENT, new SvgImageExporter(width, height, blackColorer, new GradientColorer()),
                        Rendering.RAINBOW, new SvgImageExporter(width, height, blackColorer, new RainbowColorer())
                ),
                reportGenerator
        );
        exportState = exportFacade.createState();
        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(10000, 1000, width, height, 1, 5, exportFacade, exportState);
    }

    @Test
    public void testTwoLines() throws IOException {
        PointIndex pointIndex = createSequentialPointIndex(4);
        IndexedLineImage source = new IndexedLineImage(pointIndex, 2);
        source.set(0, 2, 3);
        source.set(1, 0, 1);
        exportFacade.exportInitial(exportState, source);

        IndexedLineImage result = lineImageTransformer.transform(source);

        exportFacade.exportResult(exportState, null, result);

        assertEquals( 0, result.getStart(0) );
    }

    @Test
    public void testTwoLinesWithReverse() throws IOException {
        PointIndex pointIndex = createSequentialPointIndex(4);
        IndexedLineImage source = new IndexedLineImage(pointIndex, 2);
        source.set(0, 3, 2);
        source.set(1, 0, 1);
        exportFacade.exportInitial(exportState, source);

        IndexedLineImage result = lineImageTransformer.transform(source);

        exportFacade.exportResult(exportState, null, result);

        assertEquals(0, result.getStart(0));
        assertEquals(1, result.getEnd(0));

        assertEquals(2, result.getStart(1));
        assertEquals(3, result.getEnd(1));
    }

    @Test
    public void testManyLines() throws IOException {
        int lineCount = 100;
        PointIndex pointIndex = createSequentialPointIndex(lineCount + 1);
        final int y = 2;

        IndexedLineImage source = new IndexedLineImage(pointIndex, lineCount);
        for(int i = 0; i < lineCount / 2; i++){
            source.set(i * 2, i * 2, i * 2 + 1);
            source.set(i * 2 + 1, lineCount - i * 2 - 1, lineCount - i * 2);
        }
        exportFacade.exportInitial(exportState, source);

        IndexedLineImage result = lineImageTransformer.transform(source);

        exportFacade.exportResult(exportState, null, result);

        assertEquals(0, result.getStart(0));
        assertEquals(1, result.getEnd(0));

        assertEquals(2, result.getStart(1));
        assertEquals(3, result.getEnd(1));
    }

    private PointIndex createSequentialPointIndex(int count){
        PointIndex pointIndex = new PointIndex(count);
        for(int i = 0; i < count; i++) {
            pointIndex.set(i, i, 0);
        }
        return pointIndex;
    }
}