package com.onlinetest.android.compass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {

    public static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "BluetoothService";
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;

    public static final int BLUETOOTH_NOT_AVAILABLE_CODE = -1;
    public static final int BLUETOOTH_SWITCH_OFF_CODE = 1;
    public static final int BLUETOOTH_SWITCH_ON_CODE = 0;

    private String address;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothService(BluetoothAdapter bluetoothAdapter) {
        btAdapter = bluetoothAdapter;
        address = "98:D3:31:90:3A:36";
    }

    public BluetoothService(String address, BluetoothAdapter bluetoothAdapter) {
        btAdapter = bluetoothAdapter;
        this.address = address;
    }

    public void connect() {
        Log.d(TAG, "...onResume - попытка соединения...");
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }
        btAdapter.cancelDiscovery();
        Log.d(TAG, "...Соединяемся...");
        try {
            btSocket.connect();
            Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }
        Log.d(TAG, "...Создание Socket...");
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.e("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    public int checkBTState() {
        if(btAdapter==null) return BLUETOOTH_NOT_AVAILABLE_CODE;

        if (btAdapter.isEnabled()) return BLUETOOTH_SWITCH_ON_CODE;

        return BLUETOOTH_SWITCH_OFF_CODE;
    }

    public void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        Log.d(TAG, "...Посылаем данные: " + message + "...");

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nВ переменной address у вас прописан 00:00:00:00:00:00, вам необходимо прописать реальный MAC-адрес Bluetooth модуля";
            msg = msg +  ".\n\nПроверьте поддержку SPP UUID: " + MY_UUID.toString() + " на Bluetooth модуле, к которому вы подключаетесь.\n\n";

            Log.e("Fatal Error", msg);
        }
    }
}
