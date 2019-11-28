package org.ao.robopaint.image;

public class LineImage {
    public final Line[] lines;
    public final boolean[] reverse;

    public LineImage(int lineCount) {
        this.lines = new Line[lineCount];
        this.reverse = new boolean[lineCount];
    }

    public LineImage(Line... lines) {
        this.lines = lines;
        this.reverse = new boolean[lines.length];
    }

    public void clone(LineImage source){
        for(int i = 0; i < source.lines.length; i++){
            lines[i] = source.lines[i];
            reverse[i] = source.reverse[i];
        }
    }
}
