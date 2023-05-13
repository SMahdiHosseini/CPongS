package com.example.cpongs.logic;

import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Board {
    List<Ball> balls = new ArrayList<>();
    int width, height;
    double angleX, angleY, angleZ;  // Only X and Y matter // so why we have angleZ :)))
    double Ax, Ay, Az;

    public Board(Pair<Integer, Integer> widthHeight) {
        width = widthHeight.first;
        height = widthHeight.second;

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                moveBalls(((double) Config.BOARD_REFRESH_RATE) / 1000);
            }
        }, 0, Config.BOARD_REFRESH_RATE);
    }

    public int getBallsCount() {
        return balls.size();
    }

    protected synchronized TimerTask moveBalls(double intervalSeconds) {
        for (Ball ball : balls) {
            ball.updateAccelerationByAngles(angleX, angleY, angleZ);
            ball.updateVelocity(intervalSeconds);
            Pair<Double, Double> ballNewPositions = ball.getNextPosition(intervalSeconds);
            ball.handleWallCollision(ballNewPositions.first, ballNewPositions.second, this);
            ball.updateAccelerationByAngles(angleX, angleY, angleZ);
            ball.updateVelocity(intervalSeconds);
        }


        for (Ball ball : balls) {  // Handle Wall Collision and update position
            ball.updatePosition(intervalSeconds);
        }
        return null;
    }

    public double getDistanceOfPoints(double b1x, double b2x, double b1y, double b2y) {
        return Math.sqrt(Math.pow(b1x - b2x, 2) + Math.pow(b1y - b2y, 2));
    }

    public boolean doesHitWall(double x, double y) {
        return x >= width || x <= 0 || y >= height || y <= 0;
    }

    public synchronized void addBall(ImageView ballImageView, double mass, double x, double y) {
        balls.add(new Ball(ballImageView, mass, x, y));
    }

    public void updateAnglesByGyroscope(float axisSpeedX, float axisSpeedY, float axisSpeedZ, float dT) {
        angleX = angleX + axisSpeedX * dT;
        angleY = angleY + axisSpeedY * dT;
        angleZ = angleZ + axisSpeedZ * dT;
//        Log.i("ANGLE", ""+angleX + "|" + angleY + "|" + angleZ + "|" + dT);
    }

    public void updateAccelerationByGravity(float axisXAcceleration, float axisYAcceleration, float axisZAcceleration) {
        angleX = Math.asin(axisXAcceleration / Config.g);
        angleY = Math.asin(axisYAcceleration / Config.g);
        angleZ = Math.asin(axisZAcceleration / Config.g);
    }
}
