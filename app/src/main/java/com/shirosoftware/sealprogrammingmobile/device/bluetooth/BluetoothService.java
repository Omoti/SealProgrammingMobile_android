package com.shirosoftware.sealprogrammingmobile.device.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Binder;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BluetoothService {
    private static final String LOG_TAG = "BluetoothService";

    //UUID
    private static final UUID UUID_SERIAL = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //接続中のBluetoothデバイス
    private BluetoothDevice mDevice = null;

    //BluetoothAdapter
    private BluetoothAdapter mBluetoothAdapter = null;

    //BluetoothSocket
    private BluetoothSocket mSocket = null;

    //Bluetoothコマンド送受信
    BluetoothCommandThread mBluetoothThread = null;
    boolean isBluetoothRunning = true;

    //SkyWay ID
    private String mSkyWayID = null;

    //Debug用
    private boolean isLedOn = false;

    private IServiceCallback mCallback = null;

    /**
     * Bluetooth Status
     */
    public enum BLUETOOTH_STATUS {
        CONNECT_START,
        CONNECTED,
        DISCONNECTED,
        COMMAND_READY,
        ERROR
    }

    /**
     * Callback
     */
    public interface IServiceCallback {
        //Bluetooth
        void updateBluetoothStatus(BLUETOOTH_STATUS status, BluetoothDevice device);

        void onSendBluetoothCommand(String command);

        void onReceiveBluetoothCommand(String command);
    }

    /**
     * Binder
     */
    public class DeviceConnectionServiceBinder extends Binder {

        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    public BluetoothService() {
        Log.d(LOG_TAG, "onBind");

        /*
         * Bluetooth初期設定
         */

        //Bluetooth Adapter取得
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Bluetooth Adapterが見つからない
        if (mBluetoothAdapter == null) {
            Log.d(LOG_TAG, "BluetoothAdapter is not found.");
        }
    }

    public void close() {
        closeBluetooth();
    }

    /**
     * コールバック登録
     *
     * @param callback
     */
    public void registerCallback(IServiceCallback callback) {
        mCallback = callback;
    }

    /**
     * コールバック登録解除
     */
    public void unregisterCallback() {
        mCallback = null;
    }

    /**
     * Bluetooth開始
     *
     * @param device
     */
    public void connectBluetooth(BluetoothDevice device) {
        mDevice = device;

        if (mDevice != null) {
            startBluetoothThread();
        }
    }

    /**
     * Bluetooth切断
     */
    public void disconnectBluetooth() {
        closeBluetooth();

        mDevice = null;
        mSocket = null;

        if (mCallback != null) {
            mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.DISCONNECTED, null);
        }
    }

    /**
     * Bluetooth接続スレッド開始
     */
    public void startBluetoothThread() {
        Log.d(LOG_TAG, "startBluetoothThread");

        if (mDevice != null) {
            isBluetoothRunning = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.CONNECT_START, mDevice);

                    if (connectBluetooth()) {
                        Log.d(LOG_TAG, "Bluetooth : Connected");

                        mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.CONNECTED, mDevice);

                        mBluetoothThread = new BluetoothCommandThread(mSocket);
                        mBluetoothThread.start();
                    } else {
                        Log.d(LOG_TAG, "Bluetooth : Cannot Connect");

                        mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.DISCONNECTED, mDevice);
                    }
                }
            }).start();
        }
    }

    ;

    /**
     * BluetoothDeviceに接続
     */
    @SuppressLint("MissingPermission")
    private boolean connectBluetooth() {
        try {
            mSocket = mDevice.createRfcommSocketToServiceRecord(UUID_SERIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mSocket == null) {
            return false;
        }

        try {
            mSocket.connect();

            return true;
        } catch (IOException e) {
            try {
                mSocket.close();
            } catch (IOException | NullPointerException closeException) {
                closeException.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Bluetooth切断
     */
    private void closeBluetooth() {
        isBluetoothRunning = false;
        mBluetoothThread = null;

        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * BluetoothDeviceにコマンド送信
     */
    public void sendCommandToBluetoothDevice(String command) {
        if (mBluetoothThread != null && command != null) {
            mBluetoothThread.write(command.getBytes());
        }
    }

    /**
     * Bluetoothコマンド送受信
     */
    private class BluetoothCommandThread extends Thread {

        InputStream inputStream;
        OutputStream outputStream;

        //コンストラクタの定義
        public BluetoothCommandThread(BluetoothSocket socket) {
            try {
                //接続済みソケットからI/Oストリームをそれぞれ取得
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.COMMAND_READY, null);
            } catch (IOException e1) {
                mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.ERROR, null);
                e1.printStackTrace();
            }
        }

        public void write(byte[] buf) {
            try {
                outputStream.write(buf);

                if (buf != null && buf.length > 0) {
                    mCallback.onSendBluetoothCommand(new String(buf, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.ERROR, null);
                e.printStackTrace();
            }
        }

        public void run() {
            byte[] buf = new byte[1024];
            String data;
            int tmpBuf = 0;

            while (isBluetoothRunning) {
                try {
                    tmpBuf = inputStream.read(buf);
                } catch (IOException e) {
                    mCallback.updateBluetoothStatus(BLUETOOTH_STATUS.ERROR, null);
                    e.printStackTrace();
                }
                if (tmpBuf != 0) {
                    data = new String(buf, StandardCharsets.UTF_8);

                    if (data.length() > 0) {
                        mCallback.onReceiveBluetoothCommand(data);
                    }
                }
            }
        }
    }
}
