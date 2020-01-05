package org.ao.robopaint.export;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExportState {
    private Path rootDir;
    private Path initialDir;
    private Path debugDir;
    private Path resultDir;

    private Path source;
    private String sourceBaseName;
    private Path sourceRendering;

    private final List<DebugState> debug = new ArrayList<>();

    private Path result;
    private Path resultRendering;

    public ExportState() {
    }

    public static class DebugState {
        private final int generation;
        private final Path path;
        private final Rendering rendering;

        public DebugState(int generation, Path path, Rendering rendering) {
            this.generation = generation;
            this.path = path;
            this.rendering = rendering;
        }

        public int getGeneration() {
            return generation;
        }

        public Path getPath() {
            return path;
        }

        public Rendering getRendering() {
            return rendering;
        }
    }

    public Path getSource() {
        return source;
    }

    public void setSource(Path source) {
        this.source = source;
    }

    public Path getSourceRendering() {
        return sourceRendering;
    }

    public void setSourceRendering(Path sourceRendering) {
        this.sourceRendering = sourceRendering;
    }

    public List<DebugState> getDebug() {
        return debug;
    }

    public Path getResult() {
        return result;
    }

    public void setResult(Path result) {
        this.result = result;
    }

    public Path getResultRendering() {
        return resultRendering;
    }

    public void setResultRendering(Path resultRendering) {
        this.resultRendering = resultRendering;
    }

    public Path getResultDir() {
        return resultDir;
    }

    public void setResultDir(Path resultDir) {
        this.resultDir = resultDir;
    }

    public Path getRootDir() {
        return rootDir;
    }

    public Path getInitialDir() {
        return initialDir;
    }

    public Path getDebugDir() {
        return debugDir;
    }

    public void setRootDir(Path rootDir) {
        this.rootDir = rootDir;
    }

    public void setInitialDir(Path initialDir) {
        this.initialDir = initialDir;
    }

    public void setDebugDir(Path debugDir) {
        this.debugDir = debugDir;
    }

    public String getSourceBaseName() {
        return sourceBaseName;
    }

    public void setSourceBaseName(String sourceBaseName) {
        this.sourceBaseName = sourceBaseName;
    }
}

