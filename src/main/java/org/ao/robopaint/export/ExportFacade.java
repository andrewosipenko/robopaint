package org.ao.robopaint.export;

import org.ao.robopaint.image.indexed.IndexedLineImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.util.Map;

import static java.time.temporal.ChronoField.*;

public class ExportFacade implements AutoCloseable {
    private static final DateTimeFormatter EXPORT_DIR_SUFFIX_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral('_')
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2)
            .parseStrict()
            .toFormatter();

    private final LineImageExporter imageExporter;
    private final Map<Rendering, LineImageExporter> debugImageExporters;

    public ExportFacade(
            LineImageExporter imageExporter,
            Map<Rendering, LineImageExporter> debugImageExporters
    ) {
        this.imageExporter = imageExporter;
        this.debugImageExporters = debugImageExporters;
    }

    public ExportState createState() {
        return new ExportState();
    }

    public void exportInitial(ExportState exportState, IndexedLineImage image) throws IOException {
        exportState.setSourceBaseName("unknown");
        exportInitialInternal(exportState, image);
    }

    public void exportInitial(ExportState exportState, Path source, IndexedLineImage image) throws IOException {
        exportState.setSourceBaseName(source.toFile().getName());
        exportInitialInternal(exportState, image);
        source = Files.copy(source, exportState.getInitialDir());
        exportState.setSource(source);
    }

    private void exportInitialInternal(ExportState exportState, IndexedLineImage image) throws IOException {


        Path root = Paths.get("outputs",
                "output_" + exportState.getSourceBaseName() + "_" + EXPORT_DIR_SUFFIX_FORMAT.format(LocalDateTime.now())
        );
        Files.createDirectories(root);
        exportState.setRootDir(root);

        Path initial = root.resolve("1_initial");
        Files.createDirectory(initial);
        Path sourceRendering = initial.resolve(exportState.getSourceBaseName() + ".svg");
        imageExporter.export(image, sourceRendering);
        exportState.setSourceRendering(sourceRendering);

        Path debug = root.resolve("2_debug");
        Files.createDirectory(debug);
        exportState.setDebugDir(debug);
    }


    public void exportDebug(ExportState exportState, IndexedLineImage image, int generation) {
        debugImageExporters.entrySet().stream().map(
                entry -> {
                    Path debugRendering = exportState.getDebugDir().resolve(entry.getKey() + "gen_" + generation + "_" + image.getNorm() + ".svg");
                    entry.getValue().export(image, debugRendering);
                    return new ExportState.DebugState(generation, debugRendering, entry.getKey());
                }
        ).forEach(exportState.getDebug()::add);
    }

    public void exportResult(ExportState exportState, Path result, IndexedLineImage resultImage) throws IOException {
        Path resultDir = exportState.getRootDir().resolve("3_result");
        Files.copy(result, resultDir);
        Path resultRendering = resultDir.resolve(result.toFile().getName() + ".svg");
        imageExporter.export(resultImage, resultRendering);
        exportState.setResultDir(resultDir);
        exportState.setResult(result);
        exportState.setResultRendering(resultRendering);
    }

    @Override
    public void close() {

    }
}
