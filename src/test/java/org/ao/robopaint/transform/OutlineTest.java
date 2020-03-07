package org.ao.robopaint.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ao.robopaint.export.LineImageExporter;
import org.ao.robopaint.export.SvgRainbowImageExporter;
import org.ao.robopaint.gcode.GCodeLineImageReader;
import org.ao.robopaint.gcode.GCodeLineImageWriter;
import org.ao.robopaint.image.LineImage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unit test for simple App.
 */
public class OutlineTest
    extends TestCase
{
    LineImageTransformer<LineImage> lineImageTransformer;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public OutlineTest(String testName ) throws IOException {
        super( testName );
        lineImageTransformer = new RandomBruteForceSpeedLineImageTransformer(1000, 50000, 600, 600, 0.1, 1, true);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( OutlineTest.class );
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

        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-result"), 600, 600, 2, false);
        lineImageExporter.export(result, "outline-result.svg");
        new GCodeLineImageWriter().write(result, Path.of("outline-result.gcode"));
    }

    private Path getClassPathResource(String resource) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
    }
}
