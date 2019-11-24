package org.ao.robopaint.transform;

import org.ao.robopaint.export.LineImageExporter;
import org.ao.robopaint.export.SvgRainbowImageExporter;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomBruteForceSpeedLineImageTransformer implements LineImageTransformer {
    private final int iterationCount;
    private final int populationSize;

    private LineImageExporter lineImageExporter;

    public RandomBruteForceSpeedLineImageTransformer(int iterationCount, int width, int height) throws IOException {
        this.iterationCount = iterationCount;
        this.populationSize = 50000;

        lineImageExporter = new SvgRainbowImageExporter(Paths.get("debug-populations"), width, height);
    }

    @Override
    public LineImage transform(LineImage lineImage) {
        NormCalculator normCalculator = new SpeedNormCalculator();
        NormedLineImage source = createNormedLineImage(lineImage, normCalculator);
        System.out.println("Initial speed " + source.norm);

        LineImageTransformerStrategy fullLineImageTransformerStrategy = new ShuffleLineImageTransformerStrategy();
        NormedLineImageTransformer fullNormedLineImageTransformer = new DefaultNormedLineImageTransformer(fullLineImageTransformerStrategy, normCalculator);

        LineImageTransformerStrategy partialLineImageTransformerStrategy = new ShuffleLineImageTransformerStrategy(0.9);
//        LineImageTransformerStrategy partialLineImageTransformerStrategy = new SwapLineImageTransformerStrategy();
        NormedLineImageTransformer partialNormedLineImageTransformer = new DefaultNormedLineImageTransformer(partialLineImageTransformerStrategy, normCalculator);


        List<NormedLineImage> population = new ArrayList<>();
        population.add(source);
        logProgress(0, population);
        population.addAll(
            Stream.generate(() -> source).limit(populationSize)
                .parallel()
                .map(fullNormedLineImageTransformer::transform)
                .collect(Collectors.toList()));
            population = population.stream()
                    .sorted(Comparator.comparing(image -> image.norm))
                    .collect(Collectors.toList());
        logProgress(1, population);

        for(int i = 1; i <= iterationCount; i++){
            List<NormedLineImage> newPopulation = population.parallelStream()
                    .map(partialNormedLineImageTransformer::transform)
                    .collect(Collectors.toList());

            population = cutPopulation(population, newPopulation, () -> fullNormedLineImageTransformer.transform(source));

            logProgress(i, population);
        }

        System.out.println("Result speed " + population.get(0).norm);
        return createLineImage(population.get(0));
    }

    private List<NormedLineImage> cutPopulation(List<NormedLineImage> population, List<NormedLineImage> newPopulation,
                                                Supplier<NormedLineImage> supplier){

        int bestProtectedCount = 1;
        Random random = ThreadLocalRandom.current();
        List<NormedLineImage> result =
                Stream.concat(
                        population.stream()
                            .limit(bestProtectedCount),
                        Stream.concat(
                            population.stream()
                                .skip(bestProtectedCount),
//                                .filter((image) -> random.nextDouble()  < 0.99), // kill older
                            newPopulation.stream()
                        )
                )
                .sorted(Comparator.comparing(image -> image.norm))
                .limit(populationSize)
                .collect(Collectors.toList());
        if(result.size() == populationSize)
            return result;
        return Stream.concat(result.stream(), Stream.generate(supplier))
                .limit(populationSize)
                .collect(Collectors.toList());
    }

    private void logProgress(int gen, List<NormedLineImage> population){
        if(gen % 50 == 0) {
            System.out.println(String.format("Gen: %5s Current best speeds %s, %s, %s, %s, %s" , gen,
                    getNorm(population, 0), getNorm(population, 1),
                    getNorm(population, populationSize / 4),
                    getNorm(population, populationSize / 2),
                    getNorm(population, 3 * populationSize / 4)));
        }
        if(gen % 500 == 0) {
            lineImageExporter.export(population.get(0), String.format("gen-%04d.svg", gen));
        }
    }

    private Double getNorm(List<NormedLineImage> population, int index) {
        if(index < population.size()){
            return population.get(index).norm;
        }
        return null;
    }

    private NormedLineImage createNormedLineImage(LineImage lineImage, NormCalculator normCalculator){
        NormedLineImage result = new NormedLineImage(lineImage.lines.length);
        result.norm = normCalculator.calculate(lineImage);
        result.copyLinesFrom(lineImage);
        return result;
    }

    private LineImage createLineImage(LineImage lineImage){
        LineImage result = new LineImage(lineImage.lines.length);
        result.copyLinesFrom(lineImage);
        return result;
    }
}
