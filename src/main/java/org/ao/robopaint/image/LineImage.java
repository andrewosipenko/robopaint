package org.ao.robopaint.image;

public class LineImage {
    public final Line[] lines;
    public final boolean[] reverse;

    public double norm = Double.MIN_VALUE;
    private boolean normCalculated;

    public LineImage(int lineCount) {
        this.lines = new Line[lineCount];
        this.reverse = new boolean[lineCount];
    }

    public LineImage(Line... lines) {
        this.lines = lines;
        this.reverse = new boolean[lines.length];
    }

    public LineImage(LineImage lineImage) {
        this.lines = lineImage.lines.clone();
        this.reverse = lineImage.reverse.clone();
        this.norm = lineImage.norm;
        this.normCalculated = lineImage.normCalculated;
    }

    public void clone(LineImage source){
        for(int i = 0; i < source.lines.length; i++){
            lines[i] = source.lines[i];
            reverse[i] = source.reverse[i];
        }
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

    public int getLineStartX(int i){
        return reverse[i] ? lines[i].x2 : lines[i].x1;
    }

    public int getLineEndX(int i){
        return reverse[i] ? lines[i].x1 : lines[i].x2;
    }

    public int getLineStartY(int i){
        return reverse[i] ? lines[i].y2 : lines[i].y1;
    }

    public int getLineEndY(int i){
        return reverse[i] ? lines[i].y1 : lines[i].y2;
    }

    public int getLineCount(){
        return lines.length;
    }
}
