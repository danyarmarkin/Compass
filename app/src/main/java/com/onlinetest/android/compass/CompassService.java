package com.onlinetest.android.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class CompassService implements SensorEventListener {

    private int degree;
    private SensorManager mSensorManager;

    public CompassService(SensorManager sensorManager) {
        mSensorManager = sensorManager;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        degree = Math.round(sensorEvent.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void openListener() {
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void closeListener() {
        mSensorManager.unregisterListener(this);
    }

    public int getDegree() {
        return degree;
    }
}
