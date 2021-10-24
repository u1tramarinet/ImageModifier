package com.u1tramarinet.imagemodifier.model.analysis;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private final List<Double> coordinateList = new ArrayList<>();
    private int clusterIndex = 0;

    public Point(double... coordinates) {
        for (double coordinate : coordinates) {
            coordinateList.add(coordinate);
        }
    }

    public Point(Point point) {
        for (int i = 0; i < point.getDegree(); i++) {
            coordinateList.add(point.get(i));
        }
        this.clusterIndex = point.clusterIndex;
    }

    public int getDegree() {
        return coordinateList.size();
    }

    public double get(int index) {
        if (coordinateList.size() <= index) {
            throw new IllegalArgumentException();
        }
        return coordinateList.get(index);
    }

    void setClusterIndex(int clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    int getClusterIndex() {
        return clusterIndex;
    }
}
