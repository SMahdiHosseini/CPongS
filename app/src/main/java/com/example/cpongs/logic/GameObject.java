package com.example.cpongs.logic;

import android.widget.ImageView;

public abstract class GameObject {
    protected ImageView imageView;
    protected float x;
    protected float y;
    protected float ax;
    protected float ay;

    protected int width;
    protected int height;

    public GameObject(ImageView imageView, float x, float y, int width, int height) {
        this.imageView = imageView;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void updatePosition(float intervalSeconds);
    public abstract void handleWallCollision();

    protected void refreshImage() {
        imageView.setX(x);
        imageView.setY(y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
