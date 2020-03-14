package org.ao.robopaint.gcode;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.io.LineImageReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GCodeLineImageReader implements LineImageReader {
    private static Logger log = Logger.getLogger(GCodeLineImageReader.class.getName());

    private static final String PEN_DOWN_Z = "Z-1";
    private static final Pattern PATTERN = Pattern.compile("G01\\sX(\\d+)\\sY(\\d+)\\sZ\\-1");

    @Override
    public LineImage read(Path path) throws IOException {
        List<Line> lines = new ArrayList<>();
        try(
            FileReader fileReader = new FileReader(path.toFile());
            BufferedReader bufferedReader = new BufferedReader(fileReader)
        ){
            String line;
            boolean prevPointDefined = false;
            int prevX = Integer.MIN_VALUE;
            int prevY = Integer.MIN_VALUE;
            while ((line = bufferedReader.readLine()) != null){
                Matcher matcher = PATTERN.matcher(line);
                if(matcher.matches()){
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    if (prevPointDefined) {
                        lines.add(new Line(prevX, prevY, x, y));
                    }
                    prevX = x;
                    prevY = y;
                    prevPointDefined = true;
                }
                else {
                    prevPointDefined = false;
                }
            }
        }
        log.info("Loaded " + lines.size() + " straight lines (pen strokes)");
        return new LineImage(lines.toArray(new Line[0]));
    }
}
