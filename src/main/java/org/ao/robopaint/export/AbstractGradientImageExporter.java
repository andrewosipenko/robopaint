package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

public abstract class AbstractGradientImageExporter implements LineImageExporter {
    private int sourceR = 0;
    private int sourceB = 0xFF;
    private int targetR = 0xFF;
    private int targetB = 0;

    @Override
    public void export(LineImage lineImage, String name) {
        for (int i = 0; i < lineImage.lines.length; i++){
            int red = (targetR - sourceR) * i / lineImage.lines.length;
            int blue = (targetB - sourceB) * (i - lineImage.lines.length) / lineImage.lines.length;
            exportLine(lineImage.lines[i], "#" + getColorHex(red) + "00" + getColorHex(blue));
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
