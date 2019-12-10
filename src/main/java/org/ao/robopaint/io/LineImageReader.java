package org.ao.robopaint.io;

import org.ao.robopaint.image.LineImage;

import java.io.IOException;
import java.nio.file.Path;

public interface LineImageReader {
    LineImage read(Path path) throws IOException;
}
