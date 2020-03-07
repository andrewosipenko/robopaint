package org.ao.robopaint.merge;

import org.ao.robopaint.image.indexed.IndexedLineImage;

public interface ImageMerger {
    void merge(IndexedLineImage source1, IndexedLineImage source2, IndexedLineImage target);
}
