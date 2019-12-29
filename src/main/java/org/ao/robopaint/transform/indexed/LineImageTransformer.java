package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.image.indexed.IndexedLineImage;

public interface LineImageTransformer {
    IndexedLineImage transform(IndexedLineImage lineImage);
}
