package org.ao.robopaint.export;

public class RainbowColorer implements Colorer {
    private String[] colors = new String[]{
            "#f80c12",
            "#ff9933",
            "#69d025",
            "#12bdb9",
            "#4444dd",
            "#442299",
            "#000000"
    };

    @Override
    public String getColor(int lineIndex, int totalLineCount) {
        return colors[lineIndex  * colors.length / totalLineCount];
    }
}
