package com.example.cpongs.logic;

import android.util.Pair;
import android.widget.ImageView;

public class Rocket extends GameObject {
    private enum AccelerationState {
        Halt,
        Increasing,
        Decreasing
    }

    private AccelerationState accelerationState;

    private final int length;
    private float angularVelocity;
    private float tilt;

    public Rocket(ImageView imageView, float x, float y, int width, int height) {
        super(imageView, x , y, width, height);
        length = imageView.getLayoutParams().width;
        imageView.setPivotX(this.x);
        accelerationState = AccelerationState.Halt;
    }

    public void updateAngularVelocity(float angularVelocity) {
        this.angularVelocity = (float) (0.5 * (this.angularVelocity + angularVelocity));
    }

    /*
     * Variable acceleration simulated by constant acceleration: Keep the same ax in same direction
     * When acceleration direction is changed, decrease the effect of decreasing acceleration
     * When phone is stopped i.e. acceleration is zero, need to halt the rocket
     * */

    public void updateAcceleration(float ax) {
        if (Math.abs(ax) < 5 && accelerationState == AccelerationState.Decreasing) {
            this.ax = 0;
            accelerationState = AccelerationState.Halt;
            return;
        }

        if (ax * this.ax > 0) {
            return;
        }

        float alpha = 0.6f;
        if (ax * this.ax < 0) {
            alpha = 0.95f;
            accelerationState = AccelerationState.Decreasing;
        } else {
            accelerationState = AccelerationState.Increasing;
        }

        // apply low-pass filter for noise reduction
        this.ax = alpha * this.ax + (1 - alpha) * ax;

        this.updatePosition(((float) Config.BOARD_REFRESH_RATE) / 1000);
    }

    @Override
    public void updatePosition(float intervalSeconds) {
        x += 0.5 * 400 * ax * Math.pow(intervalSeconds, 2);
        tilt += angularVelocity * intervalSeconds;
        imageView.setPivotX(x - length / 2f);
        handleWallCollision();
        refreshImage();
    }

    @Override
    protected void refreshImage() {
        imageView.setX(x - length / 2f);
//        imageView.setY(y);
        imageView.setRotation((float) Math.toDegrees(tilt));
    }

    public Pair<Float, Float> getStartPoint() {
        return new Pair<>((float) (x - (length / 2) * Math.cos(tilt)), (float) (y - (length / 2) * Math.sin(tilt)));
    }

    public Pair<Float, Float> getEndPoint() {
        return new Pair<>((float) (x + (length / 2) * Math.cos(tilt)), (float) (y + (length / 2) * Math.sin(tilt)));
    }

    public float getTilt() {
        return tilt;
    }

    @Override
    public void handleWallCollision() {
        if (getEndPoint().first >= width) {
            this.ax = 0;
            accelerationState = AccelerationState.Halt;
            this.x = (float) (width - (length / 2) * Math.cos(tilt));
        }
        if (getStartPoint().first <= 0) {
            this.ax = 0;
            accelerationState = AccelerationState.Halt;
            this.x = (float) ((length / 2) * Math.cos(tilt));
        }
    }
}
