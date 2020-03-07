package org.ao.robopaint.export;

public final class NoOpColorer implements Colorer {
    @Override
    public String getColor(int lineIndex, int totalLineCount) {
        return null;
    }
}
