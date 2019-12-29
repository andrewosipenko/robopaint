package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.norm.NormCalculator;

public class ReverseLineImageTransformer extends AbstractAreaTransformer implements LineImageTransformer {

    public ReverseLineImageTransformer(double ratio, NormCalculator normCalculator) {
        super(ratio, 1, normCalculator);
    }

    @Override
    public IndexedLineImage transform(IndexedLineImage source) {
        IndexedLineImage target = new IndexedLineImage(source);

        int area = getArea(source);
        int start = getStart(source, area);
        for(int i = start; i < start + area; i++) {
            reverse(target, i);
        }
        target.setNorm(normCalculator.calculate(target));
        return target;
    }

    private void reverse(IndexedLineImage image, int index){
        int start = image.getStart(index);
        int end = image.getEnd(index);
        image.set(index, end, start);
    }
}
