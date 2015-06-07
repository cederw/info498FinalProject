package com.info498.bestgroup.tindar;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by iguest on 6/3/15.
 */
public class Tindar extends Application {

    private static Tindar instance; // singleton
    public ConnectedThread connectedThread; // thread for managing connection between two devices


    public Tindar() {

        if (instance == null) {
            instance = this;
        } else {
            Log.e("Tindar", "There is an error beep boop. You tried to create more than 1 QuizDroid");
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void makeThread(BluetoothSocket btSocket) {
        connectedThread = new ConnectedThread(btSocket);
        connectedThread.start();
    }

    Handler connectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // testing
            //TextView tv = (TextView) findViewById(R.id.bt_result);
            //tv.setText(msg.getData().getString("message"));
            if(msg.getData().getString("message").equals("0")){
                //Intent intent = new Intent(instance, Flash.class);
                //sendBroadcast(intent);
                Toast.makeText(getApplicationContext(), "received!", Toast.LENGTH_SHORT).show();
            }
        }
    };


    // thread that manages writing output to and reading input from a bluetooth device
    public class ConnectedThread extends Thread {

        private static final String CONNECTED_TAG = "ConnectedThread";
        //private static final int READ_MESSAGE = 42;
        private final BluetoothSocket btSocket;
        private final InputStream btInputStream;
        private final OutputStream btOutputStream;

        public ConnectedThread(BluetoothSocket socket) {
            btSocket = socket;
            InputStream inputTemp = null;
            OutputStream outputTemp = null;

            try {
                inputTemp = socket.getInputStream();
                outputTemp = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(CONNECTED_TAG, "Error getting input/output stream from bluetooth socket");
            }

            btInputStream = inputTemp;
            btOutputStream = outputTemp;
        }

        @Override
        public void run() {
            Log.i(CONNECTED_TAG, "Running connection management thread");
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = btInputStream.read(buffer);

                    // set up message to send to handler
                    Message msg = connectionHandler.obtainMessage();
                    Bundle msgBundle = new Bundle();
                    msgBundle.putString("message", new String(buffer));
                    msg.setData(msgBundle);
                    msg.sendToTarget();
                } catch (IOException e) {
                    Log.e(CONNECTED_TAG, "Error reading from input stream");
                    break;
                }
            }
        }

        // used to send data to the remote device
        public void write(byte[] bytes) {
            try {
                Log.i(CONNECTED_TAG, "Attempting to write to socket output stream");
                btOutputStream.write(bytes);
            } catch (IOException e) {
                Log.e(CONNECTED_TAG, "Error writing with output stream");
            }
        }

        // manually close the connection to the bluetooth socket
        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e(CONNECTED_TAG, "Could not close bluetooth socket");
            }
        }
    }
}
