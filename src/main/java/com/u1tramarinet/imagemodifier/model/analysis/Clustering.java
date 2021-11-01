package com.u1tramarinet.imagemodifier.model.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Clustering<P extends Point> {
    private static final int TRIAL_COUNT = 10;
    private final Distance<P> distance;

    public Clustering(Distance<P> distance) {
        this.distance = distance;
    }

    public final Result<P> execute(List<Pixel<P>> pixels, int numberOfCluster) {
        List<Pixel<P>> points = copyPixels(pixels);
        int degree = points.get(0).point.getDegree();

        // Create initial centroids
        List<Point> centroids = createInitialCentroids(numberOfCluster, points);

        for (int i = 0; i < TRIAL_COUNT; i++) {
            // Assign points to clusters
            for (Pixel<P> pixel : points) {
                double nearestClusterDistance = Double.MAX_VALUE;
                int nearestClusterIdx = 0;
                for (int clusterIdx = 0; clusterIdx < numberOfCluster; clusterIdx++) {
                    double distance = this.distance.get((P) centroids.get(clusterIdx), pixel.point);
                    if (distance < nearestClusterDistance) {
                        nearestClusterDistance = distance;
                        nearestClusterIdx = clusterIdx;
                    }
                }
                pixel.point.setClusterIndex(nearestClusterIdx);
            }

            // Recreate centroids
            double[][] positions = new double[numberOfCluster][degree];
            int[] counts = new int[numberOfCluster];
            for (double[] array : positions) {
                Arrays.fill(array, 0);
            }
            Arrays.fill(counts, 0);
            for (Pixel<P> pixel : points) {
                int clusterIdx = pixel.point.getClusterIndex();
                for (int axis = 0; axis < degree; axis++) {
                    positions[clusterIdx][axis] += pixel.point.get(axis);
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

        List<Pixel<P>> out = new ArrayList<>();
        for (Pixel<P> pixel : points) {
            int clusterIdx = pixel.point.getClusterIndex();
            Point centroid = centroids.get(clusterIdx);
            out.add(new Pixel(pixel.x, pixel.y, centroid));
        }
        return new Result<P>(centroids, out, numberOfCluster);
    }

    private List<Pixel<P>> copyPixels(List<Pixel<P>> input) {
        List<Pixel<P>> output = new ArrayList<>();
        for (Pixel<P> pixel : input) {
            output.add(new Pixel<>(pixel.x, pixel.y, pixel));
        }
        return output;
    }

    private List<Point> createInitialCentroids(int numberOfCluster, List<Pixel<P>> pixels) {
        int degree = pixels.get(0).point.getDegree();

        // Calculate range
        double[] minimums = new double[degree];
        double[] maximums = new double[degree];
        Arrays.fill(minimums, Double.MAX_VALUE);
        Arrays.fill(maximums, Double.MIN_VALUE);
        for (Pixel<P> pixel : pixels) {
            for (int axis = 0; axis < degree; axis++) {
                double value = pixel.point.get(axis);
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
            step[axis] = (maximums[axis] - minimums[axis]) / (numberOfCluster + 1);
        }

        // Assign indexes
        int[][] matrix = new int[degree][numberOfCluster];
        for (int axis = 0; axis < degree; axis++) {
            for (int idx = 0; idx < numberOfCluster; idx++) {
                int clusterIdx = (axis + idx) % numberOfCluster;
                matrix[axis][clusterIdx] = idx;
            }
        }

        // Create centroids
        List<Point> points = new ArrayList<>();
        for (int clusterIdx = 0; clusterIdx < numberOfCluster; clusterIdx++) {
            double[] coordinates = new double[degree];
            for (int axis = 0; axis < degree; axis++) {
                coordinates[axis] = step[axis] * (matrix[axis][clusterIdx] + 1);
            }
            points.add(new Point(coordinates));
        }
        return points;
    }

    public static class Result<P extends Point> {
        final List<Point> centroids;
        final List<Pixel<P>> pixels;
        final int numberOfClusters;

        private Result(List<Point> centroids, List<Pixel<P>> pixels, int numberOfClusters) {
            this.centroids = centroids;
            this.pixels = Collections.unmodifiableList(pixels);
            this.numberOfClusters = numberOfClusters;
        }
    }
}
