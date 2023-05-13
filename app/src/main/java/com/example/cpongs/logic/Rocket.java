package com.example.cpongs.logic;

import android.util.Pair;
import android.widget.ImageView;

public class Rocket extends GameObject {
    private final int length;
    private float angularVelocity;
    private float tilt;

    public Rocket(ImageView imageView, float x, float y) {
        super(imageView, x, y);
        imageView.setPivotX(x);
        imageView.setPivotY(y);
        length = imageView.getLayoutParams().width;
    }

    public void updateAngularVelocity(float angularVelocity) {
        this.angularVelocity = (float) (0.5 * (this.angularVelocity + angularVelocity));
    }

    public void updateAcceleration(float ax) {
        final float alpha = 0.8f;
        // apply low-pass filter for noise reduction
        this.ax = alpha * this.ax + (1 - alpha) * ax;
    }

    @Override
    public void updatePosition(float intervalSeconds) {
        x += 0.5 * ax * Math.pow(intervalSeconds, 2);
        tilt += angularVelocity * intervalSeconds;
        refreshImage();
    }

    @Override
    protected void refreshImage() {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setRotation((float) Math.toDegrees(tilt));
    }

    public int getLength() {
        return length;
    }

    public Pair<Float, Float> getStartPoint() {
        return new Pair<>((float) (x - length / 2 * Math.cos(tilt)), (float) (y - length / 2 * Math.sin(tilt)));
    }

    public Pair<Float, Float> getEndPoint() {
        return new Pair<>((float) (x + length / 2 * Math.cos(tilt)), (float) (y + length / 2 * Math.sin(tilt)));
    }

    public float getTilt() {
        return tilt;
    }
}
