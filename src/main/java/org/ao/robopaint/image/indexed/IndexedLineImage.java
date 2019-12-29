package org.ao.robopaint.image.indexed;

public class IndexedLineImage {
    private final PointIndex pointIndex;
    private final int[] start;
    private final int[] end;
    private double norm = Double.MIN_VALUE;

    public IndexedLineImage(PointIndex pointIndex, int lineCount) {
        this.pointIndex = pointIndex;
        this.start = new int[lineCount];
        this.end = new int[lineCount];
    }

    public IndexedLineImage(IndexedLineImage source) {
        this(source.pointIndex, source.getLineCount());
        copyFrom(source);
    }

    public void set(int index, int start, int end) {
        this.start[index] = start;
        this.end[index] = end;
    }

    public int getStart(int index) {
        return start[index];
    }

    public int getEnd(int index){
        return end[index];
    }

    public int getLineStartX(int index){
        return pointIndex.x(start[index]);
    }
    public int getLineStartY(int index){
        return pointIndex.y(start[index]);
    }
    public int getLineEndX(int index){
        return pointIndex.x(end[index]);
    }
    public int getLineEndY(int index){
        return pointIndex.y(end[index]);
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }

    public double getNorm() {
        return norm;
    }

    public int getLineCount(){
        return start.length;
    }

    private void copyFrom(IndexedLineImage source){
        for (int i = 0; i < source.getLineCount(); i++) {
            start[i] = source.start[i];
            end[i] = source.end[i];
        }
        norm = source.norm;
    }

    public PointIndex getPointIndex() {
        return pointIndex;
    }
}
