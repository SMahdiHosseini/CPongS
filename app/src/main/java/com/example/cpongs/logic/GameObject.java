package com.example.cpongs.logic;

import android.widget.ImageView;

public abstract class GameObject {
    protected ImageView imageView;

    protected double mass;

    protected double x;
    protected double y;

    protected double vx;
    protected double vy;

    protected double ax;
    protected double ay;

    public GameObject(ImageView imageView, double mass, double x, double y) {
        this.imageView = imageView;
        this.mass = mass;

        this.x = x;
        this.y = y;
    }

    public void updateVelocity(double intervalSeconds) {
        vx = ax * intervalSeconds + vx;
        vy = ay * intervalSeconds + vy;
    }

    protected void refreshImage() {
        imageView.setX((float) x);
        imageView.setY((float) y);
    }
}
