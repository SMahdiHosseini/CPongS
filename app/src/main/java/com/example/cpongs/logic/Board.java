package com.example.cpongs.logic;

import android.util.Pair;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;


public class Board {
    Ball ball;
    Rocket rocket;
    int width, height;

    public Board(Pair<Integer, Integer> widthHeight) {
        width = widthHeight.first;
        height = widthHeight.second;

        Timer t = new Timer();
        // 100 fps
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                update(((float) Config.BOARD_REFRESH_RATE) / 1000);
            }
        }, 0, Config.BOARD_REFRESH_RATE);
    }

    protected synchronized TimerTask update(float intervalSeconds) {
        ball.updateAccelerationByAngles(angleX, angleY, angleZ);
        ball.updateVelocity(intervalSeconds);
        Pair<float, float> ballNewPositions = ball.getNextPosition(intervalSeconds);
        ball.handleWallCollision(ballNewPositions.first, ballNewPositions.second, this);
        ball.updateAccelerationByAngles(angleX, angleY, angleZ);
        ball.updateVelocity(intervalSeconds);


        for (Ball ball : balls) {  // Handle Wall Collision and update position
            ball.updatePosition(intervalSeconds);
        }
        return null;
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y2 - y1, 2));
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

    public void handleWallCollision(){
        handleBallWallCollision();
        handleBallRocketCollision();
    }

    public void handleBallWallCollision(){

    }

    public void handleBallRocketCollision(){
        if (checkRocketBallCollision()){

        }
    }
    public boolean checkRocketBallCollision() {
        Pair<Float, Float> rocketStartPoint = rocket.getStartPoint();
        Pair<Float, Float> rocketEndPoint = rocket.getEndPoint();
        float distance = distanceToLineSegment(rocketStartPoint.first, rocketStartPoint.second,
                rocketEndPoint.first, rocketEndPoint.second, ball.getX(), ball.getY(), ball.getRadius());
        return distance <= 0;
    }

    private static float distanceToLineSegment(float lineStartX, float lineStartY, float lineEndX,
                                               float lineEndY, float circleX, float circleY, float circleRadius) {
        // Calculate the vector from the start point of the line segment to the end point of the line segment
        float lineVectorX = lineEndX - lineStartX;
        float lineVectorY = lineEndY - lineStartY;

        // Calculate the vector from the start point of the line segment to the center of the circle
        float pointVectorX = circleX - lineStartX;
        float pointVectorY = circleY - lineStartY;

        // Project the point vector onto the line vector
        float dotProduct = lineVectorX * pointVectorX + lineVectorY * pointVectorY;
        float lineLengthSquared = lineVectorX * lineVectorX + lineVectorY * lineVectorY;
        float t = dotProduct / lineLengthSquared;

        // Check if the projection falls outside the line segment
        if (t < 0) {
            // The closest point is the start point of the line segment
            return distance(circleX, circleY, lineStartX, lineStartY) - circleRadius;
        } else if (t > 1) {
            // The closest point is the end point of the line segment
            return distance(circleX, circleY, lineEndX, lineEndY) - circleRadius;
        } else {
            // Calculate the closest point on the line segment
            float projectionX = lineStartX + t * lineVectorX;
            float projectionY = lineStartY + t * lineVectorY;

            // Calculate the distance between the circle and the closest point on the line segment
            float distanceToLine = distance(circleX, circleY, projectionX, projectionY);
            return distanceToLine - circleRadius;
        }
    }
}
