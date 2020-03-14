package org.ao.robopaint.merge;

import org.ao.robopaint.image.LineImage;

public interface ImageMerger {
    void merge(LineImage source1, LineImage source2, LineImage target);
}
