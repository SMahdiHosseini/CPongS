package com.example.cpongs.logic.Normal;

import android.widget.ImageView;

import com.example.cpongs.logic.Config;
import com.example.cpongs.logic.GameObject;

public class Ball extends GameObject {
    private final float radius;
    private float vx;
    private float vy;

    public Ball(ImageView imageView, float x, float y, int width, int height, float radius) {
        super(imageView, x, y, width, height);
        this.radius = radius;
        ax = 0;
        ay = Config.g * Config.pixelsPerMeter;
    }

    public void updateVelocity(float intervalSeconds) {
        vy += ay * intervalSeconds;
    }

    @Override
    public void updatePosition(float intervalSeconds) {
        x = (float) (0.5 * ax * Math.pow(intervalSeconds, 2) + vx * intervalSeconds + x);
        y = (float) (0.5 * ay * Math.pow(intervalSeconds, 2) + vy * intervalSeconds + y);
        updateVelocity(intervalSeconds);
        handleWallCollision();
        refreshImage();
    }

    public void handleRocketCollision(float tilt) {
        float newVx = (float) (vy*Math.sin(2*tilt) + vx*Math.cos(2*tilt));
        float newVy = (float) (-vy*Math.cos(2*tilt) - vx*Math.sin(2*tilt));
        vx = newVx;
        vy = newVy;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void handleWallCollision() {
        if (x + radius >= width || x  - radius <= 0)
            this.vx = - vx;
        if (y  + radius >= height || y - radius <= 0)
            this.vy = - vy;
    }

    @Override
    public void refreshImage() {
        imageView.setX(x - radius);
        imageView.setY(y - radius);
    }
}