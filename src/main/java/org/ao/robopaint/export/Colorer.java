package org.ao.robopaint.export;

public interface Colorer {
    public static final Colorer NOOP_COLORER = new NoOpColorer();
    String getColor(int lineIndex, int totalLineCount);
}
