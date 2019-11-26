package org.ao.robopaint.read;

import org.ao.robopaint.image.LineImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public interface LineImageReader {
    LineImage read(Path path) throws IOException;
}
