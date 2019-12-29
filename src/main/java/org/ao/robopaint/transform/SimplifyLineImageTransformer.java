package org.ao.robopaint.transform;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

import java.util.*;
import java.util.logging.Logger;

public class SimplifyLineImageTransformer implements ReversableLineImageTransformer {
    private static Logger log = Logger.getLogger(SimplifyLineImageTransformer.class.getName());

    private Map<Line, List<Line>> reverseMap;
    @Override
    public LineImage transform(LineImage lineImage) {
        List<Line> result = new ArrayList<>();
        LinkedList<Line> currentLine = new LinkedList<>();
        reverseMap = new HashMap<>();

        for (Line line : lineImage.lines) {
            if (currentLine.isEmpty()) {
                currentLine.addLast(line);
            }
            else {
                if (!checkIfLinesConnected(currentLine.peekLast(), line)){
                    putTransformedLine(currentLine, result);
                    currentLine = new LinkedList<>();
                }
                currentLine.addLast(line);
            }
        }
        if(!currentLine.isEmpty()){
            putTransformedLine(currentLine, result);
        }
        log.info("Simplified image to " + result.size() + " lines from original " + lineImage.lines.length);
        return new LineImage(result.toArray(new Line[0]));
    }

    private void putTransformedLine(LinkedList<Line> currentLine, List<Line> result) {
        Line transformedLine;
        if (currentLine.size() == 1){
            transformedLine = currentLine.peekFirst();
        }
        else {
            transformedLine = new Line (currentLine.peekFirst().x1, currentLine.peekFirst().y1,
                    currentLine.peekLast().x2, currentLine.peekLast().y2);
        }
        reverseMap.put(transformedLine, currentLine);
        result.add(transformedLine);
    }

    private boolean checkIfLinesConnected(Line line1, Line line2) {
        return Math.abs(line2.x1 - line1.x2) <= 1 && Math.abs(line2.y1 - line1.y2) <= 1;
    }

    @Override
    public LineImage reverse(LineImage lineImage) {
        List<Line> result = Arrays.stream(lineImage.lines)
                .map(reverseMap::get)
                .reduce((a, b) -> {
                    List<Line> c = new ArrayList<>();
                    c.addAll(a);
                    c.addAll(b);
                    return c;
                }).orElse(Collections.emptyList());
        return new LineImage(result.toArray(new Line[0]));
    }
}
