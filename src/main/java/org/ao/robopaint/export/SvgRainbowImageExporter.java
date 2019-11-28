package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;
import org.ao.robopaint.image.LineImage;

import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SvgRainbowImageExporter
//        extends AbstractRainbowImageExporter {
    extends AbstractGradientImageExporter {
    private final int scale;
    private final Path dir;
    private final int width;
    private final int height;

    private final ThreadLocal<Writer> writer = new ThreadLocal<>();

    public SvgRainbowImageExporter(Path dir, int width, int height, int scale) throws IOException {
        if (Files.isDirectory(dir)) {
            Files.list(dir).forEach(path -> {
                try {
                    Files.delete(path);
                }
                catch (IOException e) {
                    throw new IOError(e);
                }
            });
        }
        else {
            Files.createDirectories(dir);
        }
        this.dir = dir;
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    @Override
    public void export(LineImage lineImage, String name) {
        try {
            Path file = dir.resolve(name);

            System.out.println("Exporting " + file);
            try (Writer writer = new FileWriter(file.toFile())) {
                this.writer.set(writer);
                writer.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"");
                writer.append(Integer.toString(width));
                writer.append("\" height=\"");
                writer.append(Integer.toString(height));
                writer.append("\" version=\"1.1\">\n");
                super.export(lineImage, name);
                writer.append("</svg>");
            }
            finally {
                writer.remove();
            }

            generateIndexHtml();
        }
        catch (IOException e){
            throw new RuntimeException("Failed to export file " + name, e);
        }
    }

    @Override
    protected void exportLine(Line line, String colorHex)  {
        try {
            Writer writer = this.writer.get();
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
            writer.append(";stroke-width:1\"/>\n");
        }
        catch (IOException e){
            throw new IOError(e);
        }
    }
    private void generateIndexHtml() throws IOException {
        Path path = dir.resolve("index.html");
        try (Writer writer = new FileWriter(path.toFile())) {
            writer.append("<html><body>\n");
            Files.list(dir)
                    .filter(p -> p.getFileName().toString().endsWith(".svg"))
                    .forEach(p -> {
                try {
                    String fileName = p.getFileName().toString();
                    writer.append("<p><img src='");
                    writer.append(fileName);
                    writer.append("' width='");
                    writer.append(Integer.toString(scale * width));
                    writer.append("' height='");
                    writer.append(Integer.toString(scale * height));
                    writer.append("'/>");
                    writer.append(fileName);
                    writer.append("</p>\n");
                } catch (IOException e) {
                    throw new IOError(e);
                }
            });
            writer.append("</body></html>");
        }
    }
}
