package org.ao.robopaint.transform;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.ao.robopaint.transform.indexed.LineImageTransformer;
import org.ao.robopaint.transform.indexed.RandomBruteForceSpeedLineImageTransformer;
import org.junit.Before;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutlineTest extends TestCase
{
    Application application;

    @Before
    public void setUp() throws Exception {
        application = new Application(4, 600, 600);
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
