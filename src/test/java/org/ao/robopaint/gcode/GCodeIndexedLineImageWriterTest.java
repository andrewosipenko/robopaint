package org.ao.robopaint.gcode;

public class GCodeIndexedLineImageWriterTest {
    private GCodeLineImageReader reader = new GCodeLineImageReader();
    private GCodeLineImageWriter writer = new GCodeLineImageWriter();
//    @Test
//    public void testWrite() throws URISyntaxException, IOException {
//        IndexedLineImage lineImage = reader.read(getClassPathResource("outline.gcode"));
//        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("initial"), 1000, 1000, 2, false);
//        lineImageExporter.export(lineImage, "outline1.svg");
//
//        Path output = Path.of("test-output.gcode");
//        writer.write(lineImage, output);
//
//        IndexedLineImage lineImage2 = reader.read(output);
//        lineImageExporter = new SvgRainbowImageExporter(Paths.get("output"), 1000, 1000, 2, false);
//        lineImageExporter.export(lineImage2, "outline2.svg");
//    }
//
//    private Path getClassPathResource(String resource) throws URISyntaxException {
//        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
//    }
}