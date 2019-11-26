package org.ao.robopaint.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ao.robopaint.export.LineImageExporter;
import org.ao.robopaint.export.SvgRainbowImageExporter;
import org.ao.robopaint.gcode.GCodeLineImageReader;
import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

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
//        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(10000, 100, 3);
        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(10000, 600, 600);
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

    public void testOutlineNoLineMerge() throws URISyntaxException, IOException {
        LineImage lineImage = new GCodeLineImageReader().read(getClassPathResource("outline.gcode"));

        LineImage result = lineImageTransformer.transform(lineImage);
    }

    public void testOutlineLineMerge() throws URISyntaxException, IOException {
        LineImage source = new GCodeLineImageReader().read(getClassPathResource("outline.gcode"));
        ReversableLineImageTransformer simplifyTransform = new SimplifyLineImageTransformer();
        LineImage simpleImage = simplifyTransform.transform(source);
        LineImage simpleResult = lineImageTransformer.transform(simpleImage);
        LineImage result = simplifyTransform.reverse(simpleResult);

        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-result"), 1000, 1000, 2);
        lineImageExporter.export(result, "outline-result.svg");
    }

    private Path getClassPathResource(String resource) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
    }
}
