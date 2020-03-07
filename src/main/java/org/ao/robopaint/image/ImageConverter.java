package org.ao.robopaint.image;

import org.ao.robopaint.image.indexed.IndexedLineImage;
import org.ao.robopaint.image.indexed.PointIndex;

import java.util.*;

public class ImageConverter {
    public IndexedLineImage convert(LineImage lineImage) {
        LinkedHashMap<Point, Integer> points = getPoints(lineImage);

        PointIndex pointIndex = getPointIndex(points.keySet());

        return getIndexedLineImage(lineImage, pointIndex, points);
    }

    private LinkedHashMap<Point, Integer> getPoints(LineImage lineImage) {
        LinkedHashMap<Point, Integer> points = new LinkedHashMap<>(lineImage.lines.length * 2);
        for(Line line : lineImage.lines) {
            points.putIfAbsent(new Point(line.x1, line.y1), points.size());
            points.putIfAbsent(new Point(line.x2, line.y2), points.size());
        }
        return points;
    }

    private PointIndex getPointIndex(Collection<Point> points) {
        PointIndex pointIndex = new PointIndex(points.size());
        int i = 0;
        for (Point point : points) {
            pointIndex.set(i++, point.x, point.y);
        }
        return pointIndex;
    }

    private IndexedLineImage getIndexedLineImage(
            LineImage lineImage,
            PointIndex pointIndex,
            LinkedHashMap<Point, Integer> points
    ){
        IndexedLineImage result = new IndexedLineImage(pointIndex, lineImage.lines.length);
        Line[] lines = lineImage.lines;
        for(int i = 0; i < lines.length ; i++) {
            Line line = lines[i];
            int start = points.get(new Point(line.x1, line.y1));
            int end = points.get(new Point(line.x2, line.y2));
            if (lineImage.reverse[i]) {
                result.set(i, end, start);
            }
            else {
                result.set(i, start, end);
            }
        }
        return result;
    }
}
