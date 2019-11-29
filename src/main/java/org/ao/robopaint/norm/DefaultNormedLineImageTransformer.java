package org.ao.robopaint.norm;

import org.ao.robopaint.norm.NormedLineImage;
import org.ao.robopaint.norm.NormCalculator;
import org.ao.robopaint.norm.NormedLineImageTransformer;
import org.ao.robopaint.transform.DefaultLineImageTransformer;
import org.ao.robopaint.transform.LineImageTransformerStrategy;

public class DefaultNormedLineImageTransformer extends DefaultLineImageTransformer<NormedLineImage> implements NormedLineImageTransformer {
    private NormCalculator normCalculator;

    public DefaultNormedLineImageTransformer(LineImageTransformerStrategy lineImageTransformerStrategy, NormCalculator normCalculator) {
        super(lineImageTransformerStrategy);
        this.normCalculator = normCalculator;
    }

    @Override
    public NormedLineImage transform(NormedLineImage lineImage) {
        NormedLineImage result = super.transform(lineImage);
        if(!result.isNormCalculated()) {
            result.setNorm(normCalculator.calculate(result));
        }
        return result;
    }

    @Override
    protected NormedLineImage createLineImage(int lineCount) {
        return new NormedLineImage(lineCount);
    }
}
