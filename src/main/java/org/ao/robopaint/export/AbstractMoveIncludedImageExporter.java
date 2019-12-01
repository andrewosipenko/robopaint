package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

public abstract class AbstractMoveIncludedImageExporter implements LineImageExporter {
    private String LINE_COLOR = "#000000";
    private String EMPTY_MOVE_COLOR = "#AAFFAA";

    private boolean exportEmptyMove;

    public AbstractMoveIncludedImageExporter(boolean exportEmptyMove) {
        this.exportEmptyMove = exportEmptyMove;
    }

    @Override
    public void export(LineImage lineImage, String name) {
        exportLine(lineImage.lines[0], LINE_COLOR);
        for (int i = 1; i < lineImage.lines.length; i++){
            Line previousLine = lineImage.lines[i - 1];
            Line line = lineImage.lines[i];

            if (exportEmptyMove && (Math.abs(previousLine.x2 - line.x1) > 1 || Math.abs(previousLine.y2 - line.y2) > 1)) {
                exportLine(new Line(previousLine.x2, previousLine.y2, line.x1, line.y1), EMPTY_MOVE_COLOR);
            }
            exportLine(line, LINE_COLOR);
        }
    }
    private String getColorHex(int color){
        String result = Integer.toHexString(color);
        if (result.length() == 1) {
            return "0" + result;
        }
        return result;
    }

    protected abstract void exportLine(Line line, String colorHex);
}
