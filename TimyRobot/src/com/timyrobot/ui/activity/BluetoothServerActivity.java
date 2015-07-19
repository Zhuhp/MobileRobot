package com.timyrobot.ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.robot.R;
import com.timyrobot.utils.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import app.akexorcist.bluetotohspp.library.BluetoothService;

/**
 * 简单的一个蓝牙服务端
 * Created by zhuhp on 15/7/19.
 */
public class BluetoothServerActivity extends Activity{

    private static final int REQUEST_ENABLE_BT = 0x100;
    private static final int MESSAGE_READ = 0x200;
    private BluetoothAdapter mBlueAdapter;
    private TextView mTextView;
    private AcceptThread mThread;
    private ConnectedThread mConnectThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_server);
        mTextView = (TextView)findViewById(R.id.tv_receive_data);
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBlueAdapter == null){
            ToastUtils.toastShort(this,R.string.blue_not_avaliable);
            finish();
        }
        if(!mBlueAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }else{
            startAccepteThread();
        }
    }

    private void startAccepteThread(){
        mThread = new AcceptThread();
        mThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    startAccepteThread();
                }else{
                    ToastUtils.toastShort(this,R.string.blue_not_enable);
                    finish();
                }
                break;
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            // Use a temporary object that is later assigned to mmServerSocket,
            // because mmServerSocket is final
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBlueAdapter.listenUsingRfcommWithServiceRecord(mBlueAdapter.getName(), BluetoothService.UUID_ANDROID_DEVICE);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        if(mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        mConnectThread = new ConnectedThread(socket);
        mConnectThread.start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_READ:
                    byte[] buff = (byte[])msg.obj;
                    int size = msg.arg1;
                    int start = msg.arg2;

                    mTextView.setText(new String(buff));
                    break;
            }
        }
    };

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
