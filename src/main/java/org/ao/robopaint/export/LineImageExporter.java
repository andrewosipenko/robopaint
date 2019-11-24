package org.ao.robopaint.export;

import org.ao.robopaint.image.LineImage;

public interface LineImageExporter {
    void export(LineImage lineImage, String name);
}
