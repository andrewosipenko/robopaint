package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.Application;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class RandomBruteForceSpeedLineImageTransformerTest {
    Application application;

    @Before
    public void setUp() throws Exception {
        application = new Application(4, 100, 4);
    }

    @Test
    public void testTwoLines() throws IOException {
        PointIndex pointIndex = createSequentialPointIndex(4);
        IndexedLineImage source = new IndexedLineImage(pointIndex, 2);
        source.set(0, 2, 3);
        source.set(1, 0, 1);
        application.getExportFacade().exportInitial(application.getExportState(), source);

        IndexedLineImage result = application.getLineImageTransformer().transform(source);

        application.getExportFacade().exportResult(application.getExportState(), null, result);

        assertEquals( 0, result.getStart(0) );
    }

    @Test
    public void testTwoLinesWithReverse() throws IOException {
        PointIndex pointIndex = createSequentialPointIndex(4);
        IndexedLineImage source = new IndexedLineImage(pointIndex, 2);
        source.set(0, 3, 2);
        source.set(1, 0, 1);
        application.getExportFacade().exportInitial(application.getExportState(), source);

        IndexedLineImage result = application.getLineImageTransformer().transform(source);

        application.getExportFacade().exportResult(application.getExportState(), null, result);

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
        application.getExportFacade().exportInitial(application.getExportState(), source);

        IndexedLineImage result = application.getLineImageTransformer().transform(source);

        application.getExportFacade().exportResult(application.getExportState(), null, result);

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