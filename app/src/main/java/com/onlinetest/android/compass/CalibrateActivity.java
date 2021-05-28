package com.onlinetest.android.compass;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CalibrateActivity extends AppCompatActivity {

    private Button mStartCalibrateButton;
    private Button mDoneButton;

    private EditText mMacEditText;

    private int mDegree = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        mStartCalibrateButton = findViewById(R.id.calibrate_start);
        mStartCalibrateButton.setOnClickListener(view -> {
            Calibrate calibrate = new Calibrate();
            calibrate.start();
        });

        mDoneButton = findViewById(R.id.calibrate_done);
        mDoneButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(MainActivity.DEGREE_EXTRA, mDegree);
            setResult(RESULT_OK, intent);
            finish();
        });

    }

    public class Calibrate extends Thread {
        Calibrate () {

        }

        @Override
        public void run () {
            super.run();

            int sum = 0;
            CompassService compassService = new CompassService((SensorManager) getSystemService(SENSOR_SERVICE));
            compassService.openListener();

            for (int i = 0; i < 200; i++) {

                sum += compassService.getDegree();

                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mDegree = Math.round(sum / 200);

            compassService.closeListener();
        }
    }
}
