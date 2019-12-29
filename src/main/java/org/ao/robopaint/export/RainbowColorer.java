package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.indexed.IndexedLineImage;

public abstract class RainbowColorer implements Colorer {
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
        return colors[(int) (lineIndex / totalLineCount)];
    }
}
