package com.example.cpongs.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cpongs.logic.Normal.Ball;
import com.example.cpongs.logic.Normal.Board;
import com.example.cpongs.R;
import com.example.cpongs.logic.Config;
import com.example.cpongs.logic.Normal.Rocket;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    Board board;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

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
            float linearAcceleration = event.values[0] * Config.pixelsPerMeter;
            board.updateRocketAcceleration(linearAcceleration);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float gyroZ = event.values[2];
            board.updateRocketTilt(-gyroZ);
        }
    }

    protected void initBoard() {
        Pair<Integer, Integer> pageSize = getDisplayWidthHeight();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        Config.pixelsPerMeter = density * 1300;
        ImageView ballImageView = findViewById(R.id.ball);
        Ball ball = new Ball(ballImageView, (float) (pageSize.first * 0.5), (float) (pageSize.second * 0.2),
                pageSize.first, pageSize.second, (float) (ballImageView.getLayoutParams().width / 2.0));
        ImageView rocketImageView = findViewById(R.id.rocket);
        Rocket rocket = new Rocket(rocketImageView, (float) (pageSize.first * 0.5), (float) (pageSize.second * 0.8),
                pageSize.first, pageSize.second);
        board = new Board(pageSize, ball, rocket);
    }

    protected Pair<Integer, Integer> getDisplayWidthHeight() {
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;
        return new Pair<>(maxX, maxY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

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