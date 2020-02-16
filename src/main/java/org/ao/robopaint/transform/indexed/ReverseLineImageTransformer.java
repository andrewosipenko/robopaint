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
        for(int i = 0; i < (int)Math.ceil(area / 2.0); i++) {
            reverse(target, start + i, start + area - i - 1);
        }
        target.setNorm(normCalculator.calculate(target));
        return target;
    }

    private void reverse(IndexedLineImage image, int index1, int index2){
        int start1 = image.getStart(index1);
        int end1 = image.getEnd(index1);
        if(index1 == index2) {
            image.set(index1, end1, start1);
        }
        else {
            int start2 = image.getStart(index2);
            int end2 = image.getEnd(index2);

            image.set(index2, end1, start1);
            image.set(index1, end2, start2);
        }
    }
}
