package com.example.cpongs.logic;

import android.util.Pair;
import android.widget.ImageView;

public class Ball extends GameObject{
    double width = Config.BALL_WIDTH;

    public Ball(ImageView imageView, double mass, double x, double y) {
        super(imageView, mass, x, y);
        imageView.getLayoutParams().height = (int)width;
        imageView.getLayoutParams().width = (int)width;
        imageView.requestLayout();
    }


    public Pair<Double, Double> getNextPosition(double intervalSeconds) {
        double newX = 0.5 * ax * Math.pow(intervalSeconds, 2) + vx * intervalSeconds + x;
        double newY = 0.5 * ay * Math.pow(intervalSeconds, 2) + vy * intervalSeconds + y;
        return new Pair<>(newX, newY);
    }

    public void updatePosition(double intervalSeconds) {
        x = 0.5 * ax * Math.pow(intervalSeconds, 2) + vx * intervalSeconds + x;
        y = 0.5 * ay * Math.pow(intervalSeconds, 2) + vy * intervalSeconds + y;

        refreshImage();
    }


    public void handleWallCollision(double newX, double newY, Board board) {
        if (board.doesHitWall(newX + width / 2, newY)) {
            vy = Math.abs(vy);
        }
        if (board.doesHitWall(newX + width / 2, newY + width)) {
            vy = -Math.abs(vy);
        }
        if (board.doesHitWall(newX, newY + width / 2)) {
            vx = Math.abs(vx);
        }
        if (board.doesHitWall(newX + width, newY + width / 2)) {
            vx = -Math.abs(vx);
        }
    }


    public void updateAccelerationByAngles(double angleX, double angleY, double angleZ) {
        double fX = mass * Config.g * Math.sin(angleY);
        double fY = mass * Config.g * Math.sin(angleX);
        double N = mass * Config.g * Math.cos(Math.atan(euclideanNorm(Math.sin(angleX), Math.sin(angleY)) / (Math.cos(angleX) + Math.cos(angleY))));
        if (this.isMoving() || this.canMove(fX, fY, N)) {
            double frictionMagnitude = N * Config.M_k;
            double frictionX = 0;
            double frictionY = 0;
            if (euclideanNorm(vx, vy) > 0) {
                frictionX = frictionMagnitude * vx / euclideanNorm(vx, vy);
                frictionY = frictionMagnitude * vy / euclideanNorm(vx, vy);
            }
            fX += -Math.signum(vx) * Math.abs(frictionX);
            fY += -Math.signum(vy) * Math.abs(frictionY);
        } else {
            fX = 0;
            fY = 0;
        }
        ax = fX / mass;
        ay = fY / mass;

        ax *= Config.SPEED_UP;
        ay *= Config.SPEED_UP;
    }


    private boolean canMove(double fX, double fY, double N) {
        double fMagnitude = euclideanNorm(fX, fY);
        double frictionMagnitude = N * Config.M_k;
        return fMagnitude > frictionMagnitude;
    }

    private double euclideanNorm(double a, double b) {  // Length of Vector (Magnitude)
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    private boolean isMoving() {
        return !(euclideanNorm(vx, vy) < Config.BALL_STOP_SPEED_THRESHOLD);
    }
}