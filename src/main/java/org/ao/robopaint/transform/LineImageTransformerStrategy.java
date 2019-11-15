package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

public interface LineImageTransformerStrategy {
    void transform(LineImage source, LineImage target);
}
