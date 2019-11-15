package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;

public abstract class DefaultLineImageTransformer<T extends LineImage> implements LineImageTransformer<T> {
    private LineImageTransformerStrategy lineImageTransformerStrategy;

    public DefaultLineImageTransformer(LineImageTransformerStrategy lineImageTransformerStrategy) {
        this.lineImageTransformerStrategy = lineImageTransformerStrategy;
    }

    @Override
    public T transform(T lineImage) {
        T result = createLineImage(lineImage.lines.length);
        lineImageTransformerStrategy.transform(lineImage, result);
        return result;
    }

    protected abstract T createLineImage(int lineCount);
}
