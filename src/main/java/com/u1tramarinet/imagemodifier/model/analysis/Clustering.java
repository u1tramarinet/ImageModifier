package com.u1tramarinet.imagemodifier.model.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Clustering {
    private static final int TRIAL_COUNT = 10;
    private final Distance distance;

    public Clustering(Distance distance) {
        this.distance = distance;
    }

    public final Result execute(List<Pixel> pixels, int numberOfCluster) {
        List<Pixel> points = copyPixels(pixels);
        int degree = points.get(0).getDegree();

        // Create initial centroids
        List<Point> centroids = createInitialCentroids(numberOfCluster, points);

        for (int i = 0; i < TRIAL_COUNT; i++) {
            // Assign points to clusters
            for (Pixel pixel : points) {
                double nearestClusterDistance = Double.MAX_VALUE;
                int nearestClusterIdx = 0;
                for (int clusterIdx = 0; clusterIdx < numberOfCluster; clusterIdx++) {
                    double distance = this.distance.get(centroids.get(clusterIdx), pixel);
                    if (distance < nearestClusterDistance) {
                        nearestClusterDistance = distance;
                        nearestClusterIdx = clusterIdx;
                    }
                }
                pixel.setClusterIndex(nearestClusterIdx);
            }

            // Recreate centroids
            double[][] positions = new double[numberOfCluster][degree];
            int[] counts = new int[numberOfCluster];
            for (double[] array : positions) {
                Arrays.fill(array, 0);
            }
            Arrays.fill(counts, 0);
            for (Pixel pixel : points) {
                int clusterIdx = pixel.getClusterIndex();
                for (int axis = 0; axis < degree; axis++) {
                    positions[clusterIdx][axis] += pixel.get(axis);
                    counts[clusterIdx]++;
                }
            }
            for (int clusterIdx = 0; clusterIdx < numberOfCluster; clusterIdx++) {
                for (int axis = 0; axis < degree; axis++) {
                    positions[clusterIdx][axis] = positions[clusterIdx][axis] / counts[clusterIdx];
                }
                centroids.set(clusterIdx, new Point(positions[clusterIdx]));
            }
        }

        List<Pixel> out = new ArrayList<>();
        for (Pixel pixel : points) {
            int clusterIdx = pixel.getClusterIndex();
            Point centroid = centroids.get(clusterIdx);
            out.add(new Pixel(pixel.x, pixel.y, centroid));
        }
        return new Result(centroids, out, numberOfCluster);
    }

    private List<Pixel> copyPixels(List<Pixel> input) {
        List<Pixel> output = new ArrayList<>();
        for (Pixel pixel : input) {
            output.add(new Pixel(pixel.x, pixel.y, pixel));
        }
        return output;
    }

    private List<Point> createInitialCentroids(int number, List<Pixel> pixels) {
        int degree = pixels.get(0).getDegree();

        // Calculate range
        double[] minimums = new double[degree];
        double[] maximums = new double[degree];
        Arrays.fill(minimums, Double.MAX_VALUE);
        Arrays.fill(maximums, Double.MIN_VALUE);
        for (Pixel pixel : pixels) {
            for (int axis = 0; axis < degree; axis++) {
                double value = pixel.get(axis);
                if (maximums[axis] < value) {
                    maximums[axis] = value;
                }
                if (minimums[axis] > value) {
                    minimums[axis] = value;
                }
            }
        }

        // Calculate steps
        double[] step = new double[degree];
        for (int axis = 0; axis < degree; axis++) {
            step[axis] = (maximums[axis] - minimums[axis]) / (degree + 1);
        }

        // Assign indexes
        int[][] matrix = new int[degree][degree];
        for (int axis = 0; axis < degree; axis++) {
            for (int idx = 0; idx < number; idx++) {
                int clusterIdx = (axis + idx) % number;
                matrix[axis][clusterIdx] = idx;
            }
        }

        // Create centroids
        List<Point> points = new ArrayList<>();
        for (int clusterIdx = 0; clusterIdx < number; clusterIdx++) {
            double[] coordinates = new double[degree];
            for (int axis = 0; axis < degree; axis++) {
                coordinates[axis] = step[axis] * (matrix[axis][clusterIdx] + 1);
            }
            points.add(new Point(coordinates));
        }
        return points;
    }

    public static class Result {
        final List<Point> centroids;
        final List<Pixel> pixels;
        final int numberOfClusters;

        private Result(List<Point> centroids, List<Pixel> pixels, int numberOfClusters) {
            this.centroids = centroids;
            this.pixels = Collections.unmodifiableList(pixels);
            this.numberOfClusters = numberOfClusters;
        }
    }
}
