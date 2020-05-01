package org.ao.robopaint.transform;

import junit.framework.TestCase;
import org.ao.robopaint.AbstractTest;
import org.ao.robopaint.Application;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutlineTest extends AbstractTest {
    private Application application;

    @Before
    public void setUp() throws Exception {
        application = new Application(1, 600, 600,
                "transform: translate(60px, 0px) scale(1.2) rotateX(180deg)", ITERATION_COUNT);
    }

    @Test
    public void testOutlineLineMerge() throws URISyntaxException, IOException {
        Path source = getClassPathResource("outline.gcode");

        application.getgCodeOptimizer().execute(source);
    }

    @Test
    public void testAgSpiralM0LineMerge() throws URISyntaxException, IOException {
        Path source = getClassPathResource("AgSpiralM0.gcode");
        application.getgCodeOptimizer().execute(source);
    }

    private Path getClassPathResource(String resource) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
    }
}
