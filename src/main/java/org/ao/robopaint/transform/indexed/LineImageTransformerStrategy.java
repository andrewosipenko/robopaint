package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.image.indexed.IndexedLineImage;

public interface LineImageTransformerStrategy<T extends IndexedLineImage> {
    void transform(T source, T target);
}
