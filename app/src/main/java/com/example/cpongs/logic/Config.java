package com.example.cpongs.logic;

public class Config {
    public static final double Mass1 = 0.01, Mass2 = 0.05;  // 1gr - 5gr
    public static final int BALL_WIDTH = 150;
    public enum sensorType{GYROSCOPE, GRAVITY};
    public static final double g = 9.81;
    public static final double M_s = 0.15, M_k = 0.10;
    public static final int BOARD_REFRESH_RATE = 10; // 10 milliseconds

    public static final float NS2S = 1.0f / 1000000000.0f;  // ns to second
    public static final double SPEED_UP = 20;
    public static final double BALL_STOP_SPEED_THRESHOLD = 0.1;
}

