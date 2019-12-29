package org.ao.robopaint.app;

import org.ao.robopaint.gcode.GCodeLineImageReader;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.image.indexed.IndexedLineImage;

import java.io.IOException;
import java.nio.file.Path;

public class OptimizeGCodeApplication {
    public Path optimize(Path gcodeSource) throws IOException {
//        LineImage source = new GCodeLineImageReader().read(gcodeSource);
//        ReversableLineImageTransformer simplifyTransform = new SimplifyLineImageTransformer();
//        IndexedLineImage simpleImage = simplifyTransform.transform(source);
//        IndexedLineImage simpleResult = lineImageTransformer.transform(simpleImage);
//        IndexedLineImage result = simplifyTransform.reverse(simpleResult);
//
//        LineImageExporter lineImageExporter = new SvgRainbowImageExporter(Paths.get("gcode-result"), 600, 600, 2, false);
//        lineImageExporter.export(result, "outline-result.svg");
//        new GCodeLineImageWriter().write(result, Path.of("outline-result.gcode"));
        return null;
    }
}
