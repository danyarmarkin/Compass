package com.onlinetest.android.compass;

import android.widget.TextView;

public class CompassDataAdapter extends Thread {

    private BluetoothService mBluetoothService;
    private CompassService mCompassService;

    private boolean mShowDegree = false;
    private boolean mSendDegree = false;

    private TextView mDegreeTextView;

    public static final int FREQUENCY_OUT = 100;
    public static final int FREQUENCY_IN = 500;
    private int[] val;
    private int index = 0;

    public CompassDataAdapter(CompassService compassService) {
        mCompassService = compassService;
        mBluetoothService = null;
        val = new int[Math.round(FREQUENCY_IN / FREQUENCY_OUT)];
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            int drg = mCompassService.getDegree();

            val[index] = drg;
            boolean b = index == val.length - 1;
            index = (index + 1) % val.length;

            if (b) {
                int sum = 0;
                for (int i : val) sum += i;
                drg = Math.round(sum / val.length);
            }

            if (isShowDegree() && b) mDegreeTextView.setText(String.valueOf(drg));

            if (isSendDegree() && b) mBluetoothService.sendData(String.valueOf(drg));

            try {
                sleep((long) Math.floor(1000 / FREQUENCY_IN));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public void setDegreeTextView(TextView degreeTextView) {
        mDegreeTextView = degreeTextView;
    }
}
