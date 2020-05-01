package org.ao.robopaint;

import org.ao.robopaint.export.ExportFacade;
import org.ao.robopaint.export.ExportState;
import org.ao.robopaint.gcode.GCodeLineImageReader;
import org.ao.robopaint.gcode.GCodeLineImageWriter;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.transform.LineImageTransformer;
import org.ao.robopaint.transform.ReversableLineImageTransformer;
import org.ao.robopaint.transform.SimplifyLineImageTransformer;

import java.io.IOException;
import java.nio.file.Path;

public class GCodeOptimizer {
    private ExportFacade exportFacade;
    private ExportState exportState;
    private LineImageTransformer lineImageTransformer;

    public GCodeOptimizer(ExportFacade exportFacade, ExportState exportState, LineImageTransformer lineImageTransformer) {
        this.exportFacade = exportFacade;
        this.exportState = exportState;
        this.lineImageTransformer = lineImageTransformer;
    }

    public Path execute(Path sourcePath) throws IOException {
        LineImage source = new GCodeLineImageReader().read(sourcePath);
        ReversableLineImageTransformer simplifyTransform = new SimplifyLineImageTransformer();
        LineImage simpleImage = simplifyTransform.transform(source);
        exportFacade.exportInitial(exportState, sourcePath, source);

        LineImage indexedLineImageResult = lineImageTransformer.transform(simpleImage);
        LineImage resultImage = simplifyTransform.reverse(indexedLineImageResult);
        Path result = Path.of("outline-result.gcode");
        new GCodeLineImageWriter().write(resultImage, result);

        exportFacade.exportResult(exportState, result, resultImage);

        return result;
    }
}
