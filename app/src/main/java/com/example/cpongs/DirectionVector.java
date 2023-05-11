package com.example.cpongs;

public class DirectionVector {
    private double x, y;

    DirectionVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double dotProduct(DirectionVector dv2) {
        return x*dv2.x + y*dv2.y;
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public DirectionVector divideBy(double d) {
        return new DirectionVector(x / d, y / d);
    }

    public DirectionVector multiplyBy(double m) {
        return new DirectionVector(x * m, y * m);
    }

    public DirectionVector plus(DirectionVector a) {
        return new DirectionVector(x + a.x, y + a.y);
    }

}
