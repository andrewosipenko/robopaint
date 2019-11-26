package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

public interface ReversableLineImageTransformer<T extends LineImage> extends LineImageTransformer<T> {
    T reverse(T lineImage);
}
