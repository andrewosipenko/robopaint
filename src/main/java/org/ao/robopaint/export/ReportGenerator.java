package org.ao.robopaint.export;

import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportGenerator {
    private final int scale;
    private final int width;
    private final int height;

    public ReportGenerator(int scale, int width, int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;
    }

    public void generate(ExportState exportState) {

    }
    private void generateIndexHtml(ExportState exportState) throws IOException {
        Path dir = exportState.getRootDir();
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
