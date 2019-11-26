package org.ao.robopaint.gcode;

import org.ao.robopaint.export.LineImageExporter;
import org.ao.robopaint.export.SvgRainbowImageExporter;
import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class GCodeLineImageReaderTest {
    private GCodeLineImageReader reader = new GCodeLineImageReader();

    @Test
    public void testRead() throws URISyntaxException, IOException {
        LineImage lineImage = reader.read(getClassPathResource("outline.gcode"));
        Line line = lineImage.lines[0];
        assertEquals(125, line.x1);
        assertEquals(535, line.y1);
        assertEquals(124, line.x2);
        assertEquals(535, line.y1);

        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-initial"), 1000, 1000, 2);
        lineImageExporter.export(lineImage, "outline.svg");
    }

    private Path getClassPathResource(String resource) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
    }
}