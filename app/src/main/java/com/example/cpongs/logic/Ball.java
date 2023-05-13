package com.example.cpongs.logic;

import android.widget.ImageView;

public class Ball extends GameObject {
    private final float radius;
    private float vx;
    private float vy;

    public Ball(ImageView imageView, float x, float y, float radius) {
        super(imageView, x, y);
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
        refreshImage();
    }

    public void handleRocketCollision(float tilt) {
        float newVx = (float) (-vy*Math.sin(2*tilt) + vx*Math.cos(2*tilt));
        float newVy = (float) (vy*Math.cos(2*tilt) - vx*Math.sin(2*tilt));
        vx = newVx;
        vy = newVy;
    }

    public float getRadius() {
        return radius;
    }
}