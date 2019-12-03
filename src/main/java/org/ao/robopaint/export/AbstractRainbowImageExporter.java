package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

public abstract class AbstractRainbowImageExporter implements LineImageExporter {
    private String[] colors = new String[]{
            "#f80c12",
            "#ff9933",
            "#69d025",
            "#12bdb9",
            "#4444dd",
            "#442299",
            "#000000"
    };

    public AbstractRainbowImageExporter(boolean exportEmptyMove) {
    }

    @Override
    public void export(LineImage lineImage, String name) {
        double colorLength = (double) lineImage.lines.length / colors.length;
        for (int i = 0; i < lineImage.lines.length; i++){
            exportLine(lineImage.lines[i], colors[(int) (i / colorLength)], true);
        }
    }
    protected abstract void exportLine(Line line, String colorHex, boolean opacity);
}
