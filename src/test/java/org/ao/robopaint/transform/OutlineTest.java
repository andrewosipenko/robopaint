package org.ao.robopaint.transform;

import junit.framework.TestCase;
import org.ao.robopaint.Application;
import org.ao.robopaint.gcode.GCodeLineImageReader;
import org.ao.robopaint.image.ImageConverter;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.image.indexed.IndexedLineImage;
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

    public void testOutlineNoLineMerge() throws URISyntaxException, IOException {
        LineImage lineImage = new GCodeLineImageReader().read(getClassPathResource("outline.gcode"));
        IndexedLineImage indexedLineImage = new ImageConverter().convert(lineImage);

        application.getExportFacade().exportInitial(application.getExportState(), indexedLineImage);

//        IndexedLineImage result = application.getLineImageTransformer().transform(indexedLineImage);
//
//        application.getExportFacade().exportResult(application.getExportState(), null, result);
    }

    public void testOutlineLineMerge() throws URISyntaxException, IOException {
        LineImage source = new GCodeLineImageReader().read(getClassPathResource("outline.gcode"));
        ReversableLineImageTransformer simplifyTransform = new SimplifyLineImageTransformer();
        LineImage simpleImage = simplifyTransform.transform(source);
        IndexedLineImage indexedLineImage = new ImageConverter().convert(simpleImage);
        application.getExportFacade().exportInitial(application.getExportState(), indexedLineImage);

        IndexedLineImage indexedLineImageResult = application.getLineImageTransformer().transform(indexedLineImage);
//        LineImage result = simplifyTransform.reverse(simpleResult);

        application.getExportFacade().exportResult(application.getExportState(), null, indexedLineImageResult);

//        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-result"), 600, 600, 2, false);
//        lineImageExporter.export(result, "outline-result.svg");
//        new GCodeLineImageWriter().write(result, Path.of("outline-result.gcode"));

    }

    public void testAgSpiralM0LineMerge() throws URISyntaxException, IOException {
        LineImage source = new GCodeLineImageReader().read(getClassPathResource("AgSpiralM0.gcode"));
        ReversableLineImageTransformer simplifyTransform = new SimplifyLineImageTransformer();
        LineImage simpleImage = simplifyTransform.transform(source);
        LineImage simpleResult = lineImageTransformer.transform(simpleImage);
        LineImage result = simplifyTransform.reverse(simpleResult);

        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-result"), 600, 600, 2, false);
        lineImageExporter.export(result, "outline-result.svg");
        new GCodeLineImageWriter().write(result, Path.of("outline-result.gcode"));
    }

    public void testAgSpiralM0LineMerge() throws URISyntaxException, IOException {
        LineImage source = new GCodeLineImageReader().read(getClassPathResource("AgSpiralM0.gcode"));
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
