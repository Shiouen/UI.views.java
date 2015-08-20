package utilities;

import java.util.List;

public class MathUtilities {
    public static double calculateMean(List<String> doubleStrings) {
        double sum = 0.0;

        for (String s : doubleStrings) {
            sum += Double.parseDouble(s);
        }

        return sum / (double) doubleStrings.size();
    }

    public static double calculateStandardDeviation(List<String> doubleStrings) {
        return Math.sqrt(calculateVariance(doubleStrings));
    }

    public static double calculateVariance(List<String> doubleStrings) {
        double mean = calculateMean(doubleStrings);

        double squareSum = 0;
        for (String s : doubleStrings) {
            squareSum += Math.pow(Double.parseDouble(s) - mean, 2);
        }

        return squareSum / (double) (doubleStrings.size() - 1);
    }
}
