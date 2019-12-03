package org.ao.robopaint.merge;

import org.ao.robopaint.norm.NormedLineImage;

public interface ImageMerger {
    void merge(NormedLineImage source1, NormedLineImage source2, NormedLineImage target);
}
