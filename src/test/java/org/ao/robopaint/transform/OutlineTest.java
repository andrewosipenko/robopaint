package org.ao.robopaint.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ao.robopaint.transform.indexed.LineImageTransformer;
import org.ao.robopaint.transform.indexed.RandomBruteForceSpeedLineImageTransformer;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class OutlineTest
    extends TestCase
{
    LineImageTransformer lineImageTransformer;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OutlineTest(String testName ) throws IOException {
        super( testName );
//        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(10000, 10000, 600, 600, 0.1, 1, true);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( OutlineTest.class );
    }


//    public void testOutlineNoLineMerge() throws URISyntaxException, IOException {
//        IndexedLineImage lineImage = new GCodeLineImageReader().read(getClassPathResource("outline.gcode"));
//
//        IndexedLineImage result = lineImageTransformer.transform(lineImage);
//    }
//
//    public void testOutlineLineMerge() throws URISyntaxException, IOException {
//    }
//
//    private Path getClassPathResource(String resource) throws URISyntaxException {
//        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
//    }
}
