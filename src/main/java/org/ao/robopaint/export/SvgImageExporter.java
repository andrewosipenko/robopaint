package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.indexed.IndexedLineImage;

import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class SvgImageExporter implements LineImageExporter {
    private final int width;
    private final int height;
    private final Colorer lineColorer;
    private final Colorer emptyMoveColorer;

    public SvgImageExporter(int width, int height, Colorer lineColorer, Colorer emptyMoveColorer) {
        this.width = width;
        this.height = height;
        this.lineColorer = lineColorer;
        this.emptyMoveColorer = emptyMoveColorer;
    }

    @Override
    public void export(IndexedLineImage lineImage, Path path) {
        try {
            System.out.println("Exporting " + path);
            try (Writer writer = new FileWriter(path.toFile())) {
                writer.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"");
                writer.append(Integer.toString(width));
                writer.append("\" height=\"");
                writer.append(Integer.toString(height));
                writer.append("\" version=\"1.1\">\n");
                exportLines(lineImage, writer);
                writer.append("</svg>");
            }
        }
        catch (IOException e){
            throw new RuntimeException("Failed to export file " + path, e);
        }
    }

    private void exportLines(IndexedLineImage lineImage, Writer writer) {
        Line previousLine = new Line(
                lineImage.getLineStartX(0),
                lineImage.getLineStartY(0),
                lineImage.getLineEndX(0),
                lineImage.getLineEndY(0)
        );
        String color = lineColorer.getColor(0, lineImage.getLineCount());
        exportLine(previousLine, color, false, writer);
        for (int i = 1; i < lineImage.getLineCount(); i++){
            Line line = new Line(
                    lineImage.getLineStartX(i),
                    lineImage.getLineStartY(i),
                    lineImage.getLineEndX(i),
                    lineImage.getLineEndY(i)
            );

            String emptyColor = emptyMoveColorer.getColor(i, lineImage.getLineCount());
            if ((Math.abs(previousLine.x2 - line.x1) > 2 || Math.abs(previousLine.y2 - line.y2) > 2)) {
                exportLine(new Line(previousLine.x2, previousLine.y2, line.x1, line.y1), emptyColor, false, writer);
            }

            color = lineColorer.getColor(i, lineImage.getLineCount());
            exportLine(line, color, true, writer);
            previousLine = line;
        }
    }

    protected void exportLine(Line line, String colorHex, boolean opacity, Writer writer)  {
        if (colorHex == null) {
            return;
        }
        try {
            writer.append("<line x1=\"");
            writer.append(Integer.toString(line.x1));
            writer.append("\" y1=\"");
            writer.append(Integer.toString(line.y1));
            writer.append("\" x2=\"");
            writer.append(Integer.toString(line.x2));
            writer.append("\" y2=\"");
            writer.append(Integer.toString(line.y2));
            writer.append("\" style=\"stroke:");
            writer.append(colorHex);
            writer.append(";stroke-width:1\"");
            if(!opacity){
                writer.append(" opacity=\"0.3\"");
            }
            writer.append("/>\n");
        }
        catch (IOException e){
            throw new IOError(e);
        }
    }
}
