package org.ao.robopaint.export;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReportGeneratorTest {
    private Path root;
    private ReportGenerator reportGenerator;

    @Before
    public void setUp() throws Exception {
        root = Files.createTempDirectory("test");
        reportGenerator = new ReportGenerator(1, 100, 100);
    }

    @Test
    public void testGenerate() throws IOException {
        ExportState exportState = new ExportState();
        exportState.setRootDir(root);
        exportState.setSourceBaseName("test");
        exportState.setSourceRendering(root.resolve("source.svg"));
        exportState.getDebug().add(new ExportState.DebugState(0, root.resolve("debug_0.svg"), Rendering.MOVE));
        exportState.getDebug().add(new ExportState.DebugState(100, root.resolve("debug_0.svg"), Rendering.MOVE));
        exportState.setResultRendering(root.resolve("result.svg"));

        Path result = reportGenerator.generate(exportState);
    }
}