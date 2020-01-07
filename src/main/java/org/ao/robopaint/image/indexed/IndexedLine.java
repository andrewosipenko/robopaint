package org.ao.robopaint.image.indexed;

public class IndexedLine {
    private final int start;
    private final int end;

    public IndexedLine(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
