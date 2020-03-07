package org.ao.robopaint.export;

import org.ao.robopaint.image.indexed.IndexedLineImage;

import java.nio.file.Path;

public interface LineImageExporter {
    void export(IndexedLineImage lineImage, Path path);
}
