package org.ao.robopaint.gcode;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.io.LineImageWriter;

import java.io.*;
import java.nio.file.Path;

public class GCodeLineImageWriter implements LineImageWriter {
    @Override
    public void write(LineImage lineImage, Path path) throws IOException {
        try(
                FileWriter fileWriter = new FileWriter(path.toFile());
                BufferedWriter writer = new BufferedWriter(fileWriter)
        ){
            writeHeader(writer);

            int previousX = Integer.MIN_VALUE;
            int previousY = Integer.MIN_VALUE;
            for(int i = 0; i < lineImage.lines.length; i++) {
                Line line = lineImage.lines[i];
                int x1, y1, x2, y2;
                if(lineImage.reverse[i]){
                    x1 = line.x2;
                    y1 = line.y2;
                    x2 = line.x1;
                    y2 = line.y1;
                }
                else {
                    x1 = line.x1;
                    y1 = line.y1;
                    x2 = line.x2;
                    y2 = line.y2;
                }
                if(x1 == previousX && y1 == previousY){
                    goDown(writer, x2, y2);
                }
                else if(Math.abs(x1 - previousX) <= 1 && Math.abs(y1 - previousY) <= 1) {
                    goDown(writer, x1, y1);
                    goDown(writer, x2, y2);
                }
                else{
                    up(writer);
                    goUp(writer, x1, y1);
                    goDown(writer, x1, y1);
                    goDown(writer, x2, y2);
                }
                previousX = x2;
                previousY = y2;
            }
        }
    }
    private void writeHeader(BufferedWriter writer) throws IOException {
        writer.write("G90");
        writer.newLine();
        writer.write("G21");
        writer.newLine();
        writer.write("G01 F8000");
        writer.newLine();
        writer.write("G00 X0 Y0 Z0");
        writer.newLine();
    }
    private void goUp(BufferedWriter writer, int x, int y) throws IOException {
        writer.write("G00 X");
        writer.write(Integer.toString(x));
        writer.write(" Y");
        writer.write(Integer.toString(y));
        writer.write(" Z1");
        writer.newLine();
    }
    private void goDown(BufferedWriter writer, int x, int y) throws IOException {
        writer.write("G01 X");
        writer.write(Integer.toString(x));
        writer.write(" Y");
        writer.write(Integer.toString(y));
        writer.write(" Z-1");
        writer.newLine();
    }
    private void up(BufferedWriter writer) throws IOException {
        writer.write("G00  Z1");
        writer.newLine();
    }
}
