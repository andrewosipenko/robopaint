package org.ao.robopaint.transform;

import org.ao.robopaint.image.LineImage;
import org.ao.robopaint.norm.*;

import java.util.Comparator;
import java.util.stream.Stream;

public class RandomBruteForceSpeedLineImageTransformer implements LineImageTransformer {
    private final int iterationCount;

    public RandomBruteForceSpeedLineImageTransformer(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    @Override
    public LineImage transform(LineImage lineImage) {
        NormCalculator normCalculator = new SpeedNormCalculator();
        NormedLineImage source = createNormedLineImage(lineImage, normCalculator);
        System.out.println("Initial speed " + source.norm);

        LineImageTransformerStrategy lineImageTransformerStrategy = new RandomLineImageTransformerStrategy();
        NormedLineImageTransformer normedLineImageTransformer = new DefaultNormedLineImageTransformer(lineImageTransformerStrategy, normCalculator);


        NormedLineImage result = Stream.generate(() -> source).limit(iterationCount)
                .parallel()
                .map(normedLineImageTransformer::transform)
                .min(Comparator.comparing(image -> image.norm))
                .get();

        System.out.println("Result speed " + result.norm);
        return createLineImage(result);
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
