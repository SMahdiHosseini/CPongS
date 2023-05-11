package com.example.cpongs;

import android.util.Pair;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BoardManager {
    List<Ball> balls = new ArrayList<>();
    int width, height;
    double angleX, angleY, angleZ;  // Only X and Y matter // so why we have angleZ :)))
    double Ax, Ay, Az;

    public BoardManager(Pair<Integer, Integer> widthHeight) {
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

    public int getBallsCount(){
        return balls.size();
    }

    protected synchronized TimerTask moveBalls(double intervalSeconds) {
        for (Ball ball : balls) {
            ball.updateAccelerationByAngles(angleX, angleY, angleZ);
            ball.updateVelocity(intervalSeconds, this);
            Pair<Double, Double> ballNewPositions = ball.getNextPosition(intervalSeconds);
            ball.handleWallCollision(ballNewPositions.first, ballNewPositions.second, this);
            ball.updateAccelerationByAngles(angleX, angleY, angleZ);
            ball.updateVelocity(intervalSeconds, this);
        }

        for (int i = 0; i < balls.size(); i++) {  // Handle Ball Collision
            for (int j = i + 1; j < balls.size(); j++) {
                Ball ball1 = balls.get(i);
                Ball ball2 = balls.get(j);
                Pair<Double, Double> ball1NewPositions = ball1.getNextPosition(intervalSeconds);
                Pair<Double, Double> ball2NewPositions = ball2.getNextPosition(intervalSeconds);
                if (doBallsHit(ball1NewPositions.first, ball2NewPositions.first, ball1NewPositions.second, ball2NewPositions.second)) {
                    handleBallCollision(ball1, ball2, intervalSeconds);
                }
            }
        }

        for (Ball ball : balls) {  // Handle Wall Collision and update position

            ball.updatePosition(intervalSeconds);
        }
        return null;
    }

    public void handleBallCollision(Ball ball1, Ball ball2, double intervalSeconds) {
        Pair<Double, Double> ball1Positions = ball1.getNextPosition(intervalSeconds);
        Pair<Double, Double> ball2Positions = ball2.getNextPosition(intervalSeconds);

        DirectionVector n = new DirectionVector(ball2Positions.first - ball1Positions.first, ball2Positions.second - ball1Positions.second);
        DirectionVector un = n.divideBy(n.magnitude());
        DirectionVector ut = new DirectionVector(-un.getY(), un.getX());
        DirectionVector v1 = new DirectionVector(ball1.vx, ball1.vy);
        DirectionVector v2 = new DirectionVector(ball2.vx, ball2.vy);
        double v1n = un.dotProduct(v1);
        double v2n = un.dotProduct(v2);
        double v1t = ut.dotProduct(v1);
        double v2t = ut.dotProduct(v2);
        double newV1t = v1t;
        double newV2t = v2t;
        double m1 = ball1.m;
        double m2 = ball2.m;
        double newV1n = (v1n * (m1 - m2) + 2 * m2 * v2n) / (m1 + m2);
        double newV2n = (v2n * (m2 - m1) + 2 * m1 * v1n) / (m1 + m2);
        DirectionVector newV1nVector = un.multiplyBy(newV1n);
        DirectionVector newV2nVector = un.multiplyBy(newV2n);
        DirectionVector newV1tVector = ut.multiplyBy(newV1t);
        DirectionVector newV2tVector = ut.multiplyBy(newV2t);

        DirectionVector newV1 = newV1nVector.plus(newV1tVector);
        DirectionVector newV2 = newV2nVector.plus(newV2tVector);

        ball1.vx = newV1.getX();
        ball1.vy = newV1.getY();
        ball2.vx = newV2.getX();
        ball2.vy = newV2.getY();
    }


    public boolean doBallsHit(double b1x, double b2x, double b1y, double b2y) {
        return getDistanceOfPoints(b1x, b2x, b1y, b2y) < Config.BALL_WIDTH;
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
