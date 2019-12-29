package org.ao.robopaint.image.indexed;

public class PointIndex {
    private final int[] x;
    private final int[] y;

    public PointIndex(int count) {
        this.x = new int[count];
        this.y = new int[count];
    }

    public void set(int index, int x, int y) {
        this.x[index] = x;
        this.y[index] = y;
    }

    public int x(int index) {
        return x[index];
    }
    public int y(int index) {
        return y[index];
    }
    public int getPointCount(){
        return x.length;
    }
}
