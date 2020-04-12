package org.ao.robopaint.transform;

import org.ao.robopaint.export.LineImageExporter;
import org.ao.robopaint.export.SvgRainbowImageExporter;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.merge.ContinuousAreaImageMerger;
import org.ao.robopaint.merge.ImageMerger;
import org.ao.robopaint.norm.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomBruteForceSpeedLineImageTransformer implements LineImageTransformer {
    private final int iterationCount;
    private final int populationSize;
    private final double transformerDistanceRatio;

    private LineImageExporter lineImageExporter;

    public RandomBruteForceSpeedLineImageTransformer(int populationSize, int iterationCount, int width, int height, double transformerDistanceRatio, int scale, boolean exportEmptyMove) throws IOException {
        this.populationSize = populationSize;
        this.iterationCount = iterationCount;
        this.transformerDistanceRatio = transformerDistanceRatio;

        lineImageExporter = new SvgRainbowImageExporter(Paths.get("debug-populations"), width, height, scale, exportEmptyMove);
    }

    @Override
    public LineImage transform(LineImage lineImage) {
        Instant start = Instant.now();
        NormCalculator normCalculator = new SpeedNormCalculator();
        NormedLineImage source = createNormedLineImage(lineImage, normCalculator);
        System.out.println("Initial speed " + source.norm);

        LineImageTransformerStrategy fullLineImageTransformerStrategy = new ShuffleLineImageTransformerStrategy();
        NormedLineImageTransformer fullNormedLineImageTransformer = new DefaultNormedLineImageTransformer(fullLineImageTransformerStrategy, normCalculator);

//        LineImageTransformerStrategy partialLineImageTransformerStrategy = new ShuffleLineImageTransformerStrategy(transformerDistanceRatio);
        SwapLineImageTransformerStrategy partialLineImageTransformerStrategy = new SwapLineImageTransformerStrategy(transformerDistanceRatio, normCalculator);
        NormedLineImageTransformer partialNormedLineImageTransformer = new DefaultNormedLineImageTransformer(partialLineImageTransformerStrategy, normCalculator);
        NormedLineImageTransformer reverseNormedLineImageTransformer = new DefaultNormedLineImageTransformer(new ReverseLineImageTransformerStrategy(0.6), normCalculator);

        ImageMerger imageMerger = new ContinuousAreaImageMerger(0.6, normCalculator);
//        ImageMerger imageMerger = new FastContinuousAreaImageMerger(0.01, partialLineImageTransformerStrategy);

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

            List<NormedLineImage> population2 = new ArrayList<>(population);
//            List<NormedLineImage> population2 = new ArrayList<>(newPopulation);
            Collections.shuffle(population2);

            List<NormedLineImage> mergedPopulation = mergePopulation(
                    population2.subList(0, population2.size() / 2),
                    population2.subList(population2.size() / 2, population2.size()), imageMerger);
            newPopulation.addAll(mergedPopulation);

            List<NormedLineImage> reversedPopulation = population.parallelStream()
                    .map(reverseNormedLineImageTransformer::transform)
                    .collect(Collectors.toList());
            newPopulation.addAll(reversedPopulation);

            population = cutPopulation(population, newPopulation, () -> fullNormedLineImageTransformer.transform(source));

            logProgress(i, population);
        }

        System.out.println("Result speed " + population.get(0).norm);
        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        System.out.println(String.format("Elapsed time: %dm %ds", duration.toMinutes(), duration.toSecondsPart()));
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
                .parallel()
                 .sorted(Comparator.comparingDouble(NormedLineImage::getNorm))
                .limit(populationSize)
                .collect(Collectors.toList());
        if(result.size() == populationSize)
            return result;
        return Stream.concat(result.stream(), Stream.generate(supplier))
                .limit(populationSize)
                .collect(Collectors.toList());
    }

    private void logProgress(int gen, List<NormedLineImage> population){
        if(gen % 200 == 0) {
            System.out.println(String.format("Gen: %5s Current best speeds %s, %s, %s, %s, %s" , gen,
                    getNorm(population, 0), getNorm(population, 1),
                    getNorm(population, populationSize / 4),
                    getNorm(population, populationSize / 2),
                    getNorm(population, 3 * populationSize / 4)));
        }
        if(gen % 1000 == 0) {
            lineImageExporter.export(population.get(0), String.format("gen-%05d.svg", gen));
        }
    }

    private List<NormedLineImage> mergePopulation(
            List<NormedLineImage> population1,
            List<NormedLineImage> population2,
            ImageMerger imageMerger
    ) {
        return IntStream.range(0, population1.size())
                .parallel()
                .mapToObj(index -> {
                    NormedLineImage lineImage = new NormedLineImage(population1.get(0).lines.length);
                    imageMerger.merge(population1.get(index), population2.get(index), lineImage);
                    return lineImage;
                })
                .collect(Collectors.toList());
    }

    private Double getNorm(List<NormedLineImage> population, int index) {
        if(index < population.size()){
            return population.get(index).norm;
        }
        return null;
    }

    private NormedLineImage createNormedLineImage(LineImage lineImage, NormCalculator normCalculator){
        NormedLineImage result = new NormedLineImage(lineImage.lines.length);
        result.setNorm(normCalculator.calculate(lineImage));
        result.clone(lineImage);
        return result;
    }

    private LineImage createLineImage(LineImage lineImage){
        LineImage result = new LineImage(lineImage.lines.length);
        result.clone(lineImage);
        return result;
    }
}