package com.info498.bestgroup.tindar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ConnectActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static final String SERVICE_NAME = "Tindar";
    private static final UUID APP_UUID = UUID.fromString("092651a2-47ef-41b2-8ffd-e3ad2c08a9e6");
    private static final int REQUEST_ENABLE_DISCOVERABLE = 420;
    private BluetoothAdapter btAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> devices; // used to populate ListView in UI
    private ConnectedThread connectedThread; // thread for managing connection between two devices

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // testing
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectedThread.isAlive()) {
                    byte[] word = "hey it works".getBytes();
                    connectedThread.write(word);
                }
            }
        });

        // attach ArrayAdapter to ListView to show available device to connect with
        devices = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, devices);
        ListView deviceList = (ListView) findViewById(R.id.devices);
        deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get MAC address from item string in list view
                String[] deviceInfo = parent
                        .getItemAtPosition(position)
                        .toString().split("\\n");
                String macAddress = deviceInfo[1];

                // get remote bluetooth device using MAC address
                BluetoothDevice device = null;
                if (btAdapter.checkBluetoothAddress(macAddress)) {
                    try {
                        device = btAdapter.getRemoteDevice(macAddress);
                    } catch (IllegalArgumentException e) {
                        Log.e(TAG, "Error getting remote device");
                    }
                }
                if (device != null) {
                    ConnectThread connectThread = new ConnectThread(device);
                    connectThread.start();
                } else {
                    Log.e(TAG, "Error setting device. Could not start connection management thread.");
                }
            }
        });
        deviceList.setAdapter(arrayAdapter);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) { // bluetooth not supported
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
        } else {
            // pop up dialog to let user enable device discoverable mode
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(discoverableIntent, REQUEST_ENABLE_DISCOVERABLE);
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryReceiver, filter);
    }

    // adds a bluetooth device to ListView when one is found
    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceInfo = device.getName() + "\n" + device.getAddress();
                if (!devices.contains(deviceInfo)) {
                    devices.add(deviceInfo);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    // @SuppressWarnings
    Handler connectionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // testing
            TextView tv = (TextView) findViewById(R.id.bt_result);
            tv.setText(msg.getData().getString("message"));
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Bluetooth must be enabled to use this service",
                    Toast.LENGTH_SHORT).show();
        } else if (resultCode == 120 && requestCode == REQUEST_ENABLE_DISCOVERABLE) {
            Log.i(TAG, "Bluetooth discoverable mode enabled");
            if (btAdapter.isEnabled()) {
                Log.i(TAG, "bluetooth enabled, accepting connections");
                // start accepting connections from other devices
                AcceptConnectionThread acceptThread = new AcceptConnectionThread();
                acceptThread.start();

                // list previously paired devices
                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        devices.add(device.getName() + "\n" + device.getAddress());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }

                btAdapter.startDiscovery();
            }
        } else {
            Log.e(TAG, "Error enabling discoverable mode");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(discoveryReceiver);
        btAdapter.cancelDiscovery();
    }


    // thread for accepting a connection from another device
    private class AcceptConnectionThread extends Thread {

        private static final String ACCEPT_TAG = "AcceptConnectionThread";
        private final BluetoothServerSocket btServerSocket;

        public AcceptConnectionThread() {
            BluetoothServerSocket temp = null;

            try {
                temp = btAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, APP_UUID);
            } catch (IOException e) {
                Log.e(ACCEPT_TAG, "IOException when trying listenUsingRfcommWithServiceRecord()");
            }

            btServerSocket = temp;
        }

        @Override
        public void run() {
            BluetoothSocket btSocket;
            while (true) {
                try {
                    Log.i(ACCEPT_TAG, "accepting connections");
                    btSocket = btServerSocket.accept();
                    if (btSocket != null) {
                        Log.i(ACCEPT_TAG, "Bluetooth connection accepted");

                        // manage connection to socket
                        connectedThread = new ConnectedThread(btSocket);
                        connectedThread.start();

                        // close server socket (only want one connection)
                        btServerSocket.close();
                        break;
                    }
                } catch (IOException e) {
                    Log.e(ACCEPT_TAG, "Error: either Bluetooth connection failed or server " +
                            "socket couldn't be closed");
                    break;
                }
            }
        }

        // manually close the connection to the bluetooth socket
        public void cancel() {
            try {
                btServerSocket.close();
            } catch (IOException e) {
                Log.e(ACCEPT_TAG, "Could not close bluetooth server socket");
            }
        }
    }


    // thread for connecting to another device
    private class ConnectThread extends Thread {

        private static final String CONNECT_TAG = "ConnectThread";
        private final BluetoothSocket btSocket;
        private final BluetoothDevice btDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket temp = null;
            btDevice = device;

            try {
                temp = device.createRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e) {
                Log.e(CONNECT_TAG, "IOException when trying createRfcommSocketToServiceRecord");
            }

            btSocket = temp;
        }

        @Override
        public void run() {
            btAdapter.cancelDiscovery(); // cancel discovery once connection is attempted
            try {
                btSocket.connect();
            } catch (IOException connectException) {
                Log.e(CONNECT_TAG, "Error connecting to socket");
                connectException.printStackTrace();
                try {
                    btSocket.close();
                } catch (IOException closeException) {
                    Log.e(CONNECT_TAG, "Error closing socket connection");
                }
                return;
            }

            Log.i(CONNECT_TAG, "Connected to socket");

            // manage connection to socket
            connectedThread = new ConnectedThread(btSocket);
            connectedThread.start();
        }

        // manually close the connection to the bluetooth socket
        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e(CONNECT_TAG, "Could not close bluetooth socket");
            }
        }
    }


    // thread that manages writing output to and reading input from a bluetooth device
    public class ConnectedThread extends Thread {

        private static final String CONNECTED_TAG = "ConnectedThread";
        private static final int READ_MESSAGE = 42;
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