package com.onlinetest.android.compass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private TextView mDegreeText;
    private Button mSendButton;

    private SensorManager mSensorManager;



    private static final byte LAMPS_AMOUNT = 16;
    private static final int SEND_DATA_INTERVAL = 75;
    private float mDrg = 0f;
    private BluetoothService mBluetoothService;

    private CompassService mCompassService;

    public static final String DEGREE_EXTRA = "com.onlinetest.android.compass.degree";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDegreeText = findViewById(R.id.degree);
        mSendButton = findViewById(R.id.send);

//        bluetoothConfig();
        mCompassService = new CompassService((SensorManager) getSystemService(SENSOR_SERVICE));
        mCompassService.openListener();

        CompassDataAdapter compassDataAdapter = new CompassDataAdapter(mCompassService);
        compassDataAdapter.setDegreeTextView(mDegreeText);
        compassDataAdapter.setShowDegree(true);
        compassDataAdapter.start();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int d = mCompassService.getDegree();
                mDegreeText.setText(String.valueOf(d));
            }
        });
    }

    private void bluetoothConfig() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        mBluetoothService = new BluetoothService(ba);
        int code = mBluetoothService.checkBTState();
        switch (code) {
            case BluetoothService.BLUETOOTH_NOT_AVAILABLE_CODE:
                errorExit("Fatal Error", "Bluetooth не поддерживается");
                break;
            case BluetoothService.BLUETOOTH_SWITCH_OFF_CODE:
                Intent enableBtIntent = new Intent(ba.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, BluetoothService.REQUEST_ENABLE_BT);
                break;
        }
        mBluetoothService.connect();
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        int calibrateDegree = data.getIntExtra(DEGREE_EXTRA, 0);
    }
}