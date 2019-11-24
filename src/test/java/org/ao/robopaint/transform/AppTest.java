package org.ao.robopaint.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    LineImageTransformer<LineImage> lineImageTransformer;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) throws IOException {
        super( testName );
        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(10000, 100, 3);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testTwoLines() {
        LineImage source = new LineImage(
                new Line(2, 0, 3, 0),
                new Line(0, 0, 1, 0)
        );

        LineImage result = lineImageTransformer.transform(source);

        assertEquals( 0, result.lines[0].x1 );
    }

    public void testManyLines() {
        int lineCount = 100;
        final int y = 2;

        Line[] lines = new Line[lineCount];
        for(int i = 0; i < lineCount / 2; i++){
            lines[i * 2] = new Line(i * 2, y, i * 2 + 1, y);
            lines[i * 2 + 1] = new Line(lineCount - i * 2 - 1, y, lineCount - i * 2, y);
        }
        LineImage source = new LineImage(lines);

        LineImage result = lineImageTransformer.transform(source);

        assertEquals( 0, result.lines[0].x1 );
    }
}
