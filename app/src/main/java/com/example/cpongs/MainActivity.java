package com.example.cpongs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private final float[] linearAcceleration = new float[3];
    private long lastUpdate = 0;
    private float lastGyroscopeX = 0.0f;
    private float lastGyroscopeY = 0.0f;
    private float lastGyroscopeZ = 0.0f;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textViewSensor);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            // Subtracting the gravity vector from the raw accelerometer data to get linear acceleration
            linearAcceleration[0] = event.values[0];

            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                lastUpdate = currentTime;

                // Computing the angle of the phone relative to a flat surface
//                float pitch = (float) Math.atan2(linearAcceleration[0], Math.sqrt(linearAcceleration[1] * linearAcceleration[1] + linearAcceleration[2] * linearAcceleration[2]));
//                float roll = (float) Math.atan2(linearAcceleration[1], Math.sqrt(linearAcceleration[0] * linearAcceleration[0] + linearAcceleration[2] * linearAcceleration[2]));

//                Log.d("MainActivity", "Pitch: " + Math.toDegrees(pitch) + ", Roll: " + Math.toDegrees(roll));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText("ACC: " + linearAcceleration[0]);
                    }
                });
            }
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                lastUpdate = currentTime;

                // Computing the change in orientation using the gyroscope sensor data
                float dt = (event.timestamp - lastUpdate) * 1.0e-9f;
                float gyroX = event.values[0];
                float gyroY = event.values[1];
                float gyroZ = event.values[2];
                float deltaX = (gyroX + lastGyroscopeX) / 2.0f * dt;
                float deltaY = (gyroY + lastGyroscopeY) / 2.0f * dt;
                float deltaZ = (gyroZ + lastGyroscopeZ) / 2.0f * dt;

                lastGyroscopeX = gyroX;
                lastGyroscopeY = gyroY;
                lastGyroscopeZ = gyroZ;

                Log.d("MainActivity", "DeltaX: " + Math.toDegrees(deltaX) + ", DeltaY: " + Math.toDegrees(deltaY) + ", DeltaZ: " + Math.toDegrees(deltaZ));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
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
}