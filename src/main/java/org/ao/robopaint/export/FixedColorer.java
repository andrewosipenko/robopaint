package org.ao.robopaint.export;

public class FixedColorer implements Colorer {
    private final String color;

    public FixedColorer(String color) {
        this.color = color;
    }

    @Override
    public String getColor(int lineIndex, int totalLineCount) {
        return color;
    }
}
