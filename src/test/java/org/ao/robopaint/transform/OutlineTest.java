package org.ao.robopaint.transform;

import junit.framework.TestCase;
import org.ao.robopaint.Application;
import org.ao.robopaint.gcode.GCodeLineImageReader;
import org.ao.robopaint.gcode.GCodeLineImageWriter;
import org.ao.robopaint.image.ImageConverter;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.junit.Before;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutlineTest extends TestCase
{
    Application application;

    @Before
    public void setUp() throws Exception {
        application = new Application(1, 600, 600);
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
        Path sourcePath = getClassPathResource("outline.gcode");
        LineImage source = new GCodeLineImageReader().read(sourcePath);
        ReversableLineImageTransformer simplifyTransform = new SimplifyLineImageTransformer();
        LineImage simpleImage = simplifyTransform.transform(source);
        ImageConverter imageConverter = new ImageConverter();
        IndexedLineImage indexedLineImage = imageConverter.convert(simpleImage);
        application.getExportFacade().exportInitial(application.getExportState(), sourcePath,
                imageConverter.convert(source));

        IndexedLineImage indexedLineImageResult = application.getLineImageTransformer().transform(indexedLineImage);
        LineImage simpleResult = imageConverter.convert(indexedLineImageResult);
        LineImage result = simplifyTransform.reverse(simpleResult);

        Path resultPath = Files.createTempFile("outline-result", ".gcode");
        new GCodeLineImageWriter().write(result, resultPath);

        application.getExportFacade().exportResult(
                application.getExportState(), resultPath, imageConverter.convert(result));
    }

    private Path getClassPathResource(String resource) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(resource).toURI());
    }
}
