package com.example.cpongs.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cpongs.logic.Board;
import com.example.cpongs.R;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private float linearAcceleration = 0;
    private long lastUpdate = 0;
    private float lastGyroscopeZ = 0.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        initBoard();
    }

    public void reset(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Subtracting the gravity vector from the raw accelerometer data to get linear acceleration
            linearAcceleration = event.values[0];
            // TODO update boardmanager
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            // Computing the change in orientation using the gyroscope sensor data
            float dt = (event.timestamp - lastUpdate) * 1.0e-9f;
            lastUpdate = event.timestamp;
            float gyroZ = event.values[2];
            float deltaZ = (gyroZ + lastGyroscopeZ) / 2.0f * dt;
            lastGyroscopeZ = gyroZ;

            //TODO update boradmanager

        }
    }

    protected void initBoard() {
        ImageView ballImageView = findViewById(R.id.ball);
        ImageView rocketImageView = findViewById(R.id.rocket);
        Board board = new Board(getDisplayWidthHeight());
    }

    protected Pair getDisplayWidthHeight() {
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;
        return new Pair(maxX, maxY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //TODO: onRestart!
}