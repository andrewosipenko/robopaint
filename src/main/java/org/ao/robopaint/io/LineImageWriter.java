package org.ao.robopaint.io;

import org.ao.robopaint.image.LineImage;

import java.io.IOException;
import java.nio.file.Path;

public interface LineImageWriter {
    void write(LineImage lineImage, Path path) throws IOException;
}
