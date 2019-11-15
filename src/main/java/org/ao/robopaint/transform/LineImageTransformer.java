package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

public interface LineImageTransformer<T extends LineImage> {
    T transform(T lineImage);
}
