package org.ao.robopaint.transform.indexed;

import org.ao.robopaint.export.ExportFacade;
import org.ao.robopaint.export.ExportState;
import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.image.Point;
import org.ao.robopaint.merge.ContinuousAreaImageMerger;
import org.ao.robopaint.merge.ImageMerger;
import org.ao.robopaint.norm.NormCalculator;
import org.ao.robopaint.norm.SpeedNormCalculator;
import org.ao.robopaint.transform.LineImageTransformer;
import org.ao.robopaint.transform.ReverseLineImageTransformer;
import org.ao.robopaint.transform.ShuffleLineImageTransformer;
import org.ao.robopaint.transform.SwapLineImageTransformer;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomBruteForceSpeedLineImageTransformer implements LineImageTransformer {
    private static Logger log = Logger.getLogger(RandomBruteForceSpeedLineImageTransformer.class.getName());

    private final int iterationCount;
    private final int populationSize;
    private final double transformerDistanceRatio;

    private final ExportFacade exportFacade;
    private final ExportState exportState;

    public RandomBruteForceSpeedLineImageTransformer(int populationSize, int iterationCount, int width, int height, double transformerDistanceRatio, int scale, ExportFacade exportFacade,
                                                     ExportState exportState) throws IOException {
        this.populationSize = populationSize;
        this.iterationCount = iterationCount;
        this.transformerDistanceRatio = transformerDistanceRatio;
        this.exportFacade = exportFacade;
        this.exportState = exportState;
    }

    @Override
    public LineImage transform(LineImage source) {
        Instant start = Instant.now();
        NormCalculator normCalculator = new SpeedNormCalculator(new Point(0, 0));
        source.setNorm(normCalculator.calculate(source));
        System.out.println("Initial speed " + source.getNorm());

        LineImageTransformer fullLineImageTransformer = new ShuffleLineImageTransformer(normCalculator);
        LineImageTransformer partialLineImageTransformer = new ShuffleLineImageTransformer(transformerDistanceRatio, normCalculator);

        LineImageTransformer swapLineImageTransformer = new SwapLineImageTransformer(transformerDistanceRatio, normCalculator);
        LineImageTransformer reverseLineImageTransformer = new ReverseLineImageTransformer(0.6, normCalculator);

        ImageMerger imageMerger = new ContinuousAreaImageMerger(0.6, normCalculator);

        List<LineImage> population = new ArrayList<>();
        population.add(source);
        logProgress(0, population);
        population.addAll(
            Stream.generate(() -> source).limit(populationSize)
                .parallel()
                .map(fullLineImageTransformer::transform)
                .collect(Collectors.toList()));
            population = population.stream()
                    .sorted(Comparator.comparing(image -> image.getNorm()))
                    .collect(Collectors.toList());
        logProgress(1, population);

        for(int i = 1; i <= iterationCount; i++){
            List<LineImage> newPopulation = population.parallelStream()
                    .map(partialLineImageTransformer::transform)
                    .collect(Collectors.toList());

            List<LineImage> population2 = new ArrayList<>(population);
//            List<LineImage> population2 = new ArrayList<>(newPopulation);
            Collections.shuffle(population2);

            List<LineImage> mergedPopulation = mergePopulation(
                    population2.subList(0, population2.size() / 2),
                    population2.subList(population2.size() / 2, population2.size()), imageMerger);
            newPopulation.addAll(mergedPopulation);

            List<LineImage> reversedPopulation = population.parallelStream()
                    .map(reverseLineImageTransformer::transform)
                    .collect(Collectors.toList());
            newPopulation.addAll(reversedPopulation);

            population = cutPopulation(population, newPopulation, () -> fullLineImageTransformer.transform(source));

            logProgress(i, population);
        }

        System.out.println("Result speed " + population.get(0).getNorm());
        Instant finish = Instant.now();
        Duration duration = Duration.between(start, finish);
        System.out.println(String.format("Elapsed time: %dm %ds", duration.toMinutes(), duration.toSecondsPart()));
        return population.get(0);
    }

    private List<LineImage> cutPopulation(List<LineImage> population, List<LineImage> newPopulation,
                                                Supplier<LineImage> supplier){

        int bestProtectedCount = 1;
        Random random = ThreadLocalRandom.current();
        List<LineImage> result =
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
                 .sorted(Comparator.comparingDouble(LineImage::getNorm))
                .limit(populationSize)
                .collect(Collectors.toList());
        if(result.size() == populationSize)
            return result;
        return Stream.concat(result.stream(), Stream.generate(supplier))
                .limit(populationSize)
                .collect(Collectors.toList());
    }

    private void logProgress(int gen, List<LineImage> population){
        if(gen % 200 == 0) {
            System.out.println(String.format("Gen: %5s Current best speeds %s, %s, %s, %s, %s" , gen,
                    getNorm(population, 0), getNorm(population, 1),
                    getNorm(population, populationSize / 4),
                    getNorm(population, populationSize / 2),
                    getNorm(population, 3 * populationSize / 4)));
        }
        if(gen % 1000 == 0) {
            try {
                exportFacade.exportDebug(exportState, population.get(0), gen);
            } catch (IOException e) {
                log.warning("Failed to export debug");
            }
        }
    }

    private List<LineImage> mergePopulation(
            List<LineImage> population1,
            List<LineImage> population2,
            ImageMerger imageMerger
    ) {
        int lineCount = population1.get(0).lines.length;
        return IntStream.range(0, population1.size())
                .parallel()
                .mapToObj(index -> {
                    LineImage lineImage = new LineImage(lineCount);
                    imageMerger.merge(population1.get(index), population2.get(index), lineImage);
                    return lineImage;
                })
                .collect(Collectors.toList());
    }

    private Double getNorm(List<LineImage> population, int index) {
        if(index < population.size()){
            return population.get(index).getNorm();
        }
        return null;
    }
}
