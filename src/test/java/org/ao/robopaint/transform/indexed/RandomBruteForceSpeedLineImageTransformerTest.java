package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.Application;
import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class RandomBruteForceSpeedLineImageTransformerTest {
    Application application;

    @Before
    public void setUp() throws Exception {
        application = new Application(4, 100, 4);
    }

    @Test
    public void testTwoLines() throws IOException {
        LineImage source = new LineImage(
                new Line(2, 0, 3, 0),
                new Line(0, 0, 1, 0)
        );

        application.getExportFacade().exportInitial(application.getExportState(), source);

        LineImage result = application.getLineImageTransformer().transform(source);

        assertEquals( 0, result.lines[0].x1 );
    }
    @Test
    public void testTwoLinesWithReverse() throws IOException {
        LineImage source = new LineImage(
                new Line(3, 0, 2, 0),
                new Line(0, 0, 1, 0)
        );

        application.getExportFacade().exportInitial(application.getExportState(), source);

        LineImage result = application.getLineImageTransformer().transform(source);

        if (result.lines[0].x1 == 0) {
            assertEquals(3, result.lines[1].x1);
            assertTrue(result.reverse[1]);
        }
        else if (result.lines[0].x1 == 3){
            assertEquals(0, result.lines[1].x1);
            assertTrue(result.reverse[1]);
        }
        else {
            fail();
        }
    }
    @Test
    public void testManyLines() throws IOException {
        int lineCount = 100;

        Line[] lines = new Line[lineCount];
        for(int i = 0; i < lineCount / 2; i++){
            int y = i % 2;
            lines[i * 2] = new Line(i * 2, y, i * 2 + 1, y);
            lines[i * 2 + 1] = new Line(lineCount - i * 2 - 1, y, lineCount - i * 2, y);
        }
        LineImage source = new LineImage(lines);

        application.getExportFacade().exportInitial(application.getExportState(), source);
        LineImage result = application.getLineImageTransformer().transform(source);

        assertEquals( 0, result.lines[0].x1 );
        assertEquals( 1, result.lines[1].x1 );
        assertEquals( 2, result.lines[2].x1 );
    }}