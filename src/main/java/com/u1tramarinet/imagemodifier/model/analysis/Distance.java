package com.u1tramarinet.imagemodifier.model.analysis;

public class Distance<T extends Point> {
    public double get(T one, T another) {
        if (one.getDegree() != another.getDegree()) {
            throw new IllegalArgumentException();
        }
        double result = 0;
        for (int i = 0; i < one.getDegree(); i++) {
            result += Math.pow(one.get(i) - another.get(i), 2);
        }
        return Math.sqrt(result);
    }
}
