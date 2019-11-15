package org.ao.robopaint.image;

public class LineImage {
    public final Line[] lines;

    public LineImage(int lineCount) {
        this.lines = new Line[lineCount];
    }

    public LineImage(Line... lines) {
        this.lines = lines;
    }

    public void copyLinesFrom(LineImage source){
        for(int i = 0; i < source.lines.length; i++){
            lines[i] = source.lines[i];
        }
    }
}
