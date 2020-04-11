package org.ao.robopaint.export;

import org.ao.robopaint.image.Line;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ExportState {
    private static final int DEBUG_GENERATION_STEP_NOT_DEFINED = -1;
    private Path rootDir;
    private Path initialDir;
    private Path debugDir;
    private Path resultDir;

    private Path source;
    private String sourceBaseName;
    private Path sourceRendering;

    private final List<DebugState> debug = new ArrayList<>();
    private int maxDebugGeneration = 0;
    private int debugGenerationStep = DEBUG_GENERATION_STEP_NOT_DEFINED;

    private Path result;
    private Path resultRendering;

    private Line boundingBox;

    public ExportState() {
    }

    public static class DebugState {
        private final int generation;
        private final Path path;
        private final Rendering rendering;
        private final double norm;

        public DebugState(int generation, Path path, Rendering rendering, double norm) {
            this.generation = generation;
            this.path = path;
            this.rendering = rendering;
            this.norm = norm;
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

        public double getNorm() {
            return norm;
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

    public Iterable<DebugState> getDebug() {
        return debug;
    }

    public void addDebug(DebugState debugState){
        debug.add(debugState);
        if(debugState.generation > maxDebugGeneration) {
            maxDebugGeneration = debugState.generation;
        }
        if(debugGenerationStep == DEBUG_GENERATION_STEP_NOT_DEFINED && debugState.generation > 0) {
            debugGenerationStep = debugState.generation;
        }
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

    public Collection<Rendering> getRenderings(){
        return debug.stream().map(DebugState::getRendering).collect(Collectors.toSet());
    }
    public int getMaxGeneration(){
        return maxDebugGeneration;
    }

    public int getGenerationStep(){
        return debugGenerationStep == DEBUG_GENERATION_STEP_NOT_DEFINED ? 1 : debugGenerationStep;
    }

    public void setBoundingBox(Line boundingBox) {
        this.boundingBox = boundingBox;
    }
}

