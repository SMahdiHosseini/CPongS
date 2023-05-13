package com.example.cpongs.logic;

import android.widget.ImageView;

public abstract class GameObject {
    protected ImageView imageView;
    protected float x;
    protected float y;
    protected float ax;
    protected float ay;

    public GameObject(ImageView imageView, float x, float y) {
        this.imageView = imageView;

        this.x = x;
        this.y = y;
    }

    public abstract void updatePosition(double intervalSeconds);
    protected void refreshImage() {
        imageView.setX(x);
        imageView.setY(y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
