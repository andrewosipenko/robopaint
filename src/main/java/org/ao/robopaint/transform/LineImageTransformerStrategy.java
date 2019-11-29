package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

public interface LineImageTransformerStrategy<T extends LineImage> {
    void transform(T source, T target);
}
