package org.ao.robopaint.gcode;

public class GCodeIndexedLineImageReaderTest {
    private GCodeLineImageReader reader = new GCodeLineImageReader();

//    @Test
//    public void testRead() throws URISyntaxException, IOException {
//        IndexedLineImage lineImage = reader.read(getClassPathResource("outline.gcode"));
//        Line line = lineImage.lines[0];
//        assertEquals(125, line.x1);
//        assertEquals(535, line.y1);
//        assertEquals(124, line.x2);
//        assertEquals(535, line.y1);
//
//        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-initial"), 1000, 1000, 2, false);
//        lineImageExporter.export(lineImage, "outline.svg");
//    }
//
//    private Path getClassPathResource(String resource) throws URISyntaxException {
//        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
//    }
}