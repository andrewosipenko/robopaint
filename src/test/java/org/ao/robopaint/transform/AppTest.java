package org.ao.robopaint.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

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
    public AppTest( String testName )
    {
        super( testName );
        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(100000);
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

    public void test5Lines() {
        int lineCount = 5;

        Line[] lines = new Line[lineCount];
        for(int i = 0; i < lineCount; i++){
            lines[lineCount - 1 - i] = new Line(i * 2, 0, i * 2 + 1, 0);
        }
        LineImage source = new LineImage(lines);

        LineImage result = lineImageTransformer.transform(source);

        assertEquals( 0, result.lines[0].x1 );
    }
}
