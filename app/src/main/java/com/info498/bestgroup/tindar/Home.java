package com.info498.bestgroup.tindar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class Home extends Activity {
    TextView title;
    TextView tagline;
    TextView search;
    com.gc.materialdesign.views.ButtonRectangle findButton;
    com.gc.materialdesign.views.ButtonRectangle stopButton;
    com.gc.materialdesign.views.ProgressBarCircularIndeterminate spinner;
    AnimatorSet oa = new AnimatorSet();
    boolean animating = false;

    // bluetooth variables
    private static final String TAG = "HomeActivity";
    private static final String SERVICE_NAME = "Tindar";
    private static final UUID APP_UUID = UUID.fromString("092651a2-47ef-41b2-8ffd-e3ad2c08a9e6");
    private static final int REQUEST_ENABLE_DISCOVERABLE = 420;
    private BluetoothAdapter btAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> devices; // used to populate ListView in UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Typeface robotoFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
        Typeface fontAwesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        title = (TextView) findViewById(R.id.title);
        tagline = (TextView) findViewById(R.id.tagline);
        search = (TextView) findViewById(R.id.search);
        findButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.findButton);
        stopButton = (com.gc.materialdesign.views.ButtonRectangle) findViewById(R.id.stopButton);
        spinner = (com.gc.materialdesign.views.ProgressBarCircularIndeterminate) findViewById(R.id.spinner);

        title.setTypeface(robotoFont);
        tagline.setTypeface(robotoFont);
        search.setTypeface(fontAwesome);

        findButton.setRippleSpeed(80f);
        stopButton.setRippleSpeed(80f);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findButton.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                animating = true;
                animateSearch();
                startBluetoothService();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.VISIBLE);
                animating = false;
            }
        });
    }

    public void animateSearch(){
        final float x = search.getX();
        final float y = search.getY();

        oa.playSequentially(ObjectAnimator.ofFloat(search, "x", 50), // anim 1
                ObjectAnimator.ofFloat(search, "y", 550), // anim 2
                ObjectAnimator.ofFloat(search, "x", x), // anim 3
                ObjectAnimator.ofFloat(search, "y", y)); // anim 4
        oa.setDuration(425);

        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                if(animating) {
                    oa.start();
                }else{
                    findButton.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        oa.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startBluetoothService() {
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
                        Tindar tindar = (Tindar)getApplication();
                        tindar.makeThread(btSocket);
                        startActivity(new Intent(Home.this, Connected.class));

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
            Tindar tindar = (Tindar)getApplication();
            tindar.makeThread(btSocket);
            startActivity(new Intent(Home.this, Connected.class));
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
}
