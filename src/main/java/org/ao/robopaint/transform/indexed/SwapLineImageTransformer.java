package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.norm.NormCalculator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SwapLineImageTransformer implements LineImageTransformer {
    private final double distanceRatio;
    private NormCalculator normCalculator;

    public SwapLineImageTransformer(double distanceRatio, NormCalculator normCalculator) {
        this.distanceRatio = distanceRatio;
        this.normCalculator = normCalculator;
    }

    public SwapLineImageTransformer(NormCalculator normCalculator) {
        this.normCalculator = normCalculator;
        distanceRatio = 1;
    }

    @Override
    public IndexedLineImage transform(IndexedLineImage source) {
        IndexedLineImage target = new IndexedLineImage(source);

        Random random = ThreadLocalRandom.current();
        int i1 = random.nextInt(target.getLineCount());
        int i2;
        if(distanceRatio == 1) {
            i2 = random.nextInt(target.getLineCount());
        }
        else {
            i2 = random.nextInt((int)(target.getLineCount() * distanceRatio)) + i1;
            if (i2 >= target.getLineCount()){
                i2 = target.getLineCount() - 1;
            }
        }
        if (i1 > i2){
            int temp = i1;
            i1 = i2;
            i2 = temp;
        }
        if(i1 == i2){
            if(i1 < target.getLineCount() - 1){
                i2 = i1 + 1;
            }
        }
        boolean reverse = random.nextBoolean();
        swapAndReverse(target, i1, i2, reverse);

        return target;
    }
    public void swapAndReverse(IndexedLineImage lineImage, int index1, int index2, boolean reverseIndex2) {
        if(index1 != index2) {
            swap(lineImage, index1, index2);
        }

        if(reverseIndex2){
            double oldLeftNorm1 = calculateLeftNorm(lineImage, index2);
            double oldRightNorm1 = calculateRightNorm(lineImage, index2);

            lineImage.set(index2, lineImage.getEnd(index2), lineImage.getStart(index2));
            double newLeftNorm1 = calculateLeftNorm(lineImage, index2);
            double newRightNorm1 = calculateRightNorm(lineImage, index2);
            lineImage.setNorm(lineImage.getNorm() - oldLeftNorm1 - oldRightNorm1 + newLeftNorm1 + newRightNorm1);
        }
    }

//    public void setLine(NormedLineImage lineImage, Line line, boolean reverse, int index){
//        double oldLeftNorm = calculateLeftNorm(lineImage, index);
//        double oldRightNorm = calculateRightNorm(lineImage, index);
//
//        lineImage.lines[index] = line;
//        lineImage.reverse[index] = reverse;
//
//        double newLeftNorm = calculateLeftNorm(lineImage, index);
//        double newRightNorm = calculateRightNorm(lineImage, index);
//
//        double normChange = - oldLeftNorm - oldRightNorm
//                    + newLeftNorm + newRightNorm;
//
//        lineImage.setNorm(lineImage.getNorm() + normChange);
//
//        if (ThreadLocalRandom.current().nextInt(100000) == 0) {
//            verifyNorm(lineImage);
//        }
//    }
    private void swap(IndexedLineImage lineImage, int index1, int index2) {


        double oldLeftNorm1 = calculateLeftNorm(lineImage, index1);
        double oldRightNorm1 = calculateRightNorm(lineImage, index1);
        double oldLeftNorm2 = calculateLeftNorm(lineImage, index2);
        double oldRightNorm2 = calculateRightNorm(lineImage, index2);

        int start = lineImage.getStart(index1);
        int end = lineImage.getEnd(index1);
        lineImage.set(index1, lineImage.getStart(index2), lineImage.getEnd(index2));
        lineImage.set(index2, start, end);

        double newLeftNorm1 = calculateLeftNorm(lineImage, index1);
        double newRightNorm1 = calculateRightNorm(lineImage, index1);
        double newLeftNorm2 = calculateLeftNorm(lineImage, index2);
        double newRightNorm2 = calculateRightNorm(lineImage, index2);

        double normChange;
        if (index2 == index1 + 1) {
            normChange = - oldLeftNorm1 - oldRightNorm1 - oldRightNorm2
                    + newLeftNorm1 + newRightNorm1 + newRightNorm2;
        }
        else {
            normChange = - oldLeftNorm1 - oldRightNorm1 - oldLeftNorm2 - oldRightNorm2
                    + newLeftNorm1 + newRightNorm1 + newLeftNorm2 + newRightNorm2;
        }
        lineImage.setNorm(lineImage.getNorm() + normChange);

        if (ThreadLocalRandom.current().nextInt(100000) == 0) {
            verifyNorm(lineImage);
        }
    }

    private void verifyNorm(IndexedLineImage lineImage) {
//        System.out.println("Norm: " + lineImage.getNorm());
        double recalculatedNorm = normCalculator.calculate(lineImage);
//        System.out.println("Recalculated norm: " + recalculatedNorm);
        if(Math.abs(lineImage.getNorm() - recalculatedNorm) > 0.1) {
            throw new RuntimeException("Norm verification failed " + lineImage.getNorm() + " != "
                    + recalculatedNorm);
        }
    }

    private double calculateLeftNorm(IndexedLineImage lineImage, int index){
        return index > 0 ? normCalculator.calculateNorm(lineImage.getEnd(index - 1),
                lineImage.getStart(index)) : 0;
    }
    private double calculateRightNorm(IndexedLineImage lineImage, int index){
        return index < lineImage.getLineCount() - 1 ? normCalculator.calculateNorm(lineImage.getEnd(index),
                lineImage.getStart(index + 1)) : 0;
    }
}
