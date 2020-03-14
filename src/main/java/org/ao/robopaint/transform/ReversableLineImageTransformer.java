package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

public interface ReversableLineImageTransformer extends LineImageTransformer {
    LineImage reverse(LineImage lineImage);
}
