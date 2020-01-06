package org.ao.robopaint.export;

public class GradientColorer implements Colorer {
    private int sourceR = 0;
    private int sourceB = 0xFF;
    private int targetR = 0xFF;
    private int targetB = 0;

    @Override
    public String getColor(int i, int totalLineCount) {
        int red = (targetR - sourceR) * i / totalLineCount;
        int blue = (targetB - sourceB) * (i - totalLineCount) / totalLineCount;
        return "#" + getColorHex(red) + "00" + getColorHex(blue);
    }
    private String getColorHex(int color){
        String result = Integer.toHexString(color);
        if (result.length() == 1) {
            return "0" + result;
        }
        return result;
    }
}
