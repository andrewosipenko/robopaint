package org.ao.robopaint.norm;

import org.ao.robopaint.image.LineImage;

public class NormedLineImage extends LineImage {
    public double norm = Double.MIN_VALUE;
    private boolean normCalculated;

    public NormedLineImage(int lineCount) {
        super(lineCount);
    }

    public void setNorm(double norm) {
        this.norm = norm;
        normCalculated = true;
    }

    public double getNorm(){
        return norm;
    }

    public boolean isNormCalculated() {
        return normCalculated;
    }
}
