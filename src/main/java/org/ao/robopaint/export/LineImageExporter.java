package org.ao.robopaint.export;

import org.ao.robopaint.image.LineImage;

import java.nio.file.Path;

public interface LineImageExporter {
    void export(LineImage lineImage, Path path);
}
