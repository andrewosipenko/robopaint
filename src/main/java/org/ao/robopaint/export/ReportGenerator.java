package org.ao.robopaint.export;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

public class ReportGenerator {
    private static Logger log = Logger.getLogger(ReportGenerator.class.getName());

    private final int scale;
    private final int width;
    private final int height;

    private Template template;

    public ReportGenerator(int scale, int width, int height) throws IOException {
        this.scale = scale;
        this.width = width;
        this.height = height;

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        configuration.setClassForTemplateLoading(this.getClass(), "/");
        configuration.setNumberFormat("0.######");
        template = configuration.getTemplate("report.ftl");
    }

    public Path generate(ExportState exportState) throws IOException {
        return generateIndexHtml(relativize(exportState));
    }
    private ExportState relativize(ExportState exportState){
        ExportState result = new ExportState();

        Path rootDir = exportState.getRootDir();
        result.setRootDir(exportState.getRootDir());

        result.setSourceBaseName(exportState.getSourceBaseName());
        result.setSourceRendering(rootDir.relativize(exportState.getSourceRendering()));

        StreamSupport.stream(exportState.getDebug().spliterator(), false)
            .map(debugState ->
                new ExportState.DebugState(debugState.getGeneration(),
                        rootDir.relativize(debugState.getPath()),
                        debugState.getRendering(),
                        debugState.getNorm())
            )
            .forEach(result::addDebug);

        if(exportState.getResultRendering() != null) {
            result.setResultRendering(rootDir.relativize(exportState.getResultRendering()));
        }
        return result;
    }

    private Path generateIndexHtml(ExportState exportState) throws IOException {
        Path path = exportState.getRootDir().resolve("index.html");
        try (Writer writer = new FileWriter(path.toFile())) {
            try {
                Map<String, Object> model = new HashMap<>();
                model.put("exportState", exportState);
                model.put("width", scale * width);
                model.put("height", scale * height);

                template.process(model, writer);
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Generated report " + path.toAbsolutePath());
        return path;
    }
}
