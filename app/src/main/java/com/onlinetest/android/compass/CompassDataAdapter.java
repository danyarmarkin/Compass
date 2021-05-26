package com.onlinetest.android.compass;

public class CompassDataAdapter extends Thread {

    private BluetoothService mBluetoothService;

    private boolean mShowDegree;
    private boolean mSendDegree;

    public CompassDataAdapter(BluetoothService bluetoothService) {
        mBluetoothService = bluetoothService;
    }

    public CompassDataAdapter() {
        mBluetoothService = null;
    }

    @Override
    public void run() {
        super.run();


    }

    public boolean isShowDegree() {
        return mShowDegree;
    }

    public void setShowDegree(boolean showDegree) {
        mShowDegree = showDegree;
    }

    public boolean isSendDegree() {
        return mSendDegree;
    }

    public void setSendDegree(boolean sendDegree) {
        mSendDegree = sendDegree;
    }

    public void setBluetoothService(BluetoothService bluetoothService) {
        mBluetoothService = bluetoothService;
    }
}
