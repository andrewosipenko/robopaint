package org.ao.robopaint.image.indexed;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexedLine that = (IndexedLine) o;
        return start == that.start &&
                end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
