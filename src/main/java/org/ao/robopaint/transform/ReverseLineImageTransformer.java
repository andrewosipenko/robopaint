package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormCalculator;

public class ReverseLineImageTransformer extends AbstractAreaTransformer {

    public ReverseLineImageTransformer(double ratio, NormCalculator normCalculator) {
        super(ratio, 1, normCalculator);
    }

    @Override
    public LineImage transform(LineImage source) {
        int area = getArea(source);
        int start = getStart(source, area);

        LineImage target = new LineImage(source);
        int end = start + area - 1;
        for(int i = 0; i <= end - start; i++) {
            target.reverse[i + start] = !target.reverse[i + start];
            target.lines[i + start] = source.lines[end - i];
        }

        target.setNorm(normCalculator.calculate(target));
        return target;
    }
}
