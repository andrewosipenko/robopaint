package org.ao.robopaint.transform;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.NormedLineImage;
import org.ao.robopaint.norm.SpeedNormCalculator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SwapLineImageTransformerStrategy implements LineImageTransformerStrategy<NormedLineImage> {
    private final double distanceRatio;

    public SwapLineImageTransformerStrategy(double distanceRatio) {
        this.distanceRatio = distanceRatio;
    }

    public SwapLineImageTransformerStrategy() {
        distanceRatio = 1;
    }

    @Override
    public void transform(NormedLineImage source, NormedLineImage target) {
        target.clone(source);
        if(source.isNormCalculated()) {
            target.setNorm(source.getNorm());
        }

        Random random = ThreadLocalRandom.current();
        int i1 = random.nextInt(target.lines.length);
        int i2;
        if(distanceRatio == 1) {
            i2 = random.nextInt(target.lines.length);
        }
        else {
            i2 = random.nextInt((int)(target.lines.length * distanceRatio)) + i1;
            if (i2 >= target.lines.length){
                i2 = target.lines.length - 1;
            }
        }
        swap(target, i1, i2);
        if(random.nextBoolean()){
            double oldLeftNorm1 = calculateLeftNorm(target, i2);
            double oldRightNorm1 = calculateRightNorm(target, i2);
            target.reverse[i2] = !target.reverse[i2];
            double newLeftNorm1 = calculateLeftNorm(target, i2);
            double newRightNorm1 = calculateRightNorm(target, i2);
            target.setNorm(target.getNorm() - oldLeftNorm1 - oldRightNorm1 + newLeftNorm1 + newRightNorm1);
        }
    }

    private static void swap(NormedLineImage lineImage, int index1, int index2)
    {
        if(index1 == index2){
            if(index1 < lineImage.lines.length - 1){
                index2 = index1 + 1;
            }
        }
        double oldLeftNorm1 = calculateLeftNorm(lineImage, index1);
        double oldRightNorm1 = calculateRightNorm(lineImage, index1);
        double oldLeftNorm2 = calculateLeftNorm(lineImage, index2);
        double oldRightNorm2 = calculateRightNorm(lineImage, index2);

        Line temp = lineImage.lines[index1];
        lineImage.lines[index1] = lineImage.lines[index2];
        lineImage.lines[index2] = temp;

        boolean reverse =  lineImage.reverse[index1];
        lineImage.reverse[index1] = lineImage.reverse[index2];
        lineImage.reverse[index2] = reverse;
        double newLeftNorm1 = calculateLeftNorm(lineImage, index1);
        double newRightNorm1 = calculateRightNorm(lineImage, index1);
        double newLeftNorm2 = calculateLeftNorm(lineImage, index2);
        double newRightNorm2 = calculateRightNorm(lineImage, index2);
        lineImage.setNorm(lineImage.getNorm() -oldLeftNorm1 - oldRightNorm1 - oldLeftNorm2 - oldRightNorm2
         + newLeftNorm1 + newRightNorm1 + newLeftNorm2 + newRightNorm2);
    }

    private static double calculateLeftNorm(NormedLineImage lineImage, int index){
        return index > 0 ? SpeedNormCalculator.calculateNorm(lineImage.lines[index - 1], lineImage.reverse[index - 1],
                lineImage.lines[index], lineImage.reverse[index]) : 0;
    }
    private static double calculateRightNorm(NormedLineImage lineImage, int index){
        return index < lineImage.lines.length - 1 ? SpeedNormCalculator.calculateNorm(lineImage.lines[index], lineImage.reverse[index],
                lineImage.lines[index + 1], lineImage.reverse[index + 1]) : 0;
    }
}
